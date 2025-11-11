package com.eps.sitemanager.repository.master;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.model.master.SiteMaster;

public interface RentAgreementMasterRepository extends CrudRepository<RentAgreementMaster,Integer> {
	
	@Transactional 
	@Modifying 
	@Query("select r from RentAgreementMaster r where r.siteId.siteId = ?1") 
	public List<RentAgreementMaster> getRentAgreementBySiteId(int siteId);
	
	
	@Transactional 
	@Modifying 
	@Query("select r from RentAgreementMaster r where r.landlordId.landlordId = ?1") 
	public List<RentAgreementMaster> getRentAgreementByLandlordId(int landlordId);
	
	
	public List<RentAgreementMaster> findByPaymentInterval(int paymentInterval);
	
	
	@Transactional 
	@Modifying 
	@Query("update RentAgreementMaster s set s.rentAgreementStatus = ?1 where s.agreementId = ?2") 
	public int updateRentAgreementMasterByStatusAndAgreementId(boolean rentAgreementStatus, int agreementId);
	
	
	@Transactional 
	@Modifying 
	@Query("select r from RentAgreementMaster r where r.landlordId.landlordId = ?1 And r.siteId.siteId = ?2") 
	public List<RentAgreementMaster> getRentAgreementBySiteIdAndLandlordId(int landlordId, int siteId);
	
	
	@Transactional 
	@Modifying 
	@Query("select r from RentAgreementMaster r where r.siteId.siteCode = ?1") 
	public List<RentAgreementMaster> getRentAgreementBySiteCode(String siteCode);
	
	@Transactional 
	@Modifying 
	@Query("select r from RentAgreementMaster r where r.landlordId.name= ?1") 
	public List<RentAgreementMaster> getRentAgreementByLandlordName(String landlordName);
	

	
	public boolean existsBySiteIdAndLandlordId(SiteMaster siteId, LandlordMaster landlordId);
	
	
}


