import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

const typhoon = {
	state: {
		currentTyphoonList: [],
		historyTyphoonList: [],
	},
	mutations: {
		SET_CURRENT_TYPHOON: (state, data) => {
			state.currentTyphoonList = data;
		},
		SET_HISTORY_TYPHOON: (state, data) => {
			state.historyTyphoonList = data;
		},
	},
	actions: {
		GetCurrentTyphoonList({ commit }) {
			return new Promise((resolve, reject) => {
				disasterPreventionApi
					.getRealTyphoon()
					.then((res) => {
						if (res.data.length > 0) {
							commit("SET_CURRENT_TYPHOON", res.data);
							resolve(res.data);
						}
					})
					.catch((err) => {
						reject(err);
					});
			});
		},
		getHistroyTyphoonList({ commit }, data) {
			return new Promise((resolve, reject) => {
				/**
				 * @desc data=>{begin,end,name}
				 */
				disasterPreventionApi
					.getTyphoons(data)
					.then((res) => {
						commit("SET_HISTORY_TYPHOON", res.data);
						resolve(res.data);
					})
					.catch((err) => {
						reject(err);
					});
			});
		},
	},
	getters: {
		currentTyphoon: (state) => state.currentTyphoonList.length > 0,
		historyTyphoonList: (state) => state.historyTyphoonList,
	},
};
export default typhoon;
