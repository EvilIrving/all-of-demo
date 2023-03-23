<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-02 19:30:31
 * @Description: 海塘极值管理-表单
 * @FilePath: /lucheng_project/src/views/seawallPrevent/baseMsg/refugeManage/RefugeManageForm.vue
-->
<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      :label-width="labelWidth"
    >
      <el-form-item label="测站名称：" prop="stName">
        <el-input
          disabled
          v-model="formData.stName"
          placeholder="请输入测站名称："
        ></el-input>
      </el-form-item>
      <el-form-item label="汛限水位：" prop="limitLevel">
        <el-input
          type="number"
          v-model="formData.limitLevel"
          placeholder="请输入汛限水位"
        ></el-input>
      </el-form-item>
      <el-form-item label="保证水位：" prop="guaranteeLevel">
        <el-input
          type="number"
          v-model="formData.guaranteeLevel"
          placeholder="请输入保证水位"
        ></el-input>
      </el-form-item>
      <el-form-item label="警戒水位：" prop="warningLevel">
        <el-input
          type="number"
          v-model="formData.warningLevel"
          placeholder="请输入警戒水位"
        ></el-input>
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')"
          >保存</el-button
        >
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { stController } from "@/api/seawallPrevent";
export default {
  name: "StationEVMForm",
  props: {
    stCode: [String, Number],
  },
  components: {},
  computed: {
    projectDetail() {
      return this.$store.state.projectDetail;
    },
  },
  data() {
    return {
      labelWidth: "25%",
      formData: {},
      rules: {
        warningLevel: [{ required: true, message: "必填项", trigger: "blur" }],
        limitLevel: [{ required: true, message: "必填项", trigger: "blur" }],
        guaranteeLevel: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
      },
    };
  },
  mounted() {
    if (this.stCode) {
      this.getDetail();
    }
  },
  methods: {
    async getDetail() {
      let res = await stController.page({
        stCode: this.stCode,
      });
      if (res.code == 0) {
        this.formData = res.data.list[0];
      }
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.save(this.formData);
        } else {
          return false;
        }
      });
    },

    async save(data) {
      let res = await stController.update(data);
      if (res.code == 0) {
        this.$message({
          message: "保存成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.closeEditDialog();
            this.$emit("getList");
          },
        });
      } else {
        this.$message.error("保存失败！");
      }
    },

    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
  },
};
</script>

