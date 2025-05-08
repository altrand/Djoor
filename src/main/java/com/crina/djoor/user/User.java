package com.crina.djoor.user;

import com.crina.djoor.shared.vo.Id;

public class User {
    private Id id;
    private String phoneNumber;

    public User(Id id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public static User create(Id id, String phoneNumber) {
        return new User(id, phoneNumber);
    }

    public String id() {
        return id.value();
    }
}
