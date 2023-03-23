/*
 * @Author: wqn
 * @Date: 2022-10-08 09:26:49
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2023-01-29 14:35:30
 */
/* eslint-disable */
// const testService = 'http://192.168.0.32:8090/';
 const Service = 'https://slgh.lucheng.gov.cn/api/';
// const Service = 'http://192.168.2.57:8090/'; // lwy
// const Service = 'http://192.168.2.123:8090/'; //wgf
//const Service = 'http://192.168.2.210:8090/'; //pqf
//const Service = 'http://192.168.2.137:8090/'; //pqf
//const Service = 'http://192.168.2.226:8090/'; //lxl
// const Service = 'http://192.168.2.57:8090/'; //lxl


const config = {
  baseUrl: process.env.NODE_ENV === 'production' ? '/api' : Service,
  gaoDeKey: 'f549bf6bed485a81e61a4e03117d8630',
  fileUploadUrl: process.env.NODE_ENV === 'production' ? '/api/auth-service/upload' : Service + 'auth-service/upload'
};

export default config;
  