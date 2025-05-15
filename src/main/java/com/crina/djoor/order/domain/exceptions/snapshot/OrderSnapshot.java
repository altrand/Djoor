package com.crina.djoor.order.domain.exceptions.snapshot;

import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.enums.PaymentMethod;
import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.order.domain.vo.CartSummary;
import com.crina.djoor.order.domain.vo.GiftOptions;

import java.util.Date;

public record OrderSnapshot(
        String id,
        String userId,
        double amount,
        com.crina.djoor.order.domain.enums.OrderState state,
        Date createdAt,
        CartSummary cartSummary,
        DeliveryMethod deliveryMethod,
        GiftOptions giftOptions,
        PaymentMethod paymentMethod
) {
}
