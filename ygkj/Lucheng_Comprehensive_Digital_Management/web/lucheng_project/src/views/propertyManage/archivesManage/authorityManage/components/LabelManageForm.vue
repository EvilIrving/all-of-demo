<!--
 * @Author: zhongg
 * @LastEditTime: 2021-10-25 10:43:56
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: /lucheng_project/src/views/propertyManage/archivesManage/authorityManage/components/LabelManageForm.vue
-->
<template>
  <div class="user-form">
    <el-form
      ref="labelManageForm"
      :model="labelManageForm"
      :rules="labelRules"
      label-width="100px"
    >
      <!-- id -->
      <el-form-item label="id" prop="id" v-show="false"></el-form-item>

      <div class="form_item">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="标签名称" prop="name">
              <el-input
                v-model="labelManageForm.name"
                placeholder="请输入标签名称"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!-- 按钮 -->
      <div class="el-dialog__footer">
        <el-row :gutter="20">
          <el-form-item>
            <div class="form-footer">
              <el-button
                class="form-btn cancel-btn"
                size="mini"
                @click="$emit('cancelDialog')"
                >取消</el-button
              >
              <el-button class="form-btn save-btn" size="mini" @click="onSubmit"
                >确定</el-button
              >
            </div>
          </el-form-item>
        </el-row>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getLabelByIdApi } from "@/api/propertyManage";
export default {
  name: "LabelManageForm",
  props: {},
  components: {},
  data() {
    return {
      labelManageForm: {
        id: "",
        name: "",
      },
      labelRules: {
        name: [{ required: true, message: "请输入标签名称", trigger: "blur" }],
      },
    };
  },
  mounted() {},
  computed: {
    headers: function () {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
  },
  methods: {
    onSubmit() {
      this.$refs.labelManageForm.validate((valid) => {
        if (valid) {
          this.$emit("addLabel", this.labelManageForm);
        }
      });
    },
    resetForm() {
      this.$refs.labelManageForm.resetFields();
    },
    async getFormData(id, code) {
      await getLabelByIdApi({ id }).then((res) => {
        const { id, name } = res.data;
        this.labelManageForm = {
          id,
          name,
        };
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.el-dialog__footer {
  border-top: 2px solid #dcdfe6;
  padding: 0;
  .el-form-item {
    margin-bottom: 0;
  }
}
.user-form {
  width: 100%;
  .el-form {
    width: 100%;
    background: #fff;
    padding: 8px 0;
    .form_item {
      margin-bottom: 8px;
      .el-form-item {
        margin-bottom: 8px;
      }
    }
  }
}
</style>
