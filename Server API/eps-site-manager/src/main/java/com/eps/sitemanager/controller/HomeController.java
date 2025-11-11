package com.eps.sitemanager.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class HomeController {
	
	
	@GetMapping("/")
	protected ResponseEntity<String> apiLanding() {
		int year = LocalDate.now().getYear();
		String copyright = String.format("Copyright %d Electronic Payment & Services Pvt. Ltd.", year);
		
		StringBuilder sb = new StringBuilder();
		sb.append("EPSSiteManager").append("<br>");
		sb.append("Site Manager Application").append("<br>");
		sb.append(copyright).append("<br>");
		sb.append(LocalDateTime.now().toString()).append("<br>");

	    return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
	}
	
}
