package org.rjj.ib;

import com.ib.controller.ApiController;
import com.ib.controller.Position;

public class AccountHandler implements ApiController.IAccountHandler {

    @Override
    public synchronized void accountValue(String account, String tag, String value, String currency) {

        System.out.printf("Account - %s, tag - %s, value - %s, currency - %s \n",
                account, tag, value, currency);
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
        System.out.printf("Position - %s \n", position);
    }
}
