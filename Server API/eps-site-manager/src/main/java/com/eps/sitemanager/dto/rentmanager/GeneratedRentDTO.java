package com.eps.sitemanager.dto.rentmanager;

import com.eps.sitemanager.utilities.RentAmountCalculator.PaymentInterval;

import lombok.Data;

@Data
public class GeneratedRentDTO {
	
	private String siteCode;
	private String landlordName;
	
    private boolean rentAgreementStatus;
    private PaymentInterval paymentInterval;
    private int monthlyRent;
    
    private int rentToPay;

}
