<!--
 * @Author: your name
 * @Date: 2020-10-15 15:31:28
 * @LastEditTime: 2021-01-14 10:47:18
 * @LastEditors: lsh
 * @Description: In User Settings Edit
 * @FilePath: \hydrology\src\layouts\components\LoginForm.vue
-->
<template>
  <div class="login-form">
    <div class="form-item">
      <el-input
        placeholder="请输入用户名"
        v-model="loginForm.username"
        @keyup.enter.native="doLogin"
      >
        <i slot="prefix" class="input-icon icon-input-account"></i>
      </el-input>
    </div>
    <div class="form-item">
      <el-input
        placeholder="请输入密码"
        type="password"
        v-model="loginForm.password"
        @keyup.enter.native="doLogin"
      >
        <i slot="prefix" class="input-icon icon-input-password"></i>
      </el-input>
    </div>
    <div class="form-item button-area">
      <div class="primary-long-btn" @click="doLogin()">登录</div>
    </div>
    <div class="form-item member-area">
      <el-checkbox label="记住密码" name="type"></el-checkbox>
      <span>忘记密码?</span>
    </div>
  </div>
</template>

<script>
import { login } from "@/api/login";
export default {
  name: "LoginForm",
  props: {},
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
    };
  },
  components: {},
  mounted() {
  },
  methods: {
    async doLogin() {
      await login(this.loginForm).then((res) => {
        if (res.code == 0) {
          let opt = res.data;
          opt.timestamp = new Date().getTime();
          this.$localData("set", "userInfo", opt);
          this.$router.replace("/waterDisasterCustom?bsType=waterDisaster&large=false&isBigScreen=false");
          this.$message({
            message: res.message,
            type: "success",
            duration: 1000,
          });
        } else {
          this.$message.error(res.message);
        }
      });
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/theme.scss";
.form-item {
  margin-top: 22px;
  .el-input__inner {
    color: #333;
    background: #fff;
  }
  &.button-area {
    margin-top: 40px;
  }
  &.member-area {
    font-size: 14px;
    display: flex;
    justify-content: space-between;
    span {
      cursor: pointer;
      color: #32bcb4;
      font-weight: bold;
    }
  }
}
</style>