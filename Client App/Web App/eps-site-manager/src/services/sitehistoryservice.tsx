import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function getSiteHistoryFromAPI(siteCode : String){
    try{
        let res = await apiBaseAxios({
            method:'get',
            url: 'site-rent-history/getbysitecode/' + siteCode,
        });
        return handleSuccessResponse(res);

    }catch(error:any){
        return handleErrorResponse(error);
    }
}
export{
    getSiteHistoryFromAPI,
}
