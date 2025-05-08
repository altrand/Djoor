package com.crina.djoor.order.infrastructure.model;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
