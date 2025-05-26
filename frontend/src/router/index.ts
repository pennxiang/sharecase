import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import Auth from '@/views/Auth.vue'

const routes = [
    {
        path: '/auth',
        component: Auth,
        meta: { layout: 'auth' }
    },
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: '',
                redirect: '/cases/time'  // 默认跳转首页
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

export default router
