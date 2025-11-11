package com.eps.sitemanager.services;

import java.util.List;
import java.util.Optional;

import com.eps.sitemanager.dto.SiteRentRecordDTO;
import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;

public interface SiteRentRecordService {

	public SiteRentRecord saveSiteRentRecord(SiteRentRecordDTO siteRentRecordDTO);

	public Optional<SiteRentRecord> updateSiteRentRecord(SiteRentRecordDTO siteRentRecordDTO,
			SiteRentRecord siteRentRecord);

	public void deleteSiteRentRecord(int siteRentRecordId);

	public Optional<SiteRentRecord> findBySiteCodeAndRentMonth(String siteCode, String rentMonth);

	public Optional<SiteRentRecord> getSiteRentRecord(int agreementId, String rentMonth);

	List<SiteRentRecord> getSiteRentRecordList(RentAgreementMaster agreement);

}
