package com.kartik.Trading.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -6249469750151183410L;

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
	
}
