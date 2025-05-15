package com.crina.djoor.product.infrastructure.repository;

import com.crina.djoor.product.infrastructure.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, String> {
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
    ProductEntity ofId(@Param("id") String id);
}
