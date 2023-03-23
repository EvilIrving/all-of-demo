<template>
  <div class="form">
    <el-form
      :model="tableForm"
      status-icon
      ref="ruleForm"
      :rules="rules"
      label-width="100px"
    >
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <el-form-item label="问题描述" prop="problemDescribe">
            <el-input
              type="textarea"
              v-model="tableForm.problemDescribe"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <!-- problemFile -->
          <el-form-item label="问题附件：">
            <upload-file
              @success="workfileListSuccess"
              @remove="handleRemove"
              :accept="accept"
              :size="15"
              :multiple="true"
              :fileList="tableForm.problemfileList"
            ></upload-file>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="title-flex"></div>
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <el-form-item label="整改说明" prop="workDescribe">
            <el-input
              type="textarea"
              v-model="tableForm.workDescribe"
              placeholder="请输入"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <el-form-item label="整改附件：">
            <!-- workFile -->
            <upload-file
              @success="problemfileListSuccess"
              @remove="handleRemove"
              :multiple="true"
              :accept="accept"
              :size="15"
              :fileList="tableForm.workFileList"
            ></upload-file>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="footer_btn">
        <el-button @click="closeEditDialog()">取消</el-button>
        <el-button type="primary" @click="save">处置</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import uploadFile from "@/components/uploadFile.vue";
import { supervisionCenter, userController } from "@/api/seawallPrevent";
export default {
  name: "SupervisionForm",
  props: {
    id: [String, Number],
  },
  components: {
    uploadFile,
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectDetail;
    },
  },
  data() {
    return {
      accept: ".jpg,.jpeg,.png,.gif,.bmp,.JPG,.JPEG,.PBG,.GIF,.BMP",
      tableForm: {
        problemDescribe: "",
        problemFile: "",
        workDescribe: "",
        workFile: "",
      },
      rules: {
        problemDescribe: [
          {
            required: true,
            message: "请填写说明",
          },
        ],
        workDescribe: [
          {
            required: true,
            message: "请填写说明",
          },
        ],
      },
      deadline: "",
      problemDescribe: "",
      problemFile: [],
      workDescribe: "",
      workFile: [],
      personList: "",
      radio: 3,
      expandedKeys: [],
      loading: false,
      showPersonTree: false,
      outworkPersonList: [],
      workerNameOptions: [],
    };
  },
  async mounted() {
    // this.getDetail();
    // this.getPersonList();
  },
  methods: {
    chagneRadio(val) {
      this.radio = val;
    },
    // async getDetail() {
    //   let res = await supervisionCenter.list({
    //     id: this.id,
    //   });
    //   if (res.code == 0) {
    //     this.tableForm = res.data.list[0];
    //   }
    // },
    async getPersonList() {
      await userController.userslist().then((res) => {
        if (res.code == 0) {
          this.loading = false;
          res.data.map((i) => {
            this.workerNameOptions.push({ label: i.realName, value: i.id });
          });
        }
      });
    },
    submitForm(formName) {
      // this.$refs[formName].validate((valid) => {
      //   if (valid) {
      //     this.save();
      //   } else {
      //     return false;
      //   }
      // });
      this.save();
    },
    async save() {
      this.tableForm.id = this.id;
      const {
        id,
        problemDescribe,
        workDescribe,
        workFile,
        problemFile,
      } = this.tableForm;
      let formData = {
        id,
        problemDescribe,
        workDescribe,
        workFile,
        problemFile,
      };
      let res = await supervisionCenter.update(formData);
      if (res.code == 0) {
        this.$message({
          message: "处置成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.closeEditDialog();
          },
        });
      } else {
        this.$message.error("处置失败！");
      }
    },
    //上传成功
    workfileListSuccess(res) {
      console.log(res);
      this.$message.success("上传成功");
      if (this.tableForm.problemFile !== "") {
        this.tableForm.problemFile += `,${res.file.data.id}`;
      } else {
        this.tableForm.problemFile = res.file.data.id;
      }
    },
    problemfileListSuccess(res) {
      this.$message.success("上传成功");
      if (this.tableForm.workFile !== "") {
        this.tableForm.workFile += `,${res.file.data.id}`;
      } else {
        this.tableForm.workFile = res.file.data.id;
      }
    },
    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
    // 重置
    resetForm() {
      this.$refs.tableForm.resetFields();
    },
    handleRemove() {
      this.form.fileurl = "";
      this.form.filename = "";
    },
  },
};
</script>

<style lang="scss" scoped>
.title-flex {
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  font-size: 0.4rem;
  color: #eee;
}
.title-flex::before,
.title-flex::after {
  content: "";
  flex: 1;
  height: 1px;
  background: #eee;
}
.work_person_out {
  cursor: pointer;
  user-select: none;
  color: #1492ff;
  & > i,
  & > span {
    color: #1492ff;
    font-size: 14px;
  }
  & > i {
    margin-right: 10px;
  }
}
</style>
