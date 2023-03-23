/*
 * @Author: dyn
 * @Date: 2022-05-13 14:00:40
 * @LastEditTime: 2022-05-13 16:30:55
 * @LastEditors: dyn
 * @Description: 预案
 * @FilePath: \lucheng\src\api\reservePlan.js
 */
// 预案
import { http } from "@/config/httpRequset.js";

// 预案列表
export function getReservePlan(data) {
    return http.post("/big-screen/floodControlDispatching/plan", data);
}
// 预案监管
export function planSupervision(data) {
    return http.post("/big-screen/floodControlDispatching/planSupervision", data);
}


