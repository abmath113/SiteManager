package com.eps.sitemanager.model.master;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "bank_master")
public class BankMaster implements Serializable{
	
	/**
	 * @author abhishek.thorat
	 * 
	 */
	private static final long serialVersionUID = -6182839960285147955L;
	
	
	private int bankId;
	private String bankName;
	private String bankCode;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bankId")
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	@Column(name = "bank_name", length = 50)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Column(name = "bank_code", length = 10)    
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Override
	public String toString() {
		return "BankMaster [bankId=" + bankId + ", bankName=" + bankName + ", bankCode=" + bankCode + "]";
	}
	
	
}
