package com.eps.sitemanager.model.master;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;



@Entity
@Table(name = "landlord_master")
public class LandlordMaster implements Serializable {

   
   
	
	private static final long serialVersionUID = -3094005439957679336L;
	
	
	private int landlordId;
    private String createdBy;
    private Date createdOn;
    private String name;
    private String beneficiaryName;
    private String mobileNo;
    private String ifscCode;
    private String accountNo;
    private String pan;
    private String aadharNo;
    private Boolean status;
    private String address;
   
    private Boolean isGST;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "landlord_id")
    public int getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(int landlordId) {
        this.landlordId = landlordId;
    }

    @Column(name = "created_by", length = 255)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "name", length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
    @Column(name = "mobile_no", length = 15)
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "ifsc_code", length = 11)
    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @Column(name = "account_no", length = 20)
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Column(name = "pan", length = 10)
    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Column(name = "aadhar_no", length = 20)
    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    @Column(name = "status")
    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Column(name = "address", length = 500)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "gst")
    @JsonProperty("gst")
    public Boolean isGST() {
        return isGST;
    }
    public void setGST(Boolean gst) {this.isGST = gst;}

	@Override
	public String toString() {
		return "LandlordMaster [landlordId=" + landlordId + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", name=" + name + ", beneficiaryName=" + beneficiaryName + ", mobileNo=" + mobileNo + ", ifscCode="
				+ ifscCode + ", accountNo=" + accountNo + ", pan=" + pan + ", aadharNo=" + aadharNo + ", status="
				+ status + ", address=" + address + "]";
	}


}



