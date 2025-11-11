import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { useAuth } from '../../App';
import { login as loginservice } from "../../services/userauthservice";
import { DateTime } from 'luxon';
import epsLogoNew from '../../resources/images/EpsLogoNew.png';
import configJSON from '../../resources/configuration.json';
import { PiEyeClosedDuotone } from "react-icons/pi";
import { PiEyeDuotone } from "react-icons/pi";

interface LoginFormValues {
    emailId: string;
    password: string;
}

interface LoginProps {
    onLoginSuccess?: (name: string, email: string) => void;
}

const Login: React.FC<LoginProps> = ({ onLoginSuccess = (emailId: string) => { } }) => {
    const [isValidating, setIsValidating] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const navigate = useNavigate();
    const { login } = useAuth();

    const initialValues: LoginFormValues = {
        emailId: '',
        password: ''
    };

    const validationSchema = Yup.object().shape({
        emailId: Yup.string().email('Invalid email').max(50).trim().lowercase().required('Email is required'),
        password: Yup.string().required('Password is required')
    });

    const handleSubmit = async (values: LoginFormValues) => {
        setIsValidating(true);
        var LoginFormData = new FormData();
        LoginFormData.append('emailId', values.emailId);
        LoginFormData.append('password', values.password);

        let loginresponse = await loginservice(LoginFormData);

        if (loginresponse.successstatus === true) {
            console.log(process.env.NODE_ENV);
            console.log('Login response:', loginresponse);
            login(loginresponse.data.firstName, loginresponse.data.emailId);
            navigate('/');
            // Store user data in local storage

            onLoginSuccess(loginresponse.data.firstName, loginresponse.data.emailId);
            setIsValidating(false);
            localStorage.setItem('token', loginresponse.data.token);
            localStorage.setItem('logintime', DateTime.now().toFormat('dd MMM yyyy HH:mm:ss'));
        } else {
            alert('Invalid credentials or network issue. Please try again.');
            setIsValidating(false);
        }
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    // useEffect(() => {
    //     const user = localStorage.getItem('currentUser');
    //     if (user) {
    //         navigate('/');
    //     }
    // }, [navigate]);

    // Redirect to register page
    const redirectToRegister = () => {
        navigate('/register');
    };
    const redirectToPasswordReset = () => {
        navigate('/emailverification');
    }


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
                    <img src={epsLogoNew} alt="Brand Logo" style={{ width: '220px' }} />
                    <h2 className="mt-2" style={{ fontSize: "1.5rem", color: "#333" }}>
                        <b style={{ color: "blue" }}>ep</b>{configJSON.appShort}
                    </h2>
                </div>
                <div className="login-right p-4" style={{ width: '50%', animation: 'slideInRight 0.5s ease-in-out' }}>
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {({ errors, touched }) => (
                            <Form>
                                <div className="mb-3">
                                    <label className="form-label">Email</label>
                                    <Field
                                        name="emailId"
                                        type="text"
                                        className={`form-control ${errors.emailId && touched.emailId ? 'border-danger' : touched.emailId ? 'border-success' : ''}`}
                                    />
                                    <ErrorMessage name="emailId" component="div" className="text-danger" />
                                </div>

                                <div className="mb-3">
                                    <div className="d-flex justify-content-between">
                                        <label className="form-label">Password</label>

                                    </div>
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

                                <div className="d-grid mb-3">
                                    <button type="submit" className="btn btn-primary" disabled={isValidating}>
                                        {isValidating && <span className="spinner-border spinner-border-sm me-2"></span>}
                                        Sign In
                                    </button>
                                </div>
                                <div className="text-center  pb-2">
                                    <button
                                        type="button"
                                        className="btn btn-link text-primary p-0"
                                        onClick={redirectToPasswordReset}
                                    >
                                        Forgot password?.....
                                    </button>
                                </div>

                                <div className="text-center">

                                    <button
                                        type="button"
                                        className="btn btn-link text-primary p-0"
                                        onClick={redirectToRegister}
                                    >
                                        New User? Register here
                                    </button>
                                </div>

                            </Form>
                        )}
                    </Formik>
                </div>

            </div>
            <div >
                <p >..
                </p>

            </div>
        </div>
    );
};

export default Login;