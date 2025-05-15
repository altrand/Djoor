package com.crina.djoor.product.infrastructure.config;

import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.product.infrastructure.repository.InMemoryProductRepository;
import com.crina.djoor.product.infrastructure.repository.JpaProductRepository;
import com.crina.djoor.shared.config.BaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig extends BaseConfig {
    @Bean
    public ProductRepository productRepository(
            InMemoryProductRepository inMemory,
            JpaProductRepository jpa
    ) {
        if ("test".equalsIgnoreCase(env)) {
            return inMemory;
        }
        return jpa;
    }
}
