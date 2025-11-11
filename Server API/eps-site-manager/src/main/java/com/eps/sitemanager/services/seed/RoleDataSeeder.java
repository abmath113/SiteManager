package com.eps.sitemanager.services.seed;

import com.eps.sitemanager.model.userauth.Role;
import com.eps.sitemanager.repository.userauth.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Order(1) // Run before UserDataSeeder
public class RoleDataSeeder implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        initializeDefaultRoles();
    }

    private void initializeDefaultRoles() {
        log.info("Initializing default roles...");

        List<String> defaultRoles = List.of("ADMIN", "USER");

        for (String roleName : defaultRoles) {
            createRoleIfNotExists(roleName);
        }

        log.info("Default roles initialization completed");
    }

    @Transactional
    private void createRoleIfNotExists(String roleName) {
        boolean exists = userRoleRepository.findAll().stream()
                .anyMatch(r -> r.getUserRole().equalsIgnoreCase(roleName));
        if (exists) {
            log.debug("Role already exists: {}", roleName);
            return;
        }

        Role role = new Role();
        role.setUserRole(roleName);
        role.setRoleDesc(roleName + " role");
        userRoleRepository.save(role);
        log.info("Created role: {}", roleName);
    }
}
