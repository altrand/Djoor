package com.crina.djoor.cart.unit;

import com.crina.djoor.shared.vo.Cart;
import com.crina.djoor.cart.domain.CartItem;
import com.crina.djoor.cart.domain.CartSummary;
import com.crina.djoor.cart.domain.Product;
import com.crina.djoor.order.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryUserRepository;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddProductToCartTest {
    private InMemoryUserRepository userRepository;
    private InMemoryProductRepository productRepository;
    private Cart cart;

    @BeforeEach
    void setUp() {
        this.userRepository = new InMemoryUserRepository();
        this.productRepository = new InMemoryProductRepository();
        this.buildSUT();
    }
    
    @Test
    void shouldCanAddProductToCart(){
        Product product = new Product("001", "Mouse", 5000);
        Cart cart = new Cart(new ArrayList<>());
        cart = cart.addProduct(product,1);
        assertEquals(1, cart.getItems().size());
        assertEquals("Mouse", cart.getItems().get(0).getProduct().getName());
        assertEquals(5000, cart.getItems().get(0).getProduct().getPrice());

    }

    @Test
    void ShouldCanAddProductToCartOrUpdateQuantityIfAlreadyPresent(){
        Product product = new Product("001", "Mouse", 5000);
        Cart cart = new Cart(new ArrayList<>());

        cart = cart.addProduct(product, 1);

        assertEquals(1, cart.getItems().size());
        assertEquals(1, cart.getItems().get(0).getQuantity());

        cart = cart.addProduct(product, 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(4, cart.getItems().get(0).getQuantity());
        assertEquals("Mouse", cart.getItems().get(0).getProduct().getName());
    }

    @Test
    void shouldCanAddProductFromBaseListToCart(){
        Product product = productRepository.findById("001");
        cart = cart.addProduct(product,1);
        assertEquals(1, cart.getItems().size());
        assertEquals("Fridge", cart.getItems().get(0).getProduct().getName());
    }

    @Test
    void shouldCanSearchProductByNameToCart(){
        Product product = productRepository.findByName("Fridge");
        cart = cart.addProduct(product, 1);
        assertEquals(1, cart.getItems().size());
        assertEquals("Fridge", cart.getItems().get(0).getProduct().getName());
    }

    @Test
    void shouldCanReturnAccurateSummaryWhenProductAdded(){
        Product product1 = new Product("010", "Laptop", 100000);
        Product product2 = new Product("011", "Phone", 75000);

        Cart cart = new Cart(List.of(new CartItem(product1,1)));
        cart = cart.addProduct(product2, 2);

        CartSummary summary = cart.generateSummary();

        assertEquals(2, summary.getNumberProductsSelected());
        assertEquals(3, summary.getQuantityProducts());
        assertEquals(250000.0, summary.getTotalPrice());
        assertEquals("Laptop", summary.getCartItemSummary().get(0).getProductName());
        assertEquals(100000, summary.getCartItemSummary().get(0).getUnitPrice());
        assertEquals(1, summary.getCartItemSummary().get(0).getQuantity());
        assertEquals(100000, summary.getCartItemSummary().get(0).getTotalPrice());
        assertEquals("Phone", summary.getCartItemSummary().get(1).getProductName());
        assertEquals(75000, summary.getCartItemSummary().get(1).getUnitPrice());
        assertEquals(2, summary.getCartItemSummary().get(1).getQuantity());
        assertEquals(150000, summary.getCartItemSummary().get(1).getTotalPrice());
    }

    void buildSUT() {
        User user = User.create(new Id("001"));
        this.userRepository.users.put(user.id(), user);
        this.productRepository.products.add(new Product("001", "Fridge", 100000));
        this.productRepository.products.add(new Product("002", "Stove", 150000));
        cart = new Cart(List.of());
    }
}
