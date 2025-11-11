package com.eps.sitemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SpringBootApplication
public class EPSSiteManagerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EPSSiteManagerApplication.class, args);
	}

	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

}




