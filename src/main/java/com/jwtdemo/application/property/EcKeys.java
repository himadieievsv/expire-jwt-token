package com.jwtdemo.application.property;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.util.Base64URL;
import org.springframework.stereotype.Component;

@Component
public class EcKeys {

    public ECKey getPrivateKey() {
        return new ECKey.Builder(Curve.P_256,
                new Base64URL("uqm4BesTXKfcdjOAYKL78MizNdVOjQEpQVwn5xrKJco"),
                new Base64URL("FcO1U6DL8istqr1pLKfOyBAeDAcF88jdmYoK2nYz6O0"))
                .keyUse(KeyUse.SIGNATURE)
                .keyID("60f9f9e0-17f4-11ea-8d71-362b9e155667")
                .d(new Base64URL("aX00h0ikq1gJZIfVLdltiDYx6y7ss-BiddHMKBlsr9E"))
                .build();
    }

    public ECKey getPublicKey() {
        return getPrivateKey().toPublicJWK();
    }
}
