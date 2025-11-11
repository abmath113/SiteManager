package com.eps.sitemanager.model.master;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "channel_manager_master")
public class ChannelManagerMaster implements Serializable{

	/**
	 * @author abhishek.thorat
	 */
	private static final long serialVersionUID = -5669275053497622354L;
	

	private int channelManagerId;
	private String channelManagerName;
	private String phoneNo;
	private String emailId;
	private boolean status;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "channel_manager_id")
	public int getChannelManagerId() {
		return channelManagerId;
	}
	public void setChannelManagerId(int channelManagerId) {
		this.channelManagerId = channelManagerId;
	}
	
	@Column(name = "channel_manager_name")
	public String getChannelManagerName() {
		return channelManagerName;
	}
	public void setChannelManagerName(String channelManagerName) {
		this.channelManagerName = channelManagerName;
	}
	
	@Column(name = "phone_no")
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Column(name = "email_id")
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	@Column(name = "status")
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ChannelManagerMaster [channelManagerId=" + channelManagerId + ", channelManagerName="
				+ channelManagerName + ", phoneNo=" + phoneNo + ", emailId=" + emailId + "]";
	}
	

	
	
}
