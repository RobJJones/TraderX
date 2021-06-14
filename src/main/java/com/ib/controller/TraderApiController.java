package com.ib.controller;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.Execution;

public class TraderApiController extends BaseController {

    public TraderApiController(IConnectionHandler handler) {
        super(handler);
    }

    public TraderApiController(IConnectionHandler handler, ApiConnection.ILogger inLogger, ApiConnection.ILogger outLogger) {
        super(handler, inLogger, outLogger);
    }

    @Override
    public boolean checkConnection() {
        return super.checkConnection();
    }


    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        System.out.println("ExecDetails. "+reqId+" - ["+contract.symbol()+"], ["+contract.secType()+"], ["+contract.currency()+"], ["+execution.execId()+
                "], ["+execution.orderId()+"], ["+execution.shares()+"]"  + ", [" + execution.lastLiquidity() + "]");
        super.execDetails(reqId, contract, execution);
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        System.out.println("CommissionReport. ["+commissionReport.execId()+"] - ["+commissionReport.commission()+"] ["+commissionReport.currency()+"] RPNL ["+commissionReport.realizedPNL()+"]");
        super.commissionReport(commissionReport);
    }
}
