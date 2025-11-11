package com.eps.sitemanager.repository.master;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eps.sitemanager.model.master.BankMaster;

public interface BankMasterRepository extends CrudRepository<BankMaster, Integer>{
	public List<BankMaster> findAll();
	public List<BankIdAndBankCode> findBankIdAndBankCodeBy();
	public List<BankMaster> findByBankId(Integer bankId);
	public List<BankMaster> findByBankName(String bankName);
	public List<BankMaster> findByBankCode(String bankCode);
}
