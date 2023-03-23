/*
 * @Author: hanyu
 * @LastEditTime: 2021-10-18 11:22:02
 * @Description: 浙政钉应用 路由
 * @FilePath: /lucheng_project/src/router/homeRoute/zzDingApp.js
 */
import homeView from "@/layouts/HomeView.vue";

export default [
  {
    path: '/zzDingApp',
    name: 'zzDingApp',
    title: '浙政钉应用',
    component: resolve => require(['@/views/zzDingApp/ZzDingApp.vue'], resolve),
    children: [
      
    ]
  }
]