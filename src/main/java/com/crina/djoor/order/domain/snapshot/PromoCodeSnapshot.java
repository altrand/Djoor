package com.crina.djoor.order.domain.snapshot;

import java.util.Date;

public record PromoCodeSnapshot(
        double discountPercentage,
        double ownerCommissionPercentage,
        String code,
        String ownerUserId,
        Date expirationDate,
        boolean isActive
) {
    public boolean isValid(Date date) {
        return isActive && date.before(expirationDate);
    }
}
