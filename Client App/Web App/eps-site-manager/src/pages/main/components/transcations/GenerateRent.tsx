import React, { useState, useContext, useMemo } from 'react';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import 'bootstrap/dist/css/bootstrap.min.css';
import { DateTime } from 'luxon';
import DataTable from "react-data-table-component";
import { TableColumn } from "react-data-table-component";
import { AppNotification } from '../../Main';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { GenerateRentService } from '../../../../services/generaterentservice';
//import { CSVLink } from 'react-csv';
import ExcelJS from 'exceljs';
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
interface RentData {
  siteCode: string;
  siteStatus: boolean;
  landlordName: string;
  status: string;
  monthlyRent: number;
  rentToPay: string;
  paymentInterval: string;
  landlordMobileNo : string;
  landlordIFSC : string;
  landlordAccountNo : string;
  landlordGSTStatus : boolean;
}

interface FormValues {
  selectedMonth: string;
}

const GenerateRent: React.FC = () => {
  const [rentDataList, setRentDataList] = useState<RentData[]>([]);
  const [exportData, setExportData] = useState<RentData[]>([]);
  const [filterText, setFilterText] = useState('');
  const appNotification: any = useContext(AppNotification);
  const [isLoading, setIsLoading] = useState(false);

  const currentMonth = DateTime.local().toFormat('yyyy-MM');

  const [selectedMonth, setSelectedMonth] = useState(currentMonth);
  const validationSchema = Yup.object({
    selectedMonth: Yup.string().required('Month and year are required'),
  });

  const initialValues: FormValues = {
    selectedMonth: currentMonth,
  };

  const columns: TableColumn<RentData>[] = [
    {
      name: "Site Code",
      selector: (row: RentData) => row.siteCode,
      sortable: true,
      width: '120px',
      wrap: true,
    },
     {
      name: "Status",
      selector: (row: RentData) => (row.siteStatus ? "Active" : "Inactive"),
      sortable: true,
      width: '100px',
      wrap: true,
    },
   
    {
      name: "GST",
       selector: (row: RentData) => row.landlordGSTStatus === null || row.landlordGSTStatus === undefined ? "NA" : row.landlordGSTStatus ? "Yes" : "No",
      sortable: true,
      
    },
   {
      name: "Landlord Name",
      selector: (row: RentData) => row.landlordName,
      sortable: true,
      wrap: true,
      grow: 2,
    },
        {
      name: "Monthly/Quarterly/Halfyearly",
      selector: (row: RentData) => row.paymentInterval,
      sortable: true,
      width: '150px',
    },
    {
      name: "Monthly Rent",
      selector: (row: RentData) => row.monthlyRent,
      sortable: true,
      right: true,
      width: '150px',
    },
  
     {
      name: DateTime.fromFormat(selectedMonth, "yyyy-MM").toFormat("MMMM yyyy"),
      selector: (row: RentData) => row.rentToPay,
      sortable: true,
      right: true,
      width: '150px',
    },
     {
      name: "Mo. No.",
      selector: (row: RentData) => row.landlordMobileNo,
      sortable: true,
      width: '120px',
      wrap: true,
    },
     {
      name: "IFSC Code",
      selector: (row: RentData) => row.landlordIFSC,
      sortable: true,
      width: '120px',
      wrap: true,
    },
    {
      name: "A/c No.",
      selector: (row: RentData) => row.landlordAccountNo,
      sortable: true,
      width: '120px',
      wrap: true,
    },
   
   
   
     
  ];




const exportToExcel = async (data: any[], fileName: string, selectedMonth: string) => {
  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet('Rent Data');
  // Add header row
  const headerRow = [
    "Site Code",
    "Status",
    "GST",
    "Landlord Name",
    "Monthly/Quarterly/Halfyearly",
    "Monthly Rent",
    `${selectedMonth}`,
    "Mo. No.",
    "IFSC Code",
    "A/c No."
  ];




  const row = worksheet.addRow(headerRow);

  // Style header
  row.eachCell((cell) => {
    cell.fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: '4F81BD' } // blue
    };
    cell.font = { color: { argb: 'FFFFFF' }, bold: true };
    cell.alignment = { horizontal: 'center' };
  });

  // Add data rows
  data.forEach(item => {
    worksheet.addRow([
       item.siteCode,
    item.siteStatus ? "Active" : "Inactive",
    item.landlordGSTStatus === null || item.landlordGSTStatus === undefined
      ? "NA"
      : item.landlordGSTStatus
      ? "Yes"
      : "No",
       item.landlordName,
       item.paymentInterval,
    item.monthlyRent,
    item.rentToPay,
   
    item.landlordMobileNo,
    item.landlordIFSC,
    item.landlordAccountNo,
    ]);
  });




  // Auto width for columns
  worksheet.columns.forEach(column => {
    let maxLength = 0;
   (row as ExcelJS.Row).eachCell((cell) => {
  cell.fill = {
    type: 'pattern',
    pattern: 'solid',
    fgColor: { argb: '4F81BD' }
  };
  cell.font = { color: { argb: 'FFFFFF' }, bold: true };
  cell.alignment = { horizontal: 'center' };
});

    column.width = maxLength + 25;
  });

  const buffer = await workbook.xlsx.writeBuffer();
  saveAs(new Blob([buffer]), `${fileName}.xlsx`);
};


  const handleSubmit = async (values: FormValues) => {
    setSelectedMonth(values.selectedMonth);
    setIsLoading(true);
    const selectedMonth = values.selectedMonth;
    const formattedDate = selectedMonth + "-01";

    let generateRentFn = async () => {
      try {
        const generatedRentFromAPI = await GenerateRentService(formattedDate);

        if (generatedRentFromAPI.successstatus === undefined) {
          toast.error('Invalid Response');
          return;
        }

        if (generatedRentFromAPI.successstatus === false) {
          toast.error(generatedRentFromAPI.error.message);
          return;
        }

        const apiData: RentData[] = generatedRentFromAPI.data.map((item: any) => ({
          ...item,
          rentToPay: item.rentToPay === -1 ? "NA" : item.rentToPay.toString()
        }));

        const currDate = DateTime.fromFormat(values.selectedMonth, 'yyyy-MM');
        setRentDataList(apiData);
        setExportData(apiData);
        toast.success(`Rent data generated successfully for ${currDate.toFormat('MMMM yyyy')}`);
      } catch (error) {
        console.error('Error generating rent data:', error);
        toast.error('Failed to generate rent data')
      } finally {
        setIsLoading(false);
      }
    }

    generateRentFn();
  };

  const csvHeaders = [
    { label: "Site Code", key: "siteCode" },
    { label: "Status", key: "status" },
    { label: "GST", key: "gst" },
    { label: "Landlord Name", key: "landlordName" },
    { label: "Monthly/Quarterly/Halfyearly", key: "paymentInterval" },
    { label: "Monthly Rent", key: "monthlyRent" },
    { label:  DateTime.fromFormat(selectedMonth, "yyyy-MM").toFormat("MMMM yyyy"), key: "rentToPay" },
    { label: "Mo. No.", key: "landlordMobileNo" },
    { label: "IFSC Code", key: "landlordIFSC" },
    { label: "A/c No.", key: "landlordAccountNo" },

  ];


  const filteredItems = useMemo(() => {
  const text = filterText.toLowerCase();

  return rentDataList.filter(item => {
    // Safely convert each property to string if exists, else ''
    const siteCode = item.siteCode ? item.siteCode.toString().toLowerCase() : '';
    const siteStatus = item.siteStatus !== undefined ? item.siteStatus.toString().toLowerCase() : '';
    const landlordGSTStatus = item.landlordGSTStatus !== undefined && item.landlordGSTStatus !== null ? item.landlordGSTStatus.toString().toLowerCase() : '';
    const landlordName = item.landlordName ? item.landlordName.toLowerCase() : '';
    const paymentInterval = item.paymentInterval ? item.paymentInterval.toLowerCase() : '';
    const monthlyRent = item.monthlyRent !== undefined && item.monthlyRent !== null ? item.monthlyRent.toString() : '';
    const rentToPay = item.rentToPay ? item.rentToPay.toString().toLowerCase() : '';
    const landlordMobileNo = item.landlordMobileNo ? item.landlordMobileNo.toLowerCase() : '';
    const landlordIFSC = item.landlordIFSC ? item.landlordIFSC.toLowerCase() : '';
    const landlordAccountNo = item.landlordAccountNo ? item.landlordAccountNo.toLowerCase() : '';

    // Return if any match
    return (
      siteCode.includes(text) ||
      siteStatus.includes(text) ||
      landlordGSTStatus.includes(text) ||
      landlordName.includes(text) ||
      paymentInterval.includes(text) ||
      monthlyRent.includes(text) ||
      rentToPay.includes(text) ||
      landlordMobileNo.includes(text) ||
      landlordIFSC.includes(text) ||
      landlordAccountNo.includes(text)
    );
  });
}, [rentDataList, filterText]);


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
          <button
            onClick={handleClear}
            className="btn btn-sm btn-outline-secondary "
            style={{
              padding: "0.25rem 0.5rem", 
              fontSize: "0.8rem", // Smaller font size
              borderRadius: "5px", // Slightly rounded corners
              transition: "background-color 0.3s ease", // Smooth hover transition
            }}
            onMouseEnter={(e) =>
              (e.currentTarget.style.backgroundColor = "#f0f0f0")
            } // Light hover effect
            onMouseLeave={(e) =>
              (e.currentTarget.style.backgroundColor = "transparent")
            }
          >
            Clear
          </button>
        </div>
       <button
   onClick={() => exportToExcel(exportData, 'rent_data', selectedMonth)}
  className="btn btn-primary btn-sm"
>
  Export to Excel
</button>

      </div>
    );
  }, [filterText, exportData, selectedMonth]);

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
              {({ errors, touched }) => (
                <Form>
                  <p className="h5">Generate Rent</p>

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
                        // min="2018-03"
                        // max={currentMonth}
                      />
                      <ErrorMessage
                        name="selectedMonth"
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
                            Generating...
                          </>
                        ) : (
                          'Generate Rent'
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

export default GenerateRent;