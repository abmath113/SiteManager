package com.eps.sitemanager.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.eps.sitemanager.configurations.LoginResponse;
import com.eps.sitemanager.dto.LoginUserDTO;
import com.eps.sitemanager.dto.RegisterUserDTO;
import com.eps.sitemanager.dto.ResetPasswordDTO;
import com.eps.sitemanager.model.userauth.User;
import com.eps.sitemanager.services.authentication.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

	private final JwtService jwtService;

	private final UserAuthService userAuthService;

	public AuthenticationController(JwtService jwtService, UserAuthService userAuthService) {
		this.jwtService = jwtService;
		this.userAuthService = userAuthService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {

		return userAuthService.saveUser(registerUserDTO);

	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
		return userAuthService.confirmEmail(confirmationToken);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
		
		User authenticatedUser = userAuthService.authenticateLoginUser(loginUserDTO);
		
		String jwtToken = jwtService.generateToken(authenticatedUser);
		
		LoginResponse loginResponse = new LoginResponse();

		loginResponse.setToken(jwtToken);
		loginResponse.setEmailId(authenticatedUser.getEmailId());
		loginResponse.setFirstName(authenticatedUser.getFirstName());
		loginResponse.setLastName(authenticatedUser.getLastName());
		loginResponse.setUserRole(authenticatedUser.getUserRole());
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/send-password-reset-email")
	public ResponseEntity<?> sendPasswordResetRequest(@RequestBody ResetPasswordDTO resetPasswordDTO){ // enter email id
		
			return userAuthService.verifyUserEmail(resetPasswordDTO.getEmailId());
	}
	@PostMapping("/verify-otp-password-update")
	public ResponseEntity<?> verifyOTPandUpdatePassword(@RequestBody ResetPasswordDTO resetPasswordDTO){ // enter email id , otp and new password
		
		if(userAuthService.verifyOTP(resetPasswordDTO.getUserOTP(), resetPasswordDTO.getEmailId())) {
			User user = userAuthService.resetUserPassword(resetPasswordDTO.getNewPassword(), resetPasswordDTO.getEmailId());
			return ResponseEntity.ok("Password for: "+ user.getFirstName()+" has been updated");
		}

		return ResponseEntity.badRequest().body("Entered OTP is invalid");
	}


}
