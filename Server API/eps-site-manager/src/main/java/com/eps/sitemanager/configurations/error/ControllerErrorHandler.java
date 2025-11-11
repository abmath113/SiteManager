package com.eps.sitemanager.configurations.error;

//import java.io.PrintWriter;
//import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler {

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleDataNotFoundExceptions(Exception e) {
		HttpStatus status = HttpStatus.NOT_FOUND; // 404
		// String stackTrace = convertStackTraceToString(e);
		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), ""), status);
	}
	
	@ExceptionHandler(FailedToSaveException.class)
	public ResponseEntity<ErrorResponse> handleFailedToSaveExceptions(Exception e) {
		HttpStatus status = HttpStatus.NOT_FOUND; // 404
		// String stackTrace = convertStackTraceToString(e);
		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), ""), status);
	}

	@ExceptionHandler(ParameterConstraintException.class)
	public ResponseEntity<ErrorResponse> handleCustomParameterConstraintExceptions(Exception e) {
		HttpStatus status = HttpStatus.BAD_REQUEST; // 400
		// String stackTrace = convertStackTraceToString(e);
		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), ""), status);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerExceptions(Exception e) {
		HttpStatus status = HttpStatus.NOT_FOUND; // 404
		// String stackTrace = convertStackTraceToString(e);
		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), ""), status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
		// String stackTrace = convertStackTraceToString(e);
		return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), ""), status);
	}

//	private String convertStackTraceToString(Exception e) {
//		// Converting the stack trace to String
//		StringWriter stringWriter = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(stringWriter);
//		e.printStackTrace(printWriter);
//		return stringWriter.toString();
//	}
	
	
}
