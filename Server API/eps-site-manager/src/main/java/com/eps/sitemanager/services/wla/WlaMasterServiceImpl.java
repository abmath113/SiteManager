package com.eps.sitemanager.services.wla;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eps.sitemanager.dto.WlaBancDTO;
import com.eps.sitemanager.model.wla.WlaBancMaster;
import com.eps.sitemanager.repository.wla.WlaBancMasterRepository;

@Service
public class WlaMasterServiceImpl implements WlaMasterService {

	@Autowired
	private WlaBancMasterRepository wlaBancMasterRepo;

	@Override
	public WlaBancMaster saveWlaBancMaster(WlaBancDTO wlaBancDTO) {

		WlaBancMaster wlaBancMaster = new WlaBancMaster();

		wlaBancMaster.setEpsSiteCode(wlaBancDTO.getEpsSiteCode());
		wlaBancMaster.setSiteType(wlaBancDTO.getSiteType());
		wlaBancMaster.setSiteSubtype(wlaBancDTO.getSiteSubtype());
		wlaBancMaster.setSiteStatus(wlaBancDTO.getSiteStatus());
		wlaBancMaster.setLen(wlaBancDTO.getLen());
		wlaBancMaster.setOldAtmId(wlaBancDTO.getOldAtmId());
		wlaBancMaster.setAtmId(wlaBancDTO.getAtmId());
		wlaBancMaster.setLocationName(wlaBancDTO.getLocationName());
		wlaBancMaster.setDistrict(wlaBancDTO.getDistrict());
		wlaBancMaster.setAddress(wlaBancDTO.getAddress());
		wlaBancMaster.setState(wlaBancDTO.getState());
		wlaBancMaster.setCity(wlaBancDTO.getCity());
		wlaBancMaster.setPinCode(wlaBancDTO.getPinCode());
		wlaBancMaster.setLocationType(wlaBancDTO.getLocationType());
		wlaBancMaster.setLocationClass(wlaBancDTO.getLocationClass());
		wlaBancMaster.setLatitude(wlaBancDTO.getLatitude());
		wlaBancMaster.setLongitude(wlaBancDTO.getLongitude());
		wlaBancMaster.setLandlordName(wlaBancDTO.getLandlordName());
		wlaBancMaster.setLandlordNumber(wlaBancDTO.getLandlordNumber());
		wlaBancMaster.setCmName(wlaBancDTO.getCmName());
		wlaBancMaster.setCmContactNumber(wlaBancDTO.getCmContactNumber());
		wlaBancMaster.setRmName(wlaBancDTO.getRmName());
		wlaBancMaster.setRmContactNumber(wlaBancDTO.getRmContactNumber());
		wlaBancMaster.setStateHeadName(wlaBancDTO.getStateHeadName());
		wlaBancMaster.setStateHeadNumber(wlaBancDTO.getStateHeadNumber());
		wlaBancMaster.setMasterFranchiseName(wlaBancDTO.getMasterFranchiseName());
		wlaBancMaster.setFranchiseContactNumber(wlaBancDTO.getFranchiseContactNumber());
		wlaBancMaster.setFranchiseMailId(wlaBancDTO.getFranchiseMailId());
		wlaBancMaster.setTisCategory(wlaBancDTO.getTisCategory());
		wlaBancMaster.setTisVendorName(wlaBancDTO.getTisVendorName());
		wlaBancMaster.setTisOrderDate(wlaBancDTO.getTisOrderDate());
		wlaBancMaster.setTisStartDate(wlaBancDTO.getTisStartDate());
		wlaBancMaster.setTisEndDate(wlaBancDTO.getTisEndDate());
		wlaBancMaster.setTisStatus(wlaBancDTO.getTisStatus());
		wlaBancMaster.setAcMake(wlaBancDTO.getAcMake());
		wlaBancMaster.setAcCapacity(wlaBancDTO.getAcCapacity());
		wlaBancMaster.setAcStatus(wlaBancDTO.getAcStatus());
		wlaBancMaster.setUpsVendor(wlaBancDTO.getUpsVendor());
		wlaBancMaster.setUpsOrderDate(wlaBancDTO.getUpsOrderDate());
		wlaBancMaster.setUpsBackupCapacityHrs(wlaBancDTO.getUpsBackupCapacityHrs());
		wlaBancMaster.setNoOfBatteries(wlaBancDTO.getNoOfBatteries());
		wlaBancMaster.setUpsDeliveryDate(wlaBancDTO.getUpsDeliveryDate());
		wlaBancMaster.setUpsInstallationDate(wlaBancDTO.getUpsInstallationDate());
		wlaBancMaster.setUpsInstallationStatus(wlaBancDTO.getUpsInstallationStatus());
		wlaBancMaster.setItServoStabilizer(wlaBancDTO.getItServoStabilizer());
		wlaBancMaster.setVsatType(wlaBancDTO.getVsatType());
		wlaBancMaster.setVsatVendor(wlaBancDTO.getVsatVendor());
		wlaBancMaster.setVsatOrderDate(wlaBancDTO.getVsatOrderDate());
		wlaBancMaster.setVsatDeliveryDate(wlaBancDTO.getVsatDeliveryDate());
		wlaBancMaster.setVsatId(wlaBancDTO.getVsatId());
		wlaBancMaster.setVsatInstallationStatus(wlaBancDTO.getVsatInstallationStatus());
		wlaBancMaster.setVsatCommissioningDate(wlaBancDTO.getVsatCommissioningDate());
		wlaBancMaster.setHugsSubnet(wlaBancDTO.getHugsSubnet());
		wlaBancMaster.setSiteBrandingVendor(wlaBancDTO.getSiteBrandingVendor());
		wlaBancMaster.setSiteBrandingType(wlaBancDTO.getSiteBrandingType());
		wlaBancMaster.setSiteBrandingOrderDate(wlaBancDTO.getSiteBrandingOrderDate());
		wlaBancMaster.setSignageInstallationDate(wlaBancDTO.getSignageInstallationDate());
		wlaBancMaster.setSignageInstallationStatus(wlaBancDTO.getSignageInstallationStatus());
		wlaBancMaster.setAtmBrandingVendor(wlaBancDTO.getAtmBrandingVendor());
		wlaBancMaster.setAtmBrandingOrderDate(wlaBancDTO.getAtmBrandingOrderDate());
		wlaBancMaster.setAtmBrandingStatus(wlaBancDTO.getAtmBrandingStatus());
		wlaBancMaster.setAtmBrandingCompleteDate(wlaBancDTO.getAtmBrandingCompleteDate());
		wlaBancMaster.setESurveillanceVendorName(wlaBancDTO.getESurveillanceVendorName());
		wlaBancMaster.setESurveillanceOrderDate(wlaBancDTO.getESurveillanceOrderDate());
		wlaBancMaster.setESurveillanceDeliveryDate(wlaBancDTO.getESurveillanceDeliveryDate());
		wlaBancMaster.setESurveillanceStatus(wlaBancDTO.getESurveillanceStatus());
		wlaBancMaster.setESurveillanceCommissioningDate(wlaBancDTO.getESurveillanceCommissioningDate());
		wlaBancMaster.setAtmVendor(wlaBancDTO.getAtmVendor());
		wlaBancMaster.setAtmModel(wlaBancDTO.getAtmModel());
		wlaBancMaster.setAtmOrderDate(wlaBancDTO.getAtmOrderDate());
		wlaBancMaster.setAtmDeliveryDate(wlaBancDTO.getAtmDeliveryDate());
		wlaBancMaster.setAtmMachineSerialNo(wlaBancDTO.getAtmMachineSerialNo());
		wlaBancMaster.setSAndGLockNo(wlaBancDTO.getSAndGLockNo());
		wlaBancMaster.setAtmMachineStatus(wlaBancDTO.getAtmMachineStatus());
		wlaBancMaster.setTechLiveDate(wlaBancDTO.getTechLiveDate());
		wlaBancMaster.setCashLiveDate(wlaBancDTO.getCashLiveDate());
		wlaBancMaster.setEjDocketNumber(wlaBancDTO.getEjDocketNumber());
		wlaBancMaster.setTssDocketNo(wlaBancDTO.getTssDocketNo());
		wlaBancMaster.setLanIpAddress(wlaBancDTO.getLanIpAddress());
		wlaBancMaster.setAtmIp(wlaBancDTO.getAtmIp());
		wlaBancMaster.setSubnetMask(wlaBancDTO.getSubnetMask());
		wlaBancMaster.setTlsDomainName(wlaBancDTO.getTlsDomainName());
		wlaBancMaster.setTcpPortNo(wlaBancDTO.getTcpPortNo());
		wlaBancMaster.setSwitchDestinationIp1(wlaBancDTO.getSwitchDestinationIp1());
		wlaBancMaster.setLoadingType(wlaBancDTO.getLoadingType());
		wlaBancMaster.setCraName(wlaBancDTO.getCraName());
		wlaBancMaster.setNewLoadingType(wlaBancDTO.getNewLoadingType());
		wlaBancMaster.setFinalRemarks(wlaBancDTO.getFinalRemarks());

		wlaBancMasterRepo.save(wlaBancMaster);

		return wlaBancMaster;
	}
	
	@Override
	public WlaBancMaster updateWlaBancMaster(int wlaBancId, WlaBancDTO wlaBancDTO) {
	    Optional<WlaBancMaster> existingWlaBancMasterOpt = wlaBancMasterRepo.findById(wlaBancId);
	    
	    if (!existingWlaBancMasterOpt.isPresent()) {
	        throw new RuntimeException("WlaBancMaster with ID " + wlaBancId + " not found");
	    }
	     
	    WlaBancMaster existingWlaBancMaster = existingWlaBancMasterOpt.get();
	    
	    existingWlaBancMaster.setEpsSiteCode(wlaBancDTO.getEpsSiteCode());
	    existingWlaBancMaster.setSiteType(wlaBancDTO.getSiteType());
	    existingWlaBancMaster.setSiteSubtype(wlaBancDTO.getSiteSubtype());
	    existingWlaBancMaster.setSiteStatus(wlaBancDTO.getSiteStatus());
	    existingWlaBancMaster.setLen(wlaBancDTO.getLen());
	    existingWlaBancMaster.setOldAtmId(wlaBancDTO.getOldAtmId());
	    existingWlaBancMaster.setAtmId(wlaBancDTO.getAtmId());
	    existingWlaBancMaster.setLocationName(wlaBancDTO.getLocationName());
	    existingWlaBancMaster.setDistrict(wlaBancDTO.getDistrict());
	    existingWlaBancMaster.setAddress(wlaBancDTO.getAddress());
	    existingWlaBancMaster.setState(wlaBancDTO.getState());
	    existingWlaBancMaster.setCity(wlaBancDTO.getCity());
	    existingWlaBancMaster.setPinCode(wlaBancDTO.getPinCode());
	    existingWlaBancMaster.setLocationType(wlaBancDTO.getLocationType());
	    existingWlaBancMaster.setLocationClass(wlaBancDTO.getLocationClass());
	    existingWlaBancMaster.setLatitude(wlaBancDTO.getLatitude());
	    existingWlaBancMaster.setLongitude(wlaBancDTO.getLongitude());
	    existingWlaBancMaster.setLandlordName(wlaBancDTO.getLandlordName());
	    existingWlaBancMaster.setLandlordNumber(wlaBancDTO.getLandlordNumber());
	    existingWlaBancMaster.setCmName(wlaBancDTO.getCmName());
	    existingWlaBancMaster.setCmContactNumber(wlaBancDTO.getCmContactNumber());
	    existingWlaBancMaster.setRmName(wlaBancDTO.getRmName());
	    existingWlaBancMaster.setRmContactNumber(wlaBancDTO.getRmContactNumber());
	    existingWlaBancMaster.setStateHeadName(wlaBancDTO.getStateHeadName());
	    existingWlaBancMaster.setStateHeadNumber(wlaBancDTO.getStateHeadNumber());
	    existingWlaBancMaster.setMasterFranchiseName(wlaBancDTO.getMasterFranchiseName());
	    existingWlaBancMaster.setFranchiseContactNumber(wlaBancDTO.getFranchiseContactNumber());
	    existingWlaBancMaster.setFranchiseMailId(wlaBancDTO.getFranchiseMailId());
	    existingWlaBancMaster.setTisCategory(wlaBancDTO.getTisCategory());
	    existingWlaBancMaster.setTisVendorName(wlaBancDTO.getTisVendorName());
	    existingWlaBancMaster.setTisOrderDate(wlaBancDTO.getTisOrderDate());
	    existingWlaBancMaster.setTisStartDate(wlaBancDTO.getTisStartDate());
	    existingWlaBancMaster.setTisEndDate(wlaBancDTO.getTisEndDate());
	    existingWlaBancMaster.setTisStatus(wlaBancDTO.getTisStatus());
	    existingWlaBancMaster.setAcMake(wlaBancDTO.getAcMake());
	    existingWlaBancMaster.setAcCapacity(wlaBancDTO.getAcCapacity());
	    existingWlaBancMaster.setAcStatus(wlaBancDTO.getAcStatus());
	    existingWlaBancMaster.setUpsVendor(wlaBancDTO.getUpsVendor());
	    existingWlaBancMaster.setUpsOrderDate(wlaBancDTO.getUpsOrderDate());
	    existingWlaBancMaster.setUpsBackupCapacityHrs(wlaBancDTO.getUpsBackupCapacityHrs());
	    existingWlaBancMaster.setNoOfBatteries(wlaBancDTO.getNoOfBatteries());
	    existingWlaBancMaster.setUpsDeliveryDate(wlaBancDTO.getUpsDeliveryDate());
	    existingWlaBancMaster.setUpsInstallationDate(wlaBancDTO.getUpsInstallationDate());
	    existingWlaBancMaster.setUpsInstallationStatus(wlaBancDTO.getUpsInstallationStatus());
	    existingWlaBancMaster.setItServoStabilizer(wlaBancDTO.getItServoStabilizer());
	    existingWlaBancMaster.setVsatType(wlaBancDTO.getVsatType());
	    existingWlaBancMaster.setVsatVendor(wlaBancDTO.getVsatVendor());
	    existingWlaBancMaster.setVsatOrderDate(wlaBancDTO.getVsatOrderDate());
	    existingWlaBancMaster.setVsatDeliveryDate(wlaBancDTO.getVsatDeliveryDate());
	    existingWlaBancMaster.setVsatId(wlaBancDTO.getVsatId());
	    existingWlaBancMaster.setVsatInstallationStatus(wlaBancDTO.getVsatInstallationStatus());
	    existingWlaBancMaster.setVsatCommissioningDate(wlaBancDTO.getVsatCommissioningDate());
	    existingWlaBancMaster.setHugsSubnet(wlaBancDTO.getHugsSubnet());
	    existingWlaBancMaster.setSiteBrandingVendor(wlaBancDTO.getSiteBrandingVendor());
	    existingWlaBancMaster.setSiteBrandingType(wlaBancDTO.getSiteBrandingType());
	    existingWlaBancMaster.setSiteBrandingOrderDate(wlaBancDTO.getSiteBrandingOrderDate());
	    existingWlaBancMaster.setSignageInstallationDate(wlaBancDTO.getSignageInstallationDate());
	    existingWlaBancMaster.setSignageInstallationStatus(wlaBancDTO.getSignageInstallationStatus());
	    existingWlaBancMaster.setAtmBrandingVendor(wlaBancDTO.getAtmBrandingVendor());
	    existingWlaBancMaster.setAtmBrandingOrderDate(wlaBancDTO.getAtmBrandingOrderDate());
	    existingWlaBancMaster.setAtmBrandingStatus(wlaBancDTO.getAtmBrandingStatus());
	    existingWlaBancMaster.setAtmBrandingCompleteDate(wlaBancDTO.getAtmBrandingCompleteDate());
	    existingWlaBancMaster.setESurveillanceVendorName(wlaBancDTO.getESurveillanceVendorName());
	    existingWlaBancMaster.setESurveillanceOrderDate(wlaBancDTO.getESurveillanceOrderDate());
	    existingWlaBancMaster.setESurveillanceDeliveryDate(wlaBancDTO.getESurveillanceDeliveryDate());
	    existingWlaBancMaster.setESurveillanceStatus(wlaBancDTO.getESurveillanceStatus());
	    existingWlaBancMaster.setESurveillanceCommissioningDate(wlaBancDTO.getESurveillanceCommissioningDate());
	    existingWlaBancMaster.setAtmVendor(wlaBancDTO.getAtmVendor());
	    existingWlaBancMaster.setAtmModel(wlaBancDTO.getAtmModel());
	    existingWlaBancMaster.setAtmOrderDate(wlaBancDTO.getAtmOrderDate());
	    existingWlaBancMaster.setAtmDeliveryDate(wlaBancDTO.getAtmDeliveryDate());
	    existingWlaBancMaster.setAtmMachineSerialNo(wlaBancDTO.getAtmMachineSerialNo());
	    existingWlaBancMaster.setSAndGLockNo(wlaBancDTO.getSAndGLockNo());
	    existingWlaBancMaster.setAtmMachineStatus(wlaBancDTO.getAtmMachineStatus());
	    existingWlaBancMaster.setTechLiveDate(wlaBancDTO.getTechLiveDate());
	    existingWlaBancMaster.setCashLiveDate(wlaBancDTO.getCashLiveDate());
	    existingWlaBancMaster.setEjDocketNumber(wlaBancDTO.getEjDocketNumber());
	    existingWlaBancMaster.setTssDocketNo(wlaBancDTO.getTssDocketNo());
	    existingWlaBancMaster.setLanIpAddress(wlaBancDTO.getLanIpAddress());
	    existingWlaBancMaster.setAtmIp(wlaBancDTO.getAtmIp());
	    existingWlaBancMaster.setSubnetMask(wlaBancDTO.getSubnetMask());
	    existingWlaBancMaster.setTlsDomainName(wlaBancDTO.getTlsDomainName());
	    existingWlaBancMaster.setTcpPortNo(wlaBancDTO.getTcpPortNo());
	    existingWlaBancMaster.setSwitchDestinationIp1(wlaBancDTO.getSwitchDestinationIp1());
	    existingWlaBancMaster.setLoadingType(wlaBancDTO.getLoadingType());
	    existingWlaBancMaster.setCraName(wlaBancDTO.getCraName());
	    existingWlaBancMaster.setNewLoadingType(wlaBancDTO.getNewLoadingType());
	    existingWlaBancMaster.setFinalRemarks(wlaBancDTO.getFinalRemarks());
	    
	    return wlaBancMasterRepo.save(existingWlaBancMaster);
	}

	@Override
	public void deleteWlaBanc(int wlaBancId) {
		if (wlaBancMasterRepo.existsById(wlaBancId)) {
			wlaBancMasterRepo.deleteById(wlaBancId);
		}

	}

	@Override
	public Optional<WlaBancMaster> getWlaBancMasterById(int wlaBancId) {
		return wlaBancMasterRepo.findById(wlaBancId);
	}

	@Override
	public Iterable<WlaBancMaster> getAllWlaBancMasters() {
		return wlaBancMasterRepo.findAll();
	}

}
