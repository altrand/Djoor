package com.crina.djoor.order.domain.vo;

import com.crina.djoor.product.domain.snapshot.ProductSnapshot;

public record CartItem(ProductSnapshot product, int quantity) {

}
