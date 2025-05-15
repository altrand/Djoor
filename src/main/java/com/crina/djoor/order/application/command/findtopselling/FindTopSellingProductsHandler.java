package com.crina.djoor.order.application.command.findtopselling;

import com.crina.djoor.order.domain.OrderItemRepository;
import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.order.domain.vo.TopSellingProduct;
import com.crina.djoor.shared.cqrs.CommandHandler;
import com.crina.djoor.shared.cqrs.TransactionalException;
import com.crina.djoor.shared.http.GenericResponse;

import java.util.List;

public class FindTopSellingProductsHandler implements CommandHandler<FindTopSellingProductsCommand, GenericResponse<FindTopSellingProductsResponse>> {
    private final OrderItemRepository orderItemRepository;

    public FindTopSellingProductsHandler(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public GenericResponse<FindTopSellingProductsResponse> handle(FindTopSellingProductsCommand command) throws TransactionalException {
        GenericResponse<FindTopSellingProductsResponse> generic = new GenericResponse<FindTopSellingProductsResponse>();
        FindTopSellingProductsResponse res = new FindTopSellingProductsResponse();

        try {
            List<TopSellingProduct> topSellingProducts = orderItemRepository.findSoldProductsBetween(command.from, command.to);
            res.isFound = true;
            res.products = topSellingProducts;
            res.message = "Found " + topSellingProducts.size() + " top selling products";
        } catch (RuntimeException e) {
            res.message = e.getMessage();
            generic.response = res;
            return generic;
        }

        generic.response = res;
        return generic;
    }
}
