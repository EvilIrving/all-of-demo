
const sluicesDistribution = {
	state: {
    sluiceActive: [], //水闸分析勾选项
	},
	mutations: {
    SET_SLUICE_ACTIVE(state, data) {
      state.sluiceActive = data;
    }
	},
	actions: {
					
	},
	getters: {
		sluiceActive: (state) => state.sluiceActive,
	},
};
export default sluicesDistribution;
