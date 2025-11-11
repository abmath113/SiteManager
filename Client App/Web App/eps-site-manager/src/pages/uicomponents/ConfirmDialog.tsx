
import { Modal } from 'react-bootstrap';


function ConfirmDialog({ title, dialogMessage, userResponse, showConfirmationDialog, handleConfirmationDialogClose }: any) {

    const selectedYES = () => {
        userResponse(true);
        handleConfirmationDialogClose();
    };

    const selectedNO = () => {
        userResponse(false);
        handleConfirmationDialogClose();
    };

    function setDialogTitle(){
        if(title === 'WARNING'){
            return <h4 className="card-header-h-color-yellow-d">{title}</h4>
        } else if(title === 'MESSAGE'){
            return <h4 className="card-header-h-color-blue-d">{title}</h4>
        } else if(title === 'ERROR'){
            return <h4 className="icon-red">{title}</h4>
        } else {
            return <h4>{title}</h4>
        }
    }

    return (
        <Modal show={showConfirmationDialog} onHide={handleConfirmationDialogClose}
            backdrop='static' keyboard={false} contentClassName='px-4 py-4'>
            <Modal.Header closeButton>
                <Modal.Title>{setDialogTitle()}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{dialogMessage}</Modal.Body>
            <Modal.Footer>
                <button type="button" className="btn btn-success btn-sm"
                    style={{ marginTop: '10px', marginBottom: '10px' }}
                    id="yesDialog" onClick={selectedYES}>Yes</button>
                <button type="button" className="btn btn-danger btn-sm"
                    style={{ marginTop: '10px', marginBottom: '10px' }}
                    id="noDialog" onClick={selectedNO}>No</button>
                <button type="button" className="btn btn-secondary btn-sm"
                    style={{ marginTop: '10px', marginBottom: '10px' }}
                    id="closeDialog" onClick={handleConfirmationDialogClose}>Close</button>
            </Modal.Footer>
        </Modal>
    );
    
}

export default ConfirmDialog;

