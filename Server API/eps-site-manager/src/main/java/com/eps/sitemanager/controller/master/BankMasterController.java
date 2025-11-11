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
import com.eps.sitemanager.dto.master.BankMasterDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.repository.master.BankIdAndBankCode;
import com.eps.sitemanager.request.validator.master.BankMasterControllerRequestValidator;
import com.eps.sitemanager.services.master.BankMasterService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/bankmaster")
public class BankMasterController {
	
	
	private final BankMasterService bankMasterService;
	private final BankMasterControllerRequestValidator reqValidator;
	
	
	@Autowired
	public BankMasterController(BankMasterService bankMasterService,BankMasterControllerRequestValidator reqValidator) {

		this.bankMasterService = bankMasterService;
		this.reqValidator = reqValidator;
	}
	
	@GetMapping("/getallbankidandbankcode")
	@Operation(tags = "Gets all Bank ID and Code", description = "Fetches all records for bank master")
	protected ResponseEntity<List<BankIdAndBankCode>> getAllBankIdAndBankCode(){
		
		return ResponseEntity.ok(bankMasterService.getAllBankIdAndBankCode());
		
	}
	
	
	@PostMapping("/save")
	@Operation(tags = "Add new Bank")
	protected ResponseEntity<BankMaster> saveBankMaster(@RequestBody BankMasterDTO bankMasterDTO){
		
		// validation 
		RequestValidaton requestValidaton = reqValidator.validateSaveUpdateRequest(bankMasterDTO);
		   if (!requestValidaton.isState()) {
	            throw new ParameterConstraintException(requestValidaton.getErrorMessage());
	        }
		   
		 BankMaster bankMaster = bankMasterService.saveBankMaster(bankMasterDTO);
		 if (bankMaster == null || bankMaster.getBankId() == 0) {
	            throw new DataNotFoundException("Failed to save Channel Manager: " + bankMasterDTO.getBankCode());
	        } else {
	            return ResponseEntity.ok(bankMaster);
	        }
	}
	
	
	
    @GetMapping("/searchall")
    @Operation(tags = "Show Bank Master", description = "Show all Bank Master records.")
    protected ResponseEntity<List<BankMaster>> showAllBankMaster() {
        List<BankMaster> bankMasterList = new ArrayList<>();
        Iterable<BankMaster> bankMasterItr = bankMasterService.getAllBankMaster();
        bankMasterItr.forEach(bankMasterList::add);
        return ResponseEntity.ok(bankMasterList);
    }
    
    
    @GetMapping("/getbyid/{id}")
    @Operation(tags = "Get Bank Master details by ID.")
    protected ResponseEntity<BankMaster> getByBankId(@PathVariable(value = "id") int bankId) {
        // Get the Bank Master Details
        Optional<BankMaster> bankMasterOpt = bankMasterService.getBankMasterById(bankId);
        if (bankMasterOpt.isPresent()) {
            return ResponseEntity.ok(bankMasterOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/getbybankcode/{bankCode}")
    @Operation(tags = "Get Bank Master details by Bank Code.")
    protected ResponseEntity<BankMaster> getByBankCode(@PathVariable(value = "bankCode") String bankCode) {
        // Get the Bank Master Details by Bank Code
        Optional<BankMaster> bankMasterOpt = bankMasterService.getBankMasterByBankCode(bankCode);
        if (bankMasterOpt.isPresent()) {
            return ResponseEntity.ok(bankMasterOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/update")
    @Operation(tags = "Update Bank Master", description = "Updates a single Bank Master record.")
    protected ResponseEntity<BankMaster> updateBankMaster(@RequestBody BankMasterDTO bankMasterDTO) {
        // Perform validation
        RequestValidaton reqValidation = reqValidator.validateSaveUpdateRequest(bankMasterDTO);
        if (!reqValidation.isState()) {
            throw new ParameterConstraintException(reqValidation.getErrorMessage());
        }

        Optional<BankMaster> bankMasterOpt = bankMasterService.getBankMasterById(bankMasterDTO.getBankId());
        if (bankMasterOpt.isEmpty() || bankMasterOpt.get().getBankId() != bankMasterDTO.getBankId()) {
            throw new DataNotFoundException("Selected Bank Master does not exist to update.");
        }

        Optional<BankMaster> updatedBankMaster = bankMasterService.updateBankMaster(bankMasterDTO, bankMasterOpt.get());
        if (updatedBankMaster.isEmpty() || updatedBankMaster.get().getBankId() != bankMasterDTO.getBankId()) {
            throw new FailedToUpdateException("Failed to update Bank Master: " + bankMasterDTO.getBankCode());
        } else {
            return ResponseEntity.ok(updatedBankMaster.get());
        }
    }

    
    @GetMapping("/getallbankdetails")
    @Operation(tags = "Gets all Bank Master details.", description = "Fetches all records for Bank Master.")
    protected ResponseEntity<Iterable<BankMaster>> getAllBankMasterDetails() {
        return ResponseEntity.ok(bankMasterService.getAllBankMaster());
    }
     
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
}
