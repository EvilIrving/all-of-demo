/*
 * @Author: hanyu
 * @LastEditTime: 2022-07-04 10:13:58
 * @Description: 全物业智管 路由
 * @FilePath: \lucheng_project\src\router\homeRoute\propertyManage.js
 */
import homeView from "@/layouts/HomeView.vue";

export default [
  {
    path: '/propertyManage',
    name: 'propertyManage',
    title: '全物业智管',
    redirect: '/propertyManage/archivesManage',
    component: homeView,
    children: [
      {
        path: '/propertyManage/archivesManage',
        name: 'archivesManage',
        title: '档案管理',
        redirect: '/propertyManage/filesManage',
        component: homeView,
        children: [
          {
            path: '/propertyManage/filesManage',
            name: 'filesManage',
            title: '档案管理',
            hideFourth: true,
            redirect: '/propertyManage/filesIntegration',
            component: (resolve) => require(['@/views/propertyManage/archivesManage/filesManage/fileManageIndex.vue'], resolve),
            children: [
              {
                path: '/propertyManage/filesIntegration',
                name: 'filesIntegration',
                title: '数据整编',
                component: (resolve) => require(['@/views/propertyManage/archivesManage/filesManage/FileManage.vue'], resolve)
              },
              // 全局搜索
              {
                path: "/propertyManage/globalSearch",
                name: "GlobalSearch",
                title: '全局搜索',
                component: (resolve) => require(['@/views/propertyManage/archivesManage/filesManage/GlobalSearch.vue'], resolve)
              },
              {
                path: '/propertyManage/myCollection',
                name: 'myCollection',
                title: '我的收藏',
                component: (resolve) => require(['@/views/propertyManage/archivesManage/filesManage/MyCollection.vue'], resolve)
              },
              {
                path: '/propertyManage/logQuery',
                name: 'logQuery',
                title: '日志查询',
                component: (resolve) => require(['@/views/propertyManage/archivesManage/filesManage/LogQuery.vue'], resolve)
              }
            ]
          }
        ]
      },
      {
        path: '/propertyManage/filePermission',
        name: 'permission',
        title: '权限管理',
        redirect: '/propertyManage/filePermission',
        component: homeView,
        children: [
          {
            path: '/propertyManage/filePermission',
            name: 'FilePermission',
            title: '权限管理',
            component: (resolve) => require(['@/views/propertyManage/archivesManage/authorityManage/FilePermission.vue'], resolve),
          },
          {
            path: '/propertyManage/labelManage',
            name: 'LabelManage',
            title: '标签管理',
            component: (resolve) => require(['@/views/propertyManage/archivesManage/authorityManage/LabelManage.vue'], resolve),
          }
        ]
      },
      {
        path: '/propertyManage/propertyUnitList',
        name: 'propertyUnitList',
        title: '物业管理',
        component: (resolve) => require(['@/views/propertyManage/propertyList/PropertyUnitList.vue'], resolve),
      },
      //暂不发布

      {
        path: '/propertyManage/unitManage',
        name: 'unitManage',
        title: '单位管理',
        redirect: '/propertyManage/threeChargePeople',
        component: homeView,
        children: [
          {
            path: '/propertyManage/threeChargePeople',
            name: 'threeChargePeople',
            title: '三个责任人',
            component: (resolve) => require(['@/views/propertyManage/threeChargePeople/Index.vue'], resolve),
          },
          {
            path: '/propertyManage/runUnitManage',
            name: 'runUnitManage',
            title: '运行单位管理',
            redirect: '/propertyManage/runUnitDuty',
            component: homeView,
            children: [
              {
                path: '/propertyManage/runUnitDuty',
                name: 'runUnitDuty',
                title: '单位职责',
                component: (resolve) => require(['@/views/propertyManage/runUnitManage/unitDuty/Index.vue'], resolve),
              },
              {
                path: '/propertyManage/runPostSetting',
                name: 'runPostSetting',
                title: '岗位设置',
                component: (resolve) => require(['@/views/propertyManage/runUnitManage/postSetting/Index.vue'], resolve),
              },
              {
                path: '/propertyManage/runPostDuty',
                name: 'runPostDuty',
                title: '岗位职责',
                hideFourth: true,
                redirect: '/propertyManage/rununitResponse',
                component: (resolve) => require(['@/views/propertyManage/runUnitManage/postDuty/ResponseIndex.vue'], resolve),
                children: [
                  // 单位负责人
                  {
                    path: '/propertyManage/runUnitResponse',
                    name: 'runUnitResponse',
                    component: (resolve) =>
                      require([
                        '@/views/propertyManage/runUnitManage/postDuty/UnitResponse.vue',
                      ], resolve),
                  },
                  // 技术
                  {
                    path: '/propertyManage/runTechResponse',
                    name: 'runTechResponse',
                    component: (resolve) =>
                      require([
                        '@/views/propertyManage/runUnitManage/postDuty/TechResponse.vue',
                      ], resolve),
                  },
                  // 财务
                  {
                    path: '/propertyManage/runMoneyResponse',
                    name: 'runMoneyResponse',
                    component: (resolve) =>
                      require([
                        '@/views/propertyManage/runUnitManage/postDuty/MoneyResponse.vue',
                      ], resolve),
                  },
                ],
              },
              {
                path: '/propertyManage/runServiceItem',
                name: 'runServiceItem',
                title: '服务事项',
                component: (resolve) => require(['@/views/propertyManage/runUnitManage/serviceItem/Index.vue'], resolve),
              }
            ]
          },
          {
            path: '/propertyManage/propertyMaintenance',
            name: 'propertyMaintenance',
            redirect:'/propertyManage/proUnitDuty',
            title: '物业维护单位',
            component: homeView,
            children: [
              {
                path: '/propertyManage/proUnitDuty',
                name: 'proUnitDuty',
                title: '单位职责',
                component: (resolve) => require(['@/views/propertyManage/propertyMaintenance/unitDuty/Index.vue'], resolve),
              },
              {
                path: '/propertyManage/proPostSetting',
                name: 'proPostSetting',
                title: '岗位设置',
                component: (resolve) => require(['@/views/propertyManage/propertyMaintenance/postSetting/Index.vue'], resolve),
              },
              {
                path: '/propertyManage/proPostDuty',
                name: 'proPostDuty',
                title: '岗位职责',
                hideFourth: true,
                redirect: '/propertyManage/proUnitResponse',
                component: (resolve) => require(['@/views/propertyManage/propertyMaintenance/postDuty/ResponseIndex.vue'], resolve),
                children: [
                  // 单位负责人
                  {
                    path: '/propertyManage/proUnitResponse',
                    name: 'unitResponse',
                    component: (resolve) =>
                      require([
                        '@/views/propertyManage/propertyMaintenance/postDuty/UnitResponse.vue',
                      ], resolve),
                  },
                 // 技术
                  {
                    path: '/propertyManage/proTechResponse',
                    name: 'proTechResponse',
                    component: (resolve) =>
                      require([
                        '@/views/propertyManage/propertyMaintenance/postDuty/TechResponse.vue',
                      ], resolve),
                  },
                 // 财务
                  {
                    path: '/propertyManage/proMoneyResponse',
                    name: 'moneyResponse',
                    component: (resolve) =>
                      require([
                        '@/views/propertyManage/propertyMaintenance/postDuty/MoneyResponse.vue',
                      ], resolve),
                  },
                ],
              },
              {
                path: '/propertyManage/proPerviceItem',
                name: 'proPerviceItem',
                title: '服务事项',
                component: (resolve) => require(['@/views/propertyManage/propertyMaintenance/serviceItem/Index.vue'], resolve),
              }
            ]
          },
        ]
      },
      {
        path: '/propertyManage/propertyUnitMatter',
        name: 'propertyUnitMatter',
        title: '物业管理事项',
        component: (resolve) => require(['@/views/propertyManage/propertyUnitMatter/PropertyUnitMatter.vue'], resolve),

      },
      {
        path: '/propertyManage/contractManagement',
        name: 'contractManagement',
        title: '合同管理',
        redirect: '/contractManagement/appointAcceptanceStandard',
        component: homeView,
        children: [
          {
            path: '/contractManagement/appointAcceptanceStandard',
            name: 'appointAcceptanceStandard',
            title: '约定验收标准',
            component: homeView,
            redirect: '/contractManagement/covenant',
            children: [
              {
                path: '/contractManagement/covenant',
                name: 'covenant',
                title: '约定事项',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/appointAcceptanceStandard/covenant/Covenant.vue'], resolve),
              },
              {
                path: '/contractManagement/acceptanceStandard',
                name: 'acceptanceStandard',
                title: '验收标准',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/appointAcceptanceStandard/acceptanceStandard/AcceptanceStandard.vue'], resolve),
              },
            ]
          },
          {
            path: '/propertyManage/contractAcceptance',
            name: 'contractAcceptance',
            title: '合同验收',
            redirect: '/propertyManage/checkAndAccept',
            component: homeView,
            children: [
              {
                path: '/propertyManage/checkAndAccept',
                name: 'checkAndAccept',
                title: '验收申请',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/contractAcceptance/CheckAndAccept.vue'], resolve),
              },
              {
                path: '/propertyManage/annualAcceptance',
                name: 'annualacceptance',
                title: '年度验收',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/contractAcceptance/AnnualAcceptance.vue'], resolve),
              },
            ]
          },
          {
            path: '/propertyManage/assessmentMmanagement',
            name: 'assessmentMmanagement',
            title: '考核管理',
            redirect: '/propertyManage/dailyEvaluation',
            component: homeView,
            children: [
              {
                path: '/propertyManage/dailyEvaluation',
                name: 'dailyEvaluation',
                title: '日常考评',
                redirect: '/propertyManage/staffAttendance',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/assessmentMmanagement/dailyEvaluation/DailyEvaluationIndex.vue'], resolve),
                children: [
                  {
                    path: '/propertyManage/staffAttendance',
                    name: 'staffAttendance',
                    title: '人员考勤',
                    component: (resolve) => require(['@/views/propertyManage/contractManagement/assessmentMmanagement/dailyEvaluation/StaffAttendance.vue'], resolve),
                  },
                  {
                    path: '/propertyManage/acceptanceAnalysis',
                    name: 'acceptanceAnalysis',
                    title: '考勤分析',
                    component: (resolve) => require(['@/views/propertyManage/contractManagement/assessmentMmanagement/dailyEvaluation/AcceptanceAnalysis.vue'], resolve),
                  },
                ]
              },
              {
                path: '/propertyManage/supervisedCheck',
                name: 'supervisedCheck',
                title: '监督检查',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/assessmentMmanagement/SupervisedCheck.vue'], resolve),
              },
              {
                path: '/propertyManage/regularCheck',
                name: 'regularCheck',
                title: '定期考评',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/assessmentMmanagement/RegularCheck.vue'], resolve),
              },
              {
                path: '/propertyManage/annualCheck',
                name: 'annualCheck',
                title: '年度考评',
                component: (resolve) => require(['@/views/propertyManage/contractManagement/assessmentMmanagement/AnnualCheck'], resolve),
              },
            ]
          },
        ]
      },
    ]
  }
]
