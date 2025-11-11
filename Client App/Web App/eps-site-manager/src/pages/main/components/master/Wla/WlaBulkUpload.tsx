import React, { useState } from 'react';
import { toast } from 'react-toastify';
import { FaUpload, FaDownload, FaPencilAlt } from 'react-icons/fa';
import { TbBuildingBank } from 'react-icons/tb';
import { Modal, Button } from 'react-bootstrap';
import DataTable from 'react-data-table-component';

import { downloadWLABulkUploadTemplate, uploadBulkWLABancs, getAllWLAMasterDetails } from '../../../../../services/wlaservice';
import WlaAddEdit from './WlaAddEdit';

interface WlaBulkUploadProps {
  // Add any props needed
}

// Define the interface for our WLA data based on your JSON structure
interface WLAData {
  wlaBancId: number;
  epsSiteCode: string;
  siteType: string;
  siteSubtype: string;
  siteStatus: string;
  locationName: string;
  // Other fields exist but we'll only display the first 6 columns
}

interface ModalMode {
  onHide: () => void;
  modeIsAdd: boolean;
  wlaBancId?: number;
}

const conditionalRowStyles = [
  {
    when: (row: WLAData) => row.siteStatus === 'Cash Live',
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
    when: (row: WLAData) => row.siteStatus !== 'Cash Live',
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

const WlaBulkUpload: React.FC<WlaBulkUploadProps> = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [isUploading, setIsUploading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [wlaData, setWlaData] = useState<WLAData[]>([]);
  const [loading, setLoading] = useState(false);
  const [filterText, setFilterText] = useState('');
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);
  const [selectedWla, setSelectedWla] = useState(false);

  const [modalModeValues, setModalModeValues] = useState<ModalMode>({
    onHide: () => setShowModal(false),
    modeIsAdd: true,
    wlaBancId: 0
  });

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      setSelectedFile(file);
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
      const response = await uploadBulkWLABancs(formData);

      if (response.successstatus && response.data) {
        const blob = new Blob([response.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'processed_wla_master.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();

        toast.success('Bulk upload processed successfully. Downloading results...');

        setSelectedFile(null);
        const fileInput = document.getElementById('wlaBulkUploadFile') as HTMLInputElement;
        if (fileInput) fileInput.value = '';
      } else {
        toast.error(response.error || 'Failed to process wla bulk upload');
      }
    } catch (error) {
      toast.error('Error processing wla bulk upload');
      console.error('Bulk upload error:', error);
    } finally {
      setIsUploading(false);
    }
  };

  const handleDownloadTemplate = async () => {
    try {
      const response = await downloadWLABulkUploadTemplate();

      if (response.successstatus === true && response.data) {
        const blob = new Blob([response.data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'wla_bulk_upload_template.xlsx');
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

  // Handle closing the WlaAddEdit modal
  const handleCloseModal = () => {
    setShowModal(false);
  };

  // Function to open add modal
  const handleShowAddModal = () => {
    setShowModal(true);
    setSelectedWla(false);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: true,
      wlaBancId: 0
    });
  };

  // Function to open edit modal
  const handleShowEditModal = (wlaBancId: number) => {
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: false,
      wlaBancId: wlaBancId
    });
    setSelectedWla(true);
    setShowModal(true);
  };

  // Function to fetch WLA data
  const handleShowAll = async () => {
    setLoading(true);
    try {
      const response = await getAllWLAMasterDetails();

      if (response.successstatus === undefined) {
        toast.error('Invalid Response: The response from the server is undefined.');
        return;
      }

      if (response.successstatus === false) {
        toast.error(`Error: ${response.error}`);
        return;
      }

      const apiData = response.data;

      if (apiData !== undefined && apiData !== null) {
        setWlaData(apiData);
        toast.success('WLA data fetched successfully');
      }
    } catch (error) {
      toast.error('Error fetching WLA data');
      console.error('Data fetch error:', error);
    } finally {
      setLoading(false);
    }
  };

  // Filter data based on search term
  const filteredItems = wlaData.filter(item => {
    const searchableFields = [
      item.wlaBancId?.toString(),
      item.epsSiteCode,
      item.siteType,
      item.siteSubtype,
      item.siteStatus,
      item.locationName
    ];

    return searchableFields.some(field =>
      field?.toLowerCase().includes(filterText.toLowerCase())
    );
  });

  // Define table columns
  const columns = [
    {
      name: "WLA Banc ID",
      selector: (row: WLAData) => row.wlaBancId,
      sortable: true,
    },
    {
      name: "EPS Site Code",
      selector: (row: WLAData) => row.epsSiteCode,
      sortable: true,
    },
    {
      name: "Site Type",
      selector: (row: WLAData) => row.siteType,
      sortable: true,
    },
    {
      name: "Site Subtype",
      selector: (row: WLAData) => row.siteSubtype || '-',
      sortable: true,
    },
    {
      name: "Site Status",
      selector: (row: WLAData) => row.siteStatus,
      sortable: true,
    },
    {
      name: "Location Name",
      selector: (row: WLAData) => row.locationName,
      sortable: true,
    },
    {
      name: "Actions",
      cell: (row: WLAData) => (
        <a style={{ color: "#f68500" }} title="Edit WLA Details"
          onClick={() => { handleShowEditModal(row.wlaBancId); }}>
          <FaPencilAlt />
        </a>
      ),
    },
  ];

  // Add subheader component for search
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
        <div className="col-md-3">
          <div className="p-3 bg-light rounded shadow-sm">
            <div className="mb-3 text-center">
              <button className="btn btn-primary btn-sm btn-success" onClick={handleShowAddModal}>
                <TbBuildingBank className="mb-1 me-1" /> Add New WLA Bank
              </button>
            </div>

            <div className="mb-3">
              <div className="input-group input-group-sm">
                <input
                  type="file"
                  className="form-control form-control-sm"
                  id="wlaBulkUploadFile"
                  accept=".xlsx,.xls"
                  onChange={handleFileSelect}
                />
              </div>
            </div>

            <div className="row mb-3">
              <div className="col-12">
                <button
                  className="btn btn-outline-primary btn-sm w-100"
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
                      <FaUpload className="me-1" /> Upload
                    </>
                  )}
                </button>
              </div>
            </div>

            <div className="row mb-3">
              <div className="col-12">
                <button
                  className="btn btn-outline-secondary btn-sm w-100"
                  onClick={handleDownloadTemplate}
                >
                  <FaDownload className="me-1" /> Download Template
                </button>
              </div>
            </div>

            <div className="row py-2">
              <div className="col-12 text-center">
                <Button
                  variant="primary"
                  size="sm"
                  className="w-80"
                  onClick={handleShowAll}
                  disabled={loading}
                >
                  {loading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                      Loading...
                    </>
                  ) : (
                    'Show All WLA Data'
                  )}
                </Button>
              </div>
            </div>
          </div>
        </div>

        {wlaData.length > 0 && (
          <div className="col-lg-9 col-12 px-2">
            <div className="row justify-content-end">
              <div className="col-12 pt-2 p-1">
                <div className="table-responsive">
                  <DataTable
                    title=""
                    columns={columns}
                    data={filteredItems}
                    conditionalRowStyles={conditionalRowStyles}
                    pagination
                    fixedHeader
                    defaultSortFieldId="wlaBancId"
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
      size="xl"
      centered
      show={showModal} 
      onHide={handleCloseModal} 
      dialogClassName="modal-40w">
        <Modal.Header closeButton>
          <Modal.Title>{selectedWla ? "Edit WLA Bank" : "Add WLA Bank"}</Modal.Title>
        </Modal.Header>
        <Modal.Body style={{ maxHeight: '70vh', overflowY: 'auto' }}>
          <WlaAddEdit
            show={true} 
            onHide={handleCloseModal}
            modeIsAdd={modalModeValues.modeIsAdd}
            wlaBancId={modalModeValues.wlaBancId}
          />
        </Modal.Body>

      </Modal>
    </div>
  );
};

export default WlaBulkUpload;