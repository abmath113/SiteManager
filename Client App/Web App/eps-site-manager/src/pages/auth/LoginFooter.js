
import { DateTime } from 'luxon';

import configJSON from '../../resources/configuration.json';

function LoginFooter() {
    return (
        <div className="col-sm-12 col-md-12 col-lg-12 mx-auto d-table h-100">
            <div className="d-table-cell align-bottom">
                <div className="container-fluid">
                    <div className="row page-footer text-muted">
                        <div className="col-md-6 col-sm-12 text-md-start text-sm-center text-center">
                            <p className="mb-0 page-footer-label">
                                <span>Copyright © {DateTime.now().toFormat('y')} </span>
                                <a className="App-link" href={configJSON.companyWebsiteLink} target="_blank" rel="noopener noreferrer">
                                    {configJSON.companyName}
                                </a>
                            </p>
                        </div>
                        <div className="col-md-6 col-sm-12 text-md-end text-sm-center text-center">
                            <p className="mb-0 page-footer-label">
                                <span>{configJSON.versionText}&nbsp;{configJSON.appVersion}</span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LoginFooter;