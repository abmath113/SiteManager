package com.eps.sitemanager.services.wla;

import java.util.Optional;

import com.eps.sitemanager.dto.WlaBancDTO;
import com.eps.sitemanager.model.wla.WlaBancMaster;

public interface WlaMasterService {
	
	
	public WlaBancMaster saveWlaBancMaster(WlaBancDTO wlaBancDTO);
	
	public void deleteWlaBanc(int wlaBancId);
	
	public Optional<WlaBancMaster> getWlaBancMasterById(int wlaBancId);
	
	public Iterable<WlaBancMaster> getAllWlaBancMasters();

	WlaBancMaster updateWlaBancMaster(int wlaBancId, WlaBancDTO wlaBancDTO);
	
	
	
	
}
