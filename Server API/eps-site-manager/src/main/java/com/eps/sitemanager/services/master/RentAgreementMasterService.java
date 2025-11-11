package com.eps.sitemanager.services.master;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.eps.sitemanager.dto.master.RentAgreementMasterDTO;
import com.eps.sitemanager.dto.master.RentAgreementMasterSearchDTO;
import com.eps.sitemanager.dto.master.RentAgreementTerminateDTO;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.model.master.SiteMaster;

public interface RentAgreementMasterService {

	// methods to be implemented
	
	// CRUD operations on Rent Agreement
	public RentAgreementMaster saveRentAgreementMaster(RentAgreementMasterDTO rentAgreementMasterDTO, 
	SiteMaster siteMaster, LandlordMaster landlordMaster);
	boolean isRentAgreementDuplicate(SiteMaster siteId, LandlordMaster landlordId,LocalDate agreementDate);
	public Optional<RentAgreementMaster> updateRentAgreementOptional(RentAgreementMasterDTO rentAgreementMasterDTO, RentAgreementMaster rentAgreementMaster);	
	public Optional<RentAgreementMaster> terminateRentAgreementOptional(RentAgreementTerminateDTO rentAgreementTerminateDTO, RentAgreementMaster rentAgreementMaster);
	public void deleteRentAgreementMaster(int agreementId);
	public int updateRentAgreementMasterStatus(boolean rentAgreementStatus, int agreementId);
	
	//Fetching Rent Agreement
	public Optional<RentAgreementMaster> getRentAgreementMasterById(int agreementId); 
	public List<RentAgreementMaster> getRentAgreementMasterListBySiteCode(String siteCode);
	public Optional<RentAgreementMaster> getRentAgreementMasterBySiteId(int siteId); 
	public Optional<RentAgreementMaster> getRentAgreementMasterByLandlordId(int landlordId); 
	 public Integer getAgreementIdBySiteCode(String siteCode);
	
	public List<RentAgreementMaster> searchRentAgreementMasters(RentAgreementMasterSearchDTO rentAgreementMasterSearchDTO);
		
	public Iterable<RentAgreementMaster> getAllRentAgreementMaster();
	
	
}