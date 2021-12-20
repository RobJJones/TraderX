package org.rjj.email;

import com.google.gson.Gson;
import com.sun.mail.imap.IMAPFolder;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.rjj.model.MovingAverageTicker;
import org.rjj.model.OversoldTicker;
import org.rjj.purchase.TradingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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
    @RestClient
    private TradingService tradingService;

    @Scheduled(every = "10s")
    void evaluateEmailAlert() {
        final long currentTimeMillisStart = System.currentTimeMillis();
        MovingAverageTicker ticker = extractAlertIndicatorsFromEmails(true);

        if (ticker!=null) tradingService.purchaseStock(ticker);
    }

    /**
     * Method reads emails from the IMAP or POP3 server.
     *
     * @param isImap - if true then we are reading messages from the IMAP server, if no then read from the POP3 server.
     */
    private MovingAverageTicker extractAlertIndicatorsFromEmails(boolean isImap) {

        long timeMillisStart = System.currentTimeMillis();

        MovingAverageTicker messageTicker = null;
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

//            ReceivedDateTerm today = new ReceivedDateTerm(ComparisonTerm.EQ,
//                    new GregorianCalendar(2021, Calendar.DECEMBER, 1).getTime());

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

 //               if (withinTradingHours(message)) {
                if (true) {
                    System.out.println("Title: " + message.getSubject());
//                    System.out.println();
//                    System.out.printf("Date: %tF %<tT %n", message.getReceivedDate());
//                    System.out.printf("Date: %tF %<tT %n", message.getSentDate());
                    System.out.println("ContentType: "+message.getContentType());

//                    System.out.println("---");

                    final Date sentDate = message.getSentDate();
                    messageTicker = getTickerFromMessageJSON(message);

                } else {
                    System.out.println(content + " outside trading hours ..." + message.getSentDate());
                }

            }

            //System.out.println("Time take - " + (System.currentTimeMillis() - timeMillisStart));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return messageTicker;
    }

    private MovingAverageTicker getTickerFromMessageJSON(Message message) throws IOException, MessagingException {

        Gson gson = new Gson();
        final MovingAverageTicker ticker = gson.fromJson(getTextFromMessage(message), MovingAverageTicker.class);

        System.out.println(ticker);
        return ticker;
    }

    private List<OversoldTicker> getMessageTickersFromCommaSeperated(Message message) throws IOException, MessagingException {
        return Arrays.stream(getTickersLine(message.getContent().toString())
                .split(", "))
                .filter(aString -> !aString.isBlank())
                .map(ticker -> {
                    try {
                        return new OversoldTicker(ticker, message.getSentDate(), extractFilter(message));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());
    }

    public OversoldTicker.AlertFilter extractFilter(Message message) throws MessagingException {
        return OversoldTicker.AlertFilter.lookupFilter(message.getSubject());
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

    private Function<String, OversoldTicker> createTicker(Message message) {
        return tickerString -> {
            try {
                return new OversoldTicker(tickerString, message.getSentDate(), extractFilter(message));
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

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
}
