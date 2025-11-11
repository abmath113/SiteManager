package com.eps.sitemanager.dto;

import java.util.List;

import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Rent Agreement Record DTO")
public class RentAgreementRecordDTO {
	
	private RentAgreementMaster agreementid;
	List<SiteRentRecord> siteRentRecordList;
	

}
