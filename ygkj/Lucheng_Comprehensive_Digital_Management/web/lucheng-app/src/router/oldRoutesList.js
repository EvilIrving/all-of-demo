//判断当前环境

const oldRoutesList = [
  {
    path: "/appIndex",
    name: "old_appIndex",
    component: (resolve) => require(["../views/oldAppView/AppIndex.vue"], resolve),
    meta: {
      posIndex: -1,
      title: "鹿城水利",
    },
  },
  {
    path: "/reservoir",
    title: "水库",
    name: "old_reservoir",
    component: (resolve) =>
      require(["../views/oldAppView/reservoir/Reservoir.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "工程",
    },
  },
  {
    path: "/reservoirDetail",
    title: "基础信息",
    meta: {
      posIndex: 1,
      title: "基础信息",
    },
    name: "old_reservoirDetail",
    component: (resolve) =>
      require(["../views/oldAppView/reservoir/ReservoirDetail.vue"], resolve),
    children: [
      {
        path: "/reservoirDetail",
        title: "基础信息",
        name: "old_reservoirInfo",
        meta: {
          title: "基础信息",
        },
        component: (resolve) =>
          require(["../views/oldAppView/reservoir/indexPage/Index.vue"], resolve),
      },
      {
        path: "/waterAndRain",
        title: "水雨情",
        name: "old_waterAndRain",
        meta: {
          title: "基础信息",
        },
        component: (resolve) =>
          require([
            "../views/oldAppView/reservoir/indexPage/WaterAndRain.vue",
          ], resolve),
      },
      {
        path: "/workInfo",
        title: "工情",
        meta: {
          title: "基础信息",
        },
        name: "old_workInfo",
        component: (resolve) =>
          require([
            "../views/oldAppView/reservoir/indexPage/WorkCondition.vue",
          ], resolve),
      },
    ],
  },
  {
    path: "/FloodPaper",
    name: "old_FloodPaper",
    component: (resolve) =>
      require(["../views/oldAppView/FloodPaper.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "汛情摘要",
    },
  },
  {
    path: "/tidalLevelForecast",
    name: "old_tidalLevelForecast",
    component: (resolve) =>
      require([
        "../views/oldAppView/tidalLevelForecast/TidalLevelForecast.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "潮位预报",
    },
  },
  {
    path: "/tidalLevelForecastDetail",
    name: "old_tidalLevelForecastDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/tidalLevelForecast/TidalLevelForecastDetail.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/rainfallForecast",
    name: "old_rainfallForecast",
    component: (resolve) =>
      require([
        "../views/oldAppView/rainfallForecast/RainfallForecast.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "降雨预报",
    },
  },
  {
    path: "/flashFloodRisk",
    name: "old_flashFloodRisk",
    component: (resolve) =>
      require(["../views/oldAppView/flashFloodRisk/FlashFloodRisk.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "山洪风险",
    },
  },
  {
    path: "/flashFloodRiskDetail",
    name: "old_flashFloodRiskDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/flashFloodRisk/FlashFloodRiskDetail.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/rainfall",
    name: "old_rainfall",
    component: (resolve) =>
      require(["../views/oldAppView/rainfall/RainfallList.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "实时雨情",
    },
  },
  {
    path: "/realTimeRainfall",
    name: "old_realTimeRainfall",
    component: (resolve) =>
      require(["../views/oldAppView/rainfall/RealTimeRainfall.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "实时雨情",
    },
  },
  {
    path: "/waterRegimen",
    name: "old_waterRegimen",
    component: (resolve) =>
      require(["../views/oldAppView/waterRegimen/WaterRegimen.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "实时水情",
    },
  },
  {
    path: "/waterRegimenList",
    name: "old_waterRegimenList",
    component: (resolve) =>
      require(["../views/oldAppView/waterRegimen/WaterRegimenList.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "实时水情",
    },
  },
  {
    path: "/businessApproval",
    name: "old_businessApproval",
    component: (resolve) =>
      require(["../views/oldAppView/business/BusinessApproval.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "业务审批",
    },
  },
  {
    path: "/businessApprovalInfo",
    name: "old_businessApprovalInfo",
    component: (resolve) =>
      require(["../views/oldAppView/business/BusinessApprovalInfo.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/businessSuggestion",
    name: "old_businessSuggestion",
    component: (resolve) =>
      require(["../views/oldAppView/business/BusinessSuggestion.vue"], resolve),
    meta: {
      keepAlive: true,
      title: "处理信息",
    },
  },
  {
    path: "/businessApprovalSuggestion",
    name: "old_businessApprovalSuggestion",
    component: (resolve) =>
      require([
        "../views/oldAppView/business/BusinessApprovalSuggestion.vue",
      ], resolve),
    meta: {
      keepAlive: true,
      title: "审批意见",
    },
  },
  {
    path: "/propertyApproval",
    name: "old_propertyApproval",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/Index.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "物业审批",
    },
  },
  {
    path: '/projectFiles/:id?',
    name: 'projectFiles',
    props: true,
    component: (resolve) => require(['../views/oldAppView/officeInformation/projectFiles/Index.vue'], resolve),
    meta: {
      posIndex: 0,
      title: '工程档案'
    }
  },
  {
    path: "/projectDetail",
    name: "old_projectDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/projectFiles/projectDetail.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/relayTask",
    name: "old_relayTask",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/RelayTask/Index.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "任务下达",
    },
  },
  {
    path: "/taskDetail",
    name: "old_taskDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/RelayTask/TaskDetail.vue",
      ], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/newTask",
    name: "old_newTask",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/RelayTask/AddNewTask.vue",
      ], resolve),
    meta: {
      posIndex: 2,
      title: "新增",
    },
  },
  {
    path: "/progressQuery",
    name: "old_progressQuery",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/ProgressQuery/Index.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "进度查询",
    },
  },
  {
    path: "/progressDetail",
    name: "old_progressDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/ProgressQuery/ProgressDetail.vue",
      ], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/notifications",
    name: "old_notifications",
    component: (resolve) =>
      require([
        "../views/oldAppView/officeInformation/propertyApproval/Notifications.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "消息提醒",
    },
  },
  // 物业督办
  {
    path: "/propertySupervision",
    name: "old_propertysupervision",
    component: (resolve) =>
      require(["../views/oldAppView/propertysupervision/Index.vue"], resolve),
    meta: { posIndex: 0, title: "物业督办" },
  },
  {
    path: "/propertySupervision/inspection",
    name: "old_inspection",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/RelayTask/Index.vue",
      ], resolve),
    meta: { posIndex: 1, title: "督查检查" },
  },
  {
    path: "/propertySupervision/inspectionDetail",
    name: "old_inspectionDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/RelayTask/TaskDetail.vue",
      ], resolve),
    meta: { posIndex: 2, title: "督查检查详情" },
  },
  {
    path: "/propertySupervision/addInspection",
    name: "old_inspectionDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/RelayTask/AddNewTask.vue",
      ], resolve),
    meta: { posIndex: 2, title: "督查检查新增" },
  },
  {
    path: "/propertySupervision/supervise",
    name: "old_supervise",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/supervise/Index.vue",
      ], resolve),
    meta: { posIndex: 1, title: "督查督办" },
  },
  {
    path: "/propertySupervision/schedule",
    name: "old_schedule",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/ProgressQuery/Index.vue",
      ], resolve),
    meta: { posIndex: 1, title: "进度查询" },
  },
  {
    path: "/propertySupervision/scheduleDetail",
    name: "old_scheduleDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/ProgressQuery/ProgressDetail.vue",
      ], resolve),
    meta: { posIndex: 2, title: "进度查询详情" },
  },
  {
    path: "/propertySupervision/archive",
    name: "old_archive",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/archive/Archive.vue",
      ], resolve),
    meta: { posIndex: 1, title: "归档成果" },
  },
  {
    path: "/propertySupervision/evaluate",
    name: "old_evaluate",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/archive/Evaluation.vue",
      ], resolve),
    meta: { posIndex: 2, title: "评价" },
  },
  {
    path: "/propertySupervision/evaluateArchive",
    name: "old_evaluateArchive",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/archive/EvaluateArchive.vue",
      ], resolve),
    meta: { posIndex: 2, title: "评价详情" },
  },
  {
    path: "/personnelAttendance",
    name: "old_personnelAttendance",
    component: (resolve) =>
      require([
        "../views/oldAppView/personalAttendance/PersonnelAttendance.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "人员考勤",
    },
  },
  {
    path: "/personnelAttendance/AttendanceDetail",
    name: "old_AttendanceDetail",
    component: (resolve) =>
      require([
        "../views/oldAppView/personalAttendance/AttendanceDetail",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "人员考勤详情",
    },
  },
  {
    path: "/acceptance",
    name: "old_acceptance",
    component: (resolve) =>
      require(["../views/oldAppView/acceptance/Acceptance.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "验收审批",
    },
  },
  {
    path: "/acceptanceParticulars",
    name: "old_acceptanceParticulars",
    component: (resolve) =>
      require(["../views/oldAppView/acceptance/AcceptanceDetail.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/acceptanceAdditional",
    name: "old_acceptanceAdditional",
    component: (resolve) =>
      require(["../views/oldAppView/acceptance/AcceptanceAdd.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "新增",
    },
  },
  {
    path: "/acceptanceMessage",
    name: "old_acceptanceMessage",
    component: (resolve) =>
      require(["../views/oldAppView/acceptance/AcceptanceMessage.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "消息提醒",
    },
  },
  {
    path: "/acceptanceProgress",
    name: "old_acceptanceProgress",
    component: (resolve) =>
      require(["../views/oldAppView/acceptance/AcceptanceProgress.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "进度查询",
    },
  },
  {
    path: "/propertySupervision/disposeInfo",
    name: "old_disposeInfo",
    component: (resolve) =>
      require([
        "../views/oldAppView/propertysupervision/supervise/disposeInfo.vue",
      ], resolve),
    meta: { title: "处理信息", posIndex: 2 },
  },

  {
    path: "/workbench",
    name: "old_workbench",
    component: (resolve) =>
      require(["../views/oldAppView/workbench/Workbench.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "工作台",
    },
  },
  {
    path: "/callpolice",
    name: "old_callpolice",
    component: (resolve) =>
      require(["../views/oldAppView/workbench/PoliceMessage.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "报警",
    },
  },
  {
    path: "/policeDetail",
    name: "old_policeDetail",
    component: (resolve) =>
      require(["../views/oldAppView/workbench/PoliceDetail.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/transinfo",
    name: "old_transinfo",
    component: (resolve) =>
      require(["../views/oldAppView/workbench/TransInfo.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "转发",
    },
  },
  {
    path: "/approve",
    name: "old_approve",
    component: (resolve) =>
      require(["../views/oldAppView/workbench/Approve.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "审批",
    },
  },
  {
    path: "/climate",
    name: "old_climate",
    component: (resolve) =>
      require(["../views/oldAppView/climate/index.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "台风",
    },
  },
  {
    path: "/loginBind",
    name: "old_loginBind",
    component: (resolve) =>
      require(["../views/oldAppView/loginBind/LoginBind.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "鹿城水利",
    },
  },
  {
    path: "/noPermissions",
    name: "old_noPermissions",
    component: (resolve) =>
      require(["../views/oldAppView/loginBind/NoPermissions.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "鹿城水利",
    },
  },
  {
    path: "/lookFile",
    name: "old_LookFile",
    component: (resolve) => require(["../views/oldAppView/lookFile/lookFile.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "文件预览",
    },
  },
];

export default oldRoutesList