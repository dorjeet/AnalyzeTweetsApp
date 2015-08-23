package com.dorjee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Connection to database failed.")
public class DatabaseConnectionException extends AbstractTweetAppException {
	
	private static final long serialVersionUID = 9118494946356544660L;

	public DatabaseConnectionException(String message) {
		super(message);
	}

}
