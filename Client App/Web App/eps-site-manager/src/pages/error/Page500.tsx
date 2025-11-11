import React from "react";
import { Link } from "react-router-dom";

function Page500(): JSX.Element {
    return (
        <main className="d-flex w-100 min-vh-100 bg-light text-dark">
            <div className="container d-flex flex-column justify-content-center align-items-center">
                <div className="text-center my-5">
                    <h1 className="display-3 text-danger">500</h1>
                    <h2 className="h4 mb-4">Oops! Something went wrong on our end.</h2>
                    <p className="lead mb-4">
                        We're experiencing some technical difficulties. Please try again later.
                    </p>
                    <div className="mt-3">
                        <Link to="/login" className="btn btn-primary me-2">
                            Go to Login Page
                        </Link>
                        <button
                            className="btn btn-secondary"
                            onClick={() => window.location.reload()}
                        >
                            Refresh Page
                        </button>
                    </div>
                </div>

                <div className="card mt-4 shadow-sm">
                    <div className="card-body">
                        <h5 className="card-title">Why am I seeing this?</h5>
                        <p className="card-text">
                            This error may occur due to temporary server issues or unexpected technical problems. 
                            We apologize for the inconvenience and appreciate your patience.
                        </p>
                    </div>
                </div>
            </div>
        </main>
    );
}

export default Page500;
