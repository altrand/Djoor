package com.crina.djoor.order.infrastructure.config;

import com.crina.djoor.order.domain.OrderRepository;
import com.crina.djoor.order.infrastructure.repository.InMemoryOrderRepository;
import com.crina.djoor.order.infrastructure.repository.JpaOrderRepository;
import com.crina.djoor.shared.config.BaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig extends BaseConfig {

    @Bean
    public OrderRepository orderRepository(
            InMemoryOrderRepository inMemory,
            JpaOrderRepository jpa
    ) {
        if ("test".equalsIgnoreCase(env)) {
            return inMemory;
        }
        return jpa;
    }

}
