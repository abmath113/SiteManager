import { DateTime } from 'luxon';
import configJSON from '../../resources/configuration.json';
import { useState } from 'react';

function FooterMain() {
    const [showContactDetails, setShowContactDetails] = useState(false);

    const handleClick = () => {
        setShowContactDetails((prev) => !prev); // Toggle visibility on click
    };

    return (
        <footer className="footer bg-light" style={{ padding: '0.1rem 0', position: 'relative' }}>
            <div className="container-fluid pt-1">
                <div className="row text-muted align-items-center">
                    <div className="col-md-4 col-sm-12 text-md-start text-sm-center text-center">
                        <p className="mb-0 main-footer-label">
                            <span>Copyright © {DateTime.now().toFormat('y')} </span>
                        </p>
                    </div>
                    <div className="col-md-4 col-sm-12 text-md-center text-sm-center">
                        <p className="mb-0 main-footer-label">
                            <span>{configJSON.versionText}&nbsp;{configJSON.appVersion}</span>
                        </p>
                    </div>
                    <div className="col-md-4 col-sm-12 text-md-end text-sm-center text-center">
                        <p 
                            className="mb-0 main-footer-label" 
                            onClick={handleClick} // Only handle click event
                            style={{ position: 'relative', cursor: 'pointer' }}
                        >
                            <span>Contact Us</span>
                            {showContactDetails && (
                               <div className="contact-details shadow-sm rounded border p-3 bg-white text-start"
                               style={{
                                   position: 'absolute',
                                   zIndex: 1000,
                                   bottom: '100%',
                                   left: '50%',
                                   transform: 'translateX(-50%)',
                                   marginBottom: '0.5rem',
                                   width: 'max-content',
                                   minWidth: '250px',
                                   maxWidth: '90%',
                                   fontSize: '0.9rem',
                               }}>
                              <strong>Abhishek Thorat:</strong><br />
                              <a href="mailto:abhishek.thorat@electronicpay.in" className="text-decoration-none text-primary">
                                  abhishek.thorat@electronicpay.in
                              </a>
                              <br />
                              <strong>Anushka Karhadkar:</strong><br />
                              <a href="mailto:anushka.karhadkar@electronicpay.in" className="text-decoration-none text-primary">
                                  anushka.karhadkar@electronicpay.in
                              </a>

                              <br />
                              
                          </div>
                          
                            )}
                        </p>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default FooterMain;