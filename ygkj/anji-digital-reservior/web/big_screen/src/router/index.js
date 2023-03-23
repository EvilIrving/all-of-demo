import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

//解决路由重复点击报错
const routerPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}

export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: resolve => require(['../layouts/Login.vue'], resolve)
  },
  {
    path: '/404',
    name: '404',
    component: resolve => require(['../layouts/404.vue'], resolve)
  },
  {
    //巡检安防
    path: '/checkingSecurity',
    name: 'checkingSecurity',
    component: resolve => require(['../views/checkingSecurity/CheckingSecurity.vue'], resolve)
  },
  {
    //大坝研判
    path: '/dike',
    name: 'dike',
    component: resolve => require(['../views/dike/Dike.vue'], resolve)
  },
  {
    //应急联动
    path: '/emergencyLinkage',
    name: 'emergencyLinkage',
    component: resolve => require(['../views/emergencyLinkage/EmergencyLinkage.vue'], resolve)
  },
  {
    //防洪调度
    path: '/floodControl',
    name: 'floodControl',
    component: resolve => require(['../views/floodControl/FloodControl.vue'], resolve)
  },
  {
    //抗旱灌溉
    path: '/irrigation',
    name: 'irrigation',
    component: resolve => require(['../views/irrigation/Irrigation.vue'], resolve)
  },
  {
    //物业化成效
    path: '/property',
    name: 'property',
    component: resolve => require(['../views/property/Property.vue'], resolve)
  },
]
const createRouter = () => new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router