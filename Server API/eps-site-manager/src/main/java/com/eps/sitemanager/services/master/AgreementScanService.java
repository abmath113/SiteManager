package com.eps.sitemanager.services.master;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AgreementScanService {

	@Value("${file.upload-dir}")
	private String uploadDir; // this sets the path where agreement scans would be stored

	
	
	public Path store(MultipartFile file, String siteCode, String landlordName) throws IOException {
		
		String orgFileName =  file.getOriginalFilename();
		String orgFileExtString = orgFileName.substring(orgFileName.lastIndexOf("."));
		String landLordNameSeq = landlordName.replaceAll("\s+", "_");
	    String fileToSaveName = siteCode + "_" + landLordNameSeq + orgFileExtString;
	    Path fileToSavePath = Paths.get(uploadDir).resolve(fileToSaveName);
	    
	   

	    // Log the upload directory and file path
	    System.out.println("Upload Directory: " + uploadDir);
	    System.out.println("Full File Path: " + fileToSavePath.toString());

	    // Ensure directory exists
	    File directory = new File(uploadDir);
	    if (!directory.exists()) {
	        directory.mkdirs();
	    }

	    // Save file to directory
	   return Files.write(fileToSavePath, file.getBytes());
	}

	
	public Path fetchFile(String siteCode, String landlordName) throws IOException {
		
	    // Prepare the filename based on siteCode and landlordName
	    String landLordNameSeq = landlordName.replaceAll("\\s+", "_");
	    String filePattern = siteCode + "_" + landLordNameSeq;

	    // Ensure directory exists
	    File directory = new File(uploadDir);
	    if (!directory.exists()) {
	        throw new FileNotFoundException("Directory does not exist: " + uploadDir);
	    }

	    // Search for the file within the directory that matches the pattern
	    File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(filePattern));
	    if (matchingFiles == null || matchingFiles.length == 0) {
	        throw new FileNotFoundException("No file found for siteCode: " + siteCode + " and landlordName: " + landlordName);
	    }

	    // If multiple files match, return the first one found (assuming unique files per combination)
	    return matchingFiles[0].toPath();
	}


}
