package org.rjj.ib;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Order;
import com.ib.controller.BaseController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Util {

    // YYMMDD + ORDER SEQUENCE 100/200/300 Parent orders 101, 102, 103 child orders of parent 100
    // 2106140000- First order of 14th June 2021
    private static int order_id = 0;

    public synchronized static int getNextOrderID() {

        if (order_id == 0) {
            Date today = new GregorianCalendar().getTime();
            String orderString = new SimpleDateFormat("yyMMdd0000").format(today);
            order_id = Integer.parseInt(orderString);
        }

        return order_id += 10;
    }

    public static int getCurrentOrderId() {
        if (order_id == 0) return getNextOrderID();
        return order_id;
    }

    public static List<Order> createBracketOrder(double quantity, double takeProfitLimitPrice, double stopLossPrice) {
        //This will be our main or "parent" §order
        int parentOrderId = getNextOrderID();
        Order parent = new Order();
        parent.orderId(parentOrderId);
        parent.action("BUY");
        parent.orderType("MKT");
        parent.totalQuantity(quantity);
        //The parent and children orders will need this attribute set to false to prevent accidental executions.
        //The LAST CHILD will have it set to true.
        parent.transmit(false);

        Order takeProfit = new Order();
        takeProfit.orderId(parent.orderId() + 1);
        takeProfit.action("SELL");
        takeProfit.orderType("LMT");
        takeProfit.totalQuantity(quantity);
        takeProfit.lmtPrice(takeProfitLimitPrice);
        takeProfit.parentId(parentOrderId);
        takeProfit.transmit(false);

        Order stopLoss = new Order();
        stopLoss.orderId(parent.orderId() + 2);
        stopLoss.action("SELL");
        stopLoss.orderType("STP");
        //Stop trigger price
        stopLoss.auxPrice(stopLossPrice);
        stopLoss.totalQuantity(quantity);
        stopLoss.parentId(parentOrderId);
        //In this case, the low side order will be the last child being sent. Therefore, it needs to set this attribute to true
        //to activate all its predecessors
        stopLoss.transmit(true);

        List<Order> bracketOrder = new ArrayList<>();
        bracketOrder.add(parent);
        bracketOrder.add(takeProfit);
        bracketOrder.add(stopLoss);

        return bracketOrder;
    }

    public static List<ContractDetails> lookupContract(BaseController controller, Contract contract) {
        if (controller == null) {
            return new ArrayList();
        } else {
            final CompletableFuture<List<ContractDetails>> future = new CompletableFuture();

            controller.reqContractDetails(contract, new BaseController.IContractDetailsHandler() {
                private final List<ContractDetails> contractDetails = new ArrayList();

                public void contractDetails(List<ContractDetails> list) {
                    this.contractDetails.addAll(list);
                    future.complete(this.contractDetails);
                }
            });

            try {
                return (List)future.get();
            } catch (InterruptedException var4) {
                var4.printStackTrace();
                Thread.currentThread().interrupt();
                return new ArrayList();
            } catch (ExecutionException var5) {
                var5.printStackTrace();
                return new ArrayList();
            }
        }
    }

    static Contract getContract(String symbol, String currency) {

        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType("STK");
        contract.currency(currency);
        contract.exchange("SMART");

        return contract;
    }
}
