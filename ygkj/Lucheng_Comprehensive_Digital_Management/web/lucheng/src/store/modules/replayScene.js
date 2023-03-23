import moment from "moment";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

const replayScene = {
	state: {
		startTimeatamap: "",
		endTimeatamap: "",
		preStartTimestamp:"",
		preEndTimestamp:"",
		currentTimeatamap: "",
		stationStat: [
			{ name: "≥800", value: 0 },
			{ name: "500~800", value: 0 },
			{ name: "250~500", value: 0 },
			{ name: "100~250", value: 0 },
			{ name: "50~100", value: 0 },
			{ name: "25~50", value: 0 },
			{ name: "10~25", value: 0 },
			{ name: "0.1~10", value: 0 },
		],
		stationPoint: [],
		rainfallMaxStat: {
			行政区划: [
				{ name: "永嘉市", value: 0 },
				{ name: "乐清市", value: 0 },
				{ name: "瓯海区", value: 0 },
				{ name: "鹿城区", value: 0 },
				{ name: "洞头区", value: 0 },
			],
			流域: [
				{ name: "飞云江", value: 0 },
				{ name: "瓯江", value: 0 },
				{ name: "鳌江", value: 0 },
			],
		},
		rainfallMax: { maxBasin: { name: "", value: 0 }, maxArea: { name: "", value: 0 }, maxPoint: { name: "", value: 0 }, totalRainfall: 0 },
		barTimer:null,
	},
	mutations: {
		SET_START_TIMEATMAP(state, timeatmap) {
			state.startTimeatamap = timeatmap;
		},
		SET_PRESTART_TIMESTAMP(state, timestamp){
			state.preStartTimestamp = timestamp;
		},
		SET_END_TIMEATMAP(state, timeatmap) {
			state.endTimeatamap = timeatmap;
		},
		SET_PREEND_TIMESTAMP(state, timeatmap){
			state.preEndTimestamp = timeatmap;
		},
		SET_CURRENT_TIMEATMAP(state, timeatmap) {
			state.currentTimeatamap = timeatmap;
		},
		SET_STATION_POINT(state, data) {
			state.stationPoint = data.map((item) => {
				return { name: item.stationName, lng: item.longitude, lat: item.latitude, drp: item.rainfall, props: { 雨量: item.rainfall +' mm'}, type: "rainfall" };
			});
		},
		SET_STATION_DATA(state, data) {
			state.stationStat = [
				{ name: "≥800", value: data.beyond800 },
				{ name: "500~800", value: data.between500And800 },
				{ name: "250~500", value: data.between250And500 },
				{ name: "100~250", value: data.between100And250 },
				{ name: "50~100", value: data.between50And100 },
				{ name: "25~50", value: data.between25And50 },
				{ name: "10~25", value: data.between10And25 },
				{ name: "0.1~10", value: data.between0And10 },
			];
		},
		SET_RAINFALL_MAX_AREA(state, data) {
			state.rainfallMaxStat["行政区划"] = data
				.filter((item, index) => index < 5)
				.map((item) => {
					return { name: item.key, value: item.value };
				});
		},
		SET_RAINFALL_MAX_BASIN(state, data) {
			state.rainfallMaxStat["流域"] = data.map((item) => {
				return { name: item.key, value: item.value };
			});
		},
		SET_RAINFALL_MAX_DATA(state, data) {
			state.rainfallMax.maxBasin = { name: data.maxRainfallBasinName || "", value: Number(data.basinMaxRainfall).toFixed(1) || 0 };
			state.rainfallMax.maxArea = { name: data.maxRainfallAreaName || "", value: Number(data.areaMaxRainfall).toFixed(1) || 0 };
			state.rainfallMax.maxPoint = { name: data.maxPointName || "", value: Number(data.maxPointRainfall).toFixed(1) || 0 };
			state.rainfallMax.totalRainfall = Number(data.totalSurfaceRainfall).toFixed(1) || 0;
		},
		SET_BAR_TIMER(state,data){
			if(data == null){
				clearInterval(state.barTimer);
			}
			state.barTimer = data;
		}
	},
	actions: {
		setStartTimeatmap({ commit }, timeatmap) {
			const currentTimeatmap = Number(
				moment(timeatmap)
					.add(1, "h")
					.format("x")
			);
			commit("SET_START_TIMEATMAP", timeatmap);
			commit("SET_CURRENT_TIMEATMAP", currentTimeatmap);
		},
		setPreStartTimestamp({ commit },timestamp){
			commit("SET_PRESTART_TIMESTAMP",timestamp);
		},
		setPreEndTimeatamp({ commit },timestamp){
			commit("SET_PREEND_TIMESTAMP",timestamp);
		},
		setCurrentTimeatmap({ commit }, timeatmap) {
			commit("SET_CURRENT_TIMEATMAP", timeatmap);
		},
		setEndTimeatamp({ commit }, timeatmap) {
			commit("SET_END_TIMEATMAP", timeatmap);
		},
		getStationData({ commit, state }) {
			return new Promise((resolve, reject) => {
				const beginTime = moment(state.startTimeatamap).format("yyyy-MM-DD HH:mm:ss");
				const endTime = moment(state.currentTimeatamap).format("yyyy-MM-DD HH:mm:ss");
				if (beginTime == "Invalid date" || endTime == "Invalid date") {
					return;
				}
				disasterPreventionApi
					.replayRainfallStation({ beginTime, endTime })
					.then((res) => {
						if (res.code == 0) {
							let { data, ...stData } = res.data;
							commit("SET_STATION_POINT", data);
							commit("SET_STATION_DATA", stData);
							resolve(res.data);
						}
					})
					.catch((err) => {
						reject(err);
					});
			});
		},
		getRainfallMaxData({ commit, state }) {
			return new Promise((resolve, reject) => {
				const beginTime = moment(state.startTimeatamap).format("yyyy-MM-DD HH:mm:ss");
				const endTime = moment(state.currentTimeatamap).format("yyyy-MM-DD HH:mm:ss");
				if (beginTime == "Invalid date" || endTime == "Invalid date") {
					return;
				}
				disasterPreventionApi
					.replayRainfallMax({ beginTime, endTime })
					.then((res) => {
						if (res.code == 0) {
							let { areaRainfallChart, basinRainfallChart, ...other } = res.data;
							commit("SET_RAINFALL_MAX_AREA", areaRainfallChart);
							commit("SET_RAINFALL_MAX_BASIN", basinRainfallChart);
							commit("SET_RAINFALL_MAX_DATA", other);
							resolve(res.data);
						}
					})
					.catch((err) => {
						reject(err);
					});
			});
		},
		setBarTimer({ commit },data){
			commit("SET_BAR_TIMER",data);
		},
	},
	getters: {
		formatterPreStartTime(state){
			return (formatterString = "yyyy-MM-DD HH:mm") => {
				return moment(state.preStartTimestamp).format(formatterString);
			};
		},
		formatterStartTime(state) {
			return (formatterString = "yyyy-MM-DD HH:mm") => {
				return moment(state.startTimeatamap).format(formatterString);
			};
		},
		formatterCurrentTime(state) {
			return (formatterString = "yyyy-MM-DD HH:mm") => {
				return moment(state.currentTimeatamap).format(formatterString);
			};
		},
		formatterPreEndTime(state){
			return (formatterString = "yyyy-MM-DD HH:mm") => {
				return moment(state.preEndTimestamp).format(formatterString);
			};
		},
		formatterEndTime(state) {
			return (formatterString = "yyyy-MM-DD HH:mm") => {
				return moment(state.endTimeatamap).format(formatterString);
			};
		},
		formatterTotalTime(state) {
			let start = moment(state.startTimeatamap);
			let end = moment(state.currentTimeatamap);
			let diff = end.diff(start, "hour");
			return diff;
		},
		stationStat: (state) => state.stationStat,
		rainfallStat(state) {
			return (type) => {
				return state.rainfallMaxStat[type];
			};
		},
		rainfallMax: (state) => state.rainfallMax,
		stationPoint: (state) => state.stationPoint,
		barTimer:(state) => state.barTimer,
	},
};
export default replayScene;
