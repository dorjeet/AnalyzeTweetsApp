package com.dorjee.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.dorjee.rest.CustomJsonDateDeserializer;

public class TweetHead {
	private String text;
	
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date createdAt;
	private User user;

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreatedAt() {
		return createdAt;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	

}
