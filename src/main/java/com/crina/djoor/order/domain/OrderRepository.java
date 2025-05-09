package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void add(Order order) throws ErrorOnSaveOrderException;

    Order findById(String commandId);

    Order ofId(String orderId);

    void update(Order order);

    Optional<Order> findInitiatedByUserId(String userId);

    List<OrderItem> findOrderItemsByOrderId(String orderId);

    void addOrUpdate(Order order);

    List<Order> findAllInitiatedByUserId(String userId);
}
