package com.crina.djoor.order.infrastructure.repository;

import com.crina.djoor.user.User;
import com.crina.djoor.user.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
public class InMemoryUserRepository implements UserRepository {

    public HashMap<String, User> users = new HashMap<>();

    @Override
    public void add(User user) {

    }

    @Override
    public Optional<User> ofId(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public boolean ofIdExist(String userId) {
        return false;
    }
}
