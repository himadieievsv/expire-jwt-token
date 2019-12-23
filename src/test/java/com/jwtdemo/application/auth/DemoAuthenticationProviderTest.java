package com.jwtdemo.application.auth;

import com.jwtdemo.application.model.TokenMetaData;
import com.jwtdemo.application.service.JwtService;

import com.jwtdemo.domain.user.User;
import com.jwtdemo.domain.user.UserService;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DemoAuthenticationProviderTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;

    @InjectMocks
    private DemoAuthenticationProvider authenticationProvider;

    @Test
    public void testAuthenticate() {
        String userName = "User Name";
        User user = new User();
        user.setName(userName);
        String testToken = "test token";
        String salt = user.getTokenSalt();
        TestingAuthenticationToken token = new TestingAuthenticationToken(testToken, null);
        TokenMetaData tokenMetaData = TokenMetaData.builder()
                .userName(userName)
                .salt(salt)
                .build();
        given(jwtService.retrieveMetaData(testToken)).willReturn(tokenMetaData);
        given(userService.getUser(userName)).willReturn(user);
        Authentication authentication = authenticationProvider.authenticate(token);

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(user);
        assertThat(authentication.getCredentials()).isNull();
    }

    @Test
    public void testSaltChanged() {
        String userName = "User Name";
        User user = new User();
        user.setName(userName);
        String testToken = "test token";
        String salt = user.getTokenSalt();
        TestingAuthenticationToken token = new TestingAuthenticationToken(testToken, null);
        TokenMetaData tokenMetaData = TokenMetaData.builder()
                .userName(userName)
                .salt(salt)
                .build();
        user.changeTokenSalt();
        given(jwtService.retrieveMetaData(testToken)).willReturn(tokenMetaData);
        given(userService.getUser(userName)).willReturn(user);
        Authentication authentication = authenticationProvider.authenticate(token);

        assertThat(authentication).isNull();
    }

    @Test
    public void testBlankToken() {
        String testToken = " ";
        TestingAuthenticationToken token = new TestingAuthenticationToken(testToken, null);
        Authentication authentication = authenticationProvider.authenticate(token);
        assertThat(authentication).isNull();
    }

    @Test
    public void testWrongToken() {
        TestingAuthenticationToken token = new TestingAuthenticationToken(new User(), null);
        Authentication authentication = authenticationProvider.authenticate(token);
        assertThat(authentication).isNull();
    }
}
