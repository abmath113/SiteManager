import React, { useState, useEffect } from 'react';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import Select from 'react-select';
import { saveSiteMasterDetails, updateSiteMasterDetails, getSiteMasterDetails, getAllBanksOnlyBankCodeAndBankId } from '../../../../../services/sitemasterservice';
import { toast } from 'react-toastify';
import { getAllChannelManagersOnlyChannelManagerIdNameAndEmailId } from '../../../../../services/channelmanagerservice';
import { State, City } from 'country-state-city';
interface FormValues {
    sitecode: string;
    siteatms: string;
    siteArea: number;
    sitestatus: boolean;
    bankId: null;
    siteaddress: string;
    projectName: string;
    state: string;
    district: string;
    channelManagerId: null;
}

interface channelManagerSelect {
    channelManagerId: number;
    channelManagerName: string;
    emailId: string;
}

interface bankCodeSelect {
    bankCode: string;
    bankId: number;
}

interface SiteAddEditProps {
    onHide: () => void;
    modeIsAdd: boolean;
    siteId?: number;
}

interface StateOption {
    value: string;
    label: string;
    stateCode: string;
}

interface CityOption {
    value: string;
    label: string;
}

const initialValues: FormValues = {
    sitecode: '',
    siteatms: '',
    siteArea: 0,
    sitestatus: false,
    bankId: null,
    siteaddress: '',
    projectName: '',
    state: '',
    district: '',
    channelManagerId: null,
};

const validationSchema = Yup.object({
    sitecode: Yup.string().required('Site code is required'),
    sitestatus: Yup.boolean().required('Active status is required'),
    bankId: Yup.number().required('Bank is required'),
    channelManagerId: Yup.number().required('Channel Manager is required'),
    siteArea: Yup.string()
        .required('Site area is required')
        .matches(/^[1-9]\d*$/, 'Site area must be a positive number without leading zeros'),
    state: Yup.string().required('State is required'),
    district: Yup.string().required('District is required'),
});

const SiteAddEdit: React.FC<SiteAddEditProps> = ({ onHide, modeIsAdd, siteId }) => {
    const [formValues, setFormValues] = useState<FormValues>(initialValues);
    const [channelManagerSelect, setChannelManagerSelect] = useState<channelManagerSelect[]>([]);
    const [bankCodeSelect, setBankCodeSelect] = useState<bankCodeSelect[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    
    // State and District dropdown states
    const [stateOptions, setStateOptions] = useState<StateOption[]>([]);
    const [cityOptions, setCityOptions] = useState<CityOption[]>([]);
    const [selectedState, setSelectedState] = useState<StateOption | null>(null);
    const [selectedCity, setSelectedCity] = useState<CityOption | null>(null);

    // Initializing state options 
    useEffect(() => {
        const indianStates = State.getStatesOfCountry('IN');
        const formattedStates = indianStates.map(state => ({
            value: state.name,
            label: state.name,
            stateCode: state.isoCode
        })).sort((a, b) => a.label.localeCompare(b.label));
        setStateOptions(formattedStates);
    }, []);

    // Update cities when state changes
    useEffect(() => {
        if (selectedState) {
            const cities = City.getCitiesOfState('IN', selectedState.stateCode);
            const formattedCities = cities.map(city => ({
                value: city.name,
                label: city.name
            })).sort((a, b) => a.label.localeCompare(b.label));
            setCityOptions(formattedCities);
        } else {
            setCityOptions([]);
            setSelectedCity(null);
        }
    }, [selectedState]);

    // Fetch dropdown data from api on component render
    useEffect(() => {
        const fetchDropdownData = async () => {
            try {
                setIsLoading(true);
                const [channelManagerResponse, bankResponse] = await Promise.all([
                    getAllChannelManagersOnlyChannelManagerIdNameAndEmailId(),
                    getAllBanksOnlyBankCodeAndBankId()
                ]);

                if (channelManagerResponse.data) {
                    setChannelManagerSelect(channelManagerResponse.data);
                }
                if (bankResponse.data) {
                    console.log(bankResponse.data);
                    setBankCodeSelect(bankResponse.data);
                }
            } catch (error) {
                console.error("Error fetching dropdown data: ", error);
                toast.error("Error fetching dropdown data");
            } finally {
                setIsLoading(false);
            }
        };
        fetchDropdownData();
    }, []);

    // Load site details for edit
    useEffect(() => {
        if (!modeIsAdd && siteId) {
            const getSiteMasterFn = async () => {
                try {
                    setIsLoading(true);
                    const siteMasterGetDetailsFromAPI = await getSiteMasterDetails(siteId);

                    if (siteMasterGetDetailsFromAPI.successstatus !== true) {
                        setIsLoading(false);
                        toast.error('Failed to load site details');
                        return;
                    }

                    const apiData = siteMasterGetDetailsFromAPI.data;
                    console.log(apiData);
                    
                    if (apiData) {
                        const newFormValues = {
                            sitecode: apiData.siteCode || '',
                            siteatms: apiData.siteATMs || '',
                            siteArea: apiData.siteArea || 0,
                            sitestatus: apiData.siteStatus === true,
                            bankId: apiData.bank?.bankId,
                            siteaddress: apiData.siteAddress || '',
                            projectName: apiData.projectName || '',
                            state: apiData.state || '',
                            district: apiData.district || '',
                            channelManagerId: apiData.channelManager?.channelManagerId ?? null,
                        };
                        
                        setFormValues(newFormValues);

                        // Set selected state and city for react-select
                        if (apiData.state) {
                            const stateOption = stateOptions.find(option => option.value === apiData.state);
                            if (stateOption) {
                                setSelectedState(stateOption);
                                
                                // Load cities for the selected state and set selected city
                                setTimeout(() => {
                                    if (apiData.district) {
                                        const cities = City.getCitiesOfState('IN', stateOption.stateCode);
                                        const formattedCities = cities.map(city => ({
                                            value: city.name,
                                            label: city.name
                                        })).sort((a, b) => a.label.localeCompare(b.label));
                                        setCityOptions(formattedCities);
                                        
                                        const cityOption = formattedCities.find(option => option.value === apiData.district);
                                        if (cityOption) {
                                            setSelectedCity(cityOption);
                                        }
                                    }
                                }, 100);
                            }
                        }
                    }
                } catch (error) {
                    console.error("Error loading site details: ", error);
                    toast.error("Error loading site details");
                } finally {
                    setIsLoading(false);
                }
            };
            getSiteMasterFn();
        }
    }, [modeIsAdd, siteId, stateOptions]);

    // Submit handler
    const handleSubmit = async (formData: FormValues) => {
        try {
            let response;
            if (modeIsAdd) {
                response = await saveSiteMasterDetails(formData);

                // Check if the response indicates success
                if (response.successstatus === true) {
                    toast.success('Site added successfully');
                    onHide();
                } else {
                    console.log("this is the error in adding site: " + JSON.stringify(response.error.message));
                    toast.error(JSON.stringify(response.error.message) || 'Failed to add site');
                }
            } else {
                const updatedData = { ...formData, siteid: siteId };
                response = await updateSiteMasterDetails(updatedData);
                if (response.successstatus === true) {
                    toast.success('Site updated successfully');
                    onHide();
                } else {
                    toast.error(response.error || 'Failed to update site');
                }
            }
        } catch (error: any) {
            console.error("Submit error: ", error);
            const errorMessage = error.response?.data?.message ||
                error.response?.status === 404 ? 'Update endpoint not found' :
                    'Failed to save/update site';
            toast.error(errorMessage);
        }
    };

    // Custom styles for react-select 
    const selectStyles = {
        control: (base: any, state: any) => ({
            ...base,
            minHeight: '38px',
            border: state.isFocused ? '1px solid #80bdff' : '1px solid #ced4da',
            boxShadow: state.isFocused ? '0 0 0 0.2rem rgba(0,123,255,.25)' : 'none',
            '&:hover': {
                border: state.isFocused ? '1px solid #80bdff' : '1px solid #adb5bd'
            }
        }),
        menu: (base: any) => ({
            ...base,
            zIndex: 9999
        })
    };

    const invalidSelectStyles = {
        ...selectStyles,
        control: (base: any, state: any) => ({
            ...base,
            minHeight: '38px',
            border: '1px solid #dc3545',
            boxShadow: state.isFocused ? '0 0 0 0.2rem rgba(220,53,69,.25)' : 'none',
            '&:hover': {
                border: '1px solid #dc3545'
            }
        })
    };

    return (
        <div className="container" style={{ maxHeight: '400px' }}>
            <Formik
                initialValues={formValues}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
                enableReinitialize
            >
                {({ errors, touched, resetForm, setFieldValue, values }) => (
                    <Form>
                        <div className="row">
                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    Site Code <span className="text-danger">*</span>
                                </label>
                                <Field
                                    name="sitecode"
                                    type="text"
                                    placeholder="Enter Site Code"
                                    className={
                                        'form-control' +
                                        (errors.sitecode && touched.sitecode ? ' is-invalid' : '')
                                    }
                                />
                                <ErrorMessage
                                    name="sitecode"
                                    component="div"
                                    className="invalid-feedback"
                                />
                            </div>
                            
                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    ATM Id <span className="text-danger">*</span>
                                </label>
                                <Field
                                    name="siteatms"
                                    type="text"
                                    placeholder="Enter Atm Code"
                                    className={
                                        'form-control' +
                                        (errors.siteatms && touched.siteatms ? ' is-invalid' : '')
                                    }
                                />
                                <ErrorMessage
                                    name="siteatms"
                                    component="div"
                                    className="invalid-feedback"
                                />
                            </div>

                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    Site Area <span className="text-danger">*</span>
                                </label>
                                <Field
                                    name="siteArea"
                                    type="text"
                                    inputMode="numeric"
                                    pattern="[0-9]*"
                                    placeholder="Enter Site Area in sq ft"
                                    className={
                                        'form-control' +
                                        (errors.siteArea && touched.siteArea ? ' is-invalid' : '')
                                    }
                                />
                                <ErrorMessage
                                    name="siteArea"
                                    component="div"
                                    className="invalid-feedback"
                                />
                            </div>

                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    Channel Manager <span className="text-danger">*</span>
                                </label>
                                <Field
                                    as="select"
                                    name="channelManagerId"
                                    value={values.channelManagerId ?? ""}
                                    className={
                                        'form-control' +
                                        (errors.channelManagerId && touched.channelManagerId ? ' is-invalid' : '')
                                                                        }
                                    onChange={(e: React.ChangeEvent<HTMLSelectElement>) =>
    setFieldValue("channelManagerId", e.target.value === "" ? null : Number(e.target.value))
  }
                                                                        
                                >
                                    <option value="">Select Channel Manager</option>
                                    {channelManagerSelect.map(manager => (
                                        <option key={manager.channelManagerId} value={manager.channelManagerId}>
                                            {manager.channelManagerName}
                                        </option>
                                    ))}
                                </Field>
                                <ErrorMessage
                                    name="channelManagerId"
                                    component="div"
                                    className="invalid-feedback"
                                />
                            </div>

                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    Active Status <span className="text-danger">*</span>
                                </label>
                                <Field
                                    as="select"
                                    name="sitestatus"
                                    className={
                                        'form-control' +
                                        (errors.sitestatus && touched.sitestatus ? ' is-invalid' : '')
                                    }
                                >
                                    <option value="">Select status</option>
                                    <option value="true">Active</option>
                                    <option value="false">Inactive</option>
                                </Field>
                                <ErrorMessage
                                    name="sitestatus"
                                    component="div"
                                    className="invalid-feedback"
                                />
                            </div>

                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    Bank <span className="text-danger">*</span>
                                </label>
                                
                                <Field
                                    as="select"
                                    name="bankId"
                                    className={'form-control' + (errors.bankId && touched.bankId ? ' is-invalid' : '')}
                                    value={values.bankId??''}
                                    onChange={(e: React.ChangeEvent<HTMLSelectElement>) =>
    setFieldValue("bankId", e.target.value === "" ? null : Number(e.target.value))
  }
                                >
                                    <option value="">Select Bank</option>
                                    {bankCodeSelect.map(bank => (
                                        <option key={bank.bankId} value={bank.bankId}>
                                            {bank.bankCode}
                                        </option>
                                    ))}
                                </Field>
                                <ErrorMessage
                                    name="bankId"
                                    component="div"
                                    className="invalid-feedback"
                                />
                            </div>

                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    Project Name
                                </label>
                                <Field
                                    name="projectName"
                                    type="text"
                                    placeholder="Enter Project Name"
                                    className="form-control"
                                />
                            </div>

                            {/* State Dropdown */}
                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    State <span className="text-danger">*</span>
                                </label>
                                <Select
                                    options={stateOptions}
                                    value={selectedState}
                                    onChange={(option) => {
                                        setSelectedState(option);
                                        setSelectedCity(null);
                                        setFieldValue('state', option ? option.value : '');
                                        setFieldValue('district', '');
                                    }}
                                    placeholder="Select a state"
                                    isClearable
                                    isSearchable
                                    styles={errors.state && touched.state ? invalidSelectStyles : selectStyles}
                                />
                                {errors.state && touched.state && (
                                    <div className="invalid-feedback d-block">
                                        {errors.state}
                                    </div>
                                )}
                            </div>

                            {/* District Dropdown */}
                            <div className="col-md-6 col-12 mb-3">
                                <label className="form-label">
                                    District <span className="text-danger">*</span>
                                </label>
                                <Select
                                    options={cityOptions}
                                    value={selectedCity}
                                    onChange={(option) => {
                                        setSelectedCity(option);
                                        setFieldValue('district', option ? option.value : '');
                                    }}
                                    placeholder={
                                        !selectedState 
                                            ? "Select a state first" 
                                            : cityOptions.length === 0 
                                                ? "Loading districts..." 
                                                : "Select a district"
                                    }
                                    isClearable
                                    isSearchable
                                    isDisabled={!selectedState || cityOptions.length === 0}
                                    styles={errors.district && touched.district ? invalidSelectStyles : selectStyles}
                                />
                                {errors.district && touched.district && (
                                    <div className="invalid-feedback d-block">
                                        {errors.district}
                                    </div>
                                )}
                            </div>

                            <div className="col-md-12 mb-3">
                                <label className="form-label">
                                    Address
                                </label>
                                <Field
                                    name="siteaddress"
                                    type="text"
                                    placeholder="Enter site address"
                                    className="form-control"
                                />
                            </div>
                        </div>

                        <div className="d-flex justify-content-between">
                            <button
                                type="button"
                                className="btn btn-secondary"
                                onClick={(e: React.MouseEvent<HTMLButtonElement>) => {
                                    e.preventDefault();
                                    resetForm();
                                    setSelectedState(null);
                                    setSelectedCity(null);
                                }}
                            >
                                Clear
                            </button>
                            <button type="submit" className="btn btn-success" disabled={isLoading}>
                                {isLoading ? 'Loading...' : (modeIsAdd ? 'Save' : 'Update')}
                            </button>
                        </div>
                    </Form>
                )}
            </Formik>
        </div>
    );
};

export default SiteAddEdit;