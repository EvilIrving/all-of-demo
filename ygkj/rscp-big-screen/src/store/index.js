import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    seawallMapList: [], // 海塘大屏地图点位/右侧列表数据
    seawallSelectFlag: true, // 海塘大屏地图右侧选择防重
    reservoirSelectFlag: true, // 水库大屏地图右侧选择防重
    leftSelectAdcd: "", // 左侧选择 adcd
    leftSelectAdnm: "", // 左侧选择 adnm
    leftSelectPreventLevel: "", // 左侧选择 preventLevel
    leftSelectProjectScale: "", // 左侧选择 project_scale
    reservoirLeftAdcdData: [], // 水库左侧adcd
    reservoirLeftScaleData: [], // 水库左侧scale
    reservoirLeftKrData: [], // 水库左侧库容
    reservoirTabs: [], // 水库统计
    basinSelectFlag: true, // 流域大屏地图右侧选择防重
    leftSelectBasin: '', // 流域左侧选择 流域
    basinLeftAdcdData: '', // 流域左侧选择 adcd
    basinListData: [], // 流域列表
    projectLeftScale: '', //其他工程大屏左侧工程规模
    leftChartClick:'',
    rightSelectList1: [
      {
        name: '预测预警',
        num: 0
      },
      {
        name: '实时报警',
        num: 0
      },
    ],
    rightSelectList2: [
      {
        name: '海塘分布',
        num: 0
      },
      {
        name: '安澜建设',
        num: 0
      },
    ],
    sgzgMapType: '风险研判', // 水库智管-地图类型
    sgzgSelectAdcd: '', // 水库智管-上方行政区划筛选
    sgzgSelectBasin: '', // 水库智管-上方流域筛选
    colorCode: 'all', // 水库智管-颜色码
    htColorCode: 'all', // 海塘研判-颜色码
    skzgSwitchXsl: false, // 水库智管-蓄水量地图浮窗开关
    skzgSwitchLegend: true, // 水库智管-图例开关
    tasklistChange: 0,
    rain: '',// 推演 地图联动 区间
    ifUnder: '',// 推演 及 地图联动 以上以下
    waterStorageQuantity: '1,2,3',// 纳蓄 蓄水量 图例勾选
    waterStorageRate: '1,2,3',// 纳蓄 蓄水率 图例勾选
    numberSelected: '', // 数字选中状态
    tempHangZhouTotal: '', //临时计算杭州分布海塘的总数
    touBuTabsActive: 'first', // 头部切换的次序
    hideCity: false, // 默认展示市级悬浮窗
    choosedItem: '分布', // 被选中的次级标题 
    choosedItemBox: '', // 被选中的次级标题 安全鉴定框框
    planId: '', //风暴潮方案id
  },
  mutations: {
    // 海塘
    SET_RIGHT_LIST_ONE(state, list) {
      state.leftchartchoose = list;
    },
    SET_LEFT_CHART_CHOOSE(state, payload) {
      state.leftChartClick = payload;
    },
    SET_CHOOSED_ITEM(state, payload) {
      state.choosedItem = payload;
    },
    SET_CHOOSED_ITEM_BORDER(state, payload) {
      state.choosedItemBox = payload;
    },
    SET_HIDECITY(state, data) {
      state.hideCity = data;
    },
    SET_TOUBUTABSACTIVE(state, data) {
      state.touBuTabsActive = data;
    },
    SET_TEMP_HANGZSHOU(state, data) {
      state.tempHangZhouTotal = data;
    },
    SET_RIGHT_LIST_TWO(state, list) {
      state.rightSelectList2 = list;
    },
    // 海塘
    SET_SEAWALL_MAP_LIST(state, list) {
      state.seawallMapList = list;
    },
    SET_SEAWALL_RIGTH_SELECT_FLAG(state, data) {
      state.seawallSelectFlag = data;
    },
    SET_LEFT_SELECT_ADCD(state, data) {
      state.leftSelectAdcd = data;
    },
    SET_LEFT_SELECT_ADNM(state, data) {
      state.leftSelectAdnm = data;
    },
    SET_LEFT_SELECT_PREVENT_LEVEL(state, data) {
      state.leftSelectPreventLevel = data;
    },
    SET_LEFT_SELECT_PROJECT_SCALE(state, data) {
      state.leftSelectProjectScale = data;
    },

    // 水库
    SET_RESERVOIR_RIGTH_SELECT_FLAG(state, data) {
      state.reservoirSelectFlag = data;
    },
    SET_RESERVOIR_LEFT_ADCD_DATA(state, data) {
      state.reservoirLeftAdcdData = data;
    },
    SET_RESERVOIR_LEFT_SCALE_DATA(state, data) {
      state.reservoirLeftScaleData = data;
    },
    SET_RESERVOIR_LEFT_KR_DATA(state, data) {
      state.reservoirLeftKrData = data;
    },
    SET_RESERVOIR_TABS(state, data) {
      state.reservoirTabs = data;
    },

    //流域
    SET_BASIN_RIGTH_SELECT_FLAG(state, data) {
      state.basinSelectFlag = data;
    },
    SET_LEFT_SELECT_BASIN(state, data) {
      state.leftSelectBasin = data;
    },
    SET_BASIN_LEFT_ADCD_DATA(state, data) {
      state.basinLeftAdcdData = data;
    },
    SET_BASIN_LIST(state, data) {
      state.basinListData = data;
    },

    //其他大屏
    SET_PROJECR_LEFT_SCALE(state, data) {
      state.projectLeftScale = data;
    },

    //水库智管
    SET_SKZG_MAP_TYPE(state, data){
      state.sgzgMapType = data;
    },
    SET_SKZG_SELECT_ADCD(state, data){
      state.sgzgSelectAdcd = data;
    },
    SET_SKZG_SELECT_BASIN(state, data){
      state.sgzgSelectBasin = data;
    },
    SET_COLORCODE(state, payload) {
      state.colorCode = payload;
    },
    SET_HTCOLORCODE(state, payload) {
      state.htColorCode = payload;
    },
    SET_RAIN(state, payload) {
      state.rain = payload;
    },
    SET_IFUNDER(state, payload) {
      state.ifUnder = payload;
    },
    SET_SKZG_SWITCH_XSL(state, data){
      state.skzgSwitchXsl = data;
    },
    SET_SKZG_SWITCH_LEGEND(state, data){
      state.skzgSwitchLegend = data;
    },
    SET_TASK_LIST_CHANGE(state, data) {
      state.tasklistChange = data;
    },
    SET_WATER_STORAGE_QUANTITY(state, payload) {
      state.waterStorageQuantity = payload;
    },
    SET_NUMBER_SELECT(state, data){
      state.numberSelected = data;
    },
    SET_WATER_STORAGE_RATE(state, payload) {
      state.waterStorageRate = payload;
    },
    SET_PLAN_ID(state, data) {
      state.planId = data
    }
  },
  actions: {},
  modules: {},
});
