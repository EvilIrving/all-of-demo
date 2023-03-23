// import axios from '../../config/httpRequset'
import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/plan-digitization';
const pre1 = "/big-screen";

export const programmeApi = {
    /**
     *  统计分析
     */
    // 分页查找
    loadPage: function(data) {
        return http.post(pre + "/busSubjectResearchType/loadPage", data);
    },
    // 统计总览 - > 成果总览
    overviewAchievements: function(data) {
        return http.post(pre + "/busSubjectResearch/overviewAchievements", data);
    },
    // 研究类别统计->经费统计
    expenditureStatistics: function(data) {
        return http.post(pre + "/busSubjectResearch/expenditureStatistics", data);
    },
    // 研究类别统计 - > 成果统计
    achievementStatistics: function(data) {
        return http.post(pre + "/busSubjectResearch/achievementStatistics", data);
    },
    // 承担单位研究统计
    researchStaticUnit: function(data) {
        return http.post(pre + "/busSubjectResearch/researchStaticUnit", data);
    },
    // 统计总览 - > 成果转化
    achievementTransformation: function(data) {
        return http.post(pre + "/busSubjectResearch/achievementTransformation", data);
    },
    // 趋势分析 - > 成果总览
    trendOverviewAchievements: function(data) {
        return http.post(pre + "/busSubjectResearch/trendOverviewAchievements", data);
    },
    // 趋势分析 - > 成果分析
    trendAchievementAnalysis: function(data) {
        return http.post(pre + "/busSubjectResearch/trendAchievementAnalysis", data);
    },
    // 趋势分析->经费分析
    trendExpenditureAnalysis: function(data) {
        return http.post(pre + "/busSubjectResearch/trendExpenditureAnalysis", data);
    },
    // c位统计
    statsCwindowOfWaterResearch: function(data) {
        return http.get(pre1 + "/waterDevPlan/statsCwindowOfWaterResearch", data);
    },
    // 水研究大屏组件统计
    statsOfWaterResearch: function(data) {
        return http.get(pre1 + "/waterDevPlan/statsOfWaterResearch", data);
    },
    // 分页查找
    busSubLoadPage: function(data) {
        return http.post(pre + "/busSubjectResearch/loadPage", data);
    },
    // 山洪报警列表
    flashFloodWarnList: function(data) {
        return http.post(pre1 + "/waterDisasterPrevention/flashFloodWarnList", data);
    },
    // 报警地图落点
    warnMapPoints: function(data) {
        return http.get(pre1 + "/waterDisasterPrevention/warnMapPoints", data);
    },
    // 报警列表
    warnMapPointStats: function(data) {
        return http.get("big-screen/waterDisasterPrevention/warnMapPointStats", data);
    }
}