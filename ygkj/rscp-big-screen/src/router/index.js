import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "intro",
    component: (resolve) => require(["../views/Intor.vue"], resolve),
  },
  {
    path: "/home",
    name: "home",
    component: (resolve) => require(["../views/Home.vue"], resolve),
    redirect: "/menu",
    children: [
      // 工程运管-驾驶舱-7个大屏
      // 驾驶舱页面
      {
        path: "/menu",
        name: "Menu",
        component: (resolve) => require(["../views/rscpDriveScreen/Menu.vue"], resolve),
      },
      // 海塘大屏
      {
        path: "/seawallScreen",
        name: "SeawallScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/seawall/SeawallScreen.vue"], resolve),
      },
      // 水库大屏
      {
        path: "/reservoirScreen",
        name: "ReservoirScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/reservoir/ReservoirScreen.vue",], resolve),
      },
      // 流域大屏
      {
        path: "/basinScreen",
        name: "BasinScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/basin/BasinScreen.vue"], resolve),
      },
      // 堤防大屏
      {
        path: "/dikeScreen",
        name: "DikeScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/dike/DikeScreen.vue"], resolve),
      },
      // 水闸大屏
      {
        path: "/sluiceScreen",
        name: "SluiceScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/sluice/SluiceScreen.vue"], resolve),
      },
      // 泵站大屏
      {
        path: "/pumpScreen",
        name: "PumpScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/pump/PumpScreen.vue"], resolve),
      },
      // 闸站大屏
      {
        path: "/gateScreen",
        name: "GateScreen",
        component: (resolve) => require(["../views/rscpDriveScreen/gate/GateScreen.vue"], resolve),
      },


      //拓展大屏-------------
      {
        path: "/reservoirScreenSimple",
        name: "ReservoirScreen",
        component: (resolve) => require(["../views/outsideScreen/reservoirSimple/ReservoirScreen.vue",], resolve),
      },
      {
        path: "/reservoirScreenQtj",
        name: "ReservoirScreen",
        component: (resolve) => require(["../views/outsideScreen/reservoirQtj/ReservoirScreen.vue",], resolve),
      },
      // 海塘白色
      {
        path: "/seawallSimpleV2",
        name: "seawallSimpleV2",
        component: (resolve) => require(["../views/outsideScreen/seawallSimpleV2/SeawallScreen.vue",], resolve),
      },
      // 水库白色 用于会商中心-水库运管（没有头部版本）
      {
        path: "/reservoirSimpleV2",
        name: "reservoirSimpleV2",
        component: (resolve) => require(["../views/outsideScreen/reservoirSimpleV2/ReservoirScreen.vue",], resolve),
      },
      {
        path: "/dikeSimpleV2",
        name: "/dikeSimpleV2",
        component: (resolve) => require(["../views/outsideScreen/dikeSimpleV2/DikeScreen.vue",], resolve),
      },
      {
        path: "/sluiceSimpleV2",
        name: "sluiceSimpleV2",
        component: (resolve) => require(["../views/outsideScreen/sluiceSimpleV2/SluiceScreen.vue",], resolve),
      },
      {
        path: "/pumpSimpleV2",
        name: "pumpSimpleV2",
        component: (resolve) => require(["../views/outsideScreen/pumpSimpleV2/PumpScreen.vue",], resolve),
      },
      {
        path: "/gateSimpleV2",
        name: "gateSimpleV2",
        component: (resolve) => require(["../views/outsideScreen/gateSimpleV2/GateScreen.vue",], resolve),
      },

      // 黑色风格-海塘
      {
        path: "/seawallBlack",
        name: "seawallBlack",
        component: (resolve) => require(["../views/outsideScreen/seawallBlack/SeawallScreen.vue",], resolve),
      },
      // 黑色风格-水库
      {
        path: "/reservoirBlack",
        name: "reservoirBlack",
        component: (resolve) => require(["../views/outsideScreen/reservoirBlack/ReservoirScreen.vue",], resolve),
      },
      // 黑色风格-堤防
      {
        path: "/dikeBlack",
        name: "dikeBlack",
        component: (resolve) => require(["../views/outsideScreen/dikeBlack/DikeScreen.vue",], resolve),
      },
      // 黑色风格-水闸
      {
        path: "/sluiceBlack",
        name: "sluiceBlack",
        component: (resolve) => require(["../views/outsideScreen/sluiceBlack/SluiceScreen.vue",], resolve),
      },
      // 黑色风格-泵站
      {
        path: "/pumpBlack",
        name: "pumpBlack",
        component: (resolve) => require(["../views/outsideScreen/pumpBlack/PumpScreen.vue",], resolve),
      },
      // 黑色风格-闸站
      {
        path: "/gateBlack",
        name: "gateBlack",
        component: (resolve) => require(["../views/outsideScreen/gateBlack/GateScreen.vue",], resolve),
      },


      //水工智管 第一次4个大屏 2021-12-16
      {
        path: "/sgzgMenu",
        name: "sgzgMenu",
        redirect: '/healthyOnline',
        component: (resolve) => require(["../views/sgzgScreen/SgzgMenu.vue",], resolve),
        children: [
          // 健康在线
          {
            path: "/healthyOnline",
            name: "HealthyOnlineScreen",
            component: (resolve) => require(["../views/sgzgScreen/healthyOnline/HealthyOnlineScreen.vue",], resolve),
          },
          // 蓄供分析
          {
            path: "/storageSupply",
            name: "StorageSupplyScreen",
            component: (resolve) => require(["../views/sgzgScreen/storageSupply/StorageSupplyScreen.vue",], resolve),
          },
          // 防潮研判
          {
            path: "/tideAnalysis",
            name: "TideAnalysisScreen",
            component: (resolve) => require(["../views/sgzgScreen/tideAnalysis/TideAnalysisScreen.vue",], resolve),
          },
          // 智慧运用
          {
            path: "/intelligenceUse",
            name: "IntelligenceUseScreen",
            component: (resolve) => require(["../views/sgzgScreen/intelligenceUse/IntelligenceUseScreen.vue",], resolve),
          }
        ]
      },

      //水工智管 第二次6个大屏 2021-12-23
      {
        path: "/sgzgDeive",
        name: "SgzgDeive",
        component: (resolve) => require(["../views/sgzgScreenNew/SgzgDeive.vue"], resolve),
      },
      {
        path: "/sgzgMenuNew",
        name: "SgzgMenuNew",
        redirect: '/zhekuHousehold',
        component: (resolve) => require(["../views/sgzgScreenNew/SgzgMenu.vue",], resolve),
        children: [
          // 浙库户籍
          {
            path: "/zhekuHousehold",
            name: "ZhekuHouseholdScreen",
            component: (resolve) => require(["../views/sgzgScreenNew/zhekuHousehold/ZhekuHouseholdScreen.vue",], resolve),
          },
          // 浙库健康
          {
            path: "/zhekuHealthy",
            name: "ZhekuHealthyScreen",
            component: (resolve) => require(["../views/sgzgScreenNew/zhekuHealthy/ZhekuHealthyScreen.vue",], resolve),
          },
          //水库蓄供
          {
            path: "/reservoirStorage",
            name: "ReservoirStorageScreen",
            component: (resolve) => require(["../views/sgzgScreenNew/reservoirStorage/ReservoirStorageScreen.vue",], resolve),
          },
          // 智慧控运
          {
            path: "/reservoirControl",
            name: "ReservoirControlScreen",
            component: (resolve) => require(["../views/sgzgScreenNew/reservoirControl/ReservoirControlScreen.vue",], resolve),
          },
          // 精准维养
          {
            path: "/repairProtect",
            name: "RepairProtectScreen",
            component: (resolve) => require(["../views/sgzgScreenNew/repairProtect/RepairProtectScreen.vue",], resolve),
          },
          // 浙库效益
          {
            path: "/reservoirBenefit",
            name: "ReservoirBenefitScreen",
            component: (resolve) => require(["../views/sgzgScreenNew/reservoirBenefit/ReservoirBenefitScreen.vue",], resolve),
          },
        ]
      },

      // 智管工程驾驶舱
      {
        path: "/skzgScreen",
        name: "skzgScreen",
        // component: (resolve) => require(["../views/sgzgOtherScreen/index/Index.vue"], resolve),
        component: (resolve) => require(["../views/zhMenu/SkzgScreenIndex.vue"], resolve),
      },
      // 水库大屏
      {
        path: "/zhMenu",
        name: "zhMenu",
        component: (resolve) => require(["../views/skzgScreen/SkzgScreenIndex.vue",], resolve),
        meta: {
          title: '智慧水库'
        }
      },
      // 堤防大屏
      {
        path: "/skzgDikeScreen",
        name: "skzgDikeScreen",
        component: (resolve) => require(["../views/sgzgOtherScreen/dike/DikeScreen.vue"], resolve),
      },
      // 水闸大屏
      {
        path: "/skzgSluiceScreen",
        name: "skzgSluiceScreen",
        component: (resolve) => require(["../views/sgzgOtherScreen/sluice/SluiceScreen.vue"], resolve),
      },
      // 泵站大屏
      {
        path: "/skzgPumpScreen",
        name: "skzgPumpScreen",
        component: (resolve) => require(["../views/sgzgOtherScreen/pump/PumpScreen.vue"], resolve),
      },
      // 闸站大屏
      {
        path: "/skzgGateScreen",
        name: "skzgGateScreen",
        component: (resolve) => require(["../views/sgzgOtherScreen/gate/GateScreen.vue"], resolve),
      },
      // 海塘大屏
      {
        path: "/skzgSeawallScreen",
        name: "skzgSeawallScreen",
        component: (resolve) => require(["../views/sgzgOtherScreen/seawall/SeawallScreen.vue"], resolve),
      },

      // 第四版智慧工程大屏
      // 智慧海塘大屏
      {
        path: "/zhSeawallScreen",
        name: "zhSeawallScreen",
        component: (resolve) => require(["../views/IntelligenceScreen/seawallScreen/SeawallScreenIndex.vue"], resolve),
        meta: {
          title: '智慧海塘'
        }
      },
      // 第五版智慧工程大屏
      // 新海塘大屏
      {
        path: "/whiteSeawall",
        name: "whiteSeawall",
        component: (resolve) => require(["../views/IntelligenceScreen/seawall/SeawallScreen.vue"], resolve),
        meta: {
          title: '海塘防潮'
        }
      },
      // 第六版海塘防潮大屏
      {
        path: "/zhSeawall",
        name: "zhSeawall",
        component: (resolve) => require(["../views/IntelligenceScreen/blueSeawall/SeawallScreen.vue"], resolve),
        meta: {
          title: '海塘防潮'
        }
      },
      // 水库安全风险管控智能模块
      {
        path: "/intelligentModule",
        name: "intelligentModule",
        component: (resolve) => require(["../views/intelligentModule/SkzgScreenIndex.vue",], resolve),
        meta: {
          title: '水库安全风险管控智能模块'
        }
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router;
