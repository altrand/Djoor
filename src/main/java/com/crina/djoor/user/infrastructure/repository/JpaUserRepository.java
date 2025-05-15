package com.crina.djoor.user.infrastructure.repository;


import com.crina.djoor.user.domain.User;
import com.crina.djoor.user.domain.UserRepository;
import com.crina.djoor.user.infrastructure.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaUserRepository implements UserRepository {
    private final SpringDataUserRepository springDataUserRepository;

    public JpaUserRepository(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public void add(User user) {

        springDataUserRepository.save(UserEntity.createFromDomain(user));
    }

    @Override
    public Optional<User> ofId(String userId) {
        Optional<UserEntity> userEntity = springDataUserRepository.findById(userId);
        return Optional.ofNullable(userEntity.get().toDomain());
    }

    @Override
    public boolean ofIdExist(String userId) {
        return false;
    }
}
