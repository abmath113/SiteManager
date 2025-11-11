import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { register } from '../../services/userauthservice';
import { PiEyeClosedDuotone } from "react-icons/pi";
import { PiEyeDuotone } from "react-icons/pi";
import epsLogoNew from '../../resources/images/EpsLogoNew.png';
import configJSON from '../../resources/configuration.json';

interface RegisterFormValues {
    firstName: string;
    lastName: string;
    emailId: string;
    password: string;
    confirmPassword: string;
}

const Register: React.FC = () => {
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const navigate = useNavigate();
    
    const initialValues: RegisterFormValues = {
        firstName: '',
        lastName: '',
        emailId: '',
        password: '',
        confirmPassword: ''
    };

    const validationSchema = Yup.object().shape({
        firstName: Yup.string().max(50, 'First name is too long').required('First name is required'),
        lastName: Yup.string().max(50, 'Last name is too long').required('Last name is required'),
        emailId: Yup.string().email('Invalid email').max(50).trim().lowercase().required('Email is required'),
        password: Yup.string().min(6, 'Password must be at least 6 characters').required('Password is required'),
        confirmPassword: Yup.string()
            .oneOf([Yup.ref('password')], 'Passwords must match')
            .required('Confirm password is required')
    });

    const handleSubmit = async (values: RegisterFormValues) => {
        setIsSubmitting(true);
        let registerFormData = new FormData();
        registerFormData.append('firstName', values.firstName);
        registerFormData.append('lastName', values.lastName);
        registerFormData.append('emailId', values.emailId);
        registerFormData.append('password', values.password);
        
        let registerResponse = await register(registerFormData); // Call API
        console.log(registerResponse);

        if (registerResponse.successstatus === true) {
            alert('Verification email sent to ' + values.emailId+ '. Please verify your email to login.');
            navigate('/login'); // Redirect to login page
        } else {
            alert('Registration failed: ' + registerResponse.error);
        }
        setIsSubmitting(false);
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const redirectToLogin = () => {
        navigate('/login');
    };

    return (
        <div className="register-page d-flex align-items-center justify-content-center min-vh-100" style={{ backgroundColor: 'rgb(105, 158, 207)' }}>
            <div className="register-container d-flex shadow p-3 rounded-lg" style={{ maxWidth: '800px', width: '100%', height: 'auto', borderRadius: '30px', backgroundColor: 'rgba(255, 255, 255, 0.55)' }}>
                <div className="register-left p-4 d-flex flex-column justify-content-center align-items-center" style={{ width: '30%' }}>
                    <img src={epsLogoNew} alt="Brand Logo" style={{ width: '80px' }} />
                    <h2 className="mt-2" style={{ fontSize: "1.5rem", color: "#333" }}>
                        <b style={{ color: "blue" }}>ep</b>{configJSON.appShort}
                    </h2>
                </div>
                <div className="register-right p-4" style={{ width: '70%' }}>
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {({ errors, touched }) => (
                            <Form>
                                <div className="row">
                                    {/* First Name */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label">First Name</label>
                                        <Field
                                            name="firstName"
                                            type="text"
                                            className={`form-control ${errors.firstName && touched.firstName ? 'border-danger' : touched.firstName ? 'border-success' : ''}`}
                                        />
                                        <ErrorMessage name="firstName" component="div" className="text-danger" />
                                    </div>

                                    {/* Last Name */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label">Last Name</label>
                                        <Field
                                            name="lastName"
                                            type="text"
                                            className={`form-control ${errors.lastName && touched.lastName ? 'border-danger' : touched.lastName ? 'border-success' : ''}`}
                                        />
                                        <ErrorMessage name="lastName" component="div" className="text-danger" />
                                    </div>

                                    {/* Email */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label">Email</label>
                                        <Field
                                            name="emailId"
                                            type="text"
                                            className={`form-control ${errors.emailId && touched.emailId ? 'border-danger' : touched.emailId ? 'border-success' : ''}`}
                                        />
                                        <ErrorMessage name="emailId" component="div" className="text-danger" />
                                    </div>

                                    {/* Password */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label">Password</label>
                                        <div className="position-relative">
                                            <Field
                                                name="password"
                                                type={showPassword ? "text" : "password"}
                                                className={`form-control ${errors.password && touched.password ? 'border-danger' : touched.password ? 'border-success' : ''}`}
                                            />
                                            <button 
                                                type="button" 
                                                className="btn position-absolute" 
                                                onClick={togglePasswordVisibility}
                                                style={{ 
                                                    border: 'none', 
                                                    background: 'transparent',
                                                    right: '8px',
                                                    top: '30%',
                                                    transform: 'translateY(-50%)',
                                                    padding: '0',
                                                    zIndex: '5'
                                                }}
                                            >
                                                {showPassword ? 
                                                    <PiEyeDuotone size={20} /> : 
                                                    <PiEyeClosedDuotone size={20} />
                                                }
                                            </button>
                                        </div>
                                        <ErrorMessage name="password" component="div" className="text-danger" />
                                    </div>

                                    {/* Confirm Password */}
                                    <div className="col-md-6 mb-3">
                                        <label className="form-label">Confirm Password</label>
                                        <div className="position-relative">
                                            <Field
                                                name="confirmPassword"
                                                type={showConfirmPassword ? "text" : "password"}
                                                className={`form-control ${errors.confirmPassword && touched.confirmPassword ? 'border-danger' : touched.confirmPassword ? 'border-success' : ''}`}
                                            />
                                            <button 
                                                type="button" 
                                                className="btn position-absolute" 
                                                onClick={toggleConfirmPasswordVisibility}
                                                style={{ 
                                                    border: 'none', 
                                                    background: 'transparent',
                                                    right: '8px',
                                                    top: '30%',
                                                    transform: 'translateY(-50%)',
                                                    padding: '0',
                                                    zIndex: '5'
                                                }}
                                            >
                                                {showConfirmPassword ? 
                                                    <PiEyeDuotone size={20} /> : 
                                                    <PiEyeClosedDuotone size={20} />
                                                }
                                            </button>
                                        </div>
                                        <ErrorMessage name="confirmPassword" component="div" className="text-danger" />
                                    </div>
                                </div>

                                {/* Submit Button */}
                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                                        {isSubmitting && <span className="spinner-border spinner-border-sm me-2"></span>}
                                        Register
                                    </button>
                                </div>

                                {/* Redirect to Login */}
                                <div className="text-center">
                                    <a href="#" className="text-primary" onClick={redirectToLogin}>Already have an account? Login</a>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </div>
    );
};

export default Register;