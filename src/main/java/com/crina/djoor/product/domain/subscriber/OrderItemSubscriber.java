package com.crina.djoor.product.domain.subscriber;


import com.crina.djoor.shared.events.DomainEvent;

public class OrderItemSubscriber implements DomainEventSubscriber<DomainEvent> {

    private final FoodMenuRepository repository;

    public OrderItemSubscriber(FoodMenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isSubscribeTo(DomainEvent event) {
        return event instanceof FoodMenuQuantityDecreased;
    }

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof FoodMenuQuantityDecreased) {
            ((FoodMenuQuantityDecreased) event).foodMenuQuantities.forEach(
                    (foodMenu) -> {
                        var command = new DecreaseQuantityCommand(
                                foodMenu.keySet().stream().findFirst().orElseThrow(),
                                foodMenu.get(foodMenu.keySet().stream().findFirst().orElseThrow())
                        );
                        (new DecreaseQuantityHandler(repository)).handle(command);
                    }
            );
        }
    }

}
