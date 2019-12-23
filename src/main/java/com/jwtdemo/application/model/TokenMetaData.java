package com.jwtdemo.application.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class TokenMetaData {
    @NonNull
    private String userName;
    @NonNull
    private String salt;
}
