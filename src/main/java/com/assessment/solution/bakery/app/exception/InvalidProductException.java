package com.assessment.solution.bakery.app.exception;

public class InvalidProductException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidProductException(String message) {
		super(message);
	}
}
