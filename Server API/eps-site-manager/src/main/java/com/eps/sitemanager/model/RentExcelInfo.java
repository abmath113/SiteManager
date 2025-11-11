package com.eps.sitemanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;

public class RentExcelInfo {
	
	
	@Override
	public String toString() {
		return "RentExcelInfo [excelSiteCode=" + excelSiteCode + ", amountPaid=" + amountPaid + ", date=" + date
				+ ", utrNo=" + utrNo + ", transactionStatus=" + transactionStatus + ", remarks=" + remarks + ", reason="
				+ reason + "]";
	}

	@ExcelField(title = "Site Code")
	private String excelSiteCode;
	
	@ExcelField(title = "Amount Paid")
    private Double amountPaid;

    @ExcelField(title = "Date")
    private String date;

    @ExcelField(title = "UTR No.")
    private String utrNo;

    @ExcelField(title = "Transaction Status")
    private String transactionStatus;

    @ExcelField(title = "Remarks")
    private String remarks;
    
    @ExcelField(title = "Reason")
    private String reason;

	public String getExcelSiteCode() {
		return excelSiteCode;
	}

	public void setExcelSiteCode(String excelSiteCode) {
		this.excelSiteCode = excelSiteCode;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUtrNo() {
		return utrNo;
	}

	public void setUtrNo(String utrNo) {
		this.utrNo = utrNo;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
    
	 //  method to get LocalDate from string date
    public LocalDate convertToLocalDate() {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
        } catch (DateTimeParseException e) {
            // Log the error or handle it as appropriate for your application
            System.out.println("Warning: Invalid date format: " + date);
            return null;
        }
    
    }
	

}
