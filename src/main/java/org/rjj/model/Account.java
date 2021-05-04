package org.rjj.model;

import com.ib.controller.ApiController;
import com.ib.controller.Position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Account {

    public BigDecimal totalCashValue;
    private ApiController.IAccountHandler accountHandler = new AccountHandler();
    private Map<Integer, List<Position>> portfolio = new HashMap<>();

    public abstract String getAccountType();
    public abstract String getAccountCode();
    public abstract int getPortNumber();
    public abstract String getDomainName();
    public abstract String getTargetApplication();

    public ApiController.IAccountHandler getAccountHandler() {
        return accountHandler;
    }

    public void setField(String twsTag, String value) {

        switch (twsTag) {
            case "TotalCashValue":
                totalCashValue = new BigDecimal(value);
                break;
        }
    }

    private class AccountHandler implements ApiController.IAccountHandler {

        @Override
        public synchronized void accountValue(String account, String tag, String value, String currency) {

            System.out.printf("Account - %s, tag - %s, value - %s, currency - %s \n",
                    account, tag, value, currency);

            setField(tag, value);
        }

        @Override
        public void accountTime(String s) {
            System.out.printf("accountTime - %s \n", s);
        }

        @Override
        public void accountDownloadEnd(String s) {
            System.out.printf("accountDownloadEnd - %s \n", s);
        }

        @Override
        public void updatePortfolio(Position position) {

            int contractId = position.contract().conid();
            if (portfolio.containsKey(contractId)) {
                portfolio.get(contractId).add(position);
            } else {
                ArrayList<Position> positions = new ArrayList<>();
                positions.add(position);
                portfolio.put(contractId, positions);
            }

            System.out.printf("Position - %s \n", position);
        }
    }
}
