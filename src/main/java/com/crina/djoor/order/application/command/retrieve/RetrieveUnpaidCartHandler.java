package com.crina.djoor.order.application.command.retrieve;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.cqrs.TransactionalException;

import java.util.Optional;

public class RetrieveUnpaidCartHandler implements CommandHandler<String, Optional<Order>> {
    private final OrderRepository orderRepository;

    public RetrieveUnpaidCartHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> handle(String userId) throws TransactionalException {
        return orderRepository.findInitiatedByUserId(userId);
    }
}
