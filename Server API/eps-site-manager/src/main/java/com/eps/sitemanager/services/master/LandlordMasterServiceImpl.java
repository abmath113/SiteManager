package com.eps.sitemanager.services.master;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.master.LandlordMasterDTO;
import com.eps.sitemanager.dto.master.LandlordMasterSearchDTO;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.repository.master.LandlordIdNameAndAccountNo;
import com.eps.sitemanager.repository.master.LandlordMasterRepository;

@Service
public class LandlordMasterServiceImpl implements LandlordMasterService {

	private final LandlordMasterRepository landlordMasterRepo;

	@Autowired
	public LandlordMasterServiceImpl(LandlordMasterRepository userRepository) {
		this.landlordMasterRepo = userRepository;
	}

	@Override
	public LandlordMaster saveLandlordMaster(LandlordMasterDTO landlordMasterDTO) {
		LandlordMaster landlordMaster = new LandlordMaster();
		
		landlordMaster.setName(landlordMasterDTO.getName());
		landlordMaster.setBeneficiaryName(landlordMasterDTO.getBeneficiaryName());
		landlordMaster.setMobileNo(landlordMasterDTO.getMobileNo());
		landlordMaster.setIfscCode(landlordMasterDTO.getIfscCode());
		landlordMaster.setPan(landlordMasterDTO.getPan());
		landlordMaster.setAadharNo(landlordMasterDTO.getAadharNo());
		landlordMaster.setAccountNo(landlordMasterDTO.getAccountNo());
		landlordMaster.setStatus(landlordMasterDTO.isStatus());
		landlordMaster.setAddress(landlordMasterDTO.getAddress());
        landlordMaster.setGST(landlordMasterDTO.getIsGST());
		
		return landlordMasterRepo.save(landlordMaster);
	}

	@Override
	public Optional<LandlordMaster> updateLandlordMaster(LandlordMasterDTO landlordMasterDTO, LandlordMaster landlordMaster) {
		//Update all the required fields
		landlordMaster.setName(landlordMasterDTO.getName());
		landlordMaster.setBeneficiaryName(landlordMasterDTO.getBeneficiaryName());
		landlordMaster.setAddress(landlordMasterDTO.getAddress());
		landlordMaster.setMobileNo(landlordMasterDTO.getMobileNo());
		landlordMaster.setStatus(landlordMasterDTO.isStatus());
		landlordMaster.setAccountNo(landlordMasterDTO.getAccountNo());
		landlordMaster.setAadharNo(landlordMasterDTO.getAadharNo());
		landlordMaster.setIfscCode(landlordMasterDTO.getIfscCode());
		landlordMaster.setPan(landlordMasterDTO.getPan());
        landlordMaster.setGST(landlordMasterDTO.getIsGST());
		return Optional.of(landlordMasterRepo.save(landlordMaster));
	}

	@Override
	public void deleteLandlordMaster(int landlordId) {
		landlordMasterRepo.deleteById(landlordId);
	}
	
	@Override
	public int updateLandlordMasterStatus(boolean landlordStatus, int landlordId) {
		return landlordMasterRepo.updateLandlordMasterStatus(landlordStatus, landlordId);
	}

	@Override
	public Optional<LandlordMaster> getLandlordMasterById(int landlordId) {
		return landlordMasterRepo.findById(landlordId);
	
	}

	@Override
	public Iterable<LandlordMaster> getAllLandlordMaster() {
		return landlordMasterRepo.findAll();
	}

	@Override
	public List<LandlordMaster> getLandlordMasterbyName(String name) {
		List<LandlordMaster>landlordList = landlordMasterRepo.findByName(name);
		return landlordList;
	}

	@Override
	public List<LandlordMaster> searchLandlordMasters(LandlordMasterSearchDTO landlordMasterSearchDTO) {
		if(landlordMasterSearchDTO.getName() != null && !landlordMasterSearchDTO.getName().equals("")) {
			return landlordMasterRepo.findByName(landlordMasterSearchDTO.getName());
		}
		if(landlordMasterSearchDTO.getAddress()!=null && !landlordMasterSearchDTO.getAddress().equals("")) {
			return landlordMasterRepo.findByAddressLike(landlordMasterSearchDTO.getAddress());
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public Optional<LandlordMaster> getLandlordMasterbyAccountNoAndIfsc(String accountNo,String ifsc) {
		List<LandlordMaster> landlordList = landlordMasterRepo.findByAccountNoAndIfscCode(accountNo, ifsc);
		if(landlordList.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(landlordList.get(0));
		}
	}

	@Override
	public List<LandlordIdNameAndAccountNo> getAllLandlordIdNameAndAccountNo() {
		return landlordMasterRepo.findLandlordIdNameAndAccountNoBy();
	}


	
}
