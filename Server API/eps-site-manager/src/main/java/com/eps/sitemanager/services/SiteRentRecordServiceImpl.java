package com.eps.sitemanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.SiteRentRecordDTO;
import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.repository.SiteRentRecordRepository;

@Service
public class SiteRentRecordServiceImpl implements SiteRentRecordService {

	@Autowired
	private SiteRentRecordRepository siteRentRecordRepository;

	@Override
	public SiteRentRecord saveSiteRentRecord(SiteRentRecordDTO siteRentRecordDTO) {

		SiteRentRecord siteRentRecord = new SiteRentRecord();

		// Map DTO to entity
		siteRentRecord.setAgreementId(siteRentRecordDTO.getAgreementId());
		siteRentRecord.setGeneratedRent(siteRentRecordDTO.getGeneratedRent());
		siteRentRecord.setRentMonth(siteRentRecordDTO.getRentMonth());

		return siteRentRecordRepository.save(siteRentRecord);
	}

	@Override
	public Optional<SiteRentRecord> updateSiteRentRecord(SiteRentRecordDTO siteRentRecordDTO,
			SiteRentRecord siteRentRecord) {

		// Check if the rent record exists in the database
		siteRentRecord.setSiteCode(siteRentRecordDTO.getSiteCode());
		siteRentRecord.setPaymentDate(siteRentRecordDTO.getPaymentDate());
		siteRentRecord.setAmountPaid(siteRentRecordDTO.getAmountPaid());
		siteRentRecord.setRemarks(siteRentRecordDTO.getRemarks());
		siteRentRecord.setUtrNo(siteRentRecordDTO.getUtrNo());
		siteRentRecord.setTransactionStatus(siteRentRecordDTO.isTransactionStatus());
		siteRentRecord.setReason(siteRentRecordDTO.getReason());

		return Optional.of(siteRentRecordRepository.save(siteRentRecord));

	}

	@Override
	public void deleteSiteRentRecord(int siteRentRecordId) {
		// TODO Auto-generated method stub

	}

	public Optional<SiteRentRecord> findBySiteCodeAndRentMonth(String siteCode, String rentMonth) {
		return siteRentRecordRepository.findBySiteCodeAndRentMonth(siteCode, rentMonth);
	}

	@Override
	public Optional<SiteRentRecord> getSiteRentRecord(int agreementId, String rentMonth) {

		return siteRentRecordRepository.findByAgreementId_AgreementIdAndRentMonth(agreementId, rentMonth);

	}
	
	@Override
	public List<SiteRentRecord> getSiteRentRecordList(RentAgreementMaster agreement){
		return siteRentRecordRepository.getSiteRentRecordsByAgreementId(agreement);
	}

}
