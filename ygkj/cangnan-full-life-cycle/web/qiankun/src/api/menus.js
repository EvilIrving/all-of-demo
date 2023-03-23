import { http } from "@/config/httpRequset";
const pre = "/sys-role-permission";

export const menuApi = {
	getMenus: function(data) {
		return http.get(pre + "/sys/home", data);
	},
};
