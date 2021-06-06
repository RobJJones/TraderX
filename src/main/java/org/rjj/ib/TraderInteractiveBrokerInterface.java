package org.rjj.ib;

import org.rjj.model.Ticker;

import java.util.List;

public interface TraderInteractiveBrokerInterface {

    String buyStocks(List<Ticker> stockList, String currency) throws BrokerConnectionException;
}
