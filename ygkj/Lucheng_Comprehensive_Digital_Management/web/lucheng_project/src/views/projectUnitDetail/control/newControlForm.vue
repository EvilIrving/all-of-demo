<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="日期：" prop="materialDate">
            <el-date-picker
              type="date"
              placeholder="请选择"
              v-model="form.materialDate"
              style="width: 300px"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="工程名称：" prop="name">
            <el-input
              v-model="form.name"
              placeholder="请输入"
              maxlength="50"
              style="width: 300px"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="11">
          <el-form-item label="工程管理单位：" prop="managementUnit">
            <el-input
              v-model="form.managementUnit"
              placeholder="请输入"
              maxlength="50"
              style="width: 300px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="委托单位：" prop="client">
            <el-input
              v-model="form.client"
              placeholder="请输入"
              maxlength="50"
              style="width: 300px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="防治类型：" prop="ptType">
        <el-select
          v-model="form.ptType"
          placeholder="请选择"
          clearable
          style="width: 300px"
        >
          <el-option
            v-for="item in typeOptions"
            :key="item.label"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="合同：">
        <upload-file
          @success="handleSuccessOne"
          @remove="handleRemoveOne"
          accept=".pdf"
          :size="15"
          name="uploadFile"
          :fileList="contractList"
        ></upload-file>
      </el-form-item>
      <el-form-item label="添加文件：">
        <upload-file
          @success="handleSuccessTwo"
          @remove="handleRemoveTwo"
          accept=".pdf"
          :size="15"
          name="uploadFile"
          :fileList="resultsReportList"
        ></upload-file>
      </el-form-item>

      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeEditDialog');
            }
          "
          >取消</el-button
        >
        <el-button type="primary" @click="submitForm('form')">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import uploadFile from "@/components/uploadFile";
import { biologicalControl } from "@/api/projectUnitDetail";
export default {
  name: "newControlForm",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: {
    uploadFile,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      form: {
        // type:'',
        name: "",
        managementUnit: "",
        materialDate: "",
        client: "",
        ptType: "",
        contract: "",
        resultsReport: "",
      },
      typeOptions: [
        { label: "白蚁防治", value: 1 },
        { label: "外来生物防治", value: 2 },
      ],
      contractList: [],
      resultsReportList: [],

      submitFlag: true,
    };
  },
  mounted() {
    this.form.name = this.projectInfo.name;
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime;
          this.form["prcd"] = this.projectInfo.prcd;
          biologicalControl
            .saveBiologicalControl(this.form)
            .then((res) => {
              console.log(res);
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                //this.$message.error(res.msg);
                this.$message.error({ message: res.msg || "添加失败" });
              }
              this.submitFlag = true;
            })
            .catch((err) => {
              this.submitFlag = true;
              this.$message.error(err);
            });
        } else {
          return false;
        }
      });
    },
    handleSuccessOne(res) {
      if (res.file.code == 0) {
        this.form.contract = res.file.data.id;
      }
    },
    handleSuccessTwo(res) {
      if (res.file.code == 0) {
        this.form.resultsReport = res.file.data.id;
      }
    },
    handleRemoveOne() {
      this.form.contract = "";
      this.contractList = []
    },
    handleRemoveTwo() {
      this.form.resultsReport = "";
      this.resultsReportList = []
    },

    async getDetail() {
      let res = await biologicalControl.getBiologicalControlDetail({
        id: this.id,
      });
      if (res.code == 0) {
        console.log(this.id, res);
        // Object.assign(this.form, res.data)
        //this.form.annual = res.data.list[0].annual.toString()
        this.form.managementUnit = res.data.managementUnit;
        this.form.materialDate = res.data.materialDate;
        this.form.client = res.data.client;
        this.form.ptType = res.data.ptType;
        this.form.contract = res.data.contract;
        this.form.resultsReport = res.data.resultsReport;
        this.form.id = res.data.id;
        if (res.data.contractFiles && res.data.contractFiles.length > 0) {
          this.contractList = res.data.contractFiles.map((item) => {
            return {
              ...item,
            };
          });
        }
        if (res.data.resultsReportFiles && res.data.resultsReportFiles.length > 0) {
          this.resultsReportList = res.data.resultsReportFiles.map((item) => {
            return {
              ...item,
            };
          });
        }
      }
    },
  },
};
</script>

<style scoped lang="scss">
.wrap-form-box {
  .training-form-box {
    display: flex;
    width: 100%;
    justify-content: space-between;
    > div {
      width: 48%;
    }
  }
  .submit-box {
    text-align: right;
  }
}
</style>