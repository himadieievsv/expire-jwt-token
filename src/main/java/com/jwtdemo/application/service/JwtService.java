package com.jwtdemo.application.service;

import com.jwtdemo.application.exception.TokenNotValidException;
import com.jwtdemo.application.model.TokenMetaData;
import com.jwtdemo.application.property.EcKeys;
import com.jwtdemo.application.property.JwtProperties;
import com.jwtdemo.domain.user.User;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {

    static final String SALT_CLAIM = "slt";

    private final Clock clock;
    private final JwsService jwsService;
    private final JwtProperties jwtProperties;
    private final EcKeys ecKeys;

    public String issueAccessToken(User user) {
        Instant issuedAt = Instant.now(clock);
        Instant expiringAt = issuedAt.plusSeconds(jwtProperties.getTokenExpiration().toSeconds());
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getName())
                .expirationTime(Date.from(expiringAt))
                .issueTime(Date.from(issuedAt))
                .claim(SALT_CLAIM, user.getTokenSalt())
                .build();

        SignedJWT signedJWT = jwsService.sign(claimsSet, ecKeys.getPrivateKey());
        return signedJWT.serialize();
    }

    public TokenMetaData retrieveMetaData(String token) {
        if (!jwsService.validate(token, ecKeys.getPublicKey())) {
            throw new TokenNotValidException("Provided token is not valid!");
        }
        SignedJWT signedJWT = jwsService.parse(token);
        JWTClaimsSet jwtClaimsSet;
        try {
            jwtClaimsSet = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new TokenNotValidException("Can't obtain claim set!");
        }
        return TokenMetaData.builder()
                .userName(jwtClaimsSet.getSubject())
                .salt((String) jwtClaimsSet.getClaim(SALT_CLAIM))
                .build();
    }
}
