/*
 * @Author: wanxin
 * @LastEditTime: 2023-01-30 09:38:12
 * @Descripttion:
 * @FilePath: \lucheng-app\src\router\index.js
 */
import { Toast } from "vant";
import Vue from "vue";
import VueRouter from "vue-router";
import { sessionData } from "../plugins/storage";
import routesList from './routesList'
import oldRoutesList from './oldRoutesList'
import store from '../store/index'
Vue.use(VueRouter);
//解决路由重复点击报错
const routerPush = VueRouter.prototype.push;
const routerReplace = VueRouter.prototype.replace;
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch((err) => err);
};

VueRouter.prototype.replace = function push(location, onResolve, onReject) {
  if (onResolve || onReject) {
    return routerReplace.call(this, location, onResolve, onReject)
  }
  return routerReplace.call(this, location).catch(err => err)
}
let routesType = process.env.NODE_ENV === "production";
//路由列表
const routes = [
  //浙里办app，小程序的单点登录和首页 ---- start
  {
    path: "/",
    name: "IRSLogin",
    component: (resolve) => require(["../layouts/IRSLogin.vue"], resolve),
    meta: {
      posIndex: -1,
      title: "鹿城全域数字化系统",
    },
  },
  //浙里办app，小程序的单点登录和首页 ---- end
  //其他方式的单点登录和首页 ---- start
  // {
  //   path: routesType ? "/login" : "/",
  //   // path: "/",
  //   name: "Login",
  //   component: (resolve) => require(["../layouts/OtherLogin.vue"], resolve),
  //   meta: {
  //     posIndex: -1,
  //     title: "鹿城全域数字化系统",
  //   },
  // },
  // {
  //   //短信进入 督办路由，勿动
  //   path: routesType ? "/" : "/supervise",
  //   // path: "/supervise",
  //   name: "super",
  //   component: (resolve) => require(["../views/Supervise"], resolve),
  //   meta: {
  //     title: "督办整改",
  //   },
  // },
  //其他方式的单点登录和首页 ---- end
]
// routes = [...router, ...routeList];

const router = new VueRouter({
  mode: "hash",
  // mode: "history",
  base: process.env.BASE_URL,
  routes,
});
//跳过token认证名单
let routeWhiteList = ['/loginBind', '/noPermissions']

//路由守卫
router.beforeEach(async (to, form, next) => {
  console.log(to, 'formformformform')
  if (to.query.type === '物业') {
    to.meta.title = '物业档案'
  } else if (to.query.type === '工程') {
    to.meta.title = '工程档案'
  }
  let userInfo = sessionData("get", "userInfo");
  let addRoutesNum = store.getters.addRoutesNum
  if (addRoutesNum == 0 && to.path != '/' && to.path != '/login') {
    let addRouteType = store.getters.addRouteType
    if (addRouteType === 'normal') {
      router.addRoutes(routesList)
      store.dispatch('addRoutesNum', 1)
    } else {
      router.addRoutes(oldRoutesList)
      store.dispatch('addRoutesNum', 1)
    }
    next({ ...to, replace: true })
  } else {
    if (userInfo && userInfo.accessToken || routeWhiteList.includes(to.path)) {
      // next({ ...to, replace: true });
      next();
    } else {
      next();
    }
  }
});
export default router;
