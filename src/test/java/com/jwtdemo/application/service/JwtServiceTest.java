package com.jwtdemo.application.service;

import com.jwtdemo.application.property.EcKeys;
import com.jwtdemo.application.property.JwtProperties;
import com.jwtdemo.domain.user.User;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static com.jwtdemo.application.service.JwtService.SALT_CLAIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ConfigurationPropertiesAutoConfiguration.class,
        EcKeys.class,
        JwtProperties.class,
        JwsService.class,
        JwtService.class
})
public class JwtServiceTest {

    @MockBean
    private Clock clock;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtService jwtService;

    @Before
    public void setUp() {
        given(clock.instant()).willReturn(Instant.parse("2019-01-11T12:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());
    }

    @Test
    public void testIssueAccessToken() throws ParseException {
        User user = new User();
        user.setName("user");
        String accessToken = jwtService.issueAccessToken(user);

        SignedJWT signedJWT = SignedJWT.parse(accessToken);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        Date expectedExpiration = Date.from(clock.instant().plusSeconds(
                jwtProperties.getTokenExpiration().toSeconds()));

        assertThat(jwtClaimsSet.getSubject()).isEqualTo(user.getName());
        assertThat(jwtClaimsSet.getIssuer()).isEqualTo(jwtProperties.getIssuer());
        assertThat(jwtClaimsSet.getExpirationTime()).isEqualTo(expectedExpiration);
        assertThat(jwtClaimsSet.getIssueTime()).isEqualTo(Date.from(clock.instant()));
        assertThat(jwtClaimsSet.getClaim(SALT_CLAIM)).isEqualTo(user.getTokenSalt());
    }
}
