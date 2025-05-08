package com.crina.djoor.product.unit;

import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.product.application.command.AddProductCommand;
import com.crina.djoor.product.application.command.AddProductToCartHandler;
import com.crina.djoor.product.infrastructure.repository.InMemoryCartRepository;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.order.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryUserRepository;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@SpringBootTest
public class AddProductToCartTest {
    private InMemoryUserRepository userRepository;
    private InMemoryProductRepository productRepository;


    @BeforeEach
    void setUp() {
        this.userRepository = new InMemoryUserRepository();
        this.productRepository = new InMemoryProductRepository();
        this.buildSUT();
    }

    @Test
    void shouldCanAddProductToCart() {
        Cart cart = new Cart(new ArrayList<>());
        Product product = new Product(new Id("002"), "Stove", 15000, 100);

        cart = cart.addProduct(product.snapshot(), 1);
        assertEquals(1, cart.cartItems().size());
        assertEquals(5000, cart.cartItems().get(0).product().price());

        AddProductCommand command = new AddProductCommand("001", "p1", 2);
        InMemoryCartRepository cartStore = new InMemoryCartRepository();
        /*AddProductToCartHandler service = new AddProductToCartHandler(productRepository, cartStore);

        service.handle(command);

        //Cart updatedCart = cartStore.loadCart("001");
        assertEquals(1, updatedCart.getItems().size());
        assertEquals(2, updatedCart.getItems().get(0).getQuantity());
        assertEquals("T-shirt", updatedCart.getItems().get(0).getProduct().getName());*/

    }

    @Test
    void ShouldCanAddProductToCartOrUpdateQuantityIfAlreadyPresent() {

        AddProductCommand command1 = new AddProductCommand("001", "012", 2);
        InMemoryCartRepository cartStore = new InMemoryCartRepository();
        /*AddProductToCartHandler service = new AddProductToCartHandler(productRepository, cartStore);

        service.handle(command1);
        Cart updatedCart = cartStore.loadCart("001");

        assertEquals(1, updatedCart.getItems().size());
        assertEquals(1, updatedCart.getItems().get(0).getQuantity());


        AddProductCommand command2 = new AddProductCommand("001","012", 3);
        service.handle(command2);
        updatedCart = cartStore.loadCart("001");

        assertEquals(1, updatedCart.getItems().size());
        assertEquals(4, updatedCart.getItems().get(0).getQuantity());
        assertEquals("Mouse", updatedCart.getItems().get(0).getProduct().getName());*/
    }

    @Test
    void shouldCanAddProductFromBaseListToCart() {
        /*Product product = productRepository.findById("001");
        cart = cart.addProduct(product,1);
        assertEquals(1, cart.getItems().size());
        assertEquals("Fridge", cart.getItems().get(0).getProduct().getName());*/
    }

    @Test
    void shouldCanSearchProductByNameToCart() {
        Product product = productRepository.findByName("Fridge");
       /* cart = cart.addProduct(product, 1);
        assertEquals(1, cart.getItems().size());
        assertEquals("Fridge", cart.getItems().get(0).getProduct().getName());*/
    }

    @Test
    void shouldCanReturnAccurateSummaryWhenProductAdded() {

        AddProductCommand command1 = new AddProductCommand("001", "010", 1);
        AddProductCommand command2 = new AddProductCommand("001", "011", 2);
        InMemoryCartRepository cartStore = new InMemoryCartRepository();
        /*AddProductToCartHandler service = new AddProductToCartHandler(productRepository, cartStore);

        service.handle(command1);
        service.handle(command2);

        Cart updatedCart = cartStore.loadCart("001");

        CartSummary summary = updatedCart.generateSummary();

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
        assertEquals(150000, summary.getCartItemSummary().get(1).getTotalPrice());*/
    }

    void buildSUT() {
        User user = User.create(new Id("001"), "678747777");
        this.userRepository.users.put(user.id(), user);
        this.productRepository.products.add(new Product(new Id("001"), "Fridge", 100000, 100));
        this.productRepository.products.add(new Product(new Id("002"), "Stove", 15000, 100));
        this.productRepository.products.add(new Product(new Id("p1"), "T-shirt", 5000, 100));
        this.productRepository.products.add(new Product(new Id("010"), "Laptop", 100000, 100));
        this.productRepository.products.add(new Product(new Id("011"), "Phone", 75000, 100));
        this.productRepository.products.add(new Product(new Id("012"), "Mouse", 5000, 100));
    }
}
