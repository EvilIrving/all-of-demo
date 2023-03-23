import { http } from "../../../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/flood-protect";
const pre1 = "/graph";
const pre2 = "/project-manage";
//降雨分析
export const rainApi = {
  //实时雨情
  realRain: data => {
    return http.post("/graph/waterAndRain/rainfall", data);
  },
  //地图点位查看详情-实时降雨图
  rainPop: data => {
    return http.post("/graph/waterAndRain/hourRainfall", data);
  },
  //实时雨量
  realRainFall: data => {
    return http.get("/flood-protect/getAllAdcdRealtimeRainfall", data);
  },
  //历史极值
  histroy: data => {
    return http.get("/flood-protect/rainfallAnalysis/getRainfallExtremum", data);
  },
  //count
  count: data => {
    return http.get("/flood-protect/rainfallAnalysis/getRainWarningStationNum", data);
  },
  //查询各个雨量站累计降雨值
  getPpStNumStatistic: data => {
    return http.get("/flood-protect/rainfallAnalysis/getPpStNumStatistic", data);
  },
  //查询温州雨量站极值
  getHistoricRainfallExtremum: () => {
    return http.get("/flood-protect/rainfallAnalysis/getHistoricRainfallExtremum");
  },
  getPointRainfall: function () {
    return http.get("/flood-protect/getCollectPointRainfall");
  },
  rainfallPredict: function(data){
    return http.post("/meteorological-visualization/weatherPredict/rainfallPredict",data);
  },
  //雨量top10
  rainfallTop10:function(data){
    return http.get("/flood-protect/waterAndRain/rainfallTop10",data);
  },
  //1/3/6小时预报的面雨量
  getSurfaceRainfall:function(){
    return http.get("/meteorological-visualization/weatherPredict/surfaceRainfall");
  },
  //大中型水库水位
  reservoirWaterAndRain:function(){
    return http.get("/flood-protect/waterAndRain/reservoirWaterAndRain");
  },
  //汛限水位水库top10
  getTopNRes:function(index){
    return http.get("/flood-protect/waterAndRain/topNRes?index"+index);
  },
	//水库超汛限个数
	getBeyondNumber:function(){
    return http.get("/flood-protect/waterAndRain/beyondNumber");
  },
	//雨量1小时超警个数
	getSuperRainNumber:function(){
    return http.get("/flood-protect/waterAndRain/superRainNumber");
  },
};
//水情分析
export const waterApi = {
	//地图点位展示
	mapPoint: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/floatWinWaterLevel", data);
	},
	history: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/hisHighestWaterLevel", data);
	},
	//count
	count: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/overWarnStatistics", data);
	},
	//水位分析
	getAllStWatLev: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/getAllStWatLev", data);
	},
	getFloatWinWaterLevel: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/floatWinWaterLevel", data);
	},
	//查询平原/流域的实时水位
	getAllStWatLevByType: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/getAllStWatLevByType", data);
	},
	//查询水位历史极值表中的极值数据
	getHistoricWaterLevelExtremum: (data) => {
		return http.get("/flood-protect/waterLevelAnalyse/getHistoricWaterLevelExtremum", data);
	},
};

//工程分析
export const workApi = {
	//地图点位展示
	mapPoint: (data) => {
		return http.post("/graph/waterAndRain/waterStations", data);
	},
	//地图点位查看详情-折线图
	pointDt: (data) => {
		return http.post("/graph/waterAndRain/waterLevels", data);
	},
	//可蓄水量/纳蓄能力统计
	count: (data) => {
		return http.get("/flood-protect/floodProject/countStorage", data);
	},
};

//降雨分析
export const precipitation = {
	//重点关注
	getCollectPointRainfall: (data) => {
		return http.get(pre + "/rainfallAnalysis/getCollectPointRainfall", data);
	},
	//水库
	getResPointRainfall: (data) => {
		return http.get(pre + "/rainfallAnalysis/getResPointRainfall", data);
	},
	//水位分析 平原水位
	watLevOfPlainBasInnerRiv: (data) => {
		return http.get(pre + "/waterLevelAnalyse/watLevOfPlainBasInnerRiv", data);
	},
	//工程分析 水闸工情
	listWaga: (data) => {
		return http.post(pre + "/floodProject/listWaga", data);
	},
	//水库纳蓄能力
	countStorage: (data) => {
		return http.get(pre + "/floodProject/countStorage", data);
	},
	//水闸排水量
	floodProjectListWagaProj: (data) => {
		return http.get(pre + "/floodProject/listWagaProj", data);
	},
	//泵站排水量
	listPustProj: (data) => {
		return http.get(pre + "/floodProject/listPustProj", data);
	},
	//潮位
	waterStations: (data) => {
		return http.post(pre1 + "/waterAndRain/waterStations", data);
	},
	//内涝积水点
	newestFloodPoint: (data) => {
		return http.get(pre + "/resInfo/newestFloodPoint", data);
	},
	// 水位弹框
	// waterStationInfo: function(data) {
	// 	return axios.post(pre1 + "/waterAndRain/waterLevels", data);
	// },
	// 视频列表
	getVideoList: function(data) {
		return http.get(pre2 + "/projectManage/projectCameras/" + data);
	  },
};
