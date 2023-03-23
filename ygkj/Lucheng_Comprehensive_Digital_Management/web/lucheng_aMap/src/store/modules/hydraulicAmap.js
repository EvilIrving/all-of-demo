
const hydraulicAmap = {
	state: {
		mapComponent:null,
		checkedList:[],//选中图层
	},
	mutations: {
		SET_MAP_COMPONENT(state,obj){
			state.mapComponent = obj;
		},
		SET_CHECKED_LIST(state,data){
			state.checkedList = data;
		}
	},
	actions: {
		SetCheckedList({ commit },data){
			commit('SET_CHECKED_LIST',data);
		},
		SetMapComponent({ commit },data){
			commit('SET_MAP_COMPONENT',data);
		}
	},
	getters: {
		CheckedList:(state) => state.checkedList,
		MapComponent:(state) => state.mapComponent,
	},
};
export default hydraulicAmap;
