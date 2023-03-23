import { http } from '@/config/httpRequset';
const pre = '/project-manage';

export function getWaterWorkList(data) {
	return http.get(pre + '/waterWorks/listWaterWorks', data);
}

export function getStats4listWaterWorks(data) {
	return http.get(pre + '/waterWorks/stats4listWaterWorks', data);
}

export function getStats4listWaterWorksState(data) {
	return http.get(pre + '/waterWorks/stats4listWaterWorksState', data);
}

export function getWaterWorksStateCurve(data) {
	return http.get(pre + '/waterWorks/waterWorksStateCurve', data);
}

export function getWaterWorksStats(data) {
	return http.get(pre + '/waterWorks/waterWorksStats', data);
}
