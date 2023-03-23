import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/big-screen";
const pre1 = "/graph";

//第四版防汛减灾大屏右侧边栏接口
export const disasterPreventionApi = {
	// 防御复盘场景下的接口
	// 潮位
	tideReplayCurve: function(data) {
		return http.get("big-screen/waterDisasterPrevention/tideReplayCurve", data);
	},
	//河网
	rivernetReplayStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/rivernetReplayStats", data);
	},
	//工程行政区划数量统计
	getstatisticalDiskNumByArea: function(data) {
		return http.get("project-manage/newProjectManage/statisticalDiskNumByArea", data);
	},
	//工程堤防数量统计
	getstatisticalProjectNum: function(data) {
		return http.get("project-manage/newProjectManage/statisticalProjectNum", data);
	},
	//工程堤防等级统计
	projectEngscalStatic: function(data) {
		return http.post("project-manage/newProjectManage/countProject", data);
	},
	//堤防c位
	getcAStatisticalProject: function(data) {
		return http.get("project-manage/newProjectManage/cAStatisticalProject", data);
	},
	// 堤防
	dikeproject: function(data) {
		return http.post("project-manage/newProjectManage/dikeProject", data);
	},
	// 泵站
	pustProject: function(data) {
		return http.post("project-manage/newProjectManage/pustProject", data);
	},
	// 山塘
	mnpdProject: function(data) {
		return http.post("project-manage/newProjectManage/mnpdProject", data);
	},
	//室内出租车--报销次数
	gettaxiExpenseNumber: function(data) {
		return http.get("/office-automation/taxiExpense/taxiExpenseNumber", data);
	},
	//室内出租车--报销金额
	gettaxiExpenseMoney: function(data) {
		return http.get("/office-automation/taxiExpense/taxiExpenseMoney", data);
	},
	//市内出租车--c位统计
	gettaxiExpenseCStatist: function(data) {
		return http.get("/office-automation/taxiExpense/taxiExpenseCStatist", data);
	},
	// 其他报销
	//会议培训
	//水库
	rsvrReplayStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/rsvrReplayStats", data);
	},
	//水闸
	wrpWagaReplayStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/wrpWagaReplayStats", data);
	},
	//山洪点位弹框
	flashFloodDetails: function(data) {
		return http.post("big-screen/waterDisasterPrevention/flashFloodDetails", data);
	},
	//第二场景河网
	NetworkStaStats: function() {
		return http.get("big-screen/waterDisasterPrevention/riverNetworkStaStats?type=2");
	},
	//大中型水库
	rsvrProjDrainageStats: function(data) {
		return http.get(pre + "/waterDisasterPrevention/rsvrProjDrainageStats", data);
	},
	//海塘统计统计
	seawallProjList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/seawallProjList", data);
	},
	//海塘分页统计
	pageSeawall: function(data) {
		return http.get("project-manage/seawall/pageSeawall", data);
	},
	//水库一级统计
	rsvrProjStats1: function(data) {
		return http.get("big-screen/waterDisasterPrevention/rsvrProjStats1", data);
	},
	//水库二级统计
	rsvrProjStats2: function(data) {
		return http.get("big-screen/waterDisasterPrevention/rsvrProjStats2", data);
	},
	//水库弹窗
	rsvrProjList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/rsvrProjList", data);
	},
	//大中型水库可分页
	rsvrUnitList: function(data) {
		return http.postJson("project-manage/rsvr/rsvrUnitList", data);
	},
	// 水库统计
	stats4RsvrUnitList: function(data) {
		return http.postJson("project-manage/rsvr/stats4RsvrUnitList", data);
	},
	//水闸
	wagaProjStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/wagaProjStats", data);
	},
	// 水闸工程列表接口
	wagaProjList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/wagaProjList", data);
	},
	// //积水点
	// wagaProjList: function(data) {
	// 	return http.get("big-screen/waterDisasterPrevention/wagaProjList", data);
	// },
	//河网
	riverNetworkStaStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/riverNetworkStaStats", data);
	},
	//潮位
	tideCurve: function(data) {
		return http.get("big-screen/waterDisasterPrevention/tideCurve", data);
	},
	// 未来三天文潮表格
	astroTideTable: function(data) {
		return http.get("big-screen/waterDisasterPrevention/astroTideTable", data);
	},
	// 海塘统计
	seawallProjStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/seawallProjStats", data);
	},
	//风险隐患
	projRiskStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/projRiskStats", data);
	},
	// 病险工程
	dilapProjStats: function(data) {
		return http.get("big-screen/waterDisasterPrevention/dilapProjStats", data);
	},
	// 获取行政区划
	getArea: function(data) {
		return http.post(pre1 + "/adcd", data);
	},
	//河网列表
	riverNetworkList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/riverNetworkList", data);
	},
	//潮位详情接口列表
	tideList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/tideList", data);
	},
	//病例工程
	projRiskList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/projRiskList", data);
	},
	//病例工程2
	projectManageProjRiskList: function(data) {
		return http.get("project-manage/projectManage/projRiskList", data);
	},
	//短临预报降雨
	rainfallPredictGrid: function(data) {
		return http.post(pre + "/waterDisasterPrevention/rainfallPredictGrid", data);
	},
	//实时降雨
	grandTotalRainfallGrid: function(data) {
		return http.post(pre + "/waterDisasterPrevention/grandTotalRainfallGrid", data);
	},
	//病险工程
	dilapProjList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/dilapProjList", data);
	},
	/**
	 * 点位list
	 */
	rightMapPoints: function(data) {
		return http.get("/big-screen/waterDisasterPrevention/rightMapPoints", data);
	},
	/**
	 * 风险类型统计
	 */
	riskTypeStatistics: function(data) {
		return http.post("/big-screen/busRiskList/riskTypeStatistics", data);
	},
	/**
	 * 隐患点销号情况
	 */
	cancellationHiddenDangerStatic: function(data) {
		return http.post("/big-screen/riskDetailsHiddendanger/cancellationHiddenDangerStatic", data);
	},
	/**
	 * 薄弱点销号情况统计
	 */
	cancellationWeakPointsStatic: function(data) {
		return http.post("/big-screen/riskDetailsHighrisk/cancellationWeakPointsStatic", data);
	},

	/**
	 * 风险清单list
	 */
	busRiskListPage: function(data) {
		return http.post("/big-screen/busRiskList/busRiskListPage", data);
	},

	/**
	 * 根据sbId查询隐患点详情
	 */
	findDetailsHiddenDangerBySbId: function(data) {
		return http.post("/big-screen/riskDetailsHiddendanger/findDetailsHiddenDangerBySbId", data);
	},

	/**
	 * 根据sbId查询高风险点/薄弱点详情
	 */
	findDetailsHighriskBySbId: function(data) {
		return http.post("/big-screen/riskDetailsHighrisk/findDetailsHighriskBySbId", data);
	},

	// 第三场景 详情接口
	//潮位分析
	waterAnalysisList: function(data) {
		return http.get("big-screen/waterDisasterPrevention/tideReplayStats", data);
	},
	// 积水列表和落点
	waterLoggingList: function(data) {
		return http.get("/big-screen/waterDisasterPrevention/waterLoggingList", data);
	},
	waterLoggingListNew: data => {
		return http.get("/flood-protect/waterLogging/pageFloodPoint", data);
	},
	waterLoggingStatsNew: data => {
		return http.get("/flood-protect/waterLogging/stats4PageFloodPoint", data);
	},
	// 积水统计
	waterLoggingStats: function(data) {
		return http.get("/big-screen/waterDisasterPrevention/waterLoggingStats", data);
	},
	// 按行政区划统计摄像头
	statsCameraByAdcd: function(data) {
		return http.get("/project-manage/cameraOps/statsCameraByAdcd", data);
	},
	// 按工程统计摄像头
	statsCameraByProject: function(data) {
		return http.get("/project-manage/cameraOps/statsCameraByProject", data);
	},
	// 统计摄像头在线离线状态
	countCameraStatus: function(data) {
		return http.get("/project-manage/cameraOps/countCameraStatus", data);
	},
	// 查询所有的摄像头
	selectCamera: function(data) {
		return http.get("/project-manage/cameraOps/selectCamera", data);
	},
	// 查询所有摄像头列表
	listCamera: function(data) {
		return http.get("/project-manage/cameraOps/pageCamera", data);
	},
	// 查询所有摄像头列表统计
	stats4PageCamera: function(data) {
		return http.get("/project-manage/cameraOps/stats4PageCamera", data);
	},
	// 防汛队伍根据行政区划划分-人员数量
	staticPeopleNumByArea: function(data) {
		return http.get("/flood-protect/team/staticPeopleNumByArea", data);
	},
	// 防汛队伍根据行政区划划分 - 队伍数量
	teamGroupByArea: function(data) {
		return http.get("/flood-protect/team/teamGroupByArea", data);
	},
	// 防汛队伍根据类型划分 - 人员数量
	staticPeopleNumByType: function(data) {
		return http.get("/flood-protect/team/staticPeopleNumByType", data);
	},
	// 防汛队伍根据类型划分 - 队伍数量
	teamGroupByType: function(data) {
		return http.get("/flood-protect/team/teamGroupByType", data);
	},
	// 仓库根据行政区划分类-仓库面积
	staticAcreageByArea: function(data) {
		return http.get("/flood-protect/warehouse/staticAcreageByArea", data);
	},
	// 仓库根据行政区划分类-物资价值
	staticMaterialPriceByArea: function(data) {
		return http.get("/flood-protect/warehouse/staticMaterialPriceByArea", data);
	},
	// 仓库根据行政区划分类-仓库数量
	warehousesGroupByArea: function(data) {
		return http.get("/flood-protect/warehouse/warehousesGroupByArea", data);
	},
	// 仓库根据仓库类型分类统计-仓库面积
	staticAcreageByType: function(data) {
		return http.get("/flood-protect/warehouse/staticAcreageByType", data);
	},
	// 仓库根据仓库类型分类统计-物资价值
	staticMaterialPriceByType: function(data) {
		return http.get("/flood-protect/warehouse/staticMaterialPriceByType", data);
	},
	// 仓库根据仓库类型分类统计-仓库数量
	warehousesGroupByType: function(data) {
		return http.get("/flood-protect/warehouse/warehousesGroupByType", data);
	},
	// 防汛仓库查询
	warehouses: function(data) {
		return http.post("/flood-protect/warehouse/warehouses", data);
	},
	// 防汛队伍查询
	teams: function(data) {
		return http.post("/flood-protect/team/teams", data);
	},
	warehousesList: function(data) {
		return http.post("/big-screen/waterDisasterPrevention/listWarehouses", data);
	},
	teamsList: function(data) {
		return http.post("/big-screen/waterDisasterPrevention/listTeam", data);
	},
	//  智能分析发送短信
	wareHouseSendSms: function(data) {
		return http.post("flood-protect/warehouse/wareHouseSendSms", data);
	},
	// 洪水风险图列表
	floodRiskList: function(data) {
		return http.post("/hydrology-digitization/FloodRiskDisasterData/loadPage", data);
	}
};
