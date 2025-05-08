package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.exceptions.snapshot.OrderSnapshot;
import com.crina.djoor.order.domain.snapshot.OrderItemSnapshot;
import com.crina.djoor.shared.vo.Id;

public class OrderItem {
    public Id id;
    public String productId;
    public int quantity;
    public double price;

    private OrderItem(String productId, int quantity, double price) {
        this.id = new Id(productId);
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem create(String productId, int quantity, double price) {
        OrderItem orderItem = new OrderItem(productId, quantity, price);
        return orderItem;
    }

    public OrderItemSnapshot snapshot() {
        return new OrderItemSnapshot(
                this.id.value(),
                this.productId,
                this.quantity,
                this.price
        );
    }
}
