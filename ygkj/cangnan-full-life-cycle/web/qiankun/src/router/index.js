import Vue from "vue";
import VueRouter from "vue-router";
import routes from "../config/routes";

Vue.use(VueRouter);

//解决路由重复点击报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
	return routerPush.call(this, location).catch((err) => err);
};

const router = new VueRouter({
	mode: "history",
	base: "",
	routes,
});

router.beforeEach((to, from, next) => {
	/* 路由发生变化修改页面title */
	if (to.meta.title) {
		document.title = to.meta.title;
	}
	next();
});
export default router;
