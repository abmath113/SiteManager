package com.eps.sitemanager.services.master;

import java.io.Console;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.master.SiteMasterDTO;
import com.eps.sitemanager.dto.master.SiteMasterSearchDTO;
import com.eps.sitemanager.model.master.BankMaster;
import com.eps.sitemanager.model.master.ChannelManagerMaster;
import com.eps.sitemanager.model.master.SiteMaster;
import com.eps.sitemanager.repository.master.BankMasterRepository;
import com.eps.sitemanager.repository.master.SiteCodeAndSiteId;
import com.eps.sitemanager.repository.master.SiteMasterRepository;

@Service
public class SiteMasterServiceImpl implements SiteMasterService {

    private SiteMasterRepository siteMasterRepo;
    private BankMasterRepository bankMasterRepo;

    @Autowired
    public SiteMasterServiceImpl(SiteMasterRepository siteMasterRepo, BankMasterRepository bankMasterRepo) {
        super();
        this.siteMasterRepo = siteMasterRepo;
        this.bankMasterRepo = bankMasterRepo;
    }

    @Override
    public SiteMaster saveSiteMaster(SiteMasterDTO siteMasterDTO,ChannelManagerMaster channelManagerMaster,BankMaster bankMaster) {
        SiteMaster siteMaster = new SiteMaster();
        siteMaster.setSiteCode(siteMasterDTO.getSitecode());
        siteMaster.setSiteATMs(siteMasterDTO.getSiteatms());
        siteMaster.setSiteArea(siteMasterDTO.getSiteArea());
        siteMaster.setSiteStatus(siteMasterDTO.isSitestatus());
        siteMaster.setBank(bankMaster);
        siteMaster.setSiteAddress(siteMasterDTO.getSiteaddress());
        siteMaster.setProjectName(siteMasterDTO.getProjectName());  
        siteMaster.setState(siteMasterDTO.getState());
        siteMaster.setDistrict(siteMasterDTO.getDistrict());
        siteMaster.setChannelManager(channelManagerMaster);
        return siteMasterRepo.save(siteMaster);
    }

    @Override
    public Optional<SiteMaster> updateSiteMaster(SiteMasterDTO siteMasterDTO, SiteMaster siteMaster,ChannelManagerMaster channelManagerMaster,BankMaster bankMaster) {
        // Update all the required fields
        siteMaster.setSiteCode(siteMasterDTO.getSitecode());
        siteMaster.setSiteATMs(siteMasterDTO.getSiteatms());
        siteMaster.setSiteArea(siteMasterDTO.getSiteArea());
        siteMaster.setSiteStatus(siteMasterDTO.isSitestatus());
        siteMaster.setBank(bankMaster);
        siteMaster.setSiteAddress(siteMasterDTO.getSiteaddress());
        siteMaster.setProjectName(siteMasterDTO.getProjectName());
        siteMaster.setState(siteMasterDTO.getState());
        siteMaster.setDistrict(siteMasterDTO.getDistrict());
        siteMaster.setChannelManager(channelManagerMaster);
        
        return Optional.of(siteMasterRepo.save(siteMaster));
    }

    @Override
    public void deleteSiteMaster(int siteId) {
        siteMasterRepo.deleteById(siteId);
    }

    @Override
    public int updateSiteMasterStatus(boolean siteStatus, int siteId) {
        return siteMasterRepo.updateSiteMasterByStatusAndSiteId(siteStatus, siteId);
    }

    @Override
    public Optional<SiteMaster> getSiteMasterById(int siteId) {
        return siteMasterRepo.findById(siteId);
    }

    @Override
    public Optional<SiteMaster> getSiteMasterBySiteCode(String siteCode) {
        Optional<SiteMaster> siteList = siteMasterRepo.findBySiteCode(siteCode);
        if (!siteList.isEmpty()) {
            return Optional.of(siteList.get());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<SiteMaster> getAllSiteMaster() {
        return siteMasterRepo.findAll();
    }

    @Override
    public Optional<SiteMaster> searchSiteMasters(SiteMasterSearchDTO siteMasterSearchDTO) {
        if (siteMasterSearchDTO.getSitecode() != null && !siteMasterSearchDTO.getSitecode().equals("")) {
            return siteMasterRepo.findBySiteCode(siteMasterSearchDTO.getSitecode());
        }
        if (siteMasterSearchDTO.getAtmid() != null && !siteMasterSearchDTO.getAtmid().equals("")) {
            return siteMasterRepo.findBySiteATMs(siteMasterSearchDTO.getAtmid());
        }
        if (siteMasterSearchDTO.getBank() != null && !siteMasterSearchDTO.getBank().equals("")) {
            return siteMasterRepo.findByBank(bankMasterRepo.findByBankId(Integer.parseInt(siteMasterSearchDTO.getBank())).get(0));
        }
        if (siteMasterSearchDTO.getLocation() != null && !siteMasterSearchDTO.getLocation().equals("")) {
            return siteMasterRepo.findBySiteAddressContaining(siteMasterSearchDTO.getLocation());
        }

        return Optional.empty();
    }

    @Override
    public boolean checkSiteAlreadyExists(String siteCode) {
        Optional<SiteMaster> siteMasterList = siteMasterRepo.findBySiteCode(siteCode);
        if (!siteMasterList.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public List<SiteCodeAndSiteId> getAllSitesOnlySiteIdAndSiteCode() {
        return siteMasterRepo.findSiteCodeAndSiteIdBy();
    }

 // Integrated method to check if a site code exists while fetching site codes
    @Override
    public boolean checkSiteCodeExists(String siteCode) {
        Iterable<SiteMaster> siteMasters = siteMasterRepo.findAll();
        for (SiteMaster siteMaster : siteMasters) {
            if (siteMaster.getSiteCode().equals(siteCode)) {
                return true; // Site code exists
            }
        }
        return false; // Site code not found
    }
}
