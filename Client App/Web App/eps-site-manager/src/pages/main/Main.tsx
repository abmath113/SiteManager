import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import './Main.css';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import FooterMain from './FooterMain';
import Content from './Content';
import AlertStack, { AlertAttributes } from '../uicomponents/AlertStack';
import { useAuth } from '../../App';

const AppNotification = React.createContext<unknown | null>(null);

function alertReducer(alertState: [], alertAction: any): any {
    switch (alertAction.type) {
        case 'success': {
            return [
                ...alertState,
                {
                    alertType: 'success',
                    alertTitle: 'Success',
                    alertContent: alertAction.message
                }];
        }
        case 'error': {
            return [
                ...alertState,
                {
                    alertType: 'danger',
                    alertTitle: 'Error',
                    alertContent: alertAction.message
                }];
        }
        case 'warn': {
            return [
                ...alertState,
                {
                    alertType: 'warning',
                    alertTitle: 'Warning',
                    alertContent: alertAction.message
                }];
        }
        case 'close-alert': {
            console.log(alertState);
            alertState.splice(alertAction.index, 1);
            console.log(alertState);
            return [
                ...alertState,
            ];
        }
        default: {
            throw new Error(`Unhandled action type: ${alertAction.type}`)
        }
    }
}


function Main() {
    const { name, logout } = useAuth();
    const [alertState, alertDispatch] = React.useReducer(alertReducer, []);
    const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
    const navigate = useNavigate();

    const redirectToLogin = function () {
        logout();
        navigate("/login");
    };

    const [checkedAuth, setCheckedAuth] = useState(false);

useEffect(() => {
    if (!name) {
        navigate("/login");
    } else {
        setCheckedAuth(true);
    }
}, [name, navigate]);

if (!checkedAuth) {
    return <div>Loading...</div>; // You can style it or add a spinner
}


    return (
        <AppNotification.Provider value={alertDispatch}>
            <div className="wrapper">
                {alertState.length > 0 &&
                    <AlertStack
                        stackWidth={40}
                        alertElements={alertState}
                        handleAlertClose={(index: number) => alertDispatch({ type: 'close-alert', index })}
                    />
                }
                <Sidebar isHidden={sidebarCollapsed} />
                <div className="main">
                    <div className="main-content">
                        <div className="navbar-header">
                            <Navbar
                                handleSidebarToggle={() => setSidebarCollapsed(!sidebarCollapsed)}
                                isSidebarHidden={sidebarCollapsed}
                            />

                        </div>
                        <Content />
                        <FooterMain />
                    </div>
                </div>
            </div>
        </AppNotification.Provider>
    );
}

export default Main;
export { AppNotification };
