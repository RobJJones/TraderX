package org.rjj.ib;

import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController;

public class OrderHandler implements ApiController.IOrderHandler {

    @Override
    public void orderState(OrderState orderState) {

        System.out.println(orderState.getStatus());
    }

    @Override
    public void orderStatus(OrderStatus status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {

        System.out.printf("OrderStatus %s, %d filled, %d remaining, %d avgFillPrice, %d permId, %d parentId, %d lastFillPrice, %d clientId, %s whyHeld, %d mktCapPrice",
                 status,  filled,  remaining,  avgFillPrice,  permId,  parentId,  lastFillPrice,  clientId,  whyHeld,  mktCapPrice);
    }

    @Override
    public void handle(int errorCode, final String errorMsg) {
        System.out.printf("Order error - %i %d %n", errorCode, errorMsg);
    }
}
