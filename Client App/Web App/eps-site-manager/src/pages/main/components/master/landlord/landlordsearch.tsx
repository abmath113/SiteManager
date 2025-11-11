import React, { useState, useContext } from "react";
import { Formik, Field, Form, ErrorMessage } from "formik";
import * as Yup from "yup";
import "bootstrap/dist/css/bootstrap.min.css";
import "./landlordsearch.css";
import { IoPersonAdd } from "react-icons/io5";

import LandlordAddEdit from "./landlordaddedit";
import { Modal, Button } from "react-bootstrap";
import { searchAllLandlordMasterDetails,searchLandlordMasterDetails } from "../../../../../services/landlordmasterservice";
import DataTable from "react-data-table-component";
import { FaPencilAlt } from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

interface FormValues {
  name: string;
  location: string;
  activeStatus: any;
}
interface LandlordAddEditProps {
  onHide: () => void;
  modeIsAdd: boolean;
  landlordId: number;
}
interface Landlord {
  // this is for the table data
  landlordId: number;
  createdBy: string;
  createdOn: Date;
  name: string;
  beneficiaryName: string;
  mobileNo: string;
  ifscCode: string;
  accountNo: string;
  pan: string;
  status: boolean;
  landlordStatusStr: string;
  aadharNo: string;
  address: string;
  gst:boolean;
  landlordGstStr: string; 
}
interface ModaMode {
  // for modal
  onHide: any;
  modeIsAdd: boolean;
  landlordId: number;
}
const conditionalRowStyles = [
  {
    when: (row: Landlord) => row.status === true,
    style: {
      backgroundColor: "#ddffde",
      minHeight: "40px",
      color: "black",
      "&:hover": {
        cursor: "pointer",
        backgroundColor: "#c6ffc6",
      },
    },
  },
  {
    when: (row: Landlord) => row.status === false,
    style: {
      backgroundColor: "#ffefef",
      minHeight: "40px",
      color: "black",
      "&:hover": {
        cursor: "pointer",
        backgroundColor: "#ffd7d6",
      },
    },
  },
];

function LandlordSearch() {
  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedLandlord(false);
    // Reset modal mode values to default when closing
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: true,
      landlordId: 0,
    });
  };
  const [showModal, setShowModal] = useState(false);
  const handleShowModal = () => {
    // Reset to initial state when adding new landlord
    setSelectedLandlord(false);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: true,
      landlordId: 0,
    });
    setShowModal(true);
  };
  const [modalModeValues, setModalModeValues] = useState<ModaMode>({
    onHide: handleCloseModal,
    modeIsAdd: true,
    landlordId: 0,
  });
  // const appNotification :any = useContext(AppNotification);

  const [landlordMasterList, setLandlordMasterList] = useState<Landlord[]>([]);
  const [searchResults, setSearchResults] = useState<Landlord[]>([]);
  const [selectedLandlord, setSelectedLandlord] = useState(false);

  const handleShowModalEdit = (landlordId: number) => {
    setShowModal(true);
    setSelectedLandlord(true);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: false,
      landlordId: landlordId,
    });
  };

  // New state for the row detail modal
  const [showRowDetailModal, setShowRowDetailModal] = useState(false);
  const [selectedRowData, setSelectedRowData] = useState<Landlord | null>(null);

  // Function to handle double click on a row
  const handleRowDoubleClick = (row: Landlord) => {
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
      name: "Landlord Name",
      selector: (row: Landlord) => row.name,
      sortable: true,
      id: "name",
    },

    {
      name: "Account No.",
      selector: (row: Landlord) => row.accountNo,
      sortable: true,
    },
    {
      name: "Mobile No.",
      selector: (row: Landlord) => row.mobileNo,
      sortable: true,
    },
    {
      name: "Landlord Status",
      selector: (row: Landlord) => (row.status ? "Active" : "Inactive"),
      sortable: true,
    },
    {
      name: "Address",
      selector: (row: Landlord) => row.address,
      sortable: true,
    },
    {
      name: "GST",
       selector: (row: Landlord) =>
    row.gst === null || row.gst ===undefined
      ? "NA"
      : row.gst
      ? "Yes"
      : "No",
      sortable: true,
      
    },
    {
      name: "Actions",
      cell: (row: Landlord) => (
        <a
          style={{ color: "#f68500" }}
          title="Edit Landlord Details"
          onClick={() => {
            handleShowModalEdit(row.landlordId);
          }}
        >
          <FaPencilAlt />
        </a>
      ),
    },
  ];
  const validationSchema = Yup.object({
    name: Yup.string(),
    location: Yup.string(),
    activeStatus: Yup.boolean(),
  });

  const initialValues: FormValues = {
    name: "",
    location: "",
    activeStatus: "",
  };

  const handleSubmit = async (values: FormValues) => {
    // Check if at least one search field is filled
    if (
      values.location === "" &&
      values.name === "" &&
      values.activeStatus === ""
    ) {
      toast.error("At least one search field required");
      return;
    }
  
    setIsLoading(true);
  
    try {
      // Prepare search DTO
      const searchDTO = {
        name: values.name || "",
        location: values.location || "",
        status: values.activeStatus === "" 
          ? null 
          : values.activeStatus === "true"
      };
  
      const searchResult = await searchLandlordMasterDetails(searchDTO);
  
      if (searchResult.successstatus === false) {
        toast.error(searchResult.error || "Search failed");
        return;
      }
  
      if (searchResult.data && searchResult.data.length > 0) {
        const apiDataMapped: Landlord[] = searchResult.data.map((e: Landlord) => ({
          ...e,
          landlordStatusStr: e.status ? "Active" : "InActive",
           isGST: e.gst ? "Yes" : "No"
           
        }));
  
        setLandlordMasterList(apiDataMapped);
        setSearchResults(apiDataMapped);
        toast.success(`Found ${apiDataMapped.length} landlord(s)`);
      } else {
        setLandlordMasterList([]);
        setSearchResults([]);
        toast.info("No landlords found matching the search criteria");
      }
    } catch (error) {
      console.error("Search error:", error);
      toast.error("An error occurred during search");
    } finally {
      setIsLoading(false);
    }
  };
  const [isLoading, setIsLoading] = useState(false);

  const handleShowAll = () => {
    let searchAllLandlordMasterFn = async () => {
      setIsLoading(true);
      try {
        const landlordMasterSearchAllResultFromAPI =
          await searchAllLandlordMasterDetails();
        if (landlordMasterSearchAllResultFromAPI.successstatus === undefined) {
          toast.error("Invalid Response");
          return;
        }

        if (landlordMasterSearchAllResultFromAPI.successstatus === false) {
          toast.error(landlordMasterSearchAllResultFromAPI.error.message);
          return;
        }
        const apiData = landlordMasterSearchAllResultFromAPI.data;

        if (apiData !== undefined && apiData !== null) {
          const apiDataMapped: Landlord[] = apiData.map((e: Landlord) => {
            const statusDesc = e.status ? "Active" : "InActive";
             const gstDesc =
    e.gst === null || e.gst === undefined
      ? "NA"
      : e.gst === true
      ? "Yes"
      : "No";
            return {
              ...e,
              landlordStatusStr: statusDesc,
              landlordGstStr: gstDesc,
            };
          });
          setLandlordMasterList(apiDataMapped);
          setSearchResults(apiDataMapped);
          toast.success("Landlord data fetched successfully");
        }
      } catch (error) {
        console.error("Error fetching landlord data:", error);
        toast.error("An error occurred while fetching landlord data");
      } finally {
        setIsLoading(false);
      }
    };
    searchAllLandlordMasterFn();
  };

  // Searching table
  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    let landlordNameValue: Boolean;
    let landlordAccNoValue: Boolean;
    let landlordMobNoValue: Boolean;
    let landlordAddressValue: Boolean;

    const searchValue = e.target.value.toLowerCase().trim();
    let searchKeyGroups;
    if (searchValue.includes("&")) {
      searchKeyGroups = searchValue.split("&");
    } else {
      searchKeyGroups = [searchValue];
    }

    let groupFilteredRows: Landlord[] = [];
    searchKeyGroups.forEach((searchKeyGroupVal) => {
      let searchKeywords;
      if (searchKeyGroupVal.includes(" ")) {
        searchKeywords = searchKeyGroupVal.split(" ");
      } else {
        searchKeywords = [searchKeyGroupVal];
      }
      let allFilteredRows: Landlord[] = [...landlordMasterList];
      searchKeywords.forEach((value) => {
        const filteredRows: Landlord[] = allFilteredRows.filter((row) => {
          landlordNameValue = row.name.toString().toLowerCase().includes(value);
          landlordAccNoValue = row.accountNo.toLowerCase().includes(value);
          landlordMobNoValue = row.mobileNo.toLowerCase().includes(value);
          landlordAddressValue = row.address.toLowerCase().includes(value);

          if (landlordNameValue) {
            return { ...row };
          } else if (landlordAccNoValue) {
            return { ...row };
          } else if (landlordMobNoValue) {
            return { ...row };
          } else if (landlordAddressValue) {
            return { ...row };
          }
        });
        allFilteredRows.pop();
        allFilteredRows = filteredRows;
      });
      groupFilteredRows.push(...allFilteredRows);
    });
    setSearchResults([...new Set(groupFilteredRows)]);
  };

  const [filterText, setFilterText] = useState("");
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);

  // Add filtered data logic
  const filteredItems = searchResults.filter((item) => {
    const searchableFields = [
      item.name,
      item.accountNo,
      item.mobileNo,
      item.address,
      item.status ? "Active" : "Inactive",
      item.gst !== null && item.gst !== undefined ? (item.gst ? "Yes" : "No") : "NA",
    ];

    return searchableFields.some((field) =>
      field.toString().toLowerCase().includes(filterText.toLowerCase())
    );
  });

  // Add subheader component
  const SubHeaderComponent = React.useMemo(() => {
    const handleClear = () => {
      if (filterText) {
        setResetPaginationToggle(!resetPaginationToggle);
        setFilterText("");
      }
    };

    return (
      <div style={{ display: "flex", alignItems: "center" }}>
        <input
          type="text"
          className="form-control form-control-sm"
          placeholder="Search records..."
          value={filterText}
          onChange={(e) => setFilterText(e.target.value)}
          style={{ width: "200px", marginRight: "10px" }}
        />
        <button className="btn btn-sm btn-secondary" onClick={handleClear}>
          Clear
        </button>
      </div>
    );
  }, [filterText, resetPaginationToggle]);

  return (
    <div className="container-fluid p-1">
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
      />
      <div className="row">
        <div className="col-md-3">
          <div className="p-3 bg-light rounded shadow-sm">
            <div className="mb-3 text-center">
              <button
                className="btn btn-primary btn-sm btn-success"
                onClick={handleShowModal}
              >
                <IoPersonAdd className="mb-1" /> Add New Landlord
              </button>
            </div>
            <Formik
              initialValues={initialValues}
              validationSchema={validationSchema}
              onSubmit={handleSubmit}
            >
              {({ errors, touched, resetForm }) => (
                <Form>
                  <p className="h5">Landlord Search</p>

                  <div className="row py-2">
                    <div className="col-12 mb-2">
                      <label className="col-form-label col-form-label-sm">
                        Name
                      </label>
                      <Field
                        name="name"
                        type="text"
                        placeholder="Enter Name"
                        maxLength="30"
                        className={
                          "form-control form-control-sm" +
                          (errors.name && touched.name
                            ? " border-danger"
                            : "") +
                          (touched.name ? " border-success" : "")
                        }
                      />
                      <ErrorMessage
                        name="name"
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
                        placeholder="Enter Location"
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

                    <div className="col-12 mb-2">
                      <label
                        htmlFor="activeStatus"
                        className="col-form-label col-form-label-sm"
                      >
                        Active Status
                      </label>
                      <Field
                        as="select"
                        name="activeStatus"
                        className={
                          "form-control form-control-sm" +
                          (errors.activeStatus && touched.activeStatus
                            ? " border-danger"
                            : "") +
                          (touched.activeStatus ? " border-success" : "")
                        }
                      >
                        <option value="">Select status</option>
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                      </Field>
                      <ErrorMessage
                        name="activeStatus"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="form-group"></div>
                  </div>

                  <div className="row py-2">
                    <div className="col-12 mb-1 text-end">
                      <Button
                        variant="secondary"
                        size="sm"
                        className="mx-1 my-2"
                        onClick={() => {
                          resetForm();
                          setLandlordMasterList([]);
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
                        style={{ display: "inline-block", minWidth: "80px" }}
                        onClick={handleShowAll}
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <div className="d-flex align-items-center">
                            <span
                              className="spinner-border spinner-border-sm me-1"
                              role="status"
                              aria-hidden="true"
                            ></span>
                            Loading...
                          </div>
                        ) : (
                          "Show All"
                        )}
                      </Button>

                      <Button
                        type="submit"
                        variant="primary"
                        size="sm"
                        className="mx-1 my-2"
                      >
                        Search
                      </Button>
                    </div>
                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </div>

        {landlordMasterList.length > 0 && (
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
                    defaultSortFieldId="name"
                    defaultSortAsc={true}
                    subHeader
                    subHeaderComponent={SubHeaderComponent}
                    paginationResetDefaultPage={resetPaginationToggle}
                  />
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      <Modal
        show={showModal}
        onHide={handleCloseModal}
        dialogClassName="modal-60w"
      >
        <Modal.Header closeButton>
          <Modal.Title>
            {selectedLandlord ? "Edit Landlord" : "Add Landlord"}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <LandlordAddEdit
          key={modalModeValues.landlordId || 'new'}
          onHide={handleCloseModal}
          modeIsAdd={modalModeValues.modeIsAdd}
          landlordId={modalModeValues.landlordId}
        />
        </Modal.Body>
      </Modal>

      <Modal show={showRowDetailModal} onHide={handleCloseRowDetailModal}>
        <Modal.Header>
          <Modal.Title>Landlord Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedRowData && (
            <div>
              <table className="modal-table">
                <tbody>
                  <tr>
                    <th>Landlord Name</th>
                    <td>{selectedRowData.name}</td>
                  </tr>
                  <tr>
                    <th>Beneficiary Name</th>
                    <td>{selectedRowData.beneficiaryName}</td>
                  </tr>
                  <tr>
                    <th>Account No.</th>
                    <td>{selectedRowData.accountNo}</td>
                  </tr>
                  <tr>
                    <th>Status</th>
                    <td>{selectedRowData.landlordStatusStr}</td>
                  </tr>
                  <tr>
                    <th>Address</th>
                    <td>{selectedRowData.address}</td>
                  </tr>
                  <tr>
                    <th>Aadhar No.</th>
                    <td>{selectedRowData.aadharNo}</td>
                  </tr>
                  <tr>
                    <th>IFSC Code</th>
                    <td>{selectedRowData.ifscCode}</td>
                  </tr>
                  <tr>
                    <th>Mobile No.</th>
                    <td>{selectedRowData.mobileNo}</td>
                  </tr>
                  <tr>
                    <th>PAN</th>
                    <td>{selectedRowData.pan}</td>
                  </tr>

                  <tr>
                    <th>GST</th>
                    <td>
                      {selectedRowData.landlordGstStr}
                    </td>
                  </tr>

                  <tr>
                    <th>Created By</th>
                    <td>{selectedRowData.createdBy}</td>
                  </tr>
                  
                  {/* <tr>
                  <th>Created On</th>
                  <td>{selectedRowData.createdOn}</td>
                </tr>  */}
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

export default LandlordSearch;
