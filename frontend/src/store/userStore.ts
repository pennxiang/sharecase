// src/stores/userStore.ts
import { defineStore } from 'pinia';
import { login } from '@/services/authApi';
import { UserInfo } from '@/types/user';

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null as UserInfo | null,
        token: '' as string,
    }),
    actions: {
        async login(username: string, password: string) {
            const res = await login(username, password);
            this.token = res.token;
            this.user = res.user;
            localStorage.setItem('token', res.token);
        },
        logout() {
            this.user = null;
            this.token = '';
            localStorage.removeItem('token');
        },
    },
});
