<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-10 16:58:52
 * @Description: 
 * @FilePath: \lucheng_project\src\views\seawallPrevent\riskEarlyWarning\peopleManagement\PeopleFrom.vue
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
      <el-form-item label="名称：" prop="name">
        <el-input
          :disabled="nameDisabled"
          v-model="formData.name"
          placeholder="请输入名称："
        ></el-input>
      </el-form-item>
      <el-form-item label="电话：" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item label="职务：" prop="duty">
        <el-input v-model="formData.duty" placeholder="请输入职务"></el-input>
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
import { customPeople } from "@/api/seawallPrevent";
export default {
  name: "StationEVMForm",
  props: {
    isAdd: [String, Number],
    id: [String, Number],
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
      nameDisabled: false,
      typeValue: "",
      rules: {
        name: [{ required: true, message: "必填项", trigger: "blur" }],
        phone: [{ required: true, message: "必填项", trigger: "blur" }],
        duty: [{ required: true, message: "必填项", trigger: "blur" }],
      },
    };
  },
  mounted() {
    !this.isAdd ? this.getDetail() : "";
    this.isAdd ? (this.nameDisabled = false) : (this.nameDisabled = true);
  },
  methods: {
    async getDetail() {
      let res = await customPeople.list({
        id: this.id,
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
      this.formData.type = 2;
      if (this.isAdd) {
        this.formData.deptid = this.id;
        const { name, phone, duty, deptid, type } = this.formData;
        let temp = { name, phone, duty, deptid, type };
        let res = await customPeople.insert(temp);
        if (res.code === 0) {
          this.$message({
            message: "新增人员成功",
            type: "success",
            duration: "1000",
            onClose: () => {
              this.closeEditDialog();
              this.$emit("getPerson");
            },
          });
        } else {
          this.$message.error("新增失败！");
        }
      } else {
        this.formData.id = this.id;
        const { name, phone, duty, id, type } = this.formData;
        let temp = { name, phone, duty, id, type };
        let res = await customPeople.update(temp);
        if (res.code == 0) {
          this.$message({
            message: "更新人员成功",
            type: "success",
            duration: "1000",
            onClose: () => {
              this.closeEditDialog();
              this.$emit("getPerson");
            },
          });
        } else {
          this.$message.error("更新失败！");
        }
      }
    },

    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
  },
};
</script>
