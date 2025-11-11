import apiBaseAxios, { ServerResponse, handleSuccessResponse, handleErrorResponse } from './apiBaseAxios';

async function getRentPrediction(agreementId: number): Promise<ServerResponse> {
    try {
        let res = await apiBaseAxios({
            method: 'get',
            url: `/rentmanager/getRentListByAgreementId/${agreementId}`,
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

export const rentPredictionService = {
    getRentPrediction
};