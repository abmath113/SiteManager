package com.eps.sitemanager.dto.master;
	
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="Landlord Master Details DTO.")
public class LandlordMasterDTO {
	

	private int landlordId;

    private String name;
    private String beneficiaryName;
    private String mobileNo;
    private String ifscCode;
    private String accountNo;
    private String pan;
    private String aadharNo;
    private boolean status;
    private String address;
    private Boolean isGST;


	
	
}
