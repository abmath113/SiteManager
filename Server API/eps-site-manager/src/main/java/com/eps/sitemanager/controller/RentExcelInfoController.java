package com.eps.sitemanager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.model.RentExcelInfo;
import com.eps.sitemanager.services.RentExcelInfoServiceImpl;
import com.eps.sitemanager.services.rentmanager.RentManagerService;

//@RestController
//@RequestMapping("/api")
public class RentExcelInfoController {
	
   
    private RentExcelInfoServiceImpl rentExcelInfoService;
    private RentManagerService rentManagerService;
    
    @Autowired
    public RentExcelInfoController(RentExcelInfoServiceImpl rentExcelInfoService, RentManagerService rentManagerService) {
        this.rentExcelInfoService = rentExcelInfoService;
        this.rentManagerService = rentManagerService;
    }
   
    // endpoint to process the Excel file  -- works fine
    @PostMapping("/process-rent-excel")
    public ResponseEntity<List<RentExcelInfo>> parseExcelInfo(@RequestParam("file") MultipartFile file) {
        try {
            List<RentExcelInfo> rentInfoList = rentExcelInfoService.parseExcel(file);
            return ResponseEntity.ok(rentInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //  endpoint to check for non-existent site codes in the database --works fine
    @PostMapping("/check-site-codes")
    public ResponseEntity<?> checkNonExistentSiteCodes(@RequestParam("file") MultipartFile file) {
        try {
            // Check if file is empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty. Please upload a valid Excel file.");
            }

            // Call the parseExcel method from the service
            List<RentExcelInfo> rentInfoList = rentExcelInfoService.parseExcel(file);

            // Filter out RentExcelInfo objects with null excelSiteCode
            List<RentExcelInfo> validRentInfoList = rentInfoList.stream()
                .filter(info -> info.getExcelSiteCode() != null)
                .collect(Collectors.toList());

            // Call the service method to check for non-existent site codes
            List<String> nonExistentSiteCodes = rentExcelInfoService.getNonExistentExcelSiteCodes(validRentInfoList);

            // Check if all site codes exist
            if (nonExistentSiteCodes == null || nonExistentSiteCodes.isEmpty()) {
                return ResponseEntity.ok("All sites exist");
            } else {
                // Return the non-existent site codes as the response
                return ResponseEntity.ok("Non-existent sites: " + String.join(", ", nonExistentSiteCodes));
            }
        } catch (Exception e) {
            // Return the actual exception message for better debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file: " + e.getMessage());
        }
    }
  
    
    //  endpoint to check for rent amount mismatches
    @PostMapping("/check-mismatched-rent-reasons")
    public ResponseEntity<List<String>> checkMismathcedRentReasons(@RequestParam("file") MultipartFile file) {
        try {
        	
            // Parse the Excel file to get RentExcelInfo list
            List<RentExcelInfo> rentInfoList = rentExcelInfoService.parseExcel(file);
            
            // Get the list of site codes with mismatched rents to amount paid
            List<String> rentMistmatchedSites = rentExcelInfoService.getMismatchedSites(rentInfoList);
            
            if (rentMistmatchedSites.isEmpty()) {
                // Return an OK status with a message if all amounts match
                return ResponseEntity.ok(List.of("All amounts match successfully."));
            } else {
                // Return the list of mismatched site codes if any mismatch occurs
                return ResponseEntity.ok(rentMistmatchedSites);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}




