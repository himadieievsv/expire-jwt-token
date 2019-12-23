package com.jwtdemo.application.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "demo.jwt")
@Data
public class JwtProperties {
    private String issuer;
    private Duration tokenExpiration;
}
