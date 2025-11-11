package com.eps.sitemanager.services.seed;

import com.eps.sitemanager.model.userauth.Role;
import com.eps.sitemanager.model.userauth.User;
import com.eps.sitemanager.repository.userauth.UserRepository;
import com.eps.sitemanager.repository.userauth.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after RoleDataSeeder
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        createUserIfNotExists(
                "anushka.karhadkar@electronicpay.in",
                "anushka@123",
                "Admin",
                "User",
                "ADMIN"
        );
        
        createUserIfNotExists(
                "abhishek.thorat@electronicpay.in",
                "Admin123",
                "Admin",
                "User",
                "ADMIN"
        );

        createUserIfNotExists(
                "user@site.com",
                "user123",
                "Default",
                "User",
                "USER"
        );
    }

    @Transactional
    private void createUserIfNotExists(String email, String password, String firstName, String lastName, String roleName) {
        if (userRepository.existsByEmailId(email)) {
            log.debug("User already exists: {}", email);
            return;
        }

        Optional<Role> roleOpt = userRoleRepository.findAll().stream()
                .filter(r -> r.getUserRole().equalsIgnoreCase(roleName))
                .findFirst();

        Role role = roleOpt.orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = new User();
        user.setEmailId(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(role);
        user.setEnabled(true);
        user.setActive(true);

        userRepository.save(user);
        log.info("Created user: {} with role: {}", email, roleName);
    }
}
