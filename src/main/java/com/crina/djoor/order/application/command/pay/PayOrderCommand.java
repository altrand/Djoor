package com.crina.djoor.order.application.command.pay;

public class PayOrderCommand {
    public String orderId;

    public PayOrderCommand(String orderId) {

        this.orderId = orderId;
    }
}
