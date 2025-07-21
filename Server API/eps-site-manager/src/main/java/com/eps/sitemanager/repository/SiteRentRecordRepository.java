package com.eps.sitemanager.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;

public interface SiteRentRecordRepository extends CrudRepository<SiteRentRecord, Integer> {

 
	Optional<SiteRentRecord> findBySiteCodeAndRentMonth(String siteCode, String rentMonth);

	// Find SiteRentRecord by agreementId and rentMonth
	Optional<SiteRentRecord> findByAgreementId_AgreementIdAndRentMonth(int agreementId, String rentMonth);

	Optional<SiteRentRecord> findByAgreementId(RentAgreementMaster agreement);

	@Transactional 
	@Modifying
	@Query("select r from SiteRentRecord r where r.agreementId = ?1")
	List<SiteRentRecord> getSiteRentRecordsByAgreementId(RentAgreementMaster agreement);
	
}
