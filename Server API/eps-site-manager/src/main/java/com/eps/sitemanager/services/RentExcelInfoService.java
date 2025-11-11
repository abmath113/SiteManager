package com.eps.sitemanager.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.model.RentExcelInfo;

public interface RentExcelInfoService {

	public List<RentExcelInfo> parseExcel(MultipartFile file) throws Exception;

	public boolean isRowEmpty(RentExcelInfo rentInfo);

	public boolean doesExcelSiteExistInDB(String excelSiteCode);

	public int getRentForMonth(String siteCode, LocalDate date);

	public List<String> getNonExistentExcelSiteCodes(List<RentExcelInfo> RentInfoExcel);

	public List<String> getMismatchedSites(List<RentExcelInfo> rentInfoList);

	public void logAndAddMismatch(List<String> mismatchedSiteCodes, RentExcelInfo rentInfo, String message);
}
