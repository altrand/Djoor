package com.crina.djoor.shared.domain;

import com.crina.djoor.shared.events.DomainEvent;
import com.crina.djoor.shared.events.DomainEventPublisher;

public abstract class AggregateRoot {
    protected void publish(DomainEvent ev) {
        DomainEventPublisher.publish(ev);
    }
}
