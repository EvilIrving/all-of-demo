/*
 * @Author: hanyu
 * @LastEditTime: 2023-01-04 13:58:39
 * @Description: 存放全局变量
 * @FilePath: \lucheng_project\src\config\config.js
 */
let localBaseUrl = 'https://slgh.lucheng.gov.cn/api' //线上
let localRscpHost = 'https://sk.zjwater.com' //线上
let localAqjcHost = 'https://dam.ygwjg.com' //线上大坝安全监测

const config = {
  baseUrl: process.env.NODE_ENV === "production" ? "/api" : localBaseUrl,
  rscpHost:
    process.env.NODE_ENV === "production"
      ? "https://sk.zjwater.com"
      : localRscpHost, //工程运管地址
  aqjcHost:
    process.env.NODE_ENV === "production"
      ? "https://dam.ygwjg.com"
      : localAqjcHost, //安全监测地址
  uploadUrl:
    process.env.NODE_ENV === "production"
      ? "/api/ums/upload"
      : localBaseUrl + "/ums/upload",
  fileUploadUrl:
    process.env.NODE_ENV === "production"
      ? "/api/ums/upload"
      : localBaseUrl + "/ums/upload",
  skFilesHttps: 'https://sk.zjwater.com'
};
export default config;
