package com.jwtdemo.domain.user;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UserTest {

    @Test
    public void testChangeTokenSalt() {
        User user = new User();
        user.changeTokenSalt();
        String tokenSalt1 = user.getTokenSalt();
        user.changeTokenSalt();
        String tokenSalt2 = user.getTokenSalt();

        assertThat(tokenSalt2).isNotEqualTo(tokenSalt1)
                .hasSize(8);
    }
}
