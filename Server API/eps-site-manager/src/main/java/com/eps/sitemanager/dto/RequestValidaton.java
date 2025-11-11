package com.eps.sitemanager.dto;


import com.eps.sitemanager.utilities.AppConstants;

import lombok.Data;



@Data 
public class RequestValidaton {
	
	private boolean state;
	private String status;
	private String errorMessage;
	
	
	
	public RequestValidaton() {
		super();
		this.state = true;
		this.status = AppConstants.STATUS_VALID;
		this.errorMessage = "";
	}
	
}

