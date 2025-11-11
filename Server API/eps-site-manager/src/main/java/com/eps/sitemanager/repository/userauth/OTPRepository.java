package com.eps.sitemanager.repository.userauth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eps.sitemanager.model.userauth.ResetOTP;
import com.eps.sitemanager.model.userauth.User;

@Repository("otprepository")
public interface OTPRepository extends JpaRepository<ResetOTP, Long> {
	
	ResetOTP findByOtp(String otp);
	
	Optional<ResetOTP> findByUser(User user);
}
