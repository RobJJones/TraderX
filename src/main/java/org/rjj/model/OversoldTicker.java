package org.rjj.model;

import java.util.Date;

public class OversoldTicker {

    private String symbol;
    private Date dateTime = new Date();
    private double lastPrice;
    private AlertFilter filter;

    public static enum AlertFilter{

        OVERSOLD_UK("Oversold Honez uk", "GBP"),
        OVERSOLD_AMERICA("Oversold Honez america", "USD");

        private String filterText;
        private String currency;

        AlertFilter(String filterText, String currency) {
            this.filterText = filterText;
            this.currency = currency;
        }

        public String getFilterText() {
            return filterText;
        }

        public String getCurrency() {
            return currency;
        }

        public static AlertFilter lookupFilter(String messsage) {
            if (messsage.contains(OVERSOLD_AMERICA.filterText)) {
                return OVERSOLD_AMERICA;
            } else if (messsage.contains(OVERSOLD_UK.filterText)) {
                return OVERSOLD_UK;
            } else {
                return null;
            }
        }
    }

    public OversoldTicker() {
        super();
    }

    public OversoldTicker(String symbol, Date dateTime, AlertFilter filter) {
        this();
        this.symbol = symbol;
        this.dateTime = dateTime;
        this.filter = filter;
    }

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

    public AlertFilter getFilter() {
        return filter;
    }

    public void setFilter(AlertFilter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "symbol='" + symbol + '\'' +
                ", dateTime=" + dateTime +
                ", lastPrice=" + lastPrice +
                ", filter=" + filter +
                '}';
    }

}
