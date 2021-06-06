package org.rjj.model;

import java.util.Date;

public class Ticker {

    private String symbol;
    private Date dateTime = new Date();
    private double lastPrice;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }


    @Override
    public String toString() {
        return "Ticker{" +
                "symbol='" + symbol + '\'' +
                ", dateTime=" + dateTime +
                ", lastPrice=" + lastPrice +
                '}';
    }
}
