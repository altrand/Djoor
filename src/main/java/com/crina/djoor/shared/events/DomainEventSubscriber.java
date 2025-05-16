package com.crina.djoor.shared.events;

public interface DomainEventSubscriber<E extends DomainEvent> {
    boolean isSubscribeTo(DomainEvent event);
    void handle(E event);
}
