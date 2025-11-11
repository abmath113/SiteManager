package com.eps.sitemanager.dto;

import com.eps.sitemanager.model.master.RentAgreementMaster;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Site Rent Records DTO.")
public class SiteRentRecordDTO {
	
	
	private int siteRentRecordId;
	private RentAgreementMaster agreementId;
	
	// Calculated rent
	private int generatedRent;
	private String rentMonth; // yyyy-mm (month for which rent is calculated)
	
	// Data coming from excel file
	private String siteCode;
	private String paymentDate; // Date were getting from excel
	private int amountPaid;	
	private String remarks; // mostly used for justifying rent mismatch
	private boolean transactionStatus;
	private String utrNo; // unique transaction number
	private String reason; // reason for failure of transcation
	
}
