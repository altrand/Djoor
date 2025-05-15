package com.crina.djoor.order.infrastructure.model;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(
        name = "order_items",
        indexes = {
                @Index(name = "order_items_order_index", columnList = "order_id"),
                @Index(name = "order_items_product_index", columnList = "productId"),
        })
public class OrderItemEntity {
    @Id
    public String id;
    @Column(nullable = false)
    public String productId;
    @Column(nullable = false)
    public int quantity;
    @Column(nullable = false)
    public double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public OrderItemEntity() {
    }

    private OrderItemEntity(
            String productId,
            double price,
            int quantity,
            OrderEntity order
    ) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
        this.id = UUID.randomUUID().toString();
    }

    public static OrderItemEntity create(
            String productId,
            double price,
            int quantity,
            OrderEntity order
    ) {

        OrderItemEntity itemEntity = new OrderItemEntity(productId, price, quantity, order);

        return itemEntity;
    }

}
