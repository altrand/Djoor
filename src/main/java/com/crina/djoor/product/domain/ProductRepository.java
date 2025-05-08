package com.crina.djoor.product.domain;

import java.util.Optional;

public interface ProductRepository {
    void add(Product product);

    Product ofId(String number);

    Product findByName(String number);

    boolean ofIdExist(String productId);
}
