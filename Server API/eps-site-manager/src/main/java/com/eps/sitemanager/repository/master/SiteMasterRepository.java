package com.eps.sitemanager.repository.master;

import jakarta.transaction.Transactional;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.model.master.SiteMaster;

public interface SiteMasterRepository extends JpaRepository <SiteMaster, Integer> {
	
	@Transactional 
	@Modifying 
	@Query("UPDATE SiteMaster s set s.siteStatus = ?1 where s.siteId = ?2") 
	public int updateSiteMasterByStatusAndSiteId(boolean siteStatus, int siteId);
	
	public Optional<SiteMaster> findById(int siteId);
	public Optional<SiteMaster> findBySiteCode(String siteCode);
	public Optional<SiteMaster> findBySiteATMs(String siteATMs);
	public Optional<SiteMaster> findByBank(BankMaster bank);
	public List<SiteMaster> findBySiteStatus(boolean siteStatus);
	Optional<SiteMaster> findBySiteAddressContaining(String siteAddress);	
	public SiteMaster getReferenceById(int siteId);

	public List<SiteCodeAndSiteId> findSiteCodeAndSiteIdBy();
	
}