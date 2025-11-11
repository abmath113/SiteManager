package com.eps.sitemanager.repository.master;

import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.eps.sitemanager.model.master.LandlordMaster;

public interface LandlordMasterRepository extends CrudRepository<LandlordMaster, Integer> {
	
	@Transactional 
	@Modifying 
	 
	@Query("update LandlordMaster s set s.status = ?1 where s.landlordId = ?2") 
	public int updateLandlordMasterStatus(boolean status, int landlordId);
	
	public List<LandlordMaster> findByName(String name);
	public List<LandlordMaster> findByMobileNo(String mobileNo);
	public List<LandlordMaster> findByIfscCode(String ifscCode);
	public List<LandlordMaster> findByAccountNo(String accountNo);
	public List<LandlordMaster> findByAccountNoAndIfscCode(String accountNo,String ifscCode);
	public List<LandlordMaster> findByPan(String pan);
	public List<LandlordMaster> findByAadharNo(String aadharNo);
	public List<LandlordMaster> findByStatus(boolean status);
	public List<LandlordMaster> findByAddressLike(String address);

	public List<LandlordIdNameAndAccountNo> findLandlordIdNameAndAccountNoBy();
	
	
}



