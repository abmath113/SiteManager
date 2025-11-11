package com.eps.sitemanager.dto.master;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="Site Master Details DTO.")
public class SiteMasterDTO {

	private int siteid;
	private String sitecode;
	private String siteatms;
	private Integer siteArea;
	private boolean sitestatus;
	private int bankId;
	private String siteaddress;
	private String projectName;
	private String state;
	private String district;
	private int channelManagerId;
	
	
}
