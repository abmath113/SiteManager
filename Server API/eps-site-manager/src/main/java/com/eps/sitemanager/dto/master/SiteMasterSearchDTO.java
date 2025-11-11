package com.eps.sitemanager.dto.master;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="Site Master Search Details DTO.")
public class SiteMasterSearchDTO {

	private String sitecode;
	private String atmid;
	private boolean sitestatus;
	private String bank;
	private String location;
	
	
}
