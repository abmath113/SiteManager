package com.eps.sitemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.sitemanager.dto.SiteHistoryDTO;
import com.eps.sitemanager.services.SiteHistoryService;

import io.swagger.v3.oas.annotations.Operation;


@RestController

@RequestMapping("/api/site-rent-history")
public class SiteRentHistoryController {
	
	private final SiteHistoryService siteHistoryService;
	
	@Autowired
	public SiteRentHistoryController(SiteHistoryService siteHistoryService) {
        this.siteHistoryService = siteHistoryService;
       
    }
	
	
		
	@GetMapping("/getbysitecode/{sitecode}")
    @Operation(
        tags = "Get site rent history for any given sitecode", 
        summary = "Retrieves comprehensive site rent history based on site code"
    )
    public ResponseEntity<SiteHistoryDTO> getSiteHistoryBySiteCode(
            @PathVariable(value = "sitecode") String siteCode) {
        
        // Validate input site code
        if (siteCode == null || siteCode.trim().isEmpty()) {
            // Return a bad request response if site code is invalid
            return ResponseEntity.badRequest().build();
        }

        try {
            // Attempt to retrieve site history
            SiteHistoryDTO siteHistory = siteHistoryService.getSiteHistoryBySiteCode(siteCode);

            // Check if site history is empty or null
            if (siteHistory == null) {
                // Return not found response if no history exists
                return ResponseEntity.notFound().build();
            }

            // Return successful response with site history
            return ResponseEntity.ok(siteHistory);

        } catch (Exception e) {
            // Log the exception (use appropriate logging mechanism)
            // This helps in tracking and debugging issues
            // logger.error("Error retrieving site history for site code: " + siteCode, e);

            // Return internal server error if unexpected exception occurs
            return ResponseEntity.internalServerError().build();
        }
    }
}
