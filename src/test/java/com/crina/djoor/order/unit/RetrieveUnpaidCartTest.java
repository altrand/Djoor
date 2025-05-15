package com.crina.djoor.order.unit;

import com.crina.djoor.order.application.command.retrieve.RetrieveUnpaidCartHandler;
import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.vo.GiftOptions;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderRepository;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.domain.User;
import com.crina.djoor.user.infrastructure.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RetrieveUnpaidCartTest {
    private InMemoryOrderRepository orderRepository;
    private InMemoryUserRepository userRepository;
    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        this.orderRepository = new InMemoryOrderRepository();
        this.userRepository = new InMemoryUserRepository();
        this.productRepository = new InMemoryProductRepository();
        this.buildSUT();
    }

    @Test
    void shouldRetrieveUnpaidOrderForUser() {

        var result = this.retrieveUnpaidCartHandler("user-001");
        assertTrue(result.isPresent());
        var snapshot = result.get().snapshot();

        assertEquals("user-001", snapshot.userId());
        assertEquals("cm-001", snapshot.id());
        assertEquals(200000.0, snapshot.amount());
        assertEquals(com.crina.djoor.order.domain.enums.OrderState.INITIATED, snapshot.state());
    }

    @Test
    void shouldReturnEmptyIfNoUnpaidOrder() {
        var result = this.retrieveUnpaidCartHandler("unknown-user");
        assertTrue(result.isEmpty());
    }

    void buildSUT() {
        User user = User.create(new Id("user-001"), "678747777");
        Product product = Product.create(new Id("010"), "Laptop", 100000, 100);
        this.productRepository.products.add(product);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(new Id("cm-001"), user.id(), product.snapshot(), 2, DeliveryMethod.HOME_DELIVERY, giftOptions, null, null, null);
        orderRepository.add(order);
    }

    private Optional<Order> retrieveUnpaidCartHandler(String userID) {

        RetrieveUnpaidCartHandler handler = new RetrieveUnpaidCartHandler(this.orderRepository);

        try {
            return handler.handle(userID);
        } catch (TransactionalException e) {
            throw new RuntimeException(e);
        }
    }
}
