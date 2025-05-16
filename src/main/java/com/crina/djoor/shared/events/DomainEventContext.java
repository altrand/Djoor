package com.crina.djoor.shared.events;

import java.util.ArrayList;
import java.util.List;

public class DomainEventContext {
    private static final ThreadLocal<List<DomainEvent>> EVENTS =
            ThreadLocal.withInitial(ArrayList::new);

    private DomainEventContext() {}

    public static void registerEvent(DomainEvent event) {
        EVENTS.get().add(event);
    }

    public static List<DomainEvent> pullEvents() {
        List<DomainEvent> events = List.copyOf(EVENTS.get());
        EVENTS.get().clear();
        return events;
    }
}
