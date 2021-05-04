package org.rjj.ib;

public interface TraderInteractiveBrokerInterface {

    boolean connect();

    void disconnect();

    boolean attemptConnection(int retryTimes);

    void updateAccount();
}
