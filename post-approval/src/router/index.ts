import { createRouter, createWebHistory } from 'vue-router';
import { constantRouterMap } from '../config/router.config';
import request from '@/utils/request';
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRouterMap
});

const addRouteLog = (url: string, interviewMethod: 'web端' | '快捷' | '浙政钉' | '微信') => {
  return request.post('/platform-management/routeLog/add', { url, interviewMethod });
};

router.beforeEach((to, from, next) => {
  const base = import.meta.env.BASE_URL;
  const url = (base + to.fullPath).replace(/\/\//, '/');
  if (import.meta.env.NODE_ENV === 'development') {
    next();
    return;
  }
  // addRouteLog(url, 'web端');
  next();
});

export default router;
