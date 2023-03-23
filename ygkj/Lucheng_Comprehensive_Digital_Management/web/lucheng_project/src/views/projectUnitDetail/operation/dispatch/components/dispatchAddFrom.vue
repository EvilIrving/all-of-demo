<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-06-13 10:09:13
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\dispatch\components\dispatchAddFrom.vue
-->
<template>
  <div>
    <div class="header-title">
      <span></span>
      基本信息
    </div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
    >
      <div class="form-item">
        <el-form-item label="年份：">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.dYear"
            type="year"
            placeholder="请选择"
            format="yyyy"
            value-format="yyyy"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="调度类型：" prop="principalId">
          <el-select
            class="form-item-value"
            v-model="ruleForm.domType"
            placeholder="请选择"
          >
            <el-option label="上级调度" value="1"> </el-option>
            <el-option label="计划内调度" value="2"> </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="调令下达时间：" prop="issueDate">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.tm"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="请选择"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="调令编号：" prop="issueDate">
          <el-input
            class="form-item-value"
            v-model="ruleForm.sOrderCode"
          ></el-input>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="调令闸门开启时间：" prop="issueDate">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.gateOpenTm"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="请选择"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="调令闸门关闭时间：" prop="issueDate">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.gateCloseTm"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="请选择"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <el-form-item v-if="ruleForm.domType != '2'" label="调令文件：">
        <uploadFile
          accept=".pdf"
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
// import { operation } from "@/api/projectUnitDetail.js";
import uploadFile from "@/components/uploadFile.vue";
import { matterApi } from '@/api/propertyManage.js'
export default {
  props: {
    id: String,
    options: Array,
  },
  components: { uploadFile },
  data() {
    return {
      ruleForm: {
        id: "",
        pid: "",
        dYear: "",
        tm: "",
        sOrderCode: "",
        gateOpenTm: "",
        gateCloseTm: "",
        domType: "",
        uploader: this.$sessionData("get", "userInfo").id,
        uploaderName: this.$sessionData("get", "userInfo").username,
        uploadfile: ""
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
      let res = await matterApi.queryByPage({
        pid: this.projectDetail.prcd,
        // type: 2,
        id: this.id,
      });
      if (res.code == 0) {
        let data = res.data.list[0];
        // console.log(data);
        this.ruleForm = {
          id: data.id,
          pid: data.pid,
          dYear: data.dYear,
          tm: data.tm,
          sOrderCode: data.sOrderCode,
          gateOpenTm: data.gateOpenTm,
          gateCloseTm: data.gateCloseTm,
          domType: data.domType,
          uploader: data.uploader,
          uploaderName: data.uploaderName,
          uploadfile: data.uploadfile,
        };
        this.fileList = data.uploadFiles.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
          };
        });
        // console.log(this.fileList, "this.fileListthis.fileLists");
      }
    },
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        this.ruleForm.uploadfile = files.id;
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
      this.ruleForm.uploadfile = "";
      this.fileList = [];
    },
    handlePreview(data) {
      console.log(data);
    },
    submitForm(formName) {
      if (this.ruleForm.domType == '2') {
        delete this.ruleForm[uploader]
        delete this.ruleForm[uploaderName]
        delete this.ruleForm[uploadfile]
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.submitFlag) {
            this.submitFlag = false;
            // this.ruleForm.issueMan = issueMan.label;
            matterApi
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
.header-title {
  display: flex;
  align-items: center;
  color: #666666;
  line-height: 26px;
  font-size: 16px;
  margin-bottom: 15px;
  span {
    width: 4px;
    height: 20px;
    background: #1492ff;
    border-radius: 2px;
    margin-right: 14px;
  }
}
</style>