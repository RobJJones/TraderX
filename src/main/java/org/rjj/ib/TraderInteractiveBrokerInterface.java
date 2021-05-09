package org.rjj.ib;

import com.ib.client.ContractDetails;

public interface TraderInteractiveBrokerInterface {

    boolean connect();

    void disconnect();

    boolean attemptConnection(int retryTimes);

    void updateAccount();

    ContractDetails getContractDetails(String symbol);
}
