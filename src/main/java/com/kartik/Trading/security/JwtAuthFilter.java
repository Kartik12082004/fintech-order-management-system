package com.kartik.Trading.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
	
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	
	public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null) {
            log.debug("No Authorization header present | path={}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
		
		if (!authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format | path={}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
		
		String token = authHeader.substring(7);
		
		try {
			
            if (jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractEmail(token);
                log.debug("JWT validated successfully | user={}", email);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.info(
                        "SecurityContext set | user={} authorities={}",
                        email,
                        userDetails.getAuthorities()
                );
            }
            
        } catch (Exception ex) {
        	
            log.error(
                    "JWT authentication failed | path={} reason={}",
                    request.getRequestURI(),
                    ex.getMessage()
            );
            
        }

		
		filterChain.doFilter(request, response);
		
	}
	
}
