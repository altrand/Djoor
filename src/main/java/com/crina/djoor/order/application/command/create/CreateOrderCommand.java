package com.crina.djoor.order.application.command.create;

import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.vo.GiftOptions;
import com.crina.djoor.product.domain.ProductDiscountPolicy;

public class CreateOrderCommand {
    public String orderId;
    public OrderProductCommand orderProductCommand;
    public String userId;
    public DeliveryMethod deliveryMethod;
    public GiftOptions giftOptions;
    public String saleCampaignCode;
    public ProductDiscountPolicy productDiscountPolicy;
    public String promoCode;

    private CreateOrderCommand() {
    }

    public CreateOrderCommand(
            String orderId,
            String userId,
            OrderProductCommand orderProductCommand,
            DeliveryMethod deliveryMethod,
            GiftOptions giftOptions,
            String saleCampaignCode,
            ProductDiscountPolicy productDiscountPolicy,
            String promoCode
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.deliveryMethod = deliveryMethod;
        this.orderProductCommand = orderProductCommand;
        this.giftOptions = giftOptions;
        this.saleCampaignCode = saleCampaignCode;
        this.productDiscountPolicy = productDiscountPolicy;
        this.promoCode = promoCode;
    }
}
