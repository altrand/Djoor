package com.crina.djoor.user.infrastructure.repository;

import com.crina.djoor.user.domain.User;
import com.crina.djoor.user.domain.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
public class InMemoryUserRepository implements UserRepository {

    public HashMap<String, User> users = new HashMap<>();

    @Override
    public void add(User user) {

        users.put(user.id(), user);
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
