package com.eps.sitemanager.services.authentication;

import java.io.ObjectInputStream.GetField;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eps.sitemanager.model.userauth.ConfirmationToken;
import com.eps.sitemanager.model.userauth.User;
import com.eps.sitemanager.repository.userauth.ConfirmationTokenRepository;

@Service
public class ConfirmationTokenService {
	
	
	private final ConfirmationTokenRepository confirmationTokenRepository;
	
	
	public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
		this.confirmationTokenRepository = confirmationTokenRepository;
	}
	

//    public User getUserByToken(String token) {
//        Optional<ConfirmationToken> confirmationToken = Optional.of(confirmationTokenRepository.findByConfirmationToken(token));
//        return confirmationToken.map(ConfirmationToken::getUserId).orElse(null);
//    }
	public void saveConfirmationToken(ConfirmationToken confirmationToken) {
		confirmationTokenRepository.save(confirmationToken);
	}
	
	public ConfirmationToken getConfirmationToken(String confirmationToken) {
		return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
	}
	
	
	
}
