import authBaseAxios,{ServerResponse,handleErrorResponse,handleSuccessResponse} from "./authBaseAxios";



async function login(formData: FormData): Promise<ServerResponse> {
    try {
        let res = await authBaseAxios({
            method: 'post',
            url: '/login',
            data: formData
            
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function register(formData: FormData): Promise<ServerResponse> {
    try {
        let res = await authBaseAxios({
            method: 'post',
            url: '/register',
            data: formData
            
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);
    }
}

async function sendPasswordResetEmail(formData: FormData): Promise<ServerResponse> {
    try {
        let res = await authBaseAxios({
            method: 'post',
            url: '/send-password-reset-email',
            data: formData
            
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);    
    }
}

async function otpPasswordReset(formData: FormData): Promise<ServerResponse> {
    try {
        let res = await authBaseAxios({
            method: 'post',
            url: '/verify-otp-password-update',
            data: formData
            
        });
        return handleSuccessResponse(res);
    } catch (error: any) {
        return handleErrorResponse(error);    
    }
}

export { login, register,sendPasswordResetEmail,otpPasswordReset };