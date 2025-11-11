
import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function saveLandlordMasterDetails(landlordmasterdto: any) {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/landlordmaster/save',
            data: landlordmasterdto
            
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

async function searchLandlordMasterDetails(landlordmastersearchdto: any) {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/landlordmaster/search',
            data: landlordmastersearchdto
            
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

async function searchAllLandlordMasterDetails() : Promise<ServerResponse>  {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/landlordmaster/searchall',

        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getLandlordMasterDetails(landlordId: number) : Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/landlordmaster/getbylandlordid/' + landlordId,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function updateLandlordMasterDetails(landlordmasterdto: any) : Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/landlordmaster/update',
            data: landlordmasterdto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

// New function to fetch all landlords 
async function getAllLandlordsOnlyLandlordIdNameAndAccountNo() {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/landlordmaster/getalllandlordsbylandlordidandlandlordnameandaccountno',
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
            data: [],
            error: errorMsg,
            successstatus: false
        };
    }
}

export { 
    saveLandlordMasterDetails,
    searchAllLandlordMasterDetails,
    searchLandlordMasterDetails,
    getLandlordMasterDetails,
    updateLandlordMasterDetails,
    getAllLandlordsOnlyLandlordIdNameAndAccountNo

}



   