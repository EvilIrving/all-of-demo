const imgUrl = 'https://cdn.ygwjg.com/rscp/screen/1.0/skzg/'
const zhImgUrl = 'https://cdn.ygwjg.com/rscp/screen/1.0/intelligence/' // 智慧工程图片地址
const swImgUrl = 'https://cdn.ygwjg.com/rscp/screen/1.0/rivers/' // 各类工程研判图片地址
// let localUrl = "http://192.168.2.97:8082";
// let localUrl = "http://192.168.2.57:8080";
// let localUrl = "http://192.168.2.111:8382";
// let localUrl = "http://192.168.2.188:8081/"; //王鼎
//let localUrl = "https://sk.zjwater.com";
// let localUrl = 'http://192.168.2.123:7070/'; //吴高峰
let localUrl = 'http://192.168.2.111:8385/'; //吴高峰
let onlineUrl = "https://sk.zjwater.com";
const config = {
  baseUrl: process.env.NODE_ENV === "production" ? "/dingrscp-api" : "/baseApi",
  host: process.env.NODE_ENV === "production" ? onlineUrl : onlineUrl,
  imgUrl: imgUrl,
  swImgUrl: swImgUrl,
  zhImgUrl: zhImgUrl,
};
export default config;
