package com.eps.sitemanager.utilities.rentcalculation;

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
	    if (escalationAfterMonths < 0) {
	        throw new ParameterConstraintException("Escalation months must be positive");
	    }
	    if (escalationPercent < 0) {
	        throw new ParameterConstraintException("Escalation percentage cannot be negative");
	    }
	    if (initialRent < 	0) {
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

//	public enum PaymentInterval {
//
//		MONTHLY(1),
//		QUARTERLY(3),
//		HALF_YEARLY(6),
//		RENT_FREE(0);
//
//		private int paymentIntervalVal;
//
//		PaymentInterval(int paymentIntervalVal) {
//			this.paymentIntervalVal = paymentIntervalVal;
//		}
//
//		public int getPaymentIntervalVal() {
//			return paymentIntervalVal;
//		}
//
//		public static PaymentInterval fromValue(int value) {
//			for(PaymentInterval interval : values()) {
//				if(interval.getPaymentIntervalVal()==value) {
//					return interval;
//				}
//			}
//			throw new IllegalArgumentException("Unknown payment Interval value: " + value);
//		}
//
//	}

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
		
		
//		PaymentInterval paymentInterval = rentAgreementMaster.getPaymentInterval() == 1 ? PaymentInterval.MONTHLY
//				: PaymentInterval.QUARTERLY;
		
		// Correctly handle the PaymentInterval enum
		PaymentInterval paymentInterval = PaymentInterval.fromValue(rentAgreementMaster.getPaymentInterval());
		

		// for getting the rentlist for any particular agreementid
		List<Integer> rentList = RentAmountCalculator.calculateRentList(agreementDate, rentPayStartDate,
				agreementEndDate, terminationDate,
//                    agreementSpan,
				escalationAfterMonths, escalationPercent, initialRent, paymentInterval

		);

		// Create the map with formatted dates and rent amounts
		LocalDate currentDate = rentPayStartDate;
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

//	public static List<Integer> calculateRentList(
//
//			LocalDate agreementDate, LocalDate rentPayStartDate, LocalDate agreementEndDate, LocalDate terminationDate,
////	 			int agreementSpan,
//			int escalationAfterMonths, int escalationPercent, int initialRent, PaymentInterval paymentInterval) {
//		
//		// Validate inputs
//		
//				RentAmountCalculator rentAmountCalculator = new RentAmountCalculator();
//				rentAmountCalculator.validateInputs(
//						agreementDate, 
//						rentPayStartDate, 
//						agreementEndDate, 
//						escalationAfterMonths, 
//						escalationPercent,
//						initialRent);
//
//		List<Integer> rentList = new ArrayList<>();
//
//		LocalDate runningDate = rentPayStartDate;
////		int agreementSpan = (int) ChronoUnit.MONTHS.between(agreementDate, agreementEndDate);
//
//		if (terminationDate != null) {
//			agreementEndDate = terminationDate;
//		}
//		
//
//		while (!runningDate.isAfter(agreementEndDate)) {
//			int calculatedRent = rentAmountCalculation(agreementDate, rentPayStartDate, agreementEndDate,
//					terminationDate, runningDate, escalationAfterMonths, escalationPercent, initialRent); //why is this code leading to null ptr exception
//			
//	
//			
//			
//			rentList.add(calculatedRent);
//			runningDate = runningDate.plusMonths(1);
//			
//		}
//		
//		
//
////		if (paymentInterval == PaymentInterval.QUARTERLY) {
////			for (int i = 1; i < rentList.size(); i += 3) {
////				int runningSum = 0;
////				for (int j = i; j < i + 3 && j < rentList.size(); j++) {
////					runningSum += rentList.get(j);
////				}
////				rentList.set(i, runningSum);
////				if (i + 1 < rentList.size())
////					rentList.set(i + 1, 0);
////				if (i + 2 < rentList.size())
////					rentList.set(i + 2, 0);
////			}
////		}
////		
//
//		// execute if the payment interval is quarterly or half yearly
////		if (paymentInterval != PaymentInterval.MONTHLY && paymentInterval != PaymentInterval.RENT_FREE) {
////		    int interval = paymentInterval.getPaymentIntervalVal(); // 3 for Quarterly, 6 for Half-Yearly
////
////		    for (int i = 0; i < rentList.size(); i += interval) {
////		        int runningSum = 0;
////		        for (int j = i; j < i + interval && j < rentList.size(); j++) {
////		            runningSum += rentList.get(j);
////		        }
////
////		        rentList.set(i, runningSum); 
////
////		        for (int j = i + 1; j < i + interval && j < rentList.size(); j++) {
////		            rentList.set(j, 0); 
////		        }
////		    }
////		}
//
//
//
//		if (paymentInterval == PaymentInterval.QUARTERLY) {
//		    for (int i = 0; i < rentList.size(); i++) {
//		        LocalDate monthDate = rentPayStartDate.plusMonths(i);
//		        int month = monthDate.getMonthValue();
//
//		        // Sum only at Jan, Apr, Jul, Oct
//		        if (month == 1 || month == 4 || month == 7 || month == 10) {
//		            int runningSum = 0;
//		            for (int j = 0; j < 3 && i + j < rentList.size(); j++) {
//		                runningSum += rentList.get(i + j);
//		            }
//		            rentList.set(i, runningSum);
//		        } else {
//		            rentList.set(i, 0);
//		        }
//		    }
//		}
//
//		if (paymentInterval == PaymentInterval.HALF_YEARLY) {
//		    for (int i = 0; i < rentList.size(); i++) {
//		        LocalDate monthDate = rentPayStartDate.plusMonths(i);
//		        int month = monthDate.getMonthValue();
//
//		        // Sum only at Jan and Jul
//		        if (month == 1 || month == 7) {
//		            int runningSum = 0;
//		            for (int j = 0; j < 6 && i + j < rentList.size(); j++) {
//		                runningSum += rentList.get(i + j);
//		            }
//		            rentList.set(i, runningSum);
//		        } else {
//		            rentList.set(i, 0);
//		        }
//		    }
//		}
//
//		// set rent 0 for rent free sites
//		if(paymentInterval == PaymentInterval.RENT_FREE) {
//			for(int i = 1; i < rentList.size(); i++) {
//				rentList.set(i, 0);
//			}
//		}
//		return rentList;
//		
//
//	}
	
	public static List<Integer> calculateRentList(
	        LocalDate agreementDate,
	        LocalDate rentPayStartDate,
	        LocalDate agreementEndDate,
	        LocalDate terminationDate,
	        int escalationAfterMonths,
	        int escalationPercent,
	        int initialRent,
	        PaymentInterval paymentInterval) {

	    List<Integer> rentList = new ArrayList<>();

	    if (terminationDate != null && terminationDate.isBefore(agreementEndDate)) {
	        agreementEndDate = terminationDate;
	    }

	    LocalDate currentMonth = rentPayStartDate;

	    while (!currentMonth.isAfter(agreementEndDate)) {
	        int rentForMonth = calculateRentForDate(
	                rentPayStartDate,
	                agreementEndDate,
	                terminationDate,
	                escalationAfterMonths,
	                escalationPercent,
	                initialRent,
	                paymentInterval,
	                currentMonth
	        );
	        rentList.add(rentForMonth);
	        currentMonth = currentMonth.plusMonths(1);
	    }

	    return rentList;
	}


	// Function for calculating rent for currDate
//	public static int rentAmountCalculation(LocalDate agreementDate, LocalDate rentPayStartDate,
//			LocalDate agreementEndDate, LocalDate terminationDate, LocalDate currDate, int escalationAfterMonths,
//			double escalationPercent, int initialRent) {
//		
//		// Validate inputs
//		
//		RentAmountCalculator rentAmountCalculator = new RentAmountCalculator();
//		rentAmountCalculator.validateInputs(
//				agreementDate, 
//				rentPayStartDate, 
//				agreementEndDate, 
//				escalationAfterMonths, 
//				escalationPercent,
//				initialRent);
//
//
//		int calculatedRent = 0;
//		int agreementSpan = (int) ChronoUnit.MONTHS.between(rentPayStartDate, agreementEndDate); // in months
//
//		// Check if current date is valid in terms of the agreement span
//		if (currDate.isBefore(agreementDate)) {
//			throw new ParameterConstraintException("Agreement Tenure is yet to start");
//		}
//
//		if (currDate.isAfter(agreementEndDate)) {
//			throw new ParameterConstraintException("Agreement Tenure has expired");
//		}
//
//		if (terminationDate != null) {
//			agreementEndDate = terminationDate;
//		}
//
//
//		// Calculate rent month by month, checking for escalations
//		int runningRent = initialRent;
//		LocalDate runningProcessDate = rentPayStartDate;
//
//		// Loop through months of agreement span
//		
//		for (int agreementRunningMonth = 0; agreementRunningMonth <= agreementSpan; agreementRunningMonth++) {
//			runningProcessDate = runningProcessDate.plusMonths(1);
//			int lastDayOfMonth = runningProcessDate.lengthOfMonth();
//
//			// Partial rent calculation for the first month of agreement
//			if (currDate.getYear() == agreementDate.getYear() && currDate.getMonth().equals(agreementDate.getMonth())) {
//				calculatedRent = (lastDayOfMonth - agreementDate.getDayOfMonth()) * (runningRent / lastDayOfMonth);
//				return calculatedRent;
//			}
//
//			// Handle rent escalation
//		
//			// Calculate months since rent start
//			int monthsSinceStart = (int) ChronoUnit.MONTHS.between(rentPayStartDate, currDate);
//			int escalationsPassed = 0;
//
//			// Apply escalation only if past threshold
//			if (escalationAfterMonths > 0 && monthsSinceStart >= escalationAfterMonths) {
//				 escalationsPassed = (monthsSinceStart - escalationAfterMonths) / escalationAfterMonths + 1;
//			}
//
//			// Calculate effective rent for the current month
//			calculatedRent = (int) (initialRent * Math.pow(1 + escalationPercent / 100.0, escalationsPassed));
//
//
//			// Exit if current date matches the running date
//			if (currDate.getYear() == rentPayStartDate.getYear() && 
//				    currDate.getMonth().equals(rentPayStartDate.getMonth())) {
//				    calculatedRent = (lastDayOfMonth - rentPayStartDate.getDayOfMonth()) 
//				                     * (runningRent / lastDayOfMonth);
//				    return calculatedRent;
//				}
//
//		}
//
//		return calculatedRent;
//	}
	
	// new code to calculate monthly rent 
	public static int calculateRentForDate(
	        LocalDate rentPayStartDate,
	        LocalDate agreementEndDate,
	        LocalDate terminationDate,
	        int escalationAfterMonths,
	        double escalationPercent,
	        int initialRent,
	        PaymentInterval paymentInterval,
	        LocalDate currDate) {

	    if (terminationDate != null && terminationDate.isBefore(agreementEndDate)) {
	        agreementEndDate = terminationDate;
	    }

	    if (currDate.isBefore(rentPayStartDate) || currDate.isAfter(agreementEndDate)) {
	        return 0; // Outside agreement duration
	    }

	    // Months since start for escalation logic
	    int monthsSinceStart = (int) ChronoUnit.MONTHS.between(rentPayStartDate, currDate);
	    int escalationsPassed = 0;

	    if (escalationAfterMonths > 0 && monthsSinceStart >= escalationAfterMonths) {
	        escalationsPassed = monthsSinceStart / escalationAfterMonths;
	    }

	    // Escalated monthly rent
	    double escalatedRent = initialRent * Math.pow(1 + escalationPercent / 100.0, escalationsPassed);

	    // Handle payment intervals
	    if (paymentInterval == PaymentInterval.MONTHLY) {
	        return (int) Math.round(escalatedRent);
	    }

	    else if (paymentInterval == PaymentInterval.QUARTERLY) {
	        int month = currDate.getMonthValue();

	        // Determine if current month is an anchor month (Jan, Apr, Jul, Oct)
	        boolean isAnchor = (month == 1 || month == 4 || month == 7 || month == 10);

	        if (isAnchor) {
	            // Add rents of 3 months in that quarter
	            double totalQuarterRent = 0;
	            for (int i = 0; i < 3; i++) {
	                LocalDate targetMonth = currDate.plusMonths(i);
	                if (targetMonth.isAfter(agreementEndDate)) break;

	                int monthsSince = (int) ChronoUnit.MONTHS.between(rentPayStartDate, targetMonth);
	                int escalations = escalationAfterMonths > 0 && monthsSince >= escalationAfterMonths
	                        ? monthsSince / escalationAfterMonths
	                        : 0;

	                double rentForThatMonth = initialRent * Math.pow(1 + escalationPercent / 100.0, escalations);
	                totalQuarterRent += rentForThatMonth;
	            }
	            return (int) Math.round(totalQuarterRent);
	        } else {
	            return 0;
	        }
	    }

	    else if (paymentInterval == PaymentInterval.HALF_YEARLY) {
	        int month = currDate.getMonthValue();
	        boolean isAnchor = (month == 1 || month == 7); // Jan, Jul anchors

	        if (isAnchor) {
	            double totalHalfYearRent = 0;
	            for (int i = 0; i < 6; i++) {
	                LocalDate targetMonth = currDate.plusMonths(i);
	                if (targetMonth.isAfter(agreementEndDate)) break;

	                int monthsSince = (int) ChronoUnit.MONTHS.between(rentPayStartDate, targetMonth);
	                int escalations = escalationAfterMonths > 0 && monthsSince >= escalationAfterMonths
	                        ? monthsSince / escalationAfterMonths
	                        : 0;

	                double rentForThatMonth = initialRent * Math.pow(1 + escalationPercent / 100.0, escalations);
	                totalHalfYearRent += rentForThatMonth;
	            }
	            return (int) Math.round(totalHalfYearRent);
	        } else {
	            return 0;
	        }
	    }

	    else if (paymentInterval == PaymentInterval.RENT_FREE) {
	        return 0;
	    }

	    return (int) Math.round(escalatedRent);
	}



}


