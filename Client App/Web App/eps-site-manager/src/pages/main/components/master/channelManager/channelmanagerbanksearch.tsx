import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import DataTable from 'react-data-table-component';
import { toast } from 'react-toastify';
import { IoPersonAdd } from 'react-icons/io5';
import { FaPencilAlt } from 'react-icons/fa';
import { searchAllChannelManagers } from '../../../../../services/channelmanagerservice';
import { searchAllBankMasters } from '../../../../../services/bankmasterservice';

import ChannelManagerAddEdit from './channelmanageraddedit';
import BankAddEdit from '../bank/bankaddedit';

interface BankMaster {
  bankId: number;
  bankName: string;
  bankCode?: string;
  status?: boolean;
  statusStr?: string;
}

interface ChannelManager {
  channelManagerId: number;
  channelManagerName: string;
  status?: boolean;
  statusStr?: string;
}

const ChannelManagerAndBankSearch: React.FC = () => {
  const [cmList, setCmList] = useState<ChannelManager[]>([]);
  const [showCmModal, setShowCmModal] = useState(false);
  const [cmModalMode, setCmModalMode] = useState<{ modeIsAdd: boolean; id: number }>({ modeIsAdd: true, id: 0 });

  const [bankList, setBankList] = useState<BankMaster[]>([]);
  const [showBankModal, setShowBankModal] = useState(false);
  const [bankModalMode, setBankModalMode] = useState<{ modeIsAdd: boolean; id: number }>({ modeIsAdd: true, id: 0 });

  const handleShowCmModalAdd = () => {
    setCmModalMode({ modeIsAdd: true, id: 0 });
    setShowCmModal(true);
  };
  const handleShowCmModalEdit = (id: number) => {
    setCmModalMode({ modeIsAdd: false, id });
    setShowCmModal(true);
  };
  const handleShowAllCms = async () => {
    try {
      setBankList([]); // clear bank data when fetching CMs
      const res = await searchAllChannelManagers();
      if (res.successstatus) {
        const mapped = res.data.map((cm: Partial<ChannelManager>) => ({
          ...cm,
          statusStr: cm.statusStr ?? (cm.status === false ? 'Inactive' : 'Active'),
        }));
        setCmList(mapped as ChannelManager[]);
        toast.success('Channel Manager data fetched successfully');
      } else {
        toast.error(res.error.message);
      }
    } catch {
      toast.error('Error fetching Channel Manager data');
    }
  };

  const handleShowBankModalAdd = () => {
    setBankModalMode({ modeIsAdd: true, id: 0 });
    setShowBankModal(true);
  };
  const handleShowBankModalEdit = (id: number) => {
    setBankModalMode({ modeIsAdd: false, id });
    setShowBankModal(true);
  };
  const handleShowAllBanks = async () => {
    try {
      setCmList([]); // clear CM data when fetching banks
      const res = await searchAllBankMasters();
      if (res.successstatus) {
        const mapped = res.data.map((b: Partial<BankMaster>) => ({
          ...b,
          statusStr: b.statusStr ?? (b.status === false ? 'Inactive' : 'Active'),
        }));
        setBankList(mapped as BankMaster[]);
        toast.success('Bank data fetched successfully');
      } else {
        toast.error(res.error.message);
      }
    } catch {
      toast.error('Error fetching Bank data');
    }
  };

  const cmColumns = [
    { name: 'Channel Manager Name', selector: (row: ChannelManager) => row.channelManagerName, sortable: true },
    { name: 'Status', selector: (row: ChannelManager) => row.statusStr || '', sortable: true },
    {
      name: 'Actions',
      cell: (row: ChannelManager) => (
        <a style={{ color: '#f68500' }} onClick={() => handleShowCmModalEdit(row.channelManagerId)}>
          <FaPencilAlt />
        </a>
      ),
    },
  ];

  const bankColumns = [
    { name: 'Bank Name', selector: (row: BankMaster) => row.bankName, sortable: true },
    { name: 'Bank Code', selector: (row: BankMaster) => row.bankCode || '', sortable: true },
    { name: 'Status', selector: (row: BankMaster) => row.statusStr || '', sortable: true },
    {
      name: 'Actions',
      cell: (row: BankMaster) => (
        <a style={{ color: '#f68500' }} onClick={() => handleShowBankModalEdit(row.bankId)}>
          <FaPencilAlt />
        </a>
      ),
    },
  ];

  return (
    <>
      <div className="row">
        <div className="col-md-3">
          <div className="p-3 bg-light rounded shadow-sm">
            <div className="mb-3 text-center">
              <button className="btn btn-success btn-sm" onClick={handleShowCmModalAdd}>
                <IoPersonAdd className="mb-1" /> Add New Channel Manager
              </button>
            </div>
            <div className="row py-2">
              <div className="col-12 text-center">
                <Button variant="primary" size="sm" onClick={handleShowAllCms}>
                  Show All Channel Managers
                </Button>
              </div>
            </div>
            <hr />
            <div className="mb-3 text-center">
              <button className="btn btn-success btn-sm" onClick={handleShowBankModalAdd}>
                <IoPersonAdd className="mb-1" /> Add New Bank
              </button>
            </div>
            <div className="row py-2">
              <div className="col-12 text-center">
                <Button variant="primary" size="sm" onClick={handleShowAllBanks}>
                  Show All Banks
                </Button>
              </div>
            </div>
          </div>
        </div>

        {cmList.length > 0 && (
          <div className="col-lg-9 col-12 px-2">
            <DataTable title="" columns={cmColumns} data={cmList} pagination />
          </div>
        )}

        {bankList.length > 0 && (
          <div className="col-lg-9 col-12 px-2">
            <DataTable title="" columns={bankColumns} data={bankList} pagination />
          </div>
        )}
      </div>

      <Modal show={showCmModal} onHide={() => setShowCmModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{cmModalMode.modeIsAdd ? 'Add Channel Manager' : 'Edit Channel Manager'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ChannelManagerAddEdit
            onHide={() => setShowCmModal(false)}
            modeIsAdd={cmModalMode.modeIsAdd}
            channelManagerId={cmModalMode.id}
          />
        </Modal.Body>
      </Modal>

      <Modal show={showBankModal} onHide={() => setShowBankModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{bankModalMode.modeIsAdd ? 'Add Bank' : 'Edit Bank'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <BankAddEdit
            onHide={() => setShowBankModal(false)}
            modeIsAdd={bankModalMode.modeIsAdd}
            bankId={bankModalMode.id}
          />
        </Modal.Body>
      </Modal>
    </>
  );
};

export default ChannelManagerAndBankSearch;
