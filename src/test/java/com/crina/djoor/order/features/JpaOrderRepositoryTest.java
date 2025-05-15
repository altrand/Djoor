package com.crina.djoor.order.features;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.vo.GiftOptions;
import com.crina.djoor.order.infrastructure.model.OrderEntity;
import com.crina.djoor.order.infrastructure.repository.JpaOrderRepository;
import com.crina.djoor.order.infrastructure.repository.SpringDataOrderRepository;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class JpaOrderRepositoryTest {
    @Autowired
    JpaOrderRepository repository;
    @Autowired
    SpringDataOrderRepository dataRepository;

    @Test
    public void shouldAddOrder() {

        User user = User.create(new Id("002"), "678747777");
        Product product = Product.create(new Id("001"), "Laptop", 100000, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(
                new Id("001"),
                user.id(),
                product.snapshot(),
                3,
                DeliveryMethod.HOME_DELIVERY,
                giftOptions,
                null,
                null,
                null
        );
        repository.add(order);
        OrderEntity orderAdd = dataRepository.ofId(order.snapshot().id());
        assertNotNull(orderAdd);
        assertTrue(orderAdd.equals(OrderEntity.createFromDomain(order)));
    }


}
