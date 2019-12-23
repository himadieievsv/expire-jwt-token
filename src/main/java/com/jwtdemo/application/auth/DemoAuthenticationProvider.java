package com.jwtdemo.application.auth;

import com.jwtdemo.application.model.TokenMetaData;
import com.jwtdemo.application.service.JwtService;
import com.jwtdemo.domain.user.User;
import com.jwtdemo.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class DemoAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof String)) {
            return null;
        }
        String token = (String) authentication.getPrincipal();
        if (token.isBlank()) {
            return null;
        }
        TokenMetaData tokenMetaData = jwtService.retrieveMetaData(token);
        User user = userService.getUser(tokenMetaData.getUserName());
        if (!user.getTokenSalt().equals(tokenMetaData.getSalt())) {
            return null;
        }
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
                user, null);
        authenticationToken.setAuthenticated(true);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
