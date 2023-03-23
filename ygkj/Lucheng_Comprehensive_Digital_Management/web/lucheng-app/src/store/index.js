import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);
let siteInfo = sessionStorage.getItem('siteInfo')
export default new Vuex.Store({
  state: {
    projectDetail: {},
    engineeringInfo: {},
    seawallCode: "",
    projectCode: "",
    filesSrc: sessionStorage.getItem('files_src') || '',
    siteInfo: siteInfo ? JSON.parse(siteInfo) : {},
    addRouteType: sessionStorage.getItem('addRoutes') || '',
    addRoutesNum: 0,
    viewType: sessionStorage.getItem('viewType') || '',
  },
  mutations: {
    SET_PROJECTDETAIL(state, paylod) {
      state.projectDetail = paylod
    },
    SET_SEAWALLLCODE(state, paylod) {
      state.seawallCode = paylod
    },
    SET_ENFINEERINGINFO(state, paylod) {
      state.engineeringInfo = paylod
    },
    SET_PROJECTCODE(state, paylod) {
      state.projectCode = paylod
    },
    SET_SITE_INFO(state, data) {
      state.siteInfo = data
    },
    SET_FILES_SRC(state, data) {
      state.filesSrc = data
    },
    SET_ADD_ROUTES_TYPE(state, data) {
      state.addRouteType = data
    },
    SET_ADD_ROUTES_NUM(state, data) {
      state.addRoutesNum = data
    },
    SET_VIEW_TYPE(state, data) {
      state.viewType = data
    },
  },
  actions: {
    setViewType({ commit }, data) {
      sessionStorage.setItem('viewType', data)
      commit('SET_VIEW_TYPE', data)
    },
    setSiteInfo({ commit }, data) {
      sessionStorage.setItem('siteInfo', JSON.stringify(data))
      commit('SET_SITE_INFO', data)
    },
    setFilesSrc({ commit }, data) {
      sessionStorage.setItem('files_src', data)
      commit('SET_FILES_SRC', data)
    },
    addRoutesType({ commit }, data) {
      commit('SET_ADD_ROUTES_TYPE', data)
      sessionStorage.setItem('addRoutes', data)
    },
    addRoutesNum({ commit }, data) {
      commit('SET_ADD_ROUTES_NUM', data)
    }
  },
  getters: {
    siteInfo: state => state.siteInfo,
    filesSrc: state => state.filesSrc,
    addRouteType: state => state.addRouteType,
    addRoutesNum: state => state.addRoutesNum,
    viewType: state => state.viewType,
  },
  modules: {},
});
