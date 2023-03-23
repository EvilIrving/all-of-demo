<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-21 17:28:02
 * @Description: 用户信息表单
 * @FilePath: \lucheng_project\src\components\PasswordForm.vue
-->
<template>
  <div class="wrap">
    <el-form
      ref="postForm"
      :model="postForm"
      :rules="rules"
      class="form-container"
    >
      <el-form-item label-width="30%" label="原密码" prop="oldPassWord">
        <el-input
          v-model.trim="postForm.oldPassWord"
          placeholder="请输入原密码"
          type="password"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item label-width="30%" label="新密码" prop="passWord">
        <el-input
          v-model.trim="postForm.passWord"
          show-password
          placeholder="请输入大于8位数且不由纯数字或者纯字母组合的密码"
          type="password"
          style="width: 70%"
        />
      </el-form-item>
      <div class="footer_btn">
        <el-button
          v-loading="loading"
          type="primary"
          class="btn_save"
          @click="submitForm('postForm')"
          >保存</el-button
        >
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { Base64 } from "js-base64";
import { sysUserPasswordSave } from "@/api/index";
import { sessionData } from "@/plugins/storage";
export default {
  name: "PasswordForm",
  computed: {
    userInfo: () => {
      return sessionData("get", "userInfo");
    },
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入新密码"));
      } else {
        if (value.length < 8 || value.length > 18) {
          callback(new Error("新密码长度错误，应为8~18位,请重新输入"));
        } else if (
          /^[0-9]+$/.test(value) ||
          /^[a-z]+$/.test(value) ||
          /^[A-Z]+$/.test(value)
        ) {
          callback(new Error("新密码不能由纯数字或者纯字母组合，请重新输入"));
        } else {
          callback();
        }
      }
    };
    return {
      loading: false,
      postForm: {
        oldPassWord: "",
        passWord: "",
      },
      rules: {
        oldPassWord: [
          { required: true, message: "请输入原密码", trigger: "blur" },
        ],
        passWord: [{ validator: validatePass, trigger: "blur" }],
      },
    };
  },
  methods: {
    submitForm(postForm) {
      this.$refs[postForm].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.postForm.oldPassWord == this.postForm.passWord) {
            this.$message({
              message: "新密码与原密码重复",
              type: "error",
              duration: "1000",
            });
            this.loading = false;
            return;
          }
          let obj = {
            oldPwd: Base64.encode(this.postForm.oldPassWord),
            newPwd: Base64.encode(this.postForm.passWord),
            id: this.userInfo.id,
            isEncryption: true
          };
          this.save(obj);
        } else {
          return false;
        }
      });
    },
    async save(data) {
      let res = await sysUserPasswordSave(data);
      if (res.code == 0) {
        this.loading = false;
        this.$message({
          message: "修改成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.closeEditDialog();
          },
        });
      } else {
        this.loading = false;
        this.$message.error(res.msg);
      }
    },
    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  height: 260px !important;
  padding-top: 20px;
}
</style>