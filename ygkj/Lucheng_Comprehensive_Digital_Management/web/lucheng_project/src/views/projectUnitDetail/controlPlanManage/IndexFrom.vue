<!--
 * @Date: 2022-04-24 13:55:24
 * @Author: 
 * @Description: 合同管理-合同验收-年度验收-表单
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-06-27 15:46:15
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\controlPlanManage\IndexFrom.vue
-->
<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      label-position="right"
      label-width="120px"
    >
      <el-form-item label="年份：" prop="year">
        <el-date-picker
          style="width: 100%"
          v-model="formData.year"
          value-format="yyyy"
          type="year"
          placeholder="请选择年份"
          :clearable="false"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="控运计划文件：">
        <uploadFile
          accept=".pdf"
          @success="handleSuccess"
          @remove="handleRemove"
          :fileList="fileList"
          :size="15"
          :limit="999"
        />
      </el-form-item>
      <div class="footer_btn">
        <el-button size="mini" type="primary" @click="submitForm('ruleForm')"
          >保存</el-button
        >
        <el-button size="mini" @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { bhControlPlan } from "@/api/propertyManage";
import uploadFile from "@/components/uploadFile.vue";
export default {
  name: "SupervisedCheckForm",
  props: {
    id: [String, Number],
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  components: { uploadFile },
  data() {
    return {
      formData: {
        year: "",
        pid: "",
        fileIds: "",
      },
      rules: {
        year: [{ required: true, message: "必填项", trigger: "change" }],
      },
      fileList: [],
      submitFormType: false,
    };
  },
  async mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.fileList.push({
          name: files.originalName,
          url: files.filePath,
          ...files,
        });
      } else {
        this.$message.error(res.msg);
      }
    },
    handleRemove(data) {
      let index = this.fileList.findIndex((item) => item.id == data.id);
      if (index != -1) {
        this.fileList.splice(index, 1);
      }
    },
    async getDetail() {
      let res = await bhControlPlan.list({
        id: this.id,
      });
      if (res.code == 0) {
        if (res.data.list.length > 0) {
          let data = res.data.list[0];
          this.formData.year = data.year;
          if (data.fileIds) {
            data.fileList.forEach((item) => {
              this.fileList.push({
                name: item.fileName,
                url: item.fileUrl,
                ...item,
              });
            });
          }
        }
      }
    },
    submitForm(formName) {
      let that = this;
      that.$refs[formName].validate((valid) => {
        if (valid) {
          if (that.id) {
            that.formData["id"] = that.id;
          }
          this.formData.pid = this.projectInfo.prcd;
          that.save();
        } else {
          return false;
        }
      });
    },

    async save() {
      if (this.submitFormType) {
        return false;
      }
      this.submitFormType = true;
      if (this.fileList.length > 0) {
        let arr = [];
        this.fileList.forEach((item) => {
          arr.push(item.id);
        });
        this.formData.fileIds = arr.join(",");
      } else {
        this.formData.fileIds = "";
      }
      let res = await bhControlPlan.save(this.formData);
      if (res.code == 0) {
        this.$message({
          message: "保存成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.submitFormType = false;
            this.closeEditDialog(1);
          },
        });
      } else {
        this.submitFormType = false;
        this.$message.error("保存失败！");
      }
    },

    async getResponseList() {
      let res = await supervisedCheck.joinUnits();
      if (res.code == 0) {
        this.responsibleUnitOptions = res.data;
      }
    },
    closeEditDialog(data) {
      this.$emit("closeEditDialog", data);
    },
  },
};
</script>
