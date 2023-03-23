import axios from '../config/httpRequset'
const pre = '/waterArea';

// 水域动态
export const WaterRegulationApi = {
    // 清四乱-四乱类型统计
    proTypeStatistics: function () {
        return axios.get(pre + "/waterSupervise/proTypeStatistics");
    },
    // 查询水域问题信息
    queryWaterProblem: function (data) {
        return axios.post(pre + "/waterDynManage/queryWaterProblem", data);
    },
    // 查询整改后图片
    queryRectifyPic: function (data) {
        return axios.get(pre + "/waterDynManage/queryRectifyPic", data);
    },
    // 查询处理记录信息
    queryRecord: function (data) {
        return axios.get(pre + "/waterDynManage/queryRecord", data);
    },
}