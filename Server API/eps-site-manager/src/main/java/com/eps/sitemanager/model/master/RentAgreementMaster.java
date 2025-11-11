package com.eps.sitemanager.model.master;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "rent_agreement_master")
public class RentAgreementMaster implements Serializable {

	/**
	 * @author abhishek.thorat
	 */
	private static final long serialVersionUID = 1986619395144951746L;

	private int agreementId;

	private SiteMaster siteId;
	private LandlordMaster landlordId;

	private LocalDate agreementDate;
	private LocalDate considerAgreementDate;
	private LocalDate rentPayStartDate;
	private LocalDate agreementEndDate;
	private LocalDate terminationDate;

	private int agreementSpan;
	private int solarPanelRent;
	private int deposit;
	private int monthlyRent;
	private int paymentInterval;
	private int escalationAfterMonths;
	private int escalationPercent;
	private boolean rentAgreementStatus;
	private String terminationRemark;

	private boolean agreementScanExist;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agreement_id")
	public int getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(int agreementId) {
		this.agreementId = agreementId;
	}





















    @ManyToOne
	public SiteMaster getSiteId() {
		return siteId;
	}

	public void setSiteId(SiteMaster siteId) {
		this.siteId = siteId;
	}

	@ManyToOne
	public LandlordMaster getLandlordId() {
		return landlordId;
	}

	public void setLandlordId(LandlordMaster landlordId) {
		this.landlordId = landlordId;
	}

	@Column(name = "agreement_date")
	public LocalDate getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(LocalDate agreementDate) {
		this.agreementDate = agreementDate;
	}

	@Column(name = "consider_agreement_date")
	public LocalDate getConsiderAgreementDate() {
		return considerAgreementDate;
	}

	public void setConsiderAgreementDate(LocalDate considerAgreementDate) {
		this.considerAgreementDate = considerAgreementDate;
	}

	@Column(name = "rent_pay_start_date")
	public LocalDate getRentPayStartDate() {
		return rentPayStartDate;
	}

	public void setRentPayStartDate(LocalDate rentPayStartDate) {
		this.rentPayStartDate = rentPayStartDate;
	}

	@Column(name = "agreement_span")
	public int getAgreementSpan() {
		return agreementSpan;
	}

	public void setAgreementSpan(int agreementSpan) {
		this.agreementSpan = agreementSpan;
	}

	@Column(name = "solar_panel_rent")
	public int getSolarPanelRent() {
		return solarPanelRent;
	}

	public void setSolarPanelRent(int solarPanelRent) {
		this.solarPanelRent = solarPanelRent;
	}

	@Column(name = "monthly_rent")
	public int getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(int monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	@Column(name = "payment_interval")
	public int getPaymentInterval() {
		return paymentInterval;
	}

	public void setPaymentInterval(int paymentInterval) {
		this.paymentInterval = paymentInterval;
	}

	@Column(name = "escalation_after_months")
	public int getEscalationAfterMonths() {
		return escalationAfterMonths;
	}

	public void setEscalationAfterMonths(int escalationAfterMonths) {
		this.escalationAfterMonths = escalationAfterMonths;
	}

	@Column(name = "escaalation_percent")
	public int getEscalationPercent() {
		return escalationPercent;
	}

	public void setEscalationPercent(int escalationPercent) {
		this.escalationPercent = escalationPercent;
	}

	@Column(name = "rent_agreement_status")
	public boolean isRentAgreementStatus() {
		return rentAgreementStatus;
	}

	public void setRentAgreementStatus(boolean rentAgreementStatus) {
		this.rentAgreementStatus = rentAgreementStatus;
	}

	@Column(name = "termination_remark")
	public String getTerminationRemark() {
		return terminationRemark;
	}

	public void setTerminationRemark(String terminationRemark) {
		this.terminationRemark = terminationRemark;
	}

	@Column(name = "termination_date")
	public LocalDate getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(LocalDate terminationDate) {
		this.terminationDate = terminationDate;
	}

	@Transient
	public boolean isAgreementScanExist() {
		return agreementScanExist;
	}

	public void setAgreementScanExist(boolean agreementScanExist) {
		this.agreementScanExist = agreementScanExist;
	}

	@Column(name = "agreement_end_date")
	public LocalDate getAgreementEndDate() {
		return agreementEndDate;
	}

	public void setAgreementEndDate(LocalDate agreementEndDate) {
		this.agreementEndDate = agreementEndDate;
	}

	@Override
	public String toString() {
		return "RentAgreementMaster [agreementId=" + agreementId + ", siteId=" + siteId + ", landlordId=" + landlordId
				+ ", agreementDate=" + agreementDate + ", considerAgreementDate=" + considerAgreementDate
				+ ", rentPayStartDate=" + rentPayStartDate + ", agreementEndDate=" + agreementEndDate
				+ ", terminationDate=" + terminationDate + ", agreementSpan=" + agreementSpan + ", solarPanelRent="
				+ solarPanelRent + ", monthlyRent=" + monthlyRent + ", paymentInterval=" + paymentInterval
				+ ", escalationAfterMonths=" + escalationAfterMonths + ", escalationPercent=" + escalationPercent
				+ ", rentAgreementStatus=" + rentAgreementStatus + ", terminationRemark=" + terminationRemark
				+ ", agreementScanExist=" + agreementScanExist + "]";
	}

}
