package org.rjj.ib.handler;

import com.ib.controller.BaseController;
import com.ib.controller.Position;
import org.rjj.model.Account;
import org.rjj.model.TimePosition;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Produces
public class AccountHandler implements BaseController.IAccountHandler {

    private Account account;

    @Inject
    public AccountHandler(Account account) {
        this.account = account;
    }

    @Override
    public synchronized void accountValue(String ibAccount, String tag, String value, String currency) {

        System.out.printf("Account - %s, tag - %s, value - %s, currency - %s \n",
                ibAccount, tag, value, currency);

        account.setField(tag, value);
    }

    @Override
    public synchronized void accountTime(String s) {

        System.out.printf("accountTime - %s \n", s);
        LocalTime localTime = LocalTime.parse(s);

        account.getLastDateSignal().set(Calendar.HOUR_OF_DAY, localTime.getHour());
        account.getLastDateSignal().set(Calendar.MINUTE, localTime.getMinute());
    }

    @Override
    public void accountDownloadEnd(String s) {
        System.out.printf("accountDownloadEnd - %s \n", s);
    }

    @Override
    public synchronized void updatePortfolio(Position position) {

        Map<Integer, List<TimePosition>> portfolio = account.getPortfolio();

        final TimePosition timePosition = new TimePosition(
                account.getLastDateSignal().getTime(), position);

        int contractId = position.contract().conid();
        if (portfolio.containsKey(contractId)) {
            portfolio.get(contractId).add(timePosition);
        } else {
            ArrayList<TimePosition> positions = new ArrayList<>();
            positions.add(timePosition);
            portfolio.put(contractId, positions);
        }

        System.out.printf("Position - %s \n", timePosition);
    }

}
