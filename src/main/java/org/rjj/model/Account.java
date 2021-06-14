package org.rjj.model;

import com.ib.controller.BaseController;
import org.rjj.ib.handler.LiveOrderHandler;

import java.math.BigDecimal;
import java.util.*;

public abstract class Account {

    public BigDecimal totalCashValue;
    private BaseController.ILiveOrderHandler liveOrderHandler = new LiveOrderHandler();
    private Map<Integer, List<TimePosition>> portfolio = new HashMap<>();
    private Calendar lastDateSignal = new GregorianCalendar();

    public abstract String getAccountType();
    public abstract String getAccountCode();
    public abstract int getPortNumber();
    public abstract String getDomainName();
    public abstract String getTargetApplication();


    public BaseController.ILiveOrderHandler getLiveOrderHandler() {
        return liveOrderHandler;
    }

    public Calendar getLastDateSignal() {
        return lastDateSignal;
    }

    public Map<Integer, List<TimePosition>> getPortfolio() {
        return portfolio;
    }

    public void setField(String twsTag, String value) {

        switch (twsTag) {
            case "TotalCashValue":
                totalCashValue = new BigDecimal(value);
                break;
        }
    }

}
