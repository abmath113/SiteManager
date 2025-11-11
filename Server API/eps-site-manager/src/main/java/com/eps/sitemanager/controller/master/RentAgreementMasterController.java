package com.eps.sitemanager.controller.master;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.eps.sitemanager.dto.master.RentAgreementMasterDTO;
import com.eps.sitemanager.dto.master.RentAgreementMasterSearchDTO;
import com.eps.sitemanager.dto.master.RentAgreementTerminateDTO;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.request.validator.master.RentAgreementMasterControllerRequestValidator;
import com.eps.sitemanager.services.master.LandlordMasterService;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.services.master.SiteMasterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/rentagreementmaster")
@Tag(name = "Rent Agreement Master")

public class RentAgreementMasterController {

	@Value("${file.upload-dir}")
	private String uploadDir; // this sets the path where agreement scans would be stored

//	@Value("${file.upload.bulk.template.rentagreement}")
//	private String bulkUploadTemplate;

	private final RentAgreementMasterService rentAgreementMasterService;
	private final SiteMasterService siteMasterService;
	private final LandlordMasterService landlordMasterService;
	private RentAgreementMasterControllerRequestValidator rentAgreementMasterControllerRequestValidator;

	@Autowired
	public RentAgreementMasterController(RentAgreementMasterService rentAgreementMasterService,
			SiteMasterService siteMasterService, LandlordMasterService landlordMasterService,
			RentAgreementMasterControllerRequestValidator rentAgreementMasterControllerRequestValidator) {
		this.rentAgreementMasterService = rentAgreementMasterService;
		this.siteMasterService = siteMasterService;
		this.landlordMasterService = landlordMasterService;
		this.rentAgreementMasterControllerRequestValidator = rentAgreementMasterControllerRequestValidator;
	}

	@PostMapping("/save")
	@Operation(tags = "Save Rent Agreement Master", summary = "Saves the Single Rent Agreement Master Details.")

	protected ResponseEntity<RentAgreementMaster> saveRentAgreementMaster(
			@RequestBody RentAgreementMasterDTO rentAgreementMasterDTO) {

		Optional<SiteMaster> siteIdExistOptional = siteMasterService
				.getSiteMasterById(rentAgreementMasterDTO.getSiteId());

		if (siteIdExistOptional.isEmpty()) {
			throw new DataNotFoundException("Site not found or Data is Empty");
		}

		Optional<LandlordMaster> landlordIdExistOptional = landlordMasterService
				.getLandlordMasterById(rentAgreementMasterDTO.getLandlordId());
		if (landlordIdExistOptional.isEmpty()) {
			throw new DataNotFoundException("Landlord not found");
		}

		if (rentAgreementMasterService.isRentAgreementDuplicate(siteIdExistOptional.get(),
				landlordIdExistOptional.get(), rentAgreementMasterDTO.getAgreementDate())) {
			throw new DataNotFoundException(
					"Rent Agreement for this site or landlord or Agreement for this date already exists");
		}
		// Perform Validation
		RequestValidaton reqValidation = rentAgreementMasterControllerRequestValidator
				.validateSaveUpdateRequest(rentAgreementMasterDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}
		RentAgreementMaster savedRentAgreementMaster = rentAgreementMasterService.saveRentAgreementMaster(
				rentAgreementMasterDTO, siteIdExistOptional.get(), landlordIdExistOptional.get());

		if (savedRentAgreementMaster == null || savedRentAgreementMaster.getAgreementId() == 0) {
			throw new DataNotFoundException("Failed to save Rent Agreement Master data, Site ID");
		} else {
			return ResponseEntity.ok(savedRentAgreementMaster);
		}
	}

	@PostMapping("/search")
	@Operation(tags = "Search Rent Agreement Master", summary = "Search agreements using sitecodes, landlord names and status")

	protected ResponseEntity<List<RentAgreementMaster>> searchRentAgreementMaster(
			@RequestBody RentAgreementMasterSearchDTO rentagreementmastersearchDTO) {
		// Perform Validation
		RequestValidaton reqValidation = rentAgreementMasterControllerRequestValidator
				.validateSearchRequest(rentagreementmastersearchDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}
//				
		// return the search Site Master Details
		List<RentAgreementMaster> rentAgreementMasterList = rentAgreementMasterService
				.searchRentAgreementMasters(rentagreementmastersearchDTO);
		return ResponseEntity.ok(rentAgreementMasterList);

	}
	
	
	@PostMapping("/update")
	@Operation(tags = "Update Rent Agreement Master", summary = "Updates the Rent Agreement Master details.")

	protected ResponseEntity<RentAgreementMaster> updateRentAgreementMaster(
			@RequestBody RentAgreementMasterDTO rentAgreementMasterDTO) {

		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterById(rentAgreementMasterDTO.agreementId);

		if (rentAgreementMasterOpt.isEmpty()) {
			throw new DataNotFoundException("Rent Agreement does not exist to update, Agreement ID ");
		}

		Optional<RentAgreementMaster> updatedRentAgreementMaster = rentAgreementMasterService
				.updateRentAgreementOptional(rentAgreementMasterDTO, rentAgreementMasterOpt.get());

		if (updatedRentAgreementMaster.isEmpty()) {
			throw new FailedToUpdateException("Failed to update Rent Agreement Master for Agreement ID");
		} else {
			return ResponseEntity.ok(updatedRentAgreementMaster.get());
		}
	}

	@PostMapping("/terminate")
	@Operation(tags = "Terminate Rent Agreement", summary = "Terminates the Rent Agreement.")

	protected ResponseEntity<RentAgreementMaster> terminateRentAgreementMaster(
			@RequestBody RentAgreementTerminateDTO rentAgreementTerminateDTO) {

		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterById(rentAgreementTerminateDTO.agreementId);

		if (rentAgreementMasterOpt.isEmpty()) {
			throw new DataNotFoundException(
					"Rent Agreement does not exist to update, Agreement ID :" + rentAgreementTerminateDTO.agreementId);
		}

		Optional<RentAgreementMaster> terminatedRentAgreementMaster = rentAgreementMasterService
				.terminateRentAgreementOptional(rentAgreementTerminateDTO, rentAgreementMasterOpt.get());

		if (terminatedRentAgreementMaster.isEmpty()) {
			throw new FailedToUpdateException("Failed to terminate Rent Agreement Master for Agreement ID"
					+ rentAgreementTerminateDTO.agreementId);
		} else {
			return ResponseEntity.ok(terminatedRentAgreementMaster.get());
		}
	}

	@GetMapping("/getbyid/{id}")
	@Operation(tags = "Get Rent Agreement Master by ID", summary = "Retrieves the Rent Agreement Master by Agreement ID.")

	protected ResponseEntity<RentAgreementMaster> getRentAgreementMasterById(
			@PathVariable(value = "id") int agreementId) {
		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterById(agreementId);

		return rentAgreementMasterOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/getbysiteid/{siteId}")
	@Operation(tags = "Get Rent Agreement Master by Site ID", summary = "Retrieves the Rent Agreement Master by Site ID.")
	protected ResponseEntity<RentAgreementMaster> getRentAgreementMasterBySiteId(
			@PathVariable(value = "siteId") int siteId) {
		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterBySiteId(siteId);

		return rentAgreementMasterOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/getbylandlordid/{landlordId}")
	@Operation(tags = "Get Rent Agreement Master by Landlord ID", summary = "Retrieves the Rent Agreement Master by Landlord ID.")
	protected ResponseEntity<RentAgreementMaster> getRentAgreementMasterByLandlordId(
			@PathVariable(value = "landlordId") int landlordId) {
		Optional<RentAgreementMaster> rentAgreementMasterOpt = rentAgreementMasterService
				.getRentAgreementMasterByLandlordId(landlordId);

		return rentAgreementMasterOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/searchall")
	@Operation(tags = "Get All Rent Agreement Masters", summary = "Retrieves all Rent Agreement Masters.")

	protected ResponseEntity<List<RentAgreementMaster>> getAllRentAgreementMaster() {

		List<RentAgreementMaster> rentAgreementMasterList = new ArrayList<>();
		Iterable<RentAgreementMaster> rentAgreementMasters = rentAgreementMasterService.getAllRentAgreementMaster();
		rentAgreementMasters.forEach(e -> {
			String siteCode = e.getSiteId().getSiteCode();
			String landlordName = e.getLandlordId().getName();

			String orgFileExtString = ".pdf";
			String landLordNameSeq = landlordName.replaceAll("\s+", "_");
			String agreementScanFileName = siteCode + "_" + landLordNameSeq + orgFileExtString;

			File agreementScanFile = new File(uploadDir, agreementScanFileName);

			if (agreementScanFile.exists()) {
				e.setAgreementScanExist(true);
			}
			rentAgreementMasterList.add(e);
		});

		return ResponseEntity.ok(rentAgreementMasterList);
	}

	// TODO Auto
//	@GetMapping("/download/BulkUploadTemplate")
//    @ApiOperation(value = "Download bulk upload excel template")
//	protected ResponseEntity<UrlResource> downloadBulkUploadTemplate() {
//		
//		 Path filePath = Paths.get(bulkUploadTemplate).normalize();
//		 
//		    UrlResource resource = null;
//			try {
//				resource = new UrlResource(filePath.toUri());
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		    if (resource.exists() && resource.isReadable()) {
//		        return ResponseEntity.ok()
//		                .header(HttpHeaders.CONTENT_DISPOSITION, 
//		                		"attachment; filename=\"" + resource.getFilename() + "\"")
//		                .body(resource);
//		    } else {
//		        return ResponseEntity.notFound().build();
//		    }
//	}

//  @DeleteMapping("/delete/{id}")
//  @ApiOperation(value = "Delete Rent Agreement Master", notes = "Deletes the Rent Agreement Master by Agreement ID.")
//  protected ResponseEntity<Void> deleteRentAgreementMaster(@PathVariable(value = "id") int agreementId) {
//      rentAgreementMasterService.deleteRentAgreementMaster(agreementId);
//      return ResponseEntity.noContent().build();
//  }

//  @PostMapping("/update-status")
//  @ApiOperation(value = "Update Rent Agreement Status", notes = "Updates the Rent Agreement Master Status by Agreement ID.")
//  protected ResponseEntity<Void> updateRentAgreementMasterStatus(
//          @RequestParam boolean status, 
//          @RequestParam int agreementId) {
//      
//      int result = rentAgreementMasterService.updateRentAgreementMasterStatus(status, agreementId);
//      
//      if (result == 1) {
//          return ResponseEntity.ok().build();
//      } else {
//          throw new FailedToUpdateException("Failed to update status for Agreement ID: " + agreementId);
//      }
//  }

}
