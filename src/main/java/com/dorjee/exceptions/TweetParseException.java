package com.dorjee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error occured while parsing tweets")
public class TweetParseException extends AbstractTweetAppException {
	private static final long serialVersionUID = -8840206109029759064L;

	public TweetParseException(String message) {
		super(message);
	}

}
