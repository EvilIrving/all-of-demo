<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      :label-width="labelWidth"
    >
      <el-form-item label="机构全称：" prop="unitName">
        <el-input
          v-model="formData.unitName"
          placeholder="请输入机构全称："
        ></el-input>
      </el-form-item>
      <el-form-item label="单位地址：" prop="unitAddress">
        <el-input
          v-model="formData.unitAddress"
          placeholder="请输入单位地址"
        ></el-input>
      </el-form-item>
      <el-form-item label="单位性质：" prop="unitNature">
        <el-input
          v-model="formData.unitNature"
          placeholder="请输入单位性质"
        ></el-input>
      </el-form-item>
      <el-form-item label="主管部门：" prop="mainDepartment">
        <el-input
          v-model="formData.mainDepartment"
          placeholder="请输入主管部门"
        ></el-input>
      </el-form-item>
      <el-form-item label="单位联系电话：" prop="unitPhone">
        <el-input
          v-model="formData.unitPhone"
          placeholder="请输入单位联系电话"
        ></el-input>
      </el-form-item>
      <el-form-item label="单位汛期值班电话：" prop="unitPhoneInFloodReason">
        <el-input
          v-model="formData.unitPhoneInFloodReason"
          placeholder="请输入单位汛期值班电话"
        ></el-input>
      </el-form-item>
      <el-form-item label="单位传真：" prop="unitFax">
        <el-input
          v-model="formData.unitFax"
          placeholder="请输入单位传真"
        ></el-input>
      </el-form-item>
      <el-form-item label="单位批文：" prop="dtmElev">
        <el-input
          v-model="formData.dtmElev"
          placeholder="请输入单位批文"
        ></el-input>
      </el-form-item>
      <el-form-item label="单位职责：" prop="unitResponsibilities">
        <el-input
          type="textarea"
          class="textarea-box"
          v-model="formData.unitResponsibilities"
          placeholder="请输入单位职责"
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
import { operationUnit } from "@/api/propertyManage";
export default {
  name: "UnitDutyForm",
  props: {
    id: [String, Number],
  },
  components: {},
  computed: {},
  data() {
    return {
      labelWidth: "25%",
      formData: {
        unitName: "",
        unitAddress: "",
        unitNature: "",
        mainDepartment: "",
        unitPhone: "",
        unitPhoneInFloodReason: "",
        unitFax: "",
      },
      rules: {
        unitName: [{ required: true, message: "必填项", trigger: "blur" }],
        // unitAddress: [{ required: true, message: "必填项", trigger: "blur" }],
        // unitNature: [{ required: true, message: "必填项", trigger: "blur" }],
        // mainDepartment: [{ required: true, message: "必填项", trigger: "blur" }],
        // unitPhone: [{ required: true, message: "必填项", trigger: "blur" }],
        // unitPhoneInFloodReason: [{ required: true, message: "必填项", trigger: "blur" }],
        // unitFax: [
        //   { required: true, message: "必填项", trigger: "blur" },
        // ],
      },
    };
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    async getDetail() {
      let res = await operationUnit.queryUnit({
        id: this.id,
        unitType: 1,
      });
      if (res.code == 0) {
        this.formData = res.data[0];
      }
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.formData.unitType = 1;
          this.formData.id = this.id || "";
          this.save(this.formData);
        } else {
          return false;
        }
      });
    },

    async save(data) {
      const {
        unitName,
        unitAddress,
        unitNature,
        mainDepartment,
        unitPhone,
        unitPhoneInFloodReason,
        unitFax,
        dtmElev,
        unitResponsibilities,
        unitType,
        id,
      } = data;
      let temp = {
        unitName,
        unitAddress,
        unitNature,
        mainDepartment,
        unitPhone,
        unitPhoneInFloodReason,
        unitFax,
        dtmElev,
        unitResponsibilities,
        unitType,
        id,
      };
      let res = await operationUnit.saveOrUpdateUnit(temp);
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

<style lang="scss" scoped>
.form {
  .textarea-box {
    line-height: 18px;
  }
}
</style>
