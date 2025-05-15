package com.crina.djoor.order.domain.enums;

public enum GiftType {
    SIMPLE(2000),
    BIRTHDAY(3000),
    WEDDING(5000);

    private final double fee;

    GiftType(double fee) {
        this.fee = fee;
    }

    public double fee() {
        return this.fee;
    }
}
