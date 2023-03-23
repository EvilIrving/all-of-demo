/*
 * @Author: hanyu
 * @LastEditTime: 2022-05-18 09:34:53
 * @Description: 应急联动
 * @FilePath: \anji_project\src\router\homeRoute\systemManagement.js
 */

import homeView from "@/layouts/HomeView.vue";

// 应急联动
export default [
  {
    path: '/systemManagement',
    name: 'systemManagement',
    title: '系统管理',
    redirect: '/systemManagement/userManagement',
    component: homeView,
    children: [
      // 应急预案
      {
        path: '/systemManagement/userManagement',
        name: 'userManagement',
        title: '单位用户管理',
        component: resolve => require(['@/views/systemManagement/userManagement/UserManagement.vue'], resolve),
      },
      {
        path: '/systemManagement/menuManagement',
        name: 'menuManagement',
        title: '菜单管理',
        component: resolve => require(['@/views/systemManagement/menuManagement/MenuManagement.vue'], resolve),
      },
      {
        path: '/systemManagement/authorityManagement',
        name: 'authorityManagement',
        title: '权限管理',
        component: resolve => require(['@/views/systemManagement/authorityManagement/AuthorityManagement.vue'], resolve),
      },
      {
        path: '/systemManagement/onDutyManagement',
        name: 'onDutyManagement',
        title: '值班管理',
        component: resolve => require(['@/views/systemManagement/onDutyManagement/OnDutyManagement.vue'], resolve),
      }
    ]
  }
]