import React, { useState } from 'react';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import epsLogoNew from '../../resources/images/EpsLogoNew.png';
import configJSON from '../../resources/configuration.json';
import { sendPasswordResetEmail } from '../../services/userauthservice';



const EmailVerification: React.FC = () => {
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const initialValues = { emailId: '' };

    const validationSchema = Yup.object().shape({
        emailId: Yup.string().email('Invalid email').max(50).trim().lowercase().required('Email is required')
    });
    const redirectToLogin = () => { 
        navigate('/login');
    }
    const handleSubmit = async (values: { emailId: string }) => {
        setIsSubmitting(true);

        var formData = new FormData();
        formData.append('emailId', values.emailId);
        let resetemiailresponse = await sendPasswordResetEmail(formData);

        if (resetemiailresponse.successstatus === true) {
            alert(`Password reset link sent to ${values.emailId}`);
            navigate('/otp-password-reset', { state: { emailId: values.emailId } });
            setIsSubmitting(false);
            return;
        }
        else {
            alert('Invalid email id');
            setIsSubmitting(false);
        }

       
    };

    return (
        <div className="login-page d-flex align-items-center justify-content-center min-vh-100" style={{ backgroundColor: 'rgb(105, 158, 207)' }}>
           <div
                className="login-container d-flex shadow p-3 rounded-lg"
                style={{
                    width: '700px',
                    borderRadius: '30px',
                    backgroundColor: 'rgba(255, 255, 255, 0.55)',
                }}
            >
                <div className="login-left p-4 d-flex flex-column justify-content-center align-items-center" style={{ width: '300px' }}>
                    <img src={epsLogoNew} alt="Brand Logo" style={{ width: '80px' }} />
                    <h2 className="mt-2" style={{ fontSize: "1.5rem", color: "#333" }}>
                        <b style={{ color: "blue" }}>ep</b>{configJSON.appShort}
                    </h2>
                </div>
                <div className="login-right p-4" style={{ width: '50%', animation: 'slideInRight 0.5s ease-in-out' }}>
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {({ errors, touched }) => (
                            <Form>
                                <div className="mb-3">
                                    <label className="form-label">Enter your Email</label>
                                    <Field
                                        name="emailId"
                                        type="text"
                                        className={`form-control ${errors.emailId && touched.emailId ? 'border-danger' : touched.emailId ? 'border-success' : ''}`}
                                    />
                                    <ErrorMessage name="emailId" component="div" className="text-danger" />
                                </div>

                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                                        {isSubmitting && <span className="spinner-border spinner-border-sm me-2"></span>}
                                        Send Reset Link
                                    </button>
                                </div>

                                <div className="text-center">
                                    <a onClick={redirectToLogin} className="text-primary">Back to Login</a>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </div>
    );
};

export default EmailVerification;
