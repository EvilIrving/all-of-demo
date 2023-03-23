<!--
 * @Author: hanyu
 * @LastEditTime: 2022-08-16 11:23:23
 * @Description: 物业管理-人员表单
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyList\components\PropertyPeopleForm.vue
-->
<template>
  <div class="wrap-form-box">
    <el-form :rules="rules" ref="form" :model="form" label-width="150px">
      <el-form-item label="岗位：" :prop="isAdd ? 'post' : ''">
        <el-select style="width: 100%" :disabled="!isAdd" v-model="form.post" placeholder="请选择">
          <el-option label="单位负责人" value="单位负责人"> </el-option>
          <el-option label="技术负责人" value="技术负责人"> </el-option>
          <el-option label="巡查负责人" value="巡查负责人"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="姓名：" :prop="isAdd ? 'username' : ''">
        <el-input
          v-model="form.username"
          :disabled="!isAdd"
          placeholder="请输入"
          maxlength="50"
        ></el-input>
      </el-form-item>
      <el-form-item label="手机：" :prop="isAdd ? 'phone' : ''">
        <el-input
          maxlength="11"
          oninput="this.value = this.value.replace(/[^0-9]/g, '')"
          v-model="form.phone"
          :disabled="!isAdd"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
      <el-form-item :label="isAdd ? '登录密码：' : '新密码：'" prop="password">
        <el-input
          v-model.trim="form.password"
          placeholder="请输入"
          maxlength="18"
        ></el-input>
      </el-form-item>
      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeEditDialog');
            }
          "
          >取消</el-button
        >
        <el-button type="primary" @click="submitForm('form')">确定</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { propertyPeopleApi } from "@/api/propertyManage";
export default {
  name: "PropertyPeopleForm",
  props: {
    id: {
      type: String,
      default: "",
    },
    deptId: [String, Number],
  },
  components: {},
  data() {
    return {
      form: {
        username: "",
        phone: "",
        password: "",
        post: ''
      },
      rules: {
        username: [{ required: true, message: "必填项", trigger: "blur" }],
        phone: [{ required: true, message: "必填项", trigger: "blur" }],
        password: [{ required: true, message: "必填项", trigger: "blur" }],
        post:[{ required: true, message: "必填项", trigger: "change" }]
      },
      submitFlag: true,
      isAdd: true,
    };
  },
  mounted() {
    if (this.id) {
      this.isAdd = false;
      this.getDetail();
    } else {
      this.isAdd = true;
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          this.form.userType = 3;
          this.form.deptId = this.deptId;
          if (this.id) {
            this.pwd();
          } else {
            this.add();
          }
        } else {
          return false;
        }
      });
    },
    async add() {
      propertyPeopleApi
        .save(this.form)
        .then((res) => {
          if (res.code == 0) {
            this.$message.success("保存成功");
            this.$emit("closeEditDialog", 1);
          } else {
            this.$message.error(res.msg);
          }
          this.submitFlag = true;
        })
        .catch((err) => {
          this.submitFlag = true;
          this.$message.error(err);
        });
    },
    async pwd() {
      propertyPeopleApi
        .pwd({
          id: this.form.id,
          password: this.form.password,
        })
        .then((res) => {
          if (res.code == 0) {
            this.$message.success("密码修改成功");
            this.$emit("closeEditDialog", 1);
          } else {
            this.$message.error(res.msg);
          }
          this.submitFlag = true;
        })
        .catch((err) => {
          this.submitFlag = true;
          this.$message.error(err);
        });
    },
    async getDetail() {
      let res = await propertyPeopleApi.list({
        id: this.id,
        deptId: this.deptId,
      });
      if (res.code == 0) {
        Object.assign(this.form, res.data.list[0]);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap-form-box {
  .training-form-box {
    display: flex;
    width: 100%;
    justify-content: space-between;
    > div {
      width: 48%;
    }
  }
  .submit-box {
    text-align: right;
  }
}
</style>
