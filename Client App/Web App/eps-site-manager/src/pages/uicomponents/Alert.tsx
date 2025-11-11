
import React from 'react';

type AlertProps = {
    id: string;
    alertType: string;
    iconComponent?: any;
    alertTitleColor?: string;
    alertTitle: string;
    alertContent: string;
    handleAlertCloseBtn: ()  => void;
}


function Alert({ id, alertType, iconComponent, alertTitleColor, alertTitle, alertContent, handleAlertCloseBtn }:AlertProps ) : JSX.Element | null {
    return (
        <div className={`card alert alert-${alertType} alert-dismissible fade show p-2`} id={'DivAlert'+id}
             role="alert" style={{ marginBottom: '5px' }}>
            <div className="card-title">
                <h5>
                    <span className="align-middle" style={{ color: alertTitleColor }}>
                     {iconComponent} <span className="align-middle">{alertTitle}</span>
                    </span>
                    <button type="button" className="btn-close btn-sm align-middle pb-2" 
                            id={'BtnAlert'+id}
                            onClick={handleAlertCloseBtn}
                            aria-label="Close">
                    </button>
                </h5>
                <hr className="m-0"></hr>
            </div>
            <div className="card-body content-card-body py-0">
                <p className="card-text">{alertContent}</p>
            </div>
        </div>
    );
}

export default Alert;
