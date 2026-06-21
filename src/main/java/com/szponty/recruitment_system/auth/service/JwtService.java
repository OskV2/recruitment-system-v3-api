package com.szponty.recruitment_system.auth.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.szponty.recruitment_system.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;

@Service
public class JwtService {

    //  this class is fully vibe-coded

    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes
    ) {
        SecretKey secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationMinutes * 60))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }
}