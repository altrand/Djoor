package com.crina.djoor.order.domain;


import com.crina.djoor.product.domain.Product;

public interface PromoCodeRepository {
    PromoCode ofCode(String code);
}
