import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/auth',
            name: '登录注册',
            component: () => import('@/views/Auth.vue')
        },
        {
            path: '/cases/patient',
            name: '病例查询',
            component: () => import('../views/CaseListByPatient.vue')
        },
        {
            path: '/cases/stat',
            name: '病发统计',
            component: () => import('@/views/IcdStatOverview.vue')
        },
        {
            path: '/cases/create',
            name: '创建病例',
            component: () => import('@/views/CaseCreate.vue')
        },
        {
            path: '/patient/icd-stat',
            name: '我的icd',
            component: () => import('@/views/PatientIcdStats.vue')
        },
        {
            path: '/cases/patient/compare',
            name: '病例对比',
            component: () => import('@/views/PdfCompare.vue')
        },
        {
            path: '/cases/time',
            name: '全平台病例',
            component: () => import('@/views/GlobalCaseList.vue')
        },
        {
            path: '/cases/patient/by-icd',
            name: '按疾病查看',
            component: () => import('@/views/CaseFilterByIcd.vue')
        }





    ]
})

// 路由守卫
/*router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!token) {
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            });
        } else {
            next();
        }
    } else {
        next();
    }
});*/

export default router