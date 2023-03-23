<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-26 17:25:38
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\projectPlan\ProjectPlanFrom.vue
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
        <el-form-item label="时间：" prop="proprietorUnitId">
          <el-date-picker class="form-item-value" v-model="value1" type="date" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="工程名称：" prop="propertyUnitId">
          <el-input
            class="form-item-value"
            readonly
            v-model="projectDetail.name"
          ></el-input>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="编制单位：" prop="prcd">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.proprietorUnitId"
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
        <el-form-item label="审批单位：" prop="serviceTime">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.propertyUnitId"
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
      <el-form-item label="预案类型：" prop="serviceLocation">
        <el-select
          style="width: 100%"
          filterable
          v-model="ruleForm.proprietorUnitId"
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
      <el-form-item label="详情：">
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
        propertyUnitId: "",
        propertyUnitName: "",
        proprietorUnitId: "",
        proprietorUnitName: "",
        serviceLocation: "",
        serviceTime: "",
        prcd: "",
        projectName: "",
        file: "",
      },
      rules: {
        propertyUnitId: [
          { required: true, message: "请选择物业单位", trigger: "change" },
        ],
        prcd: [{ required: true, message: "请选择工程", trigger: "change" }],
        proprietorUnitId: [
          { required: true, message: "请选择业主单位", trigger: "change" },
        ],
        serviceLocation: [
          { required: true, message: "请输入服务地点", trigger: "blur" },
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
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  async mounted() {
    await this.deptList();
    await this.propertyUnit();
    if (this.id) {
      await this.loadPageOverhaul();
    }
  },
  methods: {
    closeDialog() {
      this.$emit("close");
    },
    async deptList() {
      let res = await deptListApi();
      console.log(res);
      if (res.code == 0) {
        this.deptListData = res.data;
      }
    },
    async propertyUnit() {
      let res = await propertyUnitApi.list();
      console.log(res);
      if (res.code == 0) {
        this.propertyUnitList = res.data.list;
      }
    },
    async loadPageOverhaul() {
      let res = await contractOperate.loadPageCovenant({
        id: this.id,
      });
      if (res.code == 0) {
        console.log(res, "dsdasasd");
        let data = res.data.list[0];
        let {
          propertyUnitId,
          propertyUnitName,
          proprietorUnitId,
          proprietorUnitName,
          serviceLocation,
          file,
          id,
        } = data;
        this.ruleForm = {
          propertyUnitId,
          propertyUnitName,
          proprietorUnitId,
          proprietorUnitName,
          serviceLocation,
          file,
          id,
        };
        this.ruleForm["serviceTime"] = [
          data.serviceStartTime,
          data.serviceEndTime,
        ];
        console.log(this.ruleForm, "111111111111111111");
        for (let item of this.options) {
          let projectData = item.children.find((ite) => ite.value == data.prcd);
          console.log(projectData);
          if (projectData) {
            this.ruleForm["prcd"] = [item.value, projectData.value];
            break;
          }
        }
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
        this.ruleForm.file = files.id;
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
            let proprietorUnitName = this.deptListData.find(
              (item) => item.id === this.ruleForm.proprietorUnitId
            );
            let propertyUnitName = this.propertyUnitList.find(
              (item) => item.id === this.ruleForm.propertyUnitId
            );
            this.options;
            let projectData = null;
            for (let item of this.options) {
              if (item.value == ruleForm.prcd[0]) {
                projectData = item.children.find(
                  (ite) => ite.value == ruleForm.prcd[1]
                );
                break;
              }
            }
            ruleForm.projectName = projectData.label;
            ruleForm.prcd = projectData.value;
            ruleForm.proprietorUnitName = proprietorUnitName.name;
            ruleForm.propertyUnitName = propertyUnitName.companyName;
            ruleForm.serviceStartTime = ruleForm.serviceTime[0];
            ruleForm.serviceEndTime = ruleForm.serviceTime[1];
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
    width: 300px;
  }
}
.btn-box {
  text-align: right;
}
</style>