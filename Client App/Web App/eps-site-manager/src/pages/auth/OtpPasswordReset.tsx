import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import epsLogoNew from '../../resources/images/EpsLogoNew.png';
import configJSON from '../../resources/configuration.json';
import { otpPasswordReset } from '../../services/userauthservice';

const OtpPasswordReset: React.FC = () => {
    const location = useLocation();
    const emailId = location.state?.emailId;
    const navigate = useNavigate();

    const [isSubmitting, setIsSubmitting] = useState(false);

    if (!emailId) {
        // No email passed from previous page
        return (
            <div className="text-center mt-5">
                <h4>Email ID missing</h4>
                <button className="btn btn-primary mt-3" onClick={() => navigate('/login')}>
                    Go to Login
                </button>
            </div>
        );
    }

    const initialValues = {
        otp: '',
        newPassword: '',
        confirmPassword: ''
    };

    const validationSchema = Yup.object().shape({
        otp: Yup.string().required('OTP is required'),
        newPassword: Yup.string().min(6, 'Min 6 chars').required('New Password is required'),
        confirmPassword: Yup.string()
            .oneOf([Yup.ref('newPassword')], 'Passwords must match')
            .required('Confirm Password is required')
    });

    const handleSubmit = async (values: typeof initialValues) => {
        setIsSubmitting(true);

        const formData = new FormData();
        formData.append('emailId', emailId);
        formData.append('userOTP', values.otp);
        formData.append('newPassword', values.newPassword);

        const response = await otpPasswordReset(formData);

        if (response.successstatus === true) {
            alert('Password reset successful!');
            navigate('/login');
        } else {
            alert(response.error || 'Invalid OTP or error resetting password');
        }

        setIsSubmitting(false);
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
                                    <label className="form-label">Enter OTP</label>
                                    <Field name="otp" type="text" className={`form-control ${errors.otp && touched.otp ? 'border-danger' : ''}`} />
                                    <ErrorMessage name="otp" component="div" className="text-danger" />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">New Password</label>
                                    <Field name="newPassword" type="password" className={`form-control ${errors.newPassword && touched.newPassword ? 'border-danger' : ''}`} />
                                    <ErrorMessage name="newPassword" component="div" className="text-danger" />
                                </div>

                                <div className="mb-3">
                                    <label className="form-label">Confirm Password</label>
                                    <Field name="confirmPassword" type="password" className={`form-control ${errors.confirmPassword && touched.confirmPassword ? 'border-danger' : ''}`} />
                                    <ErrorMessage name="confirmPassword" component="div" className="text-danger" />
                                </div>

                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-success" disabled={isSubmitting}>
                                        {isSubmitting && <span className="spinner-border spinner-border-sm me-2"></span>}
                                        Reset Password
                                    </button>
                                </div>
                                <div className="text-center">
                                    <a onClick={() => navigate('/emailverification')} className="text-primary">Re-Enter Email address </a>
                                </div>
                                <div className="text-center">
                                    <a onClick={() => navigate('/login')} className="text-primary">Back to Login</a>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </div>
    );
};

export default OtpPasswordReset;
