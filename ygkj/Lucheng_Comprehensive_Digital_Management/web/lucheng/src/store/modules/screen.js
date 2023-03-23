import axios from "axios";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention/index.js";
import mapConfig from "../../config/mapConfig";

const screen = {
	state: {
		mapComponent: null,
		weatherPopover: false,
		selectArea: "温州市",
		selectBasin: "全流域",
		forecastRainfallLayers: { 温州市: { hour1: "", hour3: "", hour6: "" } },
		screenLoading: false,
		previewModule: false,//预演模式
		previewData: {},
		rainfallArea: false,//等值面开关标记
		affairId: "",//演练id
		seawallRangModule: false,
		seawallData: [],
		seawallDrillData: {},
	},
	mutations: {
		SET_MAP_COMPONENT(state, obj) {
			state.mapComponent = obj;
		},
		SET_WEATHER_POPOVER(state, status) {
			state.weatherPopover = status;
		},
		SET_CURRENT_AREA(state, value) {
			state.selectArea = value;
		},
		SET_CURRENT_BASIN(state, value) {
			state.selectBasin = value;
		},
		SAVE_RAINFALLLAYER(state, { hour, area, jobId }) {
			state.forecastRainfallLayers[area][`hour${hour}`] = jobId;
		},
		SET_SCREEN_LOADING(state, status) {
			state.screenLoading = status;
		},
		SET_PREVIEWMODULE(state, status) {
			state.previewModule = status;
		},
		SET_PREVIEWDATA(state, status) {
			state.previewData = status;
		},
		SET_RAINFALL_AREA(state, status) {
			state.rainfallArea = status;
		},
		SET_AFFAIR_ID(state, data) {
			state.affairId = data;
		},
		SET_SEAWALLRANGMODULE(state, data) {
			state.seawallRangModule = data;
		},
		SET_SEAWALLDATA(state, data) {
			state.seawallData = data;
		},
		SET_SEAWALL_DRILL_DATA(state, data) {
			state.seawallDrillData = data
		}
	},
	actions: {
		SetMapComponent({ commit }, obj) {
			commit("SET_MAP_COMPONENT", obj);
		},
		setWeatherPopover({ commit }, value) {
			commit("SET_WEATHER_POPOVER", value);
		},
		changeArea({ commit }, name) {
			commit("SET_CURRENT_AREA", name);
		},
		changeBasin({ commit }, name) {
			commit("SET_CURRENT_BASIN", name);
		},
		changeScreenLoading({ commit }, status) {
			commit("SET_SCREEN_LOADING", status);
		},
		setSeawallDrillData({ commit }, data) {
			commit("SET_SEAWALL_DRILL_DATA", data);
		},
		/**
		 * 获得等于量面数据
		 * @param {*} opt
		 * @param {*} type 不知道干嘛的标识符
		 * @param {*} area 区划
		 * @param {*} hour 小时
		 * @returns Promise resolve=>{ mapImage, jobId, type }
		 */
		getForecastRainfallLayer({ commit }, { opt, type, area, hour = 1 }) {
			return new Promise((resolve, reject) => {
				let url = mapConfig.mapServer + "gps/PointTRaster/GPServer/PointTRaster/";
				let typeUrl = "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPIDWReal0522/GPServer/dengzhiPIDWReal/";
				let areaUrl = type ? typeUrl + "submitJob" : url + "submitJob";
				let params = new URLSearchParams();
				if (type) {
					params.append("station", JSON.stringify(opt));
				} else {
					params.append("points", JSON.stringify(opt));
					params.append("FiledValue", "value");
					params.append("pixSize", "0.01");
					params.append("pixAllocationType", "MAXIMUM");
				}
				params.append("f", "pjson");
				let pollingTimer = null;
				const options = {
					method: "post",
					url: areaUrl,
					headers: { "content-type": "application/x-www-form-urlencoded" },
					data: params,
				};
				axios(options).then((res) => {
					let startTimer = () => {
						pollingTimer = setTimeout(() => {
							let jobUrl = type ? `${typeUrl}jobs/${res.data.jobId}/results/Idwres` : `${url}jobs/${res.data.jobId}/results/outRaster`;
							axios({ method: "get", url: `${jobUrl}?f=pjson` }).then((result) => {
								if (result.data.value.mapImage.error) {
									startTimer();
								} else {
									clearTimeout(pollingTimer);
									commit("SAVE_RAINFALLLAYER", { hour, area, jobId: res.data.jobId });
									resolve({ mapImage: result.data.value.mapImage.extent, jobId: res.data.jobId, type });
								}
							});
						}, 1000);
					};
					startTimer();
				});
			});
		},
		changePreviewModule({ commit, state }, status) {
			commit("SET_PREVIEWMODULE", status);
			//退出演练 请求退出演练接口
			if (!status) {
				if (state.affairId) {
					disasterPreventionApi.closeDrillAffair({ affairId: state.affairId });
					commit("SET_AFFAIR_ID", "");
				}
			}
		},
		changeSeawallRangModule({ commit }, status) {
			commit("SET_SEAWALLRANGMODULE", status);
		},
		setSeawallData({ commit }, status) {
			commit("SET_SEAWALLDATA", status);
		},
		setPreviewData({ commit }, data) {
			commit('SET_PREVIEWDATA', data);
		},
		setRainfallArea({ commit }, status) {
			commit('SET_RAINFALL_AREA', status);
		},
		SetAffairId({ commit }, data) {
			commit("SET_AFFAIR_ID", data);
		},
	},
	getters: {
		ScreenMap: (state) => state.mapComponent,
		weatherPopover: (state) => state.weatherPopover,
		currentSelectArea: (state) => state.selectArea,
		currentSelectBasin: (state) => state.selectBasin,
		screenLoading: (state) => state.screenLoading,
		previewModule: (state) => state.previewModule,
		previewData: (state) => state.previewData,
		rainfallArea: (state) => state.rainfallArea,
		AffairId: (state) => state.affairId,
		seawallRangModule: (state) => state.seawallRangModule,
		seawallData: (state) => state.seawallData,
		SeawallDrillData: (state) => state.seawallDrillData
	},
};
export default screen;
