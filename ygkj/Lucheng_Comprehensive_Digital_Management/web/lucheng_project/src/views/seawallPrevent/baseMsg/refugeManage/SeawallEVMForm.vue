<!--
 * @Author: hanyu
 * @LastEditTime: 2022-01-11 13:40:27
 * @Description: 海塘极值管理-表单
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\refugeManage\SeawallEVMForm.vue
-->
<template>
  <div class="form">
    <el-form :model="formData" status-icon :rules="rules" ref="ruleForm" :label-width="labelWidth">
      <el-form-item label="海塘名称：" prop="seawallName">
        <el-input disabled v-model="formData.seawallName" placeholder="请输入海塘名称"></el-input>
      </el-form-item>
      <el-form-item label="红色警戒水位：" prop="tideStationRedLevel">
        <el-input type="number" v-model="formData.tideStationRedLevel" placeholder="请输入红色警戒水位"></el-input>
      </el-form-item>
      <el-form-item label="橙色警戒水位：" prop="tideStationOrangeLevel">
        <el-input type="number" v-model="formData.tideStationOrangeLevel" placeholder="请输入橙色警戒水位"></el-input>
      </el-form-item>
      <el-form-item label="黄色警戒水位：" prop="tideStationYellowLevel">
        <el-input type="number" v-model="formData.tideStationYellowLevel" placeholder="请输入黄色警戒水位"></el-input>
      </el-form-item>
      <el-form-item label="蓝色警戒水位：" prop="tideStationBlueLevel">
        <el-input type="number" v-model="formData.tideStationBlueLevel" placeholder="请输入蓝色警戒水位"></el-input>
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { seaController } from "@/api/seawallPrevent";
export default {
  name: "RefugeManageForm",
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
        tideStationBlueLevel: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        tideStationYellowLevel: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        tideStationOrangeLevel: [
          { required: true, message: "必填项", trigger: "blur" },
        ],
        tideStationRedLevel: [
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
      let res = await seaController.page({
        seawallCode: this.stCode,
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
      const {
        seawallCode,
        tideStationBlueLevel,
        tideStationYellowLevel,
        tideStationOrangeLevel,
        tideStationRedLevel,
      } = data;
      let formData = {
        seawallCode,
        tideStationBlueLevel,
        tideStationYellowLevel,
        tideStationOrangeLevel,
        tideStationRedLevel,
      };
      let res = await seaController.update(formData);
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

    closeMapSelectDialog() {
      this.showMapDialog = false;
      this.showMapSelect = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
}
</style>
