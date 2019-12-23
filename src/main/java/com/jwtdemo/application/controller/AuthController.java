package com.jwtdemo.application.controller;


import com.jwtdemo.application.model.Credentials;
import com.jwtdemo.application.service.JwtService;
import com.jwtdemo.domain.user.User;
import com.jwtdemo.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody Credentials credentials) {
        User user = userService.getUser(credentials.getUsername(), credentials.getSecret());
        return jwtService.issueAccessToken(user);
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal User user) {
        userService.changeTokenSalt(user);
        return "OK";
    }
}
