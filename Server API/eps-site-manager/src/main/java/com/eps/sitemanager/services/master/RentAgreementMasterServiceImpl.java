package com.eps.sitemanager.services.master;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.master.RentAgreementMasterDTO;
import com.eps.sitemanager.dto.master.RentAgreementMasterSearchDTO;
import com.eps.sitemanager.dto.master.RentAgreementTerminateDTO;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.RentAgreementMasterRepository;

@Service
public class RentAgreementMasterServiceImpl implements RentAgreementMasterService {

	@Autowired
	private RentAgreementMasterRepository rentAgreementMasterRepository;

    @Override
	public RentAgreementMaster saveRentAgreementMaster(RentAgreementMasterDTO rentAgreementMasterDTO,
			SiteMaster siteMaster, LandlordMaster landlordMaster) {

		RentAgreementMaster rentAgreementMaster = new RentAgreementMaster();

		// Setting values from DTO to Entity
		rentAgreementMaster.setAgreementId(rentAgreementMasterDTO.getAgreementId());
		rentAgreementMaster.setSiteId(siteMaster);
		rentAgreementMaster.setLandlordId(landlordMaster);
		rentAgreementMaster.setAgreementDate(rentAgreementMasterDTO.getAgreementDate());
		rentAgreementMaster.setRentPayStartDate(rentAgreementMasterDTO.getRentPayStartDate());
		rentAgreementMaster.setAgreementEndDate(rentAgreementMasterDTO.getAgreementEndDate());
		rentAgreementMaster.setSolarPanelRent(rentAgreementMasterDTO.getSolarPanelRent());
		rentAgreementMaster.setMonthlyRent(rentAgreementMasterDTO.getMonthlyRent());
		rentAgreementMaster.setDeposit(rentAgreementMasterDTO.getDeposit());
		rentAgreementMaster.setPaymentInterval(rentAgreementMasterDTO.getPaymentInterval());
		rentAgreementMaster.setRentAgreementStatus(rentAgreementMasterDTO.isRentAgreementStatus());
		rentAgreementMaster.setEscalationAfterMonths(rentAgreementMasterDTO.getEscalationAfterMonths());
		rentAgreementMaster.setEscalationPercent(rentAgreementMasterDTO.getEscalationPercent());
		//so that termination date can be added through bulk-upload
		rentAgreementMaster.setTerminationDate(rentAgreementMasterDTO.getTerminationDate());




		// Saving the RentAgreementMaster entity
		return rentAgreementMasterRepository.save(rentAgreementMaster);
	}
	
	
	

	@Override
	public boolean isRentAgreementDuplicate(SiteMaster siteId, LandlordMaster landlordId, LocalDate agreementDate) {

		List<RentAgreementMaster> agreementList = rentAgreementMasterRepository.getRentAgreementBySiteIdAndLandlordId(landlordId.getLandlordId(), siteId.getSiteId());

		if (agreementList == null || agreementList.isEmpty()) {
		    return false;
		}

		for (RentAgreementMaster rentAgreement : agreementList) {
		    // Calculating agreement start and end dates for comparison with given agreement date
		    LocalDate existAgreementStartDate = rentAgreement.getAgreementDate();
		    LocalDate existAgreementEndDate = rentAgreement.getAgreementEndDate();
		    
		    // Skip this agreement if either start or end date is null
		    if (existAgreementStartDate == null || existAgreementEndDate == null) {
		        continue;
		    }

		    // Check if agreementDate is equal to or lies between start and end dates (inclusive)
		    if (agreementDate.compareTo(existAgreementStartDate) >= 0 && 
		        agreementDate.compareTo(existAgreementEndDate) <= 0) {
		        return true;
		    }
		}

		return false;

	}

	@Override
	public Optional<RentAgreementMaster> updateRentAgreementOptional(RentAgreementMasterDTO rentAgreementMasterDTO,
			RentAgreementMaster rentAgreementMaster) {
		// Updating values from DTO to existing entity
		rentAgreementMaster.setAgreementDate(rentAgreementMasterDTO.getAgreementDate());
		rentAgreementMaster.setRentPayStartDate(rentAgreementMasterDTO.getRentPayStartDate());
		rentAgreementMaster.setAgreementSpan(rentAgreementMasterDTO.getAgreementSpan());
		rentAgreementMaster.setSolarPanelRent(rentAgreementMasterDTO.getSolarPanelRent());
		rentAgreementMaster.setMonthlyRent(rentAgreementMasterDTO.getMonthlyRent());
		rentAgreementMaster.setDeposit(rentAgreementMasterDTO.getDeposit());
		rentAgreementMaster.setPaymentInterval(rentAgreementMasterDTO.getPaymentInterval());
		rentAgreementMaster.setRentAgreementStatus(rentAgreementMasterDTO.isRentAgreementStatus());
		rentAgreementMaster.setEscalationAfterMonths(rentAgreementMasterDTO.getEscalationAfterMonths());
		rentAgreementMaster.setEscalationPercent(rentAgreementMasterDTO.getEscalationPercent());

		// Saving the updated RentAgreementMaster entity
		RentAgreementMaster updatedRentAgreementMaster = rentAgreementMasterRepository.save(rentAgreementMaster);
		return Optional.of(updatedRentAgreementMaster);
	}

	@Override
	public Optional<RentAgreementMaster> terminateRentAgreementOptional(
			RentAgreementTerminateDTO rentAgreementTerminateDTO, RentAgreementMaster rentAgreementMaster) {

		rentAgreementMaster.setTerminationDate(rentAgreementTerminateDTO.getTerminationDate());
		rentAgreementMaster.setTerminationRemark(rentAgreementTerminateDTO.getTerminationRemark());

		// Saving the updated RentAgreementMaster entity
		RentAgreementMaster terminatedRentAgreementMaster = rentAgreementMasterRepository.save(rentAgreementMaster);
		return Optional.of(terminatedRentAgreementMaster);
	}

	@Override
	public void deleteRentAgreementMaster(int agreementId) {
		// Deleting RentAgreementMaster by agreementId
		rentAgreementMasterRepository.deleteById(agreementId);
	}

	@Override
	public int updateRentAgreementMasterStatus(boolean rentAgreementStatus, int agreementId) {
		return rentAgreementMasterRepository.updateRentAgreementMasterByStatusAndAgreementId(rentAgreementStatus,
				agreementId);
	}

	@Override
	public Optional<RentAgreementMaster> getRentAgreementMasterById(int agreementId) {

		// Fetching RentAgreementMaster by agreementId
		return rentAgreementMasterRepository.findById(agreementId);
	}

	@Override
	public Optional<RentAgreementMaster> getRentAgreementMasterBySiteId(int siteId) {
		// Fetching RentAgreementMaster by siteId
		List<RentAgreementMaster> rentAgreementMaster = rentAgreementMasterRepository.getRentAgreementBySiteId(siteId);

		if (rentAgreementMaster.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(rentAgreementMaster.get(0));
	}

	@Override
	public Optional<RentAgreementMaster> getRentAgreementMasterByLandlordId(int landlordId) {
		// Fetching RentAgreementMaster by landlordId
		List<RentAgreementMaster> rentAgreementMaster = rentAgreementMasterRepository
				.getRentAgreementByLandlordId(landlordId);
		if (rentAgreementMaster.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(rentAgreementMaster.get(0));
	}

	@Override
	public Iterable<RentAgreementMaster> getAllRentAgreementMaster() {
		// Fetching all RentAgreementMaster entities
		return rentAgreementMasterRepository.findAll();
	}

	@Override
	public List<RentAgreementMaster> searchRentAgreementMasters(
			RentAgreementMasterSearchDTO rentAgreementMasterSearchDTO) {

		List<RentAgreementMaster> results = new ArrayList<>();

		if (rentAgreementMasterSearchDTO.getSitecode() != null
				&& !rentAgreementMasterSearchDTO.getSitecode().equals("")) {
			results = rentAgreementMasterRepository
					.getRentAgreementBySiteCode(rentAgreementMasterSearchDTO.getSitecode());
		}

		// Check if both sitecode and name are provided
		if (rentAgreementMasterSearchDTO.getName() != null && !rentAgreementMasterSearchDTO.getName().equals("")) {

			// Fetch RentAgreementMaster using both siteId and landlordId
			results = rentAgreementMasterRepository
					.getRentAgreementByLandlordName(rentAgreementMasterSearchDTO.getName());

		}

		return results;
	}

	public Integer getAgreementIdBySiteCode(String siteCode) {
		// Fetch the list of RentAgreementMaster entities based on siteCode
		List<RentAgreementMaster> agreements = rentAgreementMasterRepository.getRentAgreementBySiteCode(siteCode);

		// Check if the list is empty
		if (agreements.isEmpty()) {
			System.out.println("No agreement found for the given site code");
		}

		// Return the agreementId from the first entry, assuming each siteCode
		// corresponds to one agreement
		return agreements.get(0).getAgreementId(); // Assuming agreementId is the primary key in RentAgreementMaster
	}

	@Override
	public List<RentAgreementMaster> getRentAgreementMasterListBySiteCode(String siteCode) {

		List<RentAgreementMaster> rentAgreementListBySiteCode = rentAgreementMasterRepository
				.getRentAgreementBySiteCode(siteCode);

		return rentAgreementListBySiteCode;
	}

}
