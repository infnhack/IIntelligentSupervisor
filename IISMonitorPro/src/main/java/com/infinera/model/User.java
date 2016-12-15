package com.infinera.model;

import java.util.HashSet;
import java.util.Set;

public class User {
	public User() {

	}

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public User(Integer id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public Set<AccessRecord> getRecords() {
//		return records;
//	}
//
//	public void setRecords(Set<AccessRecord> records) {
//		this.records = records;
//	}
	
	@Override
	public String toString() {
		return "[ID=" + id + ", name=" + name + ", email=" + email + "]";
	}
	
	Integer id;
	String name;
	String email;
//	Set<AccessRecord> records = new HashSet<AccessRecord>();

}
