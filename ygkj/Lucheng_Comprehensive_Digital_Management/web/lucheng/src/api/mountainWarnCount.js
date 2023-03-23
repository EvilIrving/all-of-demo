import { http } from "@/config/httpRequset.js";

export function getMountainTorrentBaseStats(data) {
	return http.get("big-screen/waterDisasterPrevention/mountainTorrentBaseStats", data);
}


