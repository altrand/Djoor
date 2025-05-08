package com.crina.djoor.order.application.command.create;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.http.GenericResponse;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.UserRepository;

import java.util.List;
import java.util.Optional;


public class CreateOrderHandler implements CommandHandler<CreateOrderCommand, GenericResponse<CreateOrderResponse>> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public CreateOrderHandler(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository

    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    @Override
    public GenericResponse<CreateOrderResponse> handle(CreateOrderCommand command) throws ErrorOnSaveOrderException {
        var generic = new GenericResponse<CreateOrderResponse>();
        CreateOrderResponse res = new CreateOrderResponse();

        try {
            Product product = this.productRepository.ofId(command.orderProductCommand.productId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found.");
            }

            // Recherche d'une commande déjà INITIÉE pour cet utilisateur

            Optional<Order> optionalOrder = orderRepository.findInitiatedByUserId(command.userId)
                    .map(orderEntity -> {
                        List<OrderItem> orderItems = orderRepository.findOrderItemsByOrderId(orderEntity.snapshot().id());
                        return Order.reconstructFromDatabase(
                                new Id(orderEntity.snapshot().id()),
                                orderEntity.snapshot().userId(),
                                orderItems,
                                orderEntity.snapshot().state(),
                                orderEntity.snapshot().createdAt()
                        );
                    });

            Order order = optionalOrder.orElseGet(() ->
                    Order.create(
                            new Id(command.orderId),
                            command.userId,
                            product.snapshot(),
                            command.orderProductCommand.nbOfProduct
                    )
            );

            // Si la commande existait déjà, ajoute un produit
            if (optionalOrder.isPresent()) {
                order.addProduct(product.snapshot(), command.orderProductCommand.nbOfProduct);
            }

            orderRepository.addOrUpdate(order);
            res.orderId = order.snapshot().id();

        } catch (RuntimeException e) {
            res.message = e.getMessage();
            generic.response = res;
            return generic;
        }

        res.isCreated = true;
        generic.response = res;
        return generic;
    }


}
