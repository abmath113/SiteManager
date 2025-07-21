import React, { useState, useEffect } from 'react';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { saveSiteMasterDetails, updateSiteMasterDetails, getSiteMasterDetails, getAllBanksOnlyBankCodeAndBankId } from '../../../../../services/sitemasterservice';
import { toast } from 'react-toastify';
import { getAllChannelManagersOnlyChannelManagerIdNameAndEmailId } from '../../../../../services/channelmanagerservice';

interface FormValues {
    sitecode: string;
    siteatms: string;
    siteArea: number;
    sitestatus: boolean;
    bankId: null;
    siteaddress: string;
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

const initialValues: FormValues = {
    sitecode: '',
    siteatms: '',
    siteArea: 0,
    sitestatus: false,
    bankId: null,
    siteaddress: '',
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
  });

const SiteAddEdit: React.FC<SiteAddEditProps> = ({ onHide, modeIsAdd, siteId }) => {
    const [formValues, setFormValues] = useState<FormValues>(initialValues);
    const [channelManagerSelect, setChannelManagerSelect] = useState<channelManagerSelect[]>([]);
    const [bankCodeSelect, setBankCodeSelect] = useState<bankCodeSelect[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    // Fetch dropdown data from api  on component render
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
                    console.log(bankResponse.data)
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
                    const siteMasterGetDetailsFromAPI = await getSiteMasterDetails(siteId);

                    if (siteMasterGetDetailsFromAPI.successstatus !== true) {
                        setIsLoading(false);
                        toast.error('Failed to load site details');
                        return;
                    }

                    const apiData = siteMasterGetDetailsFromAPI.data;
                    console.log(apiData)
                    if (apiData) {

                        setFormValues({
                            sitecode: apiData.siteCode || '',
                            siteatms: apiData.siteATMs || '',
                            siteArea: apiData.siteArea || 0,
                            sitestatus: apiData.siteStatus === true, // Explicit boolean conversion
                            bankId: apiData.bank?.bankId, // 
                            siteaddress: apiData.siteAddress || '',
                            channelManagerId: apiData.channelManager?.channelManagerId ?? null,
                        });
                        console.log(formValues.sitecode);
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
    }, [modeIsAdd, siteId]);

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
                    toast.error(response.error || 'Failed to add site');
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
    return (
        <div className="container" style={{ maxHeight: '400px' }}>
            <Formik
                initialValues={formValues}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
                enableReinitialize
            >
                {({ errors, touched, resetForm }) => (
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
                                    className={
                                        'form-control' +
                                        (errors.channelManagerId && touched.channelManagerId ? ' is-invalid' : '')
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
                                }}
                            >
                                Clear
                            </button>
                            <button type="submit" className="btn btn-success">
                                {modeIsAdd ? 'Save' : 'Update'}
                            </button>
                        </div>
                    </Form>
                )}
            </Formik>
        </div>
    );
};

export default SiteAddEdit;