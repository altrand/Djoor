package com.crina.djoor.shared.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DomainEventPublisher {
    private static final List<DomainEventSubscriber<? extends DomainEvent>> SUBSCRIBERS = new CopyOnWriteArrayList<>();

    public static void register(DomainEventSubscriber<? extends DomainEvent> s) {
        SUBSCRIBERS.add(s);
    }

    public static <E extends DomainEvent> void publish(E event) {
        DomainEventContext.registerEvent(event);
        for (DomainEventSubscriber<? extends DomainEvent> subscriber : SUBSCRIBERS) {
           if ( subscriber.isSubscribeTo(event)) {
                ((DomainEventSubscriber<E>) subscriber).handle(event);
            }
        }
    }
}
