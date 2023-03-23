import { registerMicroApps, setDefaultMountApp, start } from 'qiankun';
import NProgress from 'nprogress'; // 进度条
import '@/style/nprogress.scss';
NProgress.configure({ showSpinner: false });
/**
 * 路由监听
 * @param {*} routerPrefix 前缀
 */
function genActiveRule(routerPrefix) {
	return location => location.pathname.startsWith(routerPrefix);
}

const synthesizeSystemEntry = process.env.NODE_ENV === 'production' ? '/synthesizeSystem/' : '//localhost:8092/';
const synthesizeAmap = process.env.NODE_ENV === 'production' ? '/moduleAMap/' : '//localhost:8091/';
const datamanagement = process.env.NODE_ENV === 'production' ? '/dataManagement/' : '//localhost:8093/';
import store from './store';

const microApps = [
	{
		name: 'four-chaos',
		entry: '/fourChaos/',
		container: '#root-view',
		activeRule: genActiveRule('/four-chaos')
	},
	{
		name: 'water-disaster-custom',
		entry: synthesizeSystemEntry,
		container: '#root-view',
		activeRule: genActiveRule('/water-disaster-custom'),
		props:{
			store
		}
	},
	{
		name: 'synthesize-amap',
		entry: synthesizeAmap,
		container: '#root-view',
		activeRule: genActiveRule('/synthesize-amap')
	},
	{
		name: 'datamanagement',
		entry: datamanagement,
		container: '#root-view',
		activeRule: genActiveRule('/datamanagement')
	},
];

//设置主应用启动后默认进入的微应用
setDefaultMountApp('/water-disaster-custom/waterDisasterCustom?large=true&isBigScreen=false&bsType=waterDisaster');

// 注册子应用及其钩子
registerMicroApps(microApps, {
	beforeLoad: app => {
		NProgress.start();
		/* if (process.env.NODE_ENV !== 'production')  */ console.log('[qiankun] beforeLoad: ', app.name);
	},
	beforeMount: app => {
		/* if (process.env.NODE_ENV !== 'production')  */ console.log('[qiankun] beforeMount: ', app.name);
	},
	afterMount: app => {
		NProgress.done();
		/* if (process.env.NODE_ENV !== 'production')  */ console.log('[qiankun] afterMount: ', app.name);
	},
	beforeUnmount: app => {
		/* if (process.env.NODE_ENV !== 'production')  */ console.log('[qiankun] beforeUnmount: ', app.name);
	},
	afterUnmount: app => {
		/* if (process.env.NODE_ENV !== 'production')  */ console.log('[qiankun] afterUnmount: ', app.name);
	}
});
export default start;
