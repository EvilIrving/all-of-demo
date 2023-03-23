/*
 * @Author: hanyu
 * @LastEditTime: 2022-05-31 17:09:11
 * @Description: 水域占用审批 路由
 * @FilePath: \lucheng_project\src\router\homeRoute\occupyApprove.js
 */
import homeView from "@/layouts/HomeView.vue";

export default [
  {
    path: '/occupyApprove',
    name: 'occupyApprove',
    title: '水域占用审批',
    // component: (resolve) => require(["@/views/occupyApprove/OccupyApprove.vue"], resolve),
    component: homeView,
    redirect: '/occupyApprove/projectSupervision',
    children: [
      // 批后监管
      {
        path: "/occupyApprove/projectSupervision",
        name: "projectSupervision",
        title: '批后监管',
        component: resolve => require(["@/views/occupyApprove/project_supervision/index.vue"], resolve),
      },
      {
        path: '/occupyApprove/hydrograph',
        name: 'hydrograph',
        title: '水域一张图',
        component: homeView,
        redirect: '/hydrograph/waterOccupy',
        children: [
          {
            path: '/hydrograph/waterOccupy',
            name: 'waterOccupy',
            title: '水域占用',
            component: resolve => require(['@/views/occupyApprove/hydrograph/waterOccupy.vue'], resolve)
          },
          {
            path: '/hydrograph/waterApproval',
            name: 'waterApproval',
            title: '水域审批',
            component: resolve => require(['@/views/occupyApprove/hydrograph/waterApproval.vue'], resolve)
          }
        ]
      },
      {
        path:'/occupyApprove/waterReport',
        name:'waterReport',
        title:'水域报表',
        component: homeView,
        redirect: '/waterReport/waterArea',
        children: [
          {
            path: '/waterReport/waterArea',
            name: 'waterArea',
            title: '水域面积报表',
            component: resolve => require(['@/views/occupyApprove/waterReport/WaterArea.vue'], resolve)
          },
          {
            path: '/waterReport/illegalOccupation',
            name: 'illegalOccupation',
            title: '违法占用报表',
            component: resolve => require(['@/views/occupyApprove/waterReport/IllegalOccupation.vue'], resolve)
          },
          {
            path: '/waterReport/waterChange',
            name: 'waterChange',
            title: '水域变化报表',
            component: resolve => require(['@/views/occupyApprove/waterReport/WaterChange.vue'], resolve)
          }
        ]
      },
      {
        path:'/occupyApprove/approval-project',
        name:'approval-project',
        title:'审批项目管理',
        component: resolve => require(["@/views/occupyApprove/approval-project/ApprovalProject.vue"], resolve),
      },
    ]
  }
]