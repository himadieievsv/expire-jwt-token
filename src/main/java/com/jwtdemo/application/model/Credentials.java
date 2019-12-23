package com.jwtdemo.application.model;

import lombok.Data;

@Data
public class Credentials {
    private String username;
    private String secret;
}
