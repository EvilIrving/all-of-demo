import { http } from "@/config/httpRequset";
const pre = "/digitization";

export const collectApi = {
	getMyCollectList: function(data) {
		return http.post(pre + "/record/mine", data);
	},
	getMyJobs: function(data){
		return http.post("/sys-role-permission/sys/permission/jobs", data);
	}
};
