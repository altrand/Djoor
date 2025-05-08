package com.crina.djoor.order.application.command.cancel;

public class CancelOrderCommand {
    public String orderId;

    public CancelOrderCommand(String orderId) {

        this.orderId = orderId;
    }
}
