/*
 * @Author: wanxin
 * @LastEditTime: 2022-05-17 17:27:59
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\api\defenseDispatch.js
 */
import axios from "../config/httpRequset";
const pre = '/flood-protect';

// 灾害防御调度
export const PersonLiableApi = {
    // 山洪灾害责任人列表
    pvPchargeList: function (data) {
        return axios.post(pre + "/torrentialFlood/pvPchargeList", data);
    },
    // 新增或编辑
    saveOrUpdate: function (data) {
        return axios.post(pre + "/torrentialFlood/pchargeSaveOrUpdate", data);
    },
    // 删除
    delete: function (data) {
        return axios.get(pre + "/torrentialFlood/pchargeDelete", data);
    },
};


export const FishingBoatNameListApi = {
    // 渔船信息 - 列表
    boatList: function (data) {
        return axios.post(pre + "/responsible/boatList", data);
    },
    // 新增或编辑
    saveOrUpdate: function (data) {
        return axios.post(pre + "/responsible/boatSaveOrUpdate", data);
    },
    // 删除
    delete: function (data) {
        return axios.get(pre + "/responsible/boatDelete", data);
    },
};

export const LargeGrainGrowersNameListApi = {
    // 种粮大户 - 列表
    growGrainList: function (data) {
        return axios.post(pre + "/responsible/growGrainList", data);
    },
    // 新增或编辑
    saveOrUpdate: function (data) {
        return axios.post(pre + "/responsible/growGrainSaveOrUpdate", data);
    },
    // 删除
    delete: function (data) {
        return axios.get(pre + "/responsible/growGrainDelete", data);
    },
};

export const defenseObjectApi = {
    // 防御对象清单 - 列表
    defenseObjectList: function (data) {
        return axios.post(pre + "/defenObject/list", data);
    },
    // 新增或编辑
    saveOrUpdate: function (data) {
        return axios.post(pre + "/defenObject/saveOrUpdate", data);
    },
    // 删除
    delete: function (data) {
        return axios.get(pre + "/defenObject/delete", data);
    },
};