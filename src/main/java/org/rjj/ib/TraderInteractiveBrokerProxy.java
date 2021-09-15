package org.rjj.ib;

import com.ib.client.ContractDetails;
import com.ib.client.Order;
import com.ib.controller.BaseController2;
import org.rjj.ib.handler.AccountHandler;
import org.rjj.ib.handler.ConnectionHandler;
import org.rjj.ib.handler.OrderHandler;
import org.rjj.model.Account;
import org.rjj.model.Ticker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import java.util.List;

@ApplicationScoped
@Produces
public class TraderInteractiveBrokerProxy implements TraderInteractiveBrokerInterface {

    private static final int DEFAULT_CLIENT_NUMBER = 1;
    public static final int DEFAULT_CONNECTION_RETRY = 5;
    private final Account account;
    private final AccountHandler accountHandler;
    BaseController2 apiController;
    ConnectionHandler connectionHandler = new ConnectionHandler();
    OrderHandler orderHandler = new OrderHandler();

    @Inject
    public TraderInteractiveBrokerProxy(Account account, AccountHandler accountHandler) {

        //Create the connection hander, then the ApiController
        //Bit of a circular dependency going on here, but it's within IBs code
        apiController = new BaseController2(connectionHandler, null, null);
        connectionHandler.setController(apiController);
        this.account = account;
        this.accountHandler = accountHandler;
        requestAccountUpdates();
    }

    private boolean connect() {
        return this.connect(DEFAULT_CONNECTION_RETRY);
    }

    private boolean connect(int retryTimes) {
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

    private void disconnect() {
        apiController.disconnect();
    }

    private boolean attemptConnection(int retryTimes) {

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

    private void requestAccountUpdates() {
        apiController.reqAccountUpdates(true, account.getAccountCode(), accountHandler);
    }

    private ContractDetails getContractDetails(String symbol, String currency) {

        List<ContractDetails> contractDetails = Util.lookupContract(apiController,
                Util.getContract(symbol, currency));

        if (contractDetails.isEmpty()) {
            return null;
        }

        return contractDetails.get(0);
    }

    private void placeBuyOrder(Ticker ticker) {

        System.out.printf("Attempting to place buy order %s, %tz, %f",
                ticker.getSymbol(), ticker.getDateTime(), ticker.getLastPrice());

        //Set-up order with +/- 10%. Will alter after we get the fill value.
        List<Order> bracketOrder = Util.createBracketOrder(1, ticker.getLastPrice() * 1.1,
                ticker.getLastPrice() * 0.9);

        apiController.placeOrModifyOrder(Util.getContract(ticker.getSymbol(), ticker.getCurrency()), bracketOrder);

        System.out.printf("Attempting to place buy order %s, %tz, %f",
                ticker.getSymbol(), ticker.getDateTime(), ticker.getLastPrice());
    }

    @Override
    public void buyStocks(List<Ticker> stockList)
            throws BrokerConnectionException {

        System.out.println("List of stocks man!");

        for (Ticker ticker : stockList) {
            buyStock(ticker);
        };
    }

    @Override
    public void buyStock(Ticker ticker) throws BrokerConnectionException {
        System.out.println("List of stocks man!");

        if (connect()) {
            placeBuyOrder(ticker);
            System.out.println(ticker);
        } else {
            throw new BrokerConnectionException();
        }
    }

    @Override
    public void retrieveOrders() throws BrokerConnectionException {

        if (connect()) {
            apiController.reqLiveOrders(account.getLiveOrderHandler());
        } else {
            throw new BrokerConnectionException();
        }

    }
}
