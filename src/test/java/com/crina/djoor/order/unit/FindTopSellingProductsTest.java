package com.crina.djoor.order.unit;

import com.crina.djoor.order.application.command.findtopselling.FindTopSellingProductsCommand;
import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.enums.PaymentMethod;
import com.crina.djoor.order.domain.vo.GiftOptions;
import com.crina.djoor.order.domain.vo.TopSellingProduct;
import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderItemRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderRepository;
import com.crina.djoor.order.application.command.findtopselling.FindTopSellingProductsHandler;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.infrastructure.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FindTopSellingProductsTest {
    private InMemoryOrderItemRepository orderItemRepository;
    private InMemoryOrderRepository orderRepository;
    private InMemoryUserRepository userRepository;
    private InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        this.orderRepository = new InMemoryOrderRepository();
        this.orderItemRepository = new InMemoryOrderItemRepository();
    }

    @Test
    void shouldReturnTopSellingProductsBetweenDates() {

        FindTopSellingProductsHandler handler = new FindTopSellingProductsHandler(orderItemRepository);

        Product product1 = Product.create(new Id("010"), "Laptop", 100000, 100);
        Product product2 = Product.create(new Id("011"), "Phone", 75000, 50);

        var giftOptions = new GiftOptions(false, null, null);
        Order order1 = Order.create(new Id("CM-O1"), "User-01", product1.snapshot(), 4, DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        Order order2 = Order.create(new Id("CM-O2"), "User-02", product2.snapshot(), 7, DeliveryMethod.HOME_DELIVERY, giftOptions, null, null, null);
        Order order3 = Order.create(new Id("CM-O3"), "User-03", product1.snapshot(), 2, DeliveryMethod.HOME_DELIVERY, giftOptions, null, null, null);
        order1.confirm();
        order2.confirm();
        order3.confirm();
        order1.pay(PaymentMethod.CRINA_PAY);
        order2.pay(PaymentMethod.CRINA_PAY);
        order3.pay(PaymentMethod.CRINA_PAY);
        orderItemRepository.addOrder(order1);
        orderItemRepository.addOrder(order2);
        orderItemRepository.addOrder(order3);

        Date from = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24); // hier
        Date to = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);   // demain

        var res = handler.handle(new FindTopSellingProductsCommand(from, to));

        List<TopSellingProduct> results = res.response.products;

        assertEquals(2, results.size());

        assertEquals("011", results.get(0).productId());  // Phone - 7 unités
        assertEquals(7, results.get(0).totalQuantitySold());

        assertEquals("010", results.get(1).productId());  // Laptop - 6 unités (4 + 2)
        assertEquals(6, results.get(1).totalQuantitySold());
    }

}
