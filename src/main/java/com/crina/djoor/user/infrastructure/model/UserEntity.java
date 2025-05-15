package com.crina.djoor.user.infrastructure.model;

import com.crina.djoor.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    public UserEntity() {
    }

    public static UserEntity createFromDomain(User user) {
        UserEntity u = new UserEntity();
        u.id = user.id();
        u.phoneNumber = user.phoneNumber();
        return u;
    }

    public User toDomain() {
        return User.createFromDB(
                new com.crina.djoor.shared.vo.Id(id),
                phoneNumber
        );
    }
}
