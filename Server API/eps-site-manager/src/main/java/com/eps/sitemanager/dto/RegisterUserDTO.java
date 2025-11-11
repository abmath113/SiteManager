package com.eps.sitemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User Registration DTO")
public class RegisterUserDTO {

	@Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotEmpty(message = "Email cannot be empty")
	private String emailId;

	@NotBlank(message = "The First Name is required.")
	@Size(min = 2, max = 100, message = "The length of full name must be between 2 and 100 characters.")
	@JsonProperty("firstName")
	private String firstName;

	@NotBlank(message = "The Last Name is required.")
	@JsonProperty("lastName")
	private String lastName;

	@NotBlank(message = "The Password is required.")
	@JsonProperty("password")
	private String password;
}
