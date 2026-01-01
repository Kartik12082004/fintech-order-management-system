package com.kartik.Trading.exception;

public class InsufficientBalanceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InsufficientBalanceException() {
        super("Insufficient balance in wallet");
    }

	public InsufficientBalanceException(String message) {
		super(message);
	}
}
