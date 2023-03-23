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
            { label: "正常", icon: "icon-map-legend-reservoirwater" },
            { label: "病险", icon: "icon-map-legend-reservoirwater-abnormal" },
        ],
    },
    {
        label: "山塘",
        legends: [
            { label: "正常", icon: "icon-map-legend-pool" },
            { label: "病险", icon: "icon-map-legend-pool-abnormal" },
        ],
    },
    {
        label: "海塘",
        legends: [
            { label: "10年以下", icon: "icon-map-seawall-over10" },
            { label: "10年", icon: "icon-map-seawall-10" },
            { label: "20年", icon: "icon-map-seawall-20" },
            { label: "50年", icon: "icon-map-seawall-50" },
            { label: "100年", icon: "icon-map-seawall-100" },
            { label: "100年以上", icon: "icon-map-seawall-over100" },
            { label: "正常", icon: "icon-map-seawall-normal" },
            { label: "病险", icon: "icon-map-seawall-abnormal" },
        ],
    },
    {
        label: "水闸",
        legends: [
            { label: "正常", icon: "icon-map-legend-sluice" },
            { label: "病险", icon: "icon-map-legend-sluice-abnormal" },
        ],
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
        label: "水库水位",
        legends: [
            { label: "正常", icon: "icon-map-legend-reservoirwater" },
            { label: "超警", icon: "icon-map-legend-reservoirwater-abnormal" },
        ],
    },
    {
        label: "河道水位",
        legends: [
            { label: "正常", icon: "icon-map-legend-riverwater" },
            { label: "超警", icon: "icon-map-legend-riverwater-abnormal" },
        ],
    },
    {
        label: "堰闸水位",
        legends: [
            { label: "正常", icon: "icon-map-legend-checkgate" },
            { label: "超警", icon: "icon-map-legend-checkgate-abnormal" },
        ],
    },
    {
        label: "潮位",
        legends: [
            { label: "正常", icon: "icon-map-legend" },
            { label: "超警", icon: "icon-map-legend-abnormal" },
        ],
    },
    {
        label: "积水",
        legends: [
            { label: "有积水", icon: "icon-map-legend-hydrops" },
            { label: "无积水", icon: "icon-map-legend-hydrops-abnormal" },
        ],
    },
    {
        label: "河道",
        legends: [
            { label: "正常", icon: "icon-legend-stagnant-normal" },
            { label: "超警", icon: "icon-legend-stagnant-warn" },
        ],
    },
    {
        label: "视频",
        legends: [
            { label: "在线", icon: "icon-map-legend-video" },
            { label: "离线", icon: "icon-map-legend-video-abnormal" },
        ],
    },
    {
        label: "取水户",
        legends: [
            { label: "正常", icon: "icon-normal-icon" },
            { label: "接近许可", icon: "icon-approach-icon" },
            { label: "超许可", icon: "icon-super-license" },
        ]
    },
    {
        label: "地下机井",
        legends: [
            { label: "正在取水", icon: "icon-being-water-icons" },
            { label: "停止取水", icon: "icon-stop-water-icons" },
            { label: "设备掉线", icon: "icon-equipment-drops-icons" },
            { label: "预警", icon: "icon-early-warning-icons" },
        ]
    },
    {
        label: "地下水位站",
        legends: [
            { label: "在线", icon: "icon-on-line-icons" },
            { label: "离线", icon: "icon-off-line-icons" },
        ]
    },
    {
        label: "取水口",
        legends: [
            { label: "正在取水", icon: "icon-being-icon" },
            { label: "停止取水", icon: "icon-stop-icon" },
            { label: "设备掉线", icon: "icon-drop-icon" },
        ],
    }, {
        label: "水文站水质",
        legends: [
            { label: "I类", icon: "icon-one-icon" },
            { label: "II类", icon: "icon-two-icon" },
            { label: "III类", icon: "icon-three-icon" },
            { label: "IV类", icon: "icon-four-icon" },
            { label: "V类", icon: "icon-five-icon" },
            { label: "劣V类", icon: "icon-six-icon" }
        ],
    }, {
        label: "生产建设项目",
        legends: [
            { label: "项目", icon: "icon-legend-rain10" },
        ],
    }
    // , {
    //     label: "入河排污口",
    //     legends: [
    //         { label: "", icon: "icon-legend-sq" },
    //     ],
    // }, {
    //     label: "雨水口",
    //     legends: [
    //         { label: "", icon: "icon-legend-sq" },
    //     ],
    // }, {
    //     label: "河景",
    //     legends: [
    //         { label: "", icon: "icon-legend-sq" },
    //     ],
    // }
];

const legends = {
    state: {
        displayLegends: [],
    },
    mutations: {
        ADD_LEGEND(state, legend) {
            state.displayLegends.unshift(legend);
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
        addLegend({ commit, state }, legendName) {
            const legend = legendList.find((item) => item.label == legendName);
            if (legend == undefined) return;
            if (state.displayLegends.findIndex(item => item.label == legend.label) == -1) {
                commit("ADD_LEGEND", legend);
            }
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