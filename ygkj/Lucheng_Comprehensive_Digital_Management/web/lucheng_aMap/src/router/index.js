import Vue from 'vue';
import VueRouter from 'vue-router';
import { hydraulicAmap } from './modules/index';
import { localData } from '../plugins/storage';
import { Message } from 'element-ui';

Vue.use(VueRouter);

//解决路由重复点击报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
	return routerPush.call(this, location).catch(err => err);
};

const routes = [
	{
		path: '/login',
		name: 'Login',
		component: resolve => require(['../layouts/Login.vue'], resolve)
	},
	{
		path: '/amaplogin',
		name: 'AmapLogin',
		component: resolve => require(['../components/amaplogin.vue'], resolve)
	},
	{
		path: '/',
		name: 'Home',
		redirect: '/hydraulicAmap',
		component: resolve => require(['../layouts/ScreenLayout'], resolve),
		meta: { keepAlive: true },
		children: [...hydraulicAmap]
	}
];

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
});

// 路由拦截 判断登录信息是否存在
/* const whiteList = ["/login", "/amaplogin"];
router.beforeEach((to, from, next) => {
	if (whiteList.indexOf(to.path) == -1) {
		let userInfo = localData("get", "userInfo");
		if (userInfo && userInfo.username) {
			let value = new Date().getTime() - userInfo.timestamp;
			if (value >= 2 * 3600 * 1000) {
				localData("clean", "userInfo");
				next("/login");
				Message({
					message: "登录过期，请重新登录",
					type: "info",
				});
			} else {
				next();
			}
		} else {
			next("/login");
		}
	} else {
		next();
	}
}); */
export default router;
