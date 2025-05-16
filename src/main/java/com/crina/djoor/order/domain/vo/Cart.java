package com.crina.djoor.order.domain.vo;

import com.crina.djoor.order.domain.GlobalCartDiscountPolicy;
import com.crina.djoor.order.domain.SaleCampaign;
import com.crina.djoor.order.domain.snapshot.SaleCampaignSnapshot;
import com.crina.djoor.product.domain.ProductDiscountPolicy;
import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import com.crina.djoor.shared.vo.Amount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public record Cart(List<CartItem> cartItems) {

    public static Cart create() {
        return new Cart(List.of());
    }

    public List<CartItem> items() {
        return Collections.unmodifiableList(cartItems);
    }

    public Cart addProduct(ProductSnapshot productSnapshot, int quantity, SaleCampaign saleCampaign, ProductDiscountPolicy productDiscountPolicy) {
        List<CartItem> updatedItems = new ArrayList<>();

        boolean isFound = false;
        for (CartItem item : cartItems) {
            if (item.product().id().equals(productSnapshot.id())) {
                int newQuantity = item.quantity() + quantity;
                if (saleCampaign != null && saleCampaign.isActive(new Date()) && newQuantity > 20) {
                    throw new IllegalStateException("Impossible d'ajouter plus de 20 unités d'un même produit pendant la période de solde.");
                }
                updatedItems.add(new CartItem(item.product(), newQuantity));
                isFound = true;
            } else {
                updatedItems.add(item);
            }
        }

        if (!isFound) {
            if (saleCampaign != null && saleCampaign.isActive(new Date()) && quantity > 20) {
                throw new IllegalStateException("Impossible d'ajouter plus de 20 unités d'un même produit pendant la période de solde.");
            }

            ProductSnapshot discountedSnapshot = productSnapshot;

            if ((saleCampaign == null || !saleCampaign.isActive(new Date())) && productDiscountPolicy != null) {
                double discountedPrice = productDiscountPolicy.applyDiscount(productSnapshot);
                discountedSnapshot = new ProductSnapshot(productSnapshot.id(), discountedPrice);
            }

            updatedItems.add(new CartItem(discountedSnapshot, quantity));
        }

        return new Cart(updatedItems);
    }

    public double price() {
        return cartItems.stream().mapToDouble(item -> item.product().price() * item.quantity()).sum();
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

            CartItemSummary cartItemSummary = new CartItemSummary(quantity, unitPrice, cost);
            cartItemSummaryList.add(cartItemSummary);
        }
        return new CartSummary(cartItemSummaryList.size(), totalQuantity, totalCost, cartItemSummaryList);

    }

    public Cart applyDiscount(SaleCampaign campaign) {
        Date currentDate = new Date();

        if (campaign == null || !campaign.isActive(currentDate)) {
            return this;
        }

        List<CartItem> discountedItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            String productId = item.product().id();

            if (campaign.isProductInSale(productId)) {
                ProductSnapshot discountedProduct = new ProductSnapshot(
                        productId,
                        campaign.applyDiscount(item.product().price())
                );
                discountedItems.add(new CartItem(discountedProduct, item.quantity()));
            } else {
                discountedItems.add(item);
            }
        }

        return new Cart(discountedItems);
    }

    public Amount computeTotalAmount() {
        double rawTotal = cartItems.stream()
                .mapToDouble(item -> item.product().price() * item.quantity())
                .sum();
        double discountedTotal = GlobalCartDiscountPolicy.applyDiscountIfEligible(this, rawTotal);
        return new Amount(discountedTotal);
    }
}
