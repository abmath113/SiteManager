package com.eps.sitemanager.request.validator.master;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.RentAgreementMasterDTO;
import com.eps.sitemanager.dto.master.RentAgreementMasterSearchDTO;
import com.eps.sitemanager.utilities.AppConstants;
import com.eps.sitemanager.utilities.RequestDataValidator;

@Component
public class RentAgreementMasterControllerRequestValidator {

	private RequestDataValidator reqValidator;

	@Autowired
	public RentAgreementMasterControllerRequestValidator(RequestDataValidator reqValidator) {
		super();
		this.reqValidator = reqValidator;
	}

	public RequestValidaton validateSaveUpdateRequest(RentAgreementMasterDTO rentAgreementMasterDTO) {
		RequestValidaton reqValidation = new RequestValidaton();

//		String landlordIdValidation = isValidLandlordId(rentAgreementMasterDTO.getLandlordId());
//		if (!AppConstants.STATUS_YES.equalsIgnoreCase(landlordIdValidation)) {
//			reqValidation.setState(false);
//			reqValidation.setStatus(AppConstants.STATUS_INVALID);
//			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + landlordIdValidation);
//			return reqValidation;
//		}

//		String siteIdValidation = isValidSiteId(rentAgreementMasterDTO.getSiteId());
//		if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteIdValidation)) {
//			reqValidation.setState(false);
//			reqValidation.setStatus(AppConstants.STATUS_INVALID);
//			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + siteIdValidation);
//			return reqValidation;
//		}
		
	
		// Validate that Agreement Date is not after Rent Pay Start Date.
		String agreementDateAndRentPayStartDateValidation = isValidAgreementDateAndRentPayStartDate(rentAgreementMasterDTO.getAgreementDate(),rentAgreementMasterDTO.getRentPayStartDate());
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(agreementDateAndRentPayStartDateValidation)) {
		reqValidation.setState(false);
		reqValidation.setStatus(AppConstants.STATUS_INVALID);
		reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + agreementDateAndRentPayStartDateValidation);
		return reqValidation;
	}
		
		// Validate that Agreement end Date is after Rent Pay Start Date.
		String rentPayStartDateAndAgreementEndDate = isValidRentPayStartDateAndAgreementEndDate(rentAgreementMasterDTO.getRentPayStartDate(), rentAgreementMasterDTO.getAgreementEndDate());
		if(!AppConstants.STATUS_YES.equalsIgnoreCase(rentPayStartDateAndAgreementEndDate)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + rentPayStartDateAndAgreementEndDate);
		}
		
		
//		if (!AppConstants.STATUS_YES.equalsIgnoreCase(agreementDateCheck)) {
//		    reqValidation.setState(false);
//		    reqValidation.setStatus(AppConstants.STATUS_INVALID);
//		    reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + agreementDateCheck);
//		    return reqValidation;
//		}



		String agreementDateValidation = isValidDate(rentAgreementMasterDTO.getAgreementDate(), "Agreement Date");
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(agreementDateValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + agreementDateValidation);
			return reqValidation;
		}
		

		String rentPayStartDateValidation = isValidDate(rentAgreementMasterDTO.getRentPayStartDate(),
				"Rent pay start Date");
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(rentPayStartDateValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + rentPayStartDateValidation);
			return reqValidation;
		}

		String monthlyRentValidation = isValidAmount(rentAgreementMasterDTO.getMonthlyRent(), "Monthly Rent");
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(monthlyRentValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + monthlyRentValidation);
			return reqValidation;
		}

		String depositValidation = isValidAmount(rentAgreementMasterDTO.getDeposit(), "Deposit");
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(depositValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + depositValidation);
			return reqValidation;
		}

		String solarRentValidation = isValidAmount(rentAgreementMasterDTO.getSolarPanelRent(), "Solar Rent");
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(solarRentValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + solarRentValidation);
			return reqValidation;
		}

//		String agreementSpanValidation = isValidAgreementSpan(rentAgreementMasterDTO.getAgreementSpan());
//		if (!AppConstants.STATUS_YES.equalsIgnoreCase(agreementSpanValidation)) {
//			reqValidation.setState(false);
//			reqValidation.setStatus(AppConstants.STATUS_INVALID);
//			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + agreementSpanValidation);
//			return reqValidation;
//		}

		return reqValidation;
	}
	

	public RequestValidaton validateSearchRequest(RentAgreementMasterSearchDTO rentagreementmastersearchdto) {
		RequestValidaton reqValidation = new RequestValidaton();

		String siteCodeValidation = isValidSiteCode(rentagreementmastersearchdto.getSitecode());
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteCodeValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + siteCodeValidation);
			return reqValidation;
		}

		String nameValidation = isValidName(rentagreementmastersearchdto.getName());
		if (!AppConstants.STATUS_YES.equalsIgnoreCase(nameValidation)) {
			reqValidation.setState(false);
			reqValidation.setStatus(AppConstants.STATUS_INVALID);
			reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + nameValidation);
			return reqValidation;
		}

		return reqValidation;
	}
	
	private String isValidAgreementDateAndRentPayStartDate(LocalDate agreementDate, LocalDate rentPayStartDate) {
	    if (agreementDate != null && rentPayStartDate != null && agreementDate.isAfter(rentPayStartDate)) {
	        return "Agreement Date must be before or equal to Rent Pay Start Date";
	    }
	    return AppConstants.STATUS_YES;
	}

	private String isValidRentPayStartDateAndAgreementEndDate(LocalDate rentPayStartDate , LocalDate agreementEndDate) {
		if(rentPayStartDate.isAfter(agreementEndDate)) {
			return "Rent Pay Start Date  must be before Agreement end Date";
		}
		return AppConstants.STATUS_YES;
	}

	private String isValidLandlordId(Integer landlordId) {
		if (landlordId == null || landlordId <= 0) {
			return "Invalid Landlord ID";
		}
		return AppConstants.STATUS_YES;
	}

	private String isValidSiteId(Integer siteId) {
		if (siteId == null || siteId <= 0) {
			return "Invalid Site ID";
		}
		return AppConstants.STATUS_YES;
	}

	private String isValidDate(LocalDate localDate, String fieldName) {
		if (localDate == null) {
			return fieldName + " is required";
		}
		// Add more date validation if needed
		return AppConstants.STATUS_YES;
	}

	private String isValidAmount(Integer amount, String fieldName) {
		if (amount == null || amount < 0) {
			return "Invalid " + fieldName;
		}
		return AppConstants.STATUS_YES;
	}

	private String isValidAgreementSpan(Integer span) {
		if (span == null || span < 1 || span > 1200) {
			return "Agreement Span should be between 1 and 1200";
		}
		return AppConstants.STATUS_YES;
	}

	private String isValidSiteCode(String siteCode) {
		if (siteCode == null || siteCode.isEmpty()) {
			return "Site Code is required";
		}
		if (siteCode.length() > 30) {
			return "Site Code length should be less than or equal to 30";
		}
		if (!reqValidator.isAlphaNumeric(siteCode)) {
			return "Site Code contains invalid characters, Allowed - A-Z a-z 0-9";
		}
		return AppConstants.STATUS_YES;
	}

	private String isValidName(String name) {
		if (name == null || name.isEmpty()) {
			return "Name is required";
		}
		if (!reqValidator.isAlphNumericWithSpace(name)) {
			return "Name contains invalid characters, Allowed - A-Z a-z";
		}
		return AppConstants.STATUS_YES;
	}
}
