package com.eps.sitemanager.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.configurations.error.ParameterConstraintException;
import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.master.ChannelManagerDTO;
import com.eps.sitemanager.dto.master.LandlordMasterDTO;
import com.eps.sitemanager.dto.master.RentAgreementMasterDTO;
import com.eps.sitemanager.dto.master.SiteMasterDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.LandlordMaster;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.request.validator.master.LandlordMasterControllerRequestValidator;
import com.eps.sitemanager.request.validator.master.RentAgreementMasterControllerRequestValidator;
import com.eps.sitemanager.request.validator.master.SiteMasterControllerRequestValidator;
import com.eps.sitemanager.services.master.BankMasterService;
import com.eps.sitemanager.services.master.ChannelManagerService;
import com.eps.sitemanager.services.master.LandlordMasterService;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.services.master.SiteMasterService;

@Service
public class BulkUploadServiceImpl implements BulkUploadService {

	private final ChannelManagerService channelManagerService;
	private final BankMasterService bankMasterService;
	private final SiteMasterService siteMasterService;
	private final LandlordMasterService landlordMasterService;
	private final RentAgreementMasterService rentAgreementMasterService;
	private SiteMasterControllerRequestValidator siteReqValidator;
	private LandlordMasterControllerRequestValidator landlordMasterControllerRequestValidator;
	private RentAgreementMasterControllerRequestValidator rentAgreementMasterControllerRequestValidator;
	
	@Autowired
	public BulkUploadServiceImpl(ChannelManagerService channelManagerService, SiteMasterService siteMasterService,
			LandlordMasterService landlordMasterService, RentAgreementMasterService rentAgreementMasterService,
			BankMasterService bankMasterService, SiteMasterControllerRequestValidator siteReqValidator,
			LandlordMasterControllerRequestValidator landlordMasterControllerRequestValidator,
			RentAgreementMasterControllerRequestValidator rentAgreementMasterControllerRequestValidator) {
		this.channelManagerService = channelManagerService;
		this.siteMasterService = siteMasterService;
		this.landlordMasterService = landlordMasterService;
		this.rentAgreementMasterService = rentAgreementMasterService;
		this.bankMasterService = bankMasterService;
		this.siteReqValidator = siteReqValidator;
		this.landlordMasterControllerRequestValidator = landlordMasterControllerRequestValidator;
		this.rentAgreementMasterControllerRequestValidator = rentAgreementMasterControllerRequestValidator;
	}

	private static final String TEMP_DIR_PATH = "D:\\EpsSiteManger\\temp\\bulk-upload";
	private static final String XLSX_EXTENSION = ".xlsx";
	private static final List<String> VALID_COLUMNS = List.of("SiteCode", "AtmId", "Channel Manager", "Site Area",
			"Site Status", "Bank", "Site Address",// sitemaster

			"LL Name", "LL Account No.", "LL Mob No.", "Beneficiary Name", "IFSC", "Pan", "Aadhar", "LL Status",
			"LL Address", // landlordmaster

			"Agreement Date", "Rent Pay Start Date", "Lease End Date","Rent", "Deposit", "Solar", "Pay Interval",
			"Agreement Status", "Escalation %", "Escalation After(Months)","Project","State","District", "Termination Date");// rentagreementmaster

	// Parses a string to an integer safely, throwing an error if the value is empty
	// or invalid.
	private int parseIntSafely(String value, String fieldName) {
		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(fieldName + " cannot be empty and must be a valid number");
		}
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			throw new RuntimeException(fieldName + " must be a valid number, received: '" + value + "'");
		}
	}

	@Override
	// Processes bulk file uploads, validates the file, reads data, and generates
	// output Excel.
	public File processBulkUpload(MultipartFile file) throws Exception {
		// Validate file is not empty
		if (file.isEmpty()) {
			throw new IllegalArgumentException("Please select a file to upload");
		}
		// Validate file name and extension
		String filename = file.getOriginalFilename();
		if (filename == null || !filename.toLowerCase().endsWith(XLSX_EXTENSION)) {
			throw new IllegalArgumentException("Please upload an Excel file (xlsx)");
		}
		try {

			File tempDir = new File(TEMP_DIR_PATH);
			if (!tempDir.exists() && !tempDir.mkdirs()) {
				throw new IOException("Failed to create temporary directory");
			}

			// Generate filenames
			File inputFile = new File(tempDir, filename + "-input" + XLSX_EXTENSION);
			File outputFile = new File(tempDir, filename + "-output" + XLSX_EXTENSION);
			file.transferTo(inputFile);

			// Validate column headers in the Excel file
			validateColumns(inputFile);

			// Read rows from the Excel file
			List<RowProcessResult> processResults;
			try (FileInputStream fis = new FileInputStream(inputFile);
					ReadableWorkbook workbookRead = new ReadableWorkbook(fis)) {

				Sheet readSheet = workbookRead.getFirstSheet();
				if (readSheet == null) {
					throw new IllegalStateException("Excel file contains no sheets");
				}

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

		} catch (IOException e) {
			throw new RuntimeException("Error processing file: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getMessage(), e);
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
						// Extract Channel Manager information from the row
						String channelManagerName = row.getCellRawValue(VALID_COLUMNS.indexOf("Channel Manager"))
								.orElse("").trim();
						String bankCode = row.getCellRawValue(VALID_COLUMNS.indexOf("Bank")).orElse("").trim();
						ChannelManagerMaster channelManager = processChannelManager(channelManagerName);
						BankMaster bankMaster = processBankMaster(bankCode);

						// Process SiteMaster
						SiteMaster siteMaster = processSite(row, channelManager, bankMaster);

						// Process LandlordMaster
						LandlordMaster landlordMaster = processLandlord(row);

						// Process RentAgreementMaster
						RentAgreementMaster rentAgreementMaster = processRentAgreement(row, siteMaster, landlordMaster);

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

	// Generates an output Excel file with processing results, including status and
	// color coding.
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
				ReadableWorkbook workbookRead = new ReadableWorkbook(fis)
						
						)
		{
			System.out.println("workbookRead"+workbookRead);

			Sheet readSheet = workbookRead.getFirstSheet();
			System.out.println("readSheet value is"+readSheet.toString());
			if (readSheet == null) {
				throw new RuntimeException("Excel file contains no sheets");
			}

			List<Row> readRows = readSheet.read();
			System.out.println("readRows value is"+readRows);
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

	public void processRows(List<Row> rows) {
		// Traverse through each row in the Excel
		rows.stream().skip(1) // Skip the first row (header row)
				.forEach(row -> {
					try {
						// Extract necessary data from the row for processing

						// Extract Channel Manager information from the row
						String channelManagerName = row.getCellRawValue(VALID_COLUMNS.indexOf("Channel Manager"))
								.orElse("").trim();
						ChannelManagerMaster channelManager = processChannelManager(channelManagerName);
						// Process BankMaster
						String Bank = row.getCellRawValue(VALID_COLUMNS.indexOf("Bank")).orElse("").trim();
						BankMaster bankMaster = processBankMaster(Bank);

						// Process SiteMaster
						SiteMaster siteMaster = processSite(row, channelManager, bankMaster);

						// Process LandlordMaster
						LandlordMaster landlordMaster = processLandlord(row);

						// Process RentAgreementMaster
						RentAgreementMaster rentAgreementMaster = processRentAgreement(row, siteMaster, landlordMaster);

						// Optionally log or save the successfully processed entries for tracking
						System.out.println("Processed Site: " + siteMaster.getSiteCode() + ", Landlord: "
								+ landlordMaster.getName() + ", Rent Agreement: "
								+ rentAgreementMaster.getAgreementDate());
					} catch (Exception e) {
						// Log the error and continue processing the next row
						System.err.println("Error processing row: " + e.getMessage());
					}
				});

	}

	private ChannelManagerMaster processChannelManager(String channelManagerName) {
		if (channelManagerName.isEmpty()) {
			throw new RuntimeException("Channel Manager name cannot be empty");
		}

		return channelManagerService.getChannelManagerMasterByName(channelManagerName).orElseGet(() -> {

			ChannelManagerDTO newChannelManagerDTO = new ChannelManagerDTO();

			newChannelManagerDTO.setChannelManagerName(channelManagerName);
			return channelManagerService.saveChannelManagerMaster(newChannelManagerDTO); // Save and return the new
																							// object
		});
	}

	private BankMaster processBankMaster(String BankCode) {
		if (BankCode.isBlank()) {
			throw new RuntimeException("Bank Code cannot be empty");
		}
		return bankMasterService.getBankMasterByBankCode(BankCode).orElseGet(() -> {
			throw new RuntimeException("Bank Code not found");
		});
	}

	private SiteMaster processSite(Row row, ChannelManagerMaster channelManager, BankMaster bankMaster) {

		String siteCode = row.getCellRawValue(VALID_COLUMNS.indexOf("SiteCode")).orElse("").trim();

		if (siteCode.isEmpty()) {
			throw new RuntimeException("SiteCode cannot be empty");
		}

		if (siteMasterService.checkSiteAlreadyExists(siteCode)) {
			return siteMasterService.getSiteMasterBySiteCode(siteCode)
					.orElseThrow(() -> new RuntimeException("SiteCode exists but could not retrieve the object"));
		}

		SiteMasterDTO siteMasterDTO = new SiteMasterDTO();
		siteMasterDTO.setSitecode(siteCode);
		siteMasterDTO.setSiteatms(row.getCellRawValue(VALID_COLUMNS.indexOf("AtmId")).orElse("").trim());
		siteMasterDTO.setSiteArea(
				Integer.parseInt(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Area")).orElse("").trim()));
		siteMasterDTO.setChannelManagerId(channelManager.getChannelManagerId());
		siteMasterDTO.setSitestatus(
				"active".equalsIgnoreCase(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Status")).orElse("").trim()));
		siteMasterDTO.setBankId(bankMaster.getBankId());

		siteMasterDTO.setSiteaddress(row.getCellRawValue(VALID_COLUMNS.indexOf("Site Address")).orElse("").trim());
		siteMasterDTO.setProjectName(row.getCellRawValue(VALID_COLUMNS.indexOf("Project")).orElse("").trim());
		siteMasterDTO.setState(row.getCellRawValue(VALID_COLUMNS.indexOf("State")).orElse("").trim());
		siteMasterDTO.setDistrict(row.getCellRawValue(VALID_COLUMNS.indexOf("District")).orElse("").trim());
		// Perform Validation
		RequestValidaton reqValidation = siteReqValidator.validateSaveUpdateRequest(siteMasterDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}

		return siteMasterService.saveSiteMaster(siteMasterDTO, channelManager, bankMaster);
	}

	private LandlordMaster processLandlord(Row row) {

		String landlordName = row.getCellRawValue(VALID_COLUMNS.indexOf("LL Name")).orElse("").trim();

		if (landlordName.isEmpty()) {
			throw new RuntimeException("Landlord name cannot be empty");
		}

		List<LandlordMaster> landlordList = landlordMasterService.getLandlordMasterbyName(landlordName);

		if (!landlordList.isEmpty()) {
			return landlordList.get(0); // Return the first matched LandlordMaster object
		}

		LandlordMasterDTO landlordMasterDTO = new LandlordMasterDTO();
		landlordMasterDTO.setName(landlordName);
		landlordMasterDTO
				.setBeneficiaryName(row.getCellRawValue(VALID_COLUMNS.indexOf("Beneficiary Name")).orElse("").trim());
		landlordMasterDTO.setAccountNo(row.getCellRawValue(VALID_COLUMNS.indexOf("LL Account No.")).orElse("").trim());
		landlordMasterDTO.setMobileNo(row.getCellRawValue(VALID_COLUMNS.indexOf("LL Mob No.")).orElse("").trim());
		landlordMasterDTO.setIfscCode(row.getCellRawValue(VALID_COLUMNS.indexOf("IFSC")).orElse("").trim());
		landlordMasterDTO.setPan(row.getCellRawValue(VALID_COLUMNS.indexOf("Pan")).orElse("").trim());
		landlordMasterDTO.setAadharNo(row.getCellRawValue(VALID_COLUMNS.indexOf("Aadhar")).orElse("").trim());
		landlordMasterDTO.setStatus(
				"active".equalsIgnoreCase(row.getCellRawValue(VALID_COLUMNS.indexOf("LL Status")).orElse("").trim()));
		landlordMasterDTO.setAddress(row.getCellRawValue(VALID_COLUMNS.indexOf("LL Address")).orElse("").trim());

		// perform validation
		RequestValidaton reqValidation = landlordMasterControllerRequestValidator
				.validateSaveUpdateRequest(landlordMasterDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());

		}
		return landlordMasterService.saveLandlordMaster(landlordMasterDTO);
	}

	private RentAgreementMaster processRentAgreement(Row row, SiteMaster siteMaster, LandlordMaster landlordMaster) {

		// Validate required fields
		String agreementDate = row.getCellRawValue(VALID_COLUMNS.indexOf("Agreement Date")).orElse("").trim();

		String rentPayStartDate = row.getCellRawValue(VALID_COLUMNS.indexOf("Rent Pay Start Date")).orElse("").trim();
		String leaseEndDate = row.getCellRawValue(VALID_COLUMNS.indexOf("Lease End Date")).orElse("").trim();
		String rent = row.getCellRawValue(VALID_COLUMNS.indexOf("Rent")).orElse("").trim();
		String deposit = row.getCellRawValue(VALID_COLUMNS.indexOf("Deposit")).orElse("").trim();
		String solar = row.getCellRawValue(VALID_COLUMNS.indexOf("Solar")).orElse("").trim();
		String payInterval = row.getCellRawValue(VALID_COLUMNS.indexOf("Pay Interval")).orElse("").trim();
		String agreementStatus = row.getCellRawValue(VALID_COLUMNS.indexOf("Agreement Status")).orElse("").trim();
		String escalationPercent = row.getCellRawValue(VALID_COLUMNS.indexOf("Escalation %")).orElse("").trim();
		String escalationAfterMonths = row.getCellRawValue(VALID_COLUMNS.indexOf("Escalation After(Months)")).orElse("")
				.trim();
		String terminationDate = row.getCellRawValue(VALID_COLUMNS.indexOf("Termination Date")).orElse("").trim();

		if (agreementDate.isEmpty() || rentPayStartDate.isEmpty() || rent.isEmpty() || payInterval.isEmpty()
				|| agreementStatus.isEmpty()) {
			throw new RuntimeException("Mandatory Rent Agreement fields cannot be empty");
		}
		
		

		// Parse dates to LocalDate
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust the format to match the
																						// expected input
		LocalDate parsedAgreementDate;
		LocalDate parsedRentPayStartDate;
		LocalDate parsedLeaseEndDate;
		LocalDate parsedTerminationDate;
		try {
			// Check if the date is in numeric (Excel serial date) format
			if (agreementDate.matches("\\d+")) {
				long excelDate = Long.parseLong(agreementDate); // Convert the string to a long
				parsedAgreementDate = LocalDate.of(1900, 1, 1).plusDays(excelDate - 2); // Adjust for Excel's date
																						// system
			} else {
				parsedAgreementDate = LocalDate.parse(agreementDate, dateFormatter); // Parse as standard yyyy-MM-dd
			}

			if (rentPayStartDate.matches("\\d+")) {
				long excelDate = Long.parseLong(rentPayStartDate); // Convert the string to a long
				parsedRentPayStartDate = LocalDate.of(1900, 1, 1).plusDays(excelDate - 2); // Adjust for Excel's
																							// date system
			} else {
				parsedRentPayStartDate = LocalDate.parse(rentPayStartDate, dateFormatter); // Parse as
																							// standard
																							// yyyy-MM-dd
			}
			if (leaseEndDate.matches("\\d+")) {
				long excelDate = Long.parseLong(leaseEndDate); // Convert the string to a long
				parsedLeaseEndDate = LocalDate.of(1900, 1, 1).plusDays(excelDate - 2); // Adjust for Excel's
																						// date system
			} else {
				parsedLeaseEndDate = LocalDate.parse(leaseEndDate, dateFormatter); // Parse as
																					// standard
																					// yyyy-MM-dd
			}
			if (terminationDate.matches("\\d+")) {
				long excelDate = Long.parseLong(terminationDate); // Convert the string to a long
				parsedTerminationDate = LocalDate.of(1900, 1, 1).plusDays(excelDate - 2); // Adjust for Excel's date
																						// system
			} else {
				parsedTerminationDate = LocalDate.parse(terminationDate, dateFormatter); // Parse as standard yyyy-MM-dd
			}


		} catch (DateTimeParseException e) {
			throw new RuntimeException("Invalid date format. Expected format is yyyy-MM-dd");
		} catch (NumberFormatException e) {
			throw new RuntimeException("Invalid numeric date format in Excel date columns.");
		}

		// Convert payInterval to int (1 for monthly, 3 for quarterly, 6 for half-yearly, 0 for rent-free)
		int paymentInterval;
		switch (payInterval.toLowerCase().trim()) {
		case "monthly":
			paymentInterval = 1;
			break;
		case "quarterly":
			paymentInterval = 3;
			break;
		case "half-yearly":
			paymentInterval = 6;
			break;
		case "rent-free":
			paymentInterval = 0;
		default:
			throw new RuntimeException("Invalid pay interval. Expected 'monthly','quarterly','half-yearly','rent-free',");
		}
		
		boolean agreementStatusBool = "live".equalsIgnoreCase(agreementStatus.trim());

		
		// Check if rentpaystartdate is before agreementdate
		if (parsedAgreementDate.isAfter(parsedRentPayStartDate)) {
		    throw new RuntimeException("Agreement Date must be before or equal to Rent Pay Start Date");
		}

		// Check for duplicate agreements
		if (rentAgreementMasterService.isRentAgreementDuplicate(siteMaster, landlordMaster, parsedAgreementDate)) {
			throw new RuntimeException("Duplicate Rent Agreement detected for SiteMaster and LandlordMaster");
		}
		
		System.out.println("Checking agreementDate: " + parsedAgreementDate + ", rentPayStartDate: " + parsedRentPayStartDate);
		
	
		

		// Create DTO
		RentAgreementMasterDTO rentAgreementMasterDTO = new RentAgreementMasterDTO();

		rentAgreementMasterDTO.setAgreementDate(parsedAgreementDate);
		rentAgreementMasterDTO.setRentPayStartDate(parsedRentPayStartDate);
		rentAgreementMasterDTO.setAgreementEndDate(parsedLeaseEndDate);
		rentAgreementMasterDTO.setMonthlyRent(parseIntSafely(rent, "Rent"));
		rentAgreementMasterDTO.setDeposit(parseIntSafely(deposit, "Deposit"));
		rentAgreementMasterDTO.setSolarPanelRent(parseIntSafely(solar, "Solar"));
		rentAgreementMasterDTO.setEscalationPercent(parseIntSafely(escalationPercent, "Escalation %"));
		rentAgreementMasterDTO
				.setEscalationAfterMonths(parseIntSafely(escalationAfterMonths, "Escalation After (Months)"));
		rentAgreementMasterDTO.setPaymentInterval(paymentInterval);
		rentAgreementMasterDTO.setRentAgreementStatus(agreementStatusBool);
		rentAgreementMasterDTO.setTerminationDate(parsedTerminationDate);

//		// Perform Validation
		
		RequestValidaton reqValidation = rentAgreementMasterControllerRequestValidator
				.validateSaveUpdateRequest(rentAgreementMasterDTO);
		if (!reqValidation.isState()) {
			throw new ParameterConstraintException(reqValidation.getErrorMessage());
		}

		return rentAgreementMasterService.saveRentAgreementMaster(rentAgreementMasterDTO, siteMaster, landlordMaster);
	}

	private void processHeaderRow(Worksheet writeSheet, Row headerRow) throws Exception {

		for (int i = 0; i < VALID_COLUMNS.size(); i++) {
			writeSheet.value(0, i, headerRow.getCellRawValue(i).orElse(""));
			writeSheet.style(0, i).fillColor("cdf2fa").horizontalAlignment("center").bold().set();
		}
		writeSheet.value(0, VALID_COLUMNS.size(), "Status");
		writeSheet.style(0, VALID_COLUMNS.size()).fillColor("cdf2fa").horizontalAlignment("center").bold().set();
	}
}
