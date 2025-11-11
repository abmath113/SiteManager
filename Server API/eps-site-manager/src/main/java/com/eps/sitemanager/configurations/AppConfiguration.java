package com.eps.sitemanager.configurations;


import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eps.sitemanager.repository.userauth.UserRepository;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;



@Configuration
public class AppConfiguration {
	
	 private final UserRepository userRepository;
	
	 public AppConfiguration(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	 @Bean
	    UserDetailsService userDetailsService() {
	        return username -> userRepository.findByEmailId(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    }
	 
	 @Bean
	    BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 
	 @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	 
	 @Bean
	    AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());

	        return authProvider;
	    }
	 
	 
//	@Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI().info(apiInfo());
//    }
//	
	private Info apiInfo() {
        return new Info()
                .title("EPSSiteManager API")
                .description("API for EPS Site Management Application.")
                .version("1.0")
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
    	int year = LocalDate.now().getYear();
		String licenseStr = String.format("© %d Electronic Payment & Services Pvt. Ltd. All rights reserved.", year);
        return new License()
                .name(licenseStr)
                .url("https://electronicpay.in");
    }

    private Contact apiContact() {
        return new Contact()
                .name("Abhishek Thorat")
                .email("abhishek.thorat@electronicpay.co.in");
    }

 

}
