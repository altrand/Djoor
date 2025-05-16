package com.crina.djoor.order.domain.events;

import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.shared.events.DomainEvent;
import java.time.Instant;

public class productQuantityDecreased implements DomainEvent {
    public String occurredOn;
    public Cart cart;
    public productQuantityDecreased(
            Cart cart
    ) {
        this.cart = cart;
        this.occurredOn = Instant.now().toString();
    }

    @Override
    public String occurredOn() {
        return this.occurredOn;
    }
}
