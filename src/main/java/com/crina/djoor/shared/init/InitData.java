package com.crina.djoor.shared.init;

import com.crina.djoor.product.domain.Product;
import com.crina.djoor.product.domain.ProductRepository;
import com.crina.djoor.shared.vo.Id;
import com.crina.djoor.user.domain.User;
import com.crina.djoor.user.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        return args -> {

            User user = User.create(new Id(UUID.randomUUID().toString()), "690677745");
            userRepository.add(user);

            Product product1 = Product.create(new Id(UUID.randomUUID().toString()), "Laptop", 300000, 100);
            Product product2 = Product.create(new Id(UUID.randomUUID().toString()), "Mouse", 5000, 100);
            productRepository.add(product1);
            productRepository.add(product2);
        };
    }

}
