import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/waterArea';

// 水域动态
export const WaterRegulationApi = {
    // 清四乱-四乱类型统计
    proTypeStatistics: function () {
        return http.get(pre + "/waterSupervise/proTypeStatistics");
    },
    // 查询水域问题信息
    queryWaterProblem: function (data) {
        return http.post(pre + "/waterDynManage/queryWaterProblem", data);
    },
    // 查询整改后图片
    queryRectifyPic: function (data) {
        return http.get(pre + "/waterDynManage/queryRectifyPic", data);
    },
    // 查询处理记录信息
    queryRecord: function (data) {
        return http.get(pre + "/waterDynManage/queryRecord", data);
    },
}