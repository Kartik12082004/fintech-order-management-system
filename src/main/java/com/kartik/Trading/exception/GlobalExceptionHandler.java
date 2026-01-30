package com.kartik.Trading.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        log.warn("Validation failed");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        return ResponseEntity.badRequest().body(errors);
    }
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(
	        ResourceNotFoundException ex) {

	    log.warn("Resource not found: {}", ex.getMessage());

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(Map.of("error", ex.getMessage()));
	}
	
	@ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflict(
            ConflictException ex) {

        log.warn("Conflict: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<Map<String, String>> handleInsufficientBalance(
	        InsufficientBalanceException ex) {

	    log.warn("Insufficient balance attempt");

	    return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body(Map.of("error", "Insufficient wallet balance"));
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, String>> handleBadCredentials(
	        BadCredentialsException ex) {

	    log.warn("Authentication failed: invalid credentials");

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of("error", "Invalid email or password"));
	}

	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        log.error("Database integrity violation", ex);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Resource already exists"));
    }
	
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {

        log.warn("Bad request: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(
            Exception ex) {

        log.error("Unhandled exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }
	
}
