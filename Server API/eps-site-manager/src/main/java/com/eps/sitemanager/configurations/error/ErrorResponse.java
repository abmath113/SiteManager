package com.eps.sitemanager.configurations.error;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ErrorResponse {

	// customizing timestamp serialization format
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	private int code;
	private String status;
	private String message;
	private String stackTrace;
	private Object data;

	public ErrorResponse() {
		timestamp = LocalDateTime.now();
	}

	public ErrorResponse(HttpStatus httpStatus, String message) {
		this();
		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
	}

	public ErrorResponse(HttpStatus httpStatus, String message, String stackTrace) {
		this(httpStatus, message);
		this.stackTrace = stackTrace;
	}

	public ErrorResponse(HttpStatus httpStatus, String message, String stackTrace, Object data) {
		this(httpStatus, message, stackTrace);
		this.data = data;
	}
	public ErrorResponse(String message, String details) {
		this();
		this.status = "ERROR";
		this.message = message;
		this.data = details;
		this.code = HttpStatus.BAD_REQUEST.value(); // Default to 400 or choose appropriate status
	}
}
