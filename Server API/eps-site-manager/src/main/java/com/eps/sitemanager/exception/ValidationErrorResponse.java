package com.eps.sitemanager.exception;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ValidationErrorResponse {
	  private String message;
	    private Map<String, String> errors;
}
