// import { PiArrowCircleDownDuotone } from 'react-icons/pi';
import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

type CachedEntry = {
    data:ServerResponse;
    expiry:number;
};

const rentCache: Record<string,CachedEntry> = {};
(window as any).rentCache = rentCache;

async function GenerateRentService(currDate: string): Promise<ServerResponse> {
    const now = Date.now();
    try {
        if(rentCache[currDate] && rentCache[currDate].expiry>now){
            console.log("Serving from cache: " , currDate);
            return rentCache[currDate].data;
        }
        let res = await apiBaseAxios({
            method: 'get',
            url: `/rentmanager/getAllRentByCurrDate/${currDate}`,
        });
        const processed = handleSuccessResponse(res);
       
        rentCache[currDate] = {
            data: processed,
            expiry: now + 5 * 60 * 1000,
        };
        return processed;
        
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}



export { GenerateRentService}

