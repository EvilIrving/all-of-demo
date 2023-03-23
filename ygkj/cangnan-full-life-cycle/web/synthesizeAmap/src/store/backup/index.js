import Vue from "vue";
import Vuex from "vuex";

import { getRainList, getWaterList, getRealRainList } from "../actions/rescueSupport";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        mapMenu: 0,
        layerList: [], //图层列表
        typhoonId: "",
        typhoonRelatedList: [], //台风风圈关联工程
        userTree: [],
        warehouseList: [], //抢险支持-防汛仓库数据
        teamList: [], //抢险支持-防汛队伍数据
        rainList: [], //抢险支持-雨情数据
        waterList: [], //抢险支持-水情数据
        teamDispatchList: [], //抢险支持-队伍调度-路线详情
        warehouseDispatchList: [], //抢险支持-仓库调度-路线详情
        waterDeep: [{
            city: "温州市",
            city_code: 3303,
            district: "瓯海区",
            district_code: 330304,
            street: "梧田街道",
            street_code: 330304002,
            id: 810091812130067,
            level: "",
            history_highest_level: "",
            name: "瓯海大道8标段",
            longitude: 120.68153209247777,
            latitude: 27.969417318039195,
            acquisition_time: "",
            water_level: 0,
            historical_highest_water_level: 0.0,
            voltage: "",
            battery: "",
            address: "浙江省温州市鹿城区瓯海大道8标段",
            name2: "wzfxb_0067",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "滨江街道",
            street_code: 330302024,
            id: 810091812130068,
            level: 0,
            history_highest_level: 16.13,
            name: "灰桥新村",
            longitude: 120.67632783755239,
            latitude: 28.019969924217062,
            acquisition_time: "2021-03-16 01:17:44",
            water_level: 0.0,
            historical_highest_water_level: 16.13,
            voltage: 12.98,
            battery: 13400,
            address: "浙江省温州市鹿城区江滨西路灰桥新村",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "五马街道",
            street_code: 330302006,
            id: 810091812130069,
            level: 0,
            history_highest_level: 24.57,
            name: "水门头公交站",
            longitude: 120.65150438828501,
            latitude: 28.027324303372144,
            acquisition_time: "2021-03-15 20:45:05",
            water_level: 0.0,
            historical_highest_water_level: 24.57,
            voltage: 13.31,
            battery: 13400,
            address: "浙江省温州市鹿城区望江东路水门头公交站附近",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "五马街道",
            street_code: 330302006,
            id: 810091812130070,
            level: 0,
            history_highest_level: 37.53,
            name: " 大高桥",
            longitude: 120.65151272515996,
            latitude: 28.012103741637368,
            acquisition_time: "2021-03-16 06:22:43",
            water_level: 0.0,
            historical_highest_water_level: 37.53,
            voltage: 13.31,
            battery: 13400,
            address: "浙江省温州市鹿城区大高桥",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "南汇街道",
            street_code: 330302025,
            id: 810091812130072,
            level: 0,
            history_highest_level: 28.63,
            name: "府东路温州大道口",
            longitude: 120.69881628651022,
            latitude: 27.984981916105973,
            acquisition_time: "",
            water_level: 0,
            historical_highest_water_level: "",
            voltage: "",
            battery: "",
            address: "浙江省温州市鹿城区府东路、温州大道与府东路口往北150米",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "滨江街道",
            street_code: 330302024,
            id: 810091812130073,
            level: 0.0,
            history_highest_level: 28.63,
            name: "会展路",
            longitude: 120.72333487659043,
            latitude: 28.006957119049797,
            acquisition_time: "2021-03-15 10:09:36",
            water_level: 0.0,
            historical_highest_water_level: 28.63,
            voltage: 13.1,
            battery: 13400,
            address: "浙江省温州市鹿城区学院东路与会展路口往东南方向100米处",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "滨江街道",
            street_code: 330302024,
            id: 810091812130074,
            level: 0.0,
            history_highest_level: 18.92,
            name: "杨府山下涂站",
            longitude: 120.72092064566502,
            latitude: 28.008747287763434,
            acquisition_time: "2021-03-15 17:00:03",
            water_level: 0.0,
            historical_highest_water_level: 18.92,
            voltage: 12.55,
            battery: 13400,
            address: "浙江省温州市鹿城区学院东路杨府山下涂站",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "广化街道",
            street_code: 330302032,
            id: 810091812130075,
            level: 0.0,
            history_highest_level: 21.12,
            name: "鹿城路高架",
            longitude: 120.62040165687092,
            latitude: 28.018829063706434,
            acquisition_time: "2021-03-15 12:48:11",
            water_level: 0.0,
            historical_highest_water_level: 21.12,
            voltage: 13.16,
            battery: 13400,
            address: "浙江省温州市鹿城区东瓯大道鹿城路高架",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130076,
            level: 0.0,
            history_highest_level: 18.59,
            name: "过境路口",
            longitude: 120.60124278636469,
            latitude: 28.020542387611123,
            acquisition_time: "2021-03-16 09:12:18",
            water_level: 0.0,
            historical_highest_water_level: 18.59,
            voltage: 13.23,
            battery: 13400,
            address: "浙江省温州市鹿城区温金璐过境路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130079,
            level: 0.0,
            history_highest_level: 16.01,
            name: "康华路口",
            longitude: 120.60076847022435,
            latitude: 28.011504011686696,
            acquisition_time: "2021-03-16 06:34:50",
            water_level: 0.0,
            historical_highest_water_level: 16.01,
            voltage: 12.75,
            battery: 13400,
            address: "浙江省温州市鹿城区康强西路康华路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "丰门街道",
            street_code: 330302033,
            id: 810091812130080,
            level: 0.0,
            history_highest_level: 7.62,
            name: "时尚港",
            longitude: 120.58703857194894,
            latitude: 28.040465439943134,
            acquisition_time: "2021-03-15 12:11:35",
            water_level: 0.0,
            historical_highest_water_level: 7.62,
            voltage: 13.9,
            battery: 13400,
            address: "浙江省温州市鹿城区时尚港",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130082,
            level: 0.0,
            history_highest_level: 3.98,
            name: "鞋都大道江湾路口",
            longitude: 120.59790009962354,
            latitude: 28.026409219998676,
            acquisition_time: "2021-03-16 09:32:01",
            water_level: 0.0,
            historical_highest_water_level: 3.98,
            voltage: 13.19,
            battery: 13400,
            address: "浙江省温州市鹿城区鞋都大道江湾路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "广化街道",
            street_code: 330302032,
            id: 810091812130090,
            level: 0.0,
            history_highest_level: 22.86,
            name: "太平寺公交站",
            longitude: 120.6225190871056,
            latitude: 28.018531765828236,
            acquisition_time: "2021-03-15 11:31:36",
            water_level: 0.0,
            historical_highest_water_level: 22.86,
            voltage: 12.93,
            battery: 13400,
            address: "浙江省温州市鹿城区太平寺公交站",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130091,
            level: 1.5,
            history_highest_level: 22.41,
            name: "温金璐江湾路口",
            longitude: 120.59625765758317,
            latitude: 28.022547515810935,
            acquisition_time: "2021-03-13 20:09:13",
            water_level: 1.5,
            historical_highest_water_level: 22.41,
            voltage: 8.4,
            battery: 500,
            address: "浙江省温州市鹿城区温金璐江湾路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "瓯海区",
            district_code: 330304,
            street: "景山街道",
            street_code: 330304001,
            id: 810091812130092,
            level: 0.0,
            history_highest_level: 8.56,
            name: "双屿锦园",
            longitude: 120.63408349659731,
            latitude: 28.007574422630714,
            acquisition_time: "2021-03-15 15:35:37",
            water_level: 0.0,
            historical_highest_water_level: 8.56,
            voltage: 13.21,
            battery: 13400,
            address: "浙江省温州市鹿城区双屿锦园",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130093,
            level: 0.0,
            history_highest_level: 10.57,
            name: "奋飞路口",
            longitude: 120.60443179177298,
            latitude: 28.022104429177741,
            acquisition_time: "2021-03-15 10:24:24",
            water_level: 0.0,
            historical_highest_water_level: 10.57,
            voltage: 12.63,
            battery: 13400,
            address: "浙江省温州市鹿城区三开路奋飞路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130094,
            level: 0.0,
            history_highest_level: 0.0,
            name: "康兴路口",
            longitude: 120.60206670524165,
            latitude: 28.013951127869333,
            acquisition_time: "2021-03-15 14:14:08",
            water_level: 0.0,
            historical_highest_water_level: 0.0,
            voltage: 13.42,
            battery: 13400,
            address: "浙江省温州市鹿城区康华路康兴路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "广化街道",
            street_code: 330302032,
            id: 810091812130098,
            level: 0.0,
            history_highest_level: 0.0,
            name: "望江西路鞋都大道口",
            longitude: 120.62416174599115,
            latitude: 28.023507622849326,
            acquisition_time: "2021-03-16 10:45:16",
            water_level: 0.0,
            historical_highest_water_level: 19.34,
            voltage: 12.91,
            battery: 13400,
            address: "浙江省温州市鹿城区望江西路鞋都大道口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091812130099,
            level: 0,
            history_highest_level: 0.0,
            name: "康兴路东",
            longitude: 120.60286920573876,
            latitude: 28.013711684202502,
            acquisition_time: "",
            water_level: 0,
            historical_highest_water_level: 0.0,
            voltage: "",
            battery: "",
            address: "浙江省温州市鹿城区康兴路东",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "瓯海区",
            district_code: 330304,
            street: "梧田街道",
            street_code: 330304002,
            id: 810091904170001,
            level: 0.0,
            history_highest_level: 3.37,
            name: "龙霞路铁路桥",
            longitude: 120.66707326949869,
            latitude: 27.98063739279835,
            acquisition_time: "2021-03-15 08:32:46",
            water_level: 0.0,
            historical_highest_water_level: 3.37,
            voltage: 13.18,
            battery: 13400,
            address: "浙江省温州市鹿城区龙霞路铁路桥下(西)",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "松台街道",
            street_code: 330302026,
            id: 810091904170002,
            level: 0,
            history_highest_level: 22.38,
            name: "勤奋路口",
            longitude: 120.6366304430861,
            latitude: 28.015289889283199,
            acquisition_time: "2021-03-15 16:44:20",
            water_level: 0.0,
            historical_highest_water_level: 22.38,
            voltage: 13.21,
            battery: 13400,
            address: "浙江省温州市鹿城区鹿城路勤奋路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "广化街道",
            street_code: 330302032,
            id: 810091904170003,
            level: 0.0,
            history_highest_level: 22.03,
            name: "帆影广场",
            longitude: 120.63111753309926,
            latitude: 28.023655034593858,
            acquisition_time: "2021-03-15 23:47:52",
            water_level: 0.0,
            historical_highest_water_level: 22.03,
            voltage: 12.69,
            battery: 13400,
            address: "浙江省温州市鹿城区帆影广场",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "丰门街道",
            street_code: 330302033,
            id: 810091904170005,
            level: 0.0,
            history_highest_level: 27.0,
            name: "腾达路口",
            longitude: 120.58669599743398,
            latitude: 28.041089541443839,
            acquisition_time: "2021-03-16 07:15:59",
            water_level: 0.0,
            historical_highest_water_level: 27.0,
            voltage: 12.44,
            battery: 13400,
            address: "浙江省温州市鹿城区鞋都大道腾达路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091904170006,
            level: 0.0,
            history_highest_level: 20.55,
            name: "双屿客运高架",
            longitude: 120.59561543604333,
            latitude: 28.022444685215905,
            acquisition_time: "2021-03-15 13:03:55",
            water_level: 0.0,
            historical_highest_water_level: 20.55,
            voltage: 13.2,
            battery: 13400,
            address: "浙江省温州市鹿城区双屿客运高架",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091904170007,
            level: 0.0,
            history_highest_level: 7.35,
            name: "佳艺幼儿园",
            longitude: 120.60089432575978,
            latitude: 28.011508553271284,
            acquisition_time: "2021-03-15 13:37:26",
            water_level: 0.0,
            historical_highest_water_level: 7.35,
            voltage: 13.15,
            battery: 13400,
            address: "浙江省温州市鹿城区佳艺幼儿园",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091904170008,
            level: 0.0,
            history_highest_level: 15.09,
            name: "康复中心",
            longitude: 120.59790257767692,
            latitude: 28.013898549488935,
            acquisition_time: "",
            water_level: 0,
            historical_highest_water_level: "",
            voltage: "",
            battery: "",
            address: "浙江省温州市鹿城区鹿城康复中心",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091904170009,
            level: 0.0,
            history_highest_level: 15.09,
            name: "双金路口",
            longitude: 120.5919692058463,
            latitude: 28.0319107886598,
            acquisition_time: "",
            water_level: 0,
            historical_highest_water_level: "",
            voltage: "",
            battery: "",
            address: "浙江省温州市鹿城区鞋都大道双金路口",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "鹿城区",
            district_code: 330302,
            street: "双屿街道",
            street_code: 330302027,
            id: 810091904170010,
            level: 0.0,
            history_highest_level: 15.09,
            name: "康华路",
            longitude: 120.60201290593268,
            latitude: 28.014093888017584,
            acquisition_time: "",
            water_level: 0,
            historical_highest_water_level: 3.46,
            voltage: "",
            battery: "",
            address: "浙江省温州市鹿城区康华路",
            name2: "",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "龙湾区",
            district_code: 330303,
            street: "状元街道",
            street_code: 330303006,
            id: 810091904170080,
            level: 0.0,
            history_highest_level: 15.09,
            name: "海鹰游泳馆",
            longitude: 120.76900179025832,
            latitude: 27.969254214131595,
            acquisition_time: "2021-03-16 10:02:50",
            water_level: 0.0,
            historical_highest_water_level: 15.09,
            voltage: 12.59,
            battery: 13400,
            address: "浙江省温州市龙湾区海鹰游泳馆",
            name2: "wzfxb_0080",
        },
        {
            city: "温州市",
            city_code: 3303,
            district: "龙湾区",
            district_code: 330303,
            street: "海滨街道",
            street_code: 330303003,
            id: 810091904170081,
            level: 0.0,
            history_highest_level: 7.84,
            name: "富农生物科技有限公司",
            longitude: 120.8434516611552,
            latitude: 27.946747755346561,
            acquisition_time: "2021-03-15 09:44:14",
            water_level: 0.0,
            historical_highest_water_level: 7.84,
            voltage: 13.16,
            battery: 13400,
            address: "浙江省温州市龙湾区富农生物科技有限公司",
            name2: "wzfxb_0081",
        },
        ],
        riskPointList: [{
            name: "海塘风险",
            longitude: 120.64540667132022,
            latitude: 27.52536351903531,
            riskType: 1,
        },
        {
            name: "山洪风险",
            longitude: 120.08555508090613,
            latitude: 27.808407278826188,
            riskType: 1,
        },
        {
            name: "堤防风险",
            longitude: 120.9491826107734,
            latitude: 27.934466171474714,
            riskType: 2,
        },
        {
            name: "淹没风险",
            longitude: 120.56128142998358,
            latitude: 28.056249042436676,
            riskType: 3,
        },
        ],
        modelInfo: {},
        receiveWater: [
            { resName: "仰义（斜井）", tm: "2021/3/16 23:15", rz: "52.41", limitLevel: "68.2", amount: "15790" },
            { resName: "仙居", tm: "2021/3/16 23:15", rz: "248.92", limitLevel: "256", amount: "7080" },
            { resName: "北溪", tm: "2021/3/16 23:15", rz: "396.46", limitLevel: "408", amount: "11540" },
            { resName: "百丈漈水库(北斗)", tm: "2021/3/16 23:15", rz: "619.16", limitLevel: "652", amount: "32840" },
            { resName: "三插溪水位", tm: "2021/3/16 23:15", rz: "321.11", limitLevel: "342", amount: "20890" },
            { resName: "高岭头水库二级水库", tm: "2021/3/16 23:15", rz: "311.71", limitLevel: "327", amount: "15290" },
            { resName: "双涧溪", tm: "2021/3/16 23:15", rz: "382.58", limitLevel: "389", amount: "6420" },
            { resName: "吴家园", tm: "2021/3/16 23:15", rz: "31", limitLevel: "44.5", amount: "13500" },
            { resName: "泽雅水库", tm: "2021/3/16 23:15", rz: "79.3", limitLevel: "104.97", amount: "25670" },
            { resName: "桥墩坝上", tm: "2021/3/16 23:15", rz: "32.27", limitLevel: "52.19", amount: "19920" },
            { resName: "赵山渡坝上水位", tm: "2021/3/16 23:15", rz: "21.78", limitLevel: "22", amount: "220" },
            { resName: "珊溪水库坝上水位", tm: "2021/3/16 23:15", rz: "125.68", limitLevel: "142.04", amount: "16360" },
            { resName: "白石水位", tm: "2021/3/16 23:15", rz: "14.38", limitLevel: "31", amount: "16620" },
            { resName: "顺溪水库", tm: "2021/3/16 23:15", rz: "153.85", limitLevel: "175", amount: "21150" },
            { resName: "钟前", tm: "2021/3/16 23:15", rz: "104.49", limitLevel: "120.16", amount: "15670" },
            { resName: "福溪水位", tm: "2021/3/16 23:15", rz: "214.1", limitLevel: "226.31", amount: "12210" },
            { resName: "金溪水库", tm: "2021/3/16 23:15", rz: "249.86", limitLevel: "260", amount: "10140" },
            { resName: "淡溪水库", tm: "2021/3/16 23:15", rz: "27.13", limitLevel: "40.76", amount: "13630" },
            { resName: "高岭头水库（压力北斗）", tm: "2021/3/16 23:15", rz: "736.64", limitLevel: "792", amount: "55360" },
        ],
        oneHourRain: [], //实时1小时雨量
        threeHourRain: [], //实时3小时雨量
        sixHourRain: [], //实时6小时雨量
        oneHourForecastRain: [], //预报1小时雨量
        threeHourForecastRain: [], //预报3小时雨量
        sixHourForecastRain: [], //预报6小时雨量
    },
    mutations: {
        // 设置地图选中菜单
        setMapMenu(state, mapMenu) {
            state.mapMenu = mapMenu;
        },
        // 设置图层列表
        setLayerList(state, list) {
            state.layerList = list;
        },
        setTyphoonId(state, id) {
            state.typhoonId = id;
        },
        setTyphoonRelatedProject(state, data) {
            state.typhoonRelatedList = data;
        },
        setUserTree(state, list) {
            state.userTree = list;
        },
        BIGSCREEN_W_LIST(state, list) {
            state.warehouseList = list;
        },
        BIGSCREEN_T_LIST(state, list) {
            state.teamList = list;
        },
        BIGSCREEN_RAIN_LIST(state, list) {
            state.rainList = list;
        },
        BIGSCREEN_WATER_LIST(state, list) {
            state.waterList = list;
        },
        TEAM_DISPATCH_LIST(state, list) {
            state.teamDispatchList = list;
        },
        WAREHOUSE_DISPATCH_LIST(state, list) {
            state.warehouseDispatchList = list;
        },
        SET_MODEL_INFO(state, info) {
            state.modelInfo = info;
        },
        SET_ONE_RAIN(state, data) {
            state.oneHourRain = data;
        },
        SET_THREE_RAIN(state, data) {
            state.threeHourRain = data;
        },
        SET_SIX_RAIN(state, data) {
            state.sixHourRain = data;
        },
        SET_FORECAST_ONE_RAIN(state, data) {
            state.oneHourForecastRain = data;
        },
        SET_FORECAST_THREE_RAIN(state, data) {
            state.threeHourForecastRain = data;
        },
        SET_FORECAST_SIX_RAIN(state, data) {
            state.sixHourForecastRain = data;
        },
    },
    actions: {
        // 抢险支持
        getRainList,
        getWaterList,
        getRealRainList,
    },
    modules: {},
    getters: {
        mapMenu: (state) => state.mapMenu
    }
});