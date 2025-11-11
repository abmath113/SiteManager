package com.eps.sitemanager.dto.master;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description="Rent Agreement Details DTO.")
public class RentAgreementMasterDTO {
	
		public int agreementId;
	    private int siteId;
	    private int landlordId;
	    
	    private LocalDate agreementDate;
//	    private LocalDate considerAgreementDate;
		private LocalDate rentPayStartDate;
	    private LocalDate agreementEndDate;
		private LocalDate terminationDate;
	    
	    private int agreementSpan;
	    private int solarPanelRent;
	    private int monthlyRent;
	    private int deposit;
	    private int paymentInterval;
	    private int escalationAfterMonths;
	    private int escalationPercent;
	    private boolean rentAgreementStatus;
		private String terminationRemark;
		
		
}
