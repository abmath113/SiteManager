import { Form, Field, ErrorMessage, Formik } from "formik";
import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { Modal } from "react-bootstrap";
import { saveWLAMasterDetails, getWLAMasterDetailsById, updateWLAMasterDetails } from "../../../../../services/wlaservice";
import * as Yup from "yup";

interface WlaAddEditProps {
    show: boolean;
    modeIsAdd: boolean;
    wlaBancId?: number;
    onHide: () => void;
}

// Display field names used in the UI
const displayFields = [
    'EPS Site Code', 'Site Type', 'Site Subtype', 'Site Status', 'Length', 'Old ATM ID', 'ATM ID', 'Location Name', 'District', 'Address', 'State', 'City', 'PIN Code', 'Location Type', 'Location Class', 'Latitude', 'Longitude', 'Landlord Name', 'Landlord Number', 'CM Name', 'CM Contact Number', 'RM Name', 'RM Contact Number', 'State Head Name', 'State Head Number', 'Master Franchise Name', 'Franchise Contact Number', 'Franchise Mail ID', 'TIS Category', 'TIS Vendor Name', 'TIS Rate', 'TIS DO Status', 'TIS Order Date', 'TIS Start Date', 'TIS End Date', 'TIS Status', 'AC Make', 'AC Capacity', 'AC Status', 'UPS Vendor', 'UPS Order Date', 'UPS Backup Capacity (Hrs)', 'Number of Batteries', 'UPS Delivery Date', 'UPS Installation Date', 'UPS Installation Status', 'IT Servo Stabilizer', 'VSAT Type', 'VSAT Vendor', 'VSAT Order Date', 'VSAT Delivery Date', 'VSAT ID', 'VSAT Installation Status', 'VSAT Commissioning Date', 'HUGS Subnet', 'Site Branding Vendor', 'Site Branding Type', 'Site Branding Order Date', 'Signage Installation Date', 'Signage Installation Status', 'ATM Branding Vendor', 'ATM Branding Order Date', 'ATM Branding Status', 'ATM Branding Complete Date', 'ATM Vendor', 'ATM Model', 'ATM Order Date', 'ATM Delivery Date', 'ATM Machine Serial Number', 'ATM Machine Status', 'Technical Live Date', 'Cash Live Date', 'EJ Docket Number', 'TSS Docket Number', 'LAN IP Address', 'ATM IP', 'Subnet Mask', 'TLS Domain Name', 'TCP Port Number', 'Switch Destination IP 1', 'Loading Type', 'CRA Name', 'New Loading Type', 'Final Remarks', 'S&G Lock Number', 'e-Surveillance Delivery Date', 'e-Surveillance Commissioning Date', 'e-Surveillance Vendor Name', 'e-Surveillance Order Date', 'e-Surveillance Status'
];

// Corresponding camelCase property names in the same order as displayFields
const camelCaseFields = [
    'epsSiteCode', 'siteType', 'siteSubtype', 'siteStatus', 'len', 'oldAtmId', 'atmId', 'locationName', 'district', 'address', 'state', 'city', 'pinCode', 'locationType', 'locationClass', 'latitude', 'longitude', 'landlordName', 'landlordNumber', 'cmName', 'cmContactNumber', 'rmName', 'rmContactNumber', 'stateHeadName', 'stateHeadNumber', 'masterFranchiseName', 'franchiseContactNumber', 'franchiseMailId', 'tisCategory', 'tisVendorName', 'tisRate', 'tisDoStatus', 'tisOrderDate', 'tisStartDate', 'tisEndDate', 'tisStatus', 'acMake', 'acCapacity', 'acStatus', 'upsVendor', 'upsOrderDate', 'upsBackupCapacityHrs', 'noOfBatteries', 'upsDeliveryDate', 'upsInstallationDate', 'upsInstallationStatus', 'itServoStabilizer', 'vsatType', 'vsatVendor', 'vsatOrderDate', 'vsatDeliveryDate', 'vsatId', 'vsatInstallationStatus', 'vsatCommissioningDate', 'hugsSubnet', 'siteBrandingVendor', 'siteBrandingType', 'siteBrandingOrderDate', 'signageInstallationDate', 'signageInstallationStatus', 'atmBrandingVendor', 'atmBrandingOrderDate', 'atmBrandingStatus', 'atmBrandingCompleteDate', 'atmVendor', 'atmModel', 'atmOrderDate', 'atmDeliveryDate', 'atmMachineSerialNo', 'atmMachineStatus', 'techLiveDate', 'cashLiveDate', 'ejDocketNumber', 'tssDocketNo', 'lanIpAddress', 'atmIp', 'subnetMask', 'tlsDomainName', 'tcpPortNo', 'switchDestinationIp1', 'loadingType', 'craName', 'newLoadingType', 'finalRemarks', 'sandGLockNo', 'esurveillanceDeliveryDate', 'esurveillanceCommissioningDate', 'esurveillanceVendorName', 'esurveillanceOrderDate', 'esurveillanceStatus'
];

// Create a mapping between the display field names and camelCase field names
const fieldMapping: Record<string, string> = {};
displayFields.forEach((displayName, index) => {
    fieldMapping[displayName] = camelCaseFields[index];
});

interface FormValues {
    epsSiteCode: string,
    siteType: string,
    siteSubtype: string,
    siteStatus: string,
    len: string,
    oldAtmId: string,
    atmId: string,
    locationName: string,
    district: string,
    address: string,
    state: string,
    city: string,
    pinCode: string,
    locationType: string,
    locationClass: string,
    latitude: string,
    longitude: string,
    landlordName: string,
    landlordNumber: string,
    cmName: string,
    cmContactNumber: string,
    rmName: string,
    rmContactNumber: string,
    stateHeadName: string,
    stateHeadNumber: string,
    masterFranchiseName: string,
    franchiseContactNumber: string,
    franchiseMailId: string,
    tisCategory: string,
    tisVendorName: string,
    tisRate: string,
    tisDoStatus: string,
    tisOrderDate: string,
    tisStartDate: string,
    tisEndDate: string,
    tisStatus: string,
    acMake: string,
    acCapacity: string,
    acStatus: string,
    upsVendor: string,
    upsOrderDate: string,
    upsBackupCapacityHrs: string,
    noOfBatteries: string,
    upsDeliveryDate: string,
    upsInstallationDate: string,
    upsInstallationStatus: string,
    itServoStabilizer: string,
    vsatType: string,
    vsatVendor: string,
    vsatOrderDate: string,
    vsatDeliveryDate: string,
    vsatId: string,
    vsatInstallationStatus: string,
    vsatCommissioningDate: string,
    hugsSubnet: string,
    siteBrandingVendor: string,
    siteBrandingType: string,
    siteBrandingOrderDate: string,
    signageInstallationDate: string,
    signageInstallationStatus: string,
    atmBrandingVendor: string,
    atmBrandingOrderDate: string,
    atmBrandingStatus: string,
    atmBrandingCompleteDate: string,
    atmVendor: string,
    atmModel: string,
    atmOrderDate: string,
    atmDeliveryDate: string,
    atmMachineSerialNo: string,
    atmMachineStatus: string,
    techLiveDate: string,
    cashLiveDate: string,
    ejDocketNumber: string,
    tssDocketNo: string,
    lanIpAddress: string,
    atmIp: string,
    subnetMask: string,
    tlsDomainName: string,
    tcpPortNo: string,
    switchDestinationIp1: string,
    loadingType: string,
    craName: string,
    newLoadingType: string,
    finalRemarks: string,
    sandGLockNo: string,
    esurveillanceDeliveryDate: string,
    esurveillanceCommissioningDate: string,
    esurveillanceVendorName: string,
    esurveillanceOrderDate: string,
    esurveillanceStatus: string
}

const initialValues: FormValues = {
    epsSiteCode: '',
    siteType: '',
    siteSubtype: '',
    siteStatus: '',
    len: '',
    oldAtmId: '',
    atmId: '',
    locationName: '',
    district: '',
    address: '',
    state: '',
    city: '',
    pinCode: '',
    locationType: '',
    locationClass: '',
    latitude: '',
    longitude: '',
    landlordName: '',
    landlordNumber: '',
    cmName: '',
    cmContactNumber: '',
    rmName: '',
    rmContactNumber: '',
    stateHeadName: '',
    stateHeadNumber: '',
    masterFranchiseName: '',
    franchiseContactNumber: '',
    franchiseMailId: '',
    tisCategory: '',
    tisVendorName: '',
    tisRate: '',
    tisDoStatus: '',
    tisOrderDate: '',
    tisStartDate: '',
    tisEndDate: '',
    tisStatus: '',
    acMake: '',
    acCapacity: '',
    acStatus: '',
    upsVendor: '',
    upsOrderDate: '',
    upsBackupCapacityHrs: '',
    noOfBatteries: '',
    upsDeliveryDate: '',
    upsInstallationDate: '',
    upsInstallationStatus: '',
    itServoStabilizer: '',
    vsatType: '',
    vsatVendor: '',
    vsatOrderDate: '',
    vsatDeliveryDate: '',
    vsatId: '',
    vsatInstallationStatus: '',
    vsatCommissioningDate: '',
    hugsSubnet: '',
    siteBrandingVendor: '',
    siteBrandingType: '',
    siteBrandingOrderDate: '',
    signageInstallationDate: '',
    signageInstallationStatus: '',
    atmBrandingVendor: '',
    atmBrandingOrderDate: '',
    atmBrandingStatus: '',
    atmBrandingCompleteDate: '',
    atmVendor: '',
    atmModel: '',
    atmOrderDate: '',
    atmDeliveryDate: '',
    atmMachineSerialNo: '',
    atmMachineStatus: '',
    techLiveDate: '',
    cashLiveDate: '',
    ejDocketNumber: '',
    tssDocketNo: '',
    lanIpAddress: '',
    atmIp: '',
    subnetMask: '',
    tlsDomainName: '',
    tcpPortNo: '',
    switchDestinationIp1: '',
    loadingType: '',
    craName: '',
    newLoadingType: '',
    finalRemarks: '',
    sandGLockNo: '',
    esurveillanceDeliveryDate: '',
    esurveillanceCommissioningDate: '',
    esurveillanceVendorName: '',
    esurveillanceOrderDate: '',
    esurveillanceStatus: '',
};

// Basic validation schema
const validationSchema = Yup.object().shape({
   // epsSiteCode: Yup.string().required('EPS Site Code is required'),

});

const WlaAddEdit = ({ show, modeIsAdd, wlaBancId, onHide }: WlaAddEditProps) => {
    const [formValues, setFormValues] = useState<FormValues>(initialValues);
    const [isLoading, setIsLoading] = useState(false);
    const [isFormSubmitting, setIsFormSubmitting] = useState(false);
    const [channelManagerSelect, setChannelManagerSelect] = useState<{ channelManagerId: number, channelManagerName: string }[]>([]);
    const [bankCodeSelect, setBankCodeSelect] = useState<{ bankId: number, bankCode: string }[]>([]);

    useEffect(() => {
        // If editing mode, fetch WLA data
        if (!modeIsAdd && wlaBancId) {
            fetchWlaData();
        }
    }, [modeIsAdd, wlaBancId]);

    const fetchWlaData = async () => {
        setIsLoading(true);
        try {
            if (wlaBancId === undefined) {
                console.error('wlaBancId is not defined');
                return;
            }

            const response = await getWLAMasterDetailsById(wlaBancId);
            if (response && response.data) {
                // Set the form values from the API response
                setFormValues(response.data);
            } else {
                toast.error('Failed to fetch WLA data');
            }
        } catch (error) {
            console.error('Error fetching WLA data:', error);
            toast.error('Error fetching WLA data');
        } finally {
            setIsLoading(false);
        }
    };

    const handleSubmit = async (values: FormValues, { setSubmitting }: any) => {
        console.log('Form submitted with values:', values);
        setIsFormSubmitting(true);

        try {
            let response;

            if (modeIsAdd) {
                // For creating new WLA Master details
                response = await saveWLAMasterDetails(values);
                if (response && response.successstatus) {
                    toast.success('WLA Master details saved successfully');
                    onHide();
                } else {
                    toast.error(response?.error || 'Failed to save WLA Master details');
                }
            } else {
                // For updating existing WLA Master details
                const updatedData = {
                    ...values,
                    wlaBancId: wlaBancId
                };
                response = await updateWLAMasterDetails(updatedData);
                if (response && response.successstatus) {
                    toast.success('WLA Master details updated successfully');
                    onHide();
                } else {
                    toast.error(response?.error || 'Failed to update WLA Master details');
                }
            }
        } catch (error) {
            console.error('Error submitting form:', error);
            toast.error('An error occurred while processing your request');
        } finally {
            setIsFormSubmitting(false);
            setSubmitting(false);
        }
    };

    return (
        <Formik
            initialValues={formValues}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
            enableReinitialize
        >
            {({ errors, touched, resetForm }) => (
                <Form>
                    <div className="row">
                        {displayFields.map((displayName, index) => {
                            const fieldName = camelCaseFields[index];

                            return (
                                <div className="col-md-4 mb-3" key={index}>
                                    <label htmlFor={fieldName} className="form-label">
                                        {displayName}
                                    </label>
                                    <Field
                                        type="text"
                                        name={fieldName}
                                        id={fieldName}
                                        className={`form-control ${errors[fieldName as keyof FormValues] &&
                                            touched[fieldName as keyof FormValues] ? 'is-invalid' : ''}`}
                                        placeholder={`Enter ${displayName}`}
                                    />
                                    <ErrorMessage name={fieldName} component="div" className="invalid-feedback" />
                                </div>
                            );
                        })}
                    </div>
                    <div className="d-flex justify-content-between mt-4">
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={() => {
                                resetForm();
                                setFormValues(initialValues);
                            }}
                        >
                            Clear
                        </button>
                        <button
                            type="submit"
                            className="btn btn-success"
                            disabled={isFormSubmitting}
                        >
                            {isFormSubmitting && (
                                <span
                                    className="spinner-grow spinner-grow-sm me-2"
                                    role="status"
                                    aria-hidden="true"
                                />
                            )}
                            {modeIsAdd ? "Save" : "Update"}
                        </button>
                    </div>
                </Form>
            )}
        </Formik>
    );
};

export default WlaAddEdit;