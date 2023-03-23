import { disasterPreventionApi } from "@/api/waterDisasterPrevention/index.js";
const rainfall = {
	state: {
		//==================
		// 短临预报
		//==================
		/** 分小时平均降雨量 */
		forecastRainfallByHour: {},
		/** 面雨量数据 */
		forecastSurfaceData: {},
		/** 各类型最大降雨量列表 */
		forecastMaxData: {},
		/** 按流域、行政区划图表数据  Data=>rows*/
		forecastChartData: {
			basin: {},
			area: {},
		},
		//==================
		// 实时降雨
		//==================
		/** 分小时平均降雨量 */
		realtimeRainfallByHour: {},
		/** 面雨量数据 */
		realtimeSurfaceData: {},
		/** 各类型最大降雨量列表 */
		realtimeMaxData: [],
		/** 按流域、行政区划、水库图表数据  Data=>rows*/
		realtimeChartData: [],
	},
	mutations: {
		SET_FORECAST_HOUR(state, data) {
			Object.getOwnPropertyNames(data).forEach((item) => {
				state.forecastRainfallByHour[item] = data[item];
			});
		},
		SET_FORECAST_SURFACE(state, data) {
			state.forecastSurfaceData = data;
		},
		SET_FORECAST_MAX(state, { hour, data }) {
			state.forecastMaxData[`hour${hour}`] = data;
		},
		SET_FORECAST_CHART(state, data) {
			state.forecastChartData = data;
		},
		SET_REALTIME_HOUR(state, { hour, data }) {
			state.realtimeRainfallByHour[`hour${hour}`] = data;
		},
		SET_REALTIME_SURFACE(state, data) {
			state.realtimeSurfaceData = data;
		},
		SET_REALTIME_MAX(state, data) {
			state.realtimeMaxData = data;
		},
		SET_REALTIME_CHART(state, data) {
			state.realtimeChartData = data;
		},
	},
	actions: {
		/** 获取预报数据 */
		getForecastData({ commit }, areaName) {
			return new Promise((resolve, reject) => {
				disasterPreventionApi
					.predictRainfallByAreaName({ areaName })
					.then((res) => {
						if (res.code == 0) {
							const data = res.data;
							console.log(data);
							const rainfallByHour = { hour1: data.rainfall_1, hour3: data.rainfall_3, hour6: data.rainfall_6 };
							const rainfallSurfaceData = { hour1: data.rainfall_1_data, hour3: data.rainfall_3_data, hour6: data.rainfall_6_data };
							commit("SET_FORECAST_HOUR", rainfallByHour);
							commit("SET_FORECAST_SURFACE", rainfallSurfaceData);
							resolve(res);
						} else {
							reject(res);
						}
					})
					.catch((err) => reject(err));
			});
		},
		/** 获取预报各小时极值 */
		getForecastEachHour({ commit }, hour) {
			return new Promise((resolve, reject) => {
				disasterPreventionApi
					.rainfallMaxValuePredict({ hour })
					.then((res) => {
						if (res.code == 0) {
							const data = res.data;
							const area = {
								name: data.maxRainfallAreaName,
								value: data.maxRainfall.toFixed(1),
								type: "area",
							};
							const basin = {
								name: data.maxRainfallBasinName,
								value: data.maxBasinRainfall.toFixed(1),
								type: "basin",
							};
							const point = {
								name: "",
								value: data.areaMaxRainfall.toFixed(1),
								lng: data.maxRainfallLongitude,
								lat: data.maxRainfallLatitude,
								type: "station",
							};
							const rainfallMax = [
								{ label: `预计${hour}小时最大降雨`, data: area },
								{ label: `预计${hour}小时最大降雨区域`, data: basin },
								{ label: `预计${hour}小时最大降雨流域`, data: point },
							];
							commit("SET_FORECAST_MAX", { hour, data: rainfallMax });
							resolve(res);
						} else {
							reject(res);
						}
					})
					.catch((err) => reject(err));
			});
		},
		/** 获取预报图表数据 */
		getForecastChart({ commit }) {
			return new Promise((resolve, reject) => {
				let basinPromise = disasterPreventionApi.rainfallPredictGrid({ type: "流域" });
				let areaPromise = disasterPreventionApi.rainfallPredictGrid({ type: "行政区划" });
				Promise.all([basinPromise, areaPromise]).then(([resBasin, resArea]) => {
					const [dataBasin, dataArea] = [resBasin.data, resArea.data];
					let basinChartData = {},
						areaChartData = {};

					[1, 3, 6].forEach((hour) => {
						basinChartData[`hour${hour}`] = dataBasin.map((item) => {
							const name = item.key;
							return {
								name,
								max: Number(item[`${hour}hourMaxRainfall`]),
								min: Number(item[`${hour}hourMinRainfall`]),
							};
						});
						areaChartData[`hour${hour}`] = dataArea
							.filter((item, index) => index < 5)
							.map((item) => {
								const name = item.key;
								return {
									name,
									max: Number(item[`${hour}hourMaxRainfall`]),
									min: Number(item[`${hour}hourMinRainfall`]),
								};
							});
					});
					commit("SET_FORECAST_CHART", { basin: basinChartData, area: areaChartData });
				});
			});
		},
		/** 获取实时数据 */
		getRealtimeData({ commit }, areaName) {
			return new Promise((resolve, reject) => {
				disasterPreventionApi.realtimeRainfallByAreaName({ areaName }).then((res) => {
					if (res.code == 0) {
					}
				});
			});
		},
	},
	getters: {
		forecastByHour: (state) => {
			const labelMap = { hour1: "1小时", hour3: "3小时", hour6: "6小时" };
			const list = Object.keys(state.forecastRainfallByHour).length > 0 ? Object.keys(state.forecastRainfallByHour) : ["hour1", "hour3", "hour6"];
			return list.map((item) => ({
				label: labelMap[item],
				data: { number: state.forecastRainfallByHour[item] || 0, unit: "mm" },
			}));
		},
		forecastChartData: (state) => {
			return (type = "basin", hour = 1) => {
				return state.forecastChartData[type]?.[`hour${hour}`] || [];
			};
		},
		forecastMaxList: (state) => {
			return (hour = 1) => {
				return (
					state.forecastMaxData[`hour${hour}`] || [
						{ label: "预计1小时最大降雨", data: {} },
						{ label: "预计1小时最大降雨区域", data: {} },
						{ label: "预计1小时最大降雨流域", data: {} },
					]
				);
			};
		},
	},
};

export default rainfall;
