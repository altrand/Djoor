package com.crina.djoor.order.domain.enums;

public enum OrderState {
    INITIATED("Commande crée"),
    CONFIRMED("Panier confirmé"),
    PAID("Payement reçu"),
    DELIVERED("Produit livré"),
    COMPLETED("Commande finalisé"),
    CANCELLED("Commande annulée");

    private final String message;

    OrderState(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
