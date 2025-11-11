package com.eps.sitemanager.dto.rentmanager;

import java.time.LocalDate;

import com.eps.sitemanager.utilities.rentcalculation.PaymentInterval;

import lombok.Data;

@Data
public class GeneratedRentDTO {
	
	private String siteCode;
	
	
	
	
	private String landlordName;
	
    private boolean rentAgreementStatus;
    private PaymentInterval paymentInterval;
    private int monthlyRent;
    private String landlordMobileNo;
    private String landlordIFSC;
    private String landlordAccountNo;
    private Boolean landlordGSTStatus;
    
    private int rentToPay;
    private boolean siteStatus;




    
   
    
    
    

}
