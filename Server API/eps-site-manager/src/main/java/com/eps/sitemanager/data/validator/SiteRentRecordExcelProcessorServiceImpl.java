package com.eps.sitemanager.data.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eps.sitemanager.dto.SiteRentRecordDTO;
import com.eps.sitemanager.model.SiteRentRecord;
import com.eps.sitemanager.model.master.RentAgreementMaster;
import com.eps.sitemanager.repository.SiteRentRecordRepository;
import com.eps.sitemanager.repository.master.RentAgreementMasterRepository;
import com.eps.sitemanager.services.master.RentAgreementMasterService;
import com.eps.sitemanager.services.master.SiteMasterService;

@Service

public class SiteRentRecordExcelProcessorServiceImpl implements SiteRentRecordExcelProcessorService {

	private final SiteMasterService siteMasterService;
	private final RentAgreementMasterService rentAgreementMasterService;
	private final SiteRentRecordRepository siteRentRecordRepository;
	private final RentAgreementMasterRepository rentAgreementMasterRepository;

	@Autowired
	public SiteRentRecordExcelProcessorServiceImpl(SiteMasterService siteMasterService,
			RentAgreementMasterService rentAgreementMasterService, SiteRentRecordRepository siteRentRecordRepository,
			RentAgreementMasterRepository rentAgreementMasterRepository) {
		this.siteMasterService = siteMasterService;
		this.rentAgreementMasterService = rentAgreementMasterService;
		this.siteRentRecordRepository = siteRentRecordRepository;
		this.rentAgreementMasterRepository = rentAgreementMasterRepository;
	}

	private static final String TEMP_DIR_PATH = "D:\\EpsSiteManger\\temp\\rent-records";
	private static final String XLSX_EXTENSION = ".xlsx";
	private static final List<String> VALID_COLUMNS = List.of(
		    "Site Code", "Paid Date", "Amount Paid", "Remarks", "UTR No", "Transaction Status", "Reason"
		);

	@Override
	public ResponseEntity<?> processExcelFile(MultipartFile file, String rentForMonth) {
	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("Please select a file to upload");
	    }

	    String filename = file.getOriginalFilename();
	    if (filename == null || !filename.toLowerCase().endsWith(XLSX_EXTENSION)) {
	        return ResponseEntity.badRequest().body("Please upload an Excel file (xlsx)");
	    }

	    try {
	        // Create temp directory
	        File tempDir = new File(TEMP_DIR_PATH);
	        if (!tempDir.exists() && !tempDir.mkdirs()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to create temporary directory");
	        }

	        // Generate filenames
	        File inputFile = new File(tempDir, filename + "-input" + XLSX_EXTENSION);
	        file.transferTo(inputFile);

	        // Validate column headers
	        validateColumns(inputFile);

	        // Process the file
	        File outputFile = new File(tempDir, filename + "-output" + XLSX_EXTENSION);
	        boolean processed = processSiteRentUploadedFile(inputFile, outputFile, rentForMonth);

	        if (!processed) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file");
	        }

	        Map<String, String> response = new HashMap<>();
	        response.put("fileName", outputFile.getName());
	        return ResponseEntity.ok(response);

	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error processing file: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}

	/**
	 * Processes an uploaded Excel file containing site rent records and generates a
	 * new Excel file with validation results. Reads data from the input file,
	 * validates each record, and writes the results to the output file with status
	 * indicators.
	 * 
	 * @param excelInputFile  The input Excel file to process
	 * @param excelOutputFile The output Excel file to generate
	 * @param rentForMonth    The month for which rent is being processed
	 * @return boolean indicating whether the file was processed successfully
	 */
	public boolean processSiteRentUploadedFile(File excelInputFile, File excelOutputFile, String rentForMonth) {
	    boolean fileProcessed = false;

	    try (FileInputStream fis = new FileInputStream(excelInputFile);
	         ReadableWorkbook workbookRead = new ReadableWorkbook(fis);
	         FileOutputStream fos = new FileOutputStream(excelOutputFile);
	         Workbook workbookWrite = new Workbook(fos, "SiteManager", "1.0")) {

	        Sheet readSheet = workbookRead.getFirstSheet();
	        if (readSheet == null) {
	            throw new RuntimeException("Excel file contains no sheets");
	        }

	        List<Row> readRows = readSheet.read();
	        if (readRows == null || readRows.isEmpty()) {
	            throw new RuntimeException("Excel file contains no data");
	        }

	        workbookWrite.setGlobalDefaultFont("Times New Roman", 11);
	        Worksheet writeSheet = workbookWrite.newWorksheet("Workbook Site Record");

	        // Process header row
	        processHeaderRow(writeSheet, readRows.get(0));

	        // Process all rows, including empty ones
	        
	        for (int r = 1; r < readRows.size(); r++) {
	            processDataRow(writeSheet, readRows.get(r), r, rentForMonth);
	        }
	        
	        fileProcessed = true;

	    } catch (Exception e) {
	        throw new RuntimeException("Error processing file: " + e.getMessage(), e);
	    }

	    return fileProcessed;
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
	                throw new RuntimeException("Invalid Excel format. Missing or incorrect column: " + VALID_COLUMNS.get(i));
	            }
	        }
	    }
	}

	/**
	 * Processes the header row of the Excel file and writes it to the output
	 * worksheet. Applies formatting to header cells including background color,
	 * alignment, and bold text.
	 * 
	 * @param writeSheet The worksheet to write to
	 * @param headerRow  The header row to process
	 * @throws Exception If there's an error processing the header
	 */
	private void processHeaderRow(Worksheet writeSheet, Row headerRow) throws Exception {
		for (int i = 0; i < 7; i++) {
			writeSheet.value(0, i, headerRow.getCellRawValue(i).orElse(""));
			writeSheet.style(0, i).fillColor("cdf2fa").horizontalAlignment("center").bold().set();
		}
		writeSheet.value(0, 7, "Status");
		writeSheet.style(0, 7).fillColor("cdf2fa").horizontalAlignment("center").bold().set();
	}

	/**
	 * Processes a data row from the input Excel file, validates the data, and
	 * writes results to the output worksheet. Includes validation status and error
	 * messages in the output.
	 * 
	 * @param writeSheet   The worksheet to write to
	 * @param currentRow   The current row being processed
	 * @param rowIndex     The index of the current row
	 * @param rentForMonth The month for which rent is being processed
	 * @throws Exception If there's an error processing the row
	 */
	private void processDataRow(Worksheet writeSheet, Row currentRow, int rowIndex, String rentForMonth)
	        throws Exception {
		DateTimeFormatter paidDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
	    // Get all values first
	    String siteCode = currentRow.getCellRawValue(0).orElse("").trim();
	    
	    
	    String paidDate = "";
		LocalDateTime paidDateL = null;
		try {
			paidDateL = currentRow.getCellAsDate(1).orElse(null);
			if (paidDateL != null) {
				paidDate = paidDateFormatter.format(paidDateL);
			}
		} catch (Exception e) {
			System.out.println(" Error : " + e.getMessage());
		}
	    
	    String amountPaid = currentRow.getCellRawValue(2).orElse("").trim();
	    String remarks = currentRow.getCellRawValue(3).orElse("").trim();
	    String utrNo = currentRow.getCellRawValue(4).orElse("").trim();
	    String transactionStatus = currentRow.getCellRawValue(5).orElse("").trim();
	    String reason = currentRow.getCellRawValue(6).orElse("").trim();
	    

	    System.out.println(paidDate);
	    // Write all values first to maintain the row structure
	    writeSheet.value(rowIndex, 0, siteCode);

		if (paidDateL == null) {
			writeSheet.value(rowIndex, 1, paidDate);
		} else {
			writeSheet.value(rowIndex, 1, paidDateL);
			writeSheet.style(rowIndex, 1).format("dd-mm-yyyy").horizontalAlignment("center").set();
		}
	    
	    writeSheet.value(rowIndex, 2, amountPaid);
	    writeSheet.value(rowIndex, 3, remarks);
	    writeSheet.value(rowIndex, 4, utrNo);
	    writeSheet.value(rowIndex, 5, transactionStatus);
	    writeSheet.value(rowIndex, 6, reason);

	    // Only proceed with validation if there's actual data
	    if (!siteCode.isEmpty()) {
	        StringBuilder errorBuilder = new StringBuilder();
	        try {
	            validateRow(siteCode, amountPaid, remarks, utrNo, transactionStatus, reason, rentForMonth, errorBuilder);
	            saveSiteRentRecord(siteCode, paidDate, amountPaid, remarks, utrNo, transactionStatus, reason);
	            
	            // If we get here, validation passed
	            writeSheet.value(rowIndex, 7, "Success");
	            writeSheet.style(rowIndex, 7).fillColor("32a852").horizontalAlignment("center").set();
	        } catch (Exception e) {
	            // Add any validation errors to the error builder
	            if (errorBuilder.length() > 0) {
	                errorBuilder.append("; ");
	            }
	            errorBuilder.append(e.getMessage());
	            
	            // Write error message
	            writeSheet.value(rowIndex, 7, errorBuilder.toString());
	            writeSheet.style(rowIndex, 7).fillColor("ff6b6b").horizontalAlignment("center").set();
	        }
	    } else {
	        // Handle empty rows by leaving status column blank
	        writeSheet.value(rowIndex, 7, "");
	    }
	}


	/**
	 * Validates a row of site rent data against business rules and database
	 * records. Checks site code existence, transaction status, rent amount, and
	 * other validation rules.
	 * 
	 * @param siteCode          Site identifier
	 * @param amountPaid        Amount paid for rent
	 * @param remarks           Additional comments
	 * @param utrNo             UTR number for transaction
	 * @param transactionStatus Status of the transaction
	 * @param reason            Reason for incomplete transaction
	 * @param rentForMonth      Month for which rent is being processed
	 * @param errorBuilder      StringBuilder to collect validation errors
	 */
	private void validateRow(String siteCode, String amountPaid, String remarks, String utrNo, String transactionStatus,
			String reason, String rentForMonth, StringBuilder errorBuilder) {
		try {
			if (!siteMasterService.checkSiteCodeExists(siteCode)) {
				appendError(errorBuilder, "Site " + siteCode + " is not present in DB");
			}

			int agreementId = rentAgreementMasterService.getAgreementIdBySiteCode(siteCode);
			Optional<SiteRentRecord> siteRentRecords = siteRentRecordRepository
					.findOneByAgreementIdAndRentMonth(agreementId, rentForMonth);

			// Check if site either has a UTR or has an incomplete transaction
			if (siteRentRecords.isPresent()
					&& ((siteRentRecords.get().getUtrNo() != null && !siteRentRecords.get().getUtrNo().trim().isEmpty())
							|| !siteRentRecords.get().isTransactionStatus())) {
				appendError(errorBuilder,
						"Site " + siteCode + " already has a transaction record. Cannot add new transaction."+ "Utrno of old transaction : "+siteRentRecords.get().getUtrNo());
				return;
			}

			if (siteRentRecords.isPresent()) {
				validateRentAmount(siteRentRecords.get(), amountPaid, remarks, errorBuilder);
			}

			validateTransactionStatus(transactionStatus, utrNo, reason, errorBuilder);

		} catch (NumberFormatException e) {
			appendError(errorBuilder, "Invalid amount format: " + amountPaid);
		} catch (Exception e) {
			appendError(errorBuilder, "Error processing record: " + e.getMessage());
		}
	}

	private void validateRentAmount(SiteRentRecord record, String amountPaid, String remarks,
			StringBuilder errorBuilder) {
		int expectedAmount = record.getGeneratedRent();
		int amountPaidInt = Integer.parseInt(amountPaid);

		if (amountPaidInt != expectedAmount && remarks.trim().isEmpty()) {
			appendError(errorBuilder,
					"Remarks required for amount mismatch: Expected " + expectedAmount + ", Paid " + amountPaid);
		}
	}

	private void validateTransactionStatus(String transactionStatus, String utrNo, String reason,
			StringBuilder errorBuilder) {
		if ("completed transaction".equalsIgnoreCase(transactionStatus.trim())) {
			if (utrNo.trim().isEmpty()) {
				appendError(errorBuilder, "UTR number required for completed transactions");
			}
		} else if (!transactionStatus.trim().isEmpty() && reason.trim().isEmpty()) {
			appendError(errorBuilder, "Reason required for incomplete transaction");
		}
	}

	private void appendError(StringBuilder errorBuilder, String message) {
		if (errorBuilder.length() > 0)
			errorBuilder.append("; ");
		errorBuilder.append(message);
	}

	/**
	 * Saves or updates a site rent record in the database. Creates a new record if
	 * one doesn't exist, otherwise updates the existing record.
	 * 
	 * @param siteCode          Site identifier
	 * @param date              Payment date
	 * @param amountPaid        Amount paid for rent
	 * @param remarks           Additional comments
	 * @param utrNo             UTR number for transaction
	 * @param transactionStatus Status of the transaction
	 * @param reason            Reason for incomplete transaction
	 */
	private void saveSiteRentRecord(String siteCode, String date, String amountPaid, String remarks, String utrNo,
			String transactionStatus, String reason) {
		// First check if the record already exists

		int agreementId = rentAgreementMasterService.getAgreementIdBySiteCode(siteCode);
		// First get the RentAgreementMaster object
		RentAgreementMaster agreement = rentAgreementMasterRepository.findById(agreementId)
				.orElseThrow(() -> new RuntimeException("Agreement not found"));

		Optional<SiteRentRecord> siteRentRecordOpt = siteRentRecordRepository.findByAgreementId(agreement);

		if (siteRentRecordOpt.isPresent()) {

			// Create DTO with new values
			SiteRentRecordDTO siteRentRecordDTO = new SiteRentRecordDTO();
			siteRentRecordDTO.setSiteCode(siteCode);			
			siteRentRecordDTO.setPaymentDate(date);
			siteRentRecordDTO.setAmountPaid(Integer.parseInt(amountPaid));
			siteRentRecordDTO.setRemarks(remarks);
			siteRentRecordDTO.setUtrNo(utrNo);
			siteRentRecordDTO.setTransactionStatus(transactionStatus.equals("Completed Transaction"));
			siteRentRecordDTO.setReason(reason);
			

			// Update existing record

			SiteRentRecord siteRentRecord = siteRentRecordOpt.get();
			// Check if the rent record exists in the database
			siteRentRecord.setSiteCode(siteRentRecordDTO.getSiteCode());
			siteRentRecord.setPaymentDate(siteRentRecordDTO.getPaymentDate());
			siteRentRecord.setAmountPaid(siteRentRecordDTO.getAmountPaid());
			siteRentRecord.setRemarks(siteRentRecordDTO.getRemarks());
			siteRentRecord.setUtrNo(siteRentRecordDTO.getUtrNo());
			siteRentRecord.setTransactionStatus(siteRentRecordDTO.isTransactionStatus());
			siteRentRecord.setReason(siteRentRecordDTO.getReason());

			siteRentRecordRepository.save(siteRentRecord);

		} else {
			// Create new record
			SiteRentRecord siteRentRecord = new SiteRentRecord();
			siteRentRecord.setSiteCode(siteCode);
			siteRentRecord.setPaymentDate(date);
			siteRentRecord.setAmountPaid(Integer.parseInt(amountPaid));
			siteRentRecord.setRemarks(remarks);
			siteRentRecord.setUtrNo(utrNo);
			siteRentRecord.setTransactionStatus(transactionStatus.equals("Completed Transaction"));
			siteRentRecord.setReason(reason);

			siteRentRecordRepository.save(siteRentRecord);
		}
	}


	
}
