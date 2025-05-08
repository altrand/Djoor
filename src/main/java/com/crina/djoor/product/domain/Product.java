package com.crina.djoor.product.domain;

import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import com.crina.djoor.shared.vo.Id;

public class Product {
    private final Id id;
    private final String name;
    private final double price;
    private int nbOfProductsInStock;

    public Product(Id id, String name, double price, int nbOfProductsInStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.nbOfProductsInStock = nbOfProductsInStock;
    }

    public static Product create(
            Id id,
            String name,
            double price,
            int nbOfProductsInStock
    ) {
        Product product = new Product(id, name, price, nbOfProductsInStock);
        return product;
    }

    public static Product createFromDB(
            Id id,
            String productName,
            double price,
            int nbOfProductsInStock
    ) {
        return new Product(id, productName, price, nbOfProductsInStock);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Id getId() {
        return id;
    }

    public ProductSnapshot snapshot() {
        return new ProductSnapshot(
                id.value(),
                price
        );
    }

    public void decreaseQuantity(int quantity) {
        this.nbOfProductsInStock -= quantity;
    }

    public void validate() {
        if (this.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        if (this.getName() == null || this.getName().isBlank()) {
            throw new IllegalArgumentException("Product name must not be empty.");
        }
        if (this.getId() == null) {
            throw new IllegalArgumentException("Product ID must not be null.");
        }
    }
}
