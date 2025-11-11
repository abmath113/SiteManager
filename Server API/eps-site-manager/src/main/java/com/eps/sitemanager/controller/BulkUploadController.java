package com.eps.sitemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.eps.sitemanager.services.BulkUploadService;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/bulk-upload")
public class BulkUploadController {

	@Value("${template.filepath}")
	private String templateFilePath;

	@Autowired
	private final BulkUploadService bulkUploadService;

	public BulkUploadController(BulkUploadService bulkUploadService) {
		this.bulkUploadService = bulkUploadService;
	}

	@PostMapping("/upload-mastertable")
	public ResponseEntity<?> processMastertable(@RequestParam("file") MultipartFile file) {
		try {
			// Validate input file
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body("File is empty");
			}

			// Process the bulk upload and get the output file
			File outputFile = bulkUploadService.processBulkUpload(file);

			// Create a Path object from the File
			Path path = Paths.get(outputFile.getAbsolutePath());

			// Create a Resource from the Path
			Resource resource = new UrlResource(path.toUri());

			// Check if the file exists and is readable
			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to read the output file");
			}

			// Prepare the response with the file as an attachment
			return ResponseEntity.ok()
					.contentType(MediaType
							.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getName() + "\"")
					.body(resource);

		} catch (IllegalArgumentException e) {
			// Handle validation errors
			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {
			// Log the full error for server-side tracking

			// Return a generic error message to the client
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing master table: " + e.getMessage());
		}
	}

	@GetMapping("/download-template")
	public ResponseEntity<Resource> downloadTemplate() {
		try {
			

			// Convert file path to Path
			Path filePath = Paths.get(templateFilePath).normalize();
			System.out.println("Resolved template file path: " + filePath.toAbsolutePath());
			// Create UrlResource
			Resource resource = new UrlResource(filePath.toUri());

			// Check if file exists and is readable
			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}

			// Set content disposition to force download
			return ResponseEntity.ok()
					.contentType(MediaType
							.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"BulkUploadTemplate.xlsx\"")
					.body(resource);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
