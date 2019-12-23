package com.jwtdemo.domain.user;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserFactoryTest {

    @Test
    public void testCreateUser() {
        String name = "user";
        UserFactory userFactory = new UserFactory();

        User user = userFactory.createUser(name);

        assertThat(user.getName()).isNotBlank()
                .isEqualTo(name);
        assertThat(user.getTokenSalt()).isNotBlank();
        assertThat(user.getSecret()).isNotBlank()
                .hasSize(12);
    }
}
