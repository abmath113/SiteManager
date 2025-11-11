package com.eps.sitemanager.repository.userauth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eps.sitemanager.model.userauth.Role;
import com.eps.sitemanager.model.userauth.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserId(Integer userId);

	Optional<User> findByFirstName(String firstName);

	Optional<User> findByEmailId(String emailId);

	List<User> findByUserRole(Role useRole);

	Boolean existsByEmailId(String emailId);

	User findByEmailIdIgnoreCase(String emailId);

}
