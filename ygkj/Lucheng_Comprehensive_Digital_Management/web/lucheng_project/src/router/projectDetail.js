/*
 * @Author: hanyu
 * @LastEditTime: 2022-03-07 10:30:21
 * @Description: 海塘详情路由
 * @FilePath: \lucheng_project\src\router\projectDetail.js
 */
import homeView from "@/layouts/HomeView.vue";

// 项目详情
export default [
  {
    path: '/ProjectMenu',
    name: 'projectMenu',
    redirect: '/preview',
    component: resolve => require(['@/views/projectDetail/ProjectMenu.vue'], resolve),
    children: [
      {
        path: '/preview',
        name: 'preview',
        title: '信息总览',
        component: (resolve) => require(['@/views/projectDetail/preview/Preview.vue'], resolve)
      },
      {
        path: '/baseMsg',
        name: 'baseMsg',
        title: '基础信息',
        redirect: '/projectMsg',
        component: homeView,
        children: [
          {
            path: '/projectMsg',
            name: 'projectMsg',
            title: '工程信息',
            component: (resolve) => require(['@/views/projectDetail/baseMsg/ProjectMsg.vue'], resolve)
          },
          {
            path: '/unitMsg',
            name: 'unitMsg',
            title: '单位信息',
            component: (resolve) => require(['@/views/projectDetail/baseMsg/UnitMsg.vue'], resolve)
          },
          {
            path: '/imageMsg',
            name: 'imageMsg',
            title: '形象面貌',
            component: (resolve) => require(['@/views/projectDetail/baseMsg/ImageMsg.vue'], resolve)
          },
          {
            path: '/delimitationMsg',
            name: 'delimitationMsg',
            title: '划界信息',
            component: (resolve) => require(['@/views/projectDetail/baseMsg/DelimitationMsg.vue'], resolve)
          },
          {
            path: '/projectMonitor',
            name: 'projectMonitor',
            title: '关联站点',
            component: (resolve) => require(['@/views/projectDetail/baseMsg/ProjectMonitor.vue'], resolve)
          },
        ]
      },
      {
        path: '/projectManage',
        name: 'projectManage',
        title: '工程管理',
        redirect: '/threePeople',
        component: homeView,
        children: [
          {
            path: '/threePeople',
            name: 'threePeople',
            title: '安全责任制',
            component: (resolve) => require(['@/views/projectDetail/projectManage/ThreePeople.vue'], resolve)
          },
          {
            path: '/bigThing',
            name: 'bigThing',
            title: '大事记管理',
            component: (resolve) => require(['@/views/projectDetail/projectManage/BigThing.vue'], resolve)
          },
          {
            path: '/propertyContract',
            name: 'propertyContract',
            title: '物业合同',
            component: (resolve) => require(['@/views/projectDetail/projectManage/PropertyContract.vue'], resolve)
          },
          {
            path: '/delimitation',
            name: 'delimitation',
            title: '划界确权',
            component: (resolve) => require(['@/views/projectDetail/projectManage/Delimitation.vue'], resolve)
          }
        ]
      },
      {
        path: '/safeManage',
        name: 'safeManage',
        title: '安全管理',
        redirect: '/safeInfo',
        component: homeView,
        children: [
          {
            path: '/safeInfo',
            name: 'safeInfo',
            title: '安全鉴定',
            component: (resolve) => require(['@/views/projectDetail/safeManage/SafeInfo.vue'], resolve)
          },
          {
            path: '/derInfo',
            name: 'derInfo',
            title: '除险加固',
            component: (resolve) => require(['@/views/projectDetail/safeManage/DerInfo.vue'], resolve)
          },
          {
            path: '/engineeringDrawings',
            name: 'engineeringDrawings',
            title: '工程图纸',
            component: (resolve) => require(['@/views/projectDetail/monitoring/EngineeringDrawings.vue'], resolve)
          },
          {
            path: '/dataMonitor',
            name: 'dataMonitor',
            title: '数据监测',
            component: (resolve) => require(['@/views/projectDetail/monitoring/DataMonitor.vue'], resolve)
          },
          {
            path: '/dataMonitorAnalysis',
            name: 'dataMonitorAnalysis',
            title: '数据分析',
            component: (resolve) => require(['@/views/projectDetail/monitoring/DataMonitorAnalysis.vue'], resolve)
          },
          {
            path: '/pointManage',
            name: 'pointManage',
            title: '测点管理',
            component: (resolve) => require(['@/views/projectDetail/monitoring/PointManage.vue'], resolve)
          }
        ]
      },
      {
        path: '/emergencyManage',
        name: 'emergencyManage',
        title: '应急管理',
        redirect: '/goodsManage',
        component: homeView,
        children: [
          {
            path: '/goodsManage',
            name: 'goodsManage',
            title: '物资管理',
            component: (resolve) => require(['@/views/projectDetail/emergencyManage/GoodsManage.vue'], resolve)
          },
          {
            path: '/reservePlan',
            name: 'reservePlan',
            title: '预案管理',
            component: (resolve) => require(['@/views/projectDetail/emergencyManage/ReservePlan.vue'], resolve)
          }
        ]
      },
      {
        path: '/dailyManage',
        name: 'dailyManage',
        title: '日常管理',
        redirect: '/filesManage',
        component: homeView,
        children: [
          {
            path: '/filesManage',
            name: 'filesManage',
            title: '档案管理',
            component: (resolve) => require(['@/views/projectDetail/dailyManage/FilesLook.vue'], resolve)
          },
          {
            path: '/maintenance',
            name: 'maintenance',
            title: '维修养护',
            component: (resolve) => require(['@/views/projectDetail/dailyManage/Maintenance.vue'], resolve)
          },
          {
            path: '/engineeringInspection',
            name: 'engineeringInspection',
            title: '工程检查',
            component: (resolve) => require(['@/views/projectDetail/dailyManage/EngineeringInspection.vue'], resolve)
          }
        ]
      },
      // {
      //   path: '/monitoring',
      //   name: 'monitoring',
      //   title: '监测维护',
      //   redirect: '/pointManage',
      //   component: homeView,
      //   children: [
      //     {
      //       path: '/pointManage',
      //       name: 'pointManage',
      //       title: '测点管理',
      //       component: (resolve) => require(['@/views/projectDetail/monitoring/PointManage.vue'], resolve)
      //     }
      //   ]
      // }
    ]
  },
]