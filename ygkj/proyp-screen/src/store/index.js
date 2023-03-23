/*
 * @Author: wqn
 * @Date: 2022-11-23 15:48:25
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-15 13:48:45
 */
import Vue from 'vue'
import Vuex from 'vuex'
import { localData } from "utils/storage";
import map from "./modules/map.js"
import reservoir from "./modules/reservoir.js"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    selectModuleName: '', // 储存选中的功能模块的名称
    selectAdnm: localData("get", "userInfo")?.adnm || '浙江省', // 全局选择行政区划
    selectAdcd: localData("get", "userInfo")?.adcd || '330000000000', // 全局选择行政区划
  },
  mutations: {
    setModuleName(state, moduleName) {
      if (moduleName && moduleName !== state.selectModuleName) {
        state.selectModuleName = moduleName
      } else {
        state.selectModuleName = ''
      }
    },
    setSelectAdnm(state, data) {
      state.selectAdnm = data;
    },
    setSelectAdcd(state, data) {
      state.selectAdcd = data;
    },
  },
  actions: {

  },
  modules: { map, reservoir }
})
