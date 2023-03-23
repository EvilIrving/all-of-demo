export default [
    //水利综合一张图
    {
        path: "/hydraulicAmap",
        name: "HydraulicAmap",
        component: (resolve) => require(["@/views/hydraulic_amap/index.vue"], resolve),
        children: [{
            path: "/waterRainAnalysis",
            name: "WaterRainAnalysis",
            component: (resolve) => require(["@/views/dataScreen/floodJudgeNewest/waterRain_analysis/WaterRainAnalysis.vue"], resolve),
        },
        {
            path: "/stationSide",
            name: "StationSide",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/StationSide.vue"], resolve),
        },
        {
            path: "/ProgrammeSide",
            name: "ProgrammeSide",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/ProgrammeSide.vue"], resolve),
        },
        // 河湖
        {
            path: "/InformationOfLakes",
            name: "InformationOfLakes",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/InformationOfLakes.vue"], resolve),
        },
        {
            path: "/ClearFour",
            name: "ClearFour",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/ClearFour.vue"], resolve),
        },
        {
            path: "/rescueSide",
            name: "RescueSide",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/RescueSide.vue"], resolve),
        },
        {
            path: "/projectSide",
            name: "ProjectSide",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/ProjectSide.vue"], resolve),
        },
        {
            path: "/typhoonSearch",
            name: "TyphoonSearch",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/TyphoonSearch.vue"], resolve),
        },
        {
            path: "/typhoonSearchInfo",
            name: "TyphoonSerachInfo",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/TyphoonSearchInfo.vue"], resolve),
        },
        {
            path: "/WaterResourSide",
            name: "WaterResourSide",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/WaterResourSide.vue"], resolve),
        },
        {
            path: "/ProductionProjSide",
            name: "ProductionProjSide",
            component: (resolve) => require(["@/views/hydraulic_amap/components/side/ProductionProjSide.vue"], resolve),
        },
        ],
    },
];