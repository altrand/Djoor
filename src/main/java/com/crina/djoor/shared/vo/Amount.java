package com.crina.djoor.shared.vo;

import com.crina.djoor.order.domain.snapshot.PromoCodeSnapshot;

import java.util.Date;

public record Amount(double value) {
    public Amount add(double value) {
        double amount = this.value + value;
        return new Amount(amount);
    }

    public Amount applyDiscount(PromoCodeSnapshot promoCodeSnapshot) {
        Date currentDate = new Date();
        if (promoCodeSnapshot == null) {
            return new Amount(this.value());
        }
        if (!promoCodeSnapshot.isValid(currentDate))
            return new Amount(this.value());

        return new Amount(this.value() * (1 - promoCodeSnapshot.discountPercentage()));
    }

    /*public Amount computeOwnerCommission(double ownerCommissionPercentage) {
        return new Amount(this.value() * ownerCommissionPercentage);
    }*/
}