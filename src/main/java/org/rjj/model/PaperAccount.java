package org.rjj.model;

public abstract class PaperAccount extends Account {

    private final String ACCOUNT_TYPE = "Paper Account";

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }
}
