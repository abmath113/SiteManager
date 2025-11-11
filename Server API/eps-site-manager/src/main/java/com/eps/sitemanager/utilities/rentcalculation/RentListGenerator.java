package com.eps.sitemanager.utilities.rentcalculation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentListGenerator {

    //Generates list of rents for full span and adjusts for payment intervals.

    public static List<Integer> generateRentList(
            LocalDate agreementDate, LocalDate rentStartDate,
            LocalDate agreementEndDate, LocalDate terminationDate,
            int escalationAfterMonths, int escalationPercent,
            int initialRent, PaymentInterval paymentInterval
    ){
        RentCalculationValidator.validateInputs(agreementDate, rentStartDate, agreementEndDate,
                escalationAfterMonths, escalationPercent, initialRent);

        List<Integer> rentList = new ArrayList<>();
        LocalDate runningDate = rentStartDate;

        while(runningDate.isBefore(agreementEndDate)){
            rentList.add(RentEscalationCalculator.calculateRent(
                    agreementDate, rentStartDate, agreementEndDate, terminationDate,
                    runningDate, escalationAfterMonths, escalationPercent, initialRent
            ));
            runningDate = runningDate.plusMonths(1);
        }

        if(paymentInterval != PaymentInterval.MONTHLY && paymentInterval!=PaymentInterval.RENT_FREE){
            adjustForInterval(rentList, paymentInterval.getValue());
        }
        if(paymentInterval ==PaymentInterval.RENT_FREE){
            rentList.replaceAll(i -> 0);
        }

        return rentList;
    }

    private static void adjustForInterval(List<Integer> rentList, int monthsPerPayment){

        for (int i = 0; i < rentList.size(); i += monthsPerPayment) {
            int sum = 0;
            for (int j = i; j < i + monthsPerPayment && j < rentList.size(); j++)
                sum += rentList.get(j);

            rentList.set(i, sum);
            for (int j = i + 1; j < i + monthsPerPayment && j < rentList.size(); j++)
                rentList.set(j, 0);
        }
    }


}
