/*
 * @Author: hanyu
 * @LastEditTime: 2022-07-04 17:23:39
 * @Description: 海塘防潮评估路由
 * @FilePath: \lucheng_project\src\router\homeRoute\seawallPrevent.js
 */
import homeView from '@/layouts/HomeView.vue'

// 项目详情
export default [
  {
    path: '/seawallPrevent',
    name: 'seawallPrevent',
    title: '海塘防潮评估',
    redirect: '/seawallPrevent/projectList',
    component: homeView,
    children: [
      // 海塘工程名录
      {
        path: '/seawallPrevent/projectList',
        name: 'projectList',
        title: '海塘工程名录',
        component: (resolve) =>
          require([
            '@/views/seawallPrevent/projectList/ProjectList.vue',
          ], resolve),
      },

      // 风暴潮预报
      {
        path: '/seawallPrevent/stormWarning',
        name: 'stormWarning',
        title: '风暴潮预报',
        redirect: '/seawallPrevent/stormForecast',
        component:homeView,
        children: [
          {
            path: "/seawallPrevent/stormForecast",
            name:'stormForecast',
            title:'风暴潮预报',
            component: (resolve) =>
                require([
                  '@/views/seawallPrevent/stormWarning/StormWarning.vue',
                ], resolve),
          },
          {
            path: '/seawallPrevent/stormWarning/predictingSection',
            name:'predictingSection',
            title:'预测断面',
            component:(resolve) =>
                require(['@/views/seawallPrevent/stormWarning/predictingSection/index.vue'
                ],resolve),
          },
          {
            path: '/seawallPrevent/stormWarning/sectionConfiguration',
            name:'sectionConfiguration',
            title:'断面配置',
            component:(resolve) =>
                require(['@/views/seawallPrevent/stormWarning/sectionConfiguration/index.vue'
                ],resolve),
          },
        ]
      },
      // 培训演练管理
      {
        path: '/seawallPrevent/trainingExercises',
        name: 'trainingExercises',
        title: '培训演练管理',
        redirect: '/seawallPrevent/exercisesManage',
        component: homeView,
        children: [
          {
            path: '/seawallPrevent/exercisesManage',
            name: 'exercisesManage',
            title: '培训管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/trainingExercises/exercisesManage/ExercisesManage.vue',
              ], resolve)
          },
          {
            path: '/seawallPrevent/trainingManage',
            name: 'trainingManage',
            title: '演练管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/trainingExercises/trainingManage/TrainingManage.vue',
              ], resolve),
          },
        ],
      },
      {
        path: '/seawallPrevent/riskEarlyWarning',
        name: 'riskEarlyWarning',
        title: '风险预警管理',
        redirect: '/seawallPrevent/peopleManagement',
        component: homeView,
        children: [
          {
            path: '/seawallPrevent/peopleManagement',
            name: 'peopleManagement',
            title: '人员管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/riskEarlyWarning/peopleManagement/PeopleManagement.vue',
              ], resolve),
          },
          {
            path: '/seawallPrevent/ruleConfiguration',
            name: 'ruleConfiguration',
            title: '规则配置',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/riskEarlyWarning/ruleConfiguration/ruleConfiguration.vue',
              ], resolve),
          },
          {
            path: '/seawallPrevent/alarmManagement',
            name: 'alarmManagement',
            title: '报警管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/riskEarlyWarning/alarmManagement/alarmManagement.vue',
              ], resolve),
          },
          // {
          //   path: '/seawallPrevent/trainingManage',
          //   name: 'trainingManage',
          //   title: '演练管理',
          //   component: (resolve) => require(['@/views/seawallPrevent/trainingExercises/trainingManage/TrainingManage.vue'], resolve)
          // }
        ],
      },
      // 基础信息管理
      {
        path: '/seawallPrevent/baseMsg',
        name: 'refugeManage',
        title: '基础信息管理',
        redirect: '/seawallPrevent/refugeManage',
        component: homeView,
        children: [
          {
            path: '/seawallPrevent/refugeManage',
            name: 'refugeManage',
            title: '避灾点管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/refugeManage/RefugeManage.vue',
              ], resolve),
          },
          {
            path: '/seawallPrevent/rescueTeamManage',
            name: 'rescueTeamManage',
            title: '救援队伍管理',
            hideFourth: true,
            redirect: '/seawallPrevent/rescueTeamManage/team',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/rescueTeamManage/RescueTeamManageIndex.vue',
              ], resolve),
            children: [
              // 救援队伍
              {
                path: '/seawallPrevent/rescueTeamManage/team',
                name: 'rescueTeamManageTeam',
                component: (resolve) =>
                  require([
                    '@/views/seawallPrevent/baseMsg/rescueTeamManage/Team.vue',
                  ], resolve),
              },
              // 队伍人员管理
              {
                path: '/seawallPrevent/rescueTeamManage/people',
                name: 'rescueTeamManagePeople',
                component: (resolve) =>
                  require([
                    '@/views/seawallPrevent/baseMsg/rescueTeamManage/People.vue',
                  ], resolve),
              },
              // 队伍设备管理
              {
                path: '/seawallPrevent/rescueTeamManage/equip',
                name: 'rescueTeamManageEquip',
                component: (resolve) =>
                  require([
                    '@/views/seawallPrevent/baseMsg/rescueTeamManage/Equip.vue',
                  ], resolve),
              },
            ],
          },
          {
            path: '/seawallPrevent/rescueWarehouseManage',
            name: 'rescueWarehouseManage',
            title: '救援仓库管理',
            hideFourth: true,
            redirect: '/seawallPrevent/rescueWarehouseManage/warehouse',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/rescueWarehouseManage/RescueWarehouseManageIndex.vue',
              ], resolve),
            children: [
              // 救援仓库
              {
                path: '/seawallPrevent/rescueWarehouseManage/warehouse',
                name: 'rescueWarehouseManageWarehouse',
                component: (resolve) =>
                  require([
                    '@/views/seawallPrevent/baseMsg/rescueWarehouseManage/Warehouse.vue',
                  ], resolve),
              },
              // 仓库物资管理
              {
                path: '/seawallPrevent/rescueWarehouseManage/material',
                name: 'rescueWarehouseManageMaterial',
                component: (resolve) =>
                  require([
                    '@/views/seawallPrevent/baseMsg/rescueWarehouseManage/Material.vue',
                  ], resolve),
              },
              // 入库清单
              {
                path: '/seawallPrevent/rescueWarehouseManage/intoStock',
                name: 'rescueWarehouseManageIntoStock',
                component: (resolve) =>
                    require([
                      '@/views/seawallPrevent/baseMsg/rescueWarehouseManage/IntoStock.vue',
                    ], resolve),
              },
              // 出库清单
              {
                path: '/seawallPrevent/rescueWarehouseManage/outStock',
                name: 'rescueWarehouseManageOutStock',
                component: (resolve) =>
                    require([
                      '@/views/seawallPrevent/baseMsg/rescueWarehouseManage/OutStock.vue',
                    ], resolve),
              },
              // 报废清单
              {
                path: '/seawallPrevent/rescueWarehouseManage/scrapped',
                name: 'rescueWarehouseManageOutStock',
                component: (resolve) =>
                    require([
                      '@/views/seawallPrevent/baseMsg/rescueWarehouseManage/Scrapped.vue',
                    ], resolve),
              },
            ]
          },
          {
            path: '/seawallPrevent/SeawallEVM',
            name: 'SeawallEVM',
            title: '海塘极值管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/refugeManage/SeawallEVM.vue',
              ], resolve),
          },
          {
            path: '/seawallPrevent/StationEVM',
            name: 'StationEVM',
            title: '测站极值管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/refugeManage/StationEVM.vue',
              ], resolve),
          },
          {
            path: '/seawallPrevent/expertDatabaseManage',
            name: 'expertDatabaseManage',
            title: '专家库管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/expertDatabaseManage/ExpertDatabaseManage.vue',
              ], resolve),
          },
          {
            path: '/seawallPrevent/knowledgeBaseManage',
            name: 'knowledgeBaseManage',
            title: '知识库管理',
            component: (resolve) =>
              require([
                '@/views/seawallPrevent/baseMsg/knowledgeBaseManage/KnowledgeBaseManage.vue',
              ], resolve),
          },
          {
            path: "/seawallPrevent/MonitoringPointManage",
            name: "monitoringPointManage",
            title: "监测点管理",
            component: (resolve) =>
              require([
                "@/views/seawallPrevent/baseMsg/MonitoringPointManage/MonitoringPointManage.vue",
              ], resolve),
          },
          {
            path: "/seawallPrevent/EmergencySummaryManage",
            name: "EmergencySummaryManage",
            title: "应急总结管理",
            component: (resolve) =>
                require([
                  "@/views/seawallPrevent/baseMsg/EmergencySummaryManage/EmergencySummaryManage.vue",
                ], resolve),
          },
        ],
      },
      // 督办中心
      {
        path: '/seawallPrevent/supervision',
        name: 'supervision',
        title: '督办中心',
        component: (resolve) =>
          require(['@/views/supervisionCenter/Supervision.vue'], resolve),
      },
    ],
  },
]
