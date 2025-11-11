package com.eps.sitemanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Reset User Password DTO")
public class ResetPasswordDTO {
	
	private String userOTP;
	private String emailId;
	private String newPassword;
	

}
