import { defineStore } from 'pinia'
import type { User } from '@/types'

export interface User {
    id: number
    loginId: string
    role: 'doctor' | 'patient'
    blockchainAddress: string
}

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null as User | null
    }),
    actions: {
        setUser(user: User) {
            this.user = user
        },
        logout() {
            this.user = null
        },
        mockLogin(role: 'doctor' | 'patient') {
            const mockUsers: Record<string, User> = {
                doctor: {
                    id: 1,
                    loginId: 'doctor001',
                    role: 'doctor',
                    blockchainAddress: '0xDOCTORADDR'
                },
                patient: {
                    id: 2,
                    loginId: 'patient001',
                    role: 'patient',
                    blockchainAddress: '0xPATIENTADDR'
                }
            }
            this.user = mockUsers[role]
        }
    },
    persist: true // 如果用了 pinia-plugin-persistedstate 插件
})
/*
export const useUserStore = defineStore('user', {
    state: () => ({
        user: null as User | null
    }),
    actions: {
        setUser(u: User) {
            this.user = u
            localStorage.setItem('user', JSON.stringify(u)) // 持久化
        },
        logout() {
            this.user = null
            localStorage.removeItem('user')
        },
        loadFromStorage() {
            const raw = localStorage.getItem('user')
            if (raw) this.user = JSON.parse(raw)
        }
    }
})
*/