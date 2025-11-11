import axios from 'axios';
import { toast } from 'react-toastify';

const baseApiUrl = process.env.REACT_APP_API_BASE_URL;

const apiBaseAxios = axios.create({
    // baseURL: 'https://192.168.202.105:8090/epssitemanagerapi/api',

    baseURL:  `${baseApiUrl}api`,

    //baseURL: 'http://localhost:8090/epssitemanagerapi/api',

    headers: { "Content-Type": "application/json" },
});
  console.log("API Base URLisss:", baseApiUrl);


// Authentication error codes
const AUTH_ERROR_CODES = [401, 403];

// Add response interceptor for handling auth errors
apiBaseAxios.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        console.log(error.response.data.message);
      
        const errmessage = error.response?.data?.message || error.message;
        // Check if it's an authentication error
        if (errmessage.toLowerCase().includes("jwt expired"))  {
            // Clear localStorage
            localStorage.clear();
            
            // Redirect to login page
            window.location.href = '/EPSSiteManager/login';
        }
        
        // Return the error to be handled by the calling function
        return Promise.reject(error);
    }
);

// Request interceptor for adding token
apiBaseAxios.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export interface ServerResponse {
    data: any;
    error: any;
    successstatus: boolean;
};

export const handleSuccessResponse = (res: any): ServerResponse => ({
    data: res.data,
    error: '',
    successstatus: true
});

export const handleErrorResponse = (error: any): ServerResponse => {

    let errorMsg = error?.response?.data || error.message || 'Unknown error';

    return {
        data: null,
        error: errorMsg,
        successstatus: false
    }
};

export default apiBaseAxios;