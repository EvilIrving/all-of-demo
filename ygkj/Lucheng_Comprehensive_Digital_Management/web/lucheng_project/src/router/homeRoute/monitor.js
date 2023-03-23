/* 
 * @Date: 2022-05-09 17:31:09
 * @Author: 
 * @Description: 工程可视化
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-31 14:12:00
 * @FilePath: \lucheng_project\src\router\homeRoute\monitor.js
 */
import homeView from "@/layouts/HomeView.vue";

// 工程可视化
export default [
  {
    path: "/monitor",
    name: "monitor",
    title: "工程可视化",
    component: (resolve) => require(['@/views/monitor/Index.vue'], resolve),
    // children: [
    //   {
    //     path: '/monitor/videoMonitor',
    //     name: 'defense',
    //     title: '视频监控',
        
    //   },
    // ]
  },
];
