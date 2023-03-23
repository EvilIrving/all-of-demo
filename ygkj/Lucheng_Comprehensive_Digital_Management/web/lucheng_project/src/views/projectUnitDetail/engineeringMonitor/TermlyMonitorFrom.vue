<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-17 17:16:37
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringMonitor\TermlyMonitorFrom.vue
-->
<template>
  <div>
    <el-form
      :model="ruleForm"
      ref="ruleForm"
      label-width="160px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="年份：">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.year"
            type="year"
            placeholder="请选择"
            format="yyyy"
            value-format="yyyy"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="水库管理单位负责人：">
          <el-input
            class="form-item-value"
            placeholder="请输入"
            v-model="ruleForm.manager"
          >
          </el-input>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="监测日期：">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.monitorDate"
            type="date"
            placeholder="请选择"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="监测单位：">
          <el-input
            class="form-item-value"
            placeholder="请输入"
            v-model="ruleForm.monitorUnit"
          >
          </el-input>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="监测类型：">
          <el-select
            class="form-item-value"
            v-model="ruleForm.monitorType"
            placeholder="请选择"
          >
            <el-option label="沉降观测" value="沉降观测"> </el-option>
            <el-option label="位移观测" value="位移观测"> </el-option>
            <el-option label="渗流监测" value="渗流监测"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="与前期对比是否异常：">
          <div class="form-item-value">
            <el-radio v-model="ruleForm.compared" label="正常">正常</el-radio>
            <el-radio v-model="ruleForm.compared" label="异常">异常</el-radio>
          </div>
        </el-form-item>
      </div>

      <el-form-item label="上传附件：">
        <div class="form-item-box">
          <p>安全监测分析报告</p>
          <upload-file
            accept=".pdf"
            @success="handleSuccessAnalyse"
            @remove="handleRemoveAnalyse"
            :fileList="fileListAnalyse"
            :size="15"
          />
        </div>
      </el-form-item>
      <el-form-item label="上传附件：">
        <div class="form-item-box">
          <p>安全监测整编报告</p>
          <upload-file
            accept=".pdf"
            @success="handleSuccessReorganize"
            @remove="handleRemoveReorganize"
            :fileList="fileListReorganize"
            :size="15"
          />
        </div>
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
import uploadFile from "@/components/uploadFile.vue";
import { regularMonitor } from "@/api/propertyManage.js";
export default {
  props: {
    id: String,
  },
  components: { uploadFile },
  data() {
    return {
      ruleForm: {
        analyzeReport: "",
        compared: "",
        manager: "",
        reorganizationReport: "",
        year: "",
        monitorType: "",
        monitorUnit: "",
        monitorDate: "",
      },
      // rules: {
      //   principalId: [
      //     { required: true, message: "请选择工作负责人", trigger: "change" },
      //   ],
      //   issueManId: [
      //     { required: true, message: "请选择签发负责人", trigger: "change" },
      //   ],
      //   issueDate: [
      //     {
      //       required: true,
      //       message: "请选择签发日期",
      //       trigger: "change",
      //     },
      //   ],
      // },
      fileListAnalyse: [],
      fileListReorganize: [],
      submitFlag: true,
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  async mounted() {
    // console.log(this.$sessionData("get", "userInfo"));
    if (this.id) {
      await this.getLoadPageDisDom();
    }
    // this.ruleForm.pid = this.projectDetail.prcd;
  },
  methods: {
    closeDialog() {
      this.$emit("close");
    },
    async getLoadPageDisDom() {
      let res = await regularMonitor.list({
        id: this.id,
      });
      if (res.code == 0) {
        let data = res.data.list[0];
        // console.log(data);
        let {
          analyzeReport,
          compared,
          manager,
          reorganizationReport,
          year,
          monitorType,
          monitorDate,
          monitorUnit,
        } = data;
        this.ruleForm = {
          id: data.id,
          analyzeReport,
          compared,
          manager,
          reorganizationReport,
          year: String(year),
          monitorType,
          monitorDate,
          monitorUnit,
        };
        if (this.ruleForm.analyzeReport) {
          this.fileListAnalyse = data.analyzeReportList.map((item) => {
            return {
              name: item.fileName,
              url: item.fileUrl,
            };
          });
        }
        if (this.ruleForm.reorganizationReport) {
          this.fileListReorganize = data.reorganizationReportList.map(
            (item) => {
              return {
                name: item.fileName,
                url: item.fileUrl,
              };
            }
          );
        }
        // this.fileList = data.uploadFiles.map((item) => {
        //   return {
        //     name: item.fileName,
        //     url: item.fileUrl,
        //   };
        // });
        // console.log(this.fileList, "this.fileListthis.fileLists");
      }
    },
    handleSuccessAnalyse(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.ruleForm.analyzeReport = files.id;
        this.fileListAnalyse = [
          {
            name: files.originalName,
            url: files.filePath,
          },
        ];
      } else {
        this.$message.error(res.msg);
        this.fileListAnalyse = [];
      }
    },
    handleRemoveAnalyse() {
      this.ruleForm.analyzeReport = "";
      this.fileListAnalyse = [];
    },
    handleSuccessReorganize(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.ruleForm.reorganizationReport = files.id;
        this.fileListReorganize = [
          {
            name: files.originalName,
            url: files.filePath,
          },
        ];
      } else {
        this.$message.error(res.msg);
        this.fileListReorganize = [];
      }
    },
    handleRemoveReorganize() {
      this.ruleForm.reorganizationReport = "";
      this.fileListReorganize = [];
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.submitFlag) {
            this.submitFlag = false;
            regularMonitor
              .save(this.ruleForm)
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
.form-item-box {
  padding-top: 13px;
  > p {
    color: #666666;
    line-height: 14px;
    font-size: 14px;
  }
}
</style>