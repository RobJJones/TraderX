package org.rjj.evaluate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TickerDetails {
    public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    public static final BigDecimal MINUS_HUNDRED = BigDecimal.valueOf(-100);
    public static DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

//time,open,high,low,close,Daily_Open,Daily_Close,Upper,Basis,Lower,VWMA,EMA,VWAP,Upper Band,Lower Band,Volume,Volume MA,ATR
//2021-09-10T10:07:00Z,11.528421775045922,11.92,11.528421775045922,11.92,11.89762501072942,11.945025000000001,11.75692120827853,11.556459067168896,11.355996926059262,11.519606705026153,11.660783060839673,11.50026167927494,11.603152971353152,11.397370387196728,70,6531.5,0.20708811155485968

    @CsvDate("yyyy-MM-dd'T'HH:mm:ssX")
    @CsvBindByName
    private Date time;

    @CsvCustomBindByName(converter = BigDecimalNaNConverter.class)
    private BigDecimal open;

    @CsvCustomBindByName(converter = BigDecimalNaNConverter.class)
    private BigDecimal high;

    @CsvCustomBindByName(converter = BigDecimalNaNConverter.class)
    private BigDecimal low;

    @CsvCustomBindByName(converter = BigDecimalNaNConverter.class)
    private BigDecimal close;

    @CsvCustomBindByName(column = "Daily_Open", converter = BigDecimalNaNConverter.class)
    private BigDecimal dailyOpen;

    @CsvCustomBindByName(column = "Daily_Close", converter = BigDecimalNaNConverter.class)
    private BigDecimal dailyClose;

    @CsvCustomBindByName(column = "Upper", converter = BigDecimalNaNConverter.class)
    private BigDecimal keltnerUpper;

    @CsvCustomBindByName(column = "Basis", converter = BigDecimalNaNConverter.class)
    private BigDecimal keltnerBasis;

    @CsvCustomBindByName(column = "Lower", converter = BigDecimalNaNConverter.class)
    private BigDecimal keltnerLower;

    @CsvCustomBindByName(column = "VWMA", converter = BigDecimalNaNConverter.class)
    private BigDecimal vwma;

    @CsvCustomBindByName(column = "EMA", converter = BigDecimalNaNConverter.class)
    private BigDecimal ema200;

    @CsvCustomBindByName(column = "VWAP", converter = BigDecimalNaNConverter.class)
    private BigDecimal vwap;

    @CsvCustomBindByName(column = "Upper Band", converter = BigDecimalNaNConverter.class)
    private BigDecimal vwapUpperBand;

    @CsvCustomBindByName(column = "Lower Band", converter = BigDecimalNaNConverter.class)
    private BigDecimal vwapLowerBand;

    @CsvCustomBindByName(column = "Volume", converter = BigDecimalNaNConverter.class)
    private BigDecimal volume;

    @CsvCustomBindByName(column = "Volume MA", converter = BigDecimalNaNConverter.class)
    private BigDecimal volumeMA;

    @CsvCustomBindByName(column = "Rating", converter = BigDecimalNaNConverter.class)
    private BigDecimal rating;

    @CsvCustomBindByName(column = "Histogram", converter = BigDecimalNaNConverter.class)
    private BigDecimal macdHistogram;

    @CsvCustomBindByName(column = "MACD", converter = BigDecimalNaNConverter.class)
    private BigDecimal macdLevel;

    @CsvCustomBindByName(column = "Signal", converter = BigDecimalNaNConverter.class)
    private BigDecimal macdSignal;

    @CsvCustomBindByName(column = "RSI", converter = BigDecimalNaNConverter.class)
    private BigDecimal rsi;

    @CsvCustomBindByName(column = "ATR", converter = BigDecimalNaNConverter.class)
    private BigDecimal averageTrueRange;

    @CsvCustomBindByName(column = "MF", converter = BigDecimalNaNConverter.class)
    private BigDecimal chaikinMF;

    @CsvCustomBindByName(column = "MOM", converter = BigDecimalNaNConverter.class)
    private BigDecimal momentum;

    @CsvCustomBindByName(column = "%K", converter = BigDecimalNaNConverter.class)
    private BigDecimal stockK;

    @CsvCustomBindByName(column = "%D", converter = BigDecimalNaNConverter.class)
    private BigDecimal stockD;

    @CsvCustomBindByName(column = "ADX", converter = BigDecimalNaNConverter.class)
    private BigDecimal averageDirIndex;

    @CsvCustomBindByName(column = "Plot", converter = BigDecimalNaNConverter.class)
    private BigDecimal awesomeOscillator;

    @CsvCustomBindByName(column = "CCI", converter = BigDecimalNaNConverter.class)
    private BigDecimal comChannelIndex;

    public Date getTime() {
        return time;
    }

    public String getFormattedTime() {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(time);
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getDailyOpen() {
        return dailyOpen;
    }

    public void setDailyOpen(BigDecimal dailyOpen) {
        this.dailyOpen = dailyOpen;
    }

    public BigDecimal getDailyClose() {
        return dailyClose;
    }

    public void setDailyClose(BigDecimal dailyClose) {
        this.dailyClose = dailyClose;
    }

    public BigDecimal getKeltnerUpper() {
        return keltnerUpper;
    }

    @BsonIgnore
    public BigDecimal getKeltnerUpperPercentage() {
        return keltnerUpper.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setKeltnerUpper(BigDecimal keltnerUpper) {
        this.keltnerUpper = keltnerUpper;
    }

    public BigDecimal getKeltnerBasis() {
        return keltnerBasis;
    }

    @BsonIgnore
    public BigDecimal getKeltnerBasisPercentage() {
        return keltnerBasis.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setKeltnerBasis(BigDecimal keltnerBasis) {
        this.keltnerBasis = keltnerBasis;
    }

    public BigDecimal getKeltnerLower() {
        return keltnerLower;
    }

    @BsonIgnore
    public BigDecimal getKeltnerLowerPercentage() {
        return keltnerLower.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setKeltnerLower(BigDecimal keltnerLower) {
        this.keltnerLower = keltnerLower;
    }

    public BigDecimal getVwma() {
        return vwma;
    }

    @BsonIgnore
    public BigDecimal getVwmaPercentage() {
        return vwma.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setVwma(BigDecimal vwma) {
        this.vwma = vwma;
    }

    public BigDecimal getEma200() {
        return ema200;
    }

    @BsonIgnore
    public BigDecimal getEma200Percentage() {
        return ema200.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setEma200(BigDecimal ema200) {
        this.ema200 = ema200;
    }

    public BigDecimal getVwap() {
        return vwap;
    }

    @BsonIgnore
    public BigDecimal getVwapPercentage() {
        return vwap.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setVwap(BigDecimal vwap) {
        this.vwap = vwap;
    }

    public BigDecimal getVwapUpperBand() {
        return vwapUpperBand;
    }

    @BsonIgnore
    public BigDecimal getVwapUpperBandPercentage() {
        return vwapUpperBand.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setVwapUpperBand(BigDecimal vwapUpperBand) {
        this.vwapUpperBand = vwapUpperBand;
    }

    public BigDecimal getVwapLowerBand() {
        return vwapLowerBand;
    }

    @BsonIgnore
    public BigDecimal getVwapLowerBandPercentage() {
        return vwapLowerBand.divide(getClose(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    public void setVwapLowerBand(BigDecimal vwapLowerBand) {
        this.vwapLowerBand = vwapLowerBand;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getVolumeMA() {
        return volumeMA;
    }

    public void setVolumeMA(BigDecimal volumeMA) {
        this.volumeMA = volumeMA;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public BigDecimal getMacdHistogram() {
        return macdHistogram;
    }

    public void setMacdHistogram(BigDecimal macdHistogram) {
        this.macdHistogram = macdHistogram;
    }

    public BigDecimal getMacdLevel() {
        return macdLevel;
    }

    public void setMacdLevel(BigDecimal macdLevel) {
        this.macdLevel = macdLevel;
    }

    public BigDecimal getMacdSignal() {
        return macdSignal;
    }

    public void setMacdSignal(BigDecimal macdSignal) {
        this.macdSignal = macdSignal;
    }

    public BigDecimal getRsi() {
        return rsi;
    }

    public void setRsi(BigDecimal rsi) {
        this.rsi = rsi;
    }

    public BigDecimal getAverageTrueRange() {
        return averageTrueRange;
    }

    public void setAverageTrueRange(BigDecimal averageTrueRange) {
        this.averageTrueRange = averageTrueRange;
    }

    public BigDecimal getChaikinMF() {
        return chaikinMF;
    }

    public void setChaikinMF(BigDecimal chaikinMF) {
        this.chaikinMF = chaikinMF;
    }

    public BigDecimal getMomentum() {
        return momentum;
    }

    public void setMomentum(BigDecimal momentum) {
        this.momentum = momentum;
    }

    public BigDecimal getStockK() {
        return stockK;
    }

    public void setStockK(BigDecimal stockK) {
        this.stockK = stockK;
    }

    public BigDecimal getStockD() {
        return stockD;
    }

    public void setStockD(BigDecimal stockD) {
        this.stockD = stockD;
    }

    public BigDecimal getAverageDirIndex() {
        return averageDirIndex;
    }

    public void setAverageDirIndex(BigDecimal averageDirIndex) {
        this.averageDirIndex = averageDirIndex;
    }

    public BigDecimal getAwesomeOscillator() {
        return awesomeOscillator;
    }

    public void setAwesomeOscillator(BigDecimal awesomeOscillator) {
        this.awesomeOscillator = awesomeOscillator;
    }

    public BigDecimal getComChannelIndex() {
        return comChannelIndex;
    }

    public void setComChannelIndex(BigDecimal comChannelIndex) {
        this.comChannelIndex = comChannelIndex;
    }

    public BigDecimal getPercentageChangeOpen() {
        return close.divide(getDailyOpen(),8, RoundingMode.HALF_UP)
                .multiply(HUNDRED)
                .add(MINUS_HUNDRED);
    }

    @Override
    public String toString() {
        return "TickerDetails{" +
                "time=" + time +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", dailyOpen=" + dailyOpen +
                ", dailyClose=" + dailyClose +
                ", keltnerUpper=" + keltnerUpper +
                ", keltnerBasis=" + keltnerBasis +
                ", keltnerLower=" + keltnerLower +
                ", vwma=" + vwma +
                ", ema200=" + ema200 +
                ", vwap=" + vwap +
                ", vwapUpperBand=" + vwapUpperBand +
                ", vwapLowerBand=" + vwapLowerBand +
                ", volume=" + volume +
                ", volumeMA=" + volumeMA +
                ", rating=" + rating +
                ", macdHistogram=" + macdHistogram +
                ", macdLevel=" + macdLevel +
                ", macdSignal=" + macdSignal +
                ", rsi=" + rsi +
                ", averageTrueRange=" + averageTrueRange +
                ", chaikinMF=" + chaikinMF +
                ", momentum=" + momentum +
                ", stockK=" + stockK +
                ", stockD=" + stockD +
                ", averageDirIndex=" + averageDirIndex +
                ", awesomeOscillator=" + awesomeOscillator +
                ", comChannelIndex=" + comChannelIndex +
                '}';
    }

    public String toDecisionEngineString() {
        return "TickerDetails{" +
                "averageTrueRange=" + averageTrueRange +
                ", time=" + getFormattedTime() +
                ", ema200=" + getEma200Percentage() +
                ", keltnerUpper=" + getKeltnerUpperPercentage() +
                ", percentageChangeOpen=" + getPercentageChangeOpen() +
                ", volumeMA=" + volumeMA +
                ", vwap=" + getVwapPercentage() +
                ", vwapUpperBand=" + getVwapUpperBandPercentage() +
                ", vwapLowerBand=" + getVwapLowerBandPercentage() +
                ", vwma=" + getVwmaPercentage() +
                '}';
    }

}
