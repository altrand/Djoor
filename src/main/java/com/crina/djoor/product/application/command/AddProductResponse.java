package com.crina.djoor.product.application.command;

import com.crina.djoor.order.domain.vo.CartItemSummary;

import java.util.List;

public class AddProductResponse {
    public boolean isAdded;
    public String message;
    public int numberProductSelected;
    public int quantityProduct;
    public double totalPrice;
    public List<CartItemSummary> cartItemSummaries;
}
