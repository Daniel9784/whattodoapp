package com.whattodo.whattodoapp.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // The secret key used for signing the JWT tokens, from application properties
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKey key;

    // This method is called after the bean is constructed to initialize the secret key
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token using the given username and custom claims.
     *
     * ⚠ WARNING: This method should only be used internally with trusted inputs.
     * Do NOT pass user-provided claims directly, as it may lead to security vulnerabilities.
     * Always validate and sanitize any custom claims before including them in the token.
     *
     * @param username the username to include as the subject of the token
     * @param claims additional claims to include in the token (e.g., roles)
     * @return a signed JWT token
     */
    public String generateToken(String username, Map<String, Object> claims) {
        long jwtExpirationMs = 43200000; // 12 hours
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * Overloaded convenience method to generate a JWT token directly from a UserDetails object.
     * This simplifies token creation by avoiding manual claims construction.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(
                "roles", userDetails.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .toList()
        );
        return generateToken(userDetails.getUsername(), claims);
    }

    // Extracts the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }

    // Extracts a specific claim from the JWT token using a claims resolver function
    public <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver) {
        final io.jsonwebtoken.Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    // Checks if the JWT token is valid for the given username
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Checks if the JWT token is expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, claims -> claims.getExpiration()).before(new Date());
    }

    // Extracts the roles from the JWT token
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> (List<String>) claims.get("roles"));
    }
}
