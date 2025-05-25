import axios from 'axios';
import type { User, CaseDetail, CaseInfo, UserDTO, IcdFrequency, AjaxResult } from '../types';

const api = axios.create({
  baseURL: '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// ------------------ 用户模块 ------------------
export const userApi = {
  // 用户注册
  register: (user: User) => 
    api.post<AjaxResult<User>>('/user/register', user),
  
  // 用户登录
  login: (loginId: string, password: string) =>
    api.post<AjaxResult>('/user/login',null, {params: { loginId, password } }),
  
  // 根据身份证查询用户
  findByIdCard: (idCard: string) =>
    api.get<AjaxResult<UserDTO>>('/user/by-idCard', { params: { idCard } }),
  
  // 根据ID查询用户
  findById: (id: number) =>
    api.get<AjaxResult<UserDTO>>(`/user/${id}`),
  
  // 更新用户信息
  update: (user: User) =>
    api.post<AjaxResult>('/user/update', user)
};

// ------------------ 病例模块 ------------------
export const caseApi = {
  // 创建病例
  create: (detail: CaseDetail) =>
    api.post<AjaxResult>('/case/create', detail),

  // 查询某人的所有病例
  getCasesByPatient: (address: string) =>
      api.get<AjaxResult<CaseInfo[]>>('/case/list', {
          params: { patientAddress: address }
      }),

  // 查询某人某段时间的病例详情
  getCasesByPatientAndTime: (address: string, from: string, to: string) =>
      api.get<AjaxResult<CaseDetail[]>>('/case/patient/cases/list', {
          params: { address, from, to }
      }),

  // 查询某人某段时间的 icd 聚合统计
  getPatientIcdStats: (address: string, from: string, to: string) =>
      api.get<AjaxResult<IcdFrequency>>('/case/patient/cases/count', {
          params: { patientAddress: address, from, to }
      }),

  // 查询某时间段的病例详情（不区分病人）
  getCasesByTimeRange: (from: string, to: string) =>
      api.get<AjaxResult<CaseDetail[]>>('/case/filterByTime', {
          params: { from, to }
      }),

  // 查询某时间段的病发率（按 icdCode 聚合）
  getIcdStatsByTimeRange: (from: string, to: string) =>
      api.get<AjaxResult<IcdFrequency>>('/case/cases/range/icd-stat', {
          params: { from, to }
      }),

  // 查询全平台 icdCode 频率排行
  getGlobalIcdFrequency: (desc = true) =>
      api.get<AjaxResult<any[]>>('/case/icd-frequency', {
          params: { desc }
      }),

  // 根据患者地址 + icdCode 查询病例
  getCasesByIcd: (address: string, icdCode: string) =>
      api.get<AjaxResult<CaseInfo[]>>('/case/filter', {
          params: { patientAddress: address, icdCode }
      }),

  // 模糊搜索病例
  searchCases: (keyword: string) =>
      api.get<AjaxResult<CaseDetail[]>>('/case/search', {
          params: { keyword }
      }),

  // 对比两个病例 PDF 差异
  compareCases: (hash1: string, hash2: string) =>
      api.get<AjaxResult<Record<string, any>>>('/case/compare', {
          params: { hash1, hash2 }
      })
}

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
    if (response.data.code === 200) {
      return response.data;
    } else {
      return Promise.reject(response.data);
    }
  },
  error => {
    return Promise.reject(error);
  }
); 