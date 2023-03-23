//判断当前环境
let routesType = process.env.NODE_ENV === "production";
const routeList = [
  {
    path: "/homePage",
    name: "homePage",
    component: (resolve) =>
      require(["../views/projectIndex/ProjectIndex"], resolve),
    meta: {
      title: "工程详情",
    },
    children: [
      {
        path: "/homePage",
        title: "首页",
        name: "homePage",
        component: (resolve) =>
          require(["../views/projectDetail/homePage/HomePage"], resolve),
      },
      {
        path: "/look",
        title: "实时监控",
        name: "look",
        component: (resolve) =>
          require(["../views/projectDetail/look/Look"], resolve),
      },
    ],
  },
  //浙里办app，小程序的单点登录和首页 ---- start
  {
    path: "/IRSAppIndex",
    name: "IRSAppIndex",
    component: (resolve) => require(["../views/appView/IRSAppIndex.vue"], resolve),
    meta: {
      posIndex: -1,
      title: "鹿城全域数字化系统",
    },
  },
  //浙里办app，小程序的单点登录和首页 ---- end
  //其他方式的单点登录的首页 ---- start
  {
    // path: routesType ? "/appIndex" : "/",
    path: "/appIndex",
    name: "appIndex",
    component: (resolve) => require(["../views/appView/AppIndex.vue"], resolve),
    meta: {
      posIndex: -1,
      title: "鹿城全域数字化系统",
    },
  },
  //其他方式的单点登录和首页 ---- end
  {
    path: "/reservoir",
    title: "水库",
    name: "reservoir",
    component: (resolve) =>
      require(["../views/appView/reservoir/Reservoir.vue"], resolve),
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
    name: "reservoirDetail",
    component: (resolve) =>
      require(["../views/appView/reservoir/ReservoirDetail.vue"], resolve),
    children: [
      {
        path: "/reservoirDetail",
        title: "基础信息",
        name: "reservoirInfo",
        meta: {
          title: "基础信息",
        },
        component: (resolve) =>
          require(["../views/appView/reservoir/indexPage/Index.vue"], resolve),
      },
      {
        path: "/waterAndRain",
        title: "水雨情",
        name: "waterAndRain",
        meta: {
          title: "基础信息",
        },
        component: (resolve) =>
          require([
            "../views/appView/reservoir/indexPage/WaterAndRain.vue",
          ], resolve),
      },
      {
        path: "/workInfo",
        title: "工情",
        meta: {
          title: "基础信息",
        },
        name: "workInfo",
        component: (resolve) =>
          require([
            "../views/appView/reservoir/indexPage/WorkCondition.vue",
          ], resolve),
      },
    ],
  },
  {
    path: "/FloodPaper",
    name: "FloodPaper",
    component: (resolve) =>
      require(["../views/appView/FloodPaper.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "汛情摘要",
    },
  },
  {
    path: "/tidalLevelForecast",
    name: "tidalLevelForecast",
    component: (resolve) =>
      require([
        "../views/appView/tidalLevelForecast/TidalLevelForecast.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "潮位预报",
    },
  },
  {
    path: "/tidalLevelForecastDetail",
    name: "tidalLevelForecastDetail",
    component: (resolve) =>
      require([
        "../views/appView/tidalLevelForecast/TidalLevelForecastDetail.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/rainfallForecast",
    name: "rainfallForecast",
    component: (resolve) =>
      require([
        "../views/appView/rainfallForecast/RainfallForecast.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "降雨预报",
    },
  },
  {
    path: "/flashFloodRisk",
    name: "flashFloodRisk",
    component: (resolve) =>
      require(["../views/appView/flashFloodRisk/FlashFloodRisk.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "山洪风险",
    },
  },
  {
    path: "/flashFloodRiskDetail",
    name: "flashFloodRiskDetail",
    component: (resolve) =>
      require([
        "../views/appView/flashFloodRisk/FlashFloodRiskDetail.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/rainfall",
    name: "rainfall",
    component: (resolve) =>
      require(["../views/appView/rainfall/RainfallList.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "实时雨情",
    },
  },
  {
    path: "/realTimeRainfall",
    name: "realTimeRainfall",
    component: (resolve) =>
      require(["../views/appView/rainfall/RealTimeRainfall.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/waterRegimen",
    name: "waterRegimen",
    component: (resolve) =>
      require(["../views/appView/waterRegimen/WaterRegimen.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "实时水情",
    },
  },
  {
    path: "/waterRegimenList",
    name: "waterRegimenList",
    component: (resolve) =>
      require(["../views/appView/waterRegimen/WaterRegimenList.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "实时水情",
    },
  },
  {
    path: "/businessApproval",
    name: "businessApproval",
    component: (resolve) =>
      require(["../views/appView/business/BusinessApproval.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "业务审批",
    },
  },
  {
    path: "/businessApprovalInfo",
    name: "businessApprovalInfo",
    component: (resolve) =>
      require(["../views/appView/business/BusinessApprovalInfo.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/businessSuggestion",
    name: "businessSuggestion",
    component: (resolve) =>
      require(["../views/appView/business/BusinessSuggestion.vue"], resolve),
    meta: {
      keepAlive: true,
      title: "处理信息",
    },
  },
  {
    path: "/businessApprovalSuggestion",
    name: "businessApprovalSuggestion",
    component: (resolve) =>
      require([
        "../views/appView/business/BusinessApprovalSuggestion.vue",
      ], resolve),
    meta: {
      keepAlive: true,
      title: "审批意见",
    },
  },
  {
    path: "/propertyApproval",
    name: "propertyApproval",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/Index.vue",
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
    component: (resolve) => require(['../views/appView/officeInformation/projectFiles/Index.vue'], resolve),
    meta: {
      posIndex: 0,
      title: '工程档案'
    }
  },
  {
    path: "/relayTask",
    name: "relayTask",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/RelayTask/Index.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "任务下达",
    },
  },
  {
    path: "/taskDetail",
    name: "taskDetail",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/RelayTask/TaskDetail.vue",
      ], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/newTask",
    name: "newTask",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/RelayTask/AddNewTask.vue",
      ], resolve),
    meta: {
      posIndex: 2,
      title: "新增",
    },
  },
  {
    path: "/progressQuery",
    name: "progressQuery",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/ProgressQuery/Index.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "进度查询",
    },
  },
  {
    path: "/progressDetail",
    name: "progressDetail",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/ProgressQuery/ProgressDetail.vue",
      ], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/notifications",
    name: "notifications",
    component: (resolve) =>
      require([
        "../views/appView/officeInformation/propertyApproval/Notifications.vue",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "消息提醒",
    },
  },
  // 物业督办
  {
    path: "/propertySupervision",
    name: "propertysupervision",
    component: (resolve) =>
      require(["../views/appView/propertysupervision/Index.vue"], resolve),
    meta: { posIndex: 0, title: "物业督办" },
  },
  {
    path: "/propertySupervision/inspection",
    name: "inspection",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/RelayTask/Index.vue",
      ], resolve),
    meta: { posIndex: 1, title: "督查检查" },
  },
  {
    path: "/propertySupervision/inspectionDetail",
    name: "inspectionDetail",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/RelayTask/TaskDetail.vue",
      ], resolve),
    meta: { posIndex: 2, title: "督查检查详情" },
  },
  {
    path: "/propertySupervision/addInspection",
    name: "inspectionDetail",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/RelayTask/AddNewTask.vue",
      ], resolve),
    meta: { posIndex: 2, title: "督查检查新增" },
  },
  {
    path: "/propertySupervision/supervise",
    name: "supervise",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/supervise/Index.vue",
      ], resolve),
    meta: { posIndex: 1, title: "督查督办" },
  },
  {
    path: "/propertySupervision/schedule",
    name: "schedule",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/ProgressQuery/Index.vue",
      ], resolve),
    meta: { posIndex: 1, title: "进度查询" },
  },
  {
    path: "/propertySupervision/scheduleDetail",
    name: "scheduleDetail",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/ProgressQuery/ProgressDetail.vue",
      ], resolve),
    meta: { posIndex: 2, title: "进度查询详情" },
  },
  {
    path: "/propertySupervision/archive",
    name: "archive",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/archive/Archive.vue",
      ], resolve),
    meta: { posIndex: 1, title: "归档成果" },
  },
  {
    path: "/propertySupervision/evaluate",
    name: "evaluate",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/archive/Evaluation.vue",
      ], resolve),
    meta: { posIndex: 2, title: "评价" },
  },
  {
    path: "/propertySupervision/evaluateArchive",
    name: "evaluateArchive",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/archive/EvaluateArchive.vue",
      ], resolve),
    meta: { posIndex: 2, title: "评价详情" },
  },
  {
    path: "/personnelAttendance",
    name: "personnelAttendance",
    component: (resolve) =>
      require([
        "../views/appView/personalAttendance/PersonnelAttendance.vue",
      ], resolve),
    meta: {
      posIndex: 0,
      title: "人员考勤",
    },
  },
  {
    path: "/personnelAttendance/AttendanceDetail",
    name: "AttendanceDetail",
    component: (resolve) =>
      require([
        "../views/appView/personalAttendance/AttendanceDetail",
      ], resolve),
    meta: {
      posIndex: 1,
      title: "人员考勤详情",
    },
  },
  {
    path: "/acceptance",
    name: "acceptance",
    component: (resolve) =>
      require(["../views/appView/acceptance/Acceptance.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "验收审批",
    },
  },
  {
    path: "/acceptanceParticulars",
    name: "acceptanceParticulars",
    component: (resolve) =>
      require(["../views/appView/acceptance/AcceptanceDetail.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "详情",
    },
  },
  {
    path: "/acceptanceAdditional",
    name: "acceptanceAdditional",
    component: (resolve) =>
      require(["../views/appView/acceptance/AcceptanceAdd.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "新增",
    },
  },
  {
    path: "/acceptanceMessage",
    name: "acceptanceMessage",
    component: (resolve) =>
      require(["../views/appView/acceptance/AcceptanceMessage.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "消息提醒",
    },
  },
  {
    path: "/acceptanceProgress",
    name: "acceptanceProgress",
    component: (resolve) =>
      require(["../views/appView/acceptance/AcceptanceProgress.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "进度查询",
    },
  },
  {
    path: "/propertySupervision/disposeInfo",
    name: "disposeInfo",
    component: (resolve) =>
      require([
        "../views/appView/propertysupervision/supervise/disposeInfo.vue",
      ], resolve),
    meta: { title: "处理信息", posIndex: 2 },
  },

  {
    path: "/workbench",
    name: "workbench",
    component: (resolve) =>
      require(["../views/appView/workbench/Workbench.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "工作台",
    },
  },
  {
    path: "/callpolice",
    name: "callpolice",
    component: (resolve) =>
      require(["../views/appView/workbench/PoliceMessage.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "报警",
    },
  },
  {
    path: "/policeDetail",
    name: "policeDetail",
    component: (resolve) =>
      require(["../views/appView/workbench/PoliceDetail.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "详情",
    },
  },
  {
    path: "/transinfo",
    name: "transinfo",
    component: (resolve) =>
      require(["../views/appView/workbench/TransInfo.vue"], resolve),
    meta: {
      posIndex: 2,
      title: "转发",
    },
  },
  {
    path: "/approve",
    name: "approve",
    component: (resolve) =>
      require(["../views/appView/workbench/Approve.vue"], resolve),
    meta: {
      posIndex: 1,
      title: "审批",
    },
  },
  {
    path: "/climate",
    name: "climate",
    component: (resolve) =>
      require(["../views/appView/climate/index.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "台风",
    },
  },
  {
    path: "/loginBind",
    name: "loginBind",
    component: (resolve) =>
      require(["../views/appView/loginBind/LoginBind.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "鹿城全域数字化系统",
    },
  },
  {
    path: "/noPermissions",
    name: "noPermissions",
    component: (resolve) =>
      require(["../views/appView/loginBind/NoPermissions.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "鹿城全域数字化系统",
    },
  },
  {
    path: "/lookFile",
    name: "LookFile",
    component: (resolve) => require(["../views/appView/lookFile/lookFile.vue"], resolve),
    meta: {
      posIndex: 0,
      title: "文件预览",
    },
  },
];


export default routeList