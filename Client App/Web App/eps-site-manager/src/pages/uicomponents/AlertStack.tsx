import React, { useEffect } from "react";
import { BiError } from 'react-icons/bi';
import { BsCheck2Circle } from "react-icons/bs";
import { MdOutlineCancel } from "react-icons/md";
import Alert from './Alert';

export type AlertAttributes = {
    iconClass?: any;
    alertTitleColor?: string;
}

function AlertStack({ stackWidth, alertElements, handleAlertClose }: any) : JSX.Element {

    // Function to determine styles based on alert type
    function alertStyles(alertType: string) : AlertAttributes {
        switch (alertType) {
            case 'danger':
                return ({
                    iconClass: <MdOutlineCancel/>,
                    alertTitleColor: '#ff3300'
                });
            case 'success':
                return ({
                    iconClass: <BsCheck2Circle/>,
                    alertTitleColor: '#339966'
                });
            case 'warning':
                return ({
                    iconClass: <BiError/>,
                    alertTitleColor: '#ff6600'
                });
            default:
                return ({});
        }
    }

    // Setup useEffect to handle automatic alert dismissal
    useEffect(() => {
        const timers = alertElements.map((_: any, index: number) => {
            // Set a timeout to close each alert after 5 seconds
            return setTimeout(() => {
                handleAlertClose(index);
            }, 3000);
        });

        // Cleanup timers when component unmounts or alerts change
        return () => {
            timers.forEach(clearTimeout);
        };
    }, [alertElements, handleAlertClose]);

    // Iterate over alertElements to create individual alerts
    const alertContents = alertElements.map((alertElement : any, index : any) : JSX.Element => {
        let alertStyl = alertStyles(alertElement.alertType);

        return (
            <Alert 
                key={index}
                id={index}
                alertType={alertElement.alertType}
                iconComponent={alertStyl.iconClass}
                alertTitleColor={alertStyl.alertTitleColor}
                alertTitle={alertElement.alertTitle}
                alertContent={alertElement.alertContent} 
                handleAlertCloseBtn={() => handleAlertClose(index)}
            />
        );
    });

    return (
        <div className="position-fixed top-0 end-0 p-2" 
        style={{ height:'100vh', zIndex: '1060', width: stackWidth + '%', overflowX:'auto' }}>
            {alertContents}
        </div>
    );
}

export default AlertStack;
