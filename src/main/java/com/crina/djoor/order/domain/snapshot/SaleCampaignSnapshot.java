package com.crina.djoor.order.domain.snapshot;

import java.util.Date;
import java.util.Set;

public record SaleCampaignSnapshot(
        Date startDate,
        Date endDate,
        String code,
        int percentage,
        int maxProducts,
        Set<String> discountedProductIds
) {
    public boolean isActive(Date currentDate) {
        return !currentDate.before(startDate) && !currentDate.after(endDate);
    }

    public boolean isProductInSale(String productId) {
        return discountedProductIds.contains(productId);
    }

    public double applyDiscount(double originalPrice) {
        return originalPrice * (1 - percentage / 100.0);
    }
}
