const planning = {
	state: {
		id: '',
		title: ''
	},
	mutations: {
		SET_ID(state, payload) {
			state.id = payload;
		},
		SET_TITLE(state, payload) {
			state.title = payload;
		}
	},
	actions: {
		setPlanningId({ commit }, id) {
			commit('SET_ID', id);
		},
		setPlanningTitle({ commit }, title) {
			commit('SET_TITLE', title);
		},
		setPlanning({ commit }, { id, title }) {
			commit('SET_ID', id);
			commit('SET_TITLE', title);
		}
	},
	getters: {
		currentPlanning: state => ({ id: state.id, title: state.title })
	}
};
export default planning;
