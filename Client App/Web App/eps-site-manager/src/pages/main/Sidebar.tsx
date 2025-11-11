import { Link, useLocation } from "react-router-dom";
import { IconContext } from "react-icons";
import { LuHistory } from "react-icons/lu";
import { TbBook2 } from "react-icons/tb";
import { TbBuildingBank } from "react-icons/tb";
import { TbUserPin } from "react-icons/tb";
import { BiTransfer } from "react-icons/bi";
import { RxSlider } from "react-icons/rx";
import { BsBank } from "react-icons/bs";
import { MdOutlineAnalytics } from "react-icons/md";
import { BsPersonVideo } from "react-icons/bs";
import SimpleBar from 'simplebar-react';
import 'simplebar-react/dist/simplebar.min.css';
import epsLogoNew from '../../resources/images/EpsLogoNew.png'
import configJSON from '../../resources/configuration.json';
import './Sidebar.css'; 
import { Sparkles } from 'lucide-react';

function Sidebar(props: any) {

    const location = useLocation();

    return (
        <nav id="sidebar" className={`sidebar ${props.isHidden ? "collapsed" : ""}`}>
            <div className="sidebar-content d-flex flex-column h-100">
                <div className="sidebar-brand p-1">
                    <h2>
                        <img className="sidebar-brand-logo" src={epsLogoNew} alt="Brand Logo" />
                        <span className="sidebar-brand-title">
                            <b>ep</b>{configJSON.appShort}
                        </span>
                    </h2>
                </div>

                <SimpleBar style={{ maxHeight: 520 }} className="flex-grow-1">
                    <IconContext.Provider value={{ color: "#0099cc", size: '1em' }}>
                        <ul className="sidebar-nav p-0 ">

                            <li className="sidebar-header px-1">Masters</li>

                                <li className={`sidebar-item ${location.pathname === '/sitesearch' ? 'active gradient-active' : ''}`}>
                                    <Link className="sidebar-link" to="sitesearch">
                                        <span className="align-middle"><TbBuildingBank /></span>
                                        <span className="align-middle">Site</span>
                                    </Link>
                                </li>

                                <li className={`sidebar-item ${location.pathname === '/landlordsearch' ? 'active gradient-active' : ''}`}>
                                    <Link className="sidebar-link" to="landlordsearch">
                                        <span className="align-middle"><TbUserPin /></span>
                                        <span className="align-middle">Landlord</span>
                                    </Link>
                                </li>

                                <li className={`sidebar-item ${location.pathname === '/rentagreementsearch' ? 'active gradient-active' : ''}`}>
                                    <Link className="sidebar-link" to="rentagreementsearch">
                                        <span className="align-middle"><TbBook2 /></span>
                                        <span className="align-middle">Rent Agreements</span>
                                    </Link>
                                </li>
                                <li className={`sidebar-item ${location.pathname === '/channelmanagerandbanksearch' ? 'active gradient-active' : ''}`}>
                                    <Link className="sidebar-link" to="channelmanagerandbanksearch">
                                    <span className="align-middle"><BsPersonVideo /></span>
                                    <span className="align-middle">CMs & Banks</span>
                                    </Link>
                                </li>
                                <li className={`sidebar-item ${location.pathname === '/wlabulkupload' ? 'active gradient-active' : ''}`}>
                                    <Link className="sidebar-link" to="wlabulkupload">
                                    <span className="align-middle"><BsBank /> </span>
                                    <span className="align-middle">WLA Bancs</span>
                                    </Link>
                                </li>
                                
                            <li className="sidebar-header px-1">Transactions</li>

                            
                            <li className={`sidebar-item ${location.pathname === '/generaterent' ? 'active gradient-active' : ''}`}>
                            <Link className="sidebar-link" to="generaterent">
                                        <span className="align-middle"><BiTransfer /></span>
                                        <span className="align-middle">Generate Rent</span>
                                    </Link>
                                </li>
                                
                                <li className={`sidebar-item ${location.pathname === '/sitehistory' ? 'active gradient-active' : ''}`}>
                            <Link className="sidebar-link" to="sitehistory">
                                    <span className="align-middle"><LuHistory /></span>
                                    <span className="align-middle">Site History</span>
                                </Link>
                            </li>
   
                                <li className={`sidebar-item ${location.pathname === '/renthistory' ? 'active gradient-active' : ''}`}>
                                <Link className="sidebar-link" to="renthistory">
                                    <span className="align-middle"><RxSlider/></span>
                                    <span className="align-middle">Rent Payout</span>
                                </Link>
                            </li>
                        </ul>
                    </IconContext.Provider>
                </SimpleBar>
                
                {/* AI Chat item at bottom of sidebar */}
                <div className="mt-auto">
                    <IconContext.Provider value={{ color: "#0099cc", size: '1em' }}>
                        <ul className="sidebar-nav p-0 mb-3">
                            <li className={`sidebar-item ${location.pathname.includes('sitemanagerchat') ? 'active gradient-active' : ''}`}>
                                <Link className="sidebar-link" to="sitemanagerchat">
                                    <span className="align-middle"><Sparkles /></span>
                                    <span className="align-middle">AI Chat</span>
                                </Link>
                            </li>
                        </ul>
                    </IconContext.Provider>
                </div>
            </div>
        </nav>
    );
}

export default Sidebar;