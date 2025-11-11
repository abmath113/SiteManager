package com.eps.sitemanager.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.model.RentExcelInfo;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.services.master.SiteMasterService;
import com.eps.sitemanager.services.rentmanager.RentManagerService;
import com.eps.sitemanager.utilities.rentcalculation.RentAmountCalculator;
import com.github.crab2died.ExcelUtils;


@Service
public class RentExcelInfoServiceImpl implements RentExcelInfoService {
	
	private SiteMasterService siteMasterService;
	private RentManagerService rentManagerService;
	private RentAgreementMasterService rentAgreementMasterService;
	
	@Autowired
	public RentExcelInfoServiceImpl(SiteMasterService siteMasterService,RentManagerService rentManagerService,RentAgreementMasterService rentAgreementMasterService) {
		this.siteMasterService = siteMasterService;
		this.rentManagerService = rentManagerService;
		this.rentAgreementMasterService = rentAgreementMasterService;
	}
	
	// parse excel data and map it RentExcelInfo object
	public List<RentExcelInfo> parseExcel(MultipartFile file) throws Exception {
		
	    // Parse the Excel file into a list of RentExcelInfo objects
	    List<RentExcelInfo> rentInfoList = ExcelUtils.getInstance().readExcel2Objects(file.getInputStream(), RentExcelInfo.class);
	    
	    
	    // Filter out empty rows
	    rentInfoList = rentInfoList.stream()
	        .filter(rentInfo -> !isRowEmpty(rentInfo)) // Filter out rows where all fields are null or empty
	        .collect(Collectors.toList());

	    // Convert String dates to LocalDate and validate
	    for (RentExcelInfo rentInfo : rentInfoList) {
	        // Trim and clean the remarks to avoid issues with "" or spaces
	        String remarks = rentInfo.getRemarks();
	        if (remarks != null) {
	            remarks = remarks.trim(); // Remove extra spaces
	            if (remarks.equals("\"\"")) {
	                remarks = ""; // Treat "" as empty
	            }
	        }
	        rentInfo.setRemarks(remarks); // Update the cleaned remarks

	        try {
	            LocalDate localDate = rentInfo.convertToLocalDate();
	            if (localDate == null) {
	                System.out.println("Warning: Invalid or empty date for rentInfo: " + rentInfo);
	            }
	            // Additional processing for localDate can go here
	        } catch (DateTimeParseException e) {
	            System.out.println("Error parsing date for rentInfo: " + rentInfo + ". Error: " + e.getMessage());
	        }
	    }

	    return rentInfoList;
	}

	// Method to check if a row is empty
	public boolean isRowEmpty(RentExcelInfo rentInfo) {
	    return rentInfo.getExcelSiteCode() == null  ||
	           rentInfo.convertToLocalDate() == null ||
	           rentInfo.getAmountPaid() == null;
	}

	
    public boolean doesExcelSiteExistInDB(String excelSiteCode) {
        
        return siteMasterService.checkSiteCodeExists(excelSiteCode); // Use the method from SiteMasterService
    }
    
    public int getRentForMonth(String siteCode, LocalDate date) {
         
        return rentManagerService.getRentForSiteCodeByDate(siteCode, date);
    }

	public List<String> getNonExistentExcelSiteCodes(List<RentExcelInfo> RentInfoExcel) {
		
		 List<String> nonExistentSiteCodes = new ArrayList<>();
		    
		    for (RentExcelInfo rentInfo : RentInfoExcel) {
		        String excelSiteCode = rentInfo.getExcelSiteCode().replace("\"", ""); 
		        
		        // Check if the site code exists in the database
		        if (!siteMasterService.checkSiteCodeExists(excelSiteCode)) {
		            nonExistentSiteCodes.add(excelSiteCode);
		        }
		    }
		    
		    return nonExistentSiteCodes;
	
	}
	
	// this service method is for checking if the amount paid to the sites is equal to the generated rent amount 
	//( if not then we'll see if there are any remarks present)
	public List<String> getMismatchedSites(List<RentExcelInfo> rentInfoList) {
	    // List for storing site codes with rent mismatches
	    List<String> mismatchedSiteCodes = new ArrayList<>();

	    // Validate input
	    if (rentInfoList == null || rentInfoList.isEmpty()) {
	        throw new IllegalArgumentException("rentInfoList cannot be null or empty");
	    }
	    System.out.println(rentInfoList);

	    // Create DateTimeFormatter for consistent date formatting
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");

	    // Iterate through the rentInfoList to compare the rent amounts
	    for (RentExcelInfo rentInfo : rentInfoList) {
	        String siteCode = rentInfo.getExcelSiteCode();
	        LocalDate rentDate = rentInfo.convertToLocalDate();
	        String formattedRentDate = rentDate.format(formatter);
	        double paidRentAmount = rentInfo.getAmountPaid();

	        // Get the agreementId for the current siteCode
	        Integer agreementId;
	        try {
	            agreementId = rentAgreementMasterService.getAgreementIdBySiteCode(siteCode);
	            if (agreementId == null) {
	                logAndAddMismatch(mismatchedSiteCodes, rentInfo, 
	                    String.format("%s | Date: %s | Error: No agreement found", siteCode, formattedRentDate));
	                continue;
	            }
	        } catch (Exception e) {
	            logAndAddMismatch(mismatchedSiteCodes, rentInfo,
	                String.format("%s | Date: %s | Error: Failed to fetch agreement", siteCode, formattedRentDate));
	            continue;
	        }

	        // Get the expected rent map using the RentAmountCalculator
	        Map<String, Integer> rentMap;
	        try {
	            rentMap = RentAmountCalculator.calculateRentListwithDate(agreementId);
	            System.out.println("\nRent map for site " + siteCode + ":");
	            System.out.println("Agreement ID: " + agreementId);
	            System.out.println("Looking for date: " + formattedRentDate);
	            
	            // Print rent map entries in a more readable format
	            if (rentMap.isEmpty()) {
	                System.out.println("WARNING: Rent map is empty!");
	            } else {
	                System.out.println("Available dates and amounts in rent map:");
	                rentMap.entrySet().stream()
	                    .sorted(Map.Entry.comparingByKey())
	                    .forEach(entry -> System.out.printf("  %s: %d%n", entry.getKey(), entry.getValue()));
	            }
	        } catch (Exception e) {
	            logAndAddMismatch(mismatchedSiteCodes, rentInfo,
	                String.format("%s | Date: %s | Error: Failed to calculate rent", siteCode, formattedRentDate));
	            continue;
	        }

	        // Check if rentMap contains the formatted date
	        Integer expectedRentAmount = rentMap.get(formattedRentDate);
	        if (expectedRentAmount == null) {
	            System.out.println("\nDEBUG: No rent amount found for date " + formattedRentDate);
	            System.out.println("Site Code: " + siteCode);
	            System.out.println("Agreement ID: " + agreementId);
	            System.out.println("Paid Amount: " + paidRentAmount);
	            
	            // Find closest dates in rentMap for context
	            String[] allDates = rentMap.keySet().toArray(new String[0]);
	            Arrays.sort(allDates);
	            
	            String closestBefore = null;
	            String closestAfter = null;
	            
	            for (String date : allDates) {
	                if (date.compareTo(formattedRentDate) < 0) {
	                    closestBefore = date;
	                } else if (date.compareTo(formattedRentDate) > 0) {
	                    closestAfter = date;
	                    break;
	                }
	            }
	                
	            if (closestBefore != null) {
	                System.out.println("Closest previous date in map: " + closestBefore + 
	                    " (Amount: " + rentMap.get(closestBefore) + ")");
	            }
	            if (closestAfter != null) {
	                System.out.println("Closest next date in map: " + closestAfter + 
	                    " (Amount: " + rentMap.get(closestAfter) + ")");
	            }
	            
	            logAndAddMismatch(mismatchedSiteCodes, rentInfo,
	                String.format("%s | Date: %s | Error: No rent amount found for date | Paid Amount: %.2f", 
	                    siteCode, formattedRentDate, paidRentAmount));
	            continue;
	        }

	        System.out.println("Expected rent for " + siteCode + ": " + expectedRentAmount);

	        // Check if the expected rent matches the paid rent
	        if (expectedRentAmount != paidRentAmount) {
	            logAndAddMismatch(mismatchedSiteCodes, rentInfo,
	                String.format("%s | Date: %s | Expected: %d | Paid: %.2f", 
	                    siteCode, formattedRentDate, expectedRentAmount, paidRentAmount));
	        }
	    }

	    return mismatchedSiteCodes;
	}

	// Helper method to add mismatches only if there are no remarks
	public void logAndAddMismatch(List<String> mismatchedSiteCodes, RentExcelInfo rentInfo, String message) {
	    if (rentInfo.getRemarks() == null || rentInfo.getRemarks().isEmpty()) {
	        mismatchedSiteCodes.add(message);
	    }
	
	}

	
}
