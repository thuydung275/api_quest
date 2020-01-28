package com.quest.etna.model;

public class UserDetails {
	
	private Integer id;
	private String username;
	private User.UserRole userRole = User.UserRole.ROLE_USER;
	
	public UserDetails() {
	}
	
	public UserDetails(Integer id, String username, User.UserRole userRole) {
		this.id = id;
		this.username = username;
		this.userRole = userRole;
	}
	
	public UserDetails(String username, User.UserRole userRole) {
		this(null, username, userRole);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public User.UserRole getUserRole() {
		return userRole;
	}
	
	public void setUserRole(User.UserRole userRole) {
		this.userRole = userRole;
	}
}
