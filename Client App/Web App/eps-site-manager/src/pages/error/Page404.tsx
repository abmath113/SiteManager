

import React from "react";
import { Link } from "react-router-dom";

function Page404() : JSX.Element {

    return (
        <main className="d-flex w-100">
            <div className="container d-flex flex-column">
                <div className="row vh-100">
                    <div className="col-sm-2 col-md-4 col-lg-5 mx-auto d-table h-100">
                        <div className="d-table-cell align-middle">
                            <div className="text-center my-5">
                                <h1 className="h2 errorHeader">Oops! Page not found</h1>
                            </div>
                            <div className="card">
                                <div className="card-body">
                                    <div className="container">
                                        <div className="row">
                                            <div className="col-sm-5">
                                                <span className="errorCode error404">404</span>
                                            </div>
                                            <div className="col-sm-7 align-self-center">
                                                <p>
                                                    <small>
                                                        We could not find the page you were looking for.<br></br>
                                                        It might be temporarily unavailable.
                                                    </small>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="text-center mt-4">
                                <small>Back to <Link to="/login">Login</Link> Page</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    );

}

export default Page404;

