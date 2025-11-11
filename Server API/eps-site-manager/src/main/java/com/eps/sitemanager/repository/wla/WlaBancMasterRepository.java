package com.eps.sitemanager.repository.wla;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.eps.sitemanager.model.wla.WlaBancMaster;

public interface WlaBancMasterRepository extends CrudRepository<WlaBancMaster, Integer> {
	
	
	public Optional<WlaBancMaster> findById(int wlaBancId);
 
	
}
