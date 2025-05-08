package com.crina.djoor.order.infrastructure.repository;


import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.OrderItemRepository;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import org.springframework.stereotype.Component;

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


}
