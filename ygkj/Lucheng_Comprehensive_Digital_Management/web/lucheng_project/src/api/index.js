import axios from '../config/httpRequset'

// 登录
export function login(data) {
  return axios.post('/auth-service/auth/login', data)
}
export function publicKey(data) {
  return axios.post("/auth-service/auth/rdspwd", data, true);
}
export function sysUserPasswordSave(data) {
  return axios.put('/auth-service/auth/updatePassword', data)
}

//专有钉钉
export function loginZzdLoginApi(data) {
  return axios.post("/auth-service/auth/zzdLogin", data);
}
//专有钉钉验证码绑定
export function sendZzdVerificationApi(data) {
  return axios.get("/auth-service/auth/sendZzdVerification", data);
}
// 登出
export function logout(data) {
  return axios.post('/ums/sys/user/logout', data)
}

// 获取工程运管页面所需参数
export function getOutsideToken(data) {
  return axios.post('/ums/sys/getOutsideToken', data)
}

// 水平台免登陆
export function validateTicketApi(data) {
  return axios.post('/auth-service/auth/validateTicket', data)
}

// 工程运管免登陆
export function validateUserCodeApi(data) {
  return axios.post('/ums/sys/rscpLogin', data)
}

// 督办登陆
export const oversee = {
  // 督办登陆
  loginByOverseeWorkId: function (workId) {
    return axios.get(pre + `/oversee/loginByOverseeWorkId/${workId}`);
  },
  // 管理制度文件
  manageinstitution: function (seawallCode) {
    return axios.get(pre + `/bhDep/manageinstitution/${seawallCode}`);
  },
  // 组织架构
  organization: function (seawallCode) {
    return axios.get(pre + `/bhDep/organization/${seawallCode}`,);
  },
  //人员信息
  listPeople: function (data) {
    return axios.post(pre + "/bhDep/listPeople", data);
  },
  //管护经费
  managefund: function (data) {
    return axios.post(pre + "/bhDep/managefund", data);
  },
  //维修养护资金申报
  mfd: function (data) {
    return axios.post(pre + "/bhDep/mfd", data);
  },
  //维修养护项目维护", data);
  mfpm: function (data) {
    return axios.post(pre + "/bhDep/mfpm", data);
  },
  //维修养护资金使用
  mfu: function (data) {
    return axios.post(pre + "/bhDep/mfu", data);
  },
  //组织架构详情
  organizationDetail: function (data) {
    return axios.post(pre + "/bhDep/organizationDetail", data);
  },
  //年度培训
  personTrain: function (data) {
    return axios.post(pre + "/bhDep/personTrain", data);
  },

};
