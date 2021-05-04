package org.rjj.model.TWS;

import org.rjj.model.PaperAccount;

public class TWSPaperAccount extends PaperAccount {

    public static final String TARGET_APPLICATION = "TWS";
    public static final String DOMAIN_NAME = "localhost";
    private final String ACCOUNT_CODE = "DU3584181";
    private final int PORT_NUMBER = 7497;

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
