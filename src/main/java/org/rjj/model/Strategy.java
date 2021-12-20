package org.rjj.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Strategy {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("movingAverageShortPeriod")
    @Expose
    private int movingAverageShortPeriod;
    @SerializedName("movingAverageLongPeriod")
    @Expose
    private int movingAverageLongPeriod;
    @SerializedName("movingAverageInput")
    @Expose
    private String movingAverageInput;
    @SerializedName("movingAverageSource")
    @Expose
    private String movingAverageSource;
    @SerializedName("averageBarsInTrade")
    @Expose
    private int averageBarsInTrade;
    @SerializedName("percentageProfitable")
    @Expose
    private double percentageProfitable;
    @SerializedName("averageTradePerDay")
    @Expose
    private double averageTradePerDay;
    @SerializedName("averageTrade")
    @Expose
    private double averageTrade;
    @SerializedName("profitFactor")
    @Expose
    private double profitFactor;
    @SerializedName("maximumDrawdown")
    @Expose
    private double maximumDrawdown;
    @SerializedName("totalClosedTrades")
    @Expose
    private int totalClosedTrades;

    /**
     * No args constructor for use in serialization
     *
     */
    public Strategy() {
    }

    public Strategy(String type, int movingAverageShortPeriod, int movingAverageLongPeriod, String movingAverageInput,
                    String movingAverageSource, int averageBarsInTrade, int averagePercentage, int averageTradePerDay, int averageTrade,
                    int profitFactor, int maximumDrawdown, int totalClosedTrades) {
        this.type = type;
        this.movingAverageShortPeriod = movingAverageShortPeriod;
        this.movingAverageLongPeriod = movingAverageLongPeriod;
        this.movingAverageInput = movingAverageInput;
        this.movingAverageSource = movingAverageSource;
        this.averageBarsInTrade = averageBarsInTrade;
        this.percentageProfitable = averagePercentage;
        this.averageTradePerDay = averageTradePerDay;
        this.averageTrade = averageTrade;
        this.profitFactor = profitFactor;
        this.maximumDrawdown = maximumDrawdown;
        this.totalClosedTrades = totalClosedTrades;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMovingAverageShortPeriod() {
        return movingAverageShortPeriod;
    }

    public void setMovingAverageShortPeriod(int movingAverageShortPeriod) {
        this.movingAverageShortPeriod = movingAverageShortPeriod;
    }

    public int getMovingAverageLongPeriod() {
        return movingAverageLongPeriod;
    }

    public void setMovingAverageLongPeriod(int movingAverageLongPeriod) {
        this.movingAverageLongPeriod = movingAverageLongPeriod;
    }

    public String getMovingAverageInput() {
        return movingAverageInput;
    }

    public void setMovingAverageInput(String movingAverageInput) {
        this.movingAverageInput = movingAverageInput;
    }

    public String getMovingAverageSource() {
        return movingAverageSource;
    }

    public void setMovingAverageSource(String movingAverageSource) {
        this.movingAverageSource = movingAverageSource;
    }

    public int getAverageBarsInTrade() {
        return averageBarsInTrade;
    }

    public void setAverageBarsInTrade(int averageBarsInTrade) {
        this.averageBarsInTrade = averageBarsInTrade;
    }

    public double getPercentageProfitable() {
        return percentageProfitable;
    }

    public void setPercentageProfitable(double percentageProfitable) {
        this.percentageProfitable = percentageProfitable;
    }

    public double getAverageTradePerDay() {
        return averageTradePerDay;
    }

    public void setAverageTradePerDay(double averageTradePerDay) {
        this.averageTradePerDay = averageTradePerDay;
    }

    public double getAverageTrade() {
        return averageTrade;
    }

    public void setAverageTrade(double averageTrade) {
        this.averageTrade = averageTrade;
    }

    public double getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(double profitFactor) {
        this.profitFactor = profitFactor;
    }

    public double getMaximumDrawdown() {
        return maximumDrawdown;
    }

    public void setMaximumDrawdown(double maximumDrawdown) {
        this.maximumDrawdown = maximumDrawdown;
    }

    public int getTotalClosedTrades() {
        return totalClosedTrades;
    }

    public void setTotalClosedTrades(int totalClosedTrades) {
        this.totalClosedTrades = totalClosedTrades;
    }

    @Override
    public String toString() {
        return "Strategy{" +
                "type='" + type + '\'' +
                ", movingAverageShortPeriod=" + movingAverageShortPeriod +
                ", movingAverageLongPeriod=" + movingAverageLongPeriod +
                ", movingAverageInput='" + movingAverageInput + '\'' +
                ", movingAverageSource='" + movingAverageSource + '\'' +
                ", averageBarsInTrade=" + averageBarsInTrade +
                ", percentageProfitable=" + percentageProfitable +
                ", averageTradePerDay=" + averageTradePerDay +
                ", averageTrade=" + averageTrade +
                ", profitFactor=" + profitFactor +
                ", maximumDrawdown=" + maximumDrawdown +
                ", totalClosedTrades=" + totalClosedTrades +
                '}';
    }
}
