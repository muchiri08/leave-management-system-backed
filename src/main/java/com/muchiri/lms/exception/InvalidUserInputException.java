package com.muchiri.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidUserInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidUserInputException(String eMessage) {
		super(eMessage);
	}

}
