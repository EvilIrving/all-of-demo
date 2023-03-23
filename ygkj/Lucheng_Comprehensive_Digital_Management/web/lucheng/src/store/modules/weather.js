import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
const weather = {
	state: {
		weatherForecast: [],
		weatherWarning: [],
	},
	mutations: {
		SET_WEATHER_FORECAST(state, data) {
			state.weatherForecast = data;
		},
		SET_WEATHER_WARNING(state, data) {
			state.weatherWarning = data;
		},
	},
	actions: {
		getWeatherForecast({ commit }) {
			return new Promise((resolve, reject) => {
				disasterPreventionApi
					.wholeWeather()
					.then((res) => {
						if (res.code == 0) {
							//formatter data
							//todo
							commit("SET_WEATHER_FORECAST", res.data);
							resolve(res.data);
						} else {
							reject(res);
						}
					})
					.catch((err) => {
						reject(err);
					});
			});
		},
		getWeatherWarning({ commit }) {
			return new Promise((resolve, reject) => {
				disasterPreventionApi
					.weatherWarning()
					.then((res) => {
						if (res.code == 0) {
							//formatter data
							//todo
							commit("SET_WEATHER_WARNING", res.data);
							resolve(res.data);
						} else {
							reject(res);
						}
					})
					.catch((err) => {
						reject(err);
					});
			});
		},
	},
	getters: {
		wenzhouWeatherForecast: (state) => state.weatherForecast.find((item) => item.areaName == "温州市") || {},
		weatherForecast: (state) => state.weatherForecast,
	},
};
export default weather;
