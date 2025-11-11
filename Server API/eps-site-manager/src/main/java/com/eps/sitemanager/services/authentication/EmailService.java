package com.eps.sitemanager.services.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    
    @Value("${app.confirmation.url}")
    private String confirmationUrl;
    
    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public void sendConfirmationEmail(String recipientEmail, String token) {
        String confirmationLink = confirmationUrl + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(senderEmail); 
        email.setTo(recipientEmail);
        email.setSubject("SiteManager Account Confirmation");
        email.setText(
        	    "Dear User,\n\n" +
        	    "Welcome to Site Manager!\n\n" +
        	    "To activate your account, please click the link below:\n\n" +
        	    confirmationLink + "\n\n" +
        	    "If you did not request this account, please ignore this email.\n\n" +
        	    "Best Regards,\n" +
        	    "Site Manager Team"  
        	);

        sendEmail(email);
    }
    
    public void sendPasswordResetOTP(String recipientEmail, String otp) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(senderEmail);
        email.setTo(recipientEmail);
        email.setSubject("SiteManager Account Password Reset");
        email.setText(
            "Dear User,\n\n" +
            "You have requested to reset your password. Please use the OTP below to proceed:\n\n" +
            "OTP: " + otp + "\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Best Regards,\n" +
            "Site Manager Team"
        );

        sendEmail(email);
    }

}
