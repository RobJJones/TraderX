package org.rjj.ib;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Order;
import com.ib.client.Util;
import com.ib.controller.TraderApiController;
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
    TraderApiController apiController;
    ConnectionHandler connectionHandler = new ConnectionHandler();
    OrderHandler orderHandler = new OrderHandler();

    @Inject
    public TraderInteractiveBrokerProxy(Account account) {

        //Create the connection hander, then the ApiController
        //Bit of a circular dependency going on here, but it's within IBs code
        apiController = new TraderApiController(connectionHandler, null, null);
        connectionHandler.setController(apiController);
        this.account = account;
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

    private void updateAccount() {
        apiController.reqAccountUpdates(true, account.getAccountCode(), account.getAccountHandler());
    }

    private ContractDetails getContractDetails(String symbol, String currency) {

        List<ContractDetails> contractDetails = Util.lookupContract(apiController,
                getContract(symbol, currency));

        if (contractDetails.isEmpty()) {
            return null;
        }

        return contractDetails.get(0);
    }

    private void placeBuyOrder(Ticker ticker, String currency) {

        System.out.printf("Attempting to place buy order %s, %tz, %f",
                ticker.getSymbol(), ticker.getDateTime(), ticker.getLastPrice());

        Order order = new Order();
        order.action("BUY");
        order.orderType("MKT");
        order.totalQuantity(1);

        apiController.placeOrModifyOrder(getContract(ticker.getSymbol(), currency),
                order, orderHandler);

        System.out.printf("Attempting to place buy order %s, %tz, %f",
                ticker.getSymbol(), ticker.getDateTime(), ticker.getLastPrice());
    }

    private static Contract getContract(String symbol, String currency) {

        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType("STK");
        contract.currency(currency);
        contract.exchange("SMART");

        return contract;
    }

    @Override
    public String buyStocks(List<Ticker> stockList, String currency)
            throws BrokerConnectionException {

        System.out.println("List of stocks man!");

        if (connect()) {

            updateAccount();
            stockList.forEach(ticker -> placeBuyOrder(ticker, currency));

            //getContractDetails(stockList.get(0));
            stockList.forEach(System.out::println);

        } else {

            throw new BrokerConnectionException();
        }

        //this.interactiveBrokerInterface.disconnect();

        /*return Response.status(200)
                .entity("Posted - "+stockList.toString())
                .build();*/

        return "Oh yeah";
    }
}
