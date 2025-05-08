package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.order.infrastructure.model.OrderEntity;
import com.crina.djoor.order.infrastructure.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    @Query("SELECT a FROM OrderItemEntity a WHERE a.id = :id")
    OrderEntity ofId(@Param("id") String id);
}
