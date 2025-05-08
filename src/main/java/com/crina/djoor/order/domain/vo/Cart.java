package com.crina.djoor.order.domain.vo;

import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.snapshot.ProductSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record Cart(List<CartItem> cartItems) {

    public static Cart create() {
        return new Cart(List.of());
    }

    public List<CartItem> items() {
        return Collections.unmodifiableList(cartItems);
    }

    public Cart addProduct(ProductSnapshot productSnapshot, int quantity) {

        List<CartItem> updatedItems = new ArrayList<>();

        boolean isFound = false;
        for (CartItem item : cartItems) {
            if (item.product().id().equals(productSnapshot.id())) {
                int newQuantity = item.quantity() + quantity;
                updatedItems.add(new CartItem(productSnapshot, newQuantity));
                isFound = true;
            } else {
                updatedItems.add(item);
            }
        }
        if (!isFound) {
            updatedItems.add(new CartItem(productSnapshot, quantity));
        }

        return new Cart(updatedItems);
    }

    public double price() {
        return cartItems.stream()
                .mapToDouble(item -> item.product().price() * item.quantity())
                .sum();
    }

    public CartSummary generateSummary() {
        int totalQuantity = 0;
        double totalCost = 0;
        List<CartItemSummary> cartItemSummaryList = new ArrayList<>();

        for (CartItem item : cartItems) {
            int quantity = item.quantity();
            double unitPrice = item.product().price();
            double cost = quantity * unitPrice;

            totalQuantity += quantity;
            totalCost += cost;

            CartItemSummary cartItemSummary = new CartItemSummary(
                    quantity, unitPrice,
                    cost);
            cartItemSummaryList.add(cartItemSummary);
        }
        return new CartSummary(cartItemSummaryList.size(), totalQuantity, totalCost, cartItemSummaryList);

    }



    /*public void validate() {
        if (orderItems == null || orderItems().isEmpty()) {
            throw new IllegalStateException("The cart must contain at least one item.");
        }

        for (OrderItem item : orderItems()) {
            if (item.quantity <= 0) {
                throw new IllegalStateException("Item quantity must be greater than zero: " + item.productId);
            }
            if (item.price <= 0) {
                throw new IllegalStateException("Item price must be greater than zero: " + item.productId);
            }
        }
    }*/
}
