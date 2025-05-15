package com.crina.djoor.order.application.command.pay;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.http.GenericResponse;

public class PayOrderHandler implements CommandHandler<PayOrderCommand, GenericResponse<PayOrderResponse>> {
    private final OrderRepository orderRepository;

    public PayOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public GenericResponse<PayOrderResponse> handle(PayOrderCommand command) throws TransactionalException {
        GenericResponse<PayOrderResponse> generic = new GenericResponse<PayOrderResponse>();
        PayOrderResponse res = new PayOrderResponse();

        Order order = orderRepository.ofId(command.orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        if (!order.snapshot().userId().equals(command.userId)) {
            throw new IllegalArgumentException("Unauthorized access to order.");
        }

        order.pay(command.paymentMethod);
        orderRepository.update(order);

        res.orderId = order.snapshot().id();
        res.isPaid = true;
        res.message = "Payment successful.";
        generic.response = res;
        return generic;
    }
}
