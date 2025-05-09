package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.domain.vo.CartItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryOrderRepository implements OrderRepository {
    public HashMap<String, Order> orders = new HashMap<>();

    @Override
    public void add(Order order) {
        orders.put(order.snapshot().id(), order);
        for (CartItem item : order.snapshot().cart().items()) {
            OrderItem orderItem = OrderItem.create(item.product().id(), item.quantity(), item.product().price());
        }
    }


    @Override
    public Order findById(String commandId) {
        return null;
    }

    @Override
    public Order ofId(String orderId) {
        return orders.get(orderId);
    }

    @Override
    public void update(Order order) {
        add(order);
    }

    @Override
    public Optional<Order> findInitiatedByUserId(String userId) {
        return orders.values().stream()
                .filter(order -> order.snapshot().state() == OrderState.INITIATED)
                .filter(order -> userId.equals(order.snapshot().userId()))
                .findFirst();
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return Collections.emptyList();
        }
        return order.snapshot().cart()
                .items()
                .stream()
                .map(item -> OrderItem.create(
                        item.product().id(),
                        item.quantity(),
                        item.product().price()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void addOrUpdate(Order order) {
        orders.put(order.snapshot().id(), order);
        for (CartItem item : order.snapshot().cart().items()) {
            OrderItem orderItem = OrderItem.create(item.product().id(), item.quantity(), item.product().price());
        }
    }

    @Override
    public List<Order> findAllInitiatedByUserId(String userId) {
        return orders.values().stream()
                .filter(order -> order.snapshot().state() == OrderState.INITIATED
                        && order.snapshot().userId().equals(userId))
                .collect(Collectors.toList());
    }
}
