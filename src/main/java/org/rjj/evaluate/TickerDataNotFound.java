package org.rjj.evaluate;

import org.rjj.model.Ticker;

public class TickerDataNotFound extends Exception {
    public TickerDataNotFound(Ticker ticker) {
        super(ticker.getSymbol());
    }
}
