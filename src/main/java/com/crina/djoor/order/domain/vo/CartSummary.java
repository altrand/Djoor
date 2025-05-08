package com.crina.djoor.order.domain.vo;

import java.util.List;

public record CartSummary(
        int numberProductsSelected,
        int quantityProduct,
        double totalPrice,
        List<CartItemSummary> cartItemSummaries) {

}
