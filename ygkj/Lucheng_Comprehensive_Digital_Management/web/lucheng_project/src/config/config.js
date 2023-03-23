/*
 * @Author: hanyu
 * @LastEditTime: 2023-01-04 13:58:39
 * @Description: 存放全局变量
 * @FilePath: \lucheng_project\src\config\config.js
 */
//  let localBaseUrl = 'http://192.168.2.123:8090' //吴高峰
//let localBaseUrl = 'http://192.168.2.110:8090'; //lxl
 let localBaseUrl = 'http://192.168.2.210:8090' //潘麒帆
//let localBaseUrl = 'http://192.168.2.57:8090' //李汪洋
// let localBaseUrl = 'http://192.168.2.252:8090' //向阳
// let localBaseUrl = 'https://slgh.lucheng.gov.cn/api' //线上
// let localRscpHost = 'http://192.168.2.61:8066/rscp_war_exploded' //韩瑜
let localRscpHost = 'https://sk.zjwater.com' //线上
let localAqjcHost = 'https://dam.ygwjg.com' //线上大坝安全监测
// let localRscpHost = "http://192.168.2.123:7070"; //wgf
// let localRscpHost = "http://192.168.2.212:8095/rscp_war_exploded/"; //dtb
// let localAqjcHost = 'http://112.17.127.75:19280' //wx本地映射
// let localAqjcHost = 'http://192.168.2.192:8083/' //wx本地映射

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
