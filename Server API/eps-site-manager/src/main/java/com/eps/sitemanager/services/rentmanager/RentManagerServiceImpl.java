package com.eps.sitemanager.services.rentmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.SiteRentRecordDTO;
import com.eps.sitemanager.dto.rentmanager.GeneratedRentDTO;
import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.services.SiteRentRecordService;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.utilities.rentcalculation.PaymentInterval;
import com.eps.sitemanager.utilities.rentcalculation.RentAmountCalculator;


@Service
public class RentManagerServiceImpl implements RentManagerService {

	private RentAgreementMasterService rentAgreementMasterService;
	
	private SiteRentRecordService siteRentRecordService;
	

	@Autowired
	public RentManagerServiceImpl(RentAgreementMasterService rentAgreementMasterService,
			SiteRentRecordService siteRentRecordService) {
		super();
		this.rentAgreementMasterService = rentAgreementMasterService;
		this.siteRentRecordService = siteRentRecordService;
	}

	// function for generation of list of predicted rent for any given site.
	@Override
	public List<Integer> getRentListByAgreementId(int agreementId) {
		
		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterById(agreementId);

		if (!rentAgreementMasterOpt.isPresent()) {
			throw new EntityNotFoundException("Rent Agreement not found for ID: " + agreementId);
		}

		RentAgreementMaster rentAgreementMaster = rentAgreementMasterOpt.get();

		// Extract necessary fields from RentAgreementMaster
		LocalDate agreementDate = rentAgreementMaster.getAgreementDate();
		LocalDate rentPayStartDate = rentAgreementMaster.getRentPayStartDate();
		LocalDate terminationDate = rentAgreementMaster.getTerminationDate();
		LocalDate agreementEndDate = rentAgreementMaster.getAgreementEndDate();
		int agreementSpan = rentAgreementMaster.getAgreementSpan();
		int escalationAfterMonths = rentAgreementMaster.getEscalationAfterMonths();
		int escalationPercent = rentAgreementMaster.getEscalationPercent();
		int initialRent = rentAgreementMaster.getMonthlyRent();
		

		// Correctly handle the PaymentInterval enum
		
		PaymentInterval paymentInterval = rentAgreementMaster.getPaymentInterval() == 1 ? PaymentInterval.MONTHLY
				: PaymentInterval.QUARTERLY;

		// Call the RentAmountCalculator to get the rent list
		return RentAmountCalculator.calculateRentList(
				agreementDate,
				rentPayStartDate,
				agreementEndDate,
				terminationDate,
				escalationAfterMonths, 
				escalationPercent, 
				initialRent, 
				paymentInterval);
	}
	
	// this function is used for generating the rent for all sites for given date.
	@Override
	public List<GeneratedRentDTO> getAllRentbyCurrDate(LocalDate currDate) {
		List<GeneratedRentDTO> rentAgreementMasterList = new ArrayList<>();
		Iterable<RentAgreementMaster> rentAgreementMasters = rentAgreementMasterService.getAllRentAgreementMaster();
		
		for(RentAgreementMaster rentAgreement  : rentAgreementMasters) {
			// Extract required parameters
						int agreementId = rentAgreement.getAgreementId();
						String landlordName = rentAgreement.getLandlordId().getName();
                        String landlordMobileNo = rentAgreement.getLandlordId().getMobileNo();
                        String landlordIFSC = rentAgreement.getLandlordId().getIfscCode();
                        String landlordAccountNo = rentAgreement.getLandlordId().getAccountNo();
                        Boolean landlordGSTStatus = rentAgreement.getLandlordId().isGST();

						String siteCode = rentAgreement.getSiteId().getSiteCode();
						Boolean siteStatus = rentAgreement.getSiteId().isSiteStatus();
						LocalDate agreementDate = rentAgreement.getAgreementDate();
						LocalDate rentPayStartDate = rentAgreement.getRentPayStartDate();
						LocalDate agreementEndDate = rentAgreement.getAgreementEndDate();
						LocalDate terminationDate = rentAgreement.getTerminationDate();
						int paymentIntervalInt = rentAgreement.getPaymentInterval();
						PaymentInterval paymentInterval = (paymentIntervalInt == 1) ? PaymentInterval.MONTHLY
								: PaymentInterval.QUARTERLY;
					//	int agreementSpan = rentAgreement.getAgreementSpan();
						int escalationAfterMonths = rentAgreement.getEscalationAfterMonths();
						int escalationPercent = rentAgreement.getEscalationPercent();
						int initialRent = rentAgreement.getMonthlyRent();
						boolean agreementStatus = rentAgreement.isRentAgreementStatus();

						List<Integer> rentList = RentAmountCalculator.calculateRentList(
								agreementDate, 
								rentPayStartDate,
								agreementEndDate,
								terminationDate,
								escalationAfterMonths,
								escalationPercent, 
								initialRent, 
								paymentInterval);
						
						int amountToPay =-1;
						int elementToAccess = ((int) ChronoUnit.MONTHS.between(rentPayStartDate, currDate)+1);
						
						if(elementToAccess>=0 && elementToAccess < rentList.size()) {
							amountToPay = rentList.get(elementToAccess);
						}
						
						// Format the current date to yyyy-MM format for rentMonth
			            String rentMonth = currDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
			            
			            // Check if a record already exists for this site and month
			            Optional<SiteRentRecord> existingRecord = siteRentRecordService
			                .findBySiteCodeAndRentMonth(siteCode, rentMonth);
			            
			            if (!existingRecord.isPresent()) {
			                // Create new SiteRentRecordDTO for saving
			                SiteRentRecordDTO siteRentRecordDTO = new SiteRentRecordDTO();
			                siteRentRecordDTO.setAgreementId(rentAgreement);
			                siteRentRecordDTO.setGeneratedRent(amountToPay);
			                siteRentRecordDTO.setRentMonth(rentMonth);
			                siteRentRecordDTO.setSiteCode(siteCode);
			                siteRentRecordDTO.setTransactionStatus(false); // Initial status as not paid
			                
			                // Save the record
			                
			                    siteRentRecordService.saveSiteRentRecord(siteRentRecordDTO); //err
			                    
			                
			            }
			        
						// Create a new GeneratedRentDTO and set its values
						GeneratedRentDTO generatedRentDTO = new GeneratedRentDTO();
						generatedRentDTO.setLandlordName(landlordName);
                        generatedRentDTO.setLandlordMobileNo(landlordMobileNo);
                        generatedRentDTO.setLandlordAccountNo(landlordAccountNo);
                        generatedRentDTO.setLandlordIFSC(landlordIFSC);
						generatedRentDTO.setSiteCode(siteCode);
						generatedRentDTO.setRentToPay(amountToPay);
						generatedRentDTO.setMonthlyRent(initialRent);
						generatedRentDTO.setPaymentInterval(paymentInterval);;
						generatedRentDTO.setRentAgreementStatus(agreementStatus);
                        generatedRentDTO.setLandlordGSTStatus(landlordGSTStatus);
						
						// Add the generated DTO to the list
						rentAgreementMasterList.add(generatedRentDTO);				
						
		}
		
		return rentAgreementMasterList;
	}
	
    //  method to get rent for a given site code and date
    public int getRentForSiteCodeByDate(String siteCode, LocalDate currDate) {
    	
    	 // Call getAllRentbyCurrDate to get the list of rents for the current date
        List<GeneratedRentDTO> rentAgreementMasterList = getAllRentbyCurrDate(currDate);
    	
        // Traverse the list to find the matching site code
        for (GeneratedRentDTO rentDTO : rentAgreementMasterList) {
            if (rentDTO.getSiteCode().equals(siteCode)) {
                // Return the rent for the matching site code
                return rentDTO.getRentToPay();
            }
        }
    	
    	return -1;

    }
}
