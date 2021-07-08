package org.rjj;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailService {
/*
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

    //@Inject
    //TraderInteractiveBrokerProxy interactiveBrokerInterface;

    /*@Scheduled(every="10s")
    void purchaseStocksFromEmail() {
        //List<Ticker> tickerList = this.extractStockOrdersFromEmail(true);

        ConnectionHandler connectionHandler = new ConnectionHandler();
        TraderApiController apiController = new TraderApiController(connectionHandler, null, null);
        connectionHandler.setController(apiController);

        ArrayList<Ticker> tickerList = new ArrayList<>();
        Ticker ticker = new Ticker();
        ticker.setCurrency("GDP");
        ticker.setSymbol("LLOY");
        tickerList.add(ticker);
        try {
            apiController.checkConnection();
        } catch (Exception e) {    //BrokerConnectionException e) {

            //TODO - Do something ??? Send email with buy failure??
            e.printStackTrace();
        }
    }

    /**
     * Method reads emails from the IMAP or POP3 server.
     *
     * @param isImap - if true then we are reading messages from the IMAP server, if no then read from the POP3 server.
     *
    private List<Ticker> extractStockOrdersFromEmail(boolean isImap) {

        long timeMillisStart = System.currentTimeMillis();

        // Create all the needed properties - empty!
        Properties connectionProperties = new Properties();
        // Create the session
        Session session = Session.getDefaultInstance(connectionProperties, null);

        try {
            System.out.print("Connecting to the IMAP server...");
            System.out.print("Searching from ");
            // Connecting to the server
            // Set the store depending on the parameter flag value
            String storeName = isImap ? "imaps" : "pop3";
            Store store = session.getStore(storeName);

            String server = isImap ? imapServer : pop3Server;
            store.connect(server, username, password);

            System.out.println("done!");

            ReceivedDateTerm today = new ReceivedDateTerm(ComparisonTerm.EQ, new Date());

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);


            SearchTerm searchTerm = new AndTerm(today, unseenFlagTerm);
            IMAPFolder inboxFolder = (IMAPFolder) store.getFolder(emailStockFolder);
            inboxFolder.open(Folder.READ_WRITE);


            // Get messages
            Message messages[] = inboxFolder.search(searchTerm);
            System.out.println("Reading messages...");

            // Display the messages
            for (Message message : messages) {
                for (Address a : message.getFrom())
                    System.out.println("From:" + a);

                message.setFlag(Flags.Flag.SEEN, true);

                System.out.println("Title: " + message.getSubject());
                System.out.println();
                System.out.printf("Date: %tF %<tT %n", message.getReceivedDate());
                System.out.printf("Date: %tF %<tT %n", message.getSentDate());
                System.out.println(message.getContent());
                System.out.println("---");
            }

            System.out.println("Time take - " + (System.currentTimeMillis()-timeMillisStart));

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Ticker> tickers = new ArrayList<>();
        Ticker ticker = new Ticker();
        ticker.setCurrency("GDP");
        ticker.setSymbol("LLOY");
        tickers.add(ticker);
        return tickers;
    }*/
}
