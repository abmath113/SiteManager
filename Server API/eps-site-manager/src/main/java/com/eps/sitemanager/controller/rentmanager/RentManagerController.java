package com.eps.sitemanager.controller.rentmanager;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.sitemanager.dto.rentmanager.GeneratedRentDTO;
import com.eps.sitemanager.services.rentmanager.RentManagerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/rentmanager")
@Tag(name = "Rent Calculation function")

public class RentManagerController {
	
    private RentManagerService rentManagerService;
    
    @Autowired
    public RentManagerController( RentManagerService rentManagerService) {
    	this.rentManagerService = rentManagerService;
    }

    // get all rent in future past and present for any given agreement
    @GetMapping("/getRentListByAgreementId/{agreementId}")
    @Operation(tags = "Get Rent List by Agreement ID", summary = "Retrieves the calculated rent list for the given Agreement ID.")
    
    public ResponseEntity<List<Integer>> getRentListByAgreementId(@PathVariable(value = "agreementId") int agreementId) {
        try {
            List<Integer> rentList = rentManagerService.getRentListByAgreementId(agreementId);
            return ResponseEntity.ok(rentList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // get all rent to pay for any given month **DATE : YYYY-MM-DD**
    @GetMapping("/getAllRentByCurrDate/{currDate}")
    @Operation(tags = "Get Rent of each site by current date")
    public ResponseEntity<List<GeneratedRentDTO>> getAllRentByCurrDate(
        @PathVariable(value = "currDate") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currDate) {
        
        try {
            List<GeneratedRentDTO> generatedRentList = rentManagerService.getAllRentbyCurrDate(currDate);
            return ResponseEntity.ok(generatedRentList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}