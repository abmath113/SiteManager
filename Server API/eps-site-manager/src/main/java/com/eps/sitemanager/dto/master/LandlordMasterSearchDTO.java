package com.eps.sitemanager.dto.master;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="Landlord Master Details DTO.")
public class LandlordMasterSearchDTO {
	

	private int landlordId;

    private String name;
    private boolean status;
    private String address;

	
	
}
