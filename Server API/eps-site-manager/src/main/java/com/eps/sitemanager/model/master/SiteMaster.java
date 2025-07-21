
package com.eps.sitemanager.model.master;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * 
 * @author Ankur Maurya
 * 
 */
@Entity
@Table(name = "site_master")
public class SiteMaster implements Serializable {

	private static final long serialVersionUID = -3957277203882592034L;

	private int siteId;
	private String siteCode;
	private int siteArea;
	private String siteATMs;
	private String siteAddress;
	private BankMaster bank;
	private boolean siteStatus;
	private ChannelManagerMaster channelManager;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "site_id")
	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	@Column(name = "site_code", length = 30)
	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	@Column(name = "site_atms", length = 80)
	public String getSiteATMs() {
		return siteATMs;
	}
	public void setSiteATMs(String siteATMs) {
		this.siteATMs = siteATMs;
	}
	
	@Column(name = "site_area")
	public int getSiteArea() {
		return siteArea;
	}

	public void setSiteArea(int siteArea) {
		this.siteArea = siteArea;
	}

	@Column(name = "site_status")
	public boolean isSiteStatus() {
		return siteStatus;
	}

	public void setSiteStatus(boolean siteStatus) {
		this.siteStatus = siteStatus;
	}

	@Column(name = "site_address", length = 500)
	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	@ManyToOne
	public BankMaster getBank() {
		return bank;
	}

	public void setBank(BankMaster bank) {
		this.bank = bank;
	}

	@ManyToOne
	public ChannelManagerMaster getChannelManager() {
		return channelManager;
	}

	public void setChannelManager(ChannelManagerMaster channelManager) {
		this.channelManager = channelManager;
	}

	@Override
	public String toString() {
		return "SiteMaster [siteId=" + siteId + ", siteCode=" + siteCode + ", siteATMs=" + siteATMs + ", siteAddress="
				+ siteAddress + ", bank=" + bank + ", siteStatus=" + siteStatus + ", channelManager=" + channelManager
				+ "]";
	}


	
	

}
