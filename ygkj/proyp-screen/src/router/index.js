/*
 * @Author: wqn
 * @Date: 2022-11-14 09:45:28
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-14 09:33:13
 */
import Vue from 'vue'
import VueRouter from 'vue-router'
import Reservoir from 'views/reservoir/Index.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    name: "intro",
    component: (resolve) => require(["../views/Intor.vue"], resolve),
  },
  {
    path: '/reservoir',
    name: '水库风险管控清单',
    component: Reservoir
  },
  {
    path: '/seawall',
    name: '海塘风险管控清单',
    component: () => import('views/seawall/Index.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.name) document.title = to.name;
  next();
})

export default router
