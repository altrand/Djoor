package com.crina.djoor.order.domain;


public interface SaleCampaignRepository {
    SaleCampaign ofCode(String code);
}
