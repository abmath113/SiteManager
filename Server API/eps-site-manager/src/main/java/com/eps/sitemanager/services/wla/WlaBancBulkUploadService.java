package com.eps.sitemanager.services.wla;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.dto.WlaBancDTO;
import com.eps.sitemanager.model.wla.WlaBancMaster;

@Service
public class WlaBancBulkUploadService {
	
	@Autowired
	private WlaMasterService wlaMasterService;
	
	
	private static final String TEMP_DIR_PATH = "D:\\EpsSiteManger\\temp\\bulk-upload-wla";
	private static final List<String> VALID_COLUMNS = List.of(
			
		    "Site Type", "Site Subtype", "Site Status", "EPS Site Code", "Old ATM ID", "ATM ID", "Location",
		    "District", "Address", "State", "City", "Pin Code", "Location Type", "Location Class", "Latitude",
		    
		    "Longitude", "Landlord Name", "Landlord Number", "CM Name", "CM Contact Number", "RM Name",
		    "RM Contact Number", "State Head Name", "State Head Number", "Master Franchise Name",
		    
		    "Franchise Contact Number", "Franchise Mail id", "TIS Category", "TIS Vendor Name","TIS Rate","TIS DO Status",
		    "TIS Order Date", "TIS Start Date", "TIS End Date", "TIS Status", "AC Make", "AC Capacity",
		    
		    "AC Status", "UPS Vendor", "UPS Order Date", "UPS Back Up Capacity Hrs", "No.Of Batteries",
		    "UPS Delivery Date", "UPS Installation Date", "UPS Installation Status", "IT/Servo Stabilizer",
		    
		    "VSAT Type", "VSAT Vendor/LINK Vendor", "VSAT Order Date", "VSAT Delivery Date", "VSAT ID",
		    "VSAT Installation Status", "VSAT Commissioning date", "Hugs Subnet", "Site Branding Vendor",
		    
		    "Site Branding Type", "Site Branding Order Date", "Signage / Lollypop Installation Date",
		    "Signage / Lollypop Installation Status", "ATM Branding Vendor", "ATM Branding Order Date",
		    
		    "ATM Branding Status", "ATM Branding Complete Date", "E-Surveillance Vendor Name",
		    "E-Surveillance Order Date", "E-Surveillance Delivery Date", "E-Surveillance Status",
		    
		    "E-Surveillance Commissioning Date", "ATM Vendor", "ATM Model", "ATM Order Date",
		    "ATM Delivery Date", "ATM Machine Serial No", "S&G lock No", "ATM Machine Status",
		    
		    "Tech Live Date", "Cash Live Date", "EJ Docket Number", "TSS Docket No", "Lan IP Address",
		    "ATM IP", "Subnet Mask", "TLS Domain Name", "TCP Port No.", "Switch Destination IP",
		    "Loading Type", "CRA Name", "New Loading Type", "Final Remarks"
		);

	private static final String XLSX_EXTENSION = ".xlsx";
	
	public File processWlaBulkUpload(MultipartFile file) throws Exception {
	    if (file.isEmpty()) {
	        throw new IllegalArgumentException("Please select a file to upload");
	    }

	    String fileName = file.getOriginalFilename();
	    
	    if (fileName == null || !fileName.toLowerCase().endsWith(XLSX_EXTENSION)) {
	        throw new IllegalArgumentException("Please upload an Excel file (xlsx)");
	    }
	    
	    File tempDir = new File(TEMP_DIR_PATH);
	    if (!tempDir.exists() && !tempDir.mkdirs()) {
	        throw new IOException("Failed to create temporary directory");
	    }

	    // Generate filenames
	    File inputFile = new File(tempDir, fileName + "-input" + XLSX_EXTENSION);
	    File outputFile = new File(tempDir, fileName + "-output" + XLSX_EXTENSION);
	    file.transferTo(inputFile);

	    // Validate column headers in the Excel file
	    validateColumns(inputFile);

	    // Read rows from the Excel file
	    List<RowProcessResult> processResults;
	    
	    // Opens the uploaded excel file as stream
	    try (FileInputStream fis = new FileInputStream(inputFile);
	    	
	    	// Wraps the stream into ReadableWorkbook object
	         ReadableWorkbook workbookRead = new ReadableWorkbook(fis)) {

	    
	        Sheet readSheet = workbookRead.getFirstSheet();
	        if (readSheet == null) {
	            throw new IllegalStateException("Excel file contains no sheets");
	        }

	        // Reads all the rows from the sheet into a list
	        List<Row> readRows = readSheet.read();
	        if (readRows == null || readRows.isEmpty()) {
	            throw new IllegalStateException("Excel file contains no data");
	        }

	        // Process rows and get results
	        processResults = processRowsWithTracking(readRows);

	        // Generate output Excel file
	        generateOutputExcel(inputFile, outputFile, processResults);
	    } catch (IOException e) {
	        throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
	    }

	    return outputFile;
	}
	
	private void generateOutputExcel(File inputFile, File outputFile, List<RowProcessResult> processResults)
			throws Exception {

		try (FileInputStream fis = new FileInputStream(inputFile);
				ReadableWorkbook readableWorkbook = new ReadableWorkbook(fis);
				FileOutputStream fos = new FileOutputStream(outputFile);
				Workbook workbook = new Workbook(fos, "BulkUpload", "1.0")) {

			// Create a new worksheet
			Worksheet worksheet = workbook.newWorksheet("Results");

			// Read input rows
			Sheet readSheet = readableWorkbook.getFirstSheet();
			List<Row> readRows = readSheet.read();

			// Write header row with original columns plus status column
			Row headerRow = readRows.get(0);
			processHeaderRow(worksheet, headerRow);

			// Write data rows with status
			for (int i = 0; i < processResults.size(); i++) {
				RowProcessResult result = processResults.get(i);
				Row originalRow = result.getRow();

				// Copy original row data
				for (int j = 0; j < VALID_COLUMNS.size(); j++) {
					worksheet.value(i + 1, j, originalRow.getCellRawValue(j).orElse(""));
				}

				// Add status column
				worksheet.value(i + 1, VALID_COLUMNS.size(), result.getStatus());

				// Apply color to status column based on result
				if ("green".equals(result.getStatusColor())) {
					worksheet.style(i + 1, VALID_COLUMNS.size()).fillColor("90EE90").set(); // Light green
				} else if ("red".equals(result.getStatusColor())) {
					worksheet.style(i + 1, VALID_COLUMNS.size()).fillColor("FF6347").set(); // Tomato red
				}
			}
		}
	}

	// Processes rows from an Excel file while tracking success or failure for each row.
		public List<RowProcessResult> processRowsWithTracking(List<Row> rows) {
			
			List<RowProcessResult> processResults = new ArrayList<>();

			// Traverse through each row in the Excel
			rows.stream().skip(1) // Skip the first row (header row)
					.forEach(row -> {
						RowProcessResult result = new RowProcessResult(row);
						try {
					

							// Process SiteMaster
							WlaBancMaster wlaBancMaster = processWlaBancMaster(row);
							
							result.setStatus("Success");
							result.setStatusColor("green");
						} catch (Exception e) {
							// Log the error and record the failure
							result.setStatus("Failure: " + e.getMessage());
							result.setStatusColor("red");
						}
						processResults.add(result);
					});

			return processResults;
		}
		
		private void processHeaderRow(Worksheet writeSheet, Row headerRow) throws Exception {

			for (int i = 0; i < VALID_COLUMNS.size(); i++) {
				writeSheet.value(0, i, headerRow.getCellRawValue(i).orElse(""));
				writeSheet.style(0, i).fillColor("cdf2fa").horizontalAlignment("center").bold().set();
			}
			writeSheet.value(0, VALID_COLUMNS.size(), "Status");
			writeSheet.style(0, VALID_COLUMNS.size()).fillColor("cdf2fa").horizontalAlignment("center").bold().set();
		}
		
		public WlaBancMaster processWlaBancMaster(Row row) {
			
		    WlaBancDTO wlaBancDTO = new WlaBancDTO();

		    wlaBancDTO.setSiteType(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Type")).orElse("").trim());
		    wlaBancDTO.setSiteSubtype(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Subtype")).orElse("").trim());
		    wlaBancDTO.setSiteStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Status")).orElse("").trim());
		    wlaBancDTO.setEpsSiteCode(row.getCellRawValue(VALID_COLUMNS.indexOf("EPS Site Code")).orElse("").trim());
		    wlaBancDTO.setOldAtmId(row.getCellRawValue(VALID_COLUMNS.indexOf("Old ATM ID")).orElse("").trim());
		    wlaBancDTO.setAtmId(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM ID")).orElse("").trim());
		    wlaBancDTO.setLocationName(row.getCellRawValue(VALID_COLUMNS.indexOf("Location")).orElse("").trim());
		    wlaBancDTO.setDistrict(row.getCellRawValue(VALID_COLUMNS.indexOf("District")).orElse("").trim());
		    wlaBancDTO.setAddress(row.getCellRawValue(VALID_COLUMNS.indexOf("Address")).orElse("").trim());
		    wlaBancDTO.setState(row.getCellRawValue(VALID_COLUMNS.indexOf("State")).orElse("").trim());
		    wlaBancDTO.setCity(row.getCellRawValue(VALID_COLUMNS.indexOf("City")).orElse("").trim());
		    wlaBancDTO.setPinCode(row.getCellRawValue(VALID_COLUMNS.indexOf("Pin Code")).orElse("").trim());
		    wlaBancDTO.setLocationType(row.getCellRawValue(VALID_COLUMNS.indexOf("Location Type")).orElse("").trim());
		    wlaBancDTO.setLocationClass(row.getCellRawValue(VALID_COLUMNS.indexOf("Location Class")).orElse("").trim());
		    wlaBancDTO.setLatitude(row.getCellRawValue(VALID_COLUMNS.indexOf("Latitude")).orElse("").trim());
		    wlaBancDTO.setLongitude(row.getCellRawValue(VALID_COLUMNS.indexOf("Longitude")).orElse("").trim());
		    wlaBancDTO.setLandlordName(row.getCellRawValue(VALID_COLUMNS.indexOf("Landlord Name")).orElse("").trim());
		    wlaBancDTO.setLandlordNumber(row.getCellRawValue(VALID_COLUMNS.indexOf("Landlord Number")).orElse("").trim());
		    wlaBancDTO.setCmName(row.getCellRawValue(VALID_COLUMNS.indexOf("CM Name")).orElse("").trim());
		    wlaBancDTO.setCmContactNumber(row.getCellRawValue(VALID_COLUMNS.indexOf("CM Contact Number")).orElse("").trim());
		    wlaBancDTO.setRmName(row.getCellRawValue(VALID_COLUMNS.indexOf("RM Name")).orElse("").trim());
		    wlaBancDTO.setRmContactNumber(row.getCellRawValue(VALID_COLUMNS.indexOf("RM Contact Number")).orElse("").trim());
		    wlaBancDTO.setStateHeadName(row.getCellRawValue(VALID_COLUMNS.indexOf("State Head Name")).orElse("").trim());
		    wlaBancDTO.setStateHeadNumber(row.getCellRawValue(VALID_COLUMNS.indexOf("State Head Number")).orElse("").trim());
		    wlaBancDTO.setMasterFranchiseName(row.getCellRawValue(VALID_COLUMNS.indexOf("Master Franchise Name")).orElse("").trim());
		    wlaBancDTO.setFranchiseContactNumber(row.getCellRawValue(VALID_COLUMNS.indexOf("Franchise Contact Number")).orElse("").trim());
		    wlaBancDTO.setFranchiseMailId(row.getCellRawValue(VALID_COLUMNS.indexOf("Franchise Mail id")).orElse("").trim());
		    wlaBancDTO.setTisCategory(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS Category")).orElse("").trim());
		    wlaBancDTO.setTisVendorName(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS Vendor Name")).orElse("").trim());
		    wlaBancDTO.setTisRate(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS Rate")).orElse("").trim());
		    wlaBancDTO.setTisDoStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS DO Status")).orElse("").trim());
		    wlaBancDTO.setTisOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS Order Date")).orElse("").trim());
		    wlaBancDTO.setTisStartDate(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS Start Date ")).orElse("").trim());
		    wlaBancDTO.setTisEndDate(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS End Date")).orElse("").trim());
		    wlaBancDTO.setTisStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("TIS Status")).orElse("").trim());
		    wlaBancDTO.setAcMake(row.getCellRawValue(VALID_COLUMNS.indexOf("AC Make")).orElse("").trim());
		    wlaBancDTO.setAcCapacity(row.getCellRawValue(VALID_COLUMNS.indexOf("AC Capacity")).orElse("").trim());
		    wlaBancDTO.setAcStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("AC Status")).orElse("").trim());
		    wlaBancDTO.setUpsVendor(row.getCellRawValue(VALID_COLUMNS.indexOf("UPS Vendor")).orElse("").trim());
		    wlaBancDTO.setUpsOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("UPS Order Date")).orElse("").trim());
		    wlaBancDTO.setUpsBackupCapacityHrs(row.getCellRawValue(VALID_COLUMNS.indexOf("UPS Back Up Capacity Hrs")).orElse("").trim());
		    wlaBancDTO.setNoOfBatteries(row.getCellRawValue(VALID_COLUMNS.indexOf("No.Of Batteries")).orElse("").trim());
		    wlaBancDTO.setUpsDeliveryDate(row.getCellRawValue(VALID_COLUMNS.indexOf("UPS Delivery Date")).orElse("").trim());
		    wlaBancDTO.setUpsInstallationDate(row.getCellRawValue(VALID_COLUMNS.indexOf("UPS Installation Date")).orElse("").trim());
		    wlaBancDTO.setUpsInstallationStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("UPS Installation Status")).orElse("").trim());
		    wlaBancDTO.setItServoStabilizer(row.getCellRawValue(VALID_COLUMNS.indexOf("IT/Servo Stabilizer")).orElse("").trim());
		    wlaBancDTO.setVsatType(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT Type")).orElse("").trim());
		    wlaBancDTO.setVsatVendor(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT Vendor/LINK Vendor")).orElse("").trim());
		    wlaBancDTO.setVsatOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT Order Date")).orElse("").trim());
		    wlaBancDTO.setVsatDeliveryDate(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT Delivery Date ")).orElse("").trim());
		    wlaBancDTO.setVsatId(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT ID")).orElse("").trim());
		    wlaBancDTO.setVsatInstallationStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT Installation Status")).orElse("").trim());
		    wlaBancDTO.setVsatCommissioningDate(row.getCellRawValue(VALID_COLUMNS.indexOf("VSAT Commissioning date")).orElse("").trim());
		    wlaBancDTO.setHugsSubnet(row.getCellRawValue(VALID_COLUMNS.indexOf("Hugs Subnet")).orElse("").trim());
		    wlaBancDTO.setSiteBrandingVendor(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Branding Vendor ")).orElse("").trim());
		    wlaBancDTO.setSiteBrandingType(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Branding Type")).orElse("").trim());
		    wlaBancDTO.setSiteBrandingOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Branding Order Date")).orElse("").trim());
		    wlaBancDTO.setSignageInstallationDate(row.getCellRawValue(VALID_COLUMNS.indexOf("Signage / Lollypop Installation Date")).orElse("").trim());
		    wlaBancDTO.setSignageInstallationStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("Signage / Lollypop Installation Status")).orElse("").trim());
		    wlaBancDTO.setAtmBrandingVendor(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Branding Vendor")).orElse("").trim());
		    wlaBancDTO.setAtmBrandingOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Branding Order Date")).orElse("").trim());
		    wlaBancDTO.setAtmBrandingStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Branding Status")).orElse("").trim());
		    wlaBancDTO.setAtmBrandingCompleteDate(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Branding Complete Date")).orElse("").trim());
		    wlaBancDTO.setESurveillanceVendorName(row.getCellRawValue(VALID_COLUMNS.indexOf("E-Surveillance Vendor Name")).orElse("").trim());
		    wlaBancDTO.setESurveillanceOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("E-Surveillance Order Date")).orElse("").trim());
		    wlaBancDTO.setESurveillanceDeliveryDate(row.getCellRawValue(VALID_COLUMNS.indexOf("E-Surveillance Delivery Date")).orElse("").trim());
		    wlaBancDTO.setESurveillanceStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("E-Surveillance Status")).orElse("").trim());
		    wlaBancDTO.setESurveillanceCommissioningDate(row.getCellRawValue(VALID_COLUMNS.indexOf("E-Surveillance Commissioning Date")).orElse("").trim());
		    wlaBancDTO.setAtmVendor(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Vendor")).orElse("").trim());
		    wlaBancDTO.setAtmModel(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Model")).orElse("").trim());
		    wlaBancDTO.setAtmOrderDate(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Order Date")).orElse("").trim());
		    wlaBancDTO.setAtmDeliveryDate(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Delivery Date")).orElse("").trim());
		    wlaBancDTO.setAtmMachineSerialNo(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Machine Serial No")).orElse("").trim());
		    wlaBancDTO.setSAndGLockNo(row.getCellRawValue(VALID_COLUMNS.indexOf("S&G lock No")).orElse("").trim());
		    wlaBancDTO.setAtmMachineStatus(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM Machine Status")).orElse("").trim());
		    wlaBancDTO.setTechLiveDate(row.getCellRawValue(VALID_COLUMNS.indexOf("Tech Live Date")).orElse("").trim());
		    wlaBancDTO.setCashLiveDate(row.getCellRawValue(VALID_COLUMNS.indexOf("Cash Live Date")).orElse("").trim());
		    wlaBancDTO.setEjDocketNumber(row.getCellRawValue(VALID_COLUMNS.indexOf("EJ Docket Number")).orElse("").trim());
		    wlaBancDTO.setTssDocketNo(row.getCellRawValue(VALID_COLUMNS.indexOf("TSS Docket No")).orElse("").trim());
		    wlaBancDTO.setLanIpAddress(row.getCellRawValue(VALID_COLUMNS.indexOf("Lan IP Address")).orElse("").trim());
		    wlaBancDTO.setAtmIp(row.getCellRawValue(VALID_COLUMNS.indexOf("ATM IP")).orElse("").trim());
		    wlaBancDTO.setSubnetMask(row.getCellRawValue(VALID_COLUMNS.indexOf("Subnet Mask")).orElse("").trim());
		    wlaBancDTO.setTlsDomainName(row.getCellRawValue(VALID_COLUMNS.indexOf("TLS Domain Name")).orElse("").trim());
		    wlaBancDTO.setTcpPortNo(row.getCellRawValue(VALID_COLUMNS.indexOf("TCP Port No.")).orElse("").trim());
		    wlaBancDTO.setSwitchDestinationIp1(row.getCellRawValue(VALID_COLUMNS.indexOf("Switch Destination IP")).orElse("").trim());
		    wlaBancDTO.setLoadingType(row.getCellRawValue(VALID_COLUMNS.indexOf("Loading Type")).orElse("").trim());
		    wlaBancDTO.setCraName(row.getCellRawValue(VALID_COLUMNS.indexOf("CRA Name")).orElse("").trim());
		    wlaBancDTO.setNewLoadingType(row.getCellRawValue(VALID_COLUMNS.indexOf("New Loading Type")).orElse("").trim());
		    wlaBancDTO.setFinalRemarks(row.getCellRawValue(VALID_COLUMNS.indexOf("Final Remarks")).orElse("").trim());

		    return wlaMasterService.saveWlaBancMaster(wlaBancDTO);
		}


	// New inner class to track row processing results
		private static class RowProcessResult {

			private final Row row;
			private String status;
			private String statusColor;

			public RowProcessResult(Row row) {
				this.row = row;
			}

			public Row getRow() {
				return row;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			public String getStatusColor() {
				return statusColor;
			}

			public void setStatusColor(String statusColor) {
				this.statusColor = statusColor;
			}
		}
	
	private void validateColumns(File excelInputFile) throws Exception {
		try (FileInputStream fis = new FileInputStream(excelInputFile);
				ReadableWorkbook workbookRead = new ReadableWorkbook(fis)) {

			Sheet readSheet = workbookRead.getFirstSheet();
			if (readSheet == null) {
				throw new RuntimeException("Excel file contains no sheets");
			}

			List<Row> readRows = readSheet.read();
			if (readRows == null || readRows.isEmpty()) {
				throw new RuntimeException("Excel file contains no data");
			}

			// Validate the header row
			Row headerRow = readRows.get(0);
			for (int i = 0; i < VALID_COLUMNS.size(); i++) {
				String columnHeader = headerRow.getCellRawValue(i).orElse("").trim();
				if (!VALID_COLUMNS.get(i).equalsIgnoreCase(columnHeader)) {
					throw new RuntimeException(
							"Invalid Excel format. Missing or incorrect column: " + VALID_COLUMNS.get(i));
				}
			}
		}
	}
	
		

}
	
	
	