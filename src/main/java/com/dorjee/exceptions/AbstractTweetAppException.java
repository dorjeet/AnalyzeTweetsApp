package com.dorjee.exceptions;

public abstract class AbstractTweetAppException extends RuntimeException {
	private String exceptionMessage;
	
	public AbstractTweetAppException(String message) {
		this.exceptionMessage = message;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
}
