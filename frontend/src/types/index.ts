export interface User {
  id?: number;
  name: string;
  phone: string;
  idCard: string;
  workId?: string;
  password?: string;
}

export interface UserDTO {
  name: string;
  phone: string;
  idCard: string;
  workId?: string;
}

export interface CaseDetail {
  id?: number;
  patientId: number;
  doctorId: number;
  icdCode: string;
  diagnosis: string;
  treatment: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CaseVersion {
  id?: number;
  caseId: number;
  version: number;
  ipfsHash: string;
  createdAt?: string;
}

export interface HotDiseaseDTO {
  icdCode: string;
  count: number;
  diseaseName: string;
}

export interface AjaxResult<T = any> {
  code: number;
  msg: string;
  data?: T;
} 