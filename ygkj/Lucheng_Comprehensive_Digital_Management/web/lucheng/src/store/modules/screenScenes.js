const screenScenes = {
	state: {
		scenesList: ["防御态势", "防洪调度", "防御复盘", { label: "抗旱调度", disabled: true }],
		scene: "防御态势",
		timelinebarShow: false,
		rainfullActive: false,
	},
	mutations: {
		SET_SCENE(state, scene) {
			state.scene = scene;
		},
		CHANGE_TIMELINEBAR_SHOW(state, flag) {
			state.timelinebarShow = flag;
		},
		SET_RAINFULL_ACTIVE(state, data) {
			state.rainfullActive = data;
		},
	},
	actions: {
		changeScene({ commit }, scene) {
			commit("SET_SCENE", scene);
			commit("SET_PREVIEWMODULE",false);
			commit("SET_BAR_TIMER",null);
		},
		changeTimelinebarShow({ commit }, flag) {
			commit("CHANGE_TIMELINEBAR_SHOW", flag);
		},
		changeRainfullActive({ commit, state }) {
			commit("SET_RAINFULL_ACTIVE", !state.rainfullActive);
		},
	},
	getters: {
		scenesList: (state) => state.scenesList,
		currentScene: (state) => state.scene,
		timelinebarShow: (state) => state.timelinebarShow,
		rainfullActive: (state) => state.rainfullActive,
	},
};
export default screenScenes;
