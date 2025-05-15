package com.crina.djoor.order.domain;

import com.crina.djoor.order.domain.exceptions.ErrorOnSaveOrderException;
import com.crina.djoor.order.domain.vo.TopSellingProduct;

import java.util.Date;
import java.util.List;

public interface OrderItemRepository {
    void add(OrderItem orderItem) throws ErrorOnSaveOrderException;

    void addOrder(Order order) throws ErrorOnSaveOrderException;

    List<TopSellingProduct> findSoldProductsBetween(Date from, Date to);
}
