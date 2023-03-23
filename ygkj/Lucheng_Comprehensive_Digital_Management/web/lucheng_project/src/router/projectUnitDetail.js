/*
 * @Author: dengzhao
 * @LastEditTime: 2022-05-05 10:40:04
 * @Description: 
 * @FilePath: \lucheng_project\src\router\projectUnitDetail.js
 */
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
    path: '/projectUnitMenu',
    name: 'projectUnitMenu',
    redirect: '/operation',
    component: resolve => require(['@/views/projectUnitDetail/projectUnitMenu.vue'], resolve),
    children: [
      {
        path: '/operation',
        name: 'operation',
        title: '运行操作',
        redirect: '/dispatchOperation',
        component: homeView,
        children: [
          {
            path: '/dispatchOperation',
            name: 'dispatchOperation',
            title: '调度运行',
            component: (resolve) => require(['@/views/projectUnitDetail/operation/dispatch/DispatchOperation.vue'], resolve),
          },
          {
            path: '/recondition',
            name: 'recondition',
            title: '检修操作',
            redirect: '/recondition/electricOperate',
            component: homeView,
            children: [
              {
                path: '/recondition/electricOperate',
                name: 'electricOperate',
                title: '电气操作',
                component: (resolve) => require(['@/views/projectUnitDetail/operation/recondition/ElectricOperate.vue'], resolve)
              },
              {
                path: '/recondition/mechanical',
                name: 'mechanical',
                title: '机械操作',
                component: (resolve) => require(['@/views/projectUnitDetail/operation/recondition/Mechanical.vue'], resolve)
              },
            ]
          },
        ]
      },
      {
        path: '/repair',
        name: 'repair',
        title: '维修养护',
        redirect: '/yearlyRepair',
        component: homeView,
        children: [
          {
            path: '/yearlyRepair',
            name: 'yearlyRepair',
            title: '年度维修',
            redirect: '/yearlyRepair/repairPlan',
            component: homeView,
            children: [
              {
                path: '/yearlyRepair/repairPlan',
                name: 'repairPlan',
                title: '年度维修养护计划',
                component: (resolve) => require(['@/views/projectUnitDetail/repair/yearly/RepairPlan.vue'], resolve),
              },
              {
                path: '/yearlyRepair/taskCreation',
                name: 'taskCreation',
                title: '日常任务创建',
                component: (resolve) => require(['@/views/projectUnitDetail/repair/yearly/TaskCreation.vue'], resolve),
              },
              {
                path: '/yearlyRepair/defectTreatment',
                name: 'defectTreatment',
                title: '隐患处理',
                component: (resolve) => require(['@/views/projectUnitDetail/repair/defectTreatment/DefectTreatment.vue'], resolve),
              },
              {
                path: '/yearlyRepair/annualSummary',
                name: 'annualSummary',
                title: '年度汇总',
                component: (resolve) => require(['@/views/projectUnitDetail/repair/annualSummary/AnnualSummary.vue'], resolve),
              },
            ]
          },
          {
            path: '/fund',
            name: 'fund',
            title: '维修养护资金',
            component: (resolve) => require(['@/views/projectUnitDetail/repair/fund/Index.vue'], resolve),
          }
        ]
      },
      {
        path: '/engineeringSupervision',
        name: 'engineeringSupervision',
        title: '工程检查',
        redirect: '/dailyInspection',
        component: homeView,
        children: [
          {
            path: '/dailyInspection',
            name: 'dailyInspection',
            title: '日常检查',
            component: (resolve) => require(['@/views/projectUnitDetail/engineeringSupervision/dailyInspection/DailyInspection.vue'], resolve),
          },
          {
            path: '/preFloodInspection',
            name: 'preFloodInspection',
            title: '汛前检查',
            component: (resolve) => require(['@/views/projectUnitDetail/engineeringSupervision/preFloodInspection/PreFloodInspection.vue'], resolve),
          },
          {
            path: '/postFloodInspection',
            name: 'postFloodInspection',
            title: '定期（汛后）检查',
            component: (resolve) => require(['@/views/projectUnitDetail/engineeringSupervision/postFloodInspection/PostFloodInspection.vue'], resolve),
          },
          {
            path: '/annualInspection',
            name: 'annualInspection',
            title: '年度检查',
            component: (resolve) => require(['@/views/projectUnitDetail/engineeringSupervision/annualInspection/AnnualInspection.vue'], resolve),
          },
          {
            path: '/specialInspection',
            name: 'specialInspection',
            title: '特别检查',
            component: (resolve) => require(['@/views/projectUnitDetail/engineeringSupervision/specialInspection/SpecialInspection.vue'], resolve),
          },
          {
            path: '/specialExamination',
            name: 'specialExamination',
            title: '专项检查',
            component: (resolve) => require(['@/views/projectUnitDetail/engineeringSupervision/specialExamination/SpecialExamination.vue'], resolve),
          },
          {
            path: '/engineeringMonitor',
            name: 'engineeringMonitor',
            title: '工程监测',
            redirect: '/engineeringMonitor/termlyMonitor',
            component: homeView,
            children: [
              {
                path: '/engineeringMonitor/termlyMonitor',
                name: 'termlyMonitor',
                title: '定期监测',
                component: (resolve) => require(['@/views/projectUnitDetail/engineeringMonitor/TermlyMonitor.vue'], resolve)
              },
              {
                path: '/engineeringMonitor/automationMonitor',
                name: 'automationMonitor',
                title: '自动化监测',
                component: (resolve) => require(['@/views/projectUnitDetail/engineeringMonitor/AutomationMonitor.vue'], resolve)
              },
            ]
          },
        ]
      },
      {
        path: '/duty',
        name: 'duty',
        title: '值班值守',
        component: resolve => require(['@/views/projectUnitDetail/duty/Index'], resolve),
      },
      {
        path: '/greenClean',
        name: 'greenClean',
        title: '绿化保洁',
        component: resolve => require(['@/views/projectUnitDetail/greenClean/Index.vue'], resolve),
      },
      {
        path: '/control',
        name: 'control',
        title: '生物防治',
        component: resolve => require(['@/views/projectUnitDetail/control/Index.vue',], resolve),
      },
      {
        path:'/support',
        name:'support',
        title:'数字化支撑',
        redirect: '/support/hardwareMt',
        component: homeView,
        children: [
          {
            path: '/support/hardwareMt',
            name: 'hardwareMt',
            title: '硬件设备维护',
            component: (resolve) => require(['@/views/projectUnitDetail/support/hardwareMt/Index.vue'], resolve),
          },
          {
            path: '/support/dataMt',
            name: 'dataMt',
            title: '数据维护',
            component: (resolve) => require(['@/views/projectUnitDetail/support/dataMt/Index.vue'], resolve),
          }
        ]
      },
      {
        path: '/staff',
        name: 'staff',
        title: '人员培训',
        redirect: '/staff/staffTraining',
        component: homeView,
        children: [
          {
            path: '/staff/staffTraining',
            name: 'staffTraining',
            title: '人员培训',
            component: (resolve) => require(['@/views/projectUnitDetail/stafftraining/StaffTraining.vue'], resolve),
          },
        ]
      },
      {
        path: '/controlPlanManage',
        name: 'controlPlanManage',
        title: '控运计划管理',
        component: resolve => require(['@/views/projectUnitDetail/controlPlanManage/Index.vue'], resolve),
      },
      // {
      //   path: '/projectPlan',
      //   name: 'projectPlan',
      //   title: '预案管理',
      //   component: resolve => require(['@/views/projectUnitDetail/projectPlan/ProjectPlan.vue',], resolve),
      // },
      /*{
        path:'/materialsManage',
        name:'materialsManage',
        title: '物资管理',
        redirect: '/emergencyFighting',
        component: homeView,
        children: [
          {
            path:'/emergencyFighting',
            name:'emergencyFighting',
            redirect:'/purchasingManagement',
            title: '防汛抢险',
            component:homeView,
            children: [
              {
                path:'/purchasingManagement',
                name:'purchasingManage',
                title: '采购管理',
                component:(resolve)=> require(['@/views/projectUnitDetail/materialsManagement/emergencyFighting/purchasingManagement/Index'],resolve)
              }
            ]
          }
        ]
      }*/
    ]
  },
]