package org.rjj.email;

import com.sun.mail.imap.IMAPFolder;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.rjj.evaluate.TickerDataRetrieval;
import org.rjj.ib.TraderInteractiveBrokerProxy;
import org.rjj.ib.handler.ConnectionHandler;
import org.rjj.model.Ticker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.search.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.rjj.Utils.EXCHANGES;

@ApplicationScoped
public class EmailService {

    @ConfigProperty(name = "email.account.server.imap")
    private String imapServer;
    @ConfigProperty(name = "email.account.server.pop3")
    private String pop3Server;
    @ConfigProperty(name = "email.username")
    private String username;
    @ConfigProperty(name = "email.password")
    private String password;
    @ConfigProperty(name = "email.stocks.folder")
    private String emailStockFolder;
    @ConfigProperty(name = "trading.last.trading.time.uk")
    private LocalTime cutoffTimeUK;
    @ConfigProperty(name = "trading.last.trading.time.us")
    private LocalTime cutoffTimeAmerica;

    @Inject
    TraderInteractiveBrokerProxy interactiveBrokerInterface;

    @Inject
    TickerDataRetrieval tickerDataRetrieval;

    @Scheduled(every = "10s")
    void evaluateEmailAlert() {

        final long currentTimeMillisStart = System.currentTimeMillis();
        List<Ticker> tickerList = extractAlertIndicatorsFromEmails(true);

        tickerDataRetrieval.executeTickersDataExtract(tickerList);

        ConnectionHandler connectionHandler = new ConnectionHandler();
        //TraderApiController apiController = new TraderApiController(connectionHandler, null, null);
        //connectionHandler.setController(apiController);

        //String[] tickers = new String[]{"LSE:RR.", "NYSE:TV"};

        //tickerDataRetrieval.executeTickersDataExtract(tickers);

        //System.out.println((System.currentTimeMillis() - currentTimeMillisStart) / 1000);

        /*Ticker ticker = new Ticker();
        ticker.setCurrency("GDP");
        ticker.setSymbol("LLOY");
        tickerList.add(ticker);*/
        try {
            //apiController.checkConnection();
        } catch (Exception e) {    //BrokerConnectionException e) {

            //TODO - Do something ??? Send email with buy failure??
            e.printStackTrace();
        }
    }

    /**
     * Method reads emails from the IMAP or POP3 server.
     *
     * @param isImap - if true then we are reading messages from the IMAP server, if no then read from the POP3 server.
     */
    private List<Ticker> extractAlertIndicatorsFromEmails(boolean isImap) {

        long timeMillisStart = System.currentTimeMillis();

        ArrayList<Ticker> tickers = new ArrayList<>();
        // Create all the needed properties - empty!
        Properties connectionProperties = new Properties();
        // Create the session
        Session session = Session.getDefaultInstance(connectionProperties, null);

        try {
            //System.out.print("Connecting to the IMAP server...");
            //System.out.print("Searching from ");
            // Connecting to the server
            // Set the store depending on the parameter flag value
            String storeName = isImap ? "imaps" : "pop3";
            Store store = session.getStore(storeName);

            String server = isImap ? imapServer : pop3Server;
            store.connect(server, username, password);

            //System.out.println("done!");

//            ReceivedDateTerm today = new ReceivedDateTerm(ComparisonTerm.EQ,
//                    new GregorianCalendar(2021, Calendar.SEPTEMBER, 24).getTime());

            ReceivedDateTerm today = new ReceivedDateTerm(ComparisonTerm.EQ,
                    new Date());

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);


            SearchTerm searchTerm = new AndTerm(today, unseenFlagTerm);
            IMAPFolder inboxFolder = (IMAPFolder) store.getFolder(emailStockFolder);
            inboxFolder.open(Folder.READ_WRITE);


            // Get messages
            Message messages[] = inboxFolder.search(searchTerm);

            // Display the messages
            for (Message message : messages) {
                for (Address a : message.getFrom())
                    //System.out.println("From:" + a);

                message.setFlag(Flags.Flag.SEEN, true);
                final String content = message.getContent().toString();

                if (withinTradingHours(message)) {

//                    System.out.println("Title: " + message.getSubject());
//                    System.out.println();
//                    System.out.printf("Date: %tF %<tT %n", message.getReceivedDate());
//                    System.out.printf("Date: %tF %<tT %n", message.getSentDate());
//                    System.out.println(content);
//                    System.out.println("---");

                    final Date sentDate = message.getSentDate();
                    final List<Ticker> messageTickers = getMessageTickers(message);
                    tickers.addAll(messageTickers);

                } else {
                    System.out.println(content + " outside trading hours ..." + message.getSentDate());
                }

            }

            tickers.forEach(System.out::println);

            //System.out.println("Time take - " + (System.currentTimeMillis() - timeMillisStart));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickers;
    }

    private List<Ticker> getMessageTickers(Message message) throws IOException, MessagingException {
        return Arrays.stream(getTickersLine(message.getContent().toString())
                .split(", "))
                .filter(aString -> !aString.isBlank())
                .map(ticker -> {
                    try {
                        return new Ticker(ticker, message.getSentDate());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());
    }

    private String getTickersLine(String content) throws IOException, MessagingException {
        return EXCHANGES.stream().map(exchangeSymbol -> {
            return content.lines()
                    .filter(aLine -> aLine.contains(exchangeSymbol))
                    .findFirst()
                    .orElse(null);
        }).filter(aString -> aString != null)
                .findFirst()
                .orElse("");
    }

    private Function<String, Ticker> createTicker(Message message) {
        return tickerString -> {
            try {
                return new Ticker(tickerString, message.getSentDate());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    private boolean withinTradingHours(Message message) throws MessagingException {

        return ((message.getSubject().toLowerCase().contains("uk") &&
                getLocalTime(message.getSentDate()).isBefore(cutoffTimeUK)) ||
                (message.getSubject().toLowerCase().contains("america") &&
                        getLocalTime(message.getSentDate()).isBefore(cutoffTimeAmerica)));
    }

    private LocalTime getLocalTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }
}
