package com.eps.sitemanager.request.validator.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.SiteMasterDTO;
import com.eps.sitemanager.dto.master.SiteMasterSearchDTO;
import com.eps.sitemanager.utilities.AppConstants;
import com.eps.sitemanager.utilities.RequestDataValidator;

@Component
public class SiteMasterControllerRequestValidator {
    
    private final RequestDataValidator reqValidator;
    
    @Autowired
    public SiteMasterControllerRequestValidator(RequestDataValidator reqValidator) {
        this.reqValidator = reqValidator;
    }
    
    public RequestValidaton validateSaveUpdateRequest(SiteMasterDTO siteMasterDTO) {
        RequestValidaton reqValidation = new RequestValidaton();
        reqValidation.setState(true);
        reqValidation.setStatus(AppConstants.STATUS_VALID);

        // Null check for DTO
        if (siteMasterDTO == null) {
            return createErrorValidation("Request data cannot be null");
        }

        // Site Code validation
        String siteCodeValidation = isValidSiteCode(siteMasterDTO.getSitecode());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteCodeValidation)) {
            return createErrorValidation(siteCodeValidation);
        }
        
        // Site ATMs validation
        String siteATMsValidation = isValidSiteATMs(siteMasterDTO.getSiteatms());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteATMsValidation)) {
            return createErrorValidation(siteATMsValidation);
        }
        
        // Bank validation
        String bankValidation = isValidBankId(siteMasterDTO.getBankId());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(bankValidation)) {
            return createErrorValidation(bankValidation);
        }
        
        // Project name validation
        String projectValidation = isValidProjectName(siteMasterDTO.getProjectName());
        if(!AppConstants.STATUS_YES.equalsIgnoreCase(projectValidation)) {
        	return createErrorValidation(projectValidation);
        }
        
        // Site Address validation
        String siteAddressValidation = isValidSiteAddress(siteMasterDTO.getSiteaddress());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteAddressValidation)) {
            return createErrorValidation(siteAddressValidation);
        }
        
        // Channel Manager validation
        String channelManagerValidation = isValidChannelManagerId(siteMasterDTO.getChannelManagerId());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(channelManagerValidation)) {
            return createErrorValidation(channelManagerValidation);
        }
        
        // Site Area validation (if required)
        if (siteMasterDTO.getSiteArea() != null) {
            String siteAreaValidation = isValidSiteArea(siteMasterDTO.getSiteArea());
            if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteAreaValidation)) {
                return createErrorValidation(siteAreaValidation);
            }
        }

        return reqValidation;
    }
    
    public RequestValidaton validateSearchRequest(SiteMasterSearchDTO siteMasterSearchDTO) {
        RequestValidaton reqValidation = new RequestValidaton();
        reqValidation.setState(true);
        reqValidation.setStatus(AppConstants.STATUS_VALID);

        // Null check for DTO
        if (siteMasterSearchDTO == null) {
            return createErrorValidation("Search criteria cannot be null");
        }

        // Optional validations for search criteria
        if (siteMasterSearchDTO.getSitecode() != null) {
            String siteCodeValidation = isValidSiteCode(siteMasterSearchDTO.getSitecode());
            if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteCodeValidation)) {
                return createErrorValidation(siteCodeValidation);
            }
        }
        
        if (siteMasterSearchDTO.getAtmid() != null) {
            String siteATMsValidation = isValidSiteATMs(siteMasterSearchDTO.getAtmid());
            if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteATMsValidation)) {
                return createErrorValidation(siteATMsValidation);
            }
        }
        
        if (siteMasterSearchDTO.getBank() != null) {
            String bankValidation = isValidBanks(siteMasterSearchDTO.getBank());
            if (!AppConstants.STATUS_YES.equalsIgnoreCase(bankValidation)) {
                return createErrorValidation(bankValidation);
            }
        }
        
        if (siteMasterSearchDTO.getLocation() != null) {
            String siteAddressValidation = isValidSiteAddress(siteMasterSearchDTO.getLocation());
            if (!AppConstants.STATUS_YES.equalsIgnoreCase(siteAddressValidation)) {
                return createErrorValidation(siteAddressValidation);
            }
        }

        return reqValidation;
    }
    
    private RequestValidaton createErrorValidation(String errorMessage) {
        RequestValidaton reqValidation = new RequestValidaton();
        reqValidation.setState(false);
        reqValidation.setStatus(AppConstants.STATUS_INVALID);
        reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + errorMessage);
        return reqValidation;
    }
    
    

    private String isValidSiteCode(String siteCode) {
        if (siteCode == null || siteCode.trim().isEmpty()) {
            return "Site Code is required";
        }
        if (siteCode.length() > 30) {
            return "Site Code length should be less than equal to 30";
        }
        if (!reqValidator.isAlphaNumeric(siteCode)) {
            return "Site Code contains invalid characters, Allowed - A-Z a-z 0-9";
        }
        return AppConstants.STATUS_YES;
    }
    
    private String isValidSiteATMs(String siteATMs) {
        if (siteATMs == null || siteATMs.trim().isEmpty()) {
            return "Site ATMs is required";
        }
        if (siteATMs.length() > 80) {
            return "Site ATMs length should be less than equal to 80";
        }
        if (!reqValidator.isAlphaNumericComma(siteATMs)) {
            return "Site ATMs contains invalid characters, Allowed - A-Z a-z 0-9 ,";
        }
        return AppConstants.STATUS_YES;
    }
    
    private String isValidBankId(Integer bankId) {
        if (bankId == null || bankId <= 0) {
            return "Invalid Bank ID";
        }
        return AppConstants.STATUS_YES;
    }
    
    private String isValidBanks(String banks) {
        if (banks == null || banks.trim().isEmpty()) {
            return "Banks information is required";
        }
        if (banks.length() > 100) {
            return "Banks information length should be less than equal to 100";
        }
        if (!reqValidator.isAlphaNumericComma(banks)) {
            return "Banks information contains invalid characters, Allowed - A-Z a-z 0-9 ,";
        }
        return AppConstants.STATUS_YES;
    }
    
    private String isValidSiteAddress(String siteAddress) {
    	siteAddress = siteAddress.replaceAll("[^A-Za-z0-9,\\.\\-#/ ]", "");

        if (siteAddress == null || siteAddress.trim().isEmpty()) {
            return "Site Address is required";
        }
        if (siteAddress.length() > 500) {
            return "Site Address length should be less than equal to 500";
        }
        if (!reqValidator.isValidAddress(siteAddress)) {
            return "Site Address contains invalid characters, Allowed - A-Z a-z 0-9 , . - # /";
        }
        return AppConstants.STATUS_YES;
    }
    
    // validate project
    private String isValidProjectName(String projectName) {
    if (projectName.length() > 500) {
        return "Site Address length should be less than equal to 500";
    }
    	 return AppConstants.STATUS_YES;
    }
    
    // validate state
    private String isValidState(String state) {
    	if (state.length() > 50) {
            return "State should be less than equal to 50";
        }
    	if(!reqValidator.isAlphaWithSpaceAndAmpersand(state)) {
    		return "State contains invalid characters, Allowed - a-z A-Z &";
    	}
        	 return AppConstants.STATUS_YES;
    }
    
    // validate district
    private String isValidDistrict(String district) {
    	if(district.length()>50) {
    		return "District should be less than equal to 50";
    	}
    	if(reqValidator.isAlphaWithSpace(district)) {
    		return "District contains invalid characters, Allowed - a-z A-Z";
    	}
    		
    	return AppConstants.STATUS_YES;
    }
    private String isValidSiteArea(Integer siteArea) {
        if (siteArea == null || siteArea < 1 || siteArea > 10000) {
            return "Site Area should be between 1 and 10000";
        }
        return AppConstants.STATUS_YES;
    }
    
    private String isValidChannelManagerId(Integer channelManagerId) {
        if (channelManagerId == null || channelManagerId <= 0) {
            return "Invalid Channel Manager ID";
        }
        return AppConstants.STATUS_YES;
    }
}