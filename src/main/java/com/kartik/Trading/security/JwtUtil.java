package com.kartik.Trading.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secretKeyString;
	
	private final long EXPIRATION_TIME = 1000 * 60 * 60;
	
	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKeyString));
	}	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getKey(), SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	public String extractEmail(String token) {
		return getClaims(token).getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			getClaims(token);
			return true;
		}catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	private Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
}
