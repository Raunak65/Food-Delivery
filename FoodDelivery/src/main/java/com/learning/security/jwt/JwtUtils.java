package com.learning.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.learning.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${foodDelivery.app.jwtSecret}")
	private String jwtSecret;
	@Value("${foodDelivery.app.jwtExpirationMs}")
	private int jwtExpiryValue;
	
	public String generateToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder().setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpiryValue))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getUsernameFromJwtToken(String authToken) {
		return Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(authToken)
				.getBody().getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			LOGGER.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			LOGGER.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			LOGGER.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOGGER.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

}
