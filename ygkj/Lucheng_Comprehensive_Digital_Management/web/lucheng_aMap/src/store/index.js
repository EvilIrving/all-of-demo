import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

const modulesFiles = require.context("./modules", true, /\.js$/);
/** @desc 自动引入modules文件夹下的*.js文件 */
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
    const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, "$1");
    const value = modulesFiles(modulePath);
    modules[moduleName] = value.default;
    return modules;
}, {});

export default new Vuex.Store({
    state: {
        rightCollapse: true, //右侧伸缩开关标记
        leftCollapse: true,
        particularsVal: {},
        typhoonShowVal: false,
        mapChangeModule: "map",
        typhoonTimeData: [],
        typhoonData: {},
        analyseData: '',
        checkedShow: ''
    },
    mutations: {
        checked(state, value) {
            state.checkedShow = value;
        },
        analyse(state, value) {
            state.analyseData = value;
        },
        typhoonDataList(state, value) {
            state.typhoonData = value;
        },
        typhoonTime(state, value) {
            state.typhoonTimeData = value;
        },
        typhoonShowList(state, value) {
            state.typhoonShowVal = value;
        },
        particularsList(state, value) {
            state.particularsVal = value;
        },
        ChangeRightCollapse(state, value) {
            state.rightCollapse = value;
        },
        SET_RIGHT_COLLAPSE(state, status) {
            state.rightCollapse = status;
        },
        SET_LEFT_COLLAPSE(state, status) {
            state.leftCollapse = status;
        },
        SetMapChangeModule(state, data) {
            state.mapChangeModule = data;
        },
    },
    actions: {
        setCollapseRight({ commit }, status) {
            commit("SET_RIGHT_COLLAPSE", status);
        },
        setCollapseLeft({ commit }, status) {
            commit("SET_LEFT_COLLAPSE", status);
        },
    },
    modules,
    getters: {
        collapseLeft: (state) => state.leftCollapse,
        collapseRight: (state) => state.rightCollapse,
        mapChangeModule: (state) => state.mapChangeModule,
        analyseData: (state) => state.analyseData,
        typhoonDataList: (state) => state.typhoonData
    },
});