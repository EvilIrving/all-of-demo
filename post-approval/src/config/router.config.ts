import { RouteRecordRaw } from 'vue-router';
import BasicLayout from '@/layout/BasicLayout/index.vue';
import LayoutWithNav from '@/layout/LayoutWithNav.vue'

export const constantRouterMap: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: LayoutWithNav,
    redirect: '/processed',
    meta: { title: '工作台', showNav: true },
    children: [
      {
        path: '/processed',
        name: 'ProcessedApproval',
        meta: { title: '批后监管', showNav: true },
        component: () => import('../views/processed.vue')
      },
      {
        path: '/unprocessed',
        name: 'UnprocessedApproval',
        meta: { title: '批后监管', showNav: true },
        component: () => import('../views/unprocessed.vue')
      }
    ]
  },
  {
    path: "/publicity",
    name: 'publicity',
    meta: { title: '批后监管', showNav: true },
    component: () => import('../layout/publicity.vue'),
  },
  {
    path: "/project",
    component: BasicLayout,
    redirect: "/",
    children: [
      {
        path: '/project/:id+',
        name: 'Project',
        meta: { title: '详情', showNav: true },
        component: () => import('../views/detail.vue')
      }
    ]
  },
];
