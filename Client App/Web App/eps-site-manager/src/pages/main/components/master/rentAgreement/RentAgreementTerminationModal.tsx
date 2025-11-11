import React, { useState } from "react";
import { Modal, Button, Alert, Form } from "react-bootstrap";
import { Formik, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { terminateRentAgreementMaster } from '../../../../../services/rentagreementservice'; 
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface RentAgreementTerminationModalProps {
  show: boolean;
  onHide: () => void;
  agreementId: number | null;
}

interface RentAgreementTerminationData {
  terminationDate: string;
  terminationRemark: string;
}

const RentAgreementTerminationModal: React.FC<RentAgreementTerminationModalProps> = ({
  show,
  onHide,
  agreementId,
}) => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const validationSchema = Yup.object().shape({
    terminationDate: Yup.date()
      .required('Termination date is required')
      .min(new Date().toISOString().split('T')[0], 'Termination date must be today or in the future'),
    terminationRemark: Yup.string()
      .required('Termination remark is required')
      .max(500, 'Remark cannot exceed 500 characters'),
  });

  const handleSubmit = async (values: RentAgreementTerminationData) => {
    if (agreementId === null) return;

    setIsSubmitting(true);
    setErrorMessage(null);

    try {
      const response = await terminateRentAgreementMaster({
        agreementId: agreementId,
        terminationDate: values.terminationDate,
        terminationRemark: values.terminationRemark
      });

      if (response.successstatus) {
        onHide();
        toast.success('Rent agreement terminated successfully!'); // Show success notification
      } else {
        setErrorMessage(response.error?.message || 'Failed to terminate the agreement. Please try again.');
      }
    } catch (error) {
      console.error('Error terminating agreement:', error);
      setErrorMessage('An unexpected error occurred. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <>
      <Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
          <Modal.Title>Terminate Rent Agreement</Modal.Title>
        </Modal.Header>
        <Formik
          initialValues={{ terminationDate: new Date().toISOString().split('T')[0], terminationRemark: '' }}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          {({ handleSubmit, errors, touched, setFieldValue }) => (
            <Form onSubmit={handleSubmit}>
              <Modal.Body>
                {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
                <Form.Group className="mb-3">
                  <Form.Label>Termination Date</Form.Label>
                  <Field
                    name="terminationDate"
                    type="date"
                    className={`form-control ${errors.terminationDate && touched.terminationDate ? 'is-invalid' : ''}`}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setFieldValue('terminationDate', e.target.value)} // Simplified
                  />
                  <ErrorMessage name="terminationDate" component="div" className="invalid-feedback" />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Termination Remark</Form.Label>
                  <Field
                    name="terminationRemark"
                    as="textarea"
                    rows={3}
                    className={`form-control ${errors.terminationRemark && touched.terminationRemark ? 'is-invalid' : ''}`}
                  />
                  <ErrorMessage name="terminationRemark" component="div" className="invalid-feedback" />
                </Form.Group>
              </Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                  Cancel
                </Button>
                <Button variant="danger" type="submit" disabled={isSubmitting}>
                  {isSubmitting ? 'Terminating...' : 'Terminate Agreement'}
                </Button>
              </Modal.Footer>
            </Form>
          )}
        </Formik>
      </Modal>
      {/* ToastContainer added here */}
      <ToastContainer />
    </>
  );
};

export default RentAgreementTerminationModal;
