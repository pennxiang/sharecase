// 定义全局TypeScript类型

export interface User {
  id?: number;
  name: string;
  phone: string;
  idCard: string;
  password?: string;
  workId?: string;
  chainAddress?: string;
  role?: string;
}

export interface UserDTO {
  name: string;
  phone: string;
  idCard: string;
  workId?: string;
}

export interface CaseInfo {
  caseId: string;
  icdCode: string;
  ipfsHash: string;
  doctor: string;
  visitTime: string;  // 后端是 LocalDateTime，建议用字符串
  doctorAddress: string;
  patientAddress: string;
  rawTimestamp: number;
  pdfUrl?: string;
}

export interface CaseDetail extends CaseInfo {
  title?: string;
  chiefComplaint?: string;
  presentIllness?: string;
  pastHistory?: string;
  diagnosis?: string;
  doctorAdvice?: string;
  hospitalAddress?: string;
}

export interface IcdFrequency  {
  icdCode: string;
  frequency: number;
}

export interface AjaxResult<T = any> {
  code: number;
  msg: string;
  data?: T;
} 