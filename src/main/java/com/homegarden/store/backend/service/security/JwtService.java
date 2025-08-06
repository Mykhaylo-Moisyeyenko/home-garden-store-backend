package com.homegarden.store.backend.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.signing.key}")
    private String jwtSecret;

    public String generateJwtToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("login", user.getUsername());
        claims.put("roles", user.getAuthorities());

        return generateJwtToken(user, claims);
    }

    public String generateJwtToken(UserDetails user, Map<String, Object> claims) {
        return Jwts.builder()
                .claims()
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)))
                .add(claims)
                .and()
                .signWith(getJwtSecret())
                .compact();
    }

    private SecretKey getJwtSecret() {
        byte[] decode = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public boolean isValidToken(String token) {
        return new Date().before(extractExpiration(token));
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaim(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaim(String jwt) {
        return Jwts.parser()
                .setSigningKey(getJwtSecret())
                .build()
                .parseSignedClaims(jwt).getPayload();
    }
}