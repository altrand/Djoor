package com.crina.djoor.order.unit;

import com.crina.djoor.order.application.command.cancel.CancelOrderCommand;
import com.crina.djoor.order.application.command.cancel.CancelOrderHandler;
import com.crina.djoor.order.application.command.cancel.CancelOrderResponse;
import com.crina.djoor.order.application.command.create.CreateOrderCommand;
import com.crina.djoor.order.application.command.create.CreateOrderHandler;
import com.crina.djoor.order.application.command.create.CreateOrderResponse;
import com.crina.djoor.order.application.command.create.OrderProductCommand;
import com.crina.djoor.order.application.command.pay.PayOrderCommand;
import com.crina.djoor.order.application.command.pay.PayOrderHandler;
import com.crina.djoor.order.application.command.pay.PayOrderResponse;
import com.crina.djoor.order.application.command.validate.ValidateOrderCommand;
import com.crina.djoor.order.application.command.validate.ValidateOrderHandler;
import com.crina.djoor.order.application.command.validate.ValidateOrderResponse;
import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryUserRepository;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.http.GenericResponse;
import com.crina.djoor.shared.vo.Amount;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class CreateOrderTest {
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
    void shouldCanCreateOrder() {
        CreateOrderCommand command = new CreateOrderCommand("001", "001", new OrderProductCommand("011", 2));

        var res = this.createOrderHandle(command);
        Order order = this.orderRepository.orders.get("001");
        assertTrue(res.response.isCreated);
        assertEquals("001", order.snapshot().id());
        assertEquals(150000.0, order.snapshot().amount());
    }

    @Test
    void shouldAddProductToExistingInitiatedOrderOrCreateNewOne() {
        // Arrange
        String userId = "user-001";
        String orderId = "order-001";
        String productId = "product-123";
        int quantity = 2;

        Product product = new Product(new Id(productId), "Casque", 50000, 100);
        productRepository.add(product);

        // Cas 1 : pas de commande initiée → on crée une nouvelle commande
        CreateOrderCommand command1 = new CreateOrderCommand(orderId, userId, new OrderProductCommand(productId, quantity));
        var response1 = this.createOrderHandle(command1);

        assertTrue(response1.response.isCreated);
        Order order1 = orderRepository.orders.get(orderId); // suppose une implémentation en mémoire
        assertNotNull(order1);
        assertEquals(1, order1.snapshot().cart().items().size());

        // Cas 2 : une commande INITIATED existe → on ajoute un produit à la même commande
        CreateOrderCommand command2 = new CreateOrderCommand("unused-id", userId, new OrderProductCommand(productId, 1));
        var response2 = createOrderHandle(command2);

        // L'ID de la commande reste celui d'origine
        assertEquals(orderId, order1.snapshot().id());
        //assertEquals(1, orderRepository.findInitiatedByUserId(userId).get().);

        Order updatedOrder = orderRepository.orders.get(orderId);
        assertEquals(1, updatedOrder.snapshot().cart().items().size());
        assertEquals(3, updatedOrder.snapshot().cart().items().get(0).quantity()); // 2 + 1
    }


    @Test
    public void shouldValidateOrder() {
        Id orderId = new Id("001");
        Product product = new Product(new Id("P001"), "Ordinateur", 150000.0, 100);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2);
        orderRepository.add(order);

        ValidateOrderCommand command = new ValidateOrderCommand("001");
        var res = this.validateOrderHandle(command);

        Order result = orderRepository.orders.get("001");
        assertTrue(res.response.isValidated);
        assertEquals("001", result.snapshot().id());
        assertEquals(300000.0, result.snapshot().amount());
        assertEquals(OrderState.CONFIRMED, result.snapshot().state());
    }

    @Test
    public void shouldPayConfirmedOrder() {
        Id orderId = new Id("001");
        Product product = new Product(new Id("P001"), "Ordinateur", 150000.0, 100);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2);
        order.confirm();
        orderRepository.add(order);

        PayOrderCommand command = new PayOrderCommand("001");
        var res = this.payOrderHandle(command);

        Order result = orderRepository.orders.get("001");
        assertTrue(res.response.isPaid);
        assertEquals("001", result.snapshot().id());
        assertEquals(300000.0, result.snapshot().amount());
        assertEquals(OrderState.PAID, result.snapshot().state());
    }

    @Test
    public void shouldCancelOrderWithinTwoHours() {
        Id orderId = new Id("002");
        Product product = new Product(new Id("P002"), "Ordinateur", 150000.0, 100);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2);
        order.confirm();

        orderRepository.add(order);

        CancelOrderCommand command = new CancelOrderCommand("002");
        var res = this.cancelOrderHandle(command);

        Order result = orderRepository.orders.get("002");
        assertTrue(res.response.isCancelled);
        assertEquals("002", result.snapshot().id());
        assertEquals(OrderState.CANCELLED, result.snapshot().state());
    }

    @Test
    void shouldNotCancelOrderAfterTwoHours() throws NoSuchFieldException, IllegalAccessException {
        Id orderId = new Id("002");
        Product product = new Product(new Id("P002"), "Ordinateur", 150000.0, 100);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2);
        order.confirm();
        order.pay();
        var field = order.getClass().getDeclaredField("createdAt");
        field.setAccessible(true);
        field.set(order, new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000 + 1000)));

        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void shouldCanCreateOrderFromCartAndRedirectToPaymentPage() {
        /*Product product = new Product("010","Phone",45000);
        User user = User.create(new Id("001"));
        Cart cart = new Cart(List.of(new CartItem(product, 1)));

        Order order = Order.createFromCart(user,cart);

        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals(45000, order.getTotalAmount());*/

    }

    void buildSUT() {
        User user = User.create(new Id("003"), "678747777");
        Product product1 = Product.create(new Id("010"), "Laptop", 100000, 100);
        Product product2 = Product.create(new Id("011"), "Phone", 75000, 50);
        this.productRepository.products.add(product1);
        this.productRepository.products.add(product2);
    }

    private GenericResponse<CreateOrderResponse> createOrderHandle(CreateOrderCommand command) {

        CreateOrderHandler handler = new CreateOrderHandler(this.orderRepository, this.userRepository, this.productRepository);

        try {
            return handler.handle(command);
        } catch (TransactionalException e) {
            throw new RuntimeException(e);
        }
    }

    private GenericResponse<ValidateOrderResponse> validateOrderHandle(ValidateOrderCommand command) {

        ValidateOrderHandler handler = new ValidateOrderHandler(this.orderRepository);

        try {
            return handler.handle(command);
        } catch (TransactionalException e) {
            throw new RuntimeException(e);
        }
    }

    private GenericResponse<PayOrderResponse> payOrderHandle(PayOrderCommand command) {
        PayOrderHandler handler = new PayOrderHandler(this.orderRepository);

        try {
            return handler.handle(command);
        } catch (TransactionalException e) {
            throw new RuntimeException(e);
        }
    }

    private GenericResponse<CancelOrderResponse> cancelOrderHandle(CancelOrderCommand command) {
        CancelOrderHandler handler = new CancelOrderHandler(this.orderRepository);
        try {
            return handler.handle(command);
        } catch (TransactionalException e) {
            throw new RuntimeException(e);
        }
    }
}
