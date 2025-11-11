package com.eps.sitemanager.request.validator.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.ChannelManagerDTO;
import com.eps.sitemanager.utilities.AppConstants;
import com.eps.sitemanager.utilities.RequestDataValidator;

@Component
public class ChannelManagerControllerRequestValidator {

    private RequestDataValidator reqValidator;

    // Constructor injection of RequestDataValidator
    @Autowired
    public ChannelManagerControllerRequestValidator(RequestDataValidator reqValidator) {
        super();
        this.reqValidator = reqValidator;
    }

    // Method to validate save/update request for ChannelManager
    public RequestValidaton validateSaveUpdateRequest(ChannelManagerDTO channelManagerDTO) {
        RequestValidaton reqValidation = new RequestValidaton();

        // Validate the Channel Manager name
        String nameValidation = isValidName(channelManagerDTO.getChannelManagerName());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(nameValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + nameValidation);
            return reqValidation;
        }

        // Validate the phone number
        String phoneNoValidation = isValidPhoneNo(channelManagerDTO.getPhoneNo());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(phoneNoValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + phoneNoValidation);
            return reqValidation;
        }

        // Validate the email ID
        String emailValidation = isValidEmailId(channelManagerDTO.getEmailId());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(emailValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + emailValidation);
            return reqValidation;
        }

        return reqValidation; // Return success validation if all fields are valid
    }

    // Method to validate name
    private String isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return "Channel Manager Name is required";
        }
        if (!reqValidator.isAlphNumericWithSpace(name)) {
            return "Channel Manager Name contains invalid characters, Allowed - A-Z a-z";
        }
        return AppConstants.STATUS_YES;
    }

    // Method to validate phone number
    private String isValidPhoneNo(String phoneNo) {
        String phoneNoString = String.valueOf(phoneNo);
        if (phoneNoString == null || phoneNoString.isEmpty()) {
            return "Phone Number is required";
        }
        if (!reqValidator.isNumericString(phoneNoString) || phoneNoString.length() != 10) {
            return "Phone Number should be 10 digits";
        }
        return AppConstants.STATUS_YES;
    }

    // Method to validate email ID
    private String isValidEmailId(String emailId) {
        if (emailId == null || emailId.isEmpty()) {
            return "Email ID is required";
        }
        if (!reqValidator.isValidEmailId(emailId)) {
            return "Invalid Email ID format";
        }
        return AppConstants.STATUS_YES;
    }
}
