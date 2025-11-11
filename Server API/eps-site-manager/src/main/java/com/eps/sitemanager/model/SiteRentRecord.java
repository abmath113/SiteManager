package com.eps.sitemanager.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.eps.sitemanager.model.master.RentAgreementMaster;

@Entity
@Table(name = "site_rent_records")
public class SiteRentRecord implements Serializable {

	/**
	 * @author abhishek.thorat
	 */
	private static final long serialVersionUID = 2734491960653352037L;

	private int siteRentRecordId;
	private RentAgreementMaster agreementId;

	// Calculated rent
	private int generatedRent;
	private String rentMonth; // yyyy-mm (month for which rent is calculated)

	// Data coming from excel file
	private String siteCode;
	private String paymentDate; // Date were getting from excel
	private int amountPaid;
	private String remarks; // mostly used for justifying rent mismatch
	private boolean transactionStatus;
	private String utrNo; // unique transaction number
	private String reason; // reason for failure of transcation

	// Getter Setter

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "site_rent_record_id")
	public int getSiteRentRecordId() {
		return siteRentRecordId;
	}

	public void setSiteRentRecordId(int siteRentRecordId) {
		this.siteRentRecordId = siteRentRecordId;
	}

	@ManyToOne
	public RentAgreementMaster getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(RentAgreementMaster agreementId) {
		this.agreementId = agreementId;
	}

	@Column(name = "generated_rent")
	public int getGeneratedRent() {
		return generatedRent;
	}

	public void setGeneratedRent(int generatedRent) {
		this.generatedRent = generatedRent;
	}

	@Column(name = "rent_month")
	public String getRentMonth() {
		return rentMonth;
	}

	public void setRentMonth(String rentMonth) {
		this.rentMonth = rentMonth;
	}

	@Column(name = "site_code")
	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	@Column(name = "payment_date")
	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Column(name = "amount_paid")
	public int getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(int amountPaid) {
		this.amountPaid = amountPaid;
	}

	@Column(name = "remarks", length = 4000)
	public String getRemarks() {
	    return remarks;
	}

	public void setRemarks(String remarks) {
	    this.remarks = remarks;
	}

	@Column(name = "reason", length = 4000)
	public String getReason() {
	    return reason;
	}

	public void setReason(String reason) {
	    this.reason = reason;
	}


	@Column(name = "transaction_status")
	public boolean isTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(boolean transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	@Column(name = "utr_no")
	public String getUtrNo() {
		return utrNo;
	}

	public void setUtrNo(String utrNo) {
		this.utrNo = utrNo;
	}



	@Override
	public String toString() {
		return "SiteRentRecord [siteRentRecordId=" + siteRentRecordId + ", agreementId=" + agreementId
				+ ", generatedRent=" + generatedRent + ", rentMonth=" + rentMonth + ", siteCode=" + siteCode
				+ ", paymentDate=" + paymentDate + ", amountPaid=" + amountPaid + ", remarks=" + remarks
				+ ", transactionStatus=" + transactionStatus + ", utrNo=" + utrNo + ", reason=" + reason + "]";
	}

}
