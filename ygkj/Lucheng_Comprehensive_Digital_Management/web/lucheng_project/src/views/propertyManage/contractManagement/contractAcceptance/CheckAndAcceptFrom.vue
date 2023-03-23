<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-06-21 10:42:50
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\contractAcceptance\CheckAndAcceptFrom.vue
-->
<template>
  <div>
    <el-form
      :model="ruleForm"
      ref="ruleForm"
      label-width="140px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="物业管理单位：" prop="propertyUnitId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.propertyUnitId"
            placeholder="请选择"
            disabled
            @change="propertyUnitPeople(ruleForm.propertyUnitId, 1)"
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
        <el-form-item label="审核人：" prop="propertyUnitReviewerId">
          <el-select
            class="form-item-value"
            filterable
            disabled
            v-model="ruleForm.propertyUnitReviewerId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in propertyPeopleList"
              :key="item.id"
              :label="item.username"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="运行管理单位：" prop="runUnitId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.runUnitId"
            placeholder="请选择"
            :disabled="title != '新增'"
            @change="propertyUnitPeople(ruleForm.runUnitId, 2)"
          >
            <el-option
              v-for="item in operationUnit"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核人：" prop="runUnitReviewerId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.runUnitReviewerId"
            placeholder="请选择"
            :disabled="title != '新增'"
          >
            <el-option
              v-for="item in operationPeopleList"
              :key="item.id"
              :label="item.username"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="水行政主管部门：" prop="waterAdministrationUnitId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.waterAdministrationUnitId"
            :disabled="title != '新增'"
            placeholder="请选择"
            @change="propertyUnitPeople(ruleForm.waterAdministrationUnitId, 3)"
          >
            <el-option
              v-for="item in administrativeUnit"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核人：" prop="waterAdministrationUnitReviewerId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.waterAdministrationUnitReviewerId"
            placeholder="请选择"
            :disabled="title != '新增'"
          >
            <el-option
              v-for="item in administrativePeopleList"
              :key="item.id"
              :label="item.username"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="合同名称：" prop="contractName">
          <el-input
            class="form-item-value"
            placeholder="请输入内容"
            v-model="ruleForm.contractName"
            :disabled="title != '新增'"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="服务期限：" prop="serviceTime">
          <el-date-picker
            class="form-item-value"
            value-format="yyyy-MM-dd"
            v-model="ruleForm.serviceTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :clearable="false"
            :disabled="title != '新增'"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <el-form-item label="问题描述：" prop="problem">
        <el-input
          type="textarea"
          :rows="4"
          placeholder="请输入内容"
          v-model="ruleForm.problem"
          resize="none"
          :disabled="title != '新增'"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="合同附件：">
        <uploadFile
          accept=".pdf,.jpg,.jpeg,.png"
          @success="handleSuccess"
          @remove="handleRemove"
          :fileList="fileList"
          :size="15"
        />
      </el-form-item>
      <el-form-item label="验收附件：">
        <uploadFile
          accept=".pdf,.jpg,.jpeg,.png"
          @success="handleSuccessOne"
          @remove="handleRemoveOne"
          :fileList="fileListOne"
          :size="15"
        />
      </el-form-item>
      <el-form-item>
        <div class="btn-box">
          <el-button size="small" @click="closeDialog">取 消</el-button>
          <el-button
            v-if="title == '新增'"
            size="small"
            type="primary"
            @click="submitForm('ruleForm')"
            >保存</el-button
          >
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {
  supervisedCheck,
  propertyUnitApi,
  propertyPeopleApi,
} from "@/api/propertyManage.js";
import { contractApi } from "@/api/projectUnitDetail.js";
import uploadFile from "@/components/uploadFile.vue";
import { cloneObj } from "@/utils/utils.js";
export default {
  props: {
    id: String,
    title: String,
    type: String,
  },
  components: { uploadFile },
  data() {
    return {
      propertyUnitList: [],
      propertyPeopleList: [],
      operationUnit: [],
      operationPeopleList: [],
      administrativeUnit: [],
      administrativePeopleList: [],
      ruleForm: {
        propertyUnitId: "",
        propertyUnitReviewerId: "",
        propertyUnitName: "",
        runUnitId: "67074514",
        runUnitReviewerId: "",
        waterAdministrationUnitId: "",
        waterAdministrationUnitReviewerId: "",
        problem: "",
        serviceTime: "",
        serviceStartTime: "",
        serviceEndTime: "",
        contractName: "",
        acceptanceFile: "",
        contractFile: "",
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
      fileListOne: [],
      submitFlag: true,
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
    userInfo() {
      return this.$sessionData("get", "userInfo");
    },
  },
  async mounted() {
    this.ruleForm.propertyUnitReviewerId = this.userInfo.id;
    this.ruleForm.propertyUnitId = this.userInfo.deptId;
    await this.deptList("67011533");
    await this.propertyUnit();
    if (this.id) {
      await this.loadPageOverhaul();
    }
  },
  methods: {
    closeDialog() {
      this.$emit("close");
    },
    async deptList(deptIds) {
      let res = await supervisedCheck.deptList({
        deptIds,
      });
      if (res.code == 0) {
        this.operationUnit = res.data;
        this.administrativeUnit = res.data.filter(
          (item) => item.id == "67004483"
        );
        this.propertyUnitPeople(this.ruleForm.runUnitId, 2);
      }
    },
    async propertyUnit() {
      let res = await propertyUnitApi.list({
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        this.propertyUnitList = res.data.list;
        this.propertyUnitPeople(this.ruleForm.propertyUnitId, 1);
      }
    },
    async propertyUnitPeople(deptId, type) {
      let res = await propertyPeopleApi.list({
        deptId,
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        if (type == 1) {
          this.propertyPeopleList = res.data.list;
        } else if (type == 2) {
          this.operationPeopleList = res.data.list;
        } else {
          this.administrativePeopleList = res.data.list;
        }
      }
    },
    async loadPageOverhaul() {
      let res = await contractApi.list({
        id: this.id,
        type: this.type
      });
      if (res.code == 0) {
        console.log(res, "dsdasasd");
        let data = res.data.list[0];
        let {
          propertyUnitId,
          propertyUnitReviewerId,
          propertyUnitName,
          runUnitId,
          runUnitReviewerId,
          waterAdministrationUnitId,
          waterAdministrationUnitReviewerId,
          problem,
          serviceStartTime,
          serviceEndTime,
          contractName,
          acceptanceFile,
          contractFile,
        } = data;
        let serviceTime = "";
        if (serviceStartTime && serviceEndTime) {
          serviceTime = [serviceStartTime, serviceEndTime];
        }
        if (propertyUnitId) {
          await this.propertyUnitPeople(propertyUnitId, 1);
        }
        if (runUnitId) {
          await this.propertyUnitPeople(runUnitId, 2);
        }
        if (waterAdministrationUnitId) {
          await this.propertyUnitPeople(waterAdministrationUnitId, 3);
        }
        this.ruleForm = {
          propertyUnitId,
          propertyUnitReviewerId,
          propertyUnitName,
          runUnitId,
          runUnitReviewerId,
          waterAdministrationUnitId,
          waterAdministrationUnitReviewerId,
          problem,
          serviceTime,
          serviceStartTime,
          serviceEndTime,
          contractName,
          acceptanceFile,
          contractFile,
        };
        if (this.ruleForm.acceptanceFile) {
          this.fileListOne = data.acceptanceFiles.map((item) => {
            return {
              name: item.fileName,
              url: item.fileUrl,
            };
          });
        }
        if (this.ruleForm.contractFile) {
          this.fileList = data.contractFiles.map((item) => {
            return {
              name: item.fileName,
              url: item.fileUrl,
            };
          });
        }
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
      this.ruleForm.contractFile = "";
      this.fileList = [];
    },
    handleSuccessOne(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.ruleForm.acceptanceFile = files.id;
        this.fileListOne = [
          {
            name: files.originalName,
            url: files.filePath,
          },
        ];
      } else {
        this.$message.error(res.msg);
        this.fileListOne = [];
      }
    },
    handleRemoveOne() {
      this.ruleForm.acceptanceFile = "";
      this.fileListOne = [];
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.submitFlag) {
            this.submitFlag = false;
            let ruleForm = cloneObj(this.ruleForm);
            let propertyUnitName = this.propertyUnitList.find(
              (item) => item.id === this.ruleForm.propertyUnitId
            );
            ruleForm.propertyUnitName = propertyUnitName.companyName;
            if (ruleForm.serviceTime && ruleForm.serviceTime.length > 0) {
              ruleForm.serviceStartTime = ruleForm.serviceTime[0];
              ruleForm.serviceEndTime = ruleForm.serviceTime[1];
            }
            delete ruleForm.serviceTime;
            contractApi
              .save(ruleForm)
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