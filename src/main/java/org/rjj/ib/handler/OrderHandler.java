package org.rjj.ib.handler;

import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.BaseController;

public class OrderHandler implements BaseController.IOrderHandler {

    @Override
    public void orderState(OrderState orderState) {

        System.out.println(orderState.getStatus());
    }

    @Override
    public void orderStatus(OrderStatus status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {

        System.out.printf("OrderStatus %s, %f filled, %f remaining, %f avgFillPrice, %d permId, %d parentId, %f lastFillPrice, %d clientId, %s whyHeld, %f mktCapPrice",
                 status,  filled,  remaining,  avgFillPrice,  permId,  parentId,  lastFillPrice,  clientId,  whyHeld,  mktCapPrice);
    }

    @Override
    public void handle(int errorCode, final String errorMsg) {
        System.out.printf("Order error - %d %s %n", errorCode, errorMsg);
    }
}
