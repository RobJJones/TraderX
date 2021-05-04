package org.rjj.ib;

import com.ib.controller.TraderApiController;
import org.rjj.model.Account;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;

@ApplicationScoped
@Produces
public class TraderInteractiveBrokerProxy implements TraderInteractiveBrokerInterface {

    private static final int DEFAULT_CLIENT_NUMBER = 1;
    public static final int DEFAULT_CONNECTION_RETRY = 5;
    private final Account account;
    TraderApiController apiController;
    ConnectionHandler connectionHandler = new ConnectionHandler();

    @Inject
    public TraderInteractiveBrokerProxy(Account account) {

        //Create the connection hander, then the ApiController
        //Bit of a circular dependency going on here, but it's within IBs code
        apiController = new TraderApiController(connectionHandler, null, null);
        connectionHandler.setController(apiController);
        this.account = account;
    }

    public boolean connect() {
        return this.connect(DEFAULT_CONNECTION_RETRY);
    }

    public boolean connect(int retryTimes) {
        apiController.connect(account.getDomainName(), account.getPortNumber(), DEFAULT_CLIENT_NUMBER, null);

        for (int i=1; i < retryTimes+1 && !apiController.checkConnection(); i++) {

            System.out.printf("Connection attempt %s failed!\n", i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return attemptConnection(5);
    }

    @Override
    public void disconnect() {
        apiController.disconnect();
    }

    @Override
    public boolean attemptConnection(int retryTimes) {

        for (int i=1; i < retryTimes+1 && !apiController.checkConnection(); i++) {

            System.out.printf("Connection attempt %s failed!\n", i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return apiController.checkConnection();
    }

    @Override
    public void updateAccount() {
        apiController.reqAccountUpdates(true, account.getAccountCode(), account.getAccountHandler());
    }
}
