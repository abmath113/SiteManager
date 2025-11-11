import React, { useState, useContext, useEffect } from "react";
import axios from 'axios';
import { toast } from 'react-toastify';
import {
  saveRentAgreementMasterDetails,
  getRentAgreementMasterDetails,
  updateRentAgreementMasterDetails,
  uploadAgreementScan
} from "../../../../../services/rentagreementservice";
import { getAllSitesOnlySiteIdAndSiteCode } from "../../../../../services/sitemasterservice";
import { getAllLandlordsOnlyLandlordIdNameAndAccountNo } from "../../../../../services/landlordmasterservice";

interface FormValues {
  siteId: number;
  landlordId: number;
  agreementDate: string;
  rentPayStartDate: string;
  agreementEndDate: string; // Add this
  monthlyRent: number;
  solarPanelRent: number;
  rentAgreementStatus: boolean;
  paymentInterval: number;
  escalationAfterMonths: number; 
  escalationPercent: number;
  file?: File;
  agreementScanExist?: boolean;
}

  interface SiteSelect {
    siteCode: string | number;
    siteId: number;
  }

  interface LandlordSelect {
    name: string | number;
    landlordId: number;
    accountNo: string;
  }

  

  interface RentAgreementAddEditProps {
  onHide: () => void;
  modeIsAdd: boolean;
  agreementId?: number;
  agreementData?: FormValues;
}

  const initialValues: FormValues = {
    agreementDate: new Date().toISOString().split('T')[0],
    rentPayStartDate: new Date().toISOString().split('T')[0],
    agreementEndDate: new Date().toISOString().split('T')[0], // Add this
    monthlyRent: 0,
    solarPanelRent: 0,
    landlordId: 0,
    siteId: 0,
    rentAgreementStatus: true,
    paymentInterval: 1,
    escalationAfterMonths: 0,
    escalationPercent: 0,
    file: undefined,
    agreementScanExist: false,
  };

  interface LandlordData {
    landlordId: number;
    name: string;
  }
  
  interface SiteData {
    siteId: number;
    siteCode: string;
  }

  interface AgreementResponse {
    agreementId: number;
    siteId: SiteData;
    landlordId: LandlordData;
    agreementDate: number[];
    rentPayStartDate: number[];
    agreementEndDate: number[]; // Add this
    monthlyRent: number;
    solarPanelRent: number;
    rentAgreementStatus: boolean;
    paymentInterval: number;
    escalationAfterMonths: number;
    escalationPercent: number;
    agreementScanExist: boolean;
  }

  const RentAgreementAddEdit: React.FC<RentAgreementAddEditProps> = ({
    onHide,
    modeIsAdd,
    agreementId,
  }) => {
    const [formValues, setFormValues] = useState<FormValues>(initialValues);
    const [siteSelect, setSiteSelect] = useState<SiteSelect[]>([]);
    const [landlordSelect, setLandlordSelect] = useState<LandlordSelect[]>([]); 
    const [errors, setErrors] = useState<Partial<Record<keyof FormValues, string>>>({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    
    useEffect(() => {
      const fetchDropdownData = async () => {
        try {
          const [sitesResponse, landlordsResponse] = await Promise.all([
            getAllSitesOnlySiteIdAndSiteCode(),
            getAllLandlordsOnlyLandlordIdNameAndAccountNo()
          ]);
  
          if (sitesResponse.data) {
            setSiteSelect(sitesResponse.data);
          }
          if (landlordsResponse.data) {
            setLandlordSelect(landlordsResponse.data);
          }
        } catch (error) {
          console.error("Error fetching dropdown data:", error);
          toast.error('Error fetching dropdown options');
        } finally {
          setIsLoading(false);
        }
      };
  
      fetchDropdownData();
    }, []);
  
    useEffect(() => {
      const loadExistingData = async () => {
        if (!modeIsAdd && agreementId && !isLoading) {
          try {
            const response = await getRentAgreementMasterDetails(agreementId);
           // console.log('Loaded agreement data:', response.data);
            
            if (response.successstatus && response.data) {
              const  agreementData = response.data as AgreementResponse;
              console.log('Agreement data:', agreementData);
              
              const formatDate = (date: string | number[] | null | undefined): string => {
                if (!date) return '';
                if (Array.isArray(date)) {
                  const [year, month, day] = date;
                  return new Date(year, month - 1, day).toISOString().split('T')[0];
                }
                return date; // already a string like "2025-04-24"
              };
              
              const formattedData: FormValues = {
                agreementDate: formatDate(agreementData.agreementDate),
                rentPayStartDate: formatDate(agreementData.rentPayStartDate),
                agreementEndDate: formatDate(agreementData.agreementEndDate),
                // ...other fields            
                monthlyRent: agreementData.monthlyRent,
                solarPanelRent: agreementData.solarPanelRent,
                // Extract IDs from the nested objects
                landlordId: agreementData.landlordId.landlordId,
                siteId: agreementData.siteId.siteId,
                rentAgreementStatus: agreementData.rentAgreementStatus,
                paymentInterval: agreementData.paymentInterval,
                escalationAfterMonths: agreementData.escalationAfterMonths,
                escalationPercent: agreementData.escalationPercent,
                agreementScanExist: agreementData.agreementScanExist
              };
  
             // console.log('Formatted data:', formattedData);
              
              // Verify that the landlordId and siteId exist in our dropdown options
              const landlordExists = landlordSelect.some(l => l.landlordId === formattedData.landlordId);
              const siteExists = siteSelect.some(s => s.siteId === formattedData.siteId);
  
              if (!landlordExists) {
                console.warn(`Landlord ID ${formattedData.landlordId} not found in dropdown options`);
              }
              if (!siteExists) {
                console.warn(`Site ID ${formattedData.siteId} not found in dropdown options`);
              }
  
              setFormValues(formattedData);
            }
          } catch (error) {
            console.error("Error loading agreement details:", error);
            toast.error( 'Error loading agreement details');
          }
        }
      };
  
      loadExistingData();
    }, [modeIsAdd, agreementId, isLoading, landlordSelect, siteSelect]);

    if (isLoading) {
      return <div>Loading...</div>;
    }

    const validateForm = (values: FormValues): Partial<Record<keyof FormValues, string>> => {
      const errors: Partial<Record<keyof FormValues, string>> = {};
    
      if (!values.landlordId) errors.landlordId = "Landlord ID is required";
      if (!values.siteId) errors.siteId = "Site is required";
      if (!values.agreementDate) errors.agreementDate = "Agreement Date is required";
      if (!values.rentPayStartDate) errors.rentPayStartDate = "Rent Pay Date is required";
      if (!values.agreementEndDate) errors.agreementEndDate = "Agreement End Date is required";
      
      // Validate that end date is after start date
      const startDate = new Date(values.agreementDate);
      const endDate = new Date(values.agreementEndDate);
      const rentDate = new Date(values.rentPayStartDate);
      
      if (endDate <= startDate) {
        errors.agreementEndDate = "End date must be after agreement date";
      }

      //Validation to check the agreement date is before rent pay start date
      if(startDate > rentDate){
        errors.agreementDate = "Rent Pay date should be after agreement date";
      }
    //   if (endDate <= rentPaystartdate) {
    //     errors.agreementDate = "Agreement end date should be after rent pay date";
    // }
    
      
      if(endDate < rentDate){
        errors.agreementDate = "End Date should be after rent pay date";
      }

      if (values.monthlyRent < 0) errors.monthlyRent = "Monthly Rent must be positive";
      if (values.solarPanelRent < 0) errors.solarPanelRent = "Solar Panel Rent must be non-negative";
      if (!values.paymentInterval) errors.paymentInterval = "Payment Interval is required";
      if (!values.escalationAfterMonths || values.escalationAfterMonths < 1) errors.escalationAfterMonths = "Escalation After Months must be at least 1";
      if (!values.escalationPercent || values.escalationPercent < 1 || values.escalationPercent > 100) errors.escalationPercent = "Escalation Percent must be between 1 and 100";
      if (values.file) {
        if (values.file.size > 5 * 1024 * 1024) errors.file = "File size is too large";
        if (!['application/pdf'].includes(values.file.type)) errors.file = "Unsupported file format";
      }
    
      return errors;
    };
            
    const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      setIsSubmitting(true);
    
      const formErrors = validateForm(formValues);
      setErrors(formErrors);
    
      if (Object.keys(formErrors).length === 0) {
        try {
          const { file, ...rentAgreementData } = formValues;
          
          // First attempt to save/update the agreement
          let agreementResponse;
          try {
            if (modeIsAdd) {
              

              agreementResponse = await saveRentAgreementMasterDetails(rentAgreementData);

              console.log('Saving new agreement:', rentAgreementData);
            } else if (agreementId) {
              agreementResponse = await updateRentAgreementMasterDetails({
                ...rentAgreementData,
                agreementId
              });
            }
    
            // Check if the response exists and has a data property
            const isSuccessful = agreementResponse?.data?.successstatus !== false;
            
            // If agreement is saved successfully
            if (isSuccessful) {
              // If no file is attached
              if (!file) {
                toast.success(`Agreement ${modeIsAdd ? 'saved' : 'updated'} successfully`);
                toast.warning('No file attached to the agreement');
                onHide();
                return;
              }
    
              // If file exists, try to upload it
              try {
                const [siteResponse, landlordResponse] = await Promise.all([
                  getAllSitesOnlySiteIdAndSiteCode(),
                  getAllLandlordsOnlyLandlordIdNameAndAccountNo(),
                ]);
    
                const selectedSite = siteResponse.data?.find(
                  (site: any) => site.siteId.toString() === formValues.siteId.toString()
                );
    
                const selectedLandlord = landlordResponse.data?.find(
                  (landlord: any) => landlord.landlordId.toString() === formValues.landlordId.toString()
                );
    
                if (selectedSite && selectedLandlord) {
                  const fileFormData = new FormData();
                  fileFormData.append("file", file);
                  fileFormData.append("siteCode", selectedSite.siteCode);
                  fileFormData.append(
                    "landlordName",
                    selectedLandlord.landlordName || selectedLandlord.name || "UnknownLandlord"
                  );
    
                  const fileResponse = await uploadAgreementScan(fileFormData);
    
                  // Check if the message contains "successfully" to determine if it's actually a success
                  const isFileUploadSuccess = fileResponse.data.successstatus || 
                                            (fileResponse.data.message && 
                                             fileResponse.data.message.toLowerCase().includes('successfully'));
    
                  if (isFileUploadSuccess) {
                    // File upload succeeded
                    toast.success(`Agreement ${modeIsAdd ? 'saved' : 'updated'} successfully with file`);
                  } else {
                    // Only show as warning if it's genuinely an error
                    toast.success(`Agreement ${modeIsAdd ? 'saved' : 'updated'} successfully`);
                    toast.warning('Agreement saved but file upload failed: ' + (fileResponse.data.message || 'Unknown error'));
                  }
                } else {
                  toast.success(`Agreement ${modeIsAdd ? 'saved' : 'updated'} successfully`);
                  toast.warning('Agreement saved but could not upload file: Site or landlord details not found');
                }
              } catch (fileError) {
                toast.success(`Agreement ${modeIsAdd ? 'saved' : 'updated'} successfully`);
                toast.warning('Agreement saved but file upload failed: ' + (axios.isAxiosError(fileError)
                  ? fileError.response?.data?.message || fileError.message
                  : 'Unknown error'));
              }
              onHide();
            } else {
              toast.error(`Failed to ${modeIsAdd ? 'save' : 'update'} agreement`);
            }
            
          } catch (agreementError) {
            console.error("Agreement save/update error:", agreementError);
            const errorMessage = axios.isAxiosError(agreementError)
              ? agreementError.response?.data?.message || 'Failed to process agreement'
              : 'Failed to process agreement';
            toast.error(errorMessage);
          }
        } catch (error) {
          console.error("General error:", error);
          const errorMessage = axios.isAxiosError(error)
            ? error.response?.data?.message || 'An unexpected error occurred'
            : error instanceof Error
              ? error.message
              : "An unexpected error occurred";
          toast.error(errorMessage);
        }
      } else {
        toast.error("Please fix the form errors before submitting");
        console.log('Form validation errors:', formErrors);
      }
    
      setIsSubmitting(false);
    };    
    
                           
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    setFormValues(prev => ({
      ...prev,
      [name]: type === 'number' ? parseFloat(value) : type === 'checkbox' ? (e.target as HTMLInputElement).checked : value,
    }));
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    setFormValues((prevValues) => ({
      ...prevValues,
      [name]: type === "number" ? Number(value) : value, // Convert numeric fields
    }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setFormValues(prev => ({ ...prev, file }));
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="row">
        <div className="col-md-4 mb-3">
          <label htmlFor="landlordId" className="form-label">
            Landlord <span className="text-danger">*</span>
          </label>
          <select
            id="landlordId"
            name="landlordId"
            value={formValues.landlordId}
            onChange={handleInputChange}
            className={`form-select ${errors.landlordId ? "is-invalid" : ""}`}
          >
            <option value="">Select Landlord</option>
            {landlordSelect.map((landlord) => (
              <option key={landlord.landlordId} value={landlord.landlordId}>
                {landlord.name} ({landlord.accountNo})
              </option>
            ))}
          </select>
          {errors.landlordId && <div className="invalid-feedback">{errors.landlordId}</div>}
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="siteId" className="form-label">
            Site <span className="text-danger">*</span>
          </label>
          <select
            id="siteId"
            name="siteId"
            value={formValues.siteId}
            onChange={handleInputChange}
            className={`form-select ${errors.siteId ? "is-invalid" : ""}`}
          >
            <option value="">Select Site</option>
            {siteSelect.map((site) => (
              <option key={site.siteId} value={site.siteId}>
                {site.siteCode}
              </option>
            ))}
          </select>
          {errors.siteId && <div className="invalid-feedback">{errors.siteId}</div>}
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="agreementDate" className="form-label">
            Agreement Date <span className="text-danger">*</span>
          </label>
          <input
            type="date"
            id="agreementDate"
            name="agreementDate"
            value={formValues.agreementDate}
            onChange={handleInputChange}
            className={`form-control ${errors.agreementDate ? "is-invalid" : ""}`}
          />
          {errors.agreementDate && <div className="invalid-feedback">{errors.agreementDate}</div>}
        </div>
      </div>

      <div className="row">
        <div className="col-md-4 mb-3">
          <label htmlFor="rentPayStartDate" className="form-label">
            Rent Pay Date <span className="text-danger">*</span>
          </label>
          <input
            type="date"
            id="rentPayStartDate"
            name="rentPayStartDate"
            value={formValues.rentPayStartDate}
            onChange={handleInputChange}
            className={`form-control ${errors.rentPayStartDate ? "is-invalid" : ""}`}
          />
          {errors.rentPayStartDate && <div className="invalid-feedback">{errors.rentPayStartDate}</div>}
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="monthlyRent" className="form-label">
            Monthly Rent <span className="text-danger">*</span>
          </label>
          <input
            type="number"
            id="monthlyRent"
            name="monthlyRent"
            value={formValues.monthlyRent}
            onChange={handleInputChange}
            className={`form-control ${errors.monthlyRent ? "is-invalid" : ""}`}
          />
          {errors.monthlyRent && <div className="invalid-feedback">{errors.monthlyRent}</div>}
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="solarPanelRent" className="form-label">
            Solar Panel Rent
          </label>
          <input
            type="number"
            id="solarPanelRent"
            name="solarPanelRent"
            value={formValues.solarPanelRent}
            onChange={handleInputChange}
            className={`form-control ${errors.solarPanelRent ? "is-invalid" : ""}`}
          />
          {errors.solarPanelRent && <div className="invalid-feedback">{errors.solarPanelRent}</div>}
        </div>
      </div>

      <div className="row">
        <div className="col-md-4 mb-3">
          <label htmlFor="agreementEndDate" className="form-label">
            Agreement End Date <span className="text-danger">*</span>
          </label>
          <input
            type="date"
            id="agreementEndDate"
            name="agreementEndDate"
            value={formValues.agreementEndDate}
            onChange={handleInputChange}
            className={`form-control ${errors.agreementEndDate ? "is-invalid" : ""}`}
          />
          {errors.agreementEndDate && <div className="invalid-feedback">{errors.agreementEndDate}</div>}
        </div>
        
        <div className="col-md-4 mb-3">
          <label htmlFor="paymentInterval" className="form-label">
            Payment Interval <span className="text-danger">*</span>
          </label>
          <select
            id="paymentInterval"
            name="paymentInterval"
            value={formValues.paymentInterval}
            onChange={handleInputChange}
            className={`form-select ${errors.paymentInterval ? "is-invalid" : ""}`}
          >
            <option value="">Select Interval</option>
            <option value="1">Monthly</option>
            <option value="3">Quarterly</option>  
            <option value="6">Half yearly</option>        
            <option value="0">Rent Free</option>
          </select>
          {errors.paymentInterval && <div className="invalid-feedback">{errors.paymentInterval}</div>}
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="rentAgreementStatus" className="form-label">
            Agreement Status <span className="text-danger">*</span>
          </label>
          <select
  id="rentAgreementStatus"
  name="rentAgreementStatus"
  value={String(formValues.rentAgreementStatus)} 
  onChange={(e) =>
    setFormValues({
      ...formValues,
      rentAgreementStatus: e.target.value === "true"  
    })
  }
  className={`form-select ${errors.rentAgreementStatus ? "is-invalid" : ""}`}
>
  <option value="true">Active</option>
  <option value="false">Inactive</option>
</select>

          {errors.rentAgreementStatus && <div className="invalid-feedback">{errors.rentAgreementStatus}</div>}
        </div>
      </div>

      <div className="row">
        <div className="col-md-4 mb-3">
          <label htmlFor="escalationAfterMonths" className="form-label">
            Escalation After (months) <span className="text-danger">*</span>
          </label>
          <input
            type="number"
            id="escalationAfterMonths"
            name="escalationAfterMonths"
            value={formValues.escalationAfterMonths}
            onChange={handleInputChange}
            className={`form-control ${errors.escalationAfterMonths ? "is-invalid" : ""}`}
          />
          {errors.escalationAfterMonths && <div className="invalid-feedback">{errors.escalationAfterMonths}</div>}
        </div>

        <div className="col-md-4 mb-3">
          <label htmlFor="escalationPercent" className="form-label">
            Escalation Percent <span className="text-danger">*</span>
          </label>
          <input
            type="number"
            id="escalationPercent"
            name="escalationPercent"
            value={formValues.escalationPercent}
            onChange={handleInputChange}
            className={`form-control ${errors.escalationPercent ? "is-invalid" : ""}`}
          />
          {errors.escalationPercent && <div className="invalid-feedback">{errors.escalationPercent}</div>}
        </div>

        <div className="col-md-4 mb-3">
        <label htmlFor="file" className="form-label">
          Upload Agreement File 
        </label>
        <input
          type="file"
          id="file"
          name="file"
          onChange={handleFileChange}
          className={`form-control ${errors.file ? "is-invalid" : ""}`}
          accept=".pdf"
        />
        {errors.file && <div className="invalid-feedback">{errors.file}</div>}
      </div>
      </div>

      <div className="d-flex justify-content-between">
        <button
          type="button"
          className="btn btn-secondary"
          onClick={() => {
            setFormValues(initialValues);
            setErrors({});
            // Reset file input if it exists
            const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
            if (fileInput) {
              fileInput.value = '';
            }
          }}
        >
          Clear
        </button>
        <button type="submit" className="btn btn-success" disabled={isSubmitting}>
          {isSubmitting ? 'Saving...' : modeIsAdd ? 'Save' : 'Update'}
        </button>
      </div>
    </form>
  );
};

export default RentAgreementAddEdit;