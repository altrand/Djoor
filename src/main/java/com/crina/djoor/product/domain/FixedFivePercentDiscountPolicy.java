package com.crina.djoor.product.domain;

import com.crina.djoor.product.domain.snapshot.ProductSnapshot;

import java.util.Set;

public class FixedFivePercentDiscountPolicy implements ProductDiscountPolicy {

    private final Set<String> eligibleProductIds;

    public FixedFivePercentDiscountPolicy(Set<String> eligibleProductIds) {
        this.eligibleProductIds = eligibleProductIds;
    }

    @Override
    public double applyDiscount(ProductSnapshot productSnapshot) {
        if (eligibleProductIds.contains(productSnapshot.id())) {
            return productSnapshot.price() * 0.95;
        }
        return productSnapshot.price();
    }
}
