package com.crina.djoor.order.domain.exceptions.snapshot;

import com.crina.djoor.order.domain.vo.Cart;

import java.util.Date;

public record OrderSnapshot(
        String id,
        String userId,
        double amount,
        com.crina.djoor.order.domain.enums.OrderState state,
        Date createdAt,
        Cart cart
) {
}
