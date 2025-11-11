package com.eps.sitemanager.services.master;

import java.util.List;
import java.util.Optional;

import com.eps.sitemanager.dto.master.ChannelManagerDTO;

import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.ChannelManagerIdNameAndEmailId;

public interface ChannelManagerService {
	
	// methods to be implemented
	
			public ChannelManagerMaster saveChannelManagerMaster(ChannelManagerDTO channelManagerDTO);
			
			public Optional<ChannelManagerMaster> updateChannelManagerMaster(ChannelManagerDTO channelManagerDTO, ChannelManagerMaster channelManagerMaster);		
			
			public void deleteChannelManagerMaster(int channelManagerId);
			public Optional<ChannelManagerMaster> getChannelManagerMasterById(int channelManagerId);
			public Iterable<ChannelManagerMaster> getAllChannelManagerMaster();
			public List<ChannelManagerIdNameAndEmailId> getAllChannelManagerIdNameAndEmailId();			
			public Optional<ChannelManagerMaster> getChannelManagerMasterByName(String channelManagerName);
			
			

}
