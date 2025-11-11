import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function getAllManagerDetails(): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/channelmanager/searchall',
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getChannelManagerById(id: number): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: `/channelmanager/getbyid/${id}`,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function saveChannelManager(channelManagerDto: any): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/channelmanager/save',
            data: channelManagerDto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function searchAllChannelManagers(): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: '/channelmanager/searchall',
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function updateChannelManager(channelManagerDto: any): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'post',
            url: '/channelmanager/update',
            data: channelManagerDto
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function getAllChannelManagersOnlyChannelManagerIdNameAndEmailId(){
    try{
        let res = await apiBaseAxios(
            {
                method: 'get',
                url: '/channelmanager/getallchannelmanagersbynameandemail'
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
    getAllManagerDetails,
    getChannelManagerById,
    saveChannelManager,
    searchAllChannelManagers,
    updateChannelManager,
    getAllChannelManagersOnlyChannelManagerIdNameAndEmailId
};