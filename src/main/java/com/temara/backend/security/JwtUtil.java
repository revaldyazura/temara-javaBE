package com.temara.backend.security;

import java.io.Serializable;
import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Annotates the class as a Spring-managed component, making it available for dependency injection.
@Component
public class JwtUtil implements Serializable {

  // Unique identifier for the class, ensuring compatibility between different versions of this serialized class.
  private static final long serialVersionUID = -2550185165626007488L;

  // Sets token validity to 5 hours (in seconds)
  public static final long jwtTokenValidity = 5 * 60 * 60;

  // Injects the JWT secret from application properties.
  @Value("${jwt.secret}")
  private String secret;

  // Extracts the username (subject) from the token
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject); // Uses getClaimFromToken to get the subject
  }

  // Extracts the expiration date of the token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration); // Uses getClaimFromToken to get expiration
  }
  // Extracts a specific claim from the token using a claims resolver function
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token); // Retrieves all claims
    return claimsResolver.apply(claims); // Applies the provided function to extract specific claim
  }
  
  // Parses all claims (payload data) from the given token
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder() // Builds a JWT parser
    .setSigningKey(getSigningKey()) // Sets the signing key for verification
    .build()
    .parseClaimsJws(token) // Parses the token and extracts claims if valid
    .getBody(); // Returns the claims part of the token
  }

  // Decodes the base64-encoded JWT secret and returns it as an HMAC-SHA signing key.
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);  // Decodes the base64 secret key
    return Keys.hmacShaKeyFor(keyBytes);  // Generates the signing key
  }
  
    private Boolean isTokenExpired(String token) {
      final Date expiration = getExpirationDateFromToken(token); // Gets the token expiration date
      return expiration.before(new Date()); // Returns true if expiration date is before current date
    }
  // Generates a token for the given user
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();  // Initializes an empty claims map
    return doGenerateToken(claims, userDetails.getUsername());  // Calls doGenerateToken with claims and username
  }
  // Creates a JWT token with claims and a subject (username) and signs it
  private String doGenerateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()  // Starts building the JWT
      .setClaims(claims)  // Adds custom claims
      .setSubject(subject)  // Sets the subject (username)
      .setIssuedAt(new Date(System.currentTimeMillis()))  // Sets the token issuance time
      .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))  // Sets expiration
      .signWith(getSigningKey(), SignatureAlgorithm.HS512)  // Signs with HMAC-SHA512
      .compact();  // Compacts the token into a string
  }

  // Validates the token by checking if username matches and token is not expired
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);  // Gets the username from token
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));  // Valid if username matches and not expired
  }
}
