<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-01 14:35:20
 * @Description: 用户信息表单
 * @FilePath: \lucheng_project\src\views\systemManagement\authorityManagement\components\JobForm.vue
-->
<template>
  <div class="dialog_wrap">
    <el-form
      ref="postForm"
      :model="postForm"
      :rules="rules"
      class="form-container"
    >
      <el-form-item label-width="30%" label="角色名称" prop="jobName">
        <el-input
          v-model="postForm.jobName"
          placeholder="请输入角色名称"
          style="width: 70%"
        />
      </el-form-item>
      <el-form-item label-width="30%" label="角色描述" prop="jobDescribe">
        <el-input
          v-model="postForm.jobDescribe"
          placeholder="请输入角色描述"
          type="textarea"
          rows="2"
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
import { sysJobListApi, sysJobSaveApi } from "@/api/systemManagement";
export default {
  name: "JobForm",
  props: {
    id: [String, Number],
  },
  components: {},
  data() {
    return {
      loading: false,
      postForm: {
        jobName: "",
        jobDescribe: "",
      },
      rules: {
        jobName: [
          { required: true, message: "请输入角色名称", trigger: "blur" },
        ],
        jobDescribe: [
          { required: true, message: "请输入角色描述", trigger: "blur" },
        ],
      },
      saveType: false,
    };
  },
  mounted() {
    if (this.id) {
      this.getData();
    }
  },
  methods: {
    async getData() {
      let res = await sysJobListApi({
        id: this.id,
      });
      if (res.code == 0) {
        this.postForm = res.data[0];
      }
    },
    submitForm(postForm) {
      this.$refs[postForm].validate((valid) => {
        if (valid) {
          if (this.saveType) {
            return false;
          }
          this.saveType = true;
          this.loading = true;
          if (this.id) {
            this.postForm.id = this.id;
          } else {
            this.postForm.flag = this.flag;
            this.postForm.pId = this.pId;
          }
          delete this.postForm.createTime;
          const formData = this.postForm;
          this.save(formData);
        } else {
          return false;
        }
      });
    },
    async save(data) {
      let res = await sysJobSaveApi(data);
      if (res.code == 0) {
        this.loading = false;
        this.$message({
          message: data.id ? "新增成功" : "修改成功",
          type: "success",
          duration: "1000",
        });
        this.closeEditDialog(1);
      } else {
        this.loading = false;
        this.$message.error(res.message);
      }
      this.saveType = false;
    },
    closeEditDialog(type) {
      this.$emit("closeEditDialog", type);
    },
  },
};
</script>

<style lang='scss' scoped>
.dialog_wrap {
}
</style>