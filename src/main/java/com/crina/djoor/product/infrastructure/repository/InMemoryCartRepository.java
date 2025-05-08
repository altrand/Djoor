package com.crina.djoor.product.infrastructure.repository;

import com.crina.djoor.order.domain.vo.Cart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryCartRepository {

    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    public Cart loadCart(String userId) {
        return carts.getOrDefault(userId, new Cart(List.of()));
    }

    public void saveCart(String userId, Cart cart) {
        carts.put(userId, cart);
    }

    public void clearCart(String userId) {
        carts.remove(userId);
    }
}
