package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.order.domain.SaleCampaign;
import com.crina.djoor.order.domain.SaleCampaignRepository;

public class InMemorySaleCampaignRepository implements SaleCampaignRepository {

    @Override
    public SaleCampaign ofCode(String code) {
        return null;
    }
}
