package com.eps.sitemanager.dto.master;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "DTO for search input for rent agreements")
public class RentAgreementMasterSearchDTO {
	
	private String sitecode;
	private String name;
	private boolean status;
	
	
}
