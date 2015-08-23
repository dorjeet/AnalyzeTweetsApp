package com.dorjee.model;

public class User {
	private String id;
	private String name;
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	private String screenName;
	private String location;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		if (location != null && location.isEmpty())
			this.location = "not specified";
		else
			this.location = location;
	}
}
