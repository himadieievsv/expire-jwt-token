package com.jwtdemo.application.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JwsServiceTest {

    private JwsService jwsService;

    @Before
    public void setUp() {
        jwsService = new JwsService();
    }

    @Test
    public void testParse() throws Exception {
        ECKey ecKey = generateKey();
        SignedJWT jwt = getSignedToken(ecKey, getClimeSet());
        SignedJWT actualJwt = jwsService.parse(jwt.serialize());

        assertThat(jwt.getJWTClaimsSet().getClaims())
                .isEqualTo(actualJwt.getJWTClaimsSet().getClaims());
    }

    @Test
    public void testValidate() throws Exception {
        ECKey ecKey = generateKey();
        SignedJWT jwt = getSignedToken(ecKey, getClimeSet());
        boolean isValid = jwsService.validate(jwt.serialize(), ecKey);

        assertThat(isValid).isTrue();
    }

    @Test
    public void testValidateFails() throws Exception {
        ECKey ecKey = generateKey();
        JWTClaimsSet climeSet = new JWTClaimsSet.Builder()
                .expirationTime(new Date(Instant.now().minusSeconds(60).toEpochMilli()))
                .build();
        SignedJWT jwt = getSignedToken(ecKey, climeSet);
        boolean isValid = jwsService.validate(jwt.serialize(), ecKey);

        assertThat(isValid).isFalse();
    }

    @Test
    public void testSign() throws Exception {
        ECKey ecKey = generateKey();
        SignedJWT jwt = jwsService.sign(getClimeSet(), ecKey);
        JWSVerifier jwsVerifier = new DefaultJWSVerifierFactory().createJWSVerifier(jwt.getHeader(), ecKey.toECPublicKey());

        assertThat(jwt.verify(jwsVerifier)).isTrue();
    }

    private ECKey generateKey() throws Exception {
        return (new ECKeyGenerator(Curve.P_256)).generate();
    }

    private SignedJWT getSignedToken(ECKey ecKey, JWTClaimsSet claimsSet) throws Exception {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(ecKey.getKeyID()).build();
        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        jwt.sign(new ECDSASigner(ecKey));
        return jwt;
    }

    private JWTClaimsSet getClimeSet() {
        return new JWTClaimsSet.Builder()
                .claim("claim1", "claim value")
                .build();
    }
}
