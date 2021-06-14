package org.rjj.ib;

import org.rjj.model.Ticker;

import java.util.List;

public interface TraderInteractiveBrokerInterface {

    void buyStocks(List<Ticker> stockList, String currency) throws BrokerConnectionException;

    void buyStock(Ticker stock, String currency) throws BrokerConnectionException;

    void retrieveOrders() throws BrokerConnectionException;
}
