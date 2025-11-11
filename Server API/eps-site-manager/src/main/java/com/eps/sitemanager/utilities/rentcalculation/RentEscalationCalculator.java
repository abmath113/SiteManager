package com.eps.sitemanager.utilities.rentcalculation;

import com.eps.sitemanager.configurations.error.ParameterConstraintException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentEscalationCalculator {


    public static int calculateRent(
            LocalDate agreementDate, LocalDate rentStartDate,
            LocalDate agreementEndDate, LocalDate terminationDate,
            LocalDate currentDate, int escalationAfterMonths,
            double escalationPercent, int initialRent
    ){
        RentCalculationValidator.validateInputs(agreementDate,rentStartDate,
                agreementEndDate,escalationAfterMonths,escalationPercent,initialRent);


        if(currentDate.isBefore(agreementEndDate)){
            throw new ParameterConstraintException("Agreement tenure is yet to start cause ," +
                    " agreementEndDate cannot be before agreementStartDate");
        }
        if(terminationDate != null && terminationDate.isBefore(currentDate)){
            throw new ParameterConstraintException("Agreement has expired");
        }
        int agreementSpan = (int) ChronoUnit.MONTHS.between(agreementDate, agreementEndDate);
        int runningRent = initialRent;
        LocalDate processDate = rentStartDate;

        for (int i = 0; i <= agreementSpan; i++) {
            processDate = processDate.plusMonths(1);

            if (escalationAfterMonths > 0 && (i + 1) % escalationAfterMonths == 0)
                runningRent = (int) (runningRent * (1 + escalationPercent / 100.0));

            if (currentDate.getYear() == processDate.getYear() &&
                    currentDate.getMonth() == processDate.getMonth())
                return runningRent;
        }

        return runningRent;

    }
}
