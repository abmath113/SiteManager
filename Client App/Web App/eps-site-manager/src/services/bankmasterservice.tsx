import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';


async function saveBankMaster(bankMasterDto: any): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/bankmaster/save',
            data: bankMasterDto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}
async function updateBankMaster(bankMasterDto: any): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/bankmaster/update',
            data: bankMasterDto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}
async function searchAllBankMasters(): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/bankmaster/searchall',
        });
        return handleSuccessResponse(res);

    }
    catch (error: any) {
        return handleErrorResponse(error);
    }
   
}
async function getBankMasterById(id: number): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: `/bankmaster/getbyid/${id}`,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}
export {
    saveBankMaster,
    updateBankMaster,
    searchAllBankMasters,
    getBankMasterById
    
};