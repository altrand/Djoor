package com.crina.djoor.product.infrastructure.model;

import com.crina.djoor.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int nbOfProductsInStock;


    public static ProductEntity createFromDomain(Product product) {

        ProductEntity productEntity = new ProductEntity();
        productEntity.id = product.id().value();
        productEntity.price = product.price();
        productEntity.nbOfProductsInStock = product.stock();

        return productEntity;
    }
}
