package com.crina.djoor.user.infrastructure.repository;

import com.crina.djoor.user.infrastructure.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, String> {
}
