package com.crina.djoor.user.domain;

import com.crina.djoor.shared.vo.Id;

public class User {
    private Id id;
    private String phoneNumber;

    private User(Id id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public static User create(Id id, String phoneNumber) {
        return new User(id, phoneNumber);
    }

    public String id() {

        return id.value();
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public static User createFromDB(
            Id id,
            String phoneNumber
    ) {
        var user =  new User(id, phoneNumber);
        return user;
    }
}
