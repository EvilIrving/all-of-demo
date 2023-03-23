<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      :label-width="labelWidth"
    >
      <el-form-item label="服务单位：" prop="companyId">
        <el-select
          style="width: 100%"
          v-model="formData.companyId"
          placeholder="请选择服务单位"
          @change="getBindProject"
        >
          <el-option
            v-for="item in unitList"
            :key="item.id"
            :value="item.id"
            :label="item.companyName"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="建设单位：" prop="projectId">
        <el-select
          style="width: 100%"
          v-model="formData.projectId"
          placeholder="请选择建设单位"
          @change="typeChange"
        >
          <el-option
            v-for="item in projectList"
            :key="item.id"
            :value="item.id"
            :label="item.name"
          ></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="工程类型：" prop="projectType">
        <el-input
          v-model="formData.projectType"
          readonly
          placeholder="请输入"
        ></el-input>
      </el-form-item> -->
      <el-form-item label="服务时间：" prop="serviceTime">
        <!-- <el-date-picker v-model="time" type="datetime" placeholder="请输入服务时间" :default-time="defaultTime"> -->
        <el-date-picker
          style="width: 100%"
          v-model="formData.serviceTime"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="至"
          placeholder="请输入服务时间"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="服务事项：" prop="projectItem">
        <el-input
          type="textarea"
          v-model="formData.projectItem"
          placeholder="请输入服务事项"
          clearable
        ></el-input>
      </el-form-item>
      <div class="footer_btn">
        <el-button size="mini" type="primary" @click="submitForm('ruleForm')"
          >确定</el-button
        >
        <el-button size="mini" @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import {
  operationUnit,
  propertyUnitApi,
  getDeptTreeTableListApi,
} from "@/api/propertyManage";
import { cloneObj } from "@/utils/utils.js";
export default {
  name: "ServiceItemFormPro",
  props: {
    id: [String, Number],
    deptId: [String],
  },
  components: {},
  data() {
    return {
      unitList: [],
      projectList: [],
      typeOptions: [],
      projectType: "",
      defaultTime: "",
      labelWidth: "25%",
      formData: {
        companyId: "",
        serviceStartTime: "",
        serviceEndTime: "",
        projectId: "",
        projectItem: "",
        serviceTime: "",
        companyName: "",
        projectName: "",
      },
      rules: {
        companyId: [{ required: true, message: "必填项", trigger: "change" }],
        projectId: [{ required: true, message: "必填项", trigger: "change" }],
        serviceTime: [{ required: true, message: "必填项", trigger: "change" }],
        projectItem: [{ required: true, message: "必填项", trigger: "blur" }],
      },
      saveType: false,
    };
  },
  async mounted() {
    await this.getList();
    this.getDeptTreeTableList();
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    async getDeptTreeTableList() {
      let res = await getDeptTreeTableListApi();
      if (res.code === 1) {
        this.projectList = res.data;
      }
    },
    async getList() {
      let res = await propertyUnitApi.list({
        pageSize: 99999,
        pageNum: 1,
      });
      if (res.code == 0) {
        this.unitList = res.data.list;
      }
    },
    // async getProjectList() {
    //   let res = await getAllProjectList();
    //   if (res.code === 0) {
    //     this.projectList = res.data;
    //   }
    // },
    async getBindProject() {
      let companyData = this.unitList.find(
        (item) => item.id == this.formData.companyId
      );
      this.formData.companyName = companyData.companyName;
    },
    typeChange(val) {
      let data = this.projectList.find((item) => item.id == val);
      this.formData.projectName = data.name;
    },
    async getDetail() {
      let res = await operationUnit.servicelist({
        id: this.id,
      });
      if (res.code == 0) {
        if (res.data.list[0]) {
          let {
            companyName,
            projectName,
            companyId,
            serviceStartTime,
            serviceEndTime,
            projectType,
            projectId,
            projectItem,
          } = res.data.list[0];
          this.formData = {
            companyName,
            projectName,
            companyId,
            serviceStartTime,
            serviceEndTime,
            projectType,
            projectId,
            projectItem,
          };
          this.formData["serviceTime"] = [serviceStartTime, serviceEndTime];
          console.log(this.formData)
        }
      }
    },

    submitForm(formName) {
      let that = this;
      that.$refs[formName].validate((valid) => {
        if (valid) {
          if (that.id) {
            that.formData.id = that.id;
          }
          that.save(that.formData);
        } else {
          return false;
        }
      });
    },

    async save(data) {
      if (this.saveType) {
        return false;
      }
      this.saveType = true;
      let formData = cloneObj(data);
      if (formData.serviceTime) {
        formData.serviceStartTime = formData.serviceTime[0];
        formData.serviceEndTime = formData.serviceTime[1];
      }
      delete formData.serviceTime;
      formData['deptId'] = this.deptId
      let res = await operationUnit.servicesave(formData);
      if (res.code == 0) {
        this.$message({
          message: "保存成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.saveType = false;
            this.closeEditDialog();
          },
        });
      } else {
        this.saveType = false;
        this.$message.error("保存失败！");
      }
    },

    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
}
</style>