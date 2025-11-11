package com.eps.sitemanager.request.validator.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.LandlordMasterDTO;
import com.eps.sitemanager.dto.master.LandlordMasterSearchDTO;
import com.eps.sitemanager.utilities.AppConstants;
import com.eps.sitemanager.utilities.RequestDataValidator;

@Component
public class LandlordMasterControllerRequestValidator {
    
    private RequestDataValidator reqValidator;

    @Autowired
    public LandlordMasterControllerRequestValidator(RequestDataValidator reqValidator) {
    	super();
        this.reqValidator = reqValidator;
    }

    public RequestValidaton validateSaveUpdateRequest(LandlordMasterDTO landlordDTO) {
        RequestValidaton reqValidation = new RequestValidaton();

        String nameValidation = isValidName(landlordDTO.getName());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(nameValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + nameValidation);
            return reqValidation;
        }
        
        String beneficiaryNameValidation = isValidName(landlordDTO.getBeneficiaryName());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(beneficiaryNameValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + beneficiaryNameValidation);
            return reqValidation;
        }

        String mobileNoValidation = isValidMobileNo(landlordDTO.getMobileNo());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(mobileNoValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + mobileNoValidation);
            return reqValidation;
        }

        String ifscCodeValidation = isValidIFSCCode(landlordDTO.getIfscCode());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(ifscCodeValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + ifscCodeValidation);
            return reqValidation;
        }

        String accountNoValidation = isValidAccountNo(landlordDTO.getAccountNo());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(accountNoValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + accountNoValidation);
            return reqValidation;
        }

        String panValidation = isValidPAN(landlordDTO.getPan());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(panValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + panValidation);
            return reqValidation;
        }

        String aadharNoValidation = isValidAadharNo(landlordDTO.getAadharNo());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(aadharNoValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + aadharNoValidation);
            return reqValidation;
        }

        String addressValidation = isValidAddress(landlordDTO.getAddress());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(addressValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + addressValidation);
            return reqValidation;
        }

        return reqValidation;
    }
    
    
    
    
    public RequestValidaton validateSearchRequest(LandlordMasterSearchDTO landlordMasterSearchDTO) {
    	
    	RequestValidaton reqValidation = new RequestValidaton();
    	
        String nameValidation = isValidName(landlordMasterSearchDTO.getName());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(nameValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + nameValidation);
            return reqValidation;
        }


        String addressValidation = isValidAddress(landlordMasterSearchDTO.getAddress());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(addressValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + addressValidation);
            return reqValidation;
        }

        return reqValidation;
    }
    
    
    

    private String isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return "Name is required";
        }
//        if (!reqValidator.isAlphNumericWithSpace(name)) {
//            return "Name contains invalid characters, Allowed - A-Z a-z";
//        }
        return AppConstants.STATUS_YES;
    }

    private String isValidMobileNo(String mobileNo) {
        if (mobileNo == null || mobileNo.isEmpty()) {
            return "Mobile No is required";
        }
        if (!reqValidator.isNumericString(mobileNo) || mobileNo.length() != 10) {
            return "Mobile No should be 10 digits";
        }
        return AppConstants.STATUS_YES;
    }

    private String isValidIFSCCode(String ifscCode) {
        if (ifscCode == null || ifscCode.isEmpty()) {
            return "IFSC Code is required";
        }
        if (ifscCode.length() != 11 || !reqValidator.isAlphaNumeric(ifscCode)) {
            return "IFSC Code should be 11 alphanumeric characters";
        }
        return AppConstants.STATUS_YES;
    }

    private String isValidAccountNo(String accountNo) {
        if (accountNo == null || accountNo.isEmpty()) {
            return "Account No is required";
        }
        if (!reqValidator.isNumericString(accountNo)) {
            return "Account No should contain only numeric characters";
        }
        return AppConstants.STATUS_YES;
    }

    private String isValidPAN(String pan) {
//        if (pan == null || pan.isEmpty()) {
//            return "PAN is required";
//        }
        if (pan.length() != 10 || !reqValidator.isAlphaNumeric(pan)) {
            return "PAN should be 10 uppercase alphabetic characters";
        }
        return AppConstants.STATUS_YES;
    }

    private String isValidAadharNo(String aadharNo) {
    	aadharNo = aadharNo.trim().replaceAll("\\s", "");

        if (aadharNo == null || aadharNo.isEmpty()) {
            return "Aadhar No is required";
        }
        if (aadharNo.length() != 12 || !reqValidator.isNumericString(aadharNo)) {
            return "Aadhar No should be 12 digits";
        }
        return AppConstants.STATUS_YES;
    }

    private String isValidAddress(String address) {
        if (address == null || address.isEmpty()) {
            return "Address is required";
        }
        if (!reqValidator.isValidAddress(address) || address.length() > 500) {
            return "Address length should be less than equal to 500 and contain only valid characters";
        }
        return AppConstants.STATUS_YES;
    }
}
