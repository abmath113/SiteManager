package com.eps.sitemanager.dto.master;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description="Rent Agreement Termination Details DTO.")
public class RentAgreementTerminateDTO {
	
	 	public int agreementId;
		private LocalDate terminationDate;

		private String terminationRemark;

		
}
