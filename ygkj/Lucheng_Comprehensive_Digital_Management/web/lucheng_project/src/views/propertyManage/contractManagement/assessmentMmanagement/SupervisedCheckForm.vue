<!--
 * @Date: 2022-04-24 13:55:24
 * @Author: 
 * @Description: 合同管理-合同验收-年度验收-表单
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-06-27 15:48:56
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\SupervisedCheckForm.vue
-->
<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      label-position="right"
      :label-width="labelWidth"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工程名称：" prop="supervisionProject">
            <el-select
              v-model="formData.supervisionProject"
              style="width: 100%"
              clearable
              placeholder="请选择工程"
            >
              <el-option
                v-for="item in projectList"
                :key="item.project_code"
                :value="item.project_code"
                :label="item.project_name"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查时间：" prop="inspectionTime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.inspectionTime"
              value-format="yyyy-MM-dd HH:mm:00"
              format="yyyy-MM-dd HH:mm:00"
              type="datetime"
              placeholder="请选择检查时间"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="责任单位：" prop="responsibleUnit">
            <el-select
              v-model="formData.responsibleUnit"
              style="width: 100%"
              clearable
              placeholder="请选择责任单位"
              @change="propertyUnitPeople"
            >
              <el-option
                v-for="item in unitOptions"
                :key="item.id"
                :value="item.id"
                :label="item.companyName"
              ></el-option>
              <!-- <el-option
                v-for="item in projectList"
                :key="item.project_code"
                :value="item.project_code"
                :label="item.project_name"
              ></el-option> -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任人：" prop="responsiblePeople">
            <el-select
              v-model="formData.responsiblePeople"
              style="width: 100%"
              clearable
              placeholder="请选择责任人"
            >
              <el-option
                v-for="item in operationPeopleList"
                :key="item.id"
                :value="item.id"
                :label="item.username"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="督办状态" prop="supervisionStatus">
            <el-select
              style="width: 100%"
              v-model="formData.supervisionStatus"
              clearable
              placeholder="请选择督办状态"
            >
              <el-option value="2" label="已督促"></el-option>
              <el-option value="1" label="未督促"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="具体问题：" prop="problems">
            <el-input
              v-model="formData.problems"
              type="textarea"
              placeholder="请输入具体问题："
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="隐患图片：">
            <uploadFile
              accept=".pdf,.jpg,.jpeg,.png"
              @success="handleSuccess"
              @remove="handleRemove"
              @review="handlePreview"
              :fileList="fileList"
              :size="15"
              :limit="999"
            />
          </el-form-item>
        </el-col>
      </el-row>

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
import {
  supervisedCheck,
  propertyUnitApi,
  propertyPeopleApi,
  getAllProjectApi,
} from "@/api/propertyManage";
import uploadFile from "@/components/uploadFile.vue";

export default {
  name: "SupervisedCheckForm",
  props: {
    id: [String, Number],
  },
  components: { uploadFile },
  data() {
    return {
      projectList: [],
      unitOptions: [],
      operationPeopleList: [],
      labelWidth: "150px",
      formData: {
        inspectionTime: "",
        problems: "",
        problemsPicture: "",
        supervisionStatus: "",
        responsibleUnit: "",
        responsiblePeople: "",
        supervisionProject: "",
      },
      rules: {
        name: [{ required: true, message: "必填项", trigger: "blur" }],
      },
      showMapDialog: false,
      showMapSelect: true,
      fileList: [],
      submitFormType: false,
    };
  },
  async mounted() {
    await this.getAllProject();
    await this.deptList();
    if (this.id) {
      this.getDetail();
    }
    this.getResponseList();
  },
  methods: {
    async getAllProject() {
      let res = await getAllProjectApi();
      if (res.code == 0) {
        this.projectList = res.data;
      }
    },
    async deptList() {
      let res = await propertyUnitApi.list({
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        this.unitOptions = res.data.list;
        console.log(this.unitOptions);
      }
    },
    async propertyUnitPeople() {
      this.formData.responsiblePeople = "";
      let res = await propertyPeopleApi.list({
        deptId: this.formData.responsibleUnit,
        pageNum: 1,
        pageSize: 99999,
      });
      if (res.code == 0) {
        this.operationPeopleList = res.data.list;
      }
    },
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
      // this.fileList = [];
    },
    handlePreview(data) {
      console.log(data);
    },
    async getDetail() {
      let res = await supervisedCheck.list({
        id: this.id,
      });
      if (res.code == 0) {
        let {
          inspectionTime,
          problems,
          problemsPicture,
          supervisionStatus,
          responsibleUnit,
          responsiblePeople,
          supervisionProject,
        } = res.data.list[0];
        this.formData = {
          inspectionTime,
          problems,
          problemsPicture,
          supervisionStatus,
          responsibleUnit,
          responsiblePeople,
          supervisionProject,
        };
        if (this.formData.problemsPicture) {
          res.data.list[0].problemsPictureList.forEach((item) => {
            this.fileList.push({
              name: item.fileName,
              url: item.fileUrl,
              ...item,
            });
          });
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
        this.formData.problemsPicture = arr.join(",");
      } else {
        this.formData.problemsPicture = "";
      }
      let res = await supervisedCheck.save(this.formData);
      if (res.code == 0) {
        this.$message({
          message: "保存成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.submitFormType = false;
            this.closeEditDialog();
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

    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  .user-form {
    width: 100%;
    .el-form {
      width: 100%;
      background: #fff;
      padding: 8px 0;
      .form_item {
        margin-bottom: 8px;
        .el-form-item {
          margin-bottom: 8px;
        }
      }
    }
  }
}
.el-dialog__footer {
  border-top: 2px solid #dcdfe6;
  padding: 0;
  .el-form-item {
    margin-bottom: 0;
  }
}

.is-plain {
  background: #fff;
}
.item_box_name {
  // @includes flexbox();
  // @includes flexAI(center);
  // @includes flexJC(space-between);
  // border-bottom: 1px solid #eee;
  height: 25px;
  padding-right: 16px;
  margin-bottom: 4px;
  p {
    font-size: 16px;
    font-weight: 600;
    color: #333333;
    height: 16px;
    line-height: 16px;
    border-left: 4px solid #1492ff;
    border-radius: 2px;
    padding-left: 10px;
  }
}
.el-dialog__body {
  padding: 10px 20px 30px;
}
.lines {
  width: 100%;
  height: 1px;
  background: #f5f5f5;
  margin: 5px 0 10px;
}
.el-input.is-disabled .el-input__inner {
  background-color: #ffffff;
  color: #4e5158;
  cursor: not-allowed;
}
.upload-demo {
  .upload-btn {
    display: flex;
    align-items: center;
    img {
      width: 14px;
      height: 13px;
      margin-right: 8px;
    }
  }
  .el-upload__tip {
    margin-top: 0;
    color: #969799;
    line-height: 18px;
  }
}
</style>