package com.kartik.Trading.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final String SECRET_KEY = "sscTqlbxWhnKEAJOEH_L5um2dxWwmQ_j8JIOnaO2GN5tdOLao1LUj5IwWh0zMWDi";
	private final long EXPIRATION_TIME = 1000 * 60 * 60;
	
	private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY));
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS256)
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
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
}
