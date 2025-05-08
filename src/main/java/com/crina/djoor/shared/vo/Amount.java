package com.crina.djoor.shared.vo;

public record Amount(double value) {
    public Amount add(double value) {
        double amount = this.value + value;
        return new Amount(amount);
    }
}