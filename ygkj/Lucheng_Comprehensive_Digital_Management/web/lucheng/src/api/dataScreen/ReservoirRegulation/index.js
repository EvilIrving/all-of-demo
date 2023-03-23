import { http } from "@/config/httpRequset";
const pre = "/project-manage";

export const reservoirRegulationApi = {
	//三个责任人详细信息
	getThreeCharge: (code) => {
		return http.get(pre + "/engineeringSupervision/threeCharge?projectCode=" + code);
	},
	//向对应责任人发送消息提醒
	sendMsg: (data) => {
		return http.post(pre + "/ding/sendWarn", data);
	},
};
