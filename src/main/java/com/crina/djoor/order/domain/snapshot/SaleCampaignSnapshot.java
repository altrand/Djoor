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
}
