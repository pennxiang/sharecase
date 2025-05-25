import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            // redirect: '/login' // 实际开发
            redirect: '/dashboard/cases' //直接到界面，测试
        },
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/Login.vue')
        },
        {
            path: '/register',
            name: 'Register',
            component: () => import('../views/Register.vue')
        },
        {
            path: '/dashboard',
            name: 'Dashboard',
            component: () => import('../views/Dashboard.vue'),
            // meta: { requiresAuth: true },
            children: [
                {
                    path: 'cases',
                    name: 'Cases',
                    component: () => import('../views/cases/CaseList.vue')
                },
                {
                    path: 'case/create',
                    name: 'CreateCase',
                    component: () => import('../views/cases/CreateCase.vue')
                },
                {
                    path: 'case/:id',
                    name: 'CaseDetail',
                    component: () => import('../views/cases/CaseDetail.vue')
                },
                {
                    path: 'case/history/:id',
                    name: 'CaseHistory',
                    component: () => import('../views/cases/CaseHistory.vue')
                },
                {
                    path: 'statistics',
                    name: 'Statistics',
                    component: () => import('../views/Statistics.vue')
                },
                {
                    path: 'profile',
                    name: 'Profile',
                    component: () => import('../views/Profile.vue')
                }
            ]
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