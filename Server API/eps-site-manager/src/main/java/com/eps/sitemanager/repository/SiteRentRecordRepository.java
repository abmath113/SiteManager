package com.eps.sitemanager.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;

public interface SiteRentRecordRepository extends CrudRepository<SiteRentRecord, Integer> {

 
	Optional<SiteRentRecord> findBySiteCodeAndRentMonth(String siteCode, String rentMonth);

	// Find SiteRentRecord by agreementId and rentMonth
	//Optional<SiteRentRecord> findByAgreementId_AgreementIdAndRentMonth(int agreementId, String rentMonth); //change back to optional after debug
	
/*	Multiple duplicate records were getting fetched while service method was expecting a single record
	Changed repository method to native query to retrieve single record using LIMIT in the query
*/
	@Query(value = "SELECT * FROM site_rent_records WHERE agreement_id_agreement_id = :agreementId AND rent_month = :rentMonth LIMIT 1", nativeQuery = true)
	Optional<SiteRentRecord> findOneByAgreementIdAndRentMonth(@Param("agreementId") int agreementId, @Param("rentMonth") String rentMonth);


	Optional<SiteRentRecord> findByAgreementId(RentAgreementMaster agreement);

	@Transactional 
	@Modifying
	@Query("select r from SiteRentRecord r where r.agreementId = ?1")
	List<SiteRentRecord> getSiteRentRecordsByAgreementId(RentAgreementMaster agreement);
	
}
