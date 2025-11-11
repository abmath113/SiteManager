package com.eps.sitemanager.utilities.rentcalculation;

import com.eps.sitemanager.configurations.error.ParameterConstraintException;

import java.time.LocalDate;

public class RentCalculationValidator {


    public static void validateInputs(LocalDate agreementDate, LocalDate rentPayStartDate,
                                      LocalDate agreementEndDate, int escalationAfterMonths,
                                      double escalationPercent, int initialRent) {

        if (agreementDate == null)
            throw new ParameterConstraintException("agreementDate cannot be null");
        if (rentPayStartDate == null)
            throw new ParameterConstraintException("rentPayStartDate cannot be null");
        if (agreementEndDate == null)
            throw new ParameterConstraintException("agreementEndDate cannot be null");

        if (agreementDate.isAfter(rentPayStartDate))
            throw new ParameterConstraintException("Agreement date must be before rent start date");
        if (rentPayStartDate.isAfter(agreementEndDate))
            throw new ParameterConstraintException("Rent start date must be before agreement end date");
        if (escalationAfterMonths < 0)
            throw new ParameterConstraintException("Escalation months must be positive");
        if (escalationPercent < 0)
            throw new ParameterConstraintException("Escalation percentage cannot be negative");
        if (initialRent < 0)
            throw new ParameterConstraintException("Initial rent must be positive");
    }



}
