import React, { useState, useContext } from 'react';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import 'bootstrap/dist/css/bootstrap.min.css';
import { IoDocumentText } from "react-icons/io5";
import { DateTime } from 'luxon';
import RentAgreementAddEdit from './rentagreementaddedit';
import { Modal, Button } from "react-bootstrap";
import { searchAllRentAgreementMasterDetails, uploadBulkRentAgreements,downloadBulkUploadTemplate } from '../../../../../services/rentagreementservice';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import DataTable from "react-data-table-component";
import { FaPencilAlt } from "react-icons/fa";
import { FaEye,FaExclamationCircle } from "react-icons/fa";
import { BsGraphUp } from "react-icons/bs";
import { ImCross } from "react-icons/im";
import { TableColumn } from "react-data-table-component";
import RentPredictionModal from '../../rentPrediction/RentPredictionModal';
import RentAgreementTerminationModal from './RentAgreementTerminationModal';
import { parseArgs } from 'util';

// Define the RentAgreement interface with Date types and new fields
interface RentAgreement {
  agreementId: number;
  agreementSpan: number;  
  agreementDate: Date; 
  rentPayStartDate:Date;
  agreementEndDate:Date;
 
  monthlyRent: number;
  solarPanelRent: number;
  landlordName: string;
  landlordId: number;
  siteId: number;
  siteCode: string;
  rentAgreementStatus: boolean;
  rentAgreementStatusStr: string;
  escalationAfterMonths: number;
  paymentInterval:string;
  escalationPercent: number;  
  terminationDate:Date |null;
  file: File;
  agreementScanExist: boolean;
  
}

interface FormValues {
  agreementId: string;
  landlordId: string;
  rentAgreementStatus: any;
  
}

interface ModaMode {
  onHide: any;
  modeIsAdd: boolean;
  agreementId: number;
}

const conditionalRowStyles = [
  {
    when: (row: RentAgreement) => row.rentAgreementStatus === true,
    style: {
      backgroundColor: '#ddffde',
      minHeight: '40px',
      color: 'black',
      '&:hover': {
        cursor: 'pointer',
        backgroundColor: '#c6ffc6',
      },
    },
  },
  {
    when: (row: RentAgreement) => row.rentAgreementStatus === false,
    style: {
      backgroundColor: '#ffefef',
      minHeight: '40px',
      color: 'black',
      '&:hover': {
        cursor: 'pointer',
        backgroundColor: '#ffd7d6',
      },
    },
  },
];

function RentAgreementSearch() {
  const [showRentPredictionModal, setShowRentPredictionModal] = useState(false);
  const [selectedAgreementIdForRent, setSelectedAgreementIdForRent] = useState<number | null>(null);
  const handleCloseModal = () => setShowModal(false);
  const [showModal, setShowModal] = useState(false);
  const [modalModeValues, setModalModeValues] = useState<ModaMode>({
    onHide: handleCloseModal,
    modeIsAdd: true,
    agreementId: 0,
  });

  const [rentAgreementList, setRentAgreementList] = useState<RentAgreement[]>([]);
  const [selectedAgreementStartDate, setSelectedAgreementStartDate] = useState<Date | null>(null);
  const [searchResults, setSearchResults] = useState<RentAgreement[]>([]);
  const [selectedAgreement, setSelectedAgreement] = useState(false);

  const [showTerminationModal, setShowTerminationModal] = useState(false);
  const [selectedAgreementIdForTermination, setSelectedAgreementIdForTermination] = useState<number | null>(null);

  const [filterText, setFilterText] = useState('');
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);

  const [isLoading, setIsLoading] = useState(false);
  const [isSearchLoading, setIsSearchLoading] = useState(false);

  const [isUploading, setIsUploading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const handleShowModalAdd = () => {
    setShowModal(true);
    setSelectedAgreement(true);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: true,
      agreementId: 0
    });
  };

  const handleShowModalEdit = (agreementId: number) => {
    setShowModal(true);
    setSelectedAgreement(true);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: false,
      agreementId: agreementId,
    });
  };

  const handleShowTerminationModal = (agreementId: number) => {
    setSelectedAgreementIdForTermination(agreementId);
    setShowTerminationModal(true);
  };

  const handleCloseTerminationModal = () => {
    setShowTerminationModal(false);
    setSelectedAgreementIdForTermination(null); // Optionally reset the selected ID
  };

  const [showRowDetailModal, setShowRowDetailModal] = useState(false);
  const [selectedRowData, setSelectedRowData] = useState<RentAgreement | null>(null);

  // Function to parse date from API response
  // function takes an array of numbers (dateArray) as input and returns a JavaScript Date object
  const parseDate = (dateStr: string): Date | null => {
    return dateStr ? new Date(dateStr) : null;
  };
  

  // Function to format Date objects to string using luxon
  const formatDate = (date: Date|null): string => {
    if(date===null || date == undefined){
      return "";
    }
    return DateTime.fromJSDate(date).toLocaleString(DateTime.DATE_MED);
  };

  // Function to handle double click on a row
  const handleRowDoubleClick = (row: RentAgreement) => {
    const formattedRow = {
      ...row,
      agreementDate: row.agreementDate,
     
    };
    setSelectedRowData(formattedRow);
    setShowRowDetailModal(true);
  };
  
  // Function to close the row detail modal
  const handleCloseRowDetailModal = () => {
    setShowRowDetailModal(false);
    setSelectedRowData(null);
  };

  // Define columns for DataTable including new fields
  const columns: TableColumn<RentAgreement>[] = [
    {
      name: "Site Code",
      selector: (row: RentAgreement) => row.siteCode,
      sortable: true,
      wrap: true,
      width: '120px',
    },
    {
      name: "Name",
      selector: (row: RentAgreement) => row.landlordName,
      sortable: true,
      grow: 2,
      wrap: true,
      minWidth: '100px',
      maxWidth: '140px',
    },
    {
      name: "Monthly Rent",
      selector: (row: RentAgreement) => row.monthlyRent,
      sortable: true,
      right: true,
      width: '140px',
    },
    {
      name: "Status",
      selector: (row: RentAgreement) => row.rentAgreementStatusStr,
      sortable: true,
      width: '87px',
    },
    {
      name: "Date",
      selector: (row: RentAgreement) => formatDate(row.agreementDate),
      sortable: true,
      width: '110px',
    },
    {
     name: "Agreement Scan",
      cell: (row: RentAgreement) => ( 
        <div>
          {row.agreementScanExist ? (
            <FaEye 
              style={{ fontSize: "1.2rem", transition: "transform 0.2s ease" }}
              onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.5)")}
              onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
              title="View Agreement Scan"
            />
          ) : (
            <FaExclamationCircle 
              style={{ fontSize: "1.2rem", transition: "transform 0.2s ease", color: "#ffa500" }}
              onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.5)")}
              onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
              title="No Agreement Scan Available"
            />
          )}
        </div>
      ),
      sortable: false,
      width: '70px',
    },
    {
      name: "Actions",
      cell: (row: RentAgreement) => (
        <div className="d-flex justify-content-around align-items-center">
          <a className="mr-2" title="Edit Rent Agreement" onClick={() => handleShowModalEdit(row.agreementId)}>
            <FaPencilAlt
             style={{ fontSize: "1.2rem", transition: "transform 0.2s ease" }}
             onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.5)")}
             onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")} />
          </a>
          <a className="mx-2 ps-2" 
          title="Get Rent Prediction" 
            onClick={() => {
              console.log("Agreement id " + row.agreementId);
              console.log("Agreement Date " + row.agreementDate);
              setSelectedAgreementIdForRent(row.agreementId);
              setSelectedAgreementStartDate(row.agreementDate);
              setShowRentPredictionModal(true);
            }}>
            <BsGraphUp
             style={{ fontSize: "1.2rem", transition: "transform 0.2s ease" }}
             onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.5)")}
             onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")} />
          </a>
          <a className="ml-2 text-danger p-3" 
          title="Terminate Rent Agreement"
           onClick={() => handleShowTerminationModal(row.agreementId)}>
            <ImCross 
             style={{ fontSize: "1.2rem", transition: "transform 0.2s ease" }}
             onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.5)")}
             onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}/>
          </a>
        </div>
      ),
      // width: '170px',
      allowOverflow: true,
    },
  ];
  

  const validationSchema = Yup.object({
    agreementId: Yup.string(),
    landlordId: Yup.string(),
    rentAgreementStatus: Yup.boolean(),
  });

  const initialValues: FormValues = {
    agreementId: '',
    landlordId: '',
    rentAgreementStatus: '',
   
  };

  const handleSubmit = async (values: FormValues) => {
    if (values.agreementId === '' && values.landlordId === '' && values.rentAgreementStatus === '') {
      toast.error('At least one search field required');
      return;
    }

    setIsSearchLoading(true);
    try {
      // Your search API call here
      // const response = await searchRentAgreements(values);
      // setSearchResults(response.data);
    } catch (error) {
      toast.error('Error performing search');
    } finally {
      setIsSearchLoading(false);
    }
  };

  const handleShowAll = async () => {
    setIsLoading(true);
    try {
      const rentAgreementSearchAllResultFromAPI = await searchAllRentAgreementMasterDetails();
     
      if (rentAgreementSearchAllResultFromAPI.successstatus === undefined) {
        toast.error('Invalid Response');
        return;
      }
  
      if (rentAgreementSearchAllResultFromAPI.successstatus === false) {
        toast.error(rentAgreementSearchAllResultFromAPI.error.message);
        return;
      }
  
      const apiData = rentAgreementSearchAllResultFromAPI.data;
      if (apiData !== undefined && apiData !== null) {
        const apiDataMapped: RentAgreement[] = apiData.map((e: any) => {
  // Parse dates
   const startDate = parseDate(e.agreementDate);
  // const startDate = e.agreementDate;
  const endDate = parseDate(e.agreementEndDate);
  console.log('real start Date:', e.agreementDate);
 console.log('Start Date:', startDate);
  // Calculate agreement span in months and years
  let agreementSpan = "";
  if (startDate && endDate) {
    // Calculate difference in months
    const startYear = startDate.getFullYear();
    const endYear = endDate.getFullYear();
    const startMonth = startDate.getMonth();
    const endMonth = endDate.getMonth();
    
    // Total months between the dates
    const totalMonths = (endYear - startYear) * 12 + (endMonth - startMonth);
    
    // Convert to years and months
    const years = Math.floor(totalMonths / 12);
    const months = totalMonths % 12;
    
    // Format the string
    if (years > 0 && months > 0) {
      agreementSpan = `${years} ${years === 1 ? 'year' : 'years'}, ${months} ${months === 1 ? 'month' : 'months'}`;
    } else if (years > 0) {
      agreementSpan = `${years} ${years === 1 ? 'year' : 'years'}`;
    } else {
      agreementSpan = `${months} ${months === 1 ? 'month' : 'months'}`;
    }
  }

  const paymentIntervalMap = {
  1: 'Monthly',
  3: 'Quarterly',
  6: 'Half-Yearly',
  0: 'Rent Free',
};

const paymentIntervalStr = paymentIntervalMap[e.paymentInterval as keyof typeof paymentIntervalMap] || 'Unknown';

  return {
    agreementId: e.agreementId,
    landlordId: e.landlordId?.id || e.landlordId,  
    landlordName: e.landlordId?.name || '',        
    siteId: e.siteId?.id || e.siteId,             
    siteCode: e.siteId?.siteCode || '',
    agreementSpan: agreementSpan, // Now a formatted string like "2 years, 3 months"
    agreementDate: startDate,
    rentPayStartDate: parseDate(e.rentPayStartDate),
    agreementEndDate: endDate,
    monthlyRent: e.monthlyRent,
    solarPanelRent: e.solarPanelRent,
    rentAgreementStatus: e.rentAgreementStatus,
   paymentInterval: paymentIntervalStr, 
    rentAgreementStatusStr: e.rentAgreementStatus ? 'Active' : 'Inactive',
    escalationAfterMonths: e.escalationAfterMonths, 
    escalationPercent: e.escalationPercent,
    terminationDate: parseDate(e.terminationDate),  
    agreementScanExist: e.agreementScanExist,
  };
});
        
        setRentAgreementList(apiDataMapped); 
        setSearchResults(apiDataMapped);      
        toast.success('All rent agreements fetched successfully');
      }
    } catch (error) {
      toast.error('Error fetching data');
    } finally {
      setIsLoading(false);
    }
  };
  
  // Add filtered data logic
  const filteredItems = searchResults.filter(
    item => {
      const searchableFields = [
        item.siteCode,
        item.landlordName,
        item.monthlyRent.toString(),
        item.rentAgreementStatusStr,
        formatDate(item.agreementDate)
      ];
      
      return searchableFields.some(field => 
        field.toLowerCase().includes(filterText.toLowerCase())
      );
    }
  );

  // Add subheader component
  const SubHeaderComponent = React.useMemo(() => {
    const handleClear = () => {
      if (filterText) {
        setResetPaginationToggle(!resetPaginationToggle);
        setFilterText('');
      }
    };

    return (
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <input
          type="text"
          className="form-control form-control-sm"
          placeholder="Search records..."
          value={filterText}
          onChange={e => setFilterText(e.target.value)}
          style={{ width: '200px', marginRight: '10px' }}
        />
        <button 
          className="btn btn-sm btn-secondary"
          onClick={handleClear}
        >
          Clear
        </button>
      </div>
    );
  }, [filterText, resetPaginationToggle]);

  const validateExcelFile = (file: File): boolean => {
    const allowedTypes = [
      'application/vnd.ms-excel',
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel.sheet.macroEnabled.12'
    ];
    return allowedTypes.includes(file.type);
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (validateExcelFile(file)) {
        setSelectedFile(file);
      } else {
        toast.error('Please upload a valid Excel file (.xls, .xlsx)');
        e.target.value = ''; // Clear the file input
        setSelectedFile(null);
      }
    }
  };

  const handleBulkUpload = async () => {
    if (!selectedFile) {
      toast.error('Please select a file first');
      return;
    }

    setIsUploading(true);
    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await uploadBulkRentAgreements(formData);

      if (response.successstatus && response.data) {
        const blob = new Blob([response.data], { 
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'processed_rent_agreements.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();

        toast.success('Bulk upload processed successfully. Downloading results...');
        
        handleShowAll();
        
        setSelectedFile(null);
        const fileInput = document.getElementById('bulkUploadFile') as HTMLInputElement;
        if (fileInput) fileInput.value = '';
      } else {
        toast.error(response.error || 'Failed to process bulk upload');
      }
    } catch (error) {
      toast.error('Error processing bulk upload');
      console.error('Bulk upload error:', error);
    } finally {
      setIsUploading(false);
    }
  };
  const handleDownloadTemplate = async () => {
    try {
      const response = await downloadBulkUploadTemplate();
      
      if (response.successstatus && response.data) {
        const blob = new Blob([response.data], { 
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'bulk_upload_template.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
        
        toast.success('Template downloaded successfully');
      } else {
        toast.error(response.error || 'Failed to download template');
      }
    } catch (error) {
      toast.error('Error downloading template');
      console.error('Template download error:', error);
    }
  };
  return (
    <div className="container-fluid p-1">
      {/* <ToastContainer /> */}
      <div className="row">
        <div className="col-md-3">
          <div className="p-1 bg-light rounded shadow-sm">
            <div className="mb-3 text-center">
              <button
                className="btn btn-primary btn-sm btn-success"
                onClick={handleShowModalAdd}
              >
                <IoDocumentText className="mb-1" /> Add New Agreement
              </button>
            </div>
            <Formik
              initialValues={initialValues}
              validationSchema={validationSchema}
              onSubmit={handleSubmit}
            >
              {({ errors, touched, resetForm }) => (
                // form for rent agreement search
                <Form>
                  <p className="h5">Agreement Search</p>

                  <div className="row py-2">
                    <div className="col-12 mb-2">
                      <label className="col-form-label col-form-label-sm">
                        Site Code
                      </label>
                      <Field
                        name="agreementId"
                        type="text"
                        placeholder="Enter Site Code"
                        className={
                          "form-control form-control-sm" +
                          (errors.agreementId && touched.agreementId
                            ? " border-danger"
                            : "") +
                          (touched.agreementId ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="agreementId"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="landlordId"
                        className="col-form-label col-form-label-sm"
                      >
                        Landlord Name
                      </label>
                      <Field
                        type="text"
                        name="landlordId"
                        placeholder="Enter Landlord Name"
                        className={
                          "form-control form-control-sm" +
                          (errors.landlordId && touched.landlordId
                            ? " border-danger"
                            : "") +
                          (touched.landlordId ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="landlordId"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="rentAgreementStatus"
                        className="col-form-label col-form-label-sm"
                      >
                        Agreement Status
                      </label>
                      <Field
                        as="select"
                        name="rentAgreementStatus"
                        className={
                          "form-control form-control-sm" +
                          (errors.rentAgreementStatus &&
                          touched.rentAgreementStatus
                            ? " border-danger"
                            : "") +
                          (touched.rentAgreementStatus ? " border-success" : "")
                        }
                      >
                        <option value="">Select status</option>
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                      </Field>
                      <ErrorMessage
                        name="rentAgreementStatus"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>
                  </div>
                  <div className="row py-2">
                    <div className="col-12 mb-1 text-end">
                      <Button
                        variant="secondary"
                        size="sm"
                        className="mx-1 my-2"
                        onClick={() => {
                          resetForm();
                          setRentAgreementList([]);
                          setSearchResults([]);
                        }}
                      >
                        Clear
                      </Button>

                      <Button
                        type="button"
                        variant="outline-secondary"
                        size="sm"
                        className="mx-1 my-2"
                        style={{ display: "inline-block", minWidth: '80px' }}
                        onClick={handleShowAll}
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <div className="d-flex align-items-center">
                            <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                            Loading...
                          </div>
                        ) : (
                          'Show All'
                        )}
                      </Button>

                      <Button
                        type="submit"
                        variant="primary"
                        size="sm"
                        className="mx-1 my-2"
                        disabled={isSearchLoading}
                      >
                        {isSearchLoading ? (
                          <div className="d-flex align-items-center">
                            <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                            Searching...
                          </div>
                        ) : (
                          'Search'
                        )}
                      </Button>
                    </div>
                  </div>
                </Form>
              )}
            </Formik>

            <div className="mb-3">
              <p className="h5">Bulk Upload</p>
              <div className="d-flex flex-column gap-2">
                {/* File Input */}
                <div className="input-group input-group-sm mb-2">
                  <input
                    type="file"
                    className="form-control form-control-sm"
                    id="bulkUploadFile"
                    accept=".xlsx,.xls"
                    onChange={handleFileSelect}
                  />
                </div>

                <button
                  className="btn btn-outline-primary btn-sm"
                  onClick={handleBulkUpload}
                  disabled={isUploading || !selectedFile}
                >
                  {isUploading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                      Uploading...
                    </>
                  ) : (
                    <>
                      <i className="fas fa-upload me-1"></i> Upload
                    </>
                  )}
                </button>

                <button
  className="btn btn-outline-secondary btn-sm"
  onClick={handleDownloadTemplate}
>
  <i className="fas fa-download me-1"></i> Download Template
</button>
              </div>
            </div>
          </div>
        </div>

        <div className="col-md-9">
          <div className="p-1 bg-light rounded shadow-sm" style={{ height: '100vh' }}>
              {/* Wrap DataTable in a div with a fixed height and overflow */}
              <div style={{ height: 'calc(100% - 50px)', overflowY: 'auto' }}> {/* Adjust the height as needed */}
                <DataTable
                  columns={columns}
                  data={filteredItems}
                  highlightOnHover
                  pointerOnHover
                  conditionalRowStyles={conditionalRowStyles}
                  onRowDoubleClicked={handleRowDoubleClick}
                  defaultSortFieldId="name"
                  defaultSortAsc={true}
                  subHeader
                  subHeaderComponent={SubHeaderComponent}
                  pagination // Ensure pagination is disabled
                />
              </div>
          </div>
        </div>
      </div>

      {/* Modal for Add/Edit */}
      <Modal 
        show={showModal} 
        onHide={handleCloseModal} 
        size="lg"
      >
        <Modal.Header closeButton>
          <Modal.Title>
            {modalModeValues.modeIsAdd ? "Add Rent Agreement" : "Edit Rent Agreement"}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <RentAgreementAddEdit
            modeIsAdd={modalModeValues.modeIsAdd}
            agreementId={modalModeValues.agreementId}
            onHide={handleCloseModal}
          />
        </Modal.Body>
      </Modal>

      {/* Modal for Row Details */}
      <Modal 
        show={showRowDetailModal} 
        onHide={handleCloseRowDetailModal}
      >
        <Modal.Header>
          <Modal.Title>Rent Agreement Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedRowData && (
            <div>
              <table className="modal-table">
                <tbody>
                  <tr>
                    <th>Landlord Name</th>
                    <td>{selectedRowData.landlordName}</td>
                  </tr>
                  <tr>
                    <th>Site Code</th>
                    <td>{selectedRowData.siteCode}</td>
                  </tr>
                  <tr>
                    <th>Monthly Rent</th>
                    <td>{selectedRowData.monthlyRent}</td>
                  </tr>
                  <tr>
                    <th>Agreement Status</th>
                    <td>{selectedRowData.rentAgreementStatusStr}</td>
                  </tr>
                  <tr>
                    <th>Agreement Span (in Months)</th>
                    <td>{selectedRowData.agreementSpan}</td>
                  </tr>
                  <tr>
                    <th>Agreement Start Date</th>
                    <td>{formatDate(selectedRowData.agreementDate)}</td>
                  </tr>
                  

                  <tr>
                    <th>Solar Panel Rent</th>
                    <td>{selectedRowData.solarPanelRent}</td>
                  </tr>
                   <tr>
                    <th>Payment Interval</th>
                    <td>{selectedRowData.paymentInterval}</td>
                  </tr>

                  <tr>
                    <th>Escalation After Months</th> 
                    <td>{selectedRowData.escalationAfterMonths}</td>{" "}
                  
                  </tr>

                  <tr>
                    <th>Escalation Percent</th> 
                    <td>{selectedRowData.escalationPercent}</td>{" "}
                  </tr>
                  
                  <tr>
                    <th>Termination Date</th> 
                    <td>{formatDate(selectedRowData.terminationDate)}</td>{" "}
                  </tr>

                  <tr>
                    <th>Rent Pay Start Date</th>
                    <td>{formatDate(selectedRowData.rentPayStartDate)}</td>
                  </tr>
                  <tr>
                    <th>Agreement End Date</th>
                    <td>{formatDate(selectedRowData.agreementEndDate)}</td>
                  </tr>

                  
                </tbody>
              </table>
            </div>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseRowDetailModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      <RentPredictionModal
        show={showRentPredictionModal}
        onHide={() => setShowRentPredictionModal(false)}
        agreementId={selectedAgreementIdForRent}
        agreementStartDate={selectedAgreementStartDate}
      />

      <RentAgreementTerminationModal
        show={showTerminationModal}
        onHide={handleCloseTerminationModal}
        agreementId={selectedAgreementIdForTermination}
      />
      

    </div>
  );
}

export default RentAgreementSearch;
