import React, { useState, useEffect } from "react";
import { Modal, Button, Alert, Tabs, Tab } from "react-bootstrap";
import { rentPredictionService } from "../../../../services/rentpredictionservice";
import { Line } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';

interface RentPredictionModalProps {
  show: boolean;
  onHide: () => void;
  agreementId: number | null;
  agreementStartDate: Date | null;
}

interface RentData {
  year: number;
  month: string;
  rent: number;
}

Chart.register(...registerables);

const RentPredictionModal: React.FC<RentPredictionModalProps> = ({
  show,
  onHide,
  agreementId,
  agreementStartDate,
}) => {
  const [rentData, setRentData] = useState<RentData[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [key, setKey] = useState<string>('table');

  useEffect(() => {
    if (show && agreementId !== null && agreementStartDate !== null) {
      fetchRentData(agreementId, agreementStartDate);
    }
  }, [show, agreementId, agreementStartDate]);

  const fetchRentData = async (id: number, startDate: Date) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await rentPredictionService.getRentPrediction(id);
      if (response.successstatus && response.data) {
        const rentList: number[] = response.data;

        const formattedData: RentData[] = rentList.map((rent, index) => {
          const currentDate = new Date(startDate);
          currentDate.setMonth(startDate.getMonth() + index);
          return {
            year: currentDate.getFullYear(),
            month: currentDate.toLocaleString("default", { month: "long" }),
            rent: rent,
          };
        });

        setRentData(formattedData);
      } else {
        throw new Error(response.error?.message || "Failed to fetch rent data");
      }
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unexpected error occurred"
      );
    } finally {
      setIsLoading(false);
    }
  };

  if (agreementId === null || agreementStartDate === null) {
    return (
      <Modal show={show} onHide={onHide} >

        <Modal.Body>
          <Alert variant="warning">
            Unable to fetch rent prediction. Missing agreement details.
          </Alert>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
  
  return (
    <Modal 
    show={show}
     onHide={onHide} 
     size="lg"
     >
      
      <Modal.Body>
        <Tabs activeKey={key} onSelect={(k) => setKey(k as string)} className="mb-3">
          <Tab eventKey="table" title="Table">
            <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
              <h5>Rent Data Table</h5>
              <table className="table">
                <thead>
                  <tr>
                    <th>Month/Year</th>
                    <th>Rent</th>
                  </tr>
                </thead>
                <tbody>
                  {rentData.map((data, index) => (
                    <tr key={index}>
                      <td>
                        <strong>{data.month}</strong> <strong>{data.year}</strong>
                      </td>
                      <td>{data.rent}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </Tab>
          <Tab eventKey="graph" title="Graph">
            {isLoading ? (
              <Alert variant="info">Loading rent data...</Alert>
            ) : error ? (
              <Alert variant="danger">{error}</Alert>
            ) : (
              <Line
                data={{
                  labels: rentData.map(data => `${data.month} ${data.year}`),
                  datasets: [
                    {
                      label: 'Rent',
                      data: rentData.map(data => data.rent),
                      backgroundColor: 'rgba(75, 192, 192, 0.6)',
                      borderColor: 'rgba(75, 192, 192, 1)',
                      fill: false,
                      tension: 0.1,
                    },
                  ],
                }}
                options={{
                  scales: {
                    y: {
                      beginAtZero: true,
                    },
                  },
                }}
              />
            )}
          </Tab>
        </Tabs>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default RentPredictionModal;