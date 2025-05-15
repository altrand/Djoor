package com.crina.djoor.user.infrastructure.config;

import com.crina.djoor.shared.config.BaseConfig;
import com.crina.djoor.user.domain.UserRepository;
import com.crina.djoor.user.infrastructure.repository.InMemoryUserRepository;
import com.crina.djoor.user.infrastructure.repository.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig extends BaseConfig {
    @Bean
    public UserRepository userRepository(
            InMemoryUserRepository inMemory,
            JpaUserRepository jpa
    ) {
        if ("test".equalsIgnoreCase(env)) {return inMemory;}
        return jpa;
    }
}
