<template>
  <div class="register-user">
    <div class="form-item">
      <el-input placeholder="请输入用户名" v-model="registerUserForm.managerName">
        <i slot="prefix" class="input-icon icon-input-account"></i>
      </el-input>
    </div>
    <div class="form-item">
      <el-input placeholder="请输入手机号码" v-model="registerUserForm.phone">
        <i slot="prefix" class="input-icon icon-input-phone"></i>
      </el-input>
    </div>
    <div class="form-item">
      <el-input placeholder="请输入身份证号码" v-model="registerUserForm.idCard">
        <i slot="prefix" class="input-icon icon-input-card"></i>
      </el-input>
    </div>
    <div class="form-item">
      <div class="primary-long-btn" @click="registerUser()">注册</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisterUserForm",
  props: {},
  data() {
    return {
      registerUserForm: {
        managerName: "",
        phone: "",
        idCard: ""
      }
    };
  },
  components: {},
  mounted() {},
  methods: {
    registerUser(){
      this.$emit("buttonClick");
    },
    retsetForm(){
      this.registerUserForm = {
        managerName: "",
        phone: "",
        idCard: "",
      }
    }
  }
};
</script>

<style lang='scss' scoped>
.form-item {
  margin-top: 22px;
}
</style>