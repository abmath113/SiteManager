import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { IoPersonAdd } from "react-icons/io5";
import { Modal, Button } from "react-bootstrap";
import { getAllManagerDetails } from '../../../../../services/channelmanagerservice';
import ChannelManagerAddEdit from './channelmanageraddedit';
import DataTable from "react-data-table-component";
import { FaPencilAlt } from "react-icons/fa";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface ChannelManager {
  channelManagerId: number;
  channelManagerName: string;
  phoneNo: string;
  emailId: string;
  status?: boolean;
  statusStr?: string;
}

interface ModalMode {
  onHide: () => void;
  modeIsAdd: boolean;
  channelManagerId: number;
}
const conditionalRowStyles = [
  {
    when: (row: ChannelManager) => row.status === true,
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
    when: (row: ChannelManager) => row.status === false,
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



function ChannelManagerSearch() {

  const [searchResults, setSearchResults] = useState<ChannelManager[]>([]);
  const [filterText, setFilterText] = useState('');
  const [resetPaginationToggle, setResetPaginationToggle] = useState(false);
  const [ChannelManagerList, setChannelManagerList] = useState<ChannelManager[]>([]);
  const [selectedChannelManager, setSelectedChannelManager] = useState(false);


  // Add filtered data logic
  const filteredItems = ChannelManagerList.filter(
    item => {
      const searchableFields = [
        item.channelManagerName,
        // item.phoneNo, // COMMENTED FOR NOW SINCE VALUES ARE NULL UNCOMMENT LATER
        // item.emailId,
        item.status ? 'Active' : 'Inactive'
      ];

      return searchableFields.some(field =>
        field.toString().toLowerCase().includes(filterText.toLowerCase())
      );
    }
  );

  const handleCloseModal = () => setShowModal(false);
  const [showModal, setShowModal] = useState(false);


  const handleShowModal = () => {
    setShowModal(true);
    setSelectedChannelManager(false);
    setModalModeValues({  // Add this to properly reset modal values for Add mode
      onHide: handleCloseModal,
      modeIsAdd: true,
      channelManagerId: 0
    });

  };


  const [modalModeValues, setModalModeValues] = useState<ModalMode>({
    onHide: handleCloseModal,
    modeIsAdd: true,
    channelManagerId: 0
  });


  const handleShowModalEdit = (channelManagerId: number) => {
    console.log("chid---" + channelManagerId);
    setModalModeValues({
      onHide: handleCloseModal,
      modeIsAdd: false,
      channelManagerId: channelManagerId,

    });
    setSelectedChannelManager(true);
    console.log("chid" + modalModeValues.channelManagerId);
    setShowModal(true); // Open modal after state is updated
  };

  const columns = [
    {
      name: "Channel Manager",
      selector: (row: ChannelManager) => row.channelManagerName,
      sortable: true,
      id: 'channelManagerName',
    },
    {
      name: "Phone Number",
      selector: (row: ChannelManager) => row.phoneNo,
      sortable: true,
    },
    {
      name: "Email ID",
      selector: (row: ChannelManager) => row.emailId,
      sortable: true,
    },
    {
      name: "Status",
      selector: (row: ChannelManager) => row.status ? 'Active' : 'Inactive',
      sortable: true,
    },
    {
      name: "Actions",
      cell: (row: ChannelManager) => (
        <a style={{ color: "#f68500" }} title="Edit Channel Manager Details"
          onClick={() => { handleShowModalEdit(row.channelManagerId); }}>
          <FaPencilAlt />
        </a>
      ),
    },
  ];
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

  const handleShowAll = () => {
    let searchAllManagersFn = async () => {
      try {
        const channelManagerSearchAllResultFromAPI = await getAllManagerDetails();
        
        console.log('channelManagerSearchAllResultFromAPI', channelManagerSearchAllResultFromAPI);

        if (channelManagerSearchAllResultFromAPI.successstatus === undefined) {
          toast.error('Invalid Response: The response from the server is undefined.');
          return;
        }

        if (channelManagerSearchAllResultFromAPI.successstatus === false) {
          toast.error(`Error: ${channelManagerSearchAllResultFromAPI.error.message}`);
          return;
        }
        const apiData = channelManagerSearchAllResultFromAPI.data;

        if (apiData !== undefined && apiData !== null) {
          const apiDataMapped: ChannelManager[] = apiData.map((e: Partial<ChannelManager>) => ({
            ...e,
            status: e.status ?? true,
            statusStr: e.status === false ? 'Inactive' : 'Active',
          }));
          setChannelManagerList(apiDataMapped);
          setSearchResults(apiDataMapped); // Add this to properly set search results
          toast.success('Channel Manager data fetched successfully');
        }
      } catch (error) {
        console.error('Error fetching channel manager data:', error);
        toast.error('An error occurred while fetching channel manager data');
      }
    };
    searchAllManagersFn();
  };

  return (
    <div className="container-fluid p-1">
      {/* <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} /> */}
      <div className="row">
        <div className="col-md-3">
          <div className="p-3 bg-light rounded shadow-sm">
            <div className="mb-3 text-center">
              <button className="btn btn-primary btn-sm btn-success" onClick={handleShowModal}>
                <IoPersonAdd className="mb-1" /> Add New Channel Manager
              </button>
            </div>
            <div className="row py-2">
              <div className="col-12 text-center">
                <Button
                  variant="primary"
                  size="sm"
                  className="w-80"
                  onClick={handleShowAll}
                >Show All Channel Managers</Button>
              </div>
            </div>
          </div>
        </div>

        {ChannelManagerList.length > 0 && (
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
                    defaultSortFieldId="name"
                    defaultSortAsc={true}
                    subHeader                    // Add this line
                    subHeaderComponent={SubHeaderComponent}  // Add this line
                    paginationResetDefaultPage={resetPaginationToggle}  // Add this line
                  />
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      <Modal show={showModal} onHide={handleCloseModal} dialogClassName="modal-40w">
        <Modal.Header closeButton>
          <Modal.Title>{selectedChannelManager ? "Edit Channel Manager" : "Add Channel Manager"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ChannelManagerAddEdit
            onHide={() => setShowModal(false)}
            modeIsAdd={modalModeValues.modeIsAdd}
            channelManagerId={modalModeValues.channelManagerId}
          />
        </Modal.Body>
      </Modal>

    </div>
  );
}

export default ChannelManagerSearch;