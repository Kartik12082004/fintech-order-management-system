package com.kartik.Trading.exception;

public class ConflictException extends RuntimeException {
	
	private static final long serialVersionUID = -7338779959553544298L;

	public ConflictException(String msg) {
		super(msg);
	}
	
}
