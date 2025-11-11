import axios from 'axios';

const baseApiUrl = process.env.REACT_APP_API_BASE_URL;


const baseURL = `${baseApiUrl}auth`; // Store the constructed baseURL in a variable
console.log("baseURL is this:", baseURL); // Now you can log it correctly


const authBaseAxios = axios.create({
  baseURL: `${baseApiUrl}auth`, 
  
  
  headers: { "Content-Type": "multipart/form-data" },
});


// Server Response interface and handlers
export interface ServerResponse {
    data:  any;
    error: any;
    successstatus: boolean;
};

export function handleSuccessResponse(respData: any): ServerResponse {
    return {
          data: respData.data,
          error: {},
          successstatus: true
    };
}

export function handleErrorResponse(respData: any): ServerResponse {
    let errorMsg = null;
    if (respData.response !== undefined) {
          errorMsg = respData.response.data;
    } else {    
          errorMsg = respData.message;
    }
    return {
          data: {},
          error: errorMsg,
          successstatus: false
    };
}

export default authBaseAxios;
