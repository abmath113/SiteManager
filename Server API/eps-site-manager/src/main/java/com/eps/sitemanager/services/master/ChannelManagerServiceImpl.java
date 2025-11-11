package com.eps.sitemanager.services.master;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.master.ChannelManagerDTO;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.ChannelManagerIdNameAndEmailId;
import com.eps.sitemanager.repository.master.ChannelManagerRepository;

@Service
public class ChannelManagerServiceImpl implements ChannelManagerService {

	private final ChannelManagerRepository channelManagerRepository;

	@Autowired
	public ChannelManagerServiceImpl(ChannelManagerRepository channelManagerRepository) {
		this.channelManagerRepository = channelManagerRepository;
	}

	@Override
	public ChannelManagerMaster saveChannelManagerMaster(ChannelManagerDTO channelManagerDTO) {
		ChannelManagerMaster channelManagerMaster = new ChannelManagerMaster();
		channelManagerMaster.setChannelManagerName(channelManagerDTO.getChannelManagerName());
		channelManagerMaster.setPhoneNo(channelManagerDTO.getPhoneNo());
		channelManagerMaster.setEmailId(channelManagerDTO.getEmailId());
		channelManagerMaster.setStatus(channelManagerDTO.isStatus());
		return channelManagerRepository.save(channelManagerMaster);

	}

	@Override
	public Optional<ChannelManagerMaster> updateChannelManagerMaster(ChannelManagerDTO channelManagerDTO,
			ChannelManagerMaster channelManagerMaster) {

		channelManagerMaster.setChannelManagerName(channelManagerDTO.getChannelManagerName());
		channelManagerMaster.setPhoneNo(channelManagerDTO.getPhoneNo());
		channelManagerMaster.setEmailId(channelManagerDTO.getEmailId());
		channelManagerMaster.setStatus(channelManagerDTO.isStatus());
		return Optional.of(channelManagerRepository.save(channelManagerMaster));
	}

	@Override
	public void deleteChannelManagerMaster(int channelManagerId) {
		channelManagerRepository.deleteById(channelManagerId);

	}

	@Override
	public Optional<ChannelManagerMaster> getChannelManagerMasterById(int channelManagerId) {

		return channelManagerRepository.findById(channelManagerId);
	}

	@Override
	public Iterable<ChannelManagerMaster> getAllChannelManagerMaster() {
		return channelManagerRepository.findAll();
	}

	@Override
	public List<ChannelManagerIdNameAndEmailId> getAllChannelManagerIdNameAndEmailId() {

		return channelManagerRepository.findChannelManagerIdNameAndEmailIdBy();

	}

	@Override
	public Optional<ChannelManagerMaster> getChannelManagerMasterByName(String channelManagerName) {
		List<ChannelManagerMaster> cmList = channelManagerRepository.findByChannelManagerName(channelManagerName);
		
		if(!cmList.isEmpty()) {
			return Optional.of(cmList.get(0));
		}
		return Optional.empty();
	}
	
}
