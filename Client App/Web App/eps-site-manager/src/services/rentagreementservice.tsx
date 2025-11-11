import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function saveRentAgreementMasterDetails(rentagreementmasterdto: any) {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/rentagreementmaster/save',
            data: rentagreementmasterdto
        });

        const respData = res.data;
        return {
            data: respData,
            error: '',
            successstatus: true
        };
    } catch (error: any) {
        let errorMsg = null;
        if (error.response !== undefined) {
            errorMsg = error.response.data;
        } else {
            errorMsg = error.message;
        }
        return {
            data: {},
            error: errorMsg,
            successstatus: false
        };
    }
}

async function uploadAgreementScan(fileFormData: FormData) {
    try {
        const res = await apiBaseAxios({
            method: 'post',
            url: '/agreementscan/upload',
            data: fileFormData,
            headers: { 'Content-Type': 'multipart/form-data' },
            timeout: 30000,
        });

        const respData = res.data;
        return {
            data: respData,
            error: '',
            successstatus: true
        };
    } catch (error: any) {
        const errorMsg = error.response?.data?.message || error.message;
        return {
            data: {},
            error: errorMsg,
            successstatus: false
        };
    }
}

async function searchAllRentAgreementMasterDetails(): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/rentagreementmaster/searchall',

        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getRentAgreementMasterDetails(agreementId: number): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/rentagreementmaster/getbyid/' + agreementId,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}
async function updateRentAgreementMasterDetails(rentagreementmasterdto: any): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/rentagreementmaster/update',
            data: rentagreementmasterdto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}
async function terminateRentAgreementMaster(rentagreementterminatedto: any): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/rentagreementmaster/terminate',
            data: rentagreementterminatedto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getAgreementScan(siteCode: string, landlordName: string): Promise<ServerResponse> {
    try {
        console.log('Requesting scan for:', { siteCode, landlordName });

        const res = await apiBaseAxios({
            method: 'get',
            url: '/agreementscan/get-agreementScan',
            params: {  // Using query parameters instead of path variables
                siteCode: encodeURIComponent(siteCode),
                landlordName: encodeURIComponent(landlordName)
            },
            responseType: 'blob',
            headers: {
                'Accept': 'application/pdf'
            }
        });
        console.log('Response status:', res.status);
        console.log('Response type:', res.data.type);

        // Check if the response is actually a PDF
        if (res.data.type !== 'application/pdf') {
            throw new Error('Invalid response type received');
        }

        return {
            data: res.data,
            error: '',
            successstatus: true
        };
    } catch (error: any) {
        console.error('API Error Details:', {
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
            message: error.message
        });

        const errorMsg = error.response?.data?.message
            || error.message
            || 'Failed to fetch agreement scan';

        return {
            data: null,
            error: errorMsg,
            successstatus: false
        };
    }
}

async function uploadBulkRentAgreements(fileFormData: FormData): Promise<ServerResponse> {
    try {
        const res = await apiBaseAxios({
            method: 'post',
            url: '/bulk-upload/upload-mastertable',
            data: fileFormData,
            headers: { 'Content-Type': 'multipart/form-data' },
            responseType: 'blob', // For handling file download response
            timeout: 60000, // Increased timeout for bulk operations
        });

        // Check if the response is an excel file
        if (!res.data.type.includes('spreadsheet')) {
            throw new Error('Invalid response type received');
        }

        return {
            data: res.data,
            error: '',
            successstatus: true
        };
    } catch (error: any) {
        const errorMsg = error.response?.data?.message || error.message;
        return {
            data: null,
            error: errorMsg,
            successstatus: false
        };
    }
}
async function downloadBulkUploadTemplate(): Promise<ServerResponse> {
    try {

        const res = await apiBaseAxios({
            method: 'get',
            url: '/bulk-upload/download-template',
            responseType: 'blob',
        });

        return {
            data: res.data,
            error: '',
            successstatus: true
        };
    } catch (error: any) {
        console.error('API Error Details:', {
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
            message: error.message
        });

        const errorMsg = error.response?.data?.message
            || error.message
            || 'Failed to fetch agreement scan';

        return {
            data: null,
            error: errorMsg,
            successstatus: false
        };
    }
}
export {
    saveRentAgreementMasterDetails,
    getRentAgreementMasterDetails,
    searchAllRentAgreementMasterDetails,
    updateRentAgreementMasterDetails,
    terminateRentAgreementMaster,
    uploadAgreementScan,
    getAgreementScan,
    uploadBulkRentAgreements,
    downloadBulkUploadTemplate
};


