package com.jwtdemo.application.service;

import com.jwtdemo.application.exception.ServiceException;
import com.jwtdemo.application.exception.TokenNotValidException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSHeader.Builder;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
class JwsService {

    @NonNull
    SignedJWT parse(@NonNull String token) {
        try {
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new TokenNotValidException("Can't parse token!", e);
        }
    }

    @NonNull
    boolean validate(@NonNull String token, @NonNull ECKey key) {
        SignedJWT signedJWT = parse(token);
        DefaultJWTClaimsVerifier claimsVerifier = new DefaultJWTClaimsVerifier();
        try {
            JWSVerifier jwsVerifier = new DefaultJWSVerifierFactory().createJWSVerifier(signedJWT.getHeader(), key.toECPublicKey());
            claimsVerifier.verify(signedJWT.getJWTClaimsSet());
            return signedJWT.verify(jwsVerifier);
        } catch (JOSEException | ParseException | BadJWTException e) {
            return false;
        }
    }

    @NonNull
    SignedJWT sign(JWTClaimsSet claimsSet, ECKey key) {
        JWSHeader jwsHeader = new Builder(JWSAlgorithm.ES256).keyID(
                key.getKeyID()).build();
        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            jwt.sign(new ECDSASigner(key));
        } catch (JOSEException e) {
            throw new ServiceException("Token signing failed!", e);
        }
        return jwt;
    }
}
