<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-08-23 14:47:35
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\appointAcceptanceStandard\covenant\CovenantForm.vue
-->
<template>
  <div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="业主单位：" prop="contractClient">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.contractClient"
            placeholder="请选择"
          >
            <el-option
              v-for="item in deptListData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="物业管理单位：" prop="contractUnit">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.contractUnit"
            placeholder="请选择"
          >
            <el-option
              v-for="item in propertyUnitList"
              :key="item.id"
              :label="item.companyName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="服务时间：" prop="serviceTime">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.serviceTime"
            value-format="yyyy-MM-dd"
            type="daterange"
            placeholder="请选择"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :clearable="false"
            @change="serviceTimeChange"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="物业合同名称：" prop="contractName">
          <el-input
            class="form-item-value"
            v-model="ruleForm.contractName"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="专项委托资质：" prop="qualification">
          <el-input
            class="form-item-value"
            v-model="ruleForm.qualification"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
        <el-form-item label="总合同经费(万元)：" prop="funds">
          <el-input
            class="form-item-value"
            v-model="ruleForm.funds"
            placeholder="请输入"
          ></el-input>
        </el-form-item>
      </div>
      <el-form-item label="物业委托管理内容：" prop="contractContent">
        <el-input
          class="form-item-value"
          v-model="ruleForm.contractContent"
          placeholder="请输入"
        ></el-input>
      </el-form-item>
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
import {
  deptListApi,
  propertyUnitApi,
  contractOperate,
} from "@/api/propertyManage.js";
import uploadFile from "@/components/uploadFile.vue";
import { cloneObj } from "@/utils/utils.js";
export default {
  props: {
    id: String,
    options: Array,
  },
  components: { uploadFile },
  data() {
    return {
      deptListData: [],
      propertyUnitList: [],
      ruleForm: {
        contractClient: "",
        contractClientNm: "",
        contractUnit: "",
        contractUnitNm: "",
        contractName: "",
        serviceTime: [],
        qualification: "",
        contractContent: '',
        funds: "",
        contractFile: "",
      },
      rules: {
        contractClient: [
          { required: true, message: "请选择业主单位", trigger: "change" },
        ],
        contractUnit: [
          { required: true, message: "请选择物业单位", trigger: "change" },
        ],
        contractName: [
          { required: true, message: "请输入物业合同名称", trigger: "blur" },
        ],
        serviceTime: [
          {
            required: true,
            message: "请选择服务时间",
            trigger: "change",
          },
        ],
      },
      fileList: [],
      submitFlag: true,
    };
  },
  async mounted() {
    await this.deptList();
    await this.propertyUnit();
    if (this.id) {
      await this.loadPageOverhaul();
    }
  },
  methods: {
    serviceTimeChange(){
      console.log(this.ruleForm.serviceTime)
    },
    closeDialog() {
      this.$emit("close");
    },
    async deptList() {
      let res = await deptListApi();
      if (res.code == 0) {
        this.deptListData = res.data;
      }
    },
    async propertyUnit() {
      let res = await propertyUnitApi.list();
      if (res.code == 0) {
        this.propertyUnitList = res.data.list;
      }
    },
    async loadPageOverhaul() {
      let res = await contractOperate.loadPageCovenant({
        id: this.id,
      });
      if (res.code == 0) {
        let data = res.data.list[0];
        let {
          contractClient,
          contractClientNm,
          contractUnit,
          contractUnitNm,
          contractName,
          qualification,
          contractContent,
          funds,
          id,
        } = data;
        this.ruleForm = {
          contractClient,
          contractClientNm,
          contractUnit,
          contractUnitNm,
          contractName,
          qualification,
          contractContent,
          funds,
          id,
        };
        let serviceTime = [data.starttm, data.endtm];
        this.fileList = data.files.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
          };
        });
        this.$set(this.ruleForm, 'serviceTime', serviceTime)
        console.log(this.ruleForm)
      }
    },
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.ruleForm.contractFile = files.id;
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
      this.ruleForm.file = "";
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
            let ruleForm = cloneObj(this.ruleForm);
            let contractClientNm = this.deptListData.find(
              (item) => item.id === this.ruleForm.contractClient
            );
            let contractUnitNm = this.propertyUnitList.find(
              (item) => item.id === this.ruleForm.contractUnit
            );
            this.options;
            ruleForm.contractClientNm = contractClientNm.name;
            ruleForm.contractUnitNm = contractUnitNm.companyName;
            ruleForm.starttm = ruleForm.serviceTime[0];
            ruleForm.endtm = ruleForm.serviceTime[1];
            delete ruleForm.serviceTime;
            contractOperate
              .saveOrUpdateCovenants(ruleForm)
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
    width: 360px;
  }
}
.btn-box {
  text-align: right;
}
</style>