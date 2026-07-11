import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';

export interface ApiResponseEnvelope<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
  errors?: Record<string, string>;
}

export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('accessToken');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => {
    const responseData = response.data;
    if (
      responseData &&
      typeof responseData === 'object' &&
      'success' in responseData &&
      'data' in responseData
    ) {
      if (!responseData.success) {
        return Promise.reject(responseData);
      }
      return responseData.data;
    }
    return responseData;
  },
  (error: AxiosError<any>) => {
    const errorData = error.response?.data;
    const standardError = {
      message: errorData?.message || error.message || 'Đã xảy ra lỗi không xác định',
      errors: errorData?.errors || null,
      status: error.response?.status,
    };

    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }

    return Promise.reject(standardError);
  }
);
