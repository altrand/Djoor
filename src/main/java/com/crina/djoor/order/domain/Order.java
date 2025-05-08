package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.domain.exceptions.snapshot.OrderSnapshot;
import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.order.domain.vo.CartItem;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import com.crina.djoor.shared.vo.Amount;
import com.crina.djoor.shared.vo.Id;

import java.util.*;

public class Order {
    private Id id;
    private String userId;
    private Cart cart;
    private Date createdAt;
    private Amount amount;
    private OrderState state;

    private Order(Id id, String userId, Cart cart, OrderState state, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.cart = cart;
        this.amount = computeTotalAmount(cart);
        this.state = state;
    }

    public static Order create(
            Id id,
            String userId,
            ProductSnapshot product,
            int quantity
    ) throws RuntimeException {


        Cart cart = Cart.create();
        cart = cart.addProduct(product, quantity);
        Order order = new Order(id, userId, cart, OrderState.INITIATED, new Date());

        return order;
    }

    public static Order reconstructFromDatabase(Id id, String userId, List<OrderItem> orderItems, OrderState state, Date createdAt) {
        Cart cart = Cart.create();

        for (OrderItem item : orderItems) {
            ProductSnapshot snapshot = new ProductSnapshot(
                    item.snapshot().productId(),
                    item.snapshot().price()
            );

            cart = cart.addProduct(snapshot, item.snapshot().quantity());
        }

        return new Order(id, userId, cart, state, createdAt);
    }

    public OrderSnapshot snapshot() {
        return new OrderSnapshot(
                this.id.value(),
                this.userId,
                this.amount.value(),
                this.state,
                this.createdAt,
                this.cart
        );
    }

    public void addProduct(ProductSnapshot product, int quantity) {
        this.cart = this.cart.addProduct(product, quantity);
        this.amount = computeTotalAmount(cart);
    }

    private Amount computeTotalAmount(Cart cart) {
        double total = cart.items().stream()
                .mapToDouble(item -> item.product().price() * item.quantity())
                .sum();
        return new Amount(total);
    }

    public void confirm() {
        if (this.state != OrderState.INITIATED) {
            throw new IllegalStateException("Order can't be confirmed in this state.");
        }
        this.state = OrderState.CONFIRMED;
    }

    public void pay() {
        if (this.state != OrderState.CONFIRMED) {
            throw new IllegalStateException("Order must be confirmed before being paid.");
        }
        this.state = OrderState.PAID;
    }

    public void cancel() {
        long now = new Date().getTime();
        long twoHoursInMillis = 2 * 60 * 60 * 1000;
        if (this.state == OrderState.PAID && (now - this.createdAt.getTime() > twoHoursInMillis)) {
            throw new IllegalStateException("The 2-hour cancellation deadline has passed.");
        }

        if (this.state == OrderState.DELIVERED && this.state == OrderState.CONFIRMED) {
            throw new IllegalStateException("Only orders not yet delivered can be cancelled.");
        }
        this.state = OrderState.CANCELLED;
    }

}
