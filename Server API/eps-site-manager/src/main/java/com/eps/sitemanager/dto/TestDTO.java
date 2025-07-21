package com.eps.sitemanager.dto;

import lombok.Data;

//@Data  // Uncomment if you want Lombok to generate getters, setters, and other methods
public class TestDTO {

    private String emailId;
    
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    
  
}
