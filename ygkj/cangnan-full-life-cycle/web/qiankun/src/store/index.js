import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    systemType:"",
    menuName:"业务场景",
    fullLoading:false,
    dataType:"",//数据类型
    objectType:"",//数据对象
    manageBtnFlag:true,
    workplaceVisible:false,//工作台开关
    selectMenu:0,//选中菜单索引
    workName:"",
    workId:"",
    workFlag:false,
  },
  mutations: {
    SET_SYSTEM_TYPE(state, value){
      state.systemType = value;
    },
    SET_MENU_NAME(state, value){
      state.menuName = value;
    },
    SET_FULL_LOADING(state,value){
      state.fullLoading = value;
    },
    SET_DATA_TYPE(state,value){
      state.dataType = value;
    },
    SET_OBJECT_TYPE(state,value){
      state.objectType = value;
    },
    SET_MANAGE_BTN_FLAG(state,data){
      state.manageBtnFlag = data;
    },
    SET_WORKPLACE_VISIBLE(state,data){
      state.workplaceVisible = data;
    },
    SET_SELECT_MENU(state,data){
      state.selectMenu = data;
    },
    SET_WORKNAME(state,data){
      state.workName = data;
    },
    SET_WORK_ID(state,data){
      state.workId = data;
    },
    SET_WORK_FLAG(state,data){
      state.workFlag = data;
    }
  },
  actions: {
    SetSystemType({ commit }, type){
      commit("SET_SYSTEM_TYPE",type);
    },
    SetMenuName({ commit }, data){
      commit("SET_MENU_NAME",data);
    },
    SetFullLoading({ commit },data){
      commit("SET_FULL_LOADING",data);
    },
    SetDataType({ commit },data){
      commit("SET_DATA_TYPE",data);
    },
    SetObjectType({ commit },data){
      commit("SET_OBJECT_TYPE",data);
    },
    SetManageBtnFlag({ commit },data){
      commit("SET_MANAGE_BTN_FLAG",data);
    },
    SetWorkPlaceVisible({ commit },data){
      commit("SET_WORKPLACE_VISIBLE",data);
    },
    SetSelectMenu({ commit },data){
      commit("SET_SELECT_MENU",data);
    },
    SetWorkName({commit},data){
      commit("SET_WORKNAME",data);
    },
    SetWorkId({commit},data){
      commit("SET_WORK_ID",data);
    },
    SetWorkFlag({commit},data){
      commit("SET_WORK_FLAG",data);
    }
  },
  getters: {
    SystemType:(state) => state.systemType,
    MenuName:(state) => state.menuName,
    FullLoading:(state) => state.fullLoading,
    DataType:(state) => state.dataType,
    ObjectType:(state) => state.objectType,
    ManageBtnFlag:(state) => state.manageBtnFlag,
    WorkplaceVisible:(state) => state.workplaceVisible,
    SelectMenu:(state) => state.selectMenu,
    WorkName:(state) => state.workName,
    WorkId:(state) => state.workId,
    WorkFlag:(state) => state.workFlag,
  }
});