package com.jwtdemo.domain.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    private String secret;
    @Setter(AccessLevel.PRIVATE)
    private String tokenSalt;

    public User() {
        this.tokenSalt = generateTokenSalt();
    }

    public void changeTokenSalt() {
        this.setTokenSalt(generateTokenSalt());
    }

    private String generateTokenSalt() {
        return RandomStringUtils.random(8, true, true);
    }
}
