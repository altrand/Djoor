package com.crina.djoor.order.domain.vo;


import com.crina.djoor.order.domain.enums.GiftType;
import com.crina.djoor.shared.vo.Address;
import com.crina.djoor.shared.vo.Amount;

public record GiftOptions(
        boolean isGift,
        Address recipientAddress,
        GiftType type
) {
    public GiftOptions {
        if (isGift) {
            if (recipientAddress == null) {
                throw new IllegalArgumentException("L'adresse du destinataire est requise pour le cadeau.");
            }
            if (type == null) {
                throw new IllegalArgumentException("Le type de cadeau est requis.");
            }
        }
    }

    public Amount cost() {
        if (!isGift) return new Amount(0);
        else return new Amount(type.fee());
    }


}
