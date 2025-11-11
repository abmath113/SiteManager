package com.eps.sitemanager.controller.master;


import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.message.ResponseMessage;
import com.eps.sitemanager.services.master.AgreementScanService;



@RestController
@RequestMapping("/api/agreementscan")
public class AgreementScanController {
	
	
	 @Autowired
	  private AgreementScanService agreementScanService;

	 @PostMapping("/upload")
	 public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, 
			 @RequestParam("siteCode") String siteCode,
			 @RequestParam("landlordName") String landlordName) {
	     String message;
	     try {
	         if (file.isEmpty()) {
	             message = "Failed to upload empty file: " + file.getOriginalFilename();
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	         }

	         // Store the file and get the database entry
	         Path savedFilePath = agreementScanService.store(file, siteCode, landlordName);
	         if(savedFilePath == null) {
	        	 message = "Failed to upload file: " + file.getOriginalFilename();
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	         }

	         // Generate the response message
	         message = "Uploaded the file successfully: " + savedFilePath.getFileName();
	         return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	     } catch (IOException e) {
	         message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
	     }
	 }
	 
	  // Endpoint to fetch and download a file based on siteCode and landlordName
	    @GetMapping("/get-agreementScan")
	 public ResponseEntity<UrlResource> downloadFile(
	            @RequestParam("siteCode") String siteCode,
	            @RequestParam("landlordName") String landlordName) {
	        try {
	            Path filePath = agreementScanService.fetchFile(siteCode, landlordName);
	            UrlResource resource = new UrlResource(filePath.toUri());

	            if (resource.exists() || resource.isReadable()) {
	                // Extract original filename from the path
	                String fileName = filePath.getFileName().toString();
	                
	             
	                return ResponseEntity.ok()
	                        
	                        .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
	                        .header("Content-Type", "application/octet-stream")
	                        .header("Content-transfer-encoding", "binary") // Specifies binary transfer encoding
	                        .header("Accept-Ranges", "bytes") // Enables byte-range requests for large files
	                        .contentType(MediaType.APPLICATION_PDF)
	                        .body(resource);
	                		
	                
	              
	            } else {
	                return ResponseEntity.status(404).body(null);
	            }
	        } catch (IOException e) {
	            return ResponseEntity.status(500).body(null);
	        }
	    }
	 
	

}
