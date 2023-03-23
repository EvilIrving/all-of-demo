<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-22 11:28:52
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\recondition\mechanicalFrom.vue
-->
<template>
  <div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="120px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="工程名称：">
          <el-input
            class="form-item-value"
            readonly
            v-model="projectDetail.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="工作负责人：" prop="principalId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.principalId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="签发负责人：" prop="issueManId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.issueManId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="签发日期：" prop="issueDate">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.issueDate"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="请选择"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <el-form-item label="上传附件：">
        <uploadFile
          accept=".pdf,.jpg,.jpeg,.png"
          @success="handleSuccess"
          @remove="handleRemove"
          @review="handlePreview"
          :fileList="fileList"
          :size="15"
        />
      </el-form-item>
      <el-form-item>
        <div class="btn-box">
          <el-button size="small" @click="closeDialog">取 消</el-button>
          <el-button size="small" type="primary" @click="submitForm('ruleForm')"
            >保存</el-button
          >
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { operation } from "@/api/projectUnitDetail.js";
import uploadFile from "@/components/uploadFile.vue";
export default {
  props: {
    id: String,
    options: Array,
  },
  components: { uploadFile },
  data() {
    return {
      ruleForm: {
        pid: "",
        issueMan: "",
        issueManId: "",
        issueDate: "",
        fileIds: "",
        principal: "",
        principalId: "",
        type: 2,
      },
      rules: {
        principalId: [
          { required: true, message: "请选择工作负责人", trigger: "change" },
        ],
        issueManId: [
          { required: true, message: "请选择签发负责人", trigger: "change" },
        ],
        issueDate: [
          {
            required: true,
            message: "请选择签发日期",
            trigger: "change",
          },
        ],
      },
      fileList: [],
      submitFlag: true,
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  async mounted() {
    if (this.id) {
      await this.loadPageOverhaul();
    }
    this.ruleForm.pid = this.projectDetail.prcd;
  },
  methods: {
    closeDialog() {
      this.$emit("close");
    },
    async loadPageOverhaul() {
      let res = await operation.loadPageOverhaul({
        pid: this.projectDetail.prcd,
        type: 2,
        id: this.id,
      });
      if (res.code == 0) {
        console.log(res, "dsdasasd");
        let data = res.data.list[0];
        let {
          pid,
          issueMan,
          issueManId,
          issueDate,
          fileIds,
          principal,
          principalId,
          id,
        } = data;
        this.ruleForm = {
          pid,
          issueMan,
          issueManId,
          issueDate,
          fileIds,
          principal,
          principalId,
          id,
          type: 2,
        };
        this.fileList = data.files.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
          };
        });
      }
    },
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.ruleForm.fileIds = files.id;
        this.fileList = [
          {
            name: files.originalName,
            url: files.filePath,
          },
        ];
      } else {
        this.$message.error(res.msg);
        this.fileList = [];
      }
    },
    handleRemove() {
      this.ruleForm.fileIds = "";
      this.fileList = [];
    },
    handlePreview(data) {
      console.log(data);
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.submitFlag) {
            this.submitFlag = false;
            let principal = this.options.find(
              (item) => item.value === this.ruleForm.principalId
            );
            let issueMan = this.options.find(
              (item) => item.value === this.ruleForm.issueManId
            );
            this.ruleForm.principal = principal.label;
            this.ruleForm.issueMan = issueMan.label;
            operation
              .saveOrUpdateOverhaul(this.ruleForm)
              .then((res) => {
                if (res.code == 0) {
                  this.$message.success("保存成功~");
                  this.$emit("close", 1);
                } else {
                  this.$message.error(res.message);
                }
                this.submitFlag = true;
              })
              .catch((err) => {
                this.submitFlag = true;
                this.$message.error(err);
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.form-item {
  display: flex;
  width: 100%;
  justify-content: space-between;
  .form-item-value {
    width: 300px;
  }
}
.btn-box {
  text-align: right;
}
</style>