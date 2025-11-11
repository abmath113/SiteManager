package com.eps.sitemanager.configurations.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIErrorController implements ErrorController {
     
	@RequestMapping("/error")
	public ResponseEntity<ErrorResponse> error() {
		HttpStatus status = HttpStatus.NOT_FOUND; // 404
		return new ResponseEntity<>(new ErrorResponse(status, "Resource Not Found", ""), status);
	}
	
    
}
