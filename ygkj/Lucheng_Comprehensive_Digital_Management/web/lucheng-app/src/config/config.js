// let localBaseUrl = "http://192.168.2.57:8090"; //李汪洋
//const localBaseUrl = 'http://192.168.2.123:8090/'; //wgf
//const localBaseUrl = 'http://192.168.2.226:8090/'; //lxl
// let localBaseUrl = 'http://192.168.2.210:8090' //潘麒帆
let localBaseUrl = "https://slgh.lucheng.gov.cn/api"; //线上
const config = {
  // baseUrl: localBaseUrl,
  baseUrl: process.env.NODE_ENV === "production" ? "https://slgh.lucheng.gov.cn/api" : localBaseUrl,
  uploadUrl:
    process.env.NODE_ENV === "production"
      ? "https://slgh.lucheng.gov.cn/api/ums/upload"
      : localBaseUrl + "/ums/upload",
};
export default config;
