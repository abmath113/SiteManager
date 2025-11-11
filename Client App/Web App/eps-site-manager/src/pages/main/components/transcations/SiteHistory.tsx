import React, { useState } from 'react';
import { getSiteHistoryFromAPI } from '../../../../services/sitehistoryservice';
import './SiteHistory.css';

interface Bank {
  bankCode: string;
  bankId: number;
  bankName: string;
}

interface ChannelManager {
  channelManagerId: number;
  channelManagerName: string;
  emailId: string;
  phoneNo: string;
}

interface SiteInfo {
  bank: Bank;
  channelManager: ChannelManager;
  siteATMs: string;
  siteAddress: string;
  siteCode: string;
  siteId: number;
  siteStatus: boolean;
  projectName: string;
  state: string;
  district: string;
}

interface LandlordInfo {
  aadharNo: string;
  accountNo: string;
  address: string;
  beneficiaryName: string;
  createdBy: string;
  createdOn: string;
  ifscCode: string;
  landlordId: number;
  mobileNo: string;
  name: string;
  pan: string;
  status: boolean;
}

interface Agreement {
  agreementDate: string;
  agreementEndDate: string;
  agreementId: number;
  agreementScanExist: boolean;
  agreementSpan: number;
  considerAgreementDate: string;
  escalationAfterMonths: number;
  escalationPercent: number;
  landlordId: LandlordInfo;
  monthlyRent: number;
  paymentInterval: number;
  rentAgreementStatus: boolean;
  rentPayStartDate: string;
  siteId: SiteInfo;
  solarPanelRent: number;
  terminationDate: string;
  terminationRemark: string;
}

interface RentRecord {
  agreementId: Agreement;
  amountPaid: number;
  generatedRent: number;
  paymentDate: string;
  reason: string;
  remarks: string;
  rentMonth: string;
  siteCode: string;
  siteRentRecordId: number;
  transactionStatus: boolean;
  utrNo: string;
}

interface RentAgreementRecord {
  agreementid: Agreement;
  siteRentRecordList: RentRecord[];
}

interface SiteHistoryData {
  rentAgreementRecordDTOList: RentAgreementRecord[];
  siteMaster: SiteInfo;
}

const SiteHistory: React.FC = () => {
  const [siteCode, setSiteCode] = useState<string>('');
  const [siteData, setSiteData] = useState<SiteHistoryData | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!siteCode.trim()) {
      setError('Please enter a site code');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const siteHistoryResponseFromAPI = await getSiteHistoryFromAPI(siteCode);
      console.log('API Response:', siteHistoryResponseFromAPI); // Debug log
      
      if (siteHistoryResponseFromAPI.data &&
        siteHistoryResponseFromAPI.data.siteMaster !== null &&
        Array.isArray(siteHistoryResponseFromAPI.data.rentAgreementRecordDTOList)) {
        setSiteData(siteHistoryResponseFromAPI.data);
      } else {
        setError('No site data available');
      }
    } catch (err) {
      console.error('API Error:', err); // Debug log
      setError('An unexpected error occurred');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string): string => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  };

  return (
    <div className="site-history-container">
      <div className="search-section">
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            value={siteCode}
            onChange={(e) => setSiteCode(e.target.value)}
            placeholder="Enter site code"
            className="search-input"
          />
          <button
            type="submit"
            disabled={loading}
            className="search-button"
          >
            {loading ? 'Loading...' : 'Search'}
          </button>
        </form>
        {error && <div className="error-message">{error}</div>}
      </div>

      {siteData && siteData.siteMaster && (
        <div className="site-data">
          <div className="card">
            <h2>Site Information</h2>
            <div className="info-grid">
              <div className="info-item">
                <label>Site Code:</label>
                <span>{siteData.siteMaster?.siteCode || 'N/A'}</span>
              </div>
              <div className="info-item">
                <label>Site Address:</label>
                <span>{siteData.siteMaster?.siteAddress || 'N/A'}</span>
              </div>
              <div className="info-item">
                <label>Bank Name:</label>
                <span>{siteData.siteMaster?.bank?.bankName || 'N/A'}</span>
              </div>
              <div className="info-item">
                <label>ATMs:</label>
                <span>{siteData.siteMaster?.siteATMs || 'N/A'}</span>
              </div>
              <div className='info-item'>
                <label>Project: </label>
                <span>{siteData.siteMaster?.projectName || 'N/A'}</span>
              </div>

            <div className='info-item'>
                <label>State: </label>
                <span>{siteData.siteMaster?.state || 'N/A'}</span>
              </div>

              <div className='info-item'>
                <label>District: </label>
                <span>{siteData.siteMaster?.district || 'N/A'}</span>
              </div>
            </div>
          </div>

          {siteData.rentAgreementRecordDTOList && siteData.rentAgreementRecordDTOList.map((agreement, index) => (
            <div key={index} className="card">
              <h2>Rent Agreement {index + 1}</h2>

              <div className="info-grid">
                <div className="info-item">
                  <label>Agreement Date:</label>
                  <span>{formatDate(agreement.agreementid?.agreementDate)}</span>
                </div>
                <div className="info-item">
                  <label>End Date:</label>
                  <span>{formatDate(agreement.agreementid?.agreementEndDate)}</span>
                </div>
                <div className="info-item">
                  <label>Monthly Rent:</label>
                  <span>₹{agreement.agreementid?.monthlyRent || 0}</span>
                </div>
                <div className="info-item">
                  <label>Solar Panel Rent:</label>
                  <span>₹{agreement.agreementid?.solarPanelRent || 0}</span>
                </div>
              </div>

              {agreement.agreementid?.landlordId && (
                <>
                  <h3>Landlord Details</h3>
                  <div className="info-grid">
                    <div className="info-item">
                      <label>Name:</label>
                      <span>{agreement.agreementid.landlordId.name || 'N/A'}</span>
                    </div>
                    <div className="info-item">
                      <label>Mobile:</label>
                      <span>{agreement.agreementid.landlordId.mobileNo || 'N/A'}</span>
                    </div>
                    <div className="info-item">
                      <label>PAN:</label>
                      <span>{agreement.agreementid.landlordId.pan || 'N/A'}</span>
                    </div>
                    <div className="info-item">
                      <label>Address:</label>
                      <span>{agreement.agreementid.landlordId.address || 'N/A'}</span>
                    </div>
                  </div>
                </>
              )}

              {agreement.siteRentRecordList && agreement.siteRentRecordList.length > 0 && (
                <>
                  <h3>Rent Payment History</h3>
                  <div className="table-container">
                    <table>
                      <thead>
                        <tr>
                          <th>Month</th>
                          <th>Generated Rent</th>
                          <th>Amount Paid</th>
                          <th>Payment Date</th>
                          <th>UTR No.</th>
                          <th>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {agreement.siteRentRecordList.map((record, idx) => (
                          <tr key={idx}>
                            <td>{record.rentMonth || 'N/A'}</td>
                            <td>₹{record.generatedRent || 0}</td>
                            <td>₹{record.amountPaid || 0}</td>
                            <td>{record.paymentDate || 'N/A'}</td>
                            <td>{record.utrNo || 'N/A'}</td>
                            <td>{record.transactionStatus ? 'Active' : 'Inactive'}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SiteHistory;