package com.eps.sitemanager.services.authentication;

import org.springframework.http.ResponseEntity;

import com.eps.sitemanager.dto.LoginUserDTO;
import com.eps.sitemanager.dto.RegisterUserDTO;
import com.eps.sitemanager.model.userauth.User;

public interface UserAuthService {
	
	
	
	public ResponseEntity<?> saveUser(RegisterUserDTO registerUserInput);
	public ResponseEntity<?> confirmEmail(String confirmationToken);
	public User authenticateLoginUser(LoginUserDTO loginUserInput);
	public ResponseEntity<?> verifyUserEmail(String emailId);
	public boolean verifyOTP(String userOTP, String emailId);
	User resetUserPassword(String newPassword, String emaildId);
	
	 
	
}
