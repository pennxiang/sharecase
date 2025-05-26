// src/services/authApi.ts
import request from './request'

export const login = (loginId: string, password: string) => {
    const params = new URLSearchParams()
    params.append('loginId', loginId)
    params.append('password', password)

    return request.post('/user/login', params)
}

