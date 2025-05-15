package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.domain.enums.PaymentMethod;
import com.crina.djoor.order.domain.exceptions.snapshot.OrderSnapshot;
import com.crina.djoor.order.domain.snapshot.PromoCodeSnapshot;
import com.crina.djoor.order.domain.snapshot.SaleCampaignSnapshot;
import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.order.domain.vo.CartItem;
import com.crina.djoor.order.domain.vo.CartSummary;
import com.crina.djoor.order.domain.vo.GiftOptions;
import com.crina.djoor.product.domain.ProductDiscountPolicy;
import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import com.crina.djoor.shared.vo.Amount;
import com.crina.djoor.shared.vo.Id;

import java.util.*;

public class Order {
    private Id id;
    private String userId;
    private Cart cart;
    private Date createdAt;
    private Amount amount;
    private OrderState state;
    private DeliveryMethod deliveryMethod;
    private Amount deliveryCost;
    private GiftOptions giftOptions;
    private PaymentMethod paymentMethod;

    private Order(Id id, String userId, Cart cart, DeliveryMethod deliveryMethod, GiftOptions giftOptions, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.cart = cart;
        this.deliveryMethod = deliveryMethod;
        this.deliveryCost = computeDeliveryCost(deliveryMethod);
        this.giftOptions = giftOptions;
        double total = cart.computeTotalAmount().value() + deliveryCost.value();

        if (giftOptions != null) {
            total += giftOptions.cost().value();
        }

        this.amount = new Amount(total);
        this.state = OrderState.INITIATED;
    }

    public static Order create(
            Id id,
            String userId,
            ProductSnapshot product,
            int quantity,
            DeliveryMethod deliveryMethod,
            GiftOptions giftOptions,
            SaleCampaignSnapshot saleCampaignSnapshot,
            ProductDiscountPolicy productDiscountPolicy,
            PromoCodeSnapshot promoCodeSnapshot
    ) throws RuntimeException {

        Cart cart = Cart.create();
        cart = cart.addProduct(product, quantity, saleCampaignSnapshot, productDiscountPolicy);

        cart = cart.applyDiscount(saleCampaignSnapshot);

        Order order = new Order(id, userId, cart, deliveryMethod, giftOptions, new Date());

        order.amount = order.amount.applyDiscount(promoCodeSnapshot);

        return order;
    }

    public static Order reconstructFromDatabase(Id id, String userId, List<OrderItem> orderItems, DeliveryMethod deliveryMethod, GiftOptions giftOptions, Date createdAt, SaleCampaignSnapshot saleCampaignSnapshot, ProductDiscountPolicy productDiscountPolicy) throws RuntimeException {
        Cart cart = Cart.create();

        for (OrderItem item : orderItems) {
            ProductSnapshot snapshot = new ProductSnapshot(
                    item.snapshot().productId(),
                    item.snapshot().price()
            );

            cart = cart.addProduct(snapshot, item.snapshot().quantity(), saleCampaignSnapshot, productDiscountPolicy);
        }

        return new Order(id, userId, cart, deliveryMethod, giftOptions, createdAt);
    }

    public OrderSnapshot snapshot() {
        return new OrderSnapshot(
                this.id.value(),
                this.userId,
                this.amount.value(),
                this.state,
                this.createdAt,
                this.cart.generateSummary(),
                this.deliveryMethod,
                this.giftOptions,
                paymentMethod
        );
    }

    public CartSummary cartSummary() {
        return cart.generateSummary();
    }

    public List<CartItem> items() {
        return cart.items();
    }

    public void confirm() {
        if (this.state != OrderState.INITIATED) {
            throw new IllegalStateException("La commande ne peut pas être confirmée dans cet état.");
        }
        this.state = OrderState.CONFIRMED;
    }

    public void pay(PaymentMethod method) {
        if (this.state != OrderState.CONFIRMED) {
            throw new IllegalStateException("La commande doit être confirmée avant d'être payée.");
        }
        this.paymentMethod = method;
        this.state = OrderState.PAID;
    }

    public void cancel() {
        long now = new Date().getTime();
        long twoHoursInMillis = 2 * 60 * 60 * 1000;
        if (this.state == OrderState.PAID && (now - this.createdAt.getTime() > twoHoursInMillis)) {
            throw new IllegalStateException("Le délai d'annulation de 2 heures est passé.");
        }

        if (this.state == OrderState.DELIVERED) {
            throw new IllegalStateException("Seules les commandes non encore livrées peuvent être annulées.");
        }
        this.state = OrderState.CANCELLED;
    }

    private Amount computeDeliveryCost(DeliveryMethod deliveryMethod) {
        switch (deliveryMethod) {
            case HOME_DELIVERY -> {
                return new Amount(2000);
            }
            case STORE_PICKUP -> {
                return new Amount(0);
            }
            default -> throw new IllegalArgumentException("Mode de livraison inconnu");
        }
    }

    public void addProduct(ProductSnapshot snapshot, int nbOfProduct, SaleCampaignSnapshot saleCampaignSnapshot, ProductDiscountPolicy productDiscountPolicy) {
        cart = cart.addProduct(snapshot, nbOfProduct, saleCampaignSnapshot, productDiscountPolicy);
        this.amount = cart.computeTotalAmount();
    }
}
