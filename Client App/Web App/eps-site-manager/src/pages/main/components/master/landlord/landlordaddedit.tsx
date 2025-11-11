import React, { useRef, useState, useEffect } from 'react';
import { Formik, Field, Form, ErrorMessage, FormikProps } from 'formik';
import * as Yup from 'yup';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { ServerResponse } from "../../../../../services/apiBaseAxios";
import {
  saveLandlordMasterDetails,
  getLandlordMasterDetails,
  updateLandlordMasterDetails
} from '../../../../../services/landlordmasterservice';

interface FormValues {
  name: string;
  mobileNo: string;
  ifscCode: string;
  accountNo: string;
  pan: string;
  aadharNo: string;
  status: boolean ;
  gst: boolean | string;
  address: string;
  beneficiaryName: string;
}

interface LandlordAddEditProps {
  onHide: () => void;
  modeIsAdd: boolean;
  landlordId: number;
}

const initialValues: FormValues = {
  name: '',
  mobileNo: '',
  ifscCode: '',
  accountNo: '',
  pan: '',
  aadharNo: '',
  status: false,
  gst:false,
  address: '',
  beneficiaryName: '',
};

const LandlordAddEdit: React.FC<LandlordAddEditProps> = ({ onHide, modeIsAdd, landlordId }) => {
  const formikRef = useRef<FormikProps<FormValues>>(null);
  const [isFormSubmitting, setIsFormSubmitting] = useState(false);
  const [formValues, setFormValues] = useState<FormValues>(initialValues);

  const validationSchema = Yup.object({
    name: Yup.string().required('Name is required'),
    mobileNo: Yup.string().required('Mobile number is required').matches(/^\d{10}$/, 'Mobile number must be 10 digits'),
    ifscCode: Yup.string().required('IFSC code is required'),
    accountNo: Yup.string().required('Account number is required'),
    pan: Yup.string().required('PAN is required').matches(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/, 'Invalid PAN format'),
    aadharNo: Yup.string()
    .transform((value) => value.replace(/\s+/g, '')) // remove all spaces
    .matches(/^\d{12}$/, 'Aadhar number must be 12 digits')
    .required('Aadhar number is required'),
    status: Yup.boolean().required('Status is required'),
    

    address: Yup.string().required('Address is required'),
    beneficiaryName: Yup.string().required('Beneficiary Name is required'),
  });

  useEffect(() => {
    if (!modeIsAdd) {
      let getLandlordMasterFn = async () => {
        try {
          const landlordMasterGetDetailsFromAPI = await getLandlordMasterDetails(landlordId);

          if (landlordMasterGetDetailsFromAPI.successstatus === undefined) {
            setIsFormSubmitting(false);
            toast.error('Invalid Response');
            return;
          }

          if (landlordMasterGetDetailsFromAPI.successstatus === false) {
            setIsFormSubmitting(false);
            toast.error(landlordMasterGetDetailsFromAPI.error.message);
            return;
          }

          const apiData = landlordMasterGetDetailsFromAPI.data;
          if (apiData !== undefined && apiData !== null) {
            console.log(apiData);
            setFormValues({
              name: apiData.name,
              mobileNo: apiData.mobileNo,
              ifscCode: apiData.ifscCode,
              accountNo: apiData.accountNo,
              pan: apiData.pan,
              aadharNo: apiData.aadharNo,
              status: apiData.status,
              gst:apiData.isGST,
              address: apiData.address,
              beneficiaryName: apiData.beneficiaryName,
            });
          } else {
            toast.error('Failed to fetch Landlord Master details');
          }
        } catch (error) {
          console.error('Error fetching landlord details:', error);
          toast.error('An error occurred while fetching landlord details');
        }
      };
      getLandlordMasterFn();
    }
  }, [modeIsAdd, landlordId]);

  const saveLandlordMasterFn = async (formData: FormValues) => {
    try {
      const landlordMasterSaveResultFromAPI = await saveLandlordMasterDetails(JSON.stringify(formData));
      handleSubmitResponse(landlordMasterSaveResultFromAPI);
    } catch (error) {
      console.error('Error saving landlord details:', error);
      toast.error('An error occurred while saving landlord details');
    } finally {
      setIsFormSubmitting(false);
    }
  };
//   const saveLandlordMasterFn = async (formData: FormValues) => {
//   try {
//     const payload = {
//       ...formData,
//       gst: formData.gst // convert string to boolean
//     };
//     const result = await saveLandlordMasterDetails(JSON.stringify(payload));
//     handleSubmitResponse(result);
//   } catch (error) {
//     console.error(error);
//     toast.error('An error occurred while saving landlord details');
//   }
// };


  const updateLandlordMasterFn = async (formData: FormValues) => {
    try {
      const formDataWithLandlordId = { ...formData, landlordId: landlordId };
      const landlordMasterUpdateResultFromAPI = await updateLandlordMasterDetails(JSON.stringify(formDataWithLandlordId));
      handleSubmitResponse(landlordMasterUpdateResultFromAPI);
    } catch (error) {
      console.error('Error updating landlord details:', error);
      toast.error('An error occurred while updating landlord details');
    } finally {
      setIsFormSubmitting(false);
    }
  };




  const handleSubmit = (formData: FormValues) => {
    setIsFormSubmitting(true);
    
    if (modeIsAdd) {
      saveLandlordMasterFn(formData);
    } else {
      updateLandlordMasterFn(formData);
    }
  };



  const handleSubmitResponse = (landlordMasterSubmitResponse: ServerResponse) => {
    if (landlordMasterSubmitResponse.successstatus === undefined) {
      toast.error('Invalid Response');
      return;
    }

    if (landlordMasterSubmitResponse.successstatus === false) {
      const errorMessage = landlordMasterSubmitResponse.error?.message || 'An error occurred';
      toast.error(errorMessage);
      return;
    }

    const apiData = landlordMasterSubmitResponse.data;
    if (apiData) {
      const name = apiData.name || 'N/A';
      const action = modeIsAdd ? 'added' : 'updated';
      const successNotificationMessage = `Name: "${name}" ${action} successfully`;
      
      toast.success(successNotificationMessage);
      onHide();
    } else {
      const errorNotificationMessage = `Failed to ${modeIsAdd ? 'Save' : 'Update'} Landlord Master`;
      toast.error(errorNotificationMessage);
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
            <div className="col-md-4 mb-3">
              <label className="form-label">
                Name <span className="text-danger">*</span>
              </label>
              <Field
                name="name"
                type="text"
                placeholder="Enter Name"
                className={
                  'form-control' +
                  (errors.name && touched.name ? ' is-invalid' : '')
                }
              />
              <ErrorMessage
                name="name"
                component="div"
                className="invalid-feedback"
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">
                Beneficiary Name <span className="text-danger">*</span>
              </label>
              <Field
                name="beneficiaryName"
                type="text"
                placeholder="Enter Beneficiary Name"
                className={
                  'form-control' +
                  (errors.beneficiaryName && touched.beneficiaryName ? ' is-invalid' : '')
                }
              />
              <ErrorMessage
                name="beneficiaryName"
                component="div"
                className="invalid-feedback"
              />
            </div>

            <div className="col-md-4 mb-3">
              <label className="form-label">
                Mobile No <span className="text-danger">*</span>
              </label>
              <Field
                name="mobileNo"
                type="text"
                placeholder="Enter Mobile No"
                className={
                  'form-control' +
                  (errors.mobileNo && touched.mobileNo ? ' is-invalid' : '')
                }
              />
              <ErrorMessage
                name="mobileNo"
                component="div"
                className="invalid-feedback"
              />
            </div>
           
          </div>

          <div className="row">
            <div className="col-md-4 mb-3">
                <label className="form-label">
                  IFSC Code <span className="text-danger">*</span>
                </label>
                <Field
                  name="ifscCode"
                  type="text"
                  placeholder="Enter IFSC Code"
                  className={
                    'form-control' +
                    (errors.ifscCode && touched.ifscCode ? ' is-invalid' : '')
                  }
                />
                <ErrorMessage
                  name="ifscCode"
                  component="div"
                  className="invalid-feedback"
                />
              </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">
                Account No <span className="text-danger">*</span>
              </label>
              <Field
                name="accountNo"
                type="text"
                placeholder="Enter Account No"
                className={
                  'form-control' +
                  (errors.accountNo && touched.accountNo ? ' is-invalid' : '')
                }
              />
              <ErrorMessage
                name="accountNo"
                component="div"
                className="invalid-feedback"
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">
                PAN <span className="text-danger">*</span>
              </label>
              <Field
                name="pan"
                type="text"
                placeholder="Enter PAN"
                className={
                  'form-control' +
                  (errors.pan && touched.pan ? ' is-invalid' : '')
                }
              />
              <ErrorMessage
                name="pan"
                component="div"
                className="invalid-feedback"
              />
            </div>

            <div className="col-md-4 mb-3">
              <label className="form-label">
                Aadhar No 
              </label>
              <Field
                name="aadharNo"
                type="text"
                placeholder="Enter Aadhar No"
                className={
                  'form-control' +
                  (errors.aadharNo && touched.aadharNo ? ' is-invalid' : '')
                }
              />
              <ErrorMessage
                name="aadharNo"
                component="div"
                className="invalid-feedback"
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">
                Status <span className="text-danger">*</span>
              </label>
              <Field
                as="select"
                name="status"
                className={
                  'form-control' +
                  (errors.status && touched.status ? ' is-invalid' : '')
                }
              >
                <option value="">Select status</option>
                <option value="true">Active</option>
                <option value="false">Inactive</option>
              </Field>
              <ErrorMessage
                name="status"
                component="div"
                className="invalid-feedback"
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">
                GST <span className="text-danger">*</span>
              </label>
              <Field
  as="select"
  name="isGST"
  
  className={'form-control' + (errors.gst && touched.gst ? ' is-invalid' : '')}
>
  <option value="">Select GST</option>
  <option value="true">Yes</option>
  <option value="false">No</option>
</Field>

              <ErrorMessage
                name="gst"
                component="div"
                className="invalid-feedback"
              />
            </div>
          </div>

          <div className="row">
            
            <div className="col-md-8 mb-3">
              <label className="form-label">
                Address
              </label>
              <Field
                name="address"
                type="text"
                placeholder="Enter Address"
                className="form-control"
              />
            </div>
          </div>

          <div className="row">
            
          </div>

          <div className="d-flex justify-content-between">
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
            <button type="submit" className="btn btn-success" disabled={isFormSubmitting}>
              {isFormSubmitting &&
                <span className="spinner-grow spinner-grow-sm text-warning"
                  role="status" aria-hidden="true"
                  style={{ marginRight: '5px' }}>
                </span>
              }
              Submit
            </button>
          </div>
        </Form>
      )}
    </Formik>
  );
}

export default LandlordAddEdit;

