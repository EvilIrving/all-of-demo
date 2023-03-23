import { disasterPreventionApi } from "@/api/waterDisasterPrevention/index.js";

const screen = {
  state: {
    bsNameObj: {
      waterDisaster: '水灾害防御',
      waterResource: '水资源保障',
      riverProtect: '河湖库保护',
      waterDevelop: '水发展规划',
      waterAffair: '水事务监管',
      waterGovernment: '水政务协同'
    },
    bsName: '海塘防潮评估',
    BigSceneName: '海塘防潮评估',
    selectList: [
      '短临预报',
      '山洪预警',
      '气象预警',
      '水库',
      '水闸',
      '河网',
      '潮位',
      '海塘',
      '病险工程',
      '风险隐患',
      '大中型水库',
      '实时降雨',
      '实时台风',
      '水情'
    ], //选择业务组件列表
    checkedArr: [], //已选择的业务组件名称数组
    checkedNoBxArr: [],
    cardHeight: 0,
    addCardDialog: false, //卡片选择弹框开关标记
    sceneList: [], //大屏下场景列表
    activeScene: {
      name: '',
      id: '',
      editable: 0
    }, //当前选中场景
    saveAsDialog: false,
    highCardHeight: 0,
    sceneComponentList: [], //场景组件信息
    DetailDialogParam: {
      codeTpye: '',
      mediumSized: '',
      analyseDialogShow: false
    },
    forecastPointFlag: false, //短临预报 地图点击标记
    statisticFlag: true, //c位开关标记
    activeCount: '',
    riverwayShows: false,
    mountainCardTab: '预报', // 选中的山洪tab
    weatherWarnNumber: {}, //气象预警数量
    isCollapse: false,
    isBigScreen: true,
    componentLoading: false, //场景切换loading
    tempZIndex: 1000, //当前卡片的最高zIndex
    typhoonPointData: {}, //台风选中点位数据
    teamDispatchList: [], //抢险支持-队伍调度-路线详情
    warehouseDispatchList: [], //抢险支持-仓库调度-路线详情
    goodsList: [], // 抢险支持-仓库调度-调度物资
    teamInfo: {}, // 抢险支持-仓库调度-选中队伍
    warehouseInfo: {}, // 抢险支持-仓库调度-选中仓库
    dispatchCarousel: false, // 抢险支持-抢险结果轮播tab是否显示
    spotsFlag: false, // 抢险支持-是否在选点
    spotsFlagType: 0, // 抢险支持-是否在选点
    isAction: false, // 抢险支持-立即抢险选中状态
    floodRiskTotal: {}, // 洪水风险影像统计
    precipitationCount: {}, // 降水量公报
    waterResourceCount: {}, // 水资源公报
    waterSupplyCount: {}, // 供水量公报
    waterUseCount: {}, // 用水量公报
    reserStorageCount: {}, // 水库蓄水量公报
    WaterResearchShow: false, //水研究专题
    trendAnalysisShow: false, //趋势分析
    // riverwayShow: false, //河道
    emergencyCount: null,
    SetTitleTypeData: '报警',
    SetAlarmDialogShow: false, //报警列表
    waterSecurityCount: {}, // 水源保障
    DistributionRiverCount: {}, //河道分析
    waterEfficiencyCount: {}, // 用水效率
    threeRedLineCount: {}, // 三条红线
    waterSavingActionSituationCount: {}, // 节水行动态势
    prophaseInfuencedRainfallCount: {}, // 前期影响雨量
    // 水政务协同
    governmentTravelCount: {}, // 出差管理
    dikeCount: {}, //堤防公报
    conferenceTrainingCount: {}, //会议培训
    citytaxiCount: {}, //市内出租车
    otherReimbursementCount: {}, //其他报销
    officialVehicleManagementCount: {}, //公务用车
    pumpStationCount: {}, //泵站公报
    MountainPondCount: {}, //山塘公报
    governmentOfficialCount: {}, // 公务接待
    governmentContractCount: {}, // 合同管理
    governmentAssetsCount: {}, // 资产管理
    governmentInsideCount: {}, // 政务内部职能管理
    radarRainList: [], //雷达降雨数组
    rangeBar: false,
    timelineList: [],
    simple: false, // 是否显示极简模式
    simpleData: {}, // 列表和点位数据
    simpleRightType: false, // 右边显示的类型
    simpleItem: '', // 
    simpleTimes: '', // 时间
    simpleRangTime: '', // 时间范围
    simplePotenData: '',
    sluiceDistributionMap: false, // 水闸分布图显示与否
    typhoonVal: '',//所选的 风暴潮ID
    previewTime: [],//预演时间段
    previewTime: [],//预演时间段
    seawallActive: {},
    doSceneSaveData: '',
    aligning: false,
  },
  mutations: {
    SET_BIGSCENENAME(state, data) {
      state.BigSceneName = data
    },
    SET_SEAWALLACTIVE(state, data) {
      state.seawallActive = data
    },
    SET_PREVIEWTIME(state, data) {
      state.previewTime = data
    },
    SET_TYPHOONVAL(state, data) {
      state.typhoonVal = data
    },
    SET_DISTRIBUTION_MAP(state, data) {
      state.sluiceDistributionMap = data
    },
    SET_SIMPLE(state, data) {
      state.simple = data
    },
    SET_SELECT_LIST(state, data) {
      state.selectList = data;
    },
    SET_CHECKED_ARR(state, data) {
      const ignoreList = ['病险工程', '实时台风'];
      state.checkedArr = data;
      state.checkedNoBxArr = data.filter((item) => !ignoreList.includes(item));
    },
    SET_CHECKED_NOBX_ARR(state, data) {
      state.checkedNoBxArr = data;
    },
    SET_CARD_HEIGHT(state, data) {
      state.cardHeight = data;
    },
    SET_HIGH_HEIGHT(state, data) {
      state.highCardHeight = data;
    },
    SET_FORECAST_POINT_FLAG(state, data) {
      state.forecastPointFlag = data;
    },
    SET_ADDCARD_DIALOG(state, data) {
      state.addCardDialog = data;
    },
    SET_SCENE_LIST(state, data) {
      state.sceneList = data;
    },
    SET_ACTIVE_SCENE(state, data) {
      state.activeScene = data;
    },
    SET_SAVEAS_SCENE(state, data) {
      state.saveAsDialog = data;
    },
    SET_SCENE_COMPONENT_LIST(state, data) {
      state.sceneComponentList = data;
    },
    SET_DETAILDIALOG_PARAM(state, data) {
      state.DetailDialogParam.codeTpye = data.codeTpye;
      state.DetailDialogParam.mediumSized = data.mediumSized;
      state.DetailDialogParam.analyseDialogShow = data.analyseDialogShow;
    },
    SET_STATISTIC_FLAG(state, data) {
      state.statisticFlag = data;
    },
    SET_ACTIVE_COUNT(state, data) {
      state.activeCount = data;
    },
    SET_MOUNTAIN_CARD_TAB(state, data) {
      state.mountainCardTab = data;
    },
    SET_WEATHER_WARN_NUMBER(state, data) {
      state.weatherWarnNumber = data;
    },
    SET_COLLAPSE(state, data) {
      state.isCollapse = data;
    },
    SET_BIGSCREEN(state, data) {
      state.isBigScreen = data;
    },
    SET_COMPONENT_LOADING(state, data) {
      state.componentLoading = data;
    },
    SET_TEMP_ZINDEX(state) {
      let idx = state.tempZIndex + 1;
      state.tempZIndex = idx;
    },
    TEAM_DISPATCH_LIST(state, list) {
      state.teamDispatchList = list;
    },
    WAREHOUSE_DISPATCH_LIST(state, list) {
      state.warehouseDispatchList = list;
    },
    SET_DISPATCH_CAROUSEL(stat, data) {
      stat.dispatchCarousel = data;
    },
    SET_TYPHOON_POINT_DATA(state, data) {
      state.typhoonPointData = data;
    },
    SET_GOODS_LIST(stat, data) {
      stat.goodsList = data;
    },
    SET_TEAM_INFO(state, data) {
      state.teamInfo = data;
    },
    SET_WAREHOUSE_INFO(state, data) {
      state.warehouseInfo = data;
    },
    SET_SPOST_FLAG(state, data) {
      state.spotsFlag = data;
    },
    SET_SPOST_FLAG_TYPE(state, data) {
      state.spotsFlagType = data;
    },
    SET_IS_ACTION(state, data) {
      state.isAction = data;
    },
    SET_FLOOD_DISK_TOTAL(state, data) {
      state.floodRiskTotal = data;
    },
    SET_BSNAME(state, data) {
      state.bsName = data;
    },
    SET_PRECIPITATION_COUNT(state, data) {
      state.precipitationCount = data;
    },
    SET_WATER_RESCOURCE_COUNT(state, data) {
      state.waterResourceCount = data;
    },
    SET_WATER_SUPPLY_COUNT(state, data) {
      state.waterSupplyCount = data;
    },
    SET_WATER_USE_COUNT(state, data) {
      state.waterUseCount = data;
    },
    SET_RESER_STORAGE_COUNT(state, data) {
      state.reserStorageCount = data;
    },
    SET_WATER_STUDY_DETAILS_SHOW(state, data) {
      // console.log(203,state, data);
      state.WaterResearchShow = data;
    },
    SET_TREND_ANALYSIS_SHOW(state, data) {
      state.trendAnalysisShow = data;
    },
    SET_RIVERWAY_SHOW(state, data) {
      // console.log(1020,state, data);
      state.riverwayShows = data;
    },
    SET_EMERGENCY_COUNT(state, data) {
      state.emergencyCount = data;
    },
    SET_ALARM_DIALOG(state, data) {
      state.SetAlarmDialogShow = data;
    },
    SET_TITLE_TYPE(state, data) {
      state.SetTitleTypeData = data;
    },
    SET_WATER_SECURITY_COUNT(state, data) {
      state.waterSecurityCount = data;
    },
    SET_DISTRIBUTION_RIVER_COUNT(state, data) {
      state.DistributionRiverCount = data;
    },
    SET_WATER_EFFICIENCY_COUNT(state, data) {
      state.waterEfficiencyCount = data;
    },
    SET_THREE_RED_LINE_COUNT(state, data) {
      state.threeRedLineCount = data;
    },
    SET_WATER_SAVING_ACTION_SITUATION_COUNT(state, data) {
      state.waterSavingActionSituationCount = data;
    },
    SET_PROPHASE_INFLUENCED_RAINFALL_COUNT(state, data) {
      state.prophaseInfuencedRainfallCount = data;
    },
    // 水政务协同
    SET_GOVERNMENTTRAVEL_COUNT(state, data) {
      state.governmentTravelCount = data;
    },
    SET_GOVERNMENTOFFICIAL_COUNT(state, data) {
      state.governmentOfficialCount = data;
    },
    SET_GOVERNMENTCONTRACT_COUNT(state, data) {
      state.governmentContractCount = data;
    },
    SET_GOVERNMENTASSETS_COUNT(state, data) {
      state.governmentAssetsCount = data;
    },
    SET_GOVERNMENTINSIDE_COUNT(state, data) {
      state.governmentInsideCount = data;
    },
    // 堤防
    SET_DIKE_COUNT(state, data) {
      state.dikeCount = data;
    },
    // 市内出租车
    SET_CITY_TAXI_COUNT(state, data) {
      state.citytaxiCount = data;
    },
    // 会议培训
    SET_CONFERENCE_TRAINING_COUNT(state, data) {
      state.conferenceTrainingCount = data;
    },
    // 其他报销
    SET_OTHER_REIMBURSEMENT_COUNT(state, data) {
      state.otherReimbursementCount = data;
    },
    // 公务用车
    SET_OFFICIAL_VEHICLE_MANAGEMENT_COUNT(state, data) {
      state.officialVehicleManagementCount = data;
    },
    // 泵站
    SET_PUMPSTATION_COUNT(state, data) {
      state.pumpStationCount = data;
    },
    // 山塘
    SET_MOUNTAINPOND_COUNT(state, data) {
      state.MountainPondCount = data;
    },
    SET_RADAR_RAIN_LIST(state, data) {
      state.radarRainList = data;
    },
    SET_RANGE_BAR(state, data) {
      state.rangeBar = data;
    },
    SET_TIMELINE_LIST(state, data) {
      state.timelineList = data;
    },
    SET_SIMPLE_DATA(state, data) {
      state.simpleData = data
    },
    SET_SIMPLE_RIGHT_TYPE(state, data) {
      state.simpleRightType = data
    },
    SET_SIMPLE_ITEM(state, data) {
      state.simpleItem = data
    },
    SET_SIMPLE_TIMES(state, data) {
      state.simpleTimes = data
    },
    SET_SIMPLE_RANG_TIME(state, data) {
      state.simpleRangTime = data
    },
    SET_SIMPLE_POTEN_DATA(state, data) {
      state.simplePotenData = data
    },
    SET_DO_SCENE_SAVE(state, data) {
      state.doSceneSaveData = data
    },
    SET_ALIGNING(state, data) {
      state.aligning = data
    }
  },
  actions: {
    setBigSceneName({ commit }, data) {
      commit('SET_BIGSCENENAME', data);
    },
    setSeawallActive({ commit }, data) {
      commit('SET_SEAWALLACTIVE', data);
    },
    setPreviewTime({ commit }, data) {
      commit('SET_PREVIEWTIME', data);
    },
    setTyphoonVal({ commit }, data) {
      commit('SET_TYPHOONVAL', data);
    },
    setDistributionMap({ commit }, data) {
      commit('SET_DISTRIBUTION_MAP', data);
    },
    setSimple({ commit }, data) {
      commit('SET_SIMPLE', data);
    },
    SetRiverway({ commit }, obj) {
      commit('SET_RIVERWAY_SHOW', obj);
    },
    SetTrendAnalysis({ commit }, obj) {
      commit('SET_TREND_ANALYSIS_SHOW', obj);
    },
    titleType({ commit }, obj) {
      commit('SET_TITLE_TYPE', obj);
    },
    SetAlarmDialog({ commit }, obj) {
      commit('SET_ALARM_DIALOG', obj);
    },
    SetWaterResearchShow({ commit }, obj) {
      commit('SET_WATER_STUDY_DETAILS_SHOW', obj);
    },
    SetSelectList({ commit }, obj) {
      commit('SET_SELECT_LIST', obj);
    },
    SetCheckedArr({ commit }, data) {
      commit('SET_CHECKED_ARR', data);
    },
    SetCardHeight({ commit }, data) {
      commit('SET_CARD_HEIGHT', data);
    },
    SetHighCardHeight({ commit }, data) {
      commit('SET_HIGH_HEIGHT', data);
    },
    SetAddcardDialog({ commit }, data) {
      commit('SET_ADDCARD_DIALOG', data);
    },
    SetForecastPointFlag({ commit }, data) {
      commit('SET_FORECAST_POINT_FLAG', data);
    },
    SetDoSceneSave({ commit }, data) {
      commit('SET_DO_SCENE_SAVE', data);
    },
    //查询大屏场景列表
    GetSceneList({ commit }, data) {
      if (!data.bsName) {
        data.bsName = '海塘防潮评估'
      }
      return new Promise((resolve) => {
        disasterPreventionApi.listScene().then((res) => {
          if (res.code == 0) {
            commit('SET_SCENE_LIST', res.data);
            // let defaultScene = {
            // 	name:res.data[1].sceneName,
            // 	id:res.data[1].id,
            // };
            // res.data.forEach(item=>{
            // 	if(item.defaulted == 1){
            // 		defaultScene = {
            // 			name: item.sceneName,
            // 			id: item.id
            // 		}
            // 	}
            // })
            resolve(res.data);
          }
        });
      });
    },
    //设置选中场景
    SetActiveScene({ commit }, data) {
      commit('SET_ACTIVE_SCENE', data);
    },
    SetSaveAsDialog({ commit }, data) {
      commit('SET_SAVEAS_SCENE', data);
    },
    //删除自定义场景
    deleteScene({ commit, state }, id) {
      return new Promise((resolve) => {
        disasterPreventionApi.deleteScene(id).then((res) => {
          if (res.code == 0) {
            let tempArr = JSON.parse(JSON.stringify(state.sceneList));
            let idx = tempArr.findIndex((item) => {
              return item.id == id;
            });
            tempArr.splice(idx, 1);
            commit('SET_SCENE_LIST', tempArr);
            resolve(res);
          }
        });
      });
    },
    //获取当前场景的组件信息
    GetSceneComponentInfo({ commit, rootGetters, getters }, data) {
      // commit('SET_COMPONENT_LOADING', true);
      return new Promise((resolve) => {
        disasterPreventionApi.listFullBigScreen(data).then((res) => {
          // commit('SET_COMPONENT_LOADING', false);
          if (res.code == 0) {
            res.data.map((item) => {
              item.unitSceneProperty = JSON.parse(item.unitSceneProperty);
            });
            commit('SET_SCENE_COMPONENT_LIST', res.data);
            let tempArr = [];
            res.data.forEach((element) => {
              tempArr.push(element.unitName);
            });
            // if (getters.BsName == '灾害防御调度') {
            //   tempArr.unshift('报警');
            // }
            resolve(tempArr);
            //设置选中卡片组件
            commit('SET_CHECKED_ARR', tempArr);
          }
        });
      });
    },
    //设置详情弹框参数
    SetDetailDialogParam({ commit }, data) {
      commit('SET_DETAILDIALOG_PARAM', data);
    },
    // 设置显示的统计卡片
    SetActiveCount({ commit }, data) {
      commit('SET_ACTIVE_COUNT', data);
    },
    // 设置山洪预警选中tab
    SetMountainCardTab({ commit }, data) {
      commit('SET_MOUNTAIN_CARD_TAB', data);
    },
    SetStatisticFlag({ commit }, data) {
      commit('SET_STATISTIC_FLAG', data);
    },
    SetWeatherWarnNumber({ commit }, data) {
      commit('SET_WEATHER_WARN_NUMBER', data);
    },
    SetCollapse({ commit }, data) {
      commit('SET_COLLAPSE', data);
    },
    SetBigScreen({ commit }, data) {
      commit('SET_BIGSCREEN', data);
    },
    SetComponentLoading({ commit }, data) {
      commit('SET_COMPONENT_LOADING', data);
    },
    SetTeamDispatchList({ commit }, data) {
      commit('TEAM_DISPATCH_LIST', data);
    },
    SetWarehouseDispatchList({ commit }, data) {
      commit('WAREHOUSE_DISPATCH_LIST', data);
    },
    SetDispatchCarousel({ commit }, data) {
      commit('SET_DISPATCH_CAROUSEL', data);
    },
    SetTyphoonPointData({ commit }, data) {
      commit('SET_TYPHOON_POINT_DATA', data);
    },
    PushCheckedNobxArr({ commit }, data) {
      commit('SET_CHECKED_NOBX_ARR', data);
    },
    SetGoodsList({ commit }, data) {
      commit('SET_GOODS_LIST', data);
    },
    SetTeamInfo({ commit }, data) {
      commit('SET_TEAM_INFO', data);
    },
    SetWarehouseInfo({ commit }, data) {
      commit('SET_WAREHOUSE_INFO', data);
    },
    SetSpotsFlag({ commit }, data) {
      commit('SET_SPOST_FLAG', data);
    },
    SetSpotsFlagType({ commit }, data) {
      commit('SET_SPOST_FLAG_TYPE', data);
    },
    SetIsAction({ commit }, data) {
      commit('SET_IS_ACTION', data);
    },
    SetFloodRiskTotal({ commit }, data) {
      commit('SET_FLOOD_DISK_TOTAL', data);
    },
    SetTempZindex({ commit }, data) {
      commit('SET_TEMP_ZINDEX');
    },
    SetBsName({ commit }, data) {
      commit('SET_BSNAME', data);
    },
    SetPrecipitationCount({ commit }, data) {
      commit('SET_PRECIPITATION_COUNT', data);
    },
    SetWaterResourceCount({ commit }, data) {
      commit('SET_WATER_RESCOURCE_COUNT', data);
    },
    SetWaterSupplyCount({ commit }, data) {
      commit('SET_WATER_SUPPLY_COUNT', data);
    },
    SetWaterUseCount({ commit }, data) {
      commit('SET_WATER_USE_COUNT', data);
    },
    SetReserStorageCount({ commit }, data) {
      commit('SET_RESER_STORAGE_COUNT', data);
    },
    SetEmergencyCount({ commit }, data) {
      commit('SET_EMERGENCY_COUNT', data);
    },
    SetWaterSecurityCount({ commit }, data) {
      commit('SET_WATER_SECURITY_COUNT', data);
    },
    SetDistributionRiverCount({ commit }, data) {
      commit('SET_DISTRIBUTION_RIVER_COUNT', data);
    },
    SetWaterEfficiencyCount({ commit }, data) {
      commit('SET_WATER_EFFICIENCY_COUNT', data);
    },
    SetThreeRedLineCount({ commit }, data) {
      commit('SET_THREE_RED_LINE_COUNT', data);
    },
    SetWaterSavingActionSituationCount({ commit }, data) {
      commit('SET_WATER_SAVING_ACTION_SITUATION_COUNT', data);
    },
    SetProphaseInfluencedRainfallCount({ commit }, data) {
      commit('SET_PROPHASE_INFLUENCED_RAINFALL_COUNT', data);
    },
    SetGovernmentTravelCount({ commit }, data) {
      commit('SET_GOVERNMENTTRAVEL_COUNT', data);
    },
    SetDikeCount({ commit }, data) {
      commit('SET_DIKE_COUNT', data);
    },
    SetconferenceTrainingCount({ commit }, data) {
      commit('SET_CONFERENCE_TRAINING_COUNT', data);
    },
    SetotherReimbursementCount({ commit }, data) {
      commit('SET_OTHER_REIMBURSEMENT_COUNT', data);
    },
    SetofficialVehicleManagementCount({ commit }, data) {
      commit('SET_OFFICIAL_VEHICLE_MANAGEMENT_COUNT', data);
    },
    SetcitytaxiCount({ commit }, data) {
      commit('SET_CITY_TAXI_COUNT', data);
    },
    SetPumpStationCount({ commit }, data) {
      commit('SET_PUMPSTATION_COUNT', data);
    },
    SetMountainPondCount({ commit }, data) {
      commit('SET_MOUNTAINPOND_COUNT', data);
    },
    SetGovernmentContractCount({ commit }, data) {
      commit('SET_GOVERNMENTCONTRACT_COUNT', data);
    },
    SetGovernmentOfficialCount({ commit }, data) {
      commit('SET_GOVERNMENTOFFICIAL_COUNT', data);
    },
    SetGovernmentAssetsCount({ commit }, data) {
      commit('SET_GOVERNMENTASSETS_COUNT', data);
    },
    SetGovernmentInsideCount({ commit }, data) {
      commit('SET_GOVERNMENTINSIDE_COUNT', data);
    },
    SetRadarRainList({ commit }, data) {
      commit('SET_RADAR_RAIN_LIST', data);
    },
    SetRangeBar({ commit }, data) {
      commit('SET_RANGE_BAR', data);
    },
    SetTimelineList({ commit }, data) {
      commit('SET_TIMELINE_LIST', data);
    },
    setSimpleData({ commit }, data) {
      commit('SET_SIMPLE_DATA', data);
    },
    setSimpleRightType({ commit }, data) {
      commit('SET_SIMPLE_RIGHT_TYPE', data);
    },
    setSimpleItem({ commit }, data) {
      commit('SET_SIMPLE_ITEM', data);
    },
    setSimpleTimes({ commit }, data) {
      commit('SET_SIMPLE_TIMES', data);
    },
    setSimpleRangTime({ commit }, data) {
      commit('SET_SIMPLE_RANG_TIME', data);
    },
    setSimplePotenData({ commit }, data) {
      commit('SET_SIMPLE_POTEN_DATA', data);
    },
    SetAligning({ commit }, data) {
      commit('SET_ALIGNING', data);
    }
  },
  getters: {
    BigSceneName: (state) => state.BigSceneName,
    Simple: (state) => state.simple,
    SeawallActive: (state) => state.seawallActive,
    PreviewTime: (state) => state.previewTime,
    TyphoonVal: (state) => state.typhoonVal,
    sluiceDistributionMap: (state) => state.sluiceDistributionMap,
    simpleData: (state) => state.simpleData,
    simpleRightType: (state) => state.simpleRightType,
    simpleItem: (state) => state.simpleItem,
    simpleTimes: (state) => state.simpleTimes,
    simpleRangTime: (state) => state.simpleRangTime,
    simplePotenData: (state) => state.simplePotenData,
    // riverwayShow: state => state.riverwayShows,
    SelectList: (state) => state.selectList,
    CheckedArr: (state) => state.checkedArr,
    CheckedNoBxArr: (state) => state.checkedNoBxArr,
    CardHeight: (state) => state.cardHeight,
    HighCardHeight: (state) => state.highCardHeight,
    AddCardDialog: (state) => state.addCardDialog,
    SceneList: (state) => state.sceneList,
    ActiveScene: (state) => state.activeScene,
    SaveAsDialog: (state) => state.saveAsDialog,
    SceneComponentList: (state) => state.sceneComponentList,
    DetailDialogParam: (state) => state.DetailDialogParam,
    StatisticFlag: (state) => state.statisticFlag,
    ActiveCount: (state) => state.activeCount,
    MountainCardTab: (state) => state.mountainCardTab,
    WeatherWarnNumber: (state) => state.weatherWarnNumber,
    IsCollapse: (state) => state.isCollapse,
    ForecastPointFlag: (state) => state.forecastPointFlag,
    isBigScreen: (state) => state.isBigScreen,
    ComponentLoading: (state) => state.componentLoading,
    TempZIndex: (state) => state.tempZIndex,
    TeamDispatchList: (state) => state.teamDispatchList,
    WarehouseDispatchList: (state) => state.warehouseDispatchList,
    DispatchCarousel: (state) => state.dispatchCarousel,
    TyphoonPointData: (state) => state.typhoonPointData,
    GoodsList: (state) => state.goodsList,
    TeamInfo: (state) => state.teamInfo,
    WarehouseInfo: (state) => state.warehouseInfo,
    SpotsFlag: (state) => state.spotsFlag,
    SpotsFlagType: (state) => state.spotsFlagType,
    IsAction: (state) => state.isAction,
    FloodRiskTotal: (state) => state.floodRiskTotal,
    BsName: (state) => state.bsName,
    PrecipitationCount: (state) => state.precipitationCount,
    WaterResourceCount: (state) => state.waterResourceCount,
    WaterSupplyCount: (state) => state.waterSupplyCount,
    WaterUseCount: (state) => state.waterUseCount,
    ReserStorageCount: (state) => state.reserStorageCount,
    BsNameObj: (state) => state.bsNameObj,
    riverwayShow: (state) => state.riverwayShows, //河道
    WaterStudyDetailsShow: (state) => state.WaterResearchShow, //水研究专题
    trendAnalysisShow: (state) => state.trendAnalysisShow, //趋势分析
    SetTitleTypeData: (state) => state.SetTitleTypeData, //报警列表
    SetAlarmDialogShow: (state) => state.SetAlarmDialogShow, //报警列表
    EmergencyCount: (state) => state.emergencyCount,
    DistributionRiverCount: (state) => state.DistributionRiverCount, // 河流分布
    waterSecurityCount: (state) => state.waterSecurityCount, // 水源保障
    waterEfficiencyCount: (state) => state.waterEfficiencyCount, // 用水效率
    threeRedLineCount: (state) => state.threeRedLineCount, // 三条红线
    waterSavingActionSituationCount: (state) => state.waterSavingActionSituationCount, // 节水行动态势
    prophaseInfuencedRainfallCount: (state) => state.prophaseInfuencedRainfallCount, // 前期影响雨量
    // 水政务协同
    governmentTravelCount: (state) => state.governmentTravelCount, // 出差
    dikeCount: (state) => state.dikeCount, // 堤防
    citytaxiCount: (state) => state.citytaxiCount, // 市内出租车
    conferenceTrainingCount: (state) => state.conferenceTrainingCount, //会议培训
    otherReimbursementCount: (state) => state.otherReimbursementCount, //其他报销
    officialVehicleManagementCount: (state) => state.officialVehicleManagementCount, //公务用车
    pumpStationCount: (state) => state.pumpStationCount, // 泵站
    MountainPondCount: (state) => state.MountainPondCount, // 山塘
    governmentOfficialCount: (state) => state.governmentOfficialCount, // 公务接待
    governmentContractCount: (state) => state.governmentContractCount, // 合同管理
    governmentAssetsCount: (state) => state.governmentAssetsCount, // 资产管理
    governmentInsideCount: (state) => state.governmentInsideCount, // 政务内部职能管理
    RadarRainList: (state) => state.radarRainList, //雷达降雨数组
    RangeBar: (state) => state.rangeBar, //大屏点位轴控制标记
    TimelineList: (state) => state.timelineList, //大屏点位轴信息
    DoSceneSaveData: (state) => state.doSceneSaveData,
    Aligning: (state) => state.aligning,
  }
};
export default screen;
