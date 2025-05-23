// src/services/authApi.ts
import request from './request';

export async function login(username: string, password: string) {
    const res = await request.post('/auth/login', { username, password });
    return res.data;
}
