package com.eps.sitemanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.RentAgreementRecordDTO;
import com.eps.sitemanager.dto.SiteHistoryDTO;
import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.SiteRentRecordRepository;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.services.master.SiteMasterService;

@Service
public class SiteHistoryServiceImpl implements SiteHistoryService {
	
	private final SiteMasterService siteMasterService;
	private final RentAgreementMasterService rentAgreementMasterService;
	private final SiteRentRecordService siteRentRecordService;

	// Constructor-based dependency injection is preferred over setter injection
	@Autowired
	public SiteHistoryServiceImpl(SiteMasterService siteMasterService,
			RentAgreementMasterService rentAgreementMasterService, SiteRentRecordService siteRentRecordService) {
		this.siteMasterService = siteMasterService;
		this.rentAgreementMasterService = rentAgreementMasterService;
		this.siteRentRecordService = siteRentRecordService;
	}

	@Override
	public SiteHistoryDTO getSiteHistoryBySiteCode(String siteCode) {
		
		// Validate input
		if (siteCode == null || siteCode.trim().isEmpty()) {
			throw new IllegalArgumentException("Site code cannot be null or empty");
		}

		// Create a new SiteHistoryDTO to store the site history
		SiteHistoryDTO siteHistoryDTO = new SiteHistoryDTO();
		siteHistoryDTO.setRentAgreementRecordDTOList(new ArrayList<>());

		// Fetch site master using site code
		Optional<SiteMaster> siteMasterOptional = siteMasterService.getSiteMasterBySiteCode(siteCode);

		// Check if site master exists
		if (!siteMasterOptional.isPresent()) {
			// Return an empty site history if no site is found
			return siteHistoryDTO;
		}

		// Get site ID
		SiteMaster siteMaster = siteMasterOptional.get();
		
		siteHistoryDTO.setSiteMaster(siteMaster);

		// Fetch rent agreement masters for the given site code
		List<RentAgreementMaster> rentAgreementMasterListBySiteCode = rentAgreementMasterService
				.getRentAgreementMasterListBySiteCode(siteCode);

		// Populate site history with rent agreement records
		for (RentAgreementMaster rentAgreementMaster : rentAgreementMasterListBySiteCode) {
			
			// Get all site rent records for the current site ID
			List<SiteRentRecord> siteRentRecordList = siteRentRecordService.getSiteRentRecordList(rentAgreementMaster);

			// Create a rent agreement record DTO
			RentAgreementRecordDTO rentAgreementRecordDTO = new RentAgreementRecordDTO();
			rentAgreementRecordDTO.setSiteRentRecordList(siteRentRecordList);
			rentAgreementRecordDTO.setAgreementid(rentAgreementMaster);

			// Add the rent agreement record to the site history
			siteHistoryDTO.getRentAgreementRecordDTOList().add(rentAgreementRecordDTO);
		}

		// Return the populated site history DTO
		return siteHistoryDTO;
	}
}