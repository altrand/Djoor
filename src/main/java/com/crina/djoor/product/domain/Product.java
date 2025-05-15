package com.crina.djoor.product.domain;

import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import com.crina.djoor.shared.vo.Id;

public class Product {
    private final Id id;
    private final String name;
    private final double price;
    private int nbOfProductsInStock;
    // private boolean isInCurrentSale = false;

    private Product(Id id, String name, double price, int nbOfProductsInStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.nbOfProductsInStock = nbOfProductsInStock;
        //this.isInCurrentSale = false;
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

    /*public static Product createFromDB(
            Id id,
            String productName,
            double price,
            int nbOfProductsInStock
    ) {
        return new Product(id, productName, price, nbOfProductsInStock);
    }*/

    public String name() {
        return name;
    }

    public int stock() {
        return nbOfProductsInStock;
    }

    public double price() {
        return price;
    }

    public Id id() {
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
        if (this.price() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        if (this.name() == null || this.name().isBlank()) {
            throw new IllegalArgumentException("Product name must not be empty.");
        }
        if (this.id() == null) {
            throw new IllegalArgumentException("Product ID must not be null.");
        }
    }

    /*public boolean isInCurrentSale() {
        return isInCurrentSale;
    }

    public void setInCurrentSale(boolean inCurrentSale) {
        isInCurrentSale = inCurrentSale;
    }*/
}
