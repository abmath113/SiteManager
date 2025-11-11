package com.eps.sitemanager.model.userauth;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User implements UserDetails {

	/*
	 * @author abhishek.thorat
	 */
	
	private static final long serialVersionUID = -4987630858919606660L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false)
	
	private String emailId;

	@Column(name = "user_password", nullable = false)
	private String password;
	
	@Column(name = "is_enabled", nullable = false)
	private boolean isEnabled;

	@ManyToOne
	@JoinColumn(name = "user_role_id")
	private Role userRole;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = false;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdOn;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of();
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return emailId;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

}
