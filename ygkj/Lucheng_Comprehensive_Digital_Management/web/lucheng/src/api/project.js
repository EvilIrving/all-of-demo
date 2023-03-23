import { http } from "@/config/httpRequset.js";

export function getAllProjectType(data) {
	return http.get("/project-manage/projectManage/getAllProjType", data);
}
