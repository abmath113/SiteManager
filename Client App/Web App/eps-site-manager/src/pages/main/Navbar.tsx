
import { useNavigate } from 'react-router-dom';
import { FiLogOut } from "react-icons/fi";
import { RiMenuLine } from "react-icons/ri";
import epsLogoNew from '../../resources/images/EpsLogoNew.png'
import configJSON from '../../resources/configuration.json';
import { useAuth } from '../../App';


function Navbar({ handleSidebarToggle, isSidebarHidden }: any) {
    const { name, email, logout } = useAuth();
    const navigate = useNavigate();


    const logOutUser = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar navbar-expand navbar-light">
            <div className={`navbar-brand-banner ${isSidebarHidden ? "show" : ""}`}>
                <h2>
                    <img className="navbar-brand-logo" src={epsLogoNew} alt="Brand Logo" />
                    <span className="navbar-brand-title">
                        <b>ep</b>{configJSON.appShort}
                    </span>
                </h2>
            </div>

            <div className="sidebar-toggle-container">
                <a className="sidebar-toggle" onClick={handleSidebarToggle}>
                    <RiMenuLine size={24} />
                </a>
            </div>

            <div className="navbar-collapse collapse">
                <ul className="navbar-nav navbar-align">
                    <li className="nav-item dropdown">

                    </li>

                    <li className="nav-item dropdown">


                        <a className="nav-link dropdown-toggle d-none d-sm-inline-block" id="userDropdown" data-bs-toggle="dropdown">
                            <span className="text-dark">{name}</span>
                        </a>
                        <div className="dropdown-menu dropdown-menu-end shadow rounded border-0 p-3" aria-labelledby="userDropdown">
                            <div className="dropdown-item-text pb-2 ">
                                <div className="text-dark">Hello, {name}</div>
                                <div className="text-muted small">{email}</div>
                                <div className="text-muted small">Login: {localStorage.getItem('logintime')}</div>
                            </div>
                            <button
                                className="btn btn-outline-danger w-100 mt-2 d-flex align-items-center justify-content-center gap-2 rounded-pill"
                                onClick={logOutUser}
                            >
                                <FiLogOut />
                                Log out
                            </button>
                        </div>

                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default Navbar;