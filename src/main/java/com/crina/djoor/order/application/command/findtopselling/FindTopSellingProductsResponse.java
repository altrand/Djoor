package com.crina.djoor.order.application.command.findtopselling;

import com.crina.djoor.order.domain.vo.TopSellingProduct;

import java.util.List;

public class FindTopSellingProductsResponse {
    public boolean isFound;
    public String message;
    public List<TopSellingProduct> products;
}
