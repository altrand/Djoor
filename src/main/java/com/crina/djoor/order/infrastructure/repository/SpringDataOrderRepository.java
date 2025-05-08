package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.order.infrastructure.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT a FROM OrderEntity a WHERE a.id = :id")
    OrderEntity ofId(@Param("id") String id);
}
