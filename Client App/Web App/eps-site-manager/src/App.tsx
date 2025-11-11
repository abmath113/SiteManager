import React, { useState, createContext, useContext, useEffect } from 'react';
import { createBrowserRouter, RouterProvider, RouteObject } from "react-router-dom";
import { motion } from 'framer-motion';
import { ToastContainer } from 'react-toastify'; // Import ToastContainer
import 'react-toastify/dist/ReactToastify.css'; // Import Toastify CSS
// AdminKit (required)
import "./adminkit/scss/app.scss";
import "./adminkit/js/modules/bootstrap";
import "./adminkit/js/modules/theme";
import "./adminkit/js/modules/feather";
import feather from "feather-icons";

// Forms
import "./adminkit/js/modules/flatpickr";
import './App.css';

import Main from './pages/main/Main';
import Page404 from './pages/error/Page404';
import Page500 from './pages/error/Page500';
import SiteSearch from './pages/main/components/master/site/sitesearch';
import LandlordSearch from './pages/main/components/master/landlord/landlordsearch';
import RentAgreementSearch from './pages/main/components/master/rentAgreement/rentagreementsearch';
import GenerateRent from './pages/main/components/transcations/GenerateRent';
import ChannelManagerAndBankSearch from './pages/main/components/master/channelManager/channelmanagerbanksearch';
import RentHistory from './pages/main/components/transcations/RentHistory';
import SiteHistory from './pages/main/components/transcations/SiteHistory';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import EmailVerification from './pages/auth/EmailVerification';
import OtpPasswordReset from './pages/auth/OtpPasswordReset';
import WlaBulkUpload from './pages/main/components/master/Wla/WlaBulkUpload';
import Chatbot from './pages/main/components/Chatbot';

interface AuthContextType {
    name: string | null;
    email: string | null;
    login: (name: string, email: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

const routes: RouteObject[] = [
    {
        path: "/",
        element: <Main />,
        errorElement: <Page500 />,
        children: [
            { path: "sitesearch", element: <SiteSearch /> },
            { path: "landlordsearch", element: <LandlordSearch /> },
            { path: "rentagreementsearch", element: <RentAgreementSearch /> },
            { path: "channelmanagerandbanksearch", element: <ChannelManagerAndBankSearch /> },
            { path: "wlabulkupload", element: <WlaBulkUpload /> },
            { path: "generaterent", element: <GenerateRent /> },
            { path: "renthistory", element: <RentHistory /> },
            { path: "sitehistory", element: <SiteHistory /> },
            { path: "sitemanagerchat", element: <Chatbot /> },

        ],
    },
    {
        path: "/login",
        element: <Login />,
        errorElement: <Page404 />,
    },
    {
        path: "/register", // New route for registration
        element: <Register />,
        errorElement: <Page404 />,
    },
    {
        path: "/emailverification", // New route for email verification
        element: <EmailVerification />,
        errorElement: <Page404 />,
    },

    {
        path: "/otp-password-reset", // New route for email verification
        element: <OtpPasswordReset />,
        errorElement: <Page404 />,
    }
];


const router = createBrowserRouter(routes, { basename: "/EPSSiteManager" });

function App() {
    const [name, setName] = useState<string | null>(null);
    const [email, setEmail] = useState<string | null>(null);

    const authValue = {
        name,
        email,
        login: (name: string, email: string) => {
            setName(name);
            setEmail(email);
            localStorage.setItem('currentUser', JSON.stringify({ name, email }));
        },
        logout: () => {
            setName(null);
            setEmail(null);
            localStorage.removeItem('currentUser');
            localStorage.removeItem('token');
            localStorage.removeItem('logintime');
        },
    };
    // Check localStorage for saved user on app load
    useEffect(() => {
        const savedUser = localStorage.getItem('currentUser');
        if (savedUser) {
            const { name, email } = JSON.parse(savedUser);
            setName(name);
            setEmail(email);
        }
        feather.replace();
    }, []);


    return (
        <AuthContext.Provider value={authValue}>
            <div>
                <RouterProvider router={router} />
                <ToastContainer
                    position="top-right"
                    autoClose={3000}
                    hideProgressBar={false}
                    newestOnTop
                    closeOnClick
                    pauseOnFocusLoss={false}
                    draggable
                    pauseOnHover
                />
            </div>
        </AuthContext.Provider>
    );

}

export default App;