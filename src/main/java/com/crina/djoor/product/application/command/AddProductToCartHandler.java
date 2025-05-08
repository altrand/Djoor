package com.crina.djoor.product.application.command;

import com.crina.djoor.order.domain.Order;
import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.order.domain.vo.CartSummary;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderRepository;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.product.infrastructure.repository.InMemoryCartRepository;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.http.GenericResponse;

public class AddProductToCartHandler implements CommandHandler<AddProductCommand, GenericResponse<AddProductResponse>> {
    private final ProductRepository productRepository;
    private final InMemoryCartRepository cartStore;
    private final OrderRepository orderRepository;

    public AddProductToCartHandler(ProductRepository productRepository, InMemoryCartRepository cartStore, InMemoryOrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.cartStore = cartStore;
        this.orderRepository = orderRepository;
    }

    @Override
    public GenericResponse<AddProductResponse> handle(AddProductCommand command) throws TransactionalException {
        var generic = new GenericResponse<AddProductResponse>();
        AddProductResponse res = new AddProductResponse();

        /*Order order = orderRepository.findById(command.commandId);
        Product product = productRepository.findById(command.productId);

        Cart cart = Order.generateCart();
        cart = Order.addProductToCart(cart, product, command.quantity);

        CartSummary cartSummary = Order.generateSummary();

        //Cart currentCart = cartStore.loadCart(command.userId);

        //Cart updatedCart = currentCart.addProduct(product, command.quantity);

        //cartStore.saveCart(command.userId, updatedCart);

        CartSummary cartSummary = updatedCart.generateSummary();

        res.isAdded = true;
        res.quantityProduct = cartSummary.getQuantityProducts();
        res.numberProductSelected = cartSummary.getNumberProductsSelected();
        res.totalPrice = cartSummary.getTotalPrice();
        res.cartItemSummaries = cartSummary.getCartItemSummary();*/
        generic.response = res;
        return generic;
    }
}
