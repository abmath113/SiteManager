import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function uploadBulkWLABancs(fileFormData: FormData): Promise<ServerResponse> {
    try {
        const res = await apiBaseAxios({
            method: 'post',
            url: '/wla-bulk-upload/upload-mastertable',
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
async function downloadWLABulkUploadTemplate(): Promise<ServerResponse> {
    try {

        const res = await apiBaseAxios({
            method: 'get',
            url: '/wla-bulk-upload/download-template',
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

async function saveWLAMasterDetails(wlamasterdto: any) : Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/wlamaster/save',
            data: wlamasterdto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getWLAMasterDetailsById(wlaBancId: number) : Promise<ServerResponse> {
    console.log("getSiteMasterDetails service is working")
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/wlamaster/getbywlabancid/' + wlaBancId,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function updateWLAMasterDetails(wlamasterdto: any) : Promise<ServerResponse> {
    console.log(wlamasterdto);

    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/wlamaster/update',
            data: wlamasterdto
            
        });
        console.log("try")

        return handleSuccessResponse(res);
    } catch (error: any) {
        console.log(error);

        return handleErrorResponse(error);
    }
}

async function getAllWLAMasterDetails() : Promise<ServerResponse> {

    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/wlamaster/getall',
            
        });

        return handleSuccessResponse(res);
    } catch (error: any) {
        console.log(error);

        return handleErrorResponse(error);
    }
}

   

export {
    uploadBulkWLABancs,
    downloadWLABulkUploadTemplate,
    saveWLAMasterDetails,
    getWLAMasterDetailsById,
    updateWLAMasterDetails,
    getAllWLAMasterDetails

};