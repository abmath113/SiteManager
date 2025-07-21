import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function GenerateRentService(currDate: String): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: `/rentmanager/getAllRentByCurrDate/${currDate}`,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}



export { GenerateRentService}