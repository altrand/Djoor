package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryProductRepository implements ProductRepository {

    public List<Product> products = new ArrayList<>();

    @Override
    public void add(Product product) {
        products.add(product);
    }

    @Override
    public Product ofId(String id) {
        return products.stream().filter(product ->  product.getId().value().equals(id)).findFirst().orElse(null);
    }



    @Override
    public Product findByName(String name) {
        return products.stream().filter(
                product -> product.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public boolean ofIdExist(String productId) {
        return false;
    }
}
