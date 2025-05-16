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
import com.crina.djoor.order.domain.OrderItem;
import com.crina.djoor.order.domain.PromoCode;
import com.crina.djoor.order.domain.SaleCampaign;
import com.crina.djoor.order.domain.enums.DeliveryMethod;
import com.crina.djoor.order.domain.enums.GiftType;
import com.crina.djoor.order.domain.enums.OrderState;
import com.crina.djoor.order.domain.enums.PaymentMethod;
import com.crina.djoor.order.domain.vo.Cart;
import com.crina.djoor.order.domain.vo.GiftOptions;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryPromoCodeRepository;
import com.crina.djoor.order.infrastructure.repository.InMemorySaleCampaignRepository;
import com.crina.djoor.product.domain.FixedFivePercentDiscountPolicy;
import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductDiscountPolicy;
import com.crina.djoor.product.domain.snapshot.ProductSnapshot;
import com.crina.djoor.product.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.http.GenericResponse;
import com.crina.djoor.shared.vo.Address;
import com.crina.djoor.shared.vo.Amount;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.domain.User;
import com.crina.djoor.user.infrastructure.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class CreateOrderTest {
    private InMemoryOrderRepository orderRepository;
    private InMemoryUserRepository userRepository;
    private InMemoryProductRepository productRepository;
    private InMemoryPromoCodeRepository promoCodeRepository;
    private InMemorySaleCampaignRepository saleCampaignRepository;

    @BeforeEach
    void setUp() {
        this.orderRepository = new InMemoryOrderRepository();
        this.userRepository = new InMemoryUserRepository();
        this.productRepository = new InMemoryProductRepository();
        this.promoCodeRepository = new InMemoryPromoCodeRepository();
        this.saleCampaignRepository = new InMemorySaleCampaignRepository();
        this.buildSUT();
    }

    @Test
    void shouldCanCreateOrder() {
        var giftOptions = new GiftOptions(false, null, null);
        CreateOrderCommand command = new CreateOrderCommand(
                "001",
                "001",
                new OrderProductCommand("011", 2),
                DeliveryMethod.STORE_PICKUP, giftOptions,
                null,
                null,
                null);

        var res = this.createOrderHandle(command);
        Order order = this.orderRepository.orders.get("001");
        assertTrue(res.response.isCreated);
        assertEquals("001", order.snapshot().id());
        assertEquals(150000.0, order.snapshot().amount());
    }

    @Test
    void shouldAddProductToExistingInitiatedOrderOrCreateNewOne() {

        String userId = "user-001";
        String orderId = "order-001";
        String productId1 = "product-001";
        String productId2 = "product-002";


        Product product1 = Product.create(new Id(productId1), "Casque", 50000, 100);
        Product product2 = Product.create(new Id(productId2), "Souris", 5000, 100);

        productRepository.add(product1);
        productRepository.add(product2);
        // Cas 1 : pas de commande initiée → on crée une nouvelle commande
        var giftOptions = new GiftOptions(false, null, null);
        CreateOrderCommand command1 = new CreateOrderCommand(orderId, userId, new OrderProductCommand(productId1, 2), DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        var response1 = this.createOrderHandle(command1);

        assertTrue(response1.response.isCreated);
        Order order1 = orderRepository.orders.get(orderId); // suppose une implémentation en mémoire
        assertNotNull(order1);
        assertEquals(1, order1.items().size());
        assertEquals(100000, order1.snapshot().amount());
        // Cas 2 : une commande INITIATED existe → on ajoute un produit à la même commande

        CreateOrderCommand command2 = new CreateOrderCommand("unused-id", userId, new OrderProductCommand(productId2, 1), DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        var response2 = createOrderHandle(command2);
        assertTrue(response2.response.isCreated);

        // L'ID de la commande reste celui d'origine
        assertEquals(orderId, order1.snapshot().id());
        //assertEquals(1, orderRepository.findInitiatedByUserId(userId).get().);

        Order updatedOrder = orderRepository.orders.get(orderId);
        List<Order> initiatedOrders = orderRepository.findAllInitiatedByUserId(userId);
        assertEquals(1, initiatedOrders.size());
        assertEquals(2, updatedOrder.items().size());
        assertEquals(3, updatedOrder.snapshot().cartSummary().quantityProduct()); // 2 + 1
        assertEquals(105000, updatedOrder.snapshot().amount());
    }


    @Test
    public void shouldValidateOrder() {
        Id orderId = new Id("001");
        Product product = Product.create(new Id("P001"), "Ordinateur", 150000.0, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2, DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
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
        Product product = Product.create(new Id("P001"), "Ordinateur", 150000.0, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2, DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        order.confirm();
        orderRepository.add(order);

        PayOrderCommand command = new PayOrderCommand("001", "user-01", PaymentMethod.CRINA_PAY);
        var res = this.payOrderHandle(command);

        Order result = orderRepository.orders.get("001");

        assertTrue(res.response.isPaid);
        assertEquals("001", result.snapshot().id());
        assertEquals(300000.0, result.snapshot().amount());
        assertEquals(OrderState.PAID, result.snapshot().state());
        assertEquals(PaymentMethod.CRINA_PAY, result.snapshot().paymentMethod());
    }

    @Test
    public void shouldCancelOrderWithinTwoHours() {
        Id orderId = new Id("002");
        Product product = Product.create(new Id("P002"), "Ordinateur", 150000.0, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2, DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
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
        Product product = Product.create(new Id("P002"), "Ordinateur", 150000.0, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(orderId, "user-01", product.snapshot(), 2, DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        order.confirm();
        order.pay(PaymentMethod.CRINA_PAY);
        var field = order.getClass().getDeclaredField("createdAt");
        field.setAccessible(true);
        field.set(order, new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000 + 1000)));

        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void shouldAddProductToExistingInitiatedOrderWithoutCreatingNewOne() {
        String userId = "user-001";
        String orderId1 = "order-001";
        String orderId2 = "order-002";
        String productId1 = "product-001";
        String productId2 = "product-002";
        Product product1 = Product.create(new Id(productId1), "Casque", 50000, 100);
        productRepository.add(product1);
        Product product2 = Product.create(new Id(productId2), "Souris", 5000, 100);
        productRepository.add(product2);

        // Crée une commande INITIÉE existante
        var giftOptions = new GiftOptions(false, null, null);
        Order existingOrder = Order.create(new Id(orderId1), userId, product1.snapshot(), 1, DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        orderRepository.add(existingOrder);

        // Prépare la commande avec un nouveau produit
        CreateOrderCommand command = new CreateOrderCommand(orderId2, userId, new OrderProductCommand(productId2, 2), DeliveryMethod.STORE_PICKUP, giftOptions, null, null, null);
        var response = this.createOrderHandle(command);


        List<Order> initiatedOrders = orderRepository.findAllInitiatedByUserId(userId);
        assertEquals(1, initiatedOrders.size());
        Order updatedOrder = initiatedOrders.get(0);

        assertTrue(updatedOrder.snapshot().id().equals(orderId1)); // Vérifie que c'est bien l'ancienne commande
        assertTrue(response.response.isCreated);
        assertEquals(3, updatedOrder.cartSummary().quantityProduct());
        assertEquals(2, updatedOrder.cartSummary().numberProductsSelected());
        assertEquals(60000, updatedOrder.cartSummary().totalPrice());
    }

    @Test
    void shouldApplyHomeDeliveryCost() {
        Product product = Product.create(new Id("P001"), "Mouse", 5000, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(
                new Id("O001"),
                "U001",
                product.snapshot(),
                2,
                DeliveryMethod.HOME_DELIVERY,
                giftOptions,
                null,
                null,
                null
        );

        var snapshot = order.snapshot();
        assertEquals(5000 * 2 + 2000, snapshot.amount()); // 10.000 + 2.000
        assertEquals(DeliveryMethod.HOME_DELIVERY, snapshot.deliveryMethod());
    }

    @Test
    void shouldNotApplyCostForStorePickup() {
        Product product = Product.create(new Id("P001"), "Mouse", 5000, 100);
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(
                new Id("O002"),
                "U001",
                product.snapshot(),
                1,
                DeliveryMethod.STORE_PICKUP,
                giftOptions,
                null,
                null,
                null
        );

        var snapshot = order.snapshot();
        assertEquals(5000, snapshot.amount()); // 5.000 + 0
        assertEquals(DeliveryMethod.STORE_PICKUP, snapshot.deliveryMethod());
    }

    @Test
    void shouldCreateOrderWithSimpleGift() {
        var address = new Address("Tongolo", "Mballa 2", "Cameroun");
        var giftOptions = new GiftOptions(true, address, GiftType.SIMPLE);
        Product product = Product.create(new Id("P001"), "Mouse", 5000, 100);
        var order = Order.create(
                new Id("O001"),
                "user1",
                product.snapshot(),
                1,
                DeliveryMethod.HOME_DELIVERY,
                giftOptions,
                null,
                null,
                null
        );

        assertEquals(5000 + 2000 + 2000, order.snapshot().amount()); // produit + livraison + cadeau
    }

    @Test
    void shouldCreateOrderWithAnniversaireGift() {
        var address = new Address("Tongolo", "Mballa 2", "Cameroun");
        var giftOptions = new GiftOptions(true, address, GiftType.BIRTHDAY);
        Product product = Product.create(new Id("P001"), "Mouse", 5000, 100);
        var order = Order.create(
                new Id("O002"),
                "user2",
                product.snapshot(),
                1,
                DeliveryMethod.STORE_PICKUP,
                giftOptions,
                null,
                null,
                null
        );

        assertEquals(5000 + 0 + 3000, order.snapshot().amount()); // pas de frais de livraison
    }

    @Test
    void shouldApplyDiscountForProductDuringSaleCampaign() {
        //Étape 1 : Création d'une campagne de solde active (30% sur 1 produit)
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date start = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 2); // +1 jour depuis "now"
        Date end = cal.getTime();

        SaleCampaign saleCampaign = new SaleCampaign(start, end, 30, 1); // 30% sur 1 produit max

        // Étape 2 : Création d’un produit
        Product product = Product.create(new Id("P-001"), "Chaussure Homme", 10000, 50);

        // Étape 3 : Activation de la réduction si le produit est éligible
        if (saleCampaign.isActive(now) && saleCampaign.canAddProduct()) {
            saleCampaign.addProduct(product.id().value());
            //product.setInCurrentSale(true);
        }

        // Étape 4 : Création de la commande avec ce produit en solde
        var giftOptions = new GiftOptions(false, null, null);
        Order order = Order.create(new Id("CMD-001"), "User-42", product.snapshot(), 2, DeliveryMethod.STORE_PICKUP, giftOptions, saleCampaign, null, null);

        // Étape 5 : Calcul attendu
        double expectedDiscountedPrice = saleCampaign.applyDiscount(product.snapshot().price()); // 10000 - 30% = 7000
        double expectedTotal = expectedDiscountedPrice * 2; // 2 produits = 14000

        // Étape 6 : Vérification du montant
        assertEquals(expectedTotal, order.snapshot().amount());

    }

    void buildSUT() {
        User user = User.create(new Id("003"), "678747777");
        Product product1 = Product.create(new Id("010"), "Laptop", 100000, 100);
        Product product2 = Product.create(new Id("011"), "Phone", 75000, 50);
        this.productRepository.products.add(product1);
        this.productRepository.products.add(product2);

    }

    @Test
    void shouldApply5PercentGlobalDiscountWhenMoreThan5DifferentProductsWithAtLeast2Each() {
        Cart cart = Cart.create();
        double unitPrice = 100.0;

        // Ajout de 6 produits différents avec chacun 2 unités
        for (int i = 0; i < 6; i++) {
            cart = cart.addProduct(Product.create(new Id("p" + i), "Nido-" + i, unitPrice, 100).snapshot(), 2, null, null);
        }

        // Créer la commande (sans frais de livraison ni cadeau)
        Order order = Order.reconstructFromDatabase(
                new Id(UUID.randomUUID().toString()),
                "user123",
                cart.items().stream()
                        .map(item -> new OrderItem(item.product().id(), item.quantity(), item.product().price()))
                        .toList(),
                DeliveryMethod.STORE_PICKUP,
                null,
                new java.util.Date(),
                null,
                null
        );

        // Montant total sans remise : 6 * 2 * 100 = 1200
        // Remise attendue : 5% de 1200 = 60 -> Total = 1140
        double total = order.snapshot().amount();
        assertEquals(1140.0, total, 0.01);
    }

    @Test
    void shouldApply10PercentGlobalDiscountWhenMoreThan10DifferentProductsWithAtLeast4Each() {
        Cart cart = Cart.create();
        double unitPrice = 50.0;

        // Ajout de 11 produits différents avec chacun 4 unités
        for (int i = 0; i < 11; i++) {
            cart = cart.addProduct(Product.create(new Id("p" + i), "Nescafe-" + i, unitPrice, 100).snapshot(), 4, null, null);
        }

        Order order = Order.reconstructFromDatabase(
                new Id(UUID.randomUUID().toString()),
                "user456",
                cart.items().stream()
                        .map(item -> new OrderItem(item.product().id(), item.quantity(), item.product().price()))
                        .toList(),
                DeliveryMethod.STORE_PICKUP,
                null,
                new java.util.Date(),
                null,
                null
        );

        // Montant total sans remise : 11 * 4 * 50 = 2200
        // Remise attendue : 10% de 2200 = 220 -> Total = 1980
        double total = order.snapshot().amount();
        assertEquals(1980.0, total, 0.01);
    }

    @Test
    void ShouldNotAddMoreThan20UnitsOfProductDuringSale() {
        // Initialiser un produit et une campagne de solde active
        Product product = Product.create(new Id("P001"), "Nescafe", 50.0, 100);
        SaleCampaign saleCampaign = new SaleCampaign(new Date(), new Date(System.currentTimeMillis() + 100000), 50, 100);

        // Créer un panier
        Cart cart = Cart.create();

        // Ajouter 20 unités du produit dans le panier (autorisé)
        cart = cart.addProduct(product.snapshot(), 20, saleCampaign, null);

        // Tenter d'ajouter une 21ème unité (cela doit échouer)
        Cart finalCart = cart;
        assertThrows(IllegalStateException.class, () -> {
            finalCart.addProduct(product.snapshot(), 1, saleCampaign, null);
        });
    }

    @Test
    void shouldCreateOrderWithDiscountedProductOutsideSalePeriod() {
        ProductSnapshot snapshot = new ProductSnapshot("P123", 100.0);
        FixedFivePercentDiscountPolicy discountPolicy = new FixedFivePercentDiscountPolicy(Set.of("P123"));

        Order order = Order.create(
                new Id("ORDER-1"),
                "user-001",
                snapshot,
                1,
                DeliveryMethod.HOME_DELIVERY,
                null,
                null, // pas de campagne de solde
                discountPolicy,
                null
        );

        // then
        assertEquals(95.0 + 2000.0, order.snapshot().amount(), 0.001); // produit à 95 + livraison
    }

    @Test
    void shouldApplyPromoCodeDiscountAndCalculateCommission() {
        ProductSnapshot product = new ProductSnapshot("PROD123", 10000);
        PromoCode promoCode = PromoCode.Create(
                0.1,
                0.05,
                "PR-001",
                "user-010",
                new Date(System.currentTimeMillis() + 1000000),
                true);


        Order order = Order.create(
                new Id("CM-001"),
                "CLT-001",
                product,
                2,
                DeliveryMethod.HOME_DELIVERY,
                null,
                null,
                null,
                promoCode
        );

        assertEquals(19800, order.snapshot().amount(), 0.001);
    }

    private GenericResponse<CreateOrderResponse> createOrderHandle(CreateOrderCommand command) {

        CreateOrderHandler handler = new CreateOrderHandler(this.orderRepository, this.userRepository, this.productRepository, this.promoCodeRepository, this.saleCampaignRepository);

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
