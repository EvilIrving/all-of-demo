<!--
 * @Date: 2022-04-24 13:55:24
 * @Author: 
 * @Description: 合同管理-合同验收-年度验收-表单
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-26 15:53:44
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\contractAcceptance\AnnualAcceptanceForm.vue
-->
<template>
  <div class="form">
    <el-form :model="formData" status-icon :rules="rules" ref="ruleForm" label-position="right" :label-width="labelWidth">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="物业管理单位：" prop="propertyUnitName">
            <el-select v-model="formData.propertyUnitName" clearable placeholder="请选择物业管理单位：" @change="propertyUnitChange">
              <el-option v-for="item in propertyUnitOptions" :key="item.id" :value="item.companyName" :label="item.companyName"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="运行管理单位：" prop="runUnitName">
            <el-select v-model="formData.runUnitName" clearable placeholder="请选择运行管理单位：" @change="runUnitChange">
              <el-option v-for="item in runUnitOptions" :key="item.name" :value="item.name" :label="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="验收时间：" prop="runAcceptanceTime">
            <el-date-picker v-model="formData.runAcceptanceTime" value-format="yyyy-MM-dd HH:00:00" format="yyyy-MM-dd HH:00:00" type="datetime" placeholder="请选择验收时间：">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="验收意见：" prop="runAcceptanceComments">
            <el-input v-model="formData.runAcceptanceComments" type="textarea" placeholder="请输入验收意见：" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 水行政主管部门： -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="水行政主管部门：" prop="type">
            <el-select v-model="formData.waterAdministrationUnitName" clearable placeholder="请选择水行政主管部门：" @change="waterUnitChange">
              <el-option v-for="item in waterUnitOptions" :key="item.value" :value="item.value" :label="item.label"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="验收时间：" prop="waterAcceptanceTime">
            <el-date-picker v-model="formData.waterAcceptanceTime" value-format="yyyy-MM-dd HH:00:00" format="yyyy-MM-dd HH:00:00" type="datetime" placeholder="请选择验收时间：">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="验收意见：" prop="waterAcceptanceComments">
            <el-input v-model="formData.waterAcceptanceComments" type="textarea" placeholder="请输入验收意见：" clearable></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="验收附件：" prop="appendix">
            <unpload-file @review='handlePreview' @success="handleSuccess" @remove="handleRemove" accept=".jpg,.jpeg,.png,.pdf,.JPG,.JPEG,.PNG,.PDF" :size="15" :fileList="fileList"></unpload-file>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="footer_btn">
        <el-button size="mini" type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button size="mini" @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { contractOperate, propertyUnitApi,supervisedCheck } from '@/api/propertyManage'
import unploadFile from '@/components/uploadFile.vue'
import LookFile from "@/components/LookFile.vue";
export default {
  name: 'AnnualAcceptanceForm',
  props: {
    id: [String, Number],
  },
  components: {
    unploadFile, LookFile
  },
  data() {
    return {
      fileDialog: false,
      url: "",
      propertyUnitOptions: [],
      runUnitOptions: [],
      waterUnitOptions: [
        { label: '鹿城农业农村局', value: '鹿城农业农村局' },
      ],
      labelWidth: '150px',
      formData: {},
      rules: {
        name: [{ required: true, message: '必填项', trigger: 'blur' }],
      },
      showMapDialog: false,
      showMapSelect: true,
      fileList: [],
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
    this.getUnitList()
    this.getRunList()
  },
  methods: {
    propertyUnitChange(val) {
      // this.formData.propertyUnitName = this.propertyUnitOptions.find((item) => item.id == val).companyName
    },
    runUnitChange(val) {
      // this.formData.runUnitName = this.runUnitOptions.find((item) => item.value == val).label
    },
    waterUnitChange(val) {
      // this.formData.waterAdministrationUnitName = this.waterUnitOptions.find((item) => item.value == val).label
    },
    async getDetail() {
      let res = await contractOperate.AAlist({
        id: this.id,
      })
      if (res.code == 0) {
        this.formData = res.data.list[0]
        if (this.formData.files && this.formData.files.length !== 0) {
          this.formData.files.forEach(element => {
            this.fileList.push(
              {
                url: element.fileUrl,
                name: element.fileName,
              },
            )
          });
        }
      }
    },

    submitForm(formName) {
      let that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          that.formData.id = that.id
          that.save(that.formData)
        } else {
          return false
        }
      })
    },

    async save(data) {
      const {
        waterAcceptanceTime,
        waterAcceptanceComments,
        runUnitName,
        runAcceptanceTime,
        runAcceptanceComments,
        propertyUnitName,
        waterAdministrationUnitName,
        runUnitId,
        waterAdministrationUnitId,
        appendix,
        id
      } = data;
      let temp = {
        waterAcceptanceTime,
        waterAcceptanceComments,
        runUnitName,
        runAcceptanceTime,
        runAcceptanceComments,
        propertyUnitName,
        waterAdministrationUnitName,
        runUnitId,
        waterAdministrationUnitId,
        appendix,
        id
      };
      let res = {}
      res = await contractOperate.AAsaveOrUpdate(temp)
      if (res.code == 0) {
        this.$message({
          message: '保存成功',
          type: 'success',
          duration: '1000',
          onClose: () => {
            this.closeEditDialog()
          },
        })
      } else {
        this.$message.error('保存失败！')
      }
    },

    closeEditDialog() {
      this.$emit('closeEditDialog')
    },
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.formData.appendix = files.id;
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
      this.formData.appendix = "";
      this.fileList = [];
    },
    handlePreview(data) {
      this.url = data.url;
      this.fileDialog = true;
    },
    async getUnitList() {
      let res = await propertyUnitApi.list({ fetchAll: true })
      if (res.code == 0) {
        this.propertyUnitOptions = res.data.list
      }
    },
    async getRunList() {
      let res = await supervisedCheck.deptList({ deptIds: 67011533 })
      if (res.code == 0) {
        this.runUnitOptions = res.data
      }
    },
  },
}
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