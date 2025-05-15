package com.crina.djoor.order.infrastructure.repository;


import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.OrderItemRepository;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.order.domain.vo.TopSellingProduct;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JpaOrderItemRepository implements OrderItemRepository {
    private final SpringDataOrderItemRepository orderItemRepository;

    public JpaOrderItemRepository(SpringDataOrderRepository orderRepository, SpringDataOrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;

    }

    @Override
    public void add(OrderItem orderItem) throws ErrorOnSaveOrderException {
        /*try {
            orderItemRepository.save(OrderItemEntity.createFromDomain(order));
        } catch (
                IllegalArgumentException |
                ConstraintViolationException |
                DataIntegrityViolationException |
                OptimisticLockingFailureException e
        ) {
            throw new ErrorOnSaveOrderException(e.getMessage());
        }*/
    }

    @Override
    public void addOrder(Order order) throws ErrorOnSaveOrderException {

    }

    @Override
    public List<TopSellingProduct> findSoldProductsBetween(Date from, Date to) {
        return List.of();
    }


}
