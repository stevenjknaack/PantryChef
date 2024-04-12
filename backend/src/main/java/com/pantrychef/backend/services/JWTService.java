package com.pantrychef.backend.services;

import com.pantrychef.backend.controllers.authentication.secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {
    private static final String SECRET_KEY = secret.getSecretKey(); //TODO

    public String extractJWTToken(String authHeader) {
        String authHeaderPrefix = "Bearer ";
        return authHeader.substring(authHeaderPrefix.length());
    }
    public String extractUsername(String jWTToken) {
        return extractClaim(jWTToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jWTToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jWTToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jWTToken, UserDetails userDetails) {
        String username = extractUsername(jWTToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jWTToken);
    }

    private boolean isTokenExpired(String jWTToken) {
        return extractExpiration(jWTToken).before(new Date());
    }

    private Date extractExpiration(String jWTToken) {
        return extractClaim(jWTToken, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jWTToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jWTToken)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
