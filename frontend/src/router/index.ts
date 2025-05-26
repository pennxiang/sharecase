import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import Auth from '@/views/Auth.vue'
import {useUserStore} from "../store/user.ts";

const routes = [
    {
        path: '/auth',
        component: Auth,
        meta: { layout: 'auth' }
    },
    {
        path: '/auth/register',
        name: '注册',
        component: () => import('@/views/Register.vue')
    },
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: '',
                redirect: '/auth'  // 默认跳转首页
            },
            {
                path: 'cases/time',
                name: '全平台病例',
                component: () => import('@/views/CaseGlobalList.vue')
            },
            {
                path: 'cases/patient',
                name: '我的病例',
                component: () => import('@/views/CaseListByPatient.vue')
            },
            {
                path: 'cases/patient/compare',
                name: '病例对比',
                component: () => import('@/views/PdfCompare.vue')
            },
            {
                path: 'cases/stat',
                name: 'ICD 统计',
                component: () => import('@/views/IcdStatOverview.vue')
            },
            {
                path: 'patient/icd-stat',
                name: '我的 ICD',
                component: () => import('@/views/PatientIcdStats.vue')
            },
            {
                path: 'settings',
                name: '个人设置',
                component: () => import('@/views/Settings.vue')
            },
            {
                path: 'cases/create',
                name: '创建病例',
                component: () => import('@/views/CaseCreate.vue')
            }
        ]
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 登录白名单
const whiteList = ['/auth', '/auth/register']

router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    const isLogin = !!userStore.user

    if (whiteList.includes(to.path)) {
        next()
    } else {
        if (isLogin) {
            next()
        } else {
            next('/auth')
        }
    }
})

export default router
