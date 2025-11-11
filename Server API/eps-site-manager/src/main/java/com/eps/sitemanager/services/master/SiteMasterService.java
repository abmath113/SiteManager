package com.eps.sitemanager.services.master;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.eps.sitemanager.dto.master.SiteMasterDTO;
import com.eps.sitemanager.dto.master.SiteMasterSearchDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.SiteCodeAndSiteId;

public interface SiteMasterService {

	
	// methods to be implemented
	public SiteMaster saveSiteMaster(SiteMasterDTO siteMasterDTO,ChannelManagerMaster channelManagerMaster,BankMaster bankMaster);
	
	
	Optional<SiteMaster> updateSiteMaster(SiteMasterDTO siteMasterDTO, SiteMaster siteMaster,
			ChannelManagerMaster channelManagerMaster, BankMaster bankMaster);
	
	public void deleteSiteMaster(int siteId);
	public int updateSiteMasterStatus(boolean siteStatus, int siteId);
	
	public Optional<SiteMaster> getSiteMasterById(int siteId);
	public Optional<SiteMaster> getSiteMasterBySiteCode(String siteCode);
	public Iterable<SiteMaster> getAllSiteMaster();
	public Optional<SiteMaster> searchSiteMasters(SiteMasterSearchDTO siteMasterSearchDTO);
	
	public boolean checkSiteAlreadyExists(String siteCode);
	
	public List<SiteCodeAndSiteId> getAllSitesOnlySiteIdAndSiteCode();
	
	public boolean checkSiteCodeExists(String siteCode);
	
	
		

}
