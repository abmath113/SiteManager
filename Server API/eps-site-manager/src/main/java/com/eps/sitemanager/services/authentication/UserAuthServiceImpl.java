package com.eps.sitemanager.services.authentication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.LoginUserDTO;
import com.eps.sitemanager.dto.RegisterUserDTO;
import com.eps.sitemanager.model.userauth.ConfirmationToken;
import com.eps.sitemanager.model.userauth.ResetOTP;
import com.eps.sitemanager.model.userauth.Role;
import com.eps.sitemanager.model.userauth.User;
import com.eps.sitemanager.repository.userauth.ConfirmationTokenRepository;
import com.eps.sitemanager.repository.userauth.UserRepository;
import com.eps.sitemanager.repository.userauth.UserRoleRepository;

@Service
public class UserAuthServiceImpl implements UserAuthService {

	@Value("${app.confirmation.url}")
	private String confirmationUrl;

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;

	private final PasswordEncoder passwordEncoder;

	AuthenticationManager authenticationManager;

	private final ConfirmationTokenService confirmationTokenService;
	private final EmailService emailService;
	private final ResetOTPService resetOTPService;

	@Autowired
	public UserAuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, ConfirmationTokenRepository confirmationTokenRepository,
			EmailService emailService, ConfirmationTokenService confirmationTokenService,
			UserRoleRepository userRoleRepository, ResetOTPService resetOTPService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
		this.confirmationTokenService = confirmationTokenService;
		this.emailService = emailService;
		this.resetOTPService = resetOTPService;
	}

	public ResponseEntity<?> saveUser(RegisterUserDTO registerUserInput) {

		if (registerUserInput == null) {
			return ResponseEntity.badRequest().body("Error: NULL user registration data");
		}

		Optional<User> userOptional = userRepository.findByEmailId(registerUserInput.getEmailId());

		if (userOptional.isPresent()) {
			if (userOptional.get().isEnabled()) {
				return ResponseEntity.badRequest().body("Error: Email is already in use !");
			}
		}

		// Create new user
		User user = createNewUser(registerUserInput);

		LocalDateTime tokenGeneratedTime = LocalDateTime.now();
		ConfirmationToken confirmationToken = new ConfirmationToken(user, tokenGeneratedTime);
		confirmationTokenService.saveConfirmationToken(confirmationToken);

		String recipientEmail = registerUserInput.getEmailId();
		String token = confirmationToken.getConfirmationToken();
		emailService.sendConfirmationEmail(recipientEmail, token);

		return ResponseEntity.ok("Verify email by the link sent on your email address");
	}

	/**
	 * Creates a new user from registration DTO
	 * 
	 * @param registerUserInput Registration data transfer object
	 * @return Saved user entity
	 */
	private User createNewUser(RegisterUserDTO registerUserInput) {
		User user = new User();

		// Set user details
		user.setFirstName(sanitizeInput(registerUserInput.getFirstName()));
		user.setLastName(sanitizeInput(registerUserInput.getLastName()));
		user.setEmailId(registerUserInput.getEmailId().trim().toLowerCase());

		// Set default role
		Role userRole = userRoleRepository.findByRoleId(4)
				.orElseThrow(() -> new IllegalStateException("Default user role not found"));
		user.setUserRole(userRole);

		// Encode password
		user.setPassword(passwordEncoder.encode(registerUserInput.getPassword()));

		// Set timestamps
		user.setCreatedOn(LocalDateTime.now());
		user.setEnabled(false); // Default to disabled until email verified

		// Save user
		return userRepository.save(user);
	}

	/**
	 * Sanitizes input to prevent XSS and other injection attacks
	 * 
	 * @param input Input string to sanitize
	 * @return Sanitized input
	 */
	private String sanitizeInput(String input) {
		if (input == null)
			return null;
		return input.trim().replaceAll("<[^>]*>", ""); // Basic HTML tag removal
	}

	@Override
	public ResponseEntity<?> confirmEmail(String confirmationToken) {
		ConfirmationToken userToken = confirmationTokenService.getConfirmationToken(confirmationToken);

		// Check if token exists
		if (userToken == null) {
			return ResponseEntity.badRequest().body("Invalid confirmation token");
		}

		LocalDateTime currDateTime = LocalDateTime.now();
		Duration duration = Duration.between(userToken.getTokenGeneratedTime(), currDateTime);

		if (duration.toHours() > 2) {
			return ResponseEntity.badRequest().body("The Token has expired, Please re-register!");
		}

		User user = userRepository.findByEmailIdIgnoreCase(userToken.getUserEntity().getEmailId());
		user.setEnabled(true);
		userRepository.save(user);
		return ResponseEntity.ok("Email Verification Successful , You can now login using your credentials !!");
	}

	public User authenticateLoginUser(LoginUserDTO loginUserInput) {

		// here we're generating UsernamePasswordAuthenticationToken cause it is the
		// spring representation of username and password.
		// principal = username and credentials = password need to be set in the object
		// of UsernamePasswordAuthenticationToken.
		UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
				loginUserInput.getEmailId(), loginUserInput.getPassword());

		// this is where real auth happens

		/*
		 * 1. Authentication delegates delegates the configured Auth Manager since here
		 * we're using username and pass therefore it's DaoAuthenticationProvider
		 * 
		 * 2.Then the implementation of UserDetailsService is called our User Entity in
		 * this case.retrieves user details from your database or other user store based
		 * on the email.
		 * 
		 * 3. The provider uses a PasswordEncoder to verify if the submitted password
		 * matches the stored password.
		 * 
		 * 4. Now if the credentials are wrong then err is thrown and 401 unauthorized
		 * response
		 * 
		 * 5. Else authentication object is populated with user details and authorities
		 */
		authenticationManager.authenticate(userPassAuthToken);

		return userRepository.findByEmailId(loginUserInput.getEmailId()).orElseThrow();
	}

	// this function verifies if the user email exists and if it does send creates
	/// an otp and sends it to emailid of user
	@Override
	public ResponseEntity<?> verifyUserEmail(String emailId) {
		
		if (!userRepository.existsByEmailId(emailId)) {
			return ResponseEntity.badRequest().body("Email Id doesn't exist");
		}

		User verifyUser = userRepository.findByEmailIdIgnoreCase(emailId);
		LocalDateTime currentTime = LocalDateTime.now();
		ResetOTP resetOTP = new ResetOTP(verifyUser, currentTime);
		/*
		 * checks if otp for that particular user already exist , if it does then
		 * overwrites else creates new otp object with that user.
		 */
		resetOTPService.saveResetOTP(resetOTP, verifyUser);
		emailService.sendPasswordResetOTP(emailId, resetOTP.getOtp());

		return ResponseEntity.ok("Verification OTP has been sent to your Email ID.");
	}
	
	@Override
	public boolean verifyOTP(String userOTP, String emailId) {
		
		// check whether user email exists in db or not
		User verifyUser = userRepository.findByEmailIdIgnoreCase(emailId);
		
		ResetOTP realOTP = resetOTPService.getOTPforUser(verifyUser);
		
		Duration otpTimeDifference = Duration.between(LocalDateTime.now(),realOTP.getOtpGeneratedTime());
		
		if (realOTP.getOtp().equals(userOTP) && otpTimeDifference.toMinutes()<10) {
			
			// after it has been verified that otp matches delete the otp so it can't be reused
			
			realOTP.setOtp(null);
			resetOTPService.saveResetOTP(realOTP, verifyUser);
			return true;
			
		}
		
		return false;
	}
	
	@Override
	public User resetUserPassword(String newPassword, String emaildId) {
		User verifiedUser = userRepository.findByEmailIdIgnoreCase(emaildId);

		verifiedUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(verifiedUser);
		return verifiedUser;
	}



}
