/*
 * @Author: wanxin
 * @LastEditTime: 2022-05-31 16:55:14
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\api\occupyApprove.js
 */
import axios from "../config/httpRequset";
const pre = '/project-manage';

// 水域报表
export const watersReportApi = {
    // 水域报表列表
    list: function (data) {
        return axios.post(pre + "/watersReport/list", data);
    },
    // 水域报表新增或修改
    saveOrUpdate: function (data) {
        return axios.post(pre + "/watersReport/saveOrUpdate", data);
    },
    // 水域报表删除
    del: function (data) {
        return axios.post(pre + "/watersReport/del", data);
    },
    // 违法占用列表
    occupationList: function (data) {
        return axios.post(pre + "/watersReportInfo/list", data);
    },
    // 违法占用报表新增或修改
    occupationSaveOrUpdate: function (data) {
        return axios.post(pre + "/watersReportInfo/saveOrUpdate", data);
    },
    // 违法占用表删除
    occupationDel: function (data) {
        return axios.post(pre + "/watersReportInfo/del", data);
    },
    // 水域变化列表
    changeList: function (data) {
        return axios.post(pre + "/watersChange/list", data);
    },
    // 水域变化新增或修改
    changeSaveOrUpdate: function (data) {
        return axios.post(pre + "/watersChange/saveOrUpdate", data);
    },
    // 水域变化删除
    changeDel: function (data) {
        return axios.post(pre + "/watersChange/del", data);
    },
};

//水域审批占用
export const hydrographApi = {
    occupyList:function (data){
        return axios.post('river-service/attRiverInfo/list',data)
    }
}