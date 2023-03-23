<template>
  <div class="login-bind">
    <div class="head_box">
      <van-nav-bar
        placeholder
        :border="false"
        safe-area-inset-top
        :title="$route.meta.title"
        right-text="按钮"
        @click-right="$router.go(-1)"
      >
        <template #right>
          <van-icon name="cross" size="18" color="#fff" />
        </template>
      </van-nav-bar>
      <div class="head_box_title">
        <div class="head_box_title_text">您好，欢迎登录!</div>
        <img
          src="../../../assets/images/pageIndex/login-logo-icon.png"
          alt=""
        />
      </div>
    </div>
    <div class="login-box">
      <div class="login-title">
        <div>账号登录</div>
        <div>绑定手机号码</div>
      </div>
      <div class="login-content">
        <div class="login-item login-phone">
          <img
            src="../../../assets/images/pageIndex/login-phone-icon.png"
            class="phone-icon"
          />
          <van-field
            v-model.trim="username"
            type="tel"
            placeholder="请输入手机号码"
          />
        </div>
        <div class="login-item login-suo">
          <div class="login-suo-left">
            <img
              src="../../../assets/images/pageIndex/login-suo-icon.png"
              class="suo-icon"
            />
            <van-field
              v-model.trim="code"
              type="number"
              placeholder="请输入验证码"
            />
          </div>
          <div class="login-suo-right" @click="getVerificationType">
            <div v-if="verificationType" class="right-text">获取验证码</div>
            <div v-else class="right-times">{{ textTime }}<span>s</span></div>
          </div>
        </div>
      </div>
      <div class="bind-box">
        <div class="bind-btn" @click="loginZzdLogin">绑定</div>
      </div>
    </div>
  </div>
</template>

<script>
import { loginZzdLoginApi, sendZzdVerificationApi } from "@/api/login";
import { localData, sessionData } from "@/plugins/storage";
export default {
  name: "",
  props: {},
  computed: {
    textTime() {
      return this.times < 10 ? `0${this.times}` : this.times;
    },
  },
  data() {
    return {
      verificationType: true,
      times: 59,
      interval: null,
      username: "",
      code: "",
      saveBindType: false,
      sendClickType: false,
      openid: ''
    };
  },
  created() {},
  mounted() {
    this.openid = this.$route.query.openid
    let oldTimes = sessionStorage.getItem("times");
    if (oldTimes) {
      this.countDown();
    }
  },
  methods: {
    async loginZzdLogin() {
      if (!this.username) {
        this.$toast.fail("请输入手机号码");
        return false;
      }
      if (!this.code) {
        this.$toast.fail("请输入验证码");
        return false;
      }
      if (this.saveBindType) {
        return false;
      }
      this.saveBindType = true;
      let res = await loginZzdLoginApi({
        isNeedBind: true,
        username: this.username,
        verificationCode: this.code,
        openid: this.openid
      });
      if (res.code == 0) {
        let opt = res.data;
        let routesType = process.env.NODE_ENV === "production" ? "/appIndex" : "/";
        opt.timestamp = new Date().getTime();
        localData("set", "userInfo", opt);
        sessionData("set", "userInfo", opt);
        this.$router.replace(routesType);
      } else {
        this.$toast.fail(res.message);
        this.saveBindType = false;
      }
    },
    async getVerificationType() {
      if (!this.username) {
        this.$toast.fail("请输入手机号码");
        return false;
      }
      if(this.sendClickType){
        return false
      }
      this.sendClickType = true
      if (this.verificationType) {
        let res = await sendZzdVerificationApi({ phone: this.username });
        if (res.code == 0) {
          this.$toast.success("验证码发送成功");
          this.countDown();
        } else {
          this.$toast.fail(res.message);
        }
        this.sendClickType = false
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
  },
  beforeDestroy() {
    clearInterval(this.interval);
  },
};
</script>

<style lang="scss" scoped>
.login-bind {
  width: 100%;
  height: 100%;
  position: relative;
  .head_box {
    position: relative;
    height: 243px;
    background: url("~@/assets/images/pageIndex/login-bind-icon.png") no-repeat;
    background-size: 100% 100%;
    .head_box_title {
      padding: 32px 16px 0 12px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .head_box_title_text {
        font-size: 24px;
        font-weight: 500;
        color: #ffffff;
      }
      img {
        width: 58px;
      }
    }
  }
  .login-box {
    width: 100%;
    height: calc(100% - 167px);
    border-radius: 12px 12px 0px 0px;
    background: #ffffff;
    position: absolute;
    top: 167px;
    left: 0;
    .login-title {
      padding: 45px 0 40px 40px;
      font-size: 16px;
      color: #666666;
      display: flex;
      align-items: flex-end;
      > div:first-child {
        padding-right: 23px;
        border-right: 1px solid #e6e6e6;
      }
      > div:last-child {
        font-size: 26px;
        padding-left: 23px;
        font-weight: 500;
        color: #333333;
      }
    }
    .login-content {
      padding: 0 12px;
      .login-item {
        height: 74px;
        display: flex;
        align-items: center;
        &.login-phone {
          padding-left: 32px;
          background: #f6f8fc;
          border-radius: 8px;
          .phone-icon {
            width: 19px;
          }
        }
        &.login-suo {
          margin-top: 24px;
          justify-content: space-between;
          > div {
            background: #f6f8fc;
            border-radius: 8px;
          }
          .suo-icon {
            width: 18px;
          }
          .login-suo-left {
            display: flex;
            align-items: center;
            padding-left: 32px;
            width: 210px;
            height: 100%;
          }
          .login-suo-right {
            width: 100px;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            .right-text {
              color: #333333;
              font-size: 16px;
            }
            .right-times {
              font-size: 24px;
              span {
                font-size: 16px;
              }
            }
          }
        }
      }
    }
    .bind-box {
      margin-top: 62px;
      padding: 0 12px;
      .bind-btn {
        width: 100%;
        height: 44px;
        background: #276af0;
        border-radius: 8px;
        line-height: 44px;
        text-align: center;
        color: #ffffff;
        font-size: 16px;
      }
    }
  }
}
</style>
<style>
/* vant 导航栏定位 全局样式 */
.van-nav-bar--fixed {
  position: fixed;
  top: 30px;
  left: 0;
  width: 100%;
}
.login-bind .van-nav-bar {
  padding-top: 30px;
  background-color: transparent;
}
.van-nav-bar__placeholder {
  padding-top: 30px;
  z-index: 9;
}
.login-bind .van-nav-bar--fixed,
.login-bind .van-nav-bar__placeholder,
.login-bind .van-nav-bar__content {
  background-color: transparent;
}
.login-bind .van-nav-bar__title {
  margin-left: 20px;
  color: #fff;
}
.login-bind .login-item .van-cell {
  padding: 0 27px;
  background-color: transparent;
  font-size: 16px;
}
</style>
