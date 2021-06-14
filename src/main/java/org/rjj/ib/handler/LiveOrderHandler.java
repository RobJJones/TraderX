package org.rjj.ib.handler;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.BaseController;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;

@ApplicationScoped
@Produces
public class LiveOrderHandler implements BaseController.ILiveOrderHandler {

    @Override
    public void openOrder(Contract contract, Order order, OrderState orderState) {
        System.out.println("LiveOrderHandler openOrder contract - " + contract);
        System.out.println("LiveOrderHandler openOrder order - " + order);
        System.out.println("LiveOrderHandler openOrder orderState - " + orderState);
    }

    @Override
    public void openOrderEnd() {
        System.out.println("LiveOrderHandler openOrderEnd");
    }

    @Override
    public void orderStatus(int i, OrderStatus orderStatus, double v, double v1, double v2, int i1, int i2, double v3, int i3, String s, double v4) {

        System.out.printf("LiveOrderHandler - orderStatus: " +
                "(int " + i + ", OrderStatus " + orderStatus + ", double " + v + ", double " + v1 + ", double " + v2
                + ", int " + i1 + ", int " + i2 + ", double " + v3 + ", int " + i3 + ", String " + s + ", double " + v4);
    }

    @Override
    public void handle(int i, int i1, String s) {
        System.out.printf("LiveOrderHandler - handle: int " + i + ", int " + i1 + ", String " + s);
    }
}
