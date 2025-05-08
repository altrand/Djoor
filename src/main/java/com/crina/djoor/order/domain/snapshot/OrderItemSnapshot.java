package com.crina.djoor.order.domain.snapshot;

public record OrderItemSnapshot(String id, String productId, int quantity, double price) {

}
