package com.eps.sitemanager.controller.wla;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.sitemanager.configurations.error.DataNotFoundException;
import com.eps.sitemanager.configurations.error.FailedToUpdateException;
import com.eps.sitemanager.configurations.error.ParameterConstraintException;
import com.eps.sitemanager.dto.RequestValidaton;
import com.eps.sitemanager.dto.WlaBancDTO;
import com.eps.sitemanager.dto.master.SiteMasterDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.model.wla.WlaBancMaster;
import com.eps.sitemanager.services.wla.WlaMasterService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/wlamaster")
public class WlaMasterController {
	
	@Autowired
	private WlaMasterService wlaMasterService;
	
	@PostMapping("/save")
	public ResponseEntity<WlaBancMaster> saveWlaMaster(@RequestBody WlaBancDTO wlaBancDTO ) {
		
		WlaBancMaster wlaBancMaster = wlaMasterService.saveWlaBancMaster(wlaBancDTO);
		if(wlaBancMaster == null || wlaBancMaster.getWlaBancId() == 0) {
			throw new DataNotFoundException("Failed to save Wla Banc : "+ wlaBancMaster );
		}
		else {
			return ResponseEntity.ok(wlaBancMaster);
		}
	}
	
	
	
	@PostMapping("/update")
	@Operation(tags = "Update WLA Master", summary = "Updates the Single WLA Master Details.")
	protected ResponseEntity<WlaBancMaster> updateSiteMaster(@RequestBody WlaBancDTO wlaBancDTO) {
		

		Optional<WlaBancMaster> wlaMasterOpt = wlaMasterService.getWlaBancMasterById(wlaBancDTO.getWlaBancId());
		if (wlaMasterOpt.isEmpty() || wlaMasterOpt.get().getWlaBancId() != wlaBancDTO.getWlaBancId()) {
			throw new DataNotFoundException("Selected WLA Banc does not exists to Update.");
		}


		// Update the WLA Master Details
		Optional<WlaBancMaster> updatedWLAMaster = Optional.ofNullable(wlaMasterService.updateWlaBancMaster(wlaBancDTO.getWlaBancId(), wlaBancDTO));
		if (updatedWLAMaster.isEmpty() || updatedWLAMaster.get().getWlaBancId() != wlaBancDTO.getWlaBancId()) {
			throw new FailedToUpdateException(
					"Failed to Update WLA Master Data, Site Code : " + wlaBancDTO.getEpsSiteCode());
		} else {
			return ResponseEntity.ok(updatedWLAMaster.get());
		}
	}
	
	
	@GetMapping("/getall")
	public ResponseEntity<List<WlaBancMaster>> showAllWlaMaster(){
		
		List<WlaBancMaster> wlaBancMasters = new ArrayList<WlaBancMaster>();
		
		Iterable<WlaBancMaster> wlaIterable = wlaMasterService.getAllWlaBancMasters();
		
		wlaIterable.forEach(wlaBancMasters::add);
		return ResponseEntity.ok(wlaBancMasters);
		
	}
	
 	@GetMapping("/getbywlabancid/{id}")
 	@Operation(tags = "Get WLA Banc details by wlaBancId.")
 	protected ResponseEntity<WlaBancMaster> getByWlaBancId(@PathVariable(value="id") int wlaBancId){
			// Get the Site Master Details
			Optional<WlaBancMaster> wlaMasterDetailOpt = wlaMasterService.getWlaBancMasterById(wlaBancId);
			if(wlaMasterDetailOpt.isPresent()) {
				return ResponseEntity.ok(wlaMasterDetailOpt.get());
			}
			return ResponseEntity.notFound().build();	
    }
 		
	
   
	
}
