package com.jwtdemo.domain.user;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setSecret(RandomStringUtils.random(12, true, true));
        user.changeTokenSalt();
        return user;
    }
}
