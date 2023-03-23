
const conceptualization = {
	state: {
    checkInfo: [], //概化图显示信息
    legendList: [], //图例
    infoDetail: {}, //概化图数据
    sluiceInfoList: [], //水闸分析右侧数据
    sluiceActiveArr: [], //水闸分析勾选项
	},
	mutations: {
		SET_CHECK_INFO(state, data) {
      state.checkInfo = data;
    },
    SET_LEGEND_LIST(state, data) {
      state.legendList = data;
    },
    SET_INFO_DETAIL(state, data) {
      state.infoDetail = data;
    },
    SET_SLUICE_INFO_LIST(state, data) {
      state.sluiceInfoList = data;
    },
    SET_SLUICE_ACTIVE_ARR(state, data) {
      state.sluiceActiveArr = data;
    }
	},
	actions: {
					
	},
	getters: {
		checkInfo: (state) => state.checkInfo,
		legendList: (state) => state.legendList,
		infoDetail: (state) => state.infoDetail,
		sluiceInfoList: (state) => state.sluiceInfoList,
		sluiceActiveArr: (state) => state.sluiceActiveArr,
	},
};
export default conceptualization;
