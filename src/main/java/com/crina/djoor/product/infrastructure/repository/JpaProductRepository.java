package com.crina.djoor.product.infrastructure.repository;

import com.crina.djoor.order.infrastructure.model.OrderEntity;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.product.infrastructure.model.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaProductRepository implements ProductRepository {
private final SpringDataProductRepository productRepository;

    public JpaProductRepository(SpringDataProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void add(Product product) {
        ProductEntity entity = ProductEntity.createFromDomain(product);
        productRepository.save(entity);

    }

    @Override
    public Product ofId(String number) {

        return null;
    }

    @Override
    public Product findByName(String number) {
        return null;
    }

    @Override
    public boolean ofIdExist(String productId) {
        return false;
    }
}
