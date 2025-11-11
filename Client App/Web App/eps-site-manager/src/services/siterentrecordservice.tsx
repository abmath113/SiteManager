import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function uploadRentRecords(fileFormData: FormData, date: string): Promise<ServerResponse> {
    try {
        const res = await apiBaseAxios({
            method: 'post',
            url: `/rent-records/upload/${date}`,
            data: fileFormData,
            headers: { 'Content-Type': 'multipart/form-data' },
            timeout: 30000,
        });

        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

export { uploadRentRecords };
