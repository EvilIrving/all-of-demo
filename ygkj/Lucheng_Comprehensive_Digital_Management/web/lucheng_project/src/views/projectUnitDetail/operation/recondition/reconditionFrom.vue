<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-20 16:42:35
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\recondition\reconditionFrom.vue
-->
<template>
  <div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="100px"
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
        <el-form-item label="操作时间：" prop="beginTime">
          <el-date-picker
            class="form-item-value"
            v-model="ruleForm.beginTime"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="选择日期时间"
          >
          </el-date-picker>
        </el-form-item>
      </div>
      <div class="form-item">
        <el-form-item label="结束时间：" prop="endTime">
          <el-date-picker
            value-format="yyyy-MM-dd HH:mm:ss"
            class="form-item-value"
            v-model="ruleForm.endTime"
            type="datetime"
            placeholder="选择日期时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="操作人员：" prop="operatorId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.operatorId"
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
        <el-form-item label="监护人员：" prop="guardianId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.guardianId"
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
        <el-form-item label="值班领导：" prop="leaderId">
          <el-select
            class="form-item-value"
            filterable
            v-model="ruleForm.leaderId"
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
        beginTime: "",
        endTime: "",
        fileIds: "",
        guardianId: "",
        guardian: "",
        operatorId: "",
        operator: "",
        leaderId: "",
        leader: "",
        type: 1,
      },
      rules: {
        guardianId: [
          { required: true, message: "请选择监护人", trigger: "change" },
        ],
        operatorId: [
          { required: true, message: "请选择操作人", trigger: "change" },
        ],
        leaderId: [
          { required: true, message: "请选择值班领导", trigger: "change" },
        ],
        beginTime: [
          {
            required: true,
            message: "请选择操作时间",
            trigger: "change",
          },
        ],
        endTime: [
          {
            required: true,
            message: "请选择结束时间",
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
  mounted() {
    if (this.id) {
      this.loadPageOverhaul();
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
        type: 1,
        id: this.id,
      });
      if (res.code == 0) {
        let data = res.data.list[0];
        let {
          pid,
          beginTime,
          endTime,
          fileIds,
          guardianId,
          guardian,
          operatorId,
          operator,
          leaderId,
          leader,
          id,
        } = data;
        this.ruleForm = {
          pid,
          beginTime,
          endTime,
          fileIds,
          guardianId,
          guardian,
          operatorId,
          operator,
          leaderId,
          leader,
          id,
          type: 1,
        };
        this.fileList = data.files.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
          };
        });
        console.log(this.fileList, "this.fileListthis.fileLists");
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
            let guardian = this.options.find(
              (item) => item.value === this.ruleForm.guardianId
            );
            let operator = this.options.find(
              (item) => item.value === this.ruleForm.operatorId
            );
            let leader = this.options.find(
              (item) => item.value === this.ruleForm.leaderId
            );
            this.ruleForm.guardian = guardian.label;
            this.ruleForm.operator = operator.label;
            this.ruleForm.leader = leader.label;
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
    width: 320px;
  }
}
.btn-box {
  text-align: right;
}
</style>