package com.eps.sitemanager.controller.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.sitemanager.configurations.error.DataNotFoundException;
import com.eps.sitemanager.configurations.error.FailedToSaveException;
import com.eps.sitemanager.configurations.error.FailedToUpdateException;
import com.eps.sitemanager.configurations.error.ParameterConstraintException;
import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.SiteMasterDTO;
import com.eps.sitemanager.dto.master.SiteMasterSearchDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.SiteCodeAndSiteId;
import com.eps.sitemanager.request.validator.master.SiteMasterControllerRequestValidator;
import com.eps.sitemanager.services.master.BankMasterService;
import com.eps.sitemanager.services.master.ChannelManagerService;
import com.eps.sitemanager.services.master.SiteMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/sitemaster")
@Tag(name="Site Master")
public class SiteMasterController {
	
	private SiteMasterService siteMasterService;
	private ChannelManagerService channelManagerService;
	private BankMasterService bankMasterService;
	private SiteMasterControllerRequestValidator reqValidator;
	
	@Autowired
	public SiteMasterController(SiteMasterService siteMasterService,ChannelManagerService channelManagerService,
			BankMasterService bankMasterService,
			SiteMasterControllerRequestValidator reqValidator
			) {
		super();
		this.siteMasterService = siteMasterService;
		this.channelManagerService = channelManagerService;
		this.bankMasterService = bankMasterService;
		this.reqValidator = reqValidator;
	}

	
	@PostMapping("/save")
	@Operation(tags = "Save Site Master",summary = "Saves the Single Site Master Details.")
	protected ResponseEntity<SiteMaster> saveSiteMaster(@RequestBody SiteMasterDTO siteMasterDTO) {
		//Perform Validation
		RequestValidaton reqValidation = reqValidator.validateSaveUpdateRequest(siteMasterDTO);
		if(!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}
		
		if(siteMasterService.checkSiteAlreadyExists(siteMasterDTO.getSitecode())) {
			throw new FailedToSaveException("Site '" + siteMasterDTO.getSitecode() + "' already exists!");
		}
		
		// check channel manager exist?
		Optional<ChannelManagerMaster> channelManagerIdExistOptional = channelManagerService.getChannelManagerMasterById(siteMasterDTO.getChannelManagerId());
			
		if (channelManagerIdExistOptional.isEmpty()) {
			throw new DataNotFoundException("Channel Manager not found or Data is Empty");
		}
		
		Optional<BankMaster> bankIdExistOptional = bankMasterService.getBankMasterById(siteMasterDTO.getBankId());
		if(bankIdExistOptional.isEmpty()) {
			throw new DataNotFoundException("Bank not found or Data is Empty");
		}
		
		//Save the Site Master Details
		SiteMaster siteMaster = siteMasterService.saveSiteMaster(siteMasterDTO,channelManagerIdExistOptional.get(),bankIdExistOptional.get());
		 
		if (siteMaster == null || siteMaster.getSiteId() == 0) {
			throw new DataNotFoundException("Failed to Save Site Master Data, Site Code : " + siteMasterDTO.getSitecode());
		} else {
			return ResponseEntity.ok(siteMaster);
		}
	}

	
	@PostMapping("/update")
	@Operation(tags = "Update Site Master", summary = "Updates the Single Site Master Details.")
	protected ResponseEntity<SiteMaster> updateSiteMaster(@RequestBody SiteMasterDTO siteMasterDTO) {
		// Perform Validation
		RequestValidaton reqValidation = reqValidator.validateSaveUpdateRequest(siteMasterDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}

		Optional<SiteMaster> siteMasterOpt = siteMasterService.getSiteMasterById(siteMasterDTO.getSiteid());
		if (siteMasterOpt.isEmpty() || siteMasterOpt.get().getSiteId() != siteMasterDTO.getSiteid()) {
			throw new DataNotFoundException("Selected Site does not exists to Update.");
		}
		
		Optional<SiteMaster> siteMasterExistingOpt = siteMasterService.getSiteMasterBySiteCode(siteMasterDTO.getSitecode());
		if (siteMasterExistingOpt.isPresent() && siteMasterExistingOpt.get().getSiteCode().equals(siteMasterDTO.getSitecode()) 
				&& siteMasterOpt.get().getSiteId() != siteMasterExistingOpt.get().getSiteId()) {
			throw new FailedToUpdateException("Site '" + siteMasterDTO.getSitecode() + "' already exists!");
		}
		
		// check channel manager exist?
		
				Optional<ChannelManagerMaster> channelManagerIdExistOptional = channelManagerService.getChannelManagerMasterById(siteMasterDTO.getChannelManagerId());	
				if (channelManagerIdExistOptional.isEmpty()) {
					throw new DataNotFoundException("Channel Manager not found or Data is Empty");
				}
				Optional<BankMaster> bankIdExistOptional = bankMasterService.getBankMasterById(siteMasterDTO.getBankId());	
				if (bankIdExistOptional.isEmpty()) {
					throw new DataNotFoundException("Bank not found or Data is Empty");
				}
				
		
		// Update the Site Master Details
		Optional<SiteMaster> updatedSiteMaster = siteMasterService.updateSiteMaster(siteMasterDTO, siteMasterOpt.get(),channelManagerIdExistOptional.get(), bankIdExistOptional.get());
		if (updatedSiteMaster.isEmpty() || updatedSiteMaster.get().getSiteId() != siteMasterDTO.getSiteid()) {
			throw new FailedToUpdateException(
					"Failed to Update Site Master Data, Site Code : " + siteMasterDTO.getSitecode());
		} else {
			return ResponseEntity.ok(updatedSiteMaster.get());
		}
	}
	
 	@PostMapping("/search")
 	@Operation(tags = "Search Site master",
 			      summary = "Searches site master from given fields." )
  	protected ResponseEntity<Optional<SiteMaster>> searchSiteMaster(@RequestBody SiteMasterSearchDTO sitermastersearchDTO){
 		// Perform Validation
 				RequestValidaton reqValidation = reqValidator.validateSearchRequest(sitermastersearchDTO);
 				if(!reqValidation.isState()) {
 					throw new ParameterConstraintException(reqValidation.getErrorMessage());
 				}
				
 				// return the search Site Master Details
 				Optional<SiteMaster> siteMasterList = siteMasterService.searchSiteMasters(sitermastersearchDTO);
 				return ResponseEntity.ok(siteMasterList);
				
	}
 		
	@GetMapping("/searchall")
	@Operation(tags = "Show Site master", summary = "Show whole sitemaster.")
	protected ResponseEntity<List<SiteMaster>> showALLSiteMaster() {
		List<SiteMaster> siteMasterList = new ArrayList<>();
		Iterable<SiteMaster> siteMasterItr = siteMasterService.getAllSiteMaster();
		siteMasterItr.forEach(siteMasterList::add);
		return ResponseEntity.ok(siteMasterList);

	}
 		
 	@GetMapping("/getbysiteid/{id}")
 	@Operation(tags = "Get site details by siteid.")
 	protected ResponseEntity<SiteMaster> getBySiteId(@PathVariable(value="id") int siteId){
			// Get the Site Master Details
			Optional<SiteMaster> siteMasterDetailOpt = siteMasterService.getSiteMasterById(siteId);
			if(siteMasterDetailOpt.isPresent()) {
				return ResponseEntity.ok(siteMasterDetailOpt.get());
			}
			return ResponseEntity.notFound().build();	

    }
 		
	
	@GetMapping("/getallsitesbysiteidandsitecode")
	@Operation(tags = "Gets all SiteId and SiteCode", summary = "Gets all records Site master but only SiteId and SiteCode ")
	protected ResponseEntity<List<SiteCodeAndSiteId>> getAllSitesOnlySiteIdAndSiteCode() {
		return ResponseEntity.ok(siteMasterService.getAllSitesOnlySiteIdAndSiteCode());
	}
 	
	
 	
 	
 	
}
	
