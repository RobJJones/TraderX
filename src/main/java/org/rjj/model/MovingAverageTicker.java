package org.rjj.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import javax.validation.Valid;

@Generated("jsonschema2pojo")
public class MovingAverageTicker {

    @SerializedName("ticker")
    @Expose
    private String ticker;
    @SerializedName("closePrice")
    @Expose
    private double closePrice;
    @SerializedName("stopPercent")
    @Expose
    private double stopPercent;
    @SerializedName("takeProfitPercent")
    @Expose
    private double takeProfitPercent;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("openPrice")
    @Expose
    private double openPrice;
    @SerializedName("minTicks")
    @Expose
    private double minTicks;
    @SerializedName("strategy")
    @Expose
    @Valid
    private Strategy strategy;
    @SerializedName("algo")
    @Expose
    @Valid
    private String AlgString;

    /**
     * No args constructor for use in serialization
     *
     */
    public MovingAverageTicker() {
    }

    public MovingAverageTicker(String ticker, double closePrice, double stopPercent, double takeProfitPercent, String time, double openPrice, double minTicks) {
        this.ticker = ticker;
        this.closePrice = closePrice;
        this.stopPercent = stopPercent;
        this.takeProfitPercent = takeProfitPercent;
        this.time = time;
        this.openPrice = openPrice;
        this.minTicks = minTicks;
    }

    /**
     *
     * @param ticker
     * @param takeProfitPercent
     * @param stopPercent
     * @param openPrice
     * @param closePrice
     * @param time
     * @param strategy
     * @param minTicks
     */
    public MovingAverageTicker(String ticker, double closePrice, double stopPercent, double takeProfitPercent,
                               String time, double openPrice, Strategy strategy, double minTicks) {
        super();
        this.ticker = ticker;
        this.closePrice = closePrice;
        this.stopPercent = stopPercent;
        this.takeProfitPercent = takeProfitPercent;
        this.time = time;
        this.openPrice = openPrice;
        this.strategy = strategy;
        this.minTicks = minTicks;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getStopPercent() {
        return stopPercent;
    }

    public void setStopPercent(double stopPercent) {
        this.stopPercent = stopPercent;
    }

    public double getTakeProfitPercent() {
        return takeProfitPercent;
    }

    public void setTakeProfitPercent(double takeProfitPercent) {
        this.takeProfitPercent = takeProfitPercent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public double getMinTicks() {return minTicks;}

    public void setMinTicks(double minTicks) {this.minTicks = minTicks;}

    @Override
    protected Object clone() throws CloneNotSupportedException {

        MovingAverageTicker ticker = null;
        try {
            ticker = (MovingAverageTicker) super.clone();
        } catch (CloneNotSupportedException e) {
            ticker = new MovingAverageTicker(
                    this.getTicker(), this.getClosePrice(), this.getStopPercent(), this.getTakeProfitPercent(),
                    this.getTime(), this.getOpenPrice(), this.getMinTicks());
        }
        ticker.strategy = (Strategy) this.strategy.clone();
        return ticker;
    }

    @Override
    public String toString() {
        return "MovingAverageTicker{" +
                "ticker='" + ticker + '\'' +
                ", closePrice=" + closePrice +
                ", stopPercent=" + stopPercent +
                ", takeProfitPercent=" + takeProfitPercent +
                ", time='" + time + '\'' +
                ", openPrice=" + openPrice +
                ", minTicks=" + minTicks +
                ", strategy=" + strategy +
                '}';
    }
}