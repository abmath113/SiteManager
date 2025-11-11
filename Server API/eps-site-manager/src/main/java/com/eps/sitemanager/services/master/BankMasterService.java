package com.eps.sitemanager.services.master;

import java.util.List;
import java.util.Optional;

import com.eps.sitemanager.dto.master.BankMasterDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.repository.master.BankIdAndBankCode;
import com.eps.sitemanager.repository.master.ChannelManagerIdNameAndEmailId;


public interface BankMasterService {
	public Iterable<BankMaster> getAllBankMaster();

	public List<BankIdAndBankCode> getAllBankIdAndBankCode();
	
	public Optional<BankMaster> getBankMasterById(int bankId);

	public Optional<BankMaster> getBankMasterByBankCode(String bankCode);
	
	
	public BankMaster saveBankMaster(BankMasterDTO bankMasterDTO);
	

	public Optional<BankMaster> updateBankMaster(BankMasterDTO bankMasterDTO,BankMaster bankMaster);

	}
