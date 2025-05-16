package com.crina.djoor.shared.vo;

import com.crina.djoor.order.domain.PromoCode;

import java.util.Date;

public record Amount(double value) {
    public Amount add(double value) {
        double amount = this.value + value;
        return new Amount(amount);
    }

    public Amount applyDiscount(PromoCode promoCode) {
        Date currentDate = new Date();
        if (promoCode == null) {
            return new Amount(this.value());
        }
        if (!promoCode.isValid(currentDate))
            return new Amount(this.value());

        return new Amount(this.value() * (1 - promoCode.discountPercentage()));
    }

    /*public Amount computeOwnerCommission(double ownerCommissionPercentage) {
        return new Amount(this.value() * ownerCommissionPercentage);
    }*/
}