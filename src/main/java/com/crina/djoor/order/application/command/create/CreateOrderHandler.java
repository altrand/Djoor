package com.crina.djoor.order.application.command.create;

import com.crina.djoor.order.domain.*;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.order.domain.snapshot.PromoCodeSnapshot;
import com.crina.djoor.order.domain.snapshot.SaleCampaignSnapshot;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.http.GenericResponse;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.domain.UserRepository;

import java.util.List;
import java.util.Optional;


public class CreateOrderHandler implements CommandHandler<CreateOrderCommand, GenericResponse<CreateOrderResponse>> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final SaleCampaignRepository saleCampaignRepository;

    public CreateOrderHandler(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository, PromoCodeRepository promoCodeRepository, SaleCampaignRepository saleCampaignRepository

    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.saleCampaignRepository = saleCampaignRepository;
    }


    @Override
    public GenericResponse<CreateOrderResponse> handle(CreateOrderCommand command) throws ErrorOnSaveOrderException {
        var generic = new GenericResponse<CreateOrderResponse>();
        CreateOrderResponse res = new CreateOrderResponse();
        PromoCodeSnapshot promoCodeSnapshot = null;
        SaleCampaignSnapshot saleCampaignSnapshot = null;

        try {
            Product product = this.productRepository.ofId(command.orderProductCommand.productId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found.");
            }

            PromoCode promoCode = this.promoCodeRepository.ofCode(command.promoCode);
            if (promoCode != null) {
                promoCodeSnapshot = promoCode.snapshot();
            }

            SaleCampaign saleCampaign = this.saleCampaignRepository.ofCode(command.saleCampaignCode);
            if (saleCampaign != null) {
                saleCampaignSnapshot = saleCampaign.snapshot();
            }
            // Vérifie s'il existe déjà une commande INITIATED pour cet utilisateur
            Optional<Order> optionalOrder = orderRepository.findInitiatedByUserId(command.userId);

            if (optionalOrder.isPresent()) {
                // On récupère la commande existante
                var snapshot = optionalOrder.get().snapshot();
                List<OrderItem> orderItems = orderRepository.findOrderItemsByOrderId(snapshot.id());

                Order existingOrder = Order.reconstructFromDatabase(
                        new Id(snapshot.id()),
                        snapshot.userId(),
                        orderItems,
                        snapshot.deliveryMethod(),
                        snapshot.giftOptions(),
                        snapshot.createdAt(),
                        null,
                        null
                );

                // Ajoute le produit à la commande existante
                existingOrder.addProduct(product.snapshot(), command.orderProductCommand.nbOfProduct, saleCampaignSnapshot, null);
                orderRepository.addOrUpdate(existingOrder);

                res.orderId = existingOrder.snapshot().id();
                res.isCreated = true;
                generic.response = res;
                return generic;
            }
            // Sinon, on crée une nouvelle commande
            Order newOrder = Order.create(
                    new Id(command.orderId),
                    command.userId,
                    product.snapshot(),
                    command.orderProductCommand.nbOfProduct,
                    command.deliveryMethod,
                    command.giftOptions,
                    saleCampaignSnapshot,
                    command.productDiscountPolicy,
                    promoCodeSnapshot
            );

            orderRepository.add(newOrder);
            res.orderId = newOrder.snapshot().id();
            res.isCreated = true;
            generic.response = res;
            return generic;
        } catch (RuntimeException e) {
            res.message = e.getMessage();
            generic.response = res;
            return generic;
        }
    }

}
