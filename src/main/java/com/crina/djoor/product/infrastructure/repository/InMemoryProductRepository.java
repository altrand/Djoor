package com.crina.djoor.product.infrastructure.repository;

import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryProductRepository implements ProductRepository {

    public List<Product> products = new ArrayList<>();

    @Override
    public void add(Product product) {
        products.add(product);
    }

    @Override
    public Product ofId(String id) {
        return products.stream().filter(product -> product.id().value().equals(id)).findFirst().orElse(null);
    }


    @Override
    public Product findByName(String name) {
        return products.stream().filter(
                product -> product.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public boolean ofIdExist(String productId) {
        return false;
    }
}
