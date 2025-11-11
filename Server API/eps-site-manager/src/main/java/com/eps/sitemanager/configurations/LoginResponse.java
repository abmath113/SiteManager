package com.eps.sitemanager.configurations;

import com.eps.sitemanager.model.userauth.Role;

import lombok.Data;

@Data
public class LoginResponse {
	
	
	private String token;
	private long expiresIn;
	private String emailId;
	private String firstName;
	private String lastName;
	private Role userRole;
	
	public String getToken() {
		return token;
	}
	
	
}
