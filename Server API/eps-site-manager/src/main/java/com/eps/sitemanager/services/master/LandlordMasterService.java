package com.eps.sitemanager.services.master;
import java.util.List;
import java.util.Optional;

import com.eps.sitemanager.dto.master.LandlordMasterDTO;
import com.eps.sitemanager.dto.master.LandlordMasterSearchDTO;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.LandlordIdNameAndAccountNo;
import com.eps.sitemanager.repository.master.SiteCodeAndSiteId;
public interface LandlordMasterService {
	
	
	// methods to be implemented
		public LandlordMaster saveLandlordMaster(LandlordMasterDTO landlordMasterDTO);
		public Optional<LandlordMaster> updateLandlordMaster(LandlordMasterDTO landlordMasterDTO, LandlordMaster landlordMaster);		
		public void deleteLandlordMaster(int landlordId);
		public int updateLandlordMasterStatus(boolean landlordStatus, int landlordId);
		
		public Optional<LandlordMaster> getLandlordMasterById(int landlordId);
		public Iterable<LandlordMaster> getAllLandlordMaster();
		public List<LandlordMaster> getLandlordMasterbyName(String name);
//		public boolean checkLandlordAlreadyExists(String AccountNo, String Ifsc);
		public Optional<LandlordMaster> getLandlordMasterbyAccountNoAndIfsc(String Accountno,String Ifsc);
		public List<LandlordMaster> searchLandlordMasters(LandlordMasterSearchDTO landlordMasterSearchDTO);
		
		
		public List<LandlordIdNameAndAccountNo> getAllLandlordIdNameAndAccountNo();
}
