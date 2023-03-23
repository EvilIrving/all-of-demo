import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

const torrents = {
	state: {
		torrentsWarning: {},
	},
	mutations: {
		SET_TORRENTS_WARNING(state, data) {
			state.torrentsWarning = data;
		},
	},
	actions: {
		getTorrentsWarning({ commit },param) {
			return new Promise((resolve, reject) => {
				let opt = {};
				if(param){
					opt = {
						endTime:param.time,
						currentFlag:param.currentFlag,
					} 
					if(param.drillAffairId){
						opt.drillAffairId = param.drillAffairId;
					}
				}
				// console.log(param,223)
				disasterPreventionApi
					.floodWarning(opt)
					.then((res) => {
						if (res.code == 0) {
							console.log(6666,res.data)
							commit("SET_TORRENTS_WARNING", res.data);
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
		torrentsWarning: (state) => state.torrentsWarning,
		effectTown: (state) => state.torrentsWarning.data?.town || [],
	},
};
export default torrents;
