package com.pantrychef.backend.services;

import com.pantrychef.backend.secret;
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

/**
 * Provides business logic for dealing with jwt tokens
 */
@Service
@RequiredArgsConstructor
public class JWTService {
    private static final String SECRET_KEY = secret.getSecretKey(); //TODO

    /**
     * Gets a jwt token from an authentication bearer token header
     * @param authHeader The header
     * @return The jwt token
     */
    public String extractJWTToken(String authHeader) {
        String authHeaderPrefix = "Bearer ";
        return authHeader.substring(authHeaderPrefix.length());
    }

    /**
     * Gets the username from a jwt token
     * @param jWTToken The token
     * @return The username
     */
    public String extractUsername(String jWTToken) {
        return extractClaim(jWTToken, Claims::getSubject);
    }

    /**
     * Extracts a claim from the token
     * @param jWTToken The token
     * @param claimsResolver A resolver for a claim
     * @return The claim
     * @param <T> The type
     */
    public <T> T extractClaim(String jWTToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jWTToken);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a jwt token for a given user
     * @param userDetails The user
     * @return The token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a token for a user given certain claims
     * @param extraClaims The extra claims
     * @param userDetails The user
     * @return The jwt token
     */
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

    /**
     * Determines if a jwt token represents the given user
     * @param jWTToken The token
     * @param userDetails The user
     * @return True if the token belongs to the user, false otherwise
     */
    public boolean isTokenValid(String jWTToken, UserDetails userDetails) {
        String username = extractUsername(jWTToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jWTToken);
    }

    /**
     * Determines if a jwt token is expired
     * @param jWTToken The token
     * @return True if expired, false otherwise
     */
    private boolean isTokenExpired(String jWTToken) {
        return extractExpiration(jWTToken).before(new Date());
    }

    /**
     * Gets the expiration date from a jwt token
     * @param jWTToken The token
     * @return The date of expiration
     */
    private Date extractExpiration(String jWTToken) {
        return extractClaim(jWTToken, Claims::getExpiration);
    }

    /**
     * Get all the claims from a jwt token
     * @param jWTToken The token
     * @return The claims
     */
    private Claims extractAllClaims(String jWTToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jWTToken)
                .getBody();
    }

    /**
     * Get a signing key
     * @return The key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
