import { http } from "@/config/httpRequset";
const pre = "/warning-service";

export const messageApi = {
	getMyMessageCount: function(data) {
		return http.get(pre + "/msg/mine", data);
	},
	getMessageType: function(data) {
		return http.get(pre + "/msg/types", data);
	},
	getMySend: function(data) {
		return http.get(pre + "/msg/recordOfMySend", data);
	},
	getMyHandled: function(data) {
		return http.get(pre + "/msg/recordOfMyHandled", data);
	},
	getMessageById: function(id) {
		return http.get(pre + `/msg/${id}`);
	},
	getPerRoles: function(){
		return http.get("/sys-role-permission/sys/permission/roles");
	}
};
