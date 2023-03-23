<template>
  <div class="form">
    <el-form :model="tableForm" status-icon label-width="100px">
      <el-row type="flex" justify="left">
        <el-col :span="12">
          <el-form-item label="工程名称：" prop="projectName">
            <span>{{
              tableForm.projectName ? tableForm.projectName : "无"
            }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="督办人：" prop="createId">
            <span>{{ tableForm.createName ? tableForm.createName : "无" }}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="12">
          <el-form-item label="督办时间：" prop="createTime">
            <span>{{
              tableForm.createTime ? tableForm.createTime : "无"
            }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止时间：" prop="deadline">
            <span>{{ tableForm.deadline ? tableForm.deadline : "无" }}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="12">
          <el-form-item label="督查状态：">
            <span>{{
              tableForm.statusName ? tableForm.statusName : "无"
            }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题描述：" prop="problemDescribe">
            <span>{{
              tableForm.problemDescribe ? tableForm.problemDescribe : "无"
            }}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <!-- problemFile -->
          <el-form-item label="问题附件：">
            <div v-if="tableForm.problemFileList">
              <div
                class="look_file"
                v-for="(item, index) in tableForm.problemFileList"
                :key="index"
                @click="preview(item.fileUrl)"
              >
                <i
                  class="el-icon-link"
                  style="color:#409eff;cursor: pointer;"
                ></i>
                <span>点击查看</span>
              </div>
            </div>
            <span v-if="!tableForm.problemFile">无附件</span>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="title-flex"></div>
      <el-row type="flex" justify="left">
        <el-col :span="12">
          <el-form-item label="整改人：" prop="workerName">
            <span>{{
              tableForm.workerName ? tableForm.workerName : "无"
            }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改时间：" prop="workTime">
            <span>{{
              tableForm.workTime ? tableForm.workTime : "无"
            }}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <el-form-item label="整改说明：" prop="workDescribe">
            <span>{{
              tableForm.workDescribe ? tableForm.workDescribe : "无"
            }}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row type="flex" justify="left">
        <el-col :span="24">
          <el-form-item label="整改附件：">
            <!-- workFile -->
            <div v-if="tableForm.workFileList">
              <div
                class="look_file"
                v-for="(item, index) in tableForm.workFileList"
                :key="index"
                @click="preview(item.fileUrl)"
              >
                <i
                  class="el-icon-link"
                  style="color:#409eff;cursor: pointer;"
                ></i>
                 <span>点击查看</span>
              </div>
            </div>
            <span v-if="!tableForm.workFile">无附件</span>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="footer_btn">
        <el-button type="primary" @click="closeEditDialog()">关闭</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import unploadFile from "@/components/uploadFile.vue";
import { supervisionCenter } from "@/api/seawallPrevent";
export default {
  name: "SupervisionForm",
  props: {
    id: [String, Number],
  },
  components: {
    unploadFile,
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectDetail;
    },
    isDisabled() {
      return this.isLook ? true : false;
    },
  },
  data() {
    return {
      tableForm: {
        projectName: "",
        createId: "",
        createTime: "",
        deadline: "",
        problemDescribe: "",
        fileList: "",
        workerName: "",
        workTime: "",
        workDescribe: "",
        workFile: "",
      },
      personList: "",
      expandedKeys: [],
      loading: false,
      showPersonTree: false,
    };
  },
  async mounted() {
    this.getDetail();
  },
  methods: {
    async getDetail() {
      let res = await supervisionCenter.list({
        id: this.id,
      });
      if (res.code == 0) {
        res.data.list[0].status == 1
          ? (res.data.list[0].statusName = "已处置")
          : res.data.list[0].status == -1
          ? (res.data.list[0].statusName = "超期未处置")
          : res.data.list[0].status == 0
          ? (res.data.list[0].statusName = "未处置")
          : "未设置状态";
        if (res.data.list[0].insideList.length !== 0) {
          res.data.list[0].insideList.map((j) => {
            if (j.workerPhone == res.data.list[0].reportPhone) {
              res.data.list[0].workerName = j.workerName;
            } else {
              res.data.list[0].workerName = "";
            }
          });
        }
        if (res.data.list[0].outsideList.length !== 0) {
          res.data.list[0].outsideList.map((j) => {
            if (j.workerPhone == res.data.list[0].reportPhone) {
              res.data.list[0].workerName = j.workerName;
            } else {
              res.data.list[0].workerName = "";
            }
          });
        }
        this.tableForm = res.data.list[0];
      }
    },
    async getPersonList() {
      await customPeople.list().then((res) => {
        if (res.code == 0) {
          this.loading = false;
          this.personList = res.data;
        }
      });
    },
    handleNodeClick(data) {
      this.selectFile = data;
      this.selectFileId = data.id;
      this.$emit("nodeClick", data.id, data);
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.save(this.tableForm);
        } else {
          return false;
        }
      });
    },
    async save(data) {
      let res = await supervisionCenter.update(data);
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
    closeEditDialog() {
      this.$emit("closeEditDialog");
    },
    preview(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$message.warning("暂无附件");
      }
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
.footer_btn {
  float: right;
}
.look_file {
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
