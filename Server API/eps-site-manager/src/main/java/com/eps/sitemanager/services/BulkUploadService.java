package com.eps.sitemanager.services;

import java.io.File;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BulkUploadService {
	
	File processBulkUpload(MultipartFile file) throws Exception;
}
