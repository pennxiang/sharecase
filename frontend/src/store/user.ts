import { defineStore } from 'pinia'
import type { User } from '@/types'

export interface User {
    id: number
    name: string
    phone: string
    idCard: string
    password: string
    workId: string
    chainAddress: string
    role: 'doctor' | 'patient'
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
        },/*
        mockLogin(role: 'doctor' | 'patient') {
            const mockUsers: Record<string, User> = {
                doctor: {
                    id: 1,
                    name: 'doctor001',
                    phone: '12345678901',
                    idCard: '123456789012345678',
                    password: 'password',
                    workId: 'doctor001',
                    chainAddress: '0xDOCTORADDR',
                    role: 'doctor',
                },
                patient: {
                    id: 2,
                    name: 'patient001',
                    phone: '12345678902',
                    idCard: '123456789012345679',
                    password: 'password',
                    workId: '',
                    chainAddress: '0xPATIENTADDR',
                    role: 'patient',
                }
            }
            this.user = mockUsers[role]
        }*/
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