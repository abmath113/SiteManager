package com.eps.sitemanager.model.wla;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abhishek.thorat
 */
@Entity
@Table(name = "wla_banc_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WlaBancMaster implements Serializable {

    private static final long serialVersionUID = 699433864193496226L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wla_banc_id")
	private int wlaBancId;

    @Column(name = "eps_site_code")
    private String epsSiteCode;

    @Column(name = "site_type")
    private String siteType;

    @Column(name = "site_subtype")
    private String siteSubtype;

    @Column(name = "site_status")
    private String siteStatus;

    @Column(name = "len")
    private String len;

    @Column(name = "old_atm_id")
    private String oldAtmId;

    @Column(name = "atm_id")
    private String atmId;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "district")
    private String district;

    @Column(name = "address")
    private String address;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "location_class")
    private String locationClass;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "landlord_name")
    private String landlordName;

    @Column(name = "landlord_number")
    private String landlordNumber;

    @Column(name = "cm_name")
    private String cmName;

    @Column(name = "cm_contact_number")
    private String cmContactNumber;

    @Column(name = "rm_name")
    private String rmName;

    @Column(name = "rm_contact_number")
    private String rmContactNumber;

    @Column(name = "state_head_name")
    private String stateHeadName;

    @Column(name = "state_head_number")
    private String stateHeadNumber;

    @Column(name = "master_franchise_name")
    private String masterFranchiseName;

    @Column(name = "franchise_contact_number")
    private String franchiseContactNumber;

    @Column(name = "franchise_mail_id")
    private String franchiseMailId;

    @Column(name = "tis_category")
    private String tisCategory;

    @Column(name = "tis_vendor_name")
    private String tisVendorName;
    
    @Column(name = "tis_rate")
    private String tisRate;
    
    @Column(name = "tis_do_status")
    private String tisDoStatus;

    @Column(name = "tis_order_date")
    private String tisOrderDate;

    @Column(name = "tis_start_date")
    private String tisStartDate;

    @Column(name = "tis_end_date")
    private String tisEndDate;

    @Column(name = "tis_status")
    private String tisStatus;

    @Column(name = "ac_make")
    private String acMake;

    @Column(name = "ac_capacity")
    private String acCapacity;

    @Column(name = "ac_status")
    private String acStatus;

    @Column(name = "ups_vendor")
    private String upsVendor;

    @Column(name = "ups_order_date")
    private String upsOrderDate;

    @Column(name = "ups_backup_capacity_hrs")
    private String upsBackupCapacityHrs;

    @Column(name = "no_of_batteries")
    private String noOfBatteries;

    @Column(name = "ups_delivery_date")
    private String upsDeliveryDate;

    @Column(name = "ups_installation_date")
    private String upsInstallationDate;

    @Column(name = "ups_installation_status")
    private String upsInstallationStatus;

    @Column(name = "it_servo_stabilizer")
    private String itServoStabilizer;

    @Column(name = "vsat_type")
    private String vsatType;

    @Column(name = "vsat_vendor")
    private String vsatVendor;

    @Column(name = "vsat_order_date")
    private String vsatOrderDate;

    @Column(name = "vsat_delivery_date")
    private String vsatDeliveryDate;

    @Column(name = "vsat_id")
    private String vsatId;

    @Column(name = "vsat_installation_status")
    private String vsatInstallationStatus;

    @Column(name = "vsat_commissioning_date")
    private String vsatCommissioningDate;

    @Column(name = "hugs_subnet")
    private String hugsSubnet;

    @Column(name = "site_branding_vendor")
    private String siteBrandingVendor;

    @Column(name = "site_branding_type")
    private String siteBrandingType;

    @Column(name = "site_branding_order_date")
    private String siteBrandingOrderDate;

    @Column(name = "signage_installation_date")
    private String signageInstallationDate;

    @Column(name = "signage_installation_status")
    private String signageInstallationStatus;

    @Column(name = "atm_branding_vendor")
    private String atmBrandingVendor;

    @Column(name = "atm_branding_order_date")
    private String atmBrandingOrderDate;

    @Column(name = "atm_branding_status")
    private String atmBrandingStatus;

    @Column(name = "atm_branding_complete_date")
    private String atmBrandingCompleteDate;

    @Column(name = "e_surveillance_vendor_name")
    private String eSurveillanceVendorName;

    @Column(name = "e_surveillance_order_date")
    private String eSurveillanceOrderDate;

    @Column(name = "e_surveillance_delivery_date")
    private String eSurveillanceDeliveryDate;

    @Column(name = "e_surveillance_status")
    private String eSurveillanceStatus;

    @Column(name = "e_surveillance_commissioning_date")
    private String eSurveillanceCommissioningDate;

    @Column(name = "atm_vendor")
    private String atmVendor;

    @Column(name = "atm_model")
    private String atmModel;

    @Column(name = "atm_order_date")
    private String atmOrderDate;

    @Column(name = "atm_delivery_date")
    private String atmDeliveryDate;

    @Column(name = "atm_machine_serial_no")
    private String atmMachineSerialNo;

    @Column(name = "s_and_g_lock_no")
    private String sAndGLockNo;

    @Column(name = "atm_machine_status")
    private String atmMachineStatus;

    @Column(name = "tech_live_date")
    private String techLiveDate;

    @Column(name = "cash_live_date")
    private String cashLiveDate;

    @Column(name = "ej_docket_number")
    private String ejDocketNumber;

    @Column(name = "tss_docket_no")
    private String tssDocketNo;

    @Column(name = "lan_ip_address")
    private String lanIpAddress;

    @Column(name = "atm_ip")
    private String atmIp;

    @Column(name = "subnet_mask")
    private String subnetMask;

    @Column(name = "tls_domain_name")
    private String tlsDomainName;

    @Column(name = "tcp_port_no")
    private String tcpPortNo;

    @Column(name = "switch_destination_ip_1")
    private String switchDestinationIp1;

    @Column(name = "loading_type")
    private String loadingType;

    @Column(name = "cra_name")
    private String craName;

    @Column(name = "new_loading_type")
    private String newLoadingType;

    @Column(name = "final_remarks")
    private String finalRemarks;

	@Override
	public String toString() {
		return "WlaBancMaster [wlaBancId=" + wlaBancId + ", epsSiteCode=" + epsSiteCode + ", siteType=" + siteType
				+ ", siteSubtype=" + siteSubtype + ", siteStatus=" + siteStatus + ", len=" + len + ", oldAtmId="
				+ oldAtmId + ", atmId=" + atmId + ", locationName=" + locationName + ", district=" + district
				+ ", address=" + address + ", state=" + state + ", city=" + city + ", pinCode=" + pinCode
				+ ", locationType=" + locationType + ", locationClass=" + locationClass + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", landlordName=" + landlordName + ", landlordNumber=" + landlordNumber
				+ ", cmName=" + cmName + ", cmContactNumber=" + cmContactNumber + ", rmName=" + rmName
				+ ", rmContactNumber=" + rmContactNumber + ", stateHeadName=" + stateHeadName + ", stateHeadNumber="
				+ stateHeadNumber + ", masterFranchiseName=" + masterFranchiseName + ", franchiseContactNumber="
				+ franchiseContactNumber + ", franchiseMailId=" + franchiseMailId + ", tisCategory=" + tisCategory
				+ ", tisVendorName=" + tisVendorName + ", tisRate=" + tisRate + ", tisDoStatus=" + tisDoStatus
				+ ", tisOrderDate=" + tisOrderDate + ", tisStartDate=" + tisStartDate + ", tisEndDate=" + tisEndDate
				+ ", tisStatus=" + tisStatus + ", acMake=" + acMake + ", acCapacity=" + acCapacity + ", acStatus="
				+ acStatus + ", upsVendor=" + upsVendor + ", upsOrderDate=" + upsOrderDate + ", upsBackupCapacityHrs="
				+ upsBackupCapacityHrs + ", noOfBatteries=" + noOfBatteries + ", upsDeliveryDate=" + upsDeliveryDate
				+ ", upsInstallationDate=" + upsInstallationDate + ", upsInstallationStatus=" + upsInstallationStatus
				+ ", itServoStabilizer=" + itServoStabilizer + ", vsatType=" + vsatType + ", vsatVendor=" + vsatVendor
				+ ", vsatOrderDate=" + vsatOrderDate + ", vsatDeliveryDate=" + vsatDeliveryDate + ", vsatId=" + vsatId
				+ ", vsatInstallationStatus=" + vsatInstallationStatus + ", vsatCommissioningDate="
				+ vsatCommissioningDate + ", hugsSubnet=" + hugsSubnet + ", siteBrandingVendor=" + siteBrandingVendor
				+ ", siteBrandingType=" + siteBrandingType + ", siteBrandingOrderDate=" + siteBrandingOrderDate
				+ ", signageInstallationDate=" + signageInstallationDate + ", signageInstallationStatus="
				+ signageInstallationStatus + ", atmBrandingVendor=" + atmBrandingVendor + ", atmBrandingOrderDate="
				+ atmBrandingOrderDate + ", atmBrandingStatus=" + atmBrandingStatus + ", atmBrandingCompleteDate="
				+ atmBrandingCompleteDate + ", eSurveillanceVendorName=" + eSurveillanceVendorName
				+ ", eSurveillanceOrderDate=" + eSurveillanceOrderDate + ", eSurveillanceDeliveryDate="
				+ eSurveillanceDeliveryDate + ", eSurveillanceStatus=" + eSurveillanceStatus
				+ ", eSurveillanceCommissioningDate=" + eSurveillanceCommissioningDate + ", atmVendor=" + atmVendor
				+ ", atmModel=" + atmModel + ", atmOrderDate=" + atmOrderDate + ", atmDeliveryDate=" + atmDeliveryDate
				+ ", atmMachineSerialNo=" + atmMachineSerialNo + ", sAndGLockNo=" + sAndGLockNo + ", atmMachineStatus="
				+ atmMachineStatus + ", techLiveDate=" + techLiveDate + ", cashLiveDate=" + cashLiveDate
				+ ", ejDocketNumber=" + ejDocketNumber + ", tssDocketNo=" + tssDocketNo + ", lanIpAddress="
				+ lanIpAddress + ", atmIp=" + atmIp + ", subnetMask=" + subnetMask + ", tlsDomainName=" + tlsDomainName
				+ ", tcpPortNo=" + tcpPortNo + ", switchDestinationIp1=" + switchDestinationIp1 + ", loadingType="
				+ loadingType + ", craName=" + craName + ", newLoadingType=" + newLoadingType + ", finalRemarks="
				+ finalRemarks + "]";
	}


}