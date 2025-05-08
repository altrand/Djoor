package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;

public interface OrderItemRepository {
    void add(OrderItem orderItem) throws ErrorOnSaveOrderException;
}
