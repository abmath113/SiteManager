
import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';



async function saveSiteMasterDetails(sitemasterdto: any) : Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/sitemaster/save',
            data: sitemasterdto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function updateSiteMasterDetails(sitemasterdto: any) : Promise<ServerResponse> {
    console.log(sitemasterdto);

    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/sitemaster/update',
            data: sitemasterdto
            
        });
        console.log("try")

        return handleSuccessResponse(res);
    } catch (error: any) {
        console.log(error);

        return handleErrorResponse(error);
    }
}


async function searchSiteMasterDetails(sitemastersearchdto: any) : Promise<ServerResponse>  {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/sitemaster/search',
            data: sitemastersearchdto

        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function searchAllSiteMasterDetails() : Promise<ServerResponse>  {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/sitemaster/searchall',

        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getSiteMasterDetails(siteId: number) : Promise<ServerResponse> {
    console.log("getSiteMasterDetails service is working")
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/sitemaster/getbysiteid/' + siteId,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

// New function to fetch all sites
async function getAllSitesOnlySiteIdAndSiteCode() {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/sitemaster/getallsitesbysiteidandsitecode',
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

async function getAllBanksOnlyBankCodeAndBankId(){
    try{
        let res = await apiBaseAxios(
            {
                method: 'get',
                url: '/bankmaster/getallbankidandbankcode'
            }
        );
        const respData = res.data;
        return{
            data: respData,
            error: '',
            successstatus: true
        };
    }
    catch(error:any){
        let errorMsg = null;
        if(error.response !== undefined){
            errorMsg = error.response.data;
        }else{
            errorMsg = error.message;
        }
        return{
            data: [],
            error: errorMsg,
            successstatus:false
        };
    }
}


export { 
    
        saveSiteMasterDetails, 
         updateSiteMasterDetails,
         searchSiteMasterDetails, 
         searchAllSiteMasterDetails,
         getSiteMasterDetails,
         getAllSitesOnlySiteIdAndSiteCode,
         getAllBanksOnlyBankCodeAndBankId
}



   