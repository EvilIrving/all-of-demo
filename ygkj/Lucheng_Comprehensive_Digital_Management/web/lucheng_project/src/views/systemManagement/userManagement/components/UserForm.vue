<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-01 14:33:27
 * @Description: 用户信息表单
 * @FilePath: \lucheng_project\src\views\systemManagement\userManagement\components\UserForm.vue
-->
<template>
  <div class="wrap">
    <div>
      <el-form
        :model="formData"
        :rules="rules"
        class="demo-form-inline"
        label-width="100px"
        ref="postForm"
      >
        <el-form-item label="所属角色：">
          <el-select v-model="formData.jobIds" placeholder="请选择所属角色">
            <el-option
              :label="item.jobName"
              :value="item.id"
              v-for="item in listData"
              :key="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn-box">
      <el-button size="small" @click="closeEditDialog">返回</el-button>
      <el-button size="small" type="primary" @click="submitForm('postForm')"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script>
import { sysJobListApi, bindUserJobApi } from "@/api/systemManagement.js";
export default {
  name: "UserForm",
  props: {
    id: [String, Number],
    jobId: [String, Number],
  },
  components: {},
  data() {
    return {
      formData: {
        jobIds: "",
      },
      rules: {
        jobIds: [{ required: true, message: "请选择所属角色", trigger: "blur" }],
      },
      listData: [],
      loading: false,
    };
  },
  mounted() {
    this.formData.jobIds = this.jobId;
    this.getList();
  },
  methods: {
    async getList() {
      let res = await sysJobListApi();
      if (res.code == 0) {
        this.listData = res.data.filter((item) => item.id != "admin");
      } else {
        this.$message.error("获取角色列表失败！");
      }
    },
    submitForm(postForm) {
      this.$refs[postForm].validate((valid) => {
        if (valid) {
          if (this.loading) {
            return false;
          }
          this.loading = true;
          if (this.id) {
            this.formData.id = this.id;
          }
          const formData = this.formData;
          this.save(formData);
        } else {
          return false;
        }
      });
    },
    async save(data) {
      let res = await bindUserJobApi(data);
      if (res.code == 0) {
        this.loading = false;
        this.$message({
          message: "角色绑定成功~",
          type: "success",
          duration: "1000",
        });
        this.closeEditDialog(1);
      } else {
        this.loading = false;
        this.$message.error(res.message);
      }
    },
    closeEditDialog(type) {
      this.$emit("closeEditDialog", type);
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  .btn-box {
    padding-top: 20px;
    text-align: right;
  }
}
</style>