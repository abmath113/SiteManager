package com.eps.sitemanager.services.master;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.master.BankMasterDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.repository.master.BankIdAndBankCode;
import com.eps.sitemanager.repository.master.BankMasterRepository;

@Service
public class BankMasterServiceImpl implements BankMasterService{
	
	private final BankMasterRepository bankMasterRepository;
	
	@Autowired
	public BankMasterServiceImpl(BankMasterRepository bankMasterRepository) {
        this.bankMasterRepository = bankMasterRepository;
    }

	@Override
	public Iterable<BankMaster> getAllBankMaster() {
		return bankMasterRepository.findAll();
	}

	@Override
	public List<BankIdAndBankCode> getAllBankIdAndBankCode() {
		return bankMasterRepository.findBankIdAndBankCodeBy();
	}

	@Override
	public Optional<BankMaster> getBankMasterById(int bankId) {
		return bankMasterRepository.findById(bankId);
	}

	@Override
	public Optional<BankMaster> getBankMasterByBankCode(String bankCode) {
		List<BankMaster> bankList = bankMasterRepository.findByBankCode(bankCode);
		
		if(!bankList.isEmpty()) {
			return Optional.of(bankList.get(0));
		}
		
		return Optional.empty();
	}

	@Override
	public BankMaster saveBankMaster(BankMasterDTO bankMasterDTO) {
		BankMaster bankMaster = new BankMaster();
		bankMaster.setBankCode(bankMasterDTO.getBankCode());
		bankMaster.setBankName(bankMasterDTO.getBankName());
		return bankMasterRepository.save(bankMaster);
	}

	@Override
	public Optional<BankMaster> updateBankMaster(BankMasterDTO bankMasterDTO,BankMaster bankMaster) {
		bankMaster.setBankCode(bankMasterDTO.getBankCode());
		bankMaster.setBankName(bankMasterDTO.getBankName());
		
		return Optional.of(bankMasterRepository.save(bankMaster));
	}

	

}
