import Vue from 'vue'
import VueRouter from 'vue-router'
import { localData, sessionData } from "../plugins/storage"
import aMap from './homeRoute/aMap'
import bigScreen from "./homeRoute/bigScreen";
import monitor from "./homeRoute/monitor";
import seawallPrevent from "./homeRoute/seawallPrevent";
import propertyManage from "./homeRoute/propertyManage";
import occupyApprove from "./homeRoute/occupyApprove";
import defenseDispatch from "./homeRoute/defenseDispatch";
import zzDingApp from "./homeRoute/zzDingApp";
import dataManage from "./homeRoute/dataManage";
import projectDetail from "./projectDetail";
import projectUnitDetail from "./projectUnitDetail";
import systemManagement from "./homeRoute/systemManagement";
Vue.use(VueRouter);

//解决路由重复点击报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch((err) => err);
};

export const routes = [
  {
    path: "/login",
    name: "Login",
    component: (resolve) => require(["../layouts/Login.vue"], resolve),
  },
  {
    path: "/404",
    name: "404",
    component: (resolve) => require(["../layouts/404.vue"], resolve),
  },
  // {
  //   path: "/",
  //   name: "home",
  //   redirect: "/aMap",
  //   component: (resolve) => require(["../layouts/Home.vue"], resolve),
  //   children: [
  //     /**
  //      * 路由自定义配置
  //      * 一级路由 {hideSecond} true:隐藏左侧菜单(可自定义二级以下路由展示方式，默认为左侧边栏展示路由);
  //      * 三级路由 {hideFourth} true:隐藏左侧四级菜单(可自定义四级路由展示方式，默认在左侧三级路由下显示)
  //      */
  //     ...aMap, //水利地图
  //     ...seawallPrevent, //海塘防潮评估
  //     ...propertyManage, //全物业智管
  //     ...occupyApprove, //水域占用审批
  //     ...defenseDispatch, //灾害防御调度
  //     // ...zzDingApp, //浙政钉应用
  //     ...monitor, //工程可视化
  //     ...bigScreen, //数字化场景
  //     // ...dataManage, //数据管理系统
  //     ...systemManagement
  //   ].reverse(),
  // },
  // ...projectDetail,
  // ...projectUnitDetail,
];


const createRouter = () => new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routes
})

const router = createRouter()

// 路由拦截 判断登录信息是否存在
// const whiteList = ['/login']
// router.beforeEach(async (to, from, next) => {
//   // console.log(JSON.stringify(to.query));
//   if (to.path === '/seawallPrevent/supervision' && JSON.stringify(to.query) !== '{}') {
//     next()
//   } else {
//     let userInfo = sessionData('get', 'userInfo');
//     if (userInfo && userInfo.accessToken) {
//       if (to.path.indexOf('&') !== -1) {
//         next('/')
//       } else {
//         next()
//       }
//     } else {
//       if (whiteList.indexOf(to.path) !== -1) {
//         next()
//       } else {
//         if (to.path.indexOf('&') !== -1) {
//           next('/')
//         } else {
//           next('/login')
//         }
//       }
//     }
//   }
// })

export default router