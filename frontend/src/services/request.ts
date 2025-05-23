// src/services/request.ts
import axios from 'axios';

const request = axios.create({
    baseURL: '/api', // 根据你的 Spring Boot 接口前缀设置
    timeout: 5000,
});

// 请求拦截器
request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
request.interceptors.response.use(
    (response) => response.data,
    (error) => {
        // 可以根据错误状态码做统一提示
        return Promise.reject(error.response?.data || error);
    }
);

export default request;
