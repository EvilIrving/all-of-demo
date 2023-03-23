
const generalMap = {
	state: {
		basinArea:"瓯江",
		generalInfoArr:['名称'],
	},
	mutations: {
		SET_BASIN_AREA(state, data) {
			state.basinArea = data;
		},
		SET_GENERAL_INFOARR(state, data){
			state.generalInfoArr = data;
		}
	},
	actions: {
					
	},
	getters: {
		basinArea: (state) => state.basinArea,
		generalInfoArr: (state) => state.generalInfoArr,
	},
};
export default generalMap;
