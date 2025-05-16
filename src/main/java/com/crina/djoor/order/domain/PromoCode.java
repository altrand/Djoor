package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.exceptions.snapshot.OrderSnapshot;
import com.crina.djoor.order.domain.snapshot.PromoCodeSnapshot;
import com.crina.djoor.shared.vo.Id;

import java.util.Date;

public class PromoCode {
    private Id id;
    private double discountPercentage;
    private double ownerCommissionPercentage;
    private String code;
    private String ownerUserId;
    private Date expirationDate;
    private boolean isActive;

    public PromoCode(double discountPercentage, double ownerCommissionPercentage, String code, String ownerUserId, Date expirationDate, boolean isActive) {
        this.code = code;
        this.ownerUserId = ownerUserId;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.discountPercentage = discountPercentage;
        this.ownerCommissionPercentage = ownerCommissionPercentage;
    }

    public static PromoCode Create(double discountPercentage, double ownerCommissionPercentage, String code, String ownerUserId, Date expirationDate, boolean isActive) {
        return new PromoCode(discountPercentage, ownerCommissionPercentage, code, ownerUserId, expirationDate, isActive);
    }

    public PromoCodeSnapshot snapshot() {
        return new PromoCodeSnapshot(
                this.discountPercentage,
                this.ownerCommissionPercentage,
                this.code,
                this.ownerUserId,
                this.expirationDate,
                this.isActive
        );
    }

    public boolean isValid(Date date) {
        return isActive && date.before(expirationDate);
    }

    public double discountPercentage() {
        return discountPercentage;
    }
}
