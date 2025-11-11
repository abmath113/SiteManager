import React, { useRef, useState, useEffect } from "react";
import { Formik, Field, Form, ErrorMessage, FormikProps } from "formik";
import * as Yup from "yup";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { ServerResponse } from "../../../../../services/apiBaseAxios";
import {
  getBankMasterById,
  saveBankMaster,
  updateBankMaster
} from "../../../../../services/bankmasterservice";

interface BankMasterFormValues {
  bankName: string;
  bankCode?: string;
}

interface BankAddEditProps {
  onHide: () => void;
  modeIsAdd: boolean;
  bankId?: number;
}

const initialValues: BankMasterFormValues = {
  bankName: "",
  bankCode: ""
};

const BankAddEdit: React.FC<BankAddEditProps> = ({
  onHide,
  modeIsAdd,
  bankId
}) => {
  const formikRef = useRef<FormikProps<BankMasterFormValues>>(null);
  const [isFormSubmitting, setIsFormSubmitting] = useState(false);
  const [formValues, setFormValues] = useState<BankMasterFormValues>(initialValues);

  const validationSchema = Yup.object({
    bankName: Yup.string().required("Bank name is required"),
    bankCode: Yup.string().nullable()
  });

  useEffect(() => {
    if (!modeIsAdd && bankId) {
      const fetchBankMasterDetails = async () => {
        try {
          const bankMasterDetailsFromAPI = await getBankMasterById(bankId);
          if (!bankMasterDetailsFromAPI.successstatus) {
            toast.error(bankMasterDetailsFromAPI.error?.message || "Failed to fetch details");
            return;
          }
          const apiData = bankMasterDetailsFromAPI.data;
          if (apiData) {
            setFormValues({
              bankName: apiData.bankName || "",
              bankCode: apiData.bankCode || ""
            });
          }
        } catch (error) {
          toast.error("Error fetching bank details");
        }
      };
      fetchBankMasterDetails();
    }
  }, [modeIsAdd, bankId]);

  const handleSubmit = async (values: BankMasterFormValues) => {
    setIsFormSubmitting(true);
    try {
      const apiResponse = modeIsAdd
        ? await saveBankMaster(values)
        : await updateBankMaster({ ...values, bankId });

      handleSubmitResponse(apiResponse);
    } catch {
      toast.error("An error occurred during submission");
    } finally {
      setIsFormSubmitting(false);
    }
  };

  const handleSubmitResponse = (response: ServerResponse) => {
    if (!response.successstatus) {
      toast.error(response.error?.message || "Operation failed");
      return;
    }
    const action = modeIsAdd ? "added" : "updated";
    toast.success(`Bank "${response.data?.bankName}" ${action} successfully`);
    onHide();
  };

  return (
    <div className="container-fluid" style={{ maxWidth: "500px" }}>
      <Formik
        innerRef={formikRef}
        initialValues={formValues}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
        enableReinitialize
      >
        {({ errors, touched, resetForm }) => (
          <Form>
            <div className="row">
              <div className="col-md-12 mb-3">
                <label className="form-label">
                  Bank Name <span className="text-danger">*</span>
                </label>
                <Field
                  name="bankName"
                  placeholder="Enter bank name"
                  className={`form-control ${
                    errors.bankName && touched.bankName ? "is-invalid" : ""
                  }`}
                />
                <ErrorMessage
                  name="bankName"
                  component="div"
                  className="invalid-feedback"
                />
              </div>

              <div className="col-md-12 mb-3">
                <label className="form-label">Bank Code</label>
                <Field
                  name="bankCode"
                  placeholder="Enter bank code"
                  className={`form-control ${
                    errors.bankCode && touched.bankCode ? "is-invalid" : ""
                  }`}
                />
                <ErrorMessage
                  name="bankCode"
                  component="div"
                  className="invalid-feedback"
                />
              </div>
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
    </div>
  );
};

export default BankAddEdit;
