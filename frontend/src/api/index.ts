import axios from 'axios';
import type { User, CaseDetail, CaseVersion, HotDiseaseDTO, AjaxResult } from '../types';

const api = axios.create({
  baseURL: '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 用户相关API
export const userApi = {
  // 用户注册
  register: (user: User) => 
    api.post<AjaxResult<User>>('/user/register', user),
  
  // 用户登录
  login: (loginId: string, password: string) =>
    api.post<AjaxResult>('/user/login', null, { params: { loginId, password } }),
  
  // 根据身份证查询用户
  findByIdCard: (idCard: string) =>
    api.get<AjaxResult>('/user/by-idCard', { params: { idCard } }),
  
  // 根据ID查询用户
  findById: (id: number) =>
    api.get<AjaxResult>(`/user/${id}`),
  
  // 更新用户信息
  update: (user: User) =>
    api.post<AjaxResult>('/user/update', user)
};

// 病例相关API
export const caseApi = {
  // 创建病例
  create: (caseDetail: CaseDetail) =>
    api.post<AjaxResult>('/case/create', caseDetail),
  
  // 获取病例详情
  getDetail: (id: number, icd: string) =>
    api.get<AjaxResult<CaseDetail>>(`/case/detail/${id}`, { params: { icd } }),
  
  // 查看病例
  viewCase: (caseId: string) =>
    api.get<AjaxResult<CaseDetail>>('/case/view', { params: { caseId } }),
  
  // 查看历史版本
  viewHistory: (caseId: string) =>
    api.get<AjaxResult<CaseVersion[]>>('/case/history', { params: { caseId } }),
  
  // 获取疾病统计
  getDiseaseStats: (start: string, end: string, asc: boolean = false) =>
    api.get<AjaxResult<HotDiseaseDTO[]>>('/case/disease-stats', { 
      params: { start, end, asc } 
    })
};

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    return Promise.reject(error);
  }
); 