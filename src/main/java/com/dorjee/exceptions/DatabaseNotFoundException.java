package com.dorjee.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Database not found")
public class DatabaseNotFoundException extends AbstractTweetAppException {
	
	private static final long serialVersionUID = -8785620205842387091L;

	public DatabaseNotFoundException(String dbName) {
		super(dbName + " database does not exist");
	}
}
