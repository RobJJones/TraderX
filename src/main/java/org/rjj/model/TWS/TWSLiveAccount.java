package org.rjj.model.TWS;

import org.rjj.model.PaperAccount;

public class TWSLiveAccount extends PaperAccount {

    public static final String TARGET_APPLICATION = "TWS";
    private static final String ACCOUNT_CODE = "U5632107";
    private static final int PORT_NUMBER = 7496;
    public static final String DOMAIN_NAME = "localhost";

    @Override
    public String getAccountCode() {
        return ACCOUNT_CODE;
    }

    @Override
    public int getPortNumber() {
        return PORT_NUMBER;
    }

    @Override
    public String getDomainName() {
        return DOMAIN_NAME;
    }

    @Override
    public String getTargetApplication() {
        return TARGET_APPLICATION;
    }
}
