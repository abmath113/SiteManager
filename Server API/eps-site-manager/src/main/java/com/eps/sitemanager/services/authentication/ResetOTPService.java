package com.eps.sitemanager.services.authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eps.sitemanager.model.userauth.ResetOTP;
import com.eps.sitemanager.model.userauth.User;
import com.eps.sitemanager.repository.userauth.OTPRepository;

@Service
public class ResetOTPService {
	
	private final OTPRepository otpRepository;
	
	public ResetOTPService(OTPRepository otpRepository) {
		this.otpRepository = otpRepository;
	}
	
	/* checks if otp for that particular user already exist ,
     *  if it does then overwrites else creates new otp object with that user.
     */
	public void saveResetOTP(ResetOTP resetOTP, User user) {
	    Optional<ResetOTP> exisitingOTPforuserOptional = otpRepository.findByUser(user);

	    if (exisitingOTPforuserOptional.isPresent()) {
	        // update the existing otp
	        ResetOTP otpToUpdate = exisitingOTPforuserOptional.get();
	        otpToUpdate.setOtp(resetOTP.getOtp());
	        otpToUpdate.setOtpGeneratedTime(LocalDateTime.now());
	        otpRepository.save(otpToUpdate);
	    } else {
	        // Save new OTP
	        resetOTP.setUser(user);
	        resetOTP.setOtpGeneratedTime(LocalDateTime.now());
	        otpRepository.save(resetOTP);
	    }
	}

	
	public ResetOTP getOTPforUser(User user) {
		return otpRepository.findByUser(user).get();
	}
	
	
}
