package com.eps.sitemanager.repository.userauth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eps.sitemanager.model.userauth.ConfirmationToken;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
	
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    
}
