package com.eps.sitemanager.services.rentmanager;

import java.time.LocalDate;
import java.util.List;

import com.eps.sitemanager.dto.rentmanager.GeneratedRentDTO;

public interface RentManagerService {
	
	public List<Integer> getRentListByAgreementId(int agreementId); 
	
	public List<GeneratedRentDTO> getAllRentbyCurrDate(LocalDate currDate);
	
	public int getRentForSiteCodeByDate(String siteCode, LocalDate currDate);
}
