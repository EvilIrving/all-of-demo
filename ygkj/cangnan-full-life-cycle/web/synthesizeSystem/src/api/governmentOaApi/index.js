/*
 * @Autor: ysl
 * @Date: 2021-08-09 17:01:54
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-14 09:27:59
 * @Description: 水政务协同接口
 * @FilePath: /synthesizeSystem/src/api/governmentOaApi/index.js
 */
import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/office-automation";
const pre1 = "/big-screen";

export const oaApi = {
	// 出差管理
	getOaOverView: function (data) {
		return http.post(pre + "/businessApply/overView", data);
	},
	// 合同管理-合同金额
	contractMoneyComponents: function (data) {
		return http.get(pre + "/contract/contractMoneyComponents", data);
	},
	// 合同管理-合同数量
	contractQuantityComponents: function (data) {
		return http.get(pre + "/contract/contractQuantityComponents", data);
	},
	// 合同管理-合同C位统计
	cStatisticalComponents: function (data) {
		return http.get(pre + "/contract/cStatisticalComponents", data);
	},
	// 合同管理-报销金额
	reimbursementMoneyComponents: function (data) {
		return http.get(pre + "/contract/reimbursementMoneyComponents", data);
	},
	// 合同管理-列表
	contractLoadPage: function (data) {
		return http.post(pre + "/contract/loadPage", data);
	},
	// 公务接待-接待人次
	receptionVisitorsComponents: function (data) {
		return http.get(pre + "/receptionApply/receptionVisitorsComponents", data);
	},
	// 公务接待-陪同人次
	accompaniedPeopleComponents: function (data) {
		return http.get(pre + "/receptionApply/accompaniedPeopleComponents", data);
	},
	// 公务接待-合同C位统计
	receptionApplyCStatisticalComponents: function (data) {
		return http.get(pre + "/receptionApply/cStatisticalComponents", data);
	},
	// 公务接待-报销金额
	reimbursementAmountComponents: function (data) {
		return http.get(pre + "/receptionApply/reimbursementAmountComponents", data);
	},
	// 公务接待-列表
	detailedTableComponents: function (data) {
		return http.get(pre + "/receptionApply/detailedTableComponents", data);
	},
	// 资产管理
	stats4OaAssets: function (data) {
		return http.get(pre1 + "/watGovCoordinate/stats4OaAssets", data);
	},
	// 政务内部职能管理-C位
	proposalCStati: function (data) {
		return http.get(pre + "/proposal/proposalCStati", data);
	},
	// 政务内部职能管理-办件统计
	proposalOfficeStati: function (data) {
		return http.get(pre + "/proposal/proposalOfficeStati", data);
	},
	// 政务内部职能管理-文件统计
	proposalFileStati: function (data) {
		return http.get(pre + "/proposal/proposalFileStati", data);
	},
	// 政务内部职能管理-详表
	proposalDetailedTable: function (data) {
		return http.get(pre + "/proposal/proposalDetailedTable", data);
	},
	//市内出租车--报销金额
	gettaxiExpenseMoney: function (data) {
		return http.get("/office-automation/taxiExpense/taxiExpenseMoney", data)
	},
	//市内出租车--c位统计
	taxiExpenseCStatist: function (data) {
		return http.get("/office-automation/taxiExpense/taxiExpenseCStatist", data)
	},
	//市内出租车--出行方式
	gettaxiExpenseWay: function (data) {
		return http.get("/office-automation/taxiExpense/taxiExpenseWay", data)
	},
	//市内出租车--报销次数
	taxiExpenseNumber: function (data) {
		return http.get(pre + "/taxiExpense/taxiExpenseNumber", data)
	},
	//市内出租车--详表
	taxiExpenseDetailedTable: function (data) {
		return http.get(pre + "/taxiExpense/taxiExpenseDetailedTable", data)
	},
	//公务用车--报销金额
	statisExpenseMoney: function (data) {
		return http.get(pre + "/carApply/statisExpenseMoney", data)
	},
	//公务用车--c位统计
	officialCarCStatis: function (data) {
		return http.get(pre + "/carApply/cStatis", data)
	},
	//公务用车--出行方式
	officialCarStatisway: function (data) {
		return http.get(pre + "/carApply/statisway", data)
	},
	//公务用车--报销次数
	statisExpenseAccount: function (data) {
		return http.get(pre + "/carApply/statisExpenseAccount", data)
	},
	//公务用车--详表
	statisDetailedTable: function (data) {
		return http.get(pre + "/carApply/statisDetailedTable", data)
	},
	//其他报销--报销金额
	otherExpenseMoney: function (data) {
		return http.get(pre + "/other/otherExpenseMoney", data)
	},
	//其他报销--c位统计
	otherExpenseCStatis: function (data) {
		return http.get(pre + "/other/otherExpenseCStatis", data)
	},
	//其他报销--报销次数
	otherExpenseNumber: function (data) {
		return http.get(pre + "/other/otherExpenseNumber", data)
	},
	//其他报销--详表
	otherExpenseDetailedTable: function (data) {
		return http.get(pre + "/other/otherExpenseDetailedTable", data)
	},
	//会议培训--报销金额
	meetTrainReimbursementAmount: function (data) {
		return http.get(pre + "/meetExpense/meetTrainReimbursementAmount", data)
	},
	//会议培训--c位统计
	meetTrainCStati: function (data) {
		return http.get(pre + "/meetExpense/meetTrainCStati", data)
	},
	//会议培训--支出方式
	meetTrainSpendType: function (data) {
		return http.get(pre + "/meetExpense/meetTrainSpendType", data)
	},
	//会议培训--会议培训次数
	meetTrainNumber: function (data) {
		return http.get(pre + "/meetExpense/meetTrainNumber", data)
	},
	//会议培训--详表
	meetTrainDetailedTable: function (data) {
		return http.get(pre + "/meetExpense/meetTrainDetailedTable", data)
	},

	//出差申请--总览
	businessApplyOverViewComponent: function (data) {
		return http.post(pre + "/businessApply/overView", data)
	},
	//公务接待--总览
	receptionApplyOverViewComponent: function (data) {
		return http.get(pre + "/receptionApply/overViewComponents", data)
	},
	//其他报销--总览
	otherOverViewComponent: function (data) {
		return http.get(pre + "/other/overView", data)
	},
	//市内出租车--总览
	taxiExpenseOverViewComponent: function (data) {
		return http.get(pre + "/taxiExpense/overViewComponent", data)
	},
	//会议培训--总览
	meetOverViewComponent: function (data) {
		return http.get(pre + "/meet/overViewComponent", data)
	},
	//公务用车--总览
	carApplyOverViewComponent: function (data) {
		return http.get(pre + "/carApply/overViewComponent", data)
	},
	//政务内部职能管理--总览
	proposalOverViewComponent: function (data) {
		return http.get(pre + "/proposal/overViewComponent", data)
	},
	//合同管理--总览
	contractOverViewComponent: function (data) {
		return http.get(pre + "/contract/overViewComponents", data)
	},
}