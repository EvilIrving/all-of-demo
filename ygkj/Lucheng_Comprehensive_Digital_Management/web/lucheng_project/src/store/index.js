import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import router from '../router/index'
import { routes } from '../router/index'
import { sysJobResTreeApi } from '@/api/systemManagement'
import { sessionData } from '../plugins/storage'
import { getAsyncRoutes } from '../router/asyncRoutes'
let projectInfo = sessionData('get', 'projectInfo') || {}
export default new Vuex.Store({
  state: {
    seawallDetail: '',
    overseeId: '',
    projectInfo: projectInfo,
    troubleDetail: {},
    routesList: [],
    allRoutesList: [],
    deptId: ''
  },
  mutations: {
    SET_SEAWALL_DETAIL(state, data) {
      state.seawallDetail = data;
    },
    SET_OVERSEEID(state, data) {
      state.overseeId = data;
    },
    SET_PROJECT_INFO(state, data) {
      sessionData('set', 'projectInfo', data)
      state.projectInfo = data;
    },
    SET_TROUBLE_DETAIL(state, data) {
      state.troubleDetail = data;
    },
    ADD_ROUTES(state, data) {
      state.routesList = data
      state.allRoutesList = routes.concat(data)
    },
    SET_DEPT_ID(state, data) {
      state.deptId = data
    }
  },
  getters: {
    routesList: state => state.routesList,
    allRoutesList: state => state.allRoutesList,
    deptId: state => state.deptId,
  },
  actions: {
    async jobRoutes({ commit }, jobId) {
      let accessedRoutes = []
      let res = await sysJobResTreeApi({
        id: jobId
      })
      if (res.code == 0) {
        if (res.data.wbTree.length) {
          accessedRoutes = getAsyncRoutes(res.data.wbTree)
          commit('ADD_ROUTES', accessedRoutes)
        } else {
          sessionData('clean', 'userInfo')
          router.push('/login')
        }
      }
      return accessedRoutes
    },
    setDeptId({commit}, data) {
      commit('SET_DEPT_ID', data)
    }
  },
  modules: {

  }
})
