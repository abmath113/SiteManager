import React, { useRef, useState, useEffect } from "react";
import { Formik, Field, Form, ErrorMessage, FormikProps } from "formik";
import * as Yup from "yup";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { ServerResponse } from "../../../../../services/apiBaseAxios";
import {
  getChannelManagerById,
  saveChannelManager,
  updateChannelManager
} from "../../../../../services/channelmanagerservice";

interface FormValues {
  channelManagerName: string;
  emailId: string;
  phoneNo: string;
  status: boolean
  
}

interface ChannelManagerAddEditProps {
  onHide: () => void;
  modeIsAdd: boolean;
  channelManagerId?: number;
}

const initialValues: FormValues = {
  channelManagerName: "",
  emailId: "",
  phoneNo: "",
  status: false,
};

const ChannelManagerAddEdit: React.FC<ChannelManagerAddEditProps> = ({
  onHide,
  modeIsAdd,
  channelManagerId,
}) => {
  const formikRef = useRef<FormikProps<FormValues>>(null);
  const [isFormSubmitting, setIsFormSubmitting] = useState(false);
  const [formValues, setFormValues] = useState<FormValues>(initialValues);

  const validationSchema = Yup.object({
    channelManagerName: Yup.string().required("Name is required"),
    emailId: Yup.string()
      .email("Invalid email format")
      .required("Email is required"),
    phoneNo: Yup.string()
      .required("Phone is required")
      .matches(/^\d{10}$/, "Phone must be 10 digits")
  });

  // load channel manager details if in edit mode
  useEffect(() => {
    console.log("channelManagerId", channelManagerId);
    if (!modeIsAdd && channelManagerId) {  
      const fetchChannelManagerDetails = async () => {
      console.log("channelManagerId", channelManagerId);
        try {
          const channelManagerDetailsFromAPI = await getChannelManagerById(channelManagerId);
          console.log("channelManagerDetailsFromAPI", channelManagerDetailsFromAPI);
          if (!channelManagerDetailsFromAPI.successstatus) {
            toast.error(channelManagerDetailsFromAPI.error?.message || "Failed to fetch details");
            return;
          }
          const apiData = channelManagerDetailsFromAPI.data;
          console.log("apiData", apiData);


          if (apiData) {

            setFormValues({
              channelManagerName: apiData.channelManagerName || "",
              emailId: apiData.emailId || "",
              phoneNo: apiData.phoneNo || "",
              status: apiData.status ?? true,
            });
          }
        } catch (error) {
          console.error("Fetch error:", error);
          toast.error("Error fetching channel manager details");
        }
      };
      fetchChannelManagerDetails();
    }
  },[modeIsAdd, channelManagerId]);

  const handleSubmit = async (values: FormValues) => {
    setIsFormSubmitting(true);
    console.log("values", values);
    try {
      const apiResponse = modeIsAdd
        ? await saveChannelManager(values)
        : await updateChannelManager({ ...values, channelManagerId: channelManagerId });

      handleSubmitResponse(apiResponse);
    } catch (error) {
      console.error("Submission error:", error);
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
    const successMessage = `Channel Manager "${response.data?.channelManagerName}" ${action} successfully`;
    toast.success(successMessage);
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
                  Name <span className="text-danger">*</span>
                </label>
                <Field
                  name="channelManagerName"
                  placeholder="Enter manager name"
                  className={`form-control ${
                    errors.channelManagerName && touched.channelManagerName ? "is-invalid" : ""
                  }`}
                />
                <ErrorMessage
                  name="channelManagerName"
                  component="div"
                  className="invalid-feedback"
                />
              </div>

              <div className="col-md-12 mb-3">
                <label className="form-label">
                  Email <span className="text-danger">*</span>
                </label>
                <Field
                  name="emailId"
                  type="email"
                  placeholder="Enter email address"
                  className={`form-control ${
                    errors.emailId && touched.emailId ? "is-invalid" : ""
                  }`}
                />
                <ErrorMessage
                  name="emailId"
                  component="div"
                  className="invalid-feedback"
                />
              </div>

              <div className="col-md-12 mb-3">
                <label className="form-label">
                  Phone <span className="text-danger">*</span>
                </label>
                <Field
                  name="phoneNo"
                  placeholder="Enter phone number"
                  className={`form-control ${
                    errors.phoneNo && touched.phoneNo ? "is-invalid" : ""
                  }`}
                />
                <ErrorMessage
                  name="phoneNo"
                  component="div"
                  className="invalid-feedback"
                />
              </div>
            </div>
            <div className="col-md-12 mb-3">
  <label className="form-label">Status</label>
  <Field name="status">
    {({ field, form }: any) => (
      <div className="form-check form-switch">
        <input
          type="checkbox"
          className="form-check-input"
          id="statusSwitch"
          {...field}
          checked={field.value}
          onChange={() => form.setFieldValue("status", !field.value)}
        />
        <label className="form-check-label" htmlFor="statusSwitch">
          {field.value ? "Active" : "Inactive"}
        </label>
      </div>
    )}
  </Field>
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

export default ChannelManagerAddEdit;