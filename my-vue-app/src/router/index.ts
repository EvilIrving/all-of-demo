//  src/router/index.ts
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import * as nProgress from 'nprogress'
const routes: Array<RouteRecordRaw> = [
    // {
    //     path: '/',
    //     redirect: '/login'
    // },
    // {
    //     path: '/:pathMatch(.*)*',
    //     name: 'NotFound',
    //     component: () => import('@views/error/404.vue')
    // },
    // {
    //     path: '/login',
    //     name: 'login',
    //     meta: {
    //         title: '登录',
    //         keepAlive: true,
    //         requireAuth: false
    //     },
    //     component: () => import('@views/layout/login.vue')
    // }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    nProgress.start()
    next()
})

router.afterEach(() => {
    nProgress.done()
})

export default router