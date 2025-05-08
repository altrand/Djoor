package com.crina.djoor.order.application.command.cancel;

import com.crina.djoor.order.application.command.pay.PayOrderResponse;
import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.http.GenericResponse;

public class CancelOrderHandler implements CommandHandler<CancelOrderCommand, GenericResponse<CancelOrderResponse>> {
    private final OrderRepository orderRepository;

    public CancelOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public GenericResponse<CancelOrderResponse> handle(CancelOrderCommand command) throws TransactionalException {
        GenericResponse<CancelOrderResponse> generic = new GenericResponse<CancelOrderResponse>();
        CancelOrderResponse res = new CancelOrderResponse();

        Order order = orderRepository.ofId(command.orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        order.cancel();
        orderRepository.update(order);

        res.orderId = order.snapshot().id();
        res.isCancelled = true;
        res.message = "Order cancelled.";
        generic.response = res;
        return generic;
    }
}
