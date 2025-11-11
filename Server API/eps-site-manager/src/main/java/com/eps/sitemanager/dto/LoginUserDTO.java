package com.eps.sitemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.Data;

@Data
@Schema(description = "User Login DTO")
public class LoginUserDTO {

	@NotBlank(message = "The Email Id is required.")
	@Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
	private String emailId;

	@NotBlank(message = "The Password is required.")
	private String password;
}
