<!--
 * @Author: hanyu
 * @LastEditTime: 2020-12-03 18:25:50
 * @Description: 登录页
 * @FilePath: /scwater_apply/src/views/Login.vue
-->
<template>
  <div class="wrap login-page">
    <div class="obscuration"></div>
    <div class="login-content clearfix">
      <div class="left-area left"></div>
      <div class="right-area left">
        <p>温州市水管理平台</p>
        <div class="form-area">
          <div class="form-title">
            <strong>{{ mainTitle }}</strong>
            <!-- <span @click="goLogin()">
              {{ subTitle }}
              <i></i>
            </span> -->
          </div>
          <div class="form-body">
            <RegisterUnitForm
              ref="registerUnitForm"
              v-show="showForm === 'registerUnit'"
              @buttonClick="nextStep()"
            ></RegisterUnitForm>
            <RegisterUserForm
              ref="registerUserForm"
              v-show="showForm === 'registerUser'"
              @buttonClick="registerUser()"
            ></RegisterUserForm>
            <LoginForm
              ref="loginForm"
              v-show="showForm === 'login'"
            ></LoginForm>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RegisterUnitForm from "./components/RegisterUnitForm";
import RegisterUserForm from "./components/RegisterUserForm";
import LoginForm from "./components/LoginForm";
import { addCompany } from "@/api/system";
export default {
  name: "Login",
  props: {},
  data() {
    return {
      showForm: "login",
      mainTitle: "用户登录",
      subTitle: "注册",
    };
  },
  components: {
    RegisterUnitForm,
    RegisterUserForm,
    LoginForm,
  },
  async mounted() {},
  methods: {
    nextStep() {
      this.showForm = "registerUser";
      this.mainTitle = "管理员注册";
    },
    async registerUser() {
      const registerUnitForm = this.$refs.registerUnitForm.registerForm;
      const registerUserForm = this.$refs.registerUserForm.registerUserForm;
      let opt = {
        areaAddr: registerUnitForm.areaAddr,
        areaCode: registerUnitForm.areaCode,
        companyName: registerUnitForm.companyName,
        industryCategory: registerUnitForm.industryCategory,
        managerName: registerUserForm.managerName,
        phone: registerUserForm.phone,
        socialCreditCode: registerUnitForm.socialCreditCode,
      };
      await addCompany(opt).then((res) => {
        if (res.code === 0) {
          this.$message({
            message: res.msg,
            type: "success",
          });
          this.$refs.registerUnitForm.retsetForm();
          this.$refs.registerUserForm.retsetForm();
          this.subTitle = "注册";
          this.mainTitle = "用户登录";
          this.showForm = "login";
        }
      });
    },
    goLogin() {
      if (this.mainTitle === "用户登录") {
        this.subTitle = "登录";
        this.mainTitle = "企业注册";
        this.showForm = "registerUnit";
      } else {
        this.subTitle = "注册";
        this.mainTitle = "用户登录";
        this.showForm = "login";
      }
    },
    resetForm() {},
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/theme.scss";
.login-page {
  background: url(../assets/images/timg4.png) no-repeat;
  background-size: 100% 100%;
  position: relative;
  .obscuration {
    width: 100%;
    height: 100%;
    background: rgba(0, 17, 53, 0.58);
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
  }
  .login-content {
    width: 980px;
    height: 580px;
    z-index: 2;
    background: #fff;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-left: -490px;
    margin-top: -320px;
    .left-area {
      width: 490px;
      height: 100%;
      background: url(../assets/images/pic.png) no-repeat;
      background-size: 100% 100%;
    }
    .right-area {
      width: calc(100% - 490px);
      height: 100%;
      padding: 60px 0 0 0;
      > p {
        text-align: center;
        font-size: 22px;
        font-weight: bold;
        letter-spacing: 3px;
      }
      .form-area {
        width: 360px;
        position: relative;
        left: 50%;
        transform: translateX(-50%);
        height: calc(100% - 36px);
        padding-top: 45px;
        .form-title {
          text-align: center;
          position: relative;
          strong {
            font-weight: bold;
            font-size: 20px;
            color: #32bcb4;
          }
          span {
            font-size: 14px;
            position: absolute;
            right: 0;
            cursor: pointer;
          }
        }
        .form-body {
          padding-top: 20px;
          height: calc(100% - 20px);
        }
      }
    }
  }
}
</style>