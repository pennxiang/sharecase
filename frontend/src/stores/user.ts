import { defineStore } from 'pinia'
import type { User } from '@/types'

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
