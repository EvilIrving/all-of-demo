/**
 * @desc 暂时只用于一张图
 */

/**
 * 图例列表
 * @todo 把叶胜隆 写css的icon弄到spite图里
 */
const legendList = [
	//
	{
		label: "水库",
		legends: [
			{ label: "正常", icon: "icon-legend-water-general-reservoir" },
			{ label: "超警", icon: "icon-legend-water-flood-control" },
		],
	},
	{
		label: "山塘",
		legends: [
			{ label: "正常", icon: "icon-legend-pond-normal" },
			{ label: "超警", icon: "icon-legend-pond-abnormal" },
		],
	},
	{
		label: "堤防",
		legends: [
			{ label: "正常", icon: "icon-legend-difang" },
			{ label: "超警", icon: "icon-legend-difangchaojing" },
		],
	},
	{
		label: "海塘",
		legends: [
			{ label: "正常", icon: "icon-legend-seawall-normal" },
			{ label: "病险", icon: "icon-legend-seawall-abnormal" },
		],
	},
	{
		label: "泵站",
		legends: [
			{ label: "正常", icon: "icon-legend-pump-normal" },
			{ label: "病险", icon: "icon-legend-pump-abnormal" },
		],
	},
	{
		label: "水闸",
		legends: [
			{ label: "关闭", icon: "icon-shui-zha-guanbi" },
			{ label: "打开", icon: "icon-shui-zha-kaiqi" },
		],
	},
	{
		label: "闸站",
		legends: [
			{ label: "正常", icon: "icon-legend-sluice-normal1" },
			{ label: "超警", icon: "icon-legend-sluice-abnormal" },
		],
	},
	{
		label: "灌区",
		legends: [],
	},
	{
		label: "雨情",
		legends: [
			{ label: "无数据", icon: "icon-legend-rain0" },
			{ label: "无降雨", icon: "icon-legend-rain9" },
			{ label: "<10", icon: "icon-legend-rain10" },
			{ label: "10~25", icon: "icon-legend-rain10" },
			{ label: "25~50", icon: "icon-legend-rain25" },
			{ label: "50~100", icon: "icon-legend-rain50" },
			{ label: "100~250", icon: "icon-legend-rain100" },
			{ label: ">250", icon: "icon-legend-rain100" },
			{ label: "超警", icon: "icon-legend-rain100" },
		],
	},
	{
		label: "积水",
		legends: [
			{ label: "有积水", icon: "icon-legend-jishuidian" },
			{ label: "无积水", icon: "icon-legend-wujishui" },
		],
	},
	{
		label: "河道",
		legends: [
			{ label: "正常", icon: "icon-legend-stagnant-normal" },
			{ label: "超警", icon: "icon-legend-stagnant-warn" },
		],
	},
];

const legends = {
	state: {
		displayLegends: [],
	},
	mutations: {
		ADD_LEGEND(state, legend) {
			state.displayLegends.push(legend);
		},
		REMOVE_LEGEND(state, legendName) {
			const findIndex = state.displayLegends.findIndex((item) => item.label == legendName);
			if (findIndex == -1) return;
			state.displayLegends.splice(findIndex, 1);
		},
		CLEAR_LEGENDS(state) {
			state.displayLegends = [];
		},
	},
	actions: {
		/**
		 * 新增图例
		 * @param {object} legend { name: { string }, children: { object }}
		 */
		addLegend({ commit }, legendName) {
			const legend = legendList.find((item) => item.label == legendName);
			if (legend == undefined) return;
			commit("ADD_LEGEND", legend);
		},
		/**
		 * 移除图例
		 */
		removeLegend({ commit }, legendName) {
			commit("REMOVE_LEGEND", legendName);
		},
		/**
		 * 清除图例
		 */
		clearLegends({ commit }) {
			commit("CLEAR_LEGENDS");
		},
	},
	getters: {
		displayLegends: (state) => state.displayLegends,
	},
};

export default legends;
