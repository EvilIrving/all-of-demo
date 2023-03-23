/*
 * @Author: hanyu
 * @LastEditTime: 2021-10-15 16:44:46
 * @Description: 一张图路由
 * @FilePath: /lucheng_project/src/router/project/aMap.js
 */
import homeView from "@/layouts/HomeView.vue";

// 项目详情
export default [
  {
    path: '/aMap',
    name: 'projectIndex',
    title: '水利地图',
    component: (resolve) => require(['@/views/aMap/AMap.vue'], resolve)
  }
]