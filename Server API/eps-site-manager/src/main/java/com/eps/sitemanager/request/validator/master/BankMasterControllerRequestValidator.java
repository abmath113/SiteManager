package com.eps.sitemanager.request.validator.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.BankMasterDTO;
import com.eps.sitemanager.utilities.AppConstants;
import com.eps.sitemanager.utilities.RequestDataValidator;

@Component
public class BankMasterControllerRequestValidator {

    private RequestDataValidator reqValidator;

    // Constructor injection of RequestDataValidator
    @Autowired
    public BankMasterControllerRequestValidator(RequestDataValidator reqValidator) {
        super();
        this.reqValidator = reqValidator;
    }

    // Method to validate save/update request for BankMaster
    public RequestValidaton validateSaveUpdateRequest(BankMasterDTO bankMasterDTO) {
        RequestValidaton reqValidation = new RequestValidaton();

        // Validate the Bank Name
        String nameValidation = isValidBankName(bankMasterDTO.getBankName());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(nameValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + nameValidation);
            return reqValidation;
        }

        // Validate the Bank Code
        String codeValidation = isValidBankCode(bankMasterDTO.getBankCode());
        if (!AppConstants.STATUS_YES.equalsIgnoreCase(codeValidation)) {
            reqValidation.setState(false);
            reqValidation.setStatus(AppConstants.STATUS_INVALID);
            reqValidation.setErrorMessage(AppConstants.ERROR_TYPE_INVALID_PARAMETER + codeValidation);
            return reqValidation;
        }

        return reqValidation; // Return success validation if all fields are valid
    }

    // Method to validate bank name
    private String isValidBankName(String bankName) {
        if (bankName == null || bankName.isEmpty()) {
            return "Bank Name is required";
        }
        if (bankName.trim().isEmpty()) {
            return "Bank Name cannot be empty or contain only spaces";
        }
        if (!reqValidator.isAlphNumericWithSpace(bankName)) {
            return "Bank Name contains invalid characters, Allowed - A-Z a-z 0-9 and spaces";
        }
        if (bankName.length() > 100) { // Assuming max length constraint
            return "Bank Name should not exceed 100 characters";
        }
        return AppConstants.STATUS_YES;
    }

    // Method to validate bank code
    private String isValidBankCode(String bankCode) {
        if (bankCode == null || bankCode.isEmpty()) {
            return "Bank Code is required";
        }
        if (bankCode.trim().isEmpty()) {
            return "Bank Code cannot be empty or contain only spaces";
        }
        if (!reqValidator.isAlphaNumeric(bankCode)) {
            return "Bank Code should contain only alphanumeric characters";
        }
        if (bankCode.length() < 2 || bankCode.length() > 20) { // Assuming length constraints
            return "Bank Code should be between 2 and 20 characters";
        }
        return AppConstants.STATUS_YES;
    }
}