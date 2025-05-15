package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.order.domain.vo.CartItem;

import java.util.List;

public class GlobalCartDiscountPolicy {

    public static double applyDiscountIfEligible(Cart cart, double originalTotal) {
        List<CartItem> items = cart.items();

        long productsWithAtLeast2 = items.stream()
                .filter(item -> item.quantity() >= 2)
                .count();

        long productsWithAtLeast4 = items.stream()
                .filter(item -> item.quantity() >= 4)
                .count();

        if (productsWithAtLeast4 >= 10) {
            return originalTotal * 0.90; // 10% de réduction
        } else if (productsWithAtLeast2 >= 5) {
            return originalTotal * 0.95; // 5% de réduction
        }

        return originalTotal; // Pas de réduction
    }
}
