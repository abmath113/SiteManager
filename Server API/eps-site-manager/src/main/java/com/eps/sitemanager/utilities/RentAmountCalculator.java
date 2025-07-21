
package com.eps.sitemanager.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eps.sitemanager.configurations.error.ParameterConstraintException;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.services.rentmanager.RentManagerService;

@Component
public class RentAmountCalculator {

	private static RentAgreementMasterService rentAgreementMasterService;
	private static RentManagerService rentManagerService;
	
	private void validateInputs(LocalDate agreementDate, LocalDate rentPayStartDate, 
	        LocalDate agreementEndDate, int escalationAfterMonths, 
	        double escalationPercent, int initialRent) {
		
		
	    if (agreementDate == null ) {
	        throw new ParameterConstraintException("agreementDate cannot be null");
	    }

	    if  (rentPayStartDate == null ) {
	        throw new ParameterConstraintException("rentPayStartDate cannot be null");
	    }

	    if (agreementEndDate == null) {
	        throw new ParameterConstraintException("agreementEndDate cannot be null");
	    }
	    if (agreementDate.isAfter(rentPayStartDate)) {
	        throw new ParameterConstraintException("Agreement date must be before or equal to rent start date");
	    }
	    if (rentPayStartDate.isAfter(agreementEndDate)) {
	        throw new ParameterConstraintException("Rent start date must be before agreement end date");
	    }
	    if (escalationAfterMonths <= 0) {
	        throw new ParameterConstraintException("Escalation months must be positive");
	    }
	    if (escalationPercent < 0) {
	        throw new ParameterConstraintException("Escalation percentage cannot be negative");
	    }
	    if (initialRent <= 0) {
	        throw new ParameterConstraintException("Initial rent must be positive");
	    }
	}

	@Autowired
	public RentAmountCalculator(RentAgreementMasterService rentAgreementMasterService,
			RentManagerService rentManagerService) {
		RentAmountCalculator.rentAgreementMasterService = rentAgreementMasterService;
		RentAmountCalculator.rentManagerService = rentManagerService;
	}

	public RentAmountCalculator() {
		// TODO Auto-generated constructor stub
	}

	public enum PaymentInterval {

		MONTHLY(1), QUARTERLY(3);

		private int paymentIntervalVal;

		PaymentInterval(int paymentIntervalVal) {
			this.paymentIntervalVal = paymentIntervalVal;
		}

		public int getPaymentIntervalVal() {
			return paymentIntervalVal;
		}
	}

	public static Map<String, Integer> calculateRentListwithDate(int agreementId) {

		if (rentAgreementMasterService == null) {
			throw new IllegalStateException("rentAgreementMasterService has not been initialized");
		}

		// System.out.println("this is calculateRentListwithDate");
		Map<String, Integer> rentlistwithDateMap = new HashMap<>();

		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterById(agreementId);

		if (!rentAgreementMasterOpt.isPresent()) {
			throw new EntityNotFoundException("Rent Agreement not found for ID: " + agreementId);
		}

		RentAgreementMaster rentAgreementMaster = rentAgreementMasterOpt.get();

		// Extract necessary fields from RentAgreementMaster
		LocalDate agreementDate = rentAgreementMaster.getConsiderAgreementDate();
		LocalDate rentPayStartDate = rentAgreementMaster.getRentPayStartDate();
		LocalDate agreementEndDate = rentAgreementMaster.getAgreementEndDate();
		int agreementSpan = rentAgreementMaster.getAgreementSpan();
		int escalationAfterMonths = rentAgreementMaster.getEscalationAfterMonths();
		int escalationPercent = rentAgreementMaster.getEscalationPercent();
		int initialRent = rentAgreementMaster.getMonthlyRent();
		LocalDate terminationDate = rentAgreementMaster.getTerminationDate();
		
		// Validate inputs
		
		RentAmountCalculator rentAmountCalculator = new RentAmountCalculator();
		rentAmountCalculator.validateInputs(
				agreementDate, 
				rentPayStartDate, 
				agreementEndDate, 
				escalationAfterMonths, 
				escalationPercent,
				initialRent);
		
		// Correctly handle the PaymentInterval enum
		PaymentInterval paymentInterval = rentAgreementMaster.getPaymentInterval() == 1 ? PaymentInterval.MONTHLY
				: PaymentInterval.QUARTERLY;

		// for getting the rentlist for any particular agreementid
		List<Integer> rentList = RentAmountCalculator.calculateRentList(agreementDate, rentPayStartDate,
				agreementEndDate, terminationDate,
//                    agreementSpan,
				escalationAfterMonths, escalationPercent, initialRent, paymentInterval

		);

		// Create the map with formatted dates and rent amounts
		LocalDate currentDate = agreementDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");

		for (int i = 0; i < rentList.size(); i++) {
			String formattedDate = currentDate.format(formatter);
			rentlistwithDateMap.put(formattedDate, rentList.get(i));
			currentDate = currentDate.plusMonths(1);
		}

		return rentlistwithDateMap;
	}

	// This method returns the list of calculated rent for each month for full
	// agreement span

	public static List<Integer> calculateRentList(

			LocalDate agreementDate, LocalDate rentPayStartDate, LocalDate agreementEndDate, LocalDate terminationDate,
//	 			int agreementSpan,
			int escalationAfterMonths, int escalationPercent, int initialRent, PaymentInterval paymentInterval) {
		
		// Validate inputs
		
				RentAmountCalculator rentAmountCalculator = new RentAmountCalculator();
				rentAmountCalculator.validateInputs(
						agreementDate, 
						rentPayStartDate, 
						agreementEndDate, 
						escalationAfterMonths, 
						escalationPercent,
						initialRent);

		List<Integer> rentList = new ArrayList<>();

		LocalDate runningDate = rentPayStartDate;
//		int agreementSpan = (int) ChronoUnit.MONTHS.between(agreementDate, agreementEndDate);

		if (terminationDate != null) {
			agreementEndDate = terminationDate;
		}

		while (runningDate.isBefore(agreementEndDate)) {
			int calculatedRent = rentAmountCalculation(agreementDate, rentPayStartDate, agreementEndDate,
					agreementEndDate, runningDate, escalationAfterMonths, escalationPercent, initialRent); //why is this code leading to null ptr exception
			
	
			
			
			rentList.add(calculatedRent);
			runningDate = runningDate.plusMonths(1);
		}

		if (paymentInterval == PaymentInterval.QUARTERLY) {
			for (int i = 1; i < rentList.size(); i += 3) {
				int runningSum = 0;
				for (int j = i; j < i + 3 && j < rentList.size(); j++) {
					runningSum += rentList.get(j);
				}
				rentList.set(i, runningSum);
				if (i + 1 < rentList.size())
					rentList.set(i + 1, 0);
				if (i + 2 < rentList.size())
					rentList.set(i + 2, 0);
			}
		}

		return rentList;
	}

	// Function for calculating rent for currDate
	public static int rentAmountCalculation(LocalDate agreementDate, LocalDate rentPayStartDate,
			LocalDate agreementEndDate, LocalDate terminationDate, LocalDate currDate, int escalationAfterMonths,
			double escalationPercent, int initialRent) {
		
		// Validate inputs
		
		RentAmountCalculator rentAmountCalculator = new RentAmountCalculator();
		rentAmountCalculator.validateInputs(
				agreementDate, 
				rentPayStartDate, 
				agreementEndDate, 
				escalationAfterMonths, 
				escalationPercent,
				initialRent);


		int calculatedRent = 0;
		int agreementSpan = (int) ChronoUnit.MONTHS.between(agreementDate, agreementEndDate); // in months

		// Check if current date is valid in terms of the agreement span
		if (currDate.isBefore(agreementDate)) {
			throw new ParameterConstraintException("Agreement Tenure is yet to start");
		}

		if (currDate.isAfter(agreementEndDate)) {
			throw new ParameterConstraintException("Agreement Tenure has expired");
		}

		if (terminationDate != null) {
			agreementEndDate = terminationDate;
		}

		// Calculate rent month by month, checking for escalations
		int runningRent = initialRent;
		LocalDate runningProcessDate = rentPayStartDate;

		// Loop through months of agreement span
		for (int agreementRunningMonth = 0; agreementRunningMonth <= agreementSpan; agreementRunningMonth++) {
			runningProcessDate = runningProcessDate.plusMonths(1);
			int lastDayOfMonth = runningProcessDate.lengthOfMonth();

			// Partial rent calculation for the first month of agreement
			if (currDate.getYear() == agreementDate.getYear() && currDate.getMonth().equals(agreementDate.getMonth())) {
				calculatedRent = (lastDayOfMonth - agreementDate.getDayOfMonth()) * (runningRent / lastDayOfMonth);
				return calculatedRent;
			}

			// Handle rent escalation
			if ((agreementRunningMonth + 1) % escalationAfterMonths == 0) {

				double daysBeforeEscalation = agreementDate.getDayOfMonth() - 1;
				double daysInMonth = runningProcessDate.lengthOfMonth();

				double perDayRent = runningRent / daysInMonth;
				double beforeEscalationRent = daysBeforeEscalation * perDayRent;

				// Update rent after escalation
				runningRent = (int) (runningRent * (1 + escalationPercent / 100.0));
				double daysAfterEscalation = runningProcessDate.lengthOfMonth() - daysBeforeEscalation;
				double afterEscalationRent = daysAfterEscalation * (runningRent / daysInMonth);

				calculatedRent = (int) (beforeEscalationRent + afterEscalationRent);
			} else {
				calculatedRent = runningRent;
			}

			// Exit if current date matches the running date
			if (currDate.getYear() == runningProcessDate.getYear()
					&& currDate.getMonth().equals(runningProcessDate.getMonth())) {
				return calculatedRent;
			}
		}

		return calculatedRent;
	}
}
