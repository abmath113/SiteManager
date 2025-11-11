package com.eps.sitemanager.model.userauth;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
public class ConfirmationToken {

	// confirmation token for email verification
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tokenGeneratedTime;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken() {}

    public ConfirmationToken(User user) {
        this.user = user;
        confirmationToken = UUID.randomUUID().toString();
    }
    public ConfirmationToken(User user,LocalDateTime tokenGeneratedTime) {
        this.user = user;
        this.tokenGeneratedTime = tokenGeneratedTime;
        confirmationToken = UUID.randomUUID().toString();
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
    
    public LocalDateTime getTokenGeneratedTime() {
    	return tokenGeneratedTime;
    }
    public void setTokenGeneratedTime(LocalDateTime tokenGeneratedTime) {
    	this.tokenGeneratedTime = tokenGeneratedTime;
    }

    public User getUserEntity() {
        return user;
    }

    public void setUserEntity(User user) {
        this.user = user;
    }

}
