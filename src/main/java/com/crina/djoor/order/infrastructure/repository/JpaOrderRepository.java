package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.order.infrastructure.model.OrderEntity;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaOrderRepository implements OrderRepository {
    private final SpringDataOrderRepository orderRepository;

    public JpaOrderRepository(SpringDataOrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    @Override
    public void add(Order order) throws ErrorOnSaveOrderException {
        try {
            OrderEntity entity = OrderEntity.createFromDomain(order);
            orderRepository.save(entity);
        } catch (
                IllegalArgumentException |
                ConstraintViolationException |
                DataIntegrityViolationException |
                OptimisticLockingFailureException e
        ) {
            throw new ErrorOnSaveOrderException(e.getMessage());
        }
    }

    @Override
    public Order findById(String commandId) {
        return null;
    }

    @Override
    public Order ofId(String orderId) {
        return null;
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public Optional<Order> findInitiatedByUserId(String userId) {
        return null;
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(String orderId) {
        return List.of();
    }

    @Override
    public void addOrUpdate(Order order) {

    }

    @Override
    public List<Order> findAllInitiatedByUserId(String userId) {
        return List.of();
    }
}
