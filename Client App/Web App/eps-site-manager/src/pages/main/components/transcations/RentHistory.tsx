import React, { useState, useMemo } from 'react';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import 'bootstrap/dist/css/bootstrap.min.css';
import DataTable from "react-data-table-component";
import { TableColumn } from "react-data-table-component";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { CSVLink } from 'react-csv';
import { DateTime } from 'luxon';
import { uploadRentRecords } from '../../../../services/siterentrecordservice';

interface RentHistoryData {
  siteCode: string;
  // Add columns for each month, e.g.:
  
  month: number;
  // ... other months
}

interface FormValues {
  rentPaidFile: File | null;
  selectedMonth: string;
}

const RentHistory: React.FC = () => {
  const [rentHistoryData, setRentHistoryData] = useState<RentHistoryData[]>([]);
  const [filterText, setFilterText] = useState('');
  const currentMonth = DateTime.local().toFormat('yyyy-MM');
  const [isLoading, setIsLoading] = useState(false);

  const validationSchema = Yup.object({
    rentPaidFile: Yup.mixed().required('Rent paid file is required'),
    selectedMonth: Yup.string().required('Month and year are required'),
  });

  const initialValues: FormValues = {
    rentPaidFile: null,
    selectedMonth: currentMonth,
  };

  const columns: TableColumn<RentHistoryData>[] = [
    {
      name: "Site Code",
      selector: (row: RentHistoryData) => row.siteCode,
      sortable: true,
      width: '120px',
    },
    // Add columns for each month
    {
      name: "Month",
      selector: (row: RentHistoryData) => row.month,
      sortable: true,
      right: true,
    },
    // ... other months
  ];

  const handleSubmit = async (values: FormValues) => {
    if (!values.rentPaidFile || !values.selectedMonth) return;
    
    setIsLoading(true);
    const formData = new FormData();
    formData.append('file', values.rentPaidFile);
    
    try {
        const response = await uploadRentRecords(formData, values.selectedMonth);
        
        if (response.successstatus) {
            // Show success message with details from the response if available
            toast.success(
                response.data?.message || 
                'Rent history data imported successfully', 
                {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                }
            );

            // If the API returns updated data, update the state
            if (response.data?.rentHistory) {
                setRentHistoryData(response.data.rentHistory);
            }
        } else {
            // Show detailed error message
            toast.error(
                response.error || 'Failed to import rent history data', 
                {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                }
            );
        }
    } catch (error) {
        // Handle unexpected errors
        toast.error(
            'An unexpected error occurred while uploading the file', 
            {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            }
        );
    } finally {
        setIsLoading(false);
    }
  };

  const filteredItems = useMemo(() => {
    return rentHistoryData.filter(
      item => item.siteCode.toLowerCase().includes(filterText.toLowerCase())
    );
  }, [rentHistoryData, filterText]);

  const subHeaderComponentMemo = useMemo(() => {
    const handleClear = () => {
      if (filterText) {
        setFilterText('');
      }
    };

    return (
      <div className="d-flex justify-content-between align-items-center w-100 mb-3">
        <div className="d-flex pe-4">
          <input
            type="text"
            placeholder="Search..."
            value={filterText}
            onChange={(e) => setFilterText(e.target.value)}
            className="form-control form-control-sm mr-2"
          />
          <button onClick={handleClear} className="btn btn-sm btn-outline-secondary">
            Clear
          </button>
        </div>
        <CSVLink
          data={rentHistoryData}
          filename="rent_history.csv"
          className="btn btn-primary btn-sm"
        >
          Export to Excel
        </CSVLink>
      </div>
    );
  }, [filterText, rentHistoryData]);

  return (
    <div className="container-fluid p-1">
      <div className="row">
        <div className="col-md-3">
          <div className="p-1 bg-light rounded shadow-sm">
            <Formik
              initialValues={initialValues}
              validationSchema={validationSchema}
              onSubmit={handleSubmit}
            >
              {({ errors, touched, setFieldValue }) => (
                <Form>
                  <p className="h5">Import Rent History</p>

                  <div className="row p-2">
                    <div className="col-12 mb-2">
                      <label className="col-form-label col-form-label-sm">
                        Select Month and Year
                      </label>
                      <Field
                        name="selectedMonth"
                        type="month"
                        className={
                          "form-control form-control-sm" +
                          (errors.selectedMonth && touched.selectedMonth
                            ? " border-danger"
                            : "") +
                          (touched.selectedMonth ? " border-success" : "")
                        }
                        min="2000-01"
                        max={currentMonth}
                      />
                      <ErrorMessage
                        name="selectedMonth"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>

                    <div className="col-12 mb-2">
                      <label className="col-form-label col-form-label-sm">
                        Upload Rent Paid Excel File
                      </label>
                      <input
                        type="file"
                        onChange={(event) => {
                          setFieldValue("rentPaidFile", event.currentTarget.files?.[0] || null);
                        }}
                        className={
                          "form-control form-control-sm" +
                          (errors.rentPaidFile && touched.rentPaidFile
                            ? " border-danger"
                            : "")
                        }
                      />
                      <ErrorMessage
                        name="rentPaidFile"
                        component="div"
                        className="p-0 text-wrap validation-error"
                      />
                    </div>
                  </div>
                  <div className="row py-2">
                    <div className="col-12 mb-1 d-flex justify-content-center">
                      <button
                        type="submit"
                        className="btn btn-success btn-sm"
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <>
                            <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                            Importing...
                          </>
                        ) : (
                          'Import Rent History'
                        )}
                      </button>
                    </div>
                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </div>

        <div className="col-md-9">
          <div className="p-1 bg-light rounded shadow-sm">
            <DataTable
              columns={columns}
              data={filteredItems}
              pagination
              striped
              highlightOnHover
              pointerOnHover
              subHeader
              subHeaderComponent={subHeaderComponentMemo}
              persistTableHead
            />
          </div>
        </div>
      </div>
      <ToastContainer />
    </div>
  );
};

export default RentHistory;
