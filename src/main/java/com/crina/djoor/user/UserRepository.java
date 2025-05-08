package com.crina.djoor.user;

import java.util.Optional;

public interface UserRepository {
    void add(User user);
    Optional<User> ofId(String userId);
    boolean ofIdExist(String userId);
}
