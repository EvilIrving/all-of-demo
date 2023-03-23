<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-14 15:21:57
 * @Description: 登录页
 * @FilePath: \lucheng_project\src\layouts\Login.vue
-->
<template>
  <div class="wrap">
    <div class="login_wrap">
      <div class="title_box">
        <img src="../assets/images/logo.svg" alt="" />
        <p>鹿城区水利全域数字化管护平台</p>
      </div>
      <div v-if="permissionsType" class="login_box" v-loading="loading">
        <h3>{{ type != "dd" ? "登录" : "账号绑定" }}</h3>
        <div class="login_input_box">
          <el-input
            type="text"
            placeholder="请输入用户名"
            v-model="username"
            clearable
            @keyup.enter.native="login()"
          ></el-input>
        </div>
        <div class="login_input_box">
          <el-input
            v-if="type != 'dd'"
            type="password"
            placeholder="请输入密码"
            v-model="password"
            clearable
            @keyup.enter.native="login()"
          ></el-input>
          <div class="login-item" v-if="type == 'dd'">
            <div class="login-suo-left">
              <el-input
                type="text"
                placeholder="请输入验证码"
                v-model="verificationCode"
                clearable
                @keyup.enter.native="login()"
              ></el-input>
            </div>
            <div class="login-suo-right" @click="getVerificationType">
              <div v-if="verificationType" class="right-text">获取验证码</div>
              <div v-else class="right-times">{{ textTime }}<span>s</span></div>
            </div>
          </div>
        </div>
        <el-button type="primary" round @click="login" v-if="type != 'dd'"
          >登录</el-button
        >
        <el-button
          type="primary"
          round
          @click="loginZzdLogin('')"
          v-if="type == 'dd'"
          >绑定</el-button
        >
      </div>
      <div class="login_box permissions-box" v-else>
        <img src="../assets/images/permissions-icon.png" alt="" />
      </div>
      <div class="tips">
        友情提示：为保证最佳体验，推荐使用chrome或IE9以上浏览器
      </div>
    </div>
  </div>
</template>

<script>
import {
  publicKey,
  login,
  validateTicketApi,
  validateUserCodeApi,
  loginZzdLoginApi,
  sendZzdVerificationApi,
} from "@/api/index";
import { permissionListApi } from "@/api/systemManagement";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { getQueryString } from "@/utils/utils";
import { metaInfo, toDo } from "@/utils/Wpk.js";
import { Base64 } from "js-base64";
import { JSEncrypt } from "jsencrypt";
// import dd from 'gdt-jsapi';
export default {
  name: "Login",
  props: {},
  computed: {
    textTime() {
      return this.times < 10 ? `0${this.times}` : this.times;
    },
  },
  data() {
    return {
      times: 59,
      username: "",
      password: "",
      verificationCode: "",
      verificationType: true,
      loading: false,
      screenLoading: true,
      interval: null,
      key: "",
      type: "project",
      authInfo: {},
      permissionsType: true,
    };
  },
  mounted() {
    //获取加密key
    this.getPublicKey()
    //获取url参数
    let ticket = getQueryString("ticket");
    let userCode = getQueryString("userCode");
    let auth_code = getQueryString("auth_code");
    let type = getQueryString("type");

    //ticket票据登录
    if (ticket) {
      this.loading = true;
      this.validateTicket(ticket);
    }
    
    // 程金刚  c7ef46fab91149e9a3ff2aadadd8a685
    // 路亚峰  u2$dc048216c0044452b247b3b7bfd936d7

    //userCode票据登录
    if (userCode) {
      this.loading = true;
      this.validateUserCode(userCode);
    }

    //浙政钉登录
    if (type && type == "dd") {
      if (auth_code) {
        this.loading = true;
        this.type = "dd";
        this.loginZzdLogin(auth_code);
        let oldTimes = sessionStorage.getItem("times");
        if (oldTimes) {
          this.countDown();
        }
      } else {
        this.permissionsType = false;
      }
    }
    // let from = getQueryString('from')
    // console.log(from,'from');
    // if(from && from == 'rscp'){
    //   // this.username = '18267829559' //江化琴
    //   this.username = '13858860396' //程劲刚
    //   this.password = 'ygkj0818'
    //   this.login()
    // }else{
    //   this.loading = false
    // }
  },
  methods: {
    async getPublicKey() {
      let res = await publicKey();
      if (res.success) {
        this.key = res.key;
      }
    },
    async validateTicket(ticket) {
      let res = await validateTicketApi({
        ticket: ticket,
      });
      if (res.code === 0) {
        this.$sessionData("set", "userInfo", res.data);
        toDo("登录", res.data.id);
        await this.getPermissionList(res.data.jobId);
        this.$router.push("/");
      }
      this.loading = false;
    },

    async loginZzdLogin(authCode) {
      let obj = {};
      if (authCode) {
        obj["authCode"] = authCode;
      } else {
        obj.isNeedBind = true;
        obj.username = this.username;
        obj.verificationCode = this.verificationCode;
        obj.openid = this.authInfo.openid;
        if (!this.username) {
          this.$message.error("请输入手机号码");
          return false;
        }
        if (!this.verificationCode) {
          this.$message.error("请输入验证码");
          return false;
        }
        this.loading = true;
      }
      let res = await loginZzdLoginApi(obj);
      if (res.code == 0) {
        this.authInfo = res.data;
        if (res.data.isBind) {
          this.$sessionData("set", "userInfo", res.data);
          toDo("登录", res.data.id);
          metaInfo();
          await this.getPermissionList(res.data.jobId);
          this.$router.push("/");
          this.loading = false;
        } else {
          this.loading = false;
        }
      } else {
        this.$message.error(res.message);
        this.loading = false;
        this.permissionsType = false;
      }
    },
    async getVerificationType() {
      if (!this.username) {
        this.$toast.fail("请输入手机号码");
        return false;
      }
      if (this.sendClickType) {
        return false;
      }
      this.sendClickType = true;
      if (this.verificationType) {
        let res = await sendZzdVerificationApi({ phone: this.username });
        if (res.code == 0) {
          this.$message.success("验证码发送成功");
          this.countDown();
        } else {
          this.$message.error(res.message);
        }
        this.sendClickType = false;
      }
    },
    countDown() {
      clearInterval(this.interval);
      let times = Date.parse(new Date());
      let oldTimes = sessionStorage.getItem("times");
      if (!oldTimes) {
        sessionStorage.setItem("times", times);
      } else {
        let differenceValue = (times - oldTimes) / 1000;
        if (differenceValue < 60) {
          this.times = 60 - differenceValue;
        } else {
          sessionStorage.removeItem("times");
        }
      }
      this.verificationType = false;
      this.interval = setInterval(() => {
        this.times--;
        if (this.times == 0) {
          clearInterval(this.interval);
          this.verificationType = true;
          this.times = 59;
          sessionStorage.removeItem("times");
        }
      }, 1000);
    },
    async validateUserCode(userCode) {
      let res = await validateUserCodeApi({
        userCode: userCode,
      });
      if (res.code === 0) {
        this.$sessionData("set", "userInfo", res.data);
        toDo("登录", res.data.id);
        await this.getPermissionList(res.data.jobId);
        this.$router.push("/");
      }
      this.loading = false;
    },
    async login() {
      NProgress.start();
      this.loading = true;
      let password = this.password;
      if (this.key) {
        let encryptor = new JSEncrypt();
        encryptor.setPublicKey(this.key);
        password = encryptor.encrypt(password);
      }
      let res = await login({
        username: Base64.encode(this.username),
        password: password,
        isEncryption: true
      });
      if (res.code === 0) {
        this.$sessionData("set", "userInfo", res.data);
        toDo("登录", res.data.id);
        await this.getPermissionList(res.data.jobId);
        this.$router.push("/");
      } else {
        this.$message.error(res.message);
      }
      this.loading = false;
      NProgress.done();
    },
    async getPermissionList(jobId) {
      let res = await permissionListApi({
        id: jobId,
      });
      if (res.code == 0) {
        this.$localData("set", "permission", res.data);
      } else {
        this.$message.error("获取功能权限失败");
      }
      // this.loading = false
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100vh;
  background: url("../assets/images/login_bg.png") no-repeat center;
  background-size: 100% 100%;
  position: relative;
  .screen_loading {
    z-index: 99;
    position: absolute;
    background: #fff;
    width: 100%;
    height: 100%;
  }
  .login_wrap {
    width: 950px;
    text-align: center;
    height: 600px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin: -300px 0 0 -475px;
    .title_box {
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);
      img {
        margin-right: 10px;
      }
      p {
        font-size: 38px;
        font-weight: bold;
        color: #fff;
        letter-spacing: 5px;
      }
    }
    .login_box {
      border-radius: 4px;
      background: #fff;
      height: 500px;
      width: 720px;
      margin: 0 auto;
      margin-top: 50px;
      padding: 40px;
      box-sizing: border-box;
      h3 {
        font-size: 32px;
        font-weight: bold;
        padding-bottom: 20px;
      }
      .login_input_box {
        text-align: left;
        margin-bottom: 30px;
        p {
          font-size: 14px;
          color: #666;
          padding-top: 40px;
        }
        .login-item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          width: 100%;
          .login-suo-left {
            display: flex;
            align-items: center;
            width: calc(100% - 120px);
            height: 100%;
          }
          .login-suo-right {
            width: 100px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #3a8ee6;
            border-radius: 4px;
            cursor: pointer;
            .right-text {
              color: #fff;
              font-size: 16px;
            }
            .right-times {
              font-size: 24px;
              color: #fff;
              span {
                font-size: 16px;
              }
            }
          }
        }
      }
      &.permissions-box {
        text-align: center;
        line-height: 520px;
        img {
          width: 118px;
        }
      }
    }
    .tips {
      font-size: 14px;
      color: #fff;
      text-align: left;
      padding-top: 15px;
      width: 720px;
      margin: 0 auto;
    }
  }
}
</style>
<style lang='scss'>
.login_box .login_input_box .el-input__inner {
  /* border: none; */
  border-top: none;
  border-left: none;
  border-right: none;
  border-radius: 0;
  padding: 0;
  height: 50px;
  line-height: 50px;
}
.login_box .el-button {
  width: 330px;
  height: 50px;
  border-radius: 25px;
  margin-top: 80px;
}
</style>
