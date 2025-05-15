package com.crina.djoor.order.application.command.pay;

import com.crina.djoor.order.domain.enums.PaymentMethod;

public class PayOrderCommand {
    public final String userId;
    public String orderId;
    public PaymentMethod paymentMethod;

    public PayOrderCommand(String orderId, String userId, PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.orderId = orderId;
        this.userId = userId;
    }
}
