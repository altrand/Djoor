package com.crina.djoor.product.domain;

import com.crina.djoor.product.domain.snapshot.ProductSnapshot;

public interface ProductDiscountPolicy {
    double applyDiscount(ProductSnapshot productSnapshot);
}
