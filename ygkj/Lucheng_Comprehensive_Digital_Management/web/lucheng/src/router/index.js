import Vue from 'vue';
import VueRouter from 'vue-router';
import { hydraulicAmap } from './modules/index';

Vue.use(VueRouter);

//解决路由重复点击报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push (location) {
  return routerPush.call(this, location).catch((err) => err);
};

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: (resolve) => require(['../layouts/Login.vue'], resolve)
  },
  {
    path: '/amaplogin',
    name: 'AmapLogin',
    component: (resolve) => require(['../components/amaplogin.vue'], resolve)
  },
  {
    path: '/',
    name: 'Home',
    redirect: '/aMapOfLucheng',
    component: (resolve) => require(['../layouts/ScreenLayout'], resolve),
    meta: { keepAlive: true },
    children: [
      ...hydraulicAmap,
      {
        path: '/waterDisasterCustom',
        name: 'waterDisasterCustom',
        meta: { title: '海塘防潮评估' },
        component: (resolve) => require(['@/views/water_disaster_custom/index_back.vue'], resolve)
      },
      {
        path: '/disasterPreventionScheduling',
        name: 'disasterPreventionScheduling',
        meta: { title: '灾害防御调度' },
        component: (resolve) => require(['@/views/water_disaster_custom/index_back_scheduling.vue'], resolve)
      },
      {
        path: '/scheduleMap',
        name: 'ScheduleMap',
        meta: { title: '流域工程调度' },
        component: (resolve) => require(['@/views/WatershedSchedule/index'], resolve)
      },
      {
        path: '/scheduleMapProject',
        name: 'scheduleMapProject',
        meta: { title: '工程概化图' },
        component: (resolve) => require(['@/views/water_disaster_custom/schedule-map/index'], resolve)
      },
      {
        path: '/ourPujiang',
        name: 'ourPujiang',
        meta: { title: '戍浦江防洪调度' },
        component: (resolve) => require(['@/views/ourPujiang/index'], resolve)
      },
      {
        path: '/index',
        name: 'index',
        component: (resolve) => require(['../views/WaterDisasterPreventionScreen/index.vue'], resolve)
      },
      {
        path: '/generalizatrionMap',
        name: 'GeneralizatrionMap',
        component: (resolve) =>
          require(['../views/WaterDisasterPreventionScreen/components/GeneralizationMap.vue'], resolve)
      },
      {
        path: '/projectScheduling',
        name: 'projectScheduling',
        meta: { title: '工程调度图' },
        component: (resolve) =>
          require(['../views/new_screen/projectScheduling/index.vue'], resolve)
      },
      {
        path: '/projectVisualization',
        name: 'projectVisualization',
        meta: { title: '工程可视化' },
        component: (resolve) =>
          require(['../views/new_screen/projectVisualization/index.vue'], resolve)
      },
      {
        path: '/aMapOfLucheng',
        name: 'aMapOfLucheng',
        component: (resolve) =>
          require(['../views/new_screen/aMapOfLucheng/Index.vue'], resolve)
      },
    ]
  }
];

const router = new VueRouter({
  mode: 'history',
  base: window.__POWERED_BY_QIANKUN__ ? '/water-disaster-custom' : process.env.BASE_URL,
  routes
});

// // 路由拦截 判断登录信息是否存在
// const whiteList = ["/login", "/amaplogin"];
// router.beforeEach((to, from, next) => {
// 	if (whiteList.indexOf(to.path) == -1) {
// 		let userInfo = localData("get", "userInfo");
// 		if (userInfo && userInfo.username) {
// 			let value = new Date().getTime() - userInfo.timestamp;
// 			if (value >= 10 * 1000) {
// 				// localData("clean", "userInfo");
// 				actions.setGlobalState({ returnLogin: true });
// 				// this.$router.replace("https://sgpt.zjwater.com/");
// 				// next("/login");
// 				Message({
// 					message: "登录过期，请重新登录",
// 					type: "info",
// 				});
// 			} else {
// 				next();
// 			}
// 		} else {
// 			// next("/login");
// 		}
// 	} else {
// 		next();
// 	}
// });
export default router;
