package com.eps.sitemanager.repository.userauth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eps.sitemanager.model.userauth.Role;

public interface UserRoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleId(Integer roleId);
}
