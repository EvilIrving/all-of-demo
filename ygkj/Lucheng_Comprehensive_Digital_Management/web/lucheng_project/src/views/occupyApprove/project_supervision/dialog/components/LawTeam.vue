<template>
  <!-- 移交执法大队 -->
  <div class="law-team">
    <el-row class="law-team-content" :gutter="10">
      <el-col :span="10">
        <div class="grid-content">
          <el-form ref="form">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-form-item label="案件持续时间(天)">
                  <el-input v-model="pendingTime" placeholder="-" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item required label="案件结办时间">
                  <el-date-picker
                    v-model="coordinatorDate"
                    :disabled="JieBanShow"
                    value-format="yyyy-MM-dd"
                    type="date"
                    placeholder="选择日期"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="结办说明">
              <el-input
                v-model="coordinatorProduce"
                type="textarea"
                :rows="4"
                :disabled="JieBanShow"
                placeholder="文本输入"
              />
            </el-form-item>
            <el-form-item v-if="!JieBanShow" style="margin-top: 16px">
              <el-button type="primary" @click="caseClosed">
                案件结办
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="14" class="rightbox">
        <div class="imgbox" v-if="JieBanShow && noProblemFlag">
          <!-- <img src="../../../../../assets/images/no-problem.png" /> -->
        </div>
        <template v-else>
          <el-col
            :span="24"
            class="grid-content"
            style="height: 40px; margin-top: 0"
          >
            <div>
              <span>上传结办文件</span>
            </div>
          </el-col>
          <el-col :span="24" class="grid-content">
            <el-upload
              :disabled="JieBanShow"
              class="upload-demo"
              :action="$config.fileUploadUrl"
              name="uploadFile"
              ref="upload"
              multiple
              drag
              :file-list="fileList"
              :on-change="fileChange"
              :headers="headers"
              :on-success="handleAvatarSuccess"
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
            >
              <i class="el-icon-upload" style="color: #008a83"></i>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <div class="el-upload__text" style="margin-top: 15px">
                支持文件pdf.jpg.png等格式，且不超过5MB
              </div>
            </el-upload>
          </el-col>
        </template>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { projectSupervisionApi } from "@/api/projectSupervision";
import { getFileList } from "@/api/login";
export default {
  name: "law-team",
  data() {
    return {
      pendingTime: null,
      coordinatorFile: null,
      JieBanShow: false,
      coordinatorProduce: null,
      selectPersonList: [],
      fileList: [],
      coordinatorDate: "",
      coordinatorFileIds: [],
      noProblemFlag: true,
    };
  },
  computed: {
    /** @returns {any} */
    headers() {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
  },
  props: {
    commonalityId: Object,
    selectTab: [String, Boolean],
  },
  mounted() {
    this.$nextTick(() => {
      this.getList();
      this.judgeFinish();
    });
  },
  watch: {
    selectTab() {
      this.init();
      this.judgeFinish();
    },
  },
  methods: {
    init() {
      this.pendingTime = "";
      this.coordinatorDate = "";
      this.coordinatorProduce = "";
      this.JieBanShow = false;
      this.fileList = [];
    },
    judgeFinish() {
      if (this.commonalityId.resultStatus == 1) {
        this.JieBanShow = true;
        this.pendingTime = this.commonalityId.pendingTime;
        this.coordinatorDate = this.commonalityId.coordinatorTime;
        this.coordinatorProduce = this.commonalityId.coordinatorProduce;
        let fileArr = this.commonalityId.coordinatorFile
          ? this.commonalityId.coordinatorFile.split(",")
          : [];
        this.fileList = fileArr.map((item) => {
          return {
            name: item,
          };
        });
      } else {
        this.JieBanShow = false;
      }
    },
    // 案件结办附件上传 开始
    uploadFile(file) {
      console.log(101, file);
    },
    fileChange(file, fileList) {
      let arr = [];
      fileList.forEach((val) => {
        arr.push(val.name);
      });
      this.coordinatorFile = arr.join(",");
      this.coordinatorFileIds = fileList.map((item) => item?.response?.data.id);
    },
    handleAvatarSuccess(res) {
      if (res.data.success) {
        this.$message({ message: "上传成功!", type: "success" });
      } else {
        this.$message({ message: "上传失败!", type: "error" });
      }
    },
    handleRemove(file, fileList) {
      let arr = [];
      fileList.forEach((val) => {
        arr.push(val.name);
      });
      this.coordinatorFile = arr.join(",");
      this.coordinatorFileIds =
        fileList.length > 0
          ? fileList.map((item) => item.response.data.id)
          : [];
    },
    async handlePictureCardPreview(file) {
      console.log(file, 222);
      if (file.response) {
        window.open(file.response.data.filePath);
      } else {
        const fileNames = this.commonalityId.coordinatorFile.split(",");
        const idx = fileNames.indexOf(file.name);
        const fileIds = this.commonalityId.coordinatorFileIds.split(",");
        const fileId = fileIds[idx];
        await getFileList({ ids: fileId }).then((res) => {
          if (res.data) {
            window.open(res.data.data[0].fileUrl);
          }
        });
      }
    },
    // 案件结办附件上传 结束
    // 案件结办
    async caseClosed() {
      let opt = {
        inspectionId: this.commonalityId.id,
        coordinatorFile: this.coordinatorFile,
        pendingTime: this.pendingTime,
        coordinatorDate: this.coordinatorDate,
        coordinatorProduce: this.coordinatorProduce,
        coordinatorFileIds:
          this.coordinatorFileIds.length > 0
            ? this.coordinatorFileIds.join(",")
            : "",
      };
      let res = await projectSupervisionApi.caseClosed(opt);
      if (res.code == 0) {
        this.$emit("detail");
      }
    },
    // 人员选择
    saveEmit(val) {
      this.selectPersonList = val.map((item) => item.id);
    },
    // 获取问题列表根据巡查id
    async getList() {
      let res = await projectSupervisionApi.getList({
        inspectionId: this.commonalityId.id,
        rectify: 4,
      });
      if (res.code == 0) {
        if (res.data.list.length > 0) {
          this.noProblemFlag = false;
        } else {
          this.noProblemFlag = true;
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.law-team {
  height: 100%;
  padding-left: 16px;
  .law-team-content {
    width: 100%;
    height: 100%;
    > .el-col {
      height: 100%;
    }
    .rightbox {
      display: flex;
      flex-flow: column nowrap;
      .imgbox {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        > img {
          width: 50%;
        }
      }
    }
  }
  .grid-content {
    // height: 100%;
    padding: 10px;
    background: #fff;
    border: 1px solid rgba(94, 91, 91, 0.2);
    margin-top: 0;
    > div {
      > p:nth-child(1) {
        font-size: 20px;
        color: rgba(0, 0, 0, 0.88);
        line-height: 24px;
        font-weight: 600;
      }
      > div {
        padding: 10px;
        ::v-deep.el-radio__label {
          color: #c2c2c2;
        }
        ::v-deep.el-radio__input.is-checked + .el-radio__label {
          color: #1f1f1f;
        }
        ::v-deep.el-radio__input.is-checked .el-radio__inner {
          border-color: #1492ff;
          background: #1492ff;
        }
        ::v-deep.el-checkbox__label {
          color: #1f1f1f;
        }
      }
    }
  }
  .upload-demo {
    height: 100%;
    ::v-deep.el-upload-dragger {
      border: 0;
      width: 100%;
    }
  }
  .inspectors-sty {
    background: #1492ff;
    padding: 5px 12px;
    color: #fff;
    margin-right: 10px;
  }
}
</style>