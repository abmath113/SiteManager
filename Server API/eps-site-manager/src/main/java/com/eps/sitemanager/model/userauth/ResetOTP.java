package com.eps.sitemanager.model.userauth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetOTP {
	
	 private static final SecureRandom random = new SecureRandom();
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="otp_id")
    private Long otpId;

    @Column(name="otp")
    private String otp;

   
    private LocalDateTime otpGeneratedTime;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    
    
    public ResetOTP(User user,LocalDateTime otpGeneratedTime) {
        this.user = user;
        this.otpGeneratedTime = otpGeneratedTime;
        otp = generateNumericOTP(4);
    }
    
    
    private String generateNumericOTP(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); 
        }
        return otp.toString();
    }
    
}
