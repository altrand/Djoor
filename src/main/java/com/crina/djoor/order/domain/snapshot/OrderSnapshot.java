package com.crina.djoor.order.domain.snapshot;

import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.enums.OrderState;

import java.util.Date;

public record OrderSnapshot(
        String id,
        String userId,
        double amount,
        Date createdAt,
        OrderState state,
        DeliveryMethod deliveryMethod
) {

}
