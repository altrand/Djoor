package com.crina.djoor.order.infrastructure.http;

import com.crina.djoor.order.application.command.create.CreateOrderCommand;
import com.crina.djoor.order.application.command.create.CreateOrderHandler;
import com.crina.djoor.order.application.command.create.CreateOrderResponse;
import com.crina.djoor.shared.cqrs.TransactionalCommandHandler;
import com.crina.djoor.shared.http.CustomResponse;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateOrderAction {

    /*private final CreateOrderHandler createOrderHandler;
    private final PlatformTransactionManager txManager;

    public CreateOrderAction(CreateOrderHandler createOrderHandler, PlatformTransactionManager txManager) {
        this.createOrderHandler = createOrderHandler;
        this.txManager = txManager;
    }

    @PostMapping("/api/orders")
    public CustomResponse<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand command) {
        var t = new TransactionalCommandHandler<>(createOrderHandler, txManager);
        var res = t.handle(command);
        var status = res.error == null ? "OK" : "ERROR";
        var custom = new CustomResponse<CreateOrderResponse>();

        if (res.error == null) {
            custom.status = "OK";
            custom.data = res.response;
        } else {
            custom.message = res.error.message;
            custom.status = "ERROR";
        }

        return custom;
    }*/
}
