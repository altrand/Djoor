package com.crina.djoor.order.application.command.validate;



public class ValidateOrderCommand {
    public String orderId;

    private ValidateOrderCommand() {}

    public ValidateOrderCommand(
            String orderId
    ) {
        this.orderId = orderId;

    }
}
