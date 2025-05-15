package com.crina.djoor.order.infrastructure.model;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.enums.OrderState;

import com.crina.djoor.order.domain.exceptions.snapshot.OrderSnapshot;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "order_user_index", columnList = "userId")
        })
public class OrderEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String userId;

    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    public static OrderEntity createFromDomain(Order order) {
        OrderSnapshot snapshot = order.snapshot();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.amount = snapshot.amount();
        orderEntity.id = snapshot.id();
        orderEntity.userId = snapshot.userId();
        orderEntity.state = snapshot.state();
        orderEntity.createdAt = snapshot.createdAt();

        // Créer les OrderItemEntity à partir du panier
        orderEntity.items = order.items().stream()
                .map(cartItem -> {
                    OrderItemEntity itemEntity = OrderItemEntity.create(cartItem.product().id(), cartItem.product().price(), cartItem.quantity(), orderEntity);
                    return itemEntity;
                })
                .toList();

        return orderEntity;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Double.compare(amount, that.amount) == 0;
    }
}
