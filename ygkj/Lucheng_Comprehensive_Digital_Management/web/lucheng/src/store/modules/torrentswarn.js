import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

const torrents = {
	state: {
		torrentsWarn: {},
	},
	mutations: {
		SET_TORRENTS_WARNING(state, data) {
			state.torrentsWarn = data;
		},
	},
	actions: {
		getTorrentsWarn({ commit },param) {
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
					.mountainTorrentBaseStats(opt)
					.then((res) => {
						if (res.code == 0) {
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
		torrentsWarn: (state) => state.torrentsWarn,
		effectTown: (state) => state.torrentsWarn.data?.town || [],
	},
};
export default torrents;
