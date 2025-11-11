package com.eps.sitemanager.dto;

import java.util.List;
import com.eps.sitemanager.model.master.SiteMaster;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Site Rent History DTO")
					
// this is what we're finally returning to frontend.

public class SiteHistoryDTO {
	
	private SiteMaster siteMaster;
	List<RentAgreementRecordDTO> rentAgreementRecordDTOList;
	

}
