package com.eps.sitemanager.repository.master;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.LandlordMaster;


public interface ChannelManagerRepository extends CrudRepository<ChannelManagerMaster, Integer> {
	
	List<ChannelManagerMaster> findByChannelManagerName(String channelManagerName);
	public List<ChannelManagerMaster> findByPhoneNo(String phoneNo);
	public List<ChannelManagerMaster> findByEmailId(String emailId);
	
	public List<ChannelManagerIdNameAndEmailId> findChannelManagerIdNameAndEmailIdBy();
	
}
