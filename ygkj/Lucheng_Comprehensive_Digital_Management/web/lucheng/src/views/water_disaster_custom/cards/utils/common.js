export const moduleAMapUrl = "https://117.149.228.207:8081/moduleAMap/";
/**
 * 跳转一张图
 * @param {string} [type]
 */
export function goAMap(type) {
	let url = moduleAMapUrl;
	if (type) url += `?type=${type}`;
	window.open(url);
}
