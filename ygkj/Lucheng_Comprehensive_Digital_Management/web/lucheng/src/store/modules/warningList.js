import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';

const dataMap = {
  暴雨: { unit: '个', icon: 'rainstorm' },
  河网: { unit: '个', icon: 'river' },
  水库: { unit: '座', icon: 'reservoir' },
  预报山洪: { unit: '个', icon: 'torrents' },
  监测山洪: { unit: '个', icon: 'torrents' },
  潮位: { unit: '个', icon: 'tidemark' }
};
const warningList = {
  state: {
    warningList: [
      { icon: 'rainstorm', title: '暴雨', number: '26', unit: '个' },
      { icon: 'river', title: '河网', number: '12', unit: '个' },
      { icon: 'reservoir', title: '水库', number: '12', unit: '座' },
      { icon: 'torrents', title: '山洪', number: '12', unit: '个' },
      { icon: 'tidemark', title: '潮位', number: '8', unit: '个' }
    ],
    warningListShow: true,
    warningListPoint: [],
    warningCount: 0,
    warningDoWarn: false
  },
  mutations: {
    SET_WARNING_LIST(state, { list, count }) {
      state.warningList = list;
      state.warningCount = count;
    },
    SET_WARNINGLIST_SHOW(state, status) {
      state.warningListShow = status;
    },
    SET_WARNING_POINT_LIST(state, list) {
      state.warningListPoint = list;
    },
    SET_WARNING_DO_WARN(state, list) {
      state.warningDoWarn = list;
    }
  },
  actions: {
    setWarningList({ commit }, list) {
      commit('SET_WARNING_LIST', { list });
    },
    showWarningList({ commit }) {
      commit('SET_WARNINGLIST_SHOW', true);
    },
    hideWarningList({ commit }) {
      commit('SET_WARNINGLIST_SHOW', false);
    },
    getWarningList({ commit }, params) {
      let opt = {};
      if (params) {
        opt = {
          endTime: params.time,
          drillAffairId: params.AffairId
        };
      }
      disasterPreventionApi.warnMapPoints(opt).then((res) => {
        let list = res.data.list.map((item) => ({
          icon: dataMap[item.type].icon,
          title: item.type,
          number: item.warnNum,
          unit: dataMap[item.type].unit
        }));
        let pointList = res.data.list.map((item) => ({ type: item.type, list: item.points }));
        commit('SET_WARNING_LIST', { list, count: res.data.totalWarn });
        commit('SET_WARNING_POINT_LIST', pointList);
        let doWarn = res.data.doWarn;
        commit('SET_WARNING_DO_WARN', doWarn);
        if (pointList.length) {
          commit('SET_WARNINGLIST_SHOW', true);
        }
      });
    }
  },
  getters: {
    currentWarningList: (state) => state.warningList,
    currentWarningListPoint: (state) => state.warningListPoint,
    currentWarningCount: (state) => state.warningCount,
    currentWarningDoWarn: (state) => state.warningDoWarn,
    warningListShow: (state) => state.warningListShow,
    getPointList: (state) => {
      return (type) => {
        return state.warningListPoint.find((item) => item.type == type).list;
      };
    }
  }
};
export default warningList;
