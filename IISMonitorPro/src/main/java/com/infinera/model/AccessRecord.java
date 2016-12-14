package com.infinera.model;

import java.sql.Timestamp;

public class AccessRecord {
	public AccessRecord() {

	}
	
	public AccessRecord(int userId, String photoPath, int isLegal) {
		this.userId = userId;
		this.photoPath = photoPath;
		this.isLegal = isLegal;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public int getIsLegal() {
		return isLegal;
	}

	public void setIsLegal(int isLegal) {
		this.isLegal = isLegal;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	int id;
	int userId;


//	User user;
	String photoPath;
	int isLegal;
	Timestamp timestamp;


}
