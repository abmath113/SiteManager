package com.eps.sitemanager.controller.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.sitemanager.configurations.error.DataNotFoundException;
import com.eps.sitemanager.configurations.error.FailedToUpdateException;
import com.eps.sitemanager.configurations.error.ParameterConstraintException;
import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.LandlordMasterDTO;
import com.eps.sitemanager.dto.master.LandlordMasterSearchDTO;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.repository.master.LandlordIdNameAndAccountNo;
import com.eps.sitemanager.request.validator.master.LandlordMasterControllerRequestValidator;
import com.eps.sitemanager.services.master.LandlordMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/landlordmaster")
@Tag(name = "Landlord Master")
class LandlordMasterController {
	private LandlordMasterService landlordMasterService;
	private LandlordMasterControllerRequestValidator reqValidator;
	
	
	@Autowired
	public LandlordMasterController(LandlordMasterService landlordMasterService,LandlordMasterControllerRequestValidator reqValidator) {
		super();
		this.landlordMasterService = landlordMasterService;
		this.reqValidator = reqValidator;
	}
	
	
	@PostMapping("/save")
	@Operation(tags = "Save Landlord Master",
			   description = "Saves the Single Landlord Master Details.")
	protected ResponseEntity<LandlordMaster> saveLandlordMaster(@RequestBody LandlordMasterDTO landlordMasterDTO){
		
		
		//perform validation
		RequestValidaton reqValidation = reqValidator.validateSaveUpdateRequest(landlordMasterDTO);
		if(!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
			
		}
		
		
		// save the landlord master details
		LandlordMaster landlordMaster = landlordMasterService.saveLandlordMaster(landlordMasterDTO);
		if(landlordMaster == null || landlordMaster.getLandlordId() == 0) {
			throw new DataNotFoundException("Failed to save Landlord Master data ,Landlord name : "+ landlordMasterDTO.getName());
		}
		else {
			return ResponseEntity.ok(landlordMaster);
		}
		
	}
	
	@PostMapping("/search")
 	@Operation(tags = "Search Landlord master",
 			   description = "Searches landlord master from given fields." )
  	protected ResponseEntity<List<LandlordMaster>> searchLandlordMaster(@RequestBody LandlordMasterSearchDTO landlordmastersearchdto){
 		// Perform Validation
 				RequestValidaton reqValidation = reqValidator.validateSearchRequest(landlordmastersearchdto);
 				if(!reqValidation.isState()) {
 					throw new ParameterConstraintException(reqValidation.getErrorMessage());
 				}
				
 				// return the landlord Master 
 				List<LandlordMaster> landlordMasterList = landlordMasterService.searchLandlordMasters(landlordmastersearchdto);
 				return ResponseEntity.ok(landlordMasterList);
				
	}
	
	
	@GetMapping("/searchall")
	@Operation(tags = "Show Landlord master", description = "Show whole Landlord Master.")
	protected ResponseEntity<List<LandlordMaster>> showALLLanlordMaster() {
		List<LandlordMaster> landlordMasterList = new ArrayList<>();
		Iterable<LandlordMaster> landlordMasterItr = landlordMasterService.getAllLandlordMaster();
		landlordMasterItr.forEach(landlordMasterList::add);
		return ResponseEntity.ok(landlordMasterList);

	}
	
	
	@GetMapping("/getbylandlordid/{id}")
	@Operation(tags = "Get Landlord details by landlordid.")
 	protected ResponseEntity<LandlordMaster> getByLandlordId(@PathVariable(value="id") int landlordId){
			// Get the Landlord Master Details
			Optional<LandlordMaster> landlordMasterDetailOpt = landlordMasterService.getLandlordMasterById(landlordId);
			if(landlordMasterDetailOpt.isPresent()) {
				return ResponseEntity.ok(landlordMasterDetailOpt.get());
			}
			return ResponseEntity.notFound().build();	
    }
	
	@PostMapping("/update")
	@Operation(tags = "Update Landlord Master", description = "Updates the Single Landlord Master Details.")
	protected ResponseEntity<LandlordMaster> updateLandlordMaster(@RequestBody LandlordMasterDTO landlordMasterDTO) {
		// Perform Validation
		RequestValidaton reqValidation = reqValidator.validateSaveUpdateRequest(landlordMasterDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}

		Optional<LandlordMaster> landlordMasterOpt = landlordMasterService.getLandlordMasterById(landlordMasterDTO.getLandlordId());
		if (landlordMasterOpt.isEmpty() || landlordMasterOpt.get().getLandlordId() != landlordMasterDTO.getLandlordId()) {
			throw new DataNotFoundException("Selected Landlord does not exists to Update.");
		}
		
		Optional<LandlordMaster> landlordMasterExistingOpt = landlordMasterService.getLandlordMasterbyAccountNoAndIfsc(landlordMasterDTO.getAccountNo(), landlordMasterDTO.getIfscCode());
		if (landlordMasterExistingOpt.isPresent() 
				&& landlordMasterExistingOpt.get().getAccountNo().equals(landlordMasterDTO.getAccountNo()) 
				&& landlordMasterExistingOpt.get().getIfscCode().equals(landlordMasterDTO.getIfscCode()) 
				&& landlordMasterExistingOpt.get().getLandlordId() != landlordMasterDTO.getLandlordId()) {
			throw new FailedToUpdateException("Landlord " + landlordMasterDTO.getAccountNo() + "' already exists!");
		}
		
		// Update the Site Master Details
		Optional<LandlordMaster> updatedLandlordMaster = landlordMasterService.updateLandlordMaster(landlordMasterDTO, landlordMasterOpt.get());
		if (updatedLandlordMaster.isEmpty() || updatedLandlordMaster.get().getLandlordId() != landlordMasterDTO.getLandlordId()) {
			throw new FailedToUpdateException(
					"Failed to Update Landlord Master : " + landlordMasterDTO.getName());
		} else {
			return ResponseEntity.ok(updatedLandlordMaster.get());
		}
	}
	
	
	
	@GetMapping("/getalllandlordsbylandlordidandlandlordnameandaccountno")
	@Operation(tags = "Gets all LandlordId, Name and Account no", description = "Gets all records Landlord master but only landlordId , landlord name and Account No")
	protected ResponseEntity<List<LandlordIdNameAndAccountNo>> getAllLandlordIdNameAndAccountNo() {
		return ResponseEntity.ok(landlordMasterService.getAllLandlordIdNameAndAccountNo());

	}
	
	
	
	
	
	
	
	
	
	
	
}
