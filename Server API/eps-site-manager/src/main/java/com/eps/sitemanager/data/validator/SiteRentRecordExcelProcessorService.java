package com.eps.sitemanager.data.validator;

import java.io.File;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface SiteRentRecordExcelProcessorService {
	
	ResponseEntity<?> processExcelFile(MultipartFile file, String rentForMonth);

	boolean processSiteRentUploadedFile(File excelInputFile, File excelOutputFile, String rentForMonth);
}
