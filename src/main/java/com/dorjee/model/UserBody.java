package com.dorjee.model;

import java.net.URL;
import java.util.Date;




import com.dorjee.rest.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UserBody {
	
	private String id;
	private String name;
	private String screenName;
	private String location;
	private String description;
	private URL profileImageUrl;
	private int followersCount;
	private int friendsCount;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date createdAt;

	
	public URL getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(URL profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	
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
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
