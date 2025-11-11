import React, { useState, useContext } from "react";
import { Formik, Field, Form, ErrorMessage } from "formik";
import * as Yup from "yup";
import "bootstrap/dist/css/bootstrap.min.css";
import "./sitesearch.css";
import { TbBuildingBank } from "react-icons/tb";
import { FaPencilAlt } from "react-icons/fa";
import { Modal, Button } from "react-bootstrap";
import SiteAddEdit from "./siteaddedit";
import DataTable from "react-data-table-component";
import { searchSiteMasterDetails, searchAllSiteMasterDetails } from '../../../../../services/sitemasterservice';
import { toast } from 'react-toastify';


interface FormValues {
  // this is for the formvalues that we're sending through json
  sitecode: string;
  atmid: string;
  sitestatus: boolean;
  bank: string;
  location: string;
}

interface Site {
  // this is for the table data
  siteId: number;
  atmId: string;
  siteArea:number;
  bank: any;
  siteAddress: string;
  siteCode: string;
  siteStatus: boolean;
  siteStatusStr: string;
  siteATMs: string;
  projectName: string;
  state: string;
  district: string;
  
}

interface ModaMode {
  onHide: any
  modeIsAdd: boolean
  siteId: number
}
const conditionalRowStyles = [
  {
    when: (row: Site) => row.siteStatus === true,
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
    when: (row: Site) => row.siteStatus === false,
    style: {
      backgroundColor: '#ffefef',
      minHeight: '40px',
      color: 'black',
      '&:hover': {
        cursor: 'pointer',
        backgroundColor: '#ffd7d6'
      },
    },
  },
];

function SiteSearch() {
  const [showModal, setShowModal] = useState(false);
  const handleCloseModal = () => setShowModal(false);
  const [modalModeValues, setModalModeValues] = useState<ModaMode>({
    onHide: handleCloseModal,
    modeIsAdd: true,
    siteId: 0
  });
  const [isLoading, setIsLoading] = useState(false);
  const [siteMasterList, setSiteMasterList] = useState<Site[]>([]);
  const [searchResults, setSearchResults] = useState<Site[]>([]);
  const [selectedSite, setSelectedSite] = useState(false);
  const [filterText, setFilterText] = useState('');
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);
 // New state for the row detail modal
 const [showRowDetailModal, setShowRowDetailModal] = useState(false);
 const [selectedRowData, setSelectedRowData] = useState<Site | null>(null);


  const handleShowModalAdd = () => {
    setShowModal(true);
    setSelectedSite(false);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: true,
      siteId: 0
    })
  };

  const handleShowModalEdit = (siteId: number) => {
    setShowModal(true);
    setSelectedSite(true);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: false,
      siteId: siteId
    })
  };
  // Function to handle double click on a row
  const handleRowDoubleClick = (row: Site) => {
    setSelectedRowData(row);
    setShowRowDetailModal(true);
  };
// Function to close the row detail modal
  const handleCloseRowDetailModal = () => {
    setShowRowDetailModal(false);
    setSelectedRowData(null);
  };

  const columns = [
    {
      name: "Site Code",
      selector: (row: Site) => row.siteCode,
      sortable: true,
      id: 'sitecode',
    },
    {
      name: "Site ATMs",
      selector: (row: Site) => row.siteATMs,
      sortable: true,
    },
    {
      name: "Bank",
      selector: (row: Site) => row.bank,
      sortable: true,
    },
    {
      name: "Status",
      selector: (row: Site) => row.siteStatusStr,
      sortable: true,
    },
    {
      name: "Address",
      selector: (row: Site) => row.siteAddress,
      sortable: true,
    },
    {
      name: "Project Name",
      selector: (row: Site) => row.projectName,
      sortable: true,
      
    },
    {
      name: "District",
      selector: (row: Site) => row.district,
      sortable: true,
    },
    {
      name: "State",
      selector: (row:Site) => row.state,
      sortable: true,
     },
    {
      name: "Actions",
      cell: (row: Site) => (
        <a style={{ color: "#f68500" }} title="Edit Site Details"
          onClick={() => { handleShowModalEdit(row.siteId); }}>
          <FaPencilAlt />
        </a>
      ),

    },
  ];


  const validationSchema = Yup.object().shape({
    sitecode: Yup.string()
      .min(3, "Site code must be at least 3 characters")
      .max(20, "Site code must not exceed 20 characters")
      .matches(/^[a-zA-Z0-9]+$/i, "Invalid Site code"),
    atmid: Yup.string()
      .min(5, "ATM ID must be at least 5 characters")
      .max(25, "ATM ID must not exceed 25 characters")
      .matches(/^[a-zA-Z0-9]+$/i, "Invalid ATM ID"),
    bank: Yup.string()
      .min(2, "Bank name must be at least 2 characters")
      .max(50, "Bank name must not exceed 50 characters")
      .matches(/^[a-zA-Z0-9 ]+$/i, "Invalid Bank name"),
    location: Yup.string()
      .min(2, "Location must be at least 2 characters")
      .max(100, "Location must not exceed 100 characters")
      .matches(/^[a-zA-Z0-9]+$/i, "Invalid Location"),
  });


  const initialValues: FormValues = {
    sitecode: "",
    atmid: "",
    sitestatus: false,
    bank: "",
    location: "",
  };


  const handleSubmit = (formData: FormValues) => {
    let searchSiteMasterFn = async () => {
      const siteMasterSearchResultFromAPI = await searchSiteMasterDetails(JSON.stringify(formData));
     

      if (siteMasterSearchResultFromAPI.successstatus === undefined) {
        toast.error('Invalid Response');
        return;
      }
      

      if (siteMasterSearchResultFromAPI.successstatus === false) {
        toast.error(siteMasterSearchResultFromAPI.error.message);
        return;
      }

      const apiData: Site[] = siteMasterSearchResultFromAPI.data;
      if (apiData !== undefined && apiData !== null) {
        const apiDataMapped: Site[] = apiData.map(e => {
          // const siteStatusDesc = e.siteStatus ? 'Active' : 'InActive';
          return {
            ...e,
            bank: e.bank.bankName,
            siteStatusStr: e.siteStatus ? 'Active' : 'InActive',
          }
        });
        setSiteMasterList(apiDataMapped);
        setSearchResults(apiDataMapped);
      } else {
        toast.error('Failed to Search Site Master');
      }
    };
    searchSiteMasterFn();
  };


  const handleShowAll = () => {
    setIsLoading(true); // Start loading
    let searchAllSiteMasterFn = async () => {
      try {
        const siteMasterSearchAllResultFromAPI = await searchAllSiteMasterDetails();
        if (siteMasterSearchAllResultFromAPI.successstatus === undefined) {
          toast.error('Invalid Response');
          console.log('Invalid Response');
          return;
        }

        if (siteMasterSearchAllResultFromAPI.successstatus === false) {
          toast.error(siteMasterSearchAllResultFromAPI.error);
          console.log(siteMasterSearchAllResultFromAPI.error);
          return;
        }

        const apiData = siteMasterSearchAllResultFromAPI.data;
        console.log(apiData);
        if (apiData !== undefined && apiData !== null) {
          const apiDataMapped: Site[] = apiData.map((e: Site) => {
            return {
              ...e,
              bank: e.bank.bankName,
              siteStatusStr: e.siteStatus ? 'Active' : 'InActive',
            }
          });
          setSiteMasterList(apiDataMapped);
          setSearchResults(apiDataMapped);
        } else {
          toast.error('Failed to Search Site Master');
        }
      } finally {
        setIsLoading(false); // Stop loading regardless of success or failure
      }
    };
    searchAllSiteMasterFn();
  };


  //  search data logic
  const filteredItems = searchResults.filter(
    item => {
      const searchableFields = [
        item.siteCode,
        item.siteATMs,
        item.bank,
        item.siteAddress,
        item.siteStatusStr,
        item.projectName,
        item.state,
        item.district,
      ];

      // return searchableFields.some(field =>
      //   field.toString().toLowerCase().includes(filterText.toLowerCase())
      // );

      // handle null values
      return searchableFields.some(field =>
    field?.toString().toLowerCase().includes(filterText.toLowerCase())
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

  return (
    <div className="container-fluid p-1">
      <div className="row">
        <div className="col-lg-3 col-12 px-2">
          <div className="p-3 bg-light rounded shadow-sm">
            <div className="mb-3 text-center">
              <Button variant="success" size="sm" onClick={handleShowModalAdd}>
                <TbBuildingBank className="mb-1" /> Add New Site
              </Button>
            </div>
            <Formik
              initialValues={initialValues}
              validationSchema={validationSchema}
              onSubmit={handleSubmit}
            >
              {({ errors, touched, resetForm }) => (
                <Form>
                  <p className="h5">Site Search</p>

                  <div className="row py-2">
                    <div className="col-12 mb-2">
                      <label className="col-form-label col-form-label-sm">
                        Site Code
                      </label>
                      <Field
                        name="sitecode"
                        type="text"
                        placeholder="Enter Site Code"
                        maxLength="30"
                        className={
                          "form-control form-control-sm" +
                          (errors.sitecode && touched.sitecode
                            ? " border-danger"
                            : "") +
                          (touched.sitecode ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="sitecode"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="atmid"
                        className="col-form-label col-form-label-sm"
                      >
                        ATM ID
                      </label>
                      <Field
                        type="text"
                        name="atmid"
                        placeholder="Enter ATM ID"
                        maxLength="30"
                        className={
                          "form-control form-control-sm" +
                          (errors.atmid && touched.atmid
                            ? " border-danger"
                            : "") +
                          (touched.atmid ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="atmid"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="activeStatus"
                        className="col-form-label col-form-label-sm"
                      >
                        Active Status
                      </label>
                      <Field
                        as="select"
                        name="sitestatus"
                        className={
                          "form-control form-control-sm" +
                          (errors.sitestatus && touched.sitestatus
                            ? " border-danger"
                            : "") +
                          (touched.sitestatus ? " border-success" : "")
                        }
                      >
                        <option value="">Select status</option>
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                      </Field>
                      <ErrorMessage
                        name="sitestatus"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="bank"
                        className="col-form-label col-form-label-sm"
                      >
                        Bank
                      </label>
                      <Field
                        type="text"
                        name="bank"
                        placeholder="Enter bank name"
                        className={
                          "form-control form-control-sm" +
                          (errors.bank && touched.bank
                            ? " border-danger"
                            : "") +
                          (touched.bank ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="bank"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="location"
                        className="col-form-label col-form-label-sm"
                      >
                        Location
                      </label>
                      <Field
                        type="text"
                        name="location"
                        placeholder="Enter location city"
                        className={
                          "form-control form-control-sm" +
                          (errors.location && touched.location
                            ? " border-danger"
                            : "") +
                          (touched.location ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="location"
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
                          setSiteMasterList([]);
                          setSearchResults([]);
                        }
                        }
                      >Clear</Button>

                    
                      <Button
                        type="button"
                        variant="outline-secondary"
                        size="sm"
                        className="mx-1 my-2"
                        style={{ display: "inline-block" }}
                        onClick={handleShowAll}
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <>
                            <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                            Loading...
                          </>
                        ) : (
                          'Show All'
                        )}
                      </Button>

                      <Button
                        type="submit"
                        variant="primary"
                        size="sm"
                        className="mx-1 my-2"
                      >Search</Button>

                    </div>
                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </div>

        {siteMasterList.length > 0 && (
          <div className="col-lg-9 col-12 px-2">
            <div className="row justify-content-end">
              <div className="col-12 pt-2 p-1">
                <div className="table-responsive">

                  <DataTable
                    title=""
                    columns={columns}
                    data={filteredItems}
                    pagination
                    fixedHeader
                    conditionalRowStyles={conditionalRowStyles}
                    onRowDoubleClicked={handleRowDoubleClick}
                    defaultSortFieldId="sitecode"
                    defaultSortAsc={true}
                    subHeader
                    subHeaderComponent={SubHeaderComponent}
                    paginationResetDefaultPage={resetPaginationToggle}
                  />
                </div>
              </div>
            </div>
          </div>)}
      </div>

      <Modal show={showModal} onHide={handleCloseModal} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
        <Modal.Header closeButton>
          <Modal.Title>{selectedSite ? "Edit Site" : "Add Site"}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="overflow-auto" style={{ maxHeight: "50vh" }}>
          <SiteAddEdit
            onHide={modalModeValues.onHide}
            modeIsAdd={modalModeValues.modeIsAdd}
            siteId={modalModeValues.siteId}
          />
        </Modal.Body>
      </Modal>



      <Modal show={showRowDetailModal} onHide={handleCloseRowDetailModal}>
        <Modal.Header>
          <Modal.Title>Site Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedRowData && (
            <div>
              <table className="modal-table">
                <tbody>
                  <tr>
                    <th>Site Code</th>
                    <td>{selectedRowData.siteCode}</td>
                  </tr>
                  <tr>
                    <th>Site Area</th>
                    <td>{selectedRowData.siteArea}</td>
                  </tr>
                  <tr>
                    <th>Bank</th>
                    <td>{selectedRowData.bank}</td>
                  </tr>
                  <tr>
                    <th>Status</th>
                    <td>{selectedRowData.siteStatusStr}</td>
                  </tr>
                  <tr>
                    <th>Address</th>
                    <td>{selectedRowData.siteAddress}</td>
                  </tr>
                  <tr>
                    <th>Site ATMs</th>
                    <td>{selectedRowData.siteATMs}</td>
                  </tr>

                  <tr>
                    <th>
                      Project 
                    </th>
                    <td>{selectedRowData.projectName}</td>
                  </tr>
                  <tr>
                    <th>
                      State
                    </th>
                    <td>{selectedRowData.state}</td>
                  </tr>
                  <tr>
                    <th>
                      District
                    </th>
                    <td>{selectedRowData.district}</td>
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


    </div>
  );
}

export default SiteSearch;

