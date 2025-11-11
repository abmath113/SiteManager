package com.eps.sitemanager.controller.master;

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
import com.eps.sitemanager.dto.master.ChannelManagerDTO;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.repository.master.ChannelManagerIdNameAndEmailId;
import com.eps.sitemanager.request.validator.master.ChannelManagerControllerRequestValidator;
import com.eps.sitemanager.services.master.ChannelManagerService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/channelmanager")
public class ChannelManagerController {

    private final ChannelManagerService channelManagerService;
    private final ChannelManagerControllerRequestValidator requestValidator;

    @Autowired
    public ChannelManagerController(ChannelManagerService channelManagerService, ChannelManagerControllerRequestValidator requestValidator) {
        this.channelManagerService = channelManagerService;
        this.requestValidator = requestValidator;
    }
    
   
    

    @PostMapping("/save")
    @Operation(tags = "Save Channel Manager details")
    protected ResponseEntity<ChannelManagerMaster> saveChannelManagerMaster(@RequestBody ChannelManagerDTO channelManagerDTO) {
        // Perform validation
        RequestValidaton requestValidaton = requestValidator.validateSaveUpdateRequest(channelManagerDTO);
        if (!requestValidaton.isState()) {
            throw new ParameterConstraintException(requestValidaton.getErrorMessage());
        }

        // Save channel manager details if validation is successful
        ChannelManagerMaster channelManagerMaster = channelManagerService.saveChannelManagerMaster(channelManagerDTO);
        if (channelManagerMaster == null || channelManagerMaster.getChannelManagerId() == 0) {
            throw new DataNotFoundException("Failed to save Channel Manager: " + channelManagerDTO.getChannelManagerName());
        } else {
            return ResponseEntity.ok(channelManagerMaster);
        }
    }

    @GetMapping("/searchall")
    @Operation(tags = "Show Channel Manager master",  description = "Show all Channel Manager Master records.")
    protected ResponseEntity<List<ChannelManagerMaster>> showAllChannelManagerMaster() {
        List<ChannelManagerMaster> channelManagerList = new ArrayList<>();
        Iterable<ChannelManagerMaster> channelManagerItr = channelManagerService.getAllChannelManagerMaster();
        channelManagerItr.forEach(channelManagerList::add);
        return ResponseEntity.ok(channelManagerList);
    }

    @GetMapping("/getbyid/{id}")
    @Operation(tags = "Get Channel Manager details by ID.")
    protected ResponseEntity<ChannelManagerMaster> getByChannelManagerId(@PathVariable(value = "id") int channelManagerId) {
        // Get the Channel Manager Details
        Optional<ChannelManagerMaster> channelManagerOpt = channelManagerService.getChannelManagerMasterById(channelManagerId);
        if (channelManagerOpt.isPresent()) {
            return ResponseEntity.ok(channelManagerOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    @Operation(tags = "Update Channel Manager", description = "Updates a single Channel Manager Master record.")
    protected ResponseEntity<ChannelManagerMaster> updateChannelManagerMaster(@RequestBody ChannelManagerDTO channelManagerDTO) {
        // Perform validation
        RequestValidaton reqValidation = requestValidator.validateSaveUpdateRequest(channelManagerDTO);
        if (!reqValidation.isState()) {
            throw new ParameterConstraintException(reqValidation.getErrorMessage());
        }

        Optional<ChannelManagerMaster> channelManagerOpt = channelManagerService.getChannelManagerMasterById(channelManagerDTO.getChannelManagerId());
        if (channelManagerOpt.isEmpty() || channelManagerOpt.get().getChannelManagerId() != channelManagerDTO.getChannelManagerId()) {
            throw new DataNotFoundException("Selected Channel Manager does not exist to update.");
        }

        Optional<ChannelManagerMaster> updatedChannelManager = channelManagerService.updateChannelManagerMaster(channelManagerDTO, channelManagerOpt.get());
        if (updatedChannelManager.isEmpty() || updatedChannelManager.get().getChannelManagerId() != channelManagerDTO.getChannelManagerId()) {
            throw new FailedToUpdateException("Failed to update Channel Manager: " + channelManagerDTO.getChannelManagerName());
        } else {
            return ResponseEntity.ok(updatedChannelManager.get());
        }
    }

    @GetMapping("/getallmanagerdetails")
    @Operation(tags = "Gets all Channel Manager ID, Name, and Phone No.", description = "Fetches all records for Channel Manager, including only ID, Name, and Phone No.")
    protected ResponseEntity<Iterable<ChannelManagerMaster>> getAllChannelManagerDetails() {
        return ResponseEntity.ok(channelManagerService.getAllChannelManagerMaster());
    }
    
    @GetMapping("/getallchannelmanagersbynameandemail")
    @Operation(tags = "Gets all Channel Manager ID, Name, and Email ID", description = "Fetches all records for Channel Manager, including only ID, Name, and Email ID.")
    protected ResponseEntity<List<ChannelManagerIdNameAndEmailId>> getAllChannelManagerIdNameAndEmailId() {
        return ResponseEntity.ok(channelManagerService.getAllChannelManagerIdNameAndEmailId());
    }
   
}	
