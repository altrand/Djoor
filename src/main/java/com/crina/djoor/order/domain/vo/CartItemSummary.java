package com.crina.djoor.order.domain.vo;

public record CartItemSummary(
        int quantity,
        double unitPrice,
        double totalPrice) {

}
