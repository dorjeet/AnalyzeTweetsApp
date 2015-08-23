package com.dorjee.model;

public class Comment {
	private String email;
	private String description;
	private String tweetId;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getTweetId() {
		return tweetId;
	}
	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("email : ").append(email);
		sb.append(" description : ").append(description);
		sb.append("tweetId = ").append(tweetId);
		return sb.toString();
	}
}
