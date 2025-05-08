package com.crina.djoor.order.application.command.validate;


import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.http.GenericResponse;

public class ValidateOrderHandler implements CommandHandler<ValidateOrderCommand, GenericResponse<ValidateOrderResponse>> {
    private final OrderRepository orderRepository;

    public ValidateOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }

    @Override
    public GenericResponse<ValidateOrderResponse> handle(ValidateOrderCommand command) throws ErrorOnSaveOrderException {
        var generic = new GenericResponse<ValidateOrderResponse>();
        ValidateOrderResponse res = new ValidateOrderResponse();

        Order order = orderRepository.ofId(command.orderId);
        if (order == null) throw new IllegalArgumentException("Order not found.");

        order.confirm();
        orderRepository.add(order);

        res.orderId = order.snapshot().id();
        res.isValidated = true;
        generic.response = res;
        return generic;
    }
}
