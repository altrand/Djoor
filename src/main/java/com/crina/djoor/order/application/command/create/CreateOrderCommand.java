package com.crina.djoor.order.application.command.create;

public class CreateOrderCommand {
    public String orderId;
    public OrderProductCommand orderProductCommand;
    public String userId;

    private CreateOrderCommand() {
    }

    public CreateOrderCommand(
            String orderId,
            String userId,
            OrderProductCommand orderProductCommand
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderProductCommand = orderProductCommand;
    }
}
