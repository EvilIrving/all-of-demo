/*
 * @Author: wqn
 * @Date: 2022-11-30 13:59:00
 * @LastEditors: wqn
 * @LastEditTime: 2022-11-30 14:50:11
 */

const imgUrl = 'https://cdn.ygwjg.com/rscp/screen/1.0/skzg/'
const zhImgUrl = 'https://cdn.ygwjg.com/rscp/screen/1.0/intelligence/' // 智慧工程图片地址
const swImgUrl = 'https://cdn.ygwjg.com/rscp/screen/1.0/rivers/' // 各类工程研判图片地址

const ypImgUrl = 'https://cdn.ygwjg.com/rscp/screen/yp-screen/'

let onlineUrl = "https://sk.zjwater.com";
let localUrl = "https://sk.zjwater.com/dingrscp-api"; //线上
// let localUrl = "http://192.168.2.97:8082/baseApi";
// let localUrl = "http://192.168.2.111:8382/baseApi";
// let localUrl = "http://192.168.2.188:8081/baseApi"; //王鼎
// let localUrl = 'http://192.168.2.123:7070/baseApi'; //吴高峰
//let localUrl = 'http://192.168.2.111:8385/'; 

export default {
  baseUrl: process.env.NODE_ENV === "production" ? "/dingrscp-api" : '/baseApi',
  host: process.env.NODE_ENV === "production" ? onlineUrl : localUrl,
  gaoDeKey: 'f549bf6bed485a81e61a4e03117d8630',
  ypImgUrl,
  imgUrl,
  zhImgUrl,
  swImgUrl,
};