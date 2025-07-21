package com.eps.sitemanager.controller.master;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.sitemanager.repository.master.BankIdAndBankCode;
import com.eps.sitemanager.services.master.BankMasterService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/bankmaster")
public class BankMasterController {
	
	
	private final BankMasterService bankMasterService;
	
	
	@Autowired
	public BankMasterController(BankMasterService bankMasterService) {

		this.bankMasterService = bankMasterService;
	}
	
	@GetMapping("/getallbankidandbankcode")
	@Operation(tags = "Gets all Bank ID and Code", description = "Fetches all records for bank master")
	protected ResponseEntity<List<BankIdAndBankCode>> getAllBankIdAndBankCode(){
		
		return ResponseEntity.ok(bankMasterService.getAllBankIdAndBankCode());
		
	}
	
}
