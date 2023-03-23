<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-22 16:59:51
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\repair\defectTreatment\components\disposeInfo.vue
-->
<template>
  <div class="dis-box">
    <div class="wrap">
      <div class="wrap_box_title">
        <p>处理信息</p>
      </div>
      <div>
        <div class="item-upload">
          <div class="label-item-upload">处理前图片：</div>
          <div class="value-item-upload">
            <div
              class="preview-item"
              v-for="(item, index) in fileListOne"
              :key="index"
            >
              <span
                class="el-icon-error icon-btn"
                @click.stop="clearImage(index, 1)"
              ></span>
              <div class="preview-list" v-if="item.filePath">
                <el-image
                  style="width: 100%; height: 100%"
                  :src="item.filePath"
                  :preview-src-list="[item.filePath]"
                >
                </el-image>
              </div>
              <div class="progress-box" v-else>
                <div :style="`width:${item.num || 0}%`"></div>
              </div>
            </div>
            <el-upload
              v-if="formData.status == 1"
              class="el-upload-box"
              :headers="headers"
              :action="$config.uploadUrl"
              name="uploadFile"
              list-type="picture-card"
              accept=".png,.jpg,.jpeg"
              :show-file-list="false"
              :on-success="handleSuccessOne"
              :on-progress="handleProgressOne"
              :before-upload="handleBeforeUploadOne"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
        </div>
        <div class="item-upload">
          <div class="label-item-upload">处理后图片：</div>
          <div class="value-item-upload">
            <div
              class="preview-item"
              v-for="(item, index) in fileListTwo"
              :key="index"
            >
              <span
                class="el-icon-error icon-btn"
                @click.stop="clearImage(index, 1)"
              ></span>
              <div class="preview-list" v-if="item.filePath">
                <el-image
                  style="width: 100%; height: 100%"
                  :src="item.filePath"
                  :preview-src-list="[item.filePath]"
                >
                </el-image>
              </div>
              <div class="progress-box" v-else>
                <div :style="`width:${item.num || 0}%`"></div>
              </div>
            </div>
            <el-upload
              v-if="formData.status == 1"
              class="el-upload-box"
              :headers="headers"
              :action="$config.uploadUrl"
              name="uploadFile"
              accept=".png,.jpg,.jpeg"
              list-type="picture-card"
              :show-file-list="false"
              :on-success="handleSuccessTwo"
              :on-progress="handleProgressTwo"
              :before-upload="handleBeforeUploadTwo"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
        </div>
        <div class="item-upload">
          <div class="label-item-upload">处理后文件：</div>
          <div class="value-item-upload uploadFile">
            <uploadFile
              accept=".pdf"
              @success="handleSuccess"
              @remove="handleRemove"
              @review="handlePreview"
              :disabled="formData.status != 1"
              :limit="100"
              :fileList="fileList"
              :size="15"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="wrap">
      <el-button class="btn-wrap" size="small" @click="goback">返回</el-button>
      <el-button
        v-if="formData.status == 1"
        class="btn-wrap"
        size="small"
        type="primary"
        @click="submit"
        >提交</el-button
      >
    </div>
  </div>
</template>

<script>
import uploadFile from "@/components/uploadFile.vue";
import { hiddenDanger } from "@/api/projectUnitDetail";
import { cloneObj } from "@/utils/utils.js";

export default {
  props: {
    infoDetail: Object,
  },
  watch: {
    infoDetail: {
      handler: function (val) {
        console.log(val, "dasdasdasd");
        this.formData = cloneObj(val);
        this.setFormData();
      },
      deep: true,
    },
  },
  computed: {
    headers() {
      return {
        Authorization: this.$sessionData("get", "userInfo").accessToken,
      };
    },
  },
  components: { uploadFile },
  data() {
    return {
      fileListOne: [],
      fileListTwo: [],
      fileList: [],
      submitType: true,
      formData: {},
    };
  },
  mounted() {
    this.formData = cloneObj(this.infoDetail);
    this.setFormData();
  },
  methods: {
    setFormData() {
      if (this.formData.imagesBefore) {
        this.formData.imagesBefore = this.formData.imagesBefore.split(",");
        this.fileListOne = this.formData.imageBeforeList.map((item) => {
          return {
            name: item.fileName,
            filePath: item.fileUrl,
            ...item,
          };
        });
      } else {
        this.formData.imagesBefore = [];
        this.fileListOne = [];
      }
      if (this.formData.imagesAfter) {
        this.formData.imagesAfter = this.formData.imagesAfter.split(",");
        this.fileListTwo = this.formData.imageAfterList.map((item) => {
          return {
            name: item.fileName,
            filePath: item.fileUrl,
            ...item,
          };
        });
      } else {
        this.formData.imagesAfter = [];
        this.fileListTwo = [];
      }
      if (this.formData.fileIds) {
        this.formData.fileIds = this.formData.fileIds.split(",");
        this.fileList = this.formData.fileList.map((item) => {
          return {
            name: item.fileName,
            url: item.fileUrl,
            ...item,
          };
        });
      } else {
        this.formData.fileIds = [];
        this.fileList = [];
      }
    },
    clearImage(index, type) {
      if (type == 1) {
        this.formData.imagesBefore.splice(index, 1);
        this.fileListOne.splice(index, 1);
      } else {
        this.formData.imagesAfter.splice(index, 1);
        this.fileListTwo.splice(index, 1);
      }
    },
    handleSuccessOne(res, file) {
      if (res.code == 0) {
        let index = this.fileListOne.findIndex((item) => item.uid == file.uid);
        this.fileListOne[index] = { ...this.fileListOne[index], ...res.data };
        this.$set(this.fileListOne, index, this.fileListOne[index]);
        this.formData.imagesBefore.push(res.data.id);
      } else {
        let index = this.fileListOne.findIndex((item) => item.uid == file.uid);
        this.$message.error(
          `${this.fileListOne[index].name}上传失败，请重新上传`
        );
        this.fileListOne.splice(index, 1);
      }
    },
    handleProgressOne(event, file) {
      let index = this.fileListOne.findIndex((item) => item.uid == file.uid);
      this.fileListOne[index].num = event.percent;
    },
    handleBeforeUploadOne(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.fileListOne.push(obj);
    },
    handleSuccessTwo(res, file) {
      console.log(file);
      if (res.code == 0) {
        let index = this.fileListTwo.findIndex((item) => item.uid == file.uid);
        this.fileListTwo[index] = { ...this.fileListTwo[index], ...res.data };
        this.$set(this.fileListTwo, index, this.fileListTwo[index]);
        this.formData.imagesAfter.push(res.data.id);
      } else {
        let index = this.fileListTwo.findIndex((item) => item.uid == file.uid);
        this.$message.error(
          `${this.fileListTwo[index].name}上传失败，请重新上传`
        );
        this.fileListTwo.splice(index, 1);
      }
    },
    handleProgressTwo(event, file) {
      let index = this.fileListTwo.findIndex((item) => item.uid == file.uid);
      this.fileListTwo[index].num = event.percent;
    },
    handleBeforeUploadTwo(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.fileListTwo.push(obj);
    },
    handleSuccess(data) {
      if (data.file.code == 0) {
        let files = data.file.data;
        let obj = {
          name: files.originalName,
          url: files.filePath,
          ...files,
        };
        this.fileList.push(obj);
        this.formData.fileIds.push(files.id);
      } else {
        this.$message.error(res.msg);
      }
    },
    handleRemove(file) {
      let index = this.fileList.findIndex((item) => item.id == file.id);
      this.fileList.splice(index, 1);
      this.formData.fileIds.splice(index, 1);
    },
    handlePreview(data) {
      console.log(data);
    },
    submit() {
      if (!this.submitType) {
        return false;
      }
      this.submitType = false;
      let formData = cloneObj(this.formData);
      formData.imagesBefore = formData.imagesBefore.join(",");
      formData.imagesAfter = formData.imagesAfter.join(",");
      formData.fileIds = formData.fileIds.join(",");
      hiddenDanger
        .implementSubmit(formData)
        .then((res) => {
          if (res.code == 0) {
            this.$message.success("提交成功~");
            this.goback();
          } else {
            this.$message.error(res.message);
          }
          this.submitType = true;
        })
        .catch((err) => {
          this.$message.error(err);
          this.submitType = true;
        });
    },
    goback() {
      this.$emit("goInfo");
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  padding-top: 16px;
  .wrap_box_title {
    position: relative;
    line-height: 25px;
    padding: 0 10px 16px 24px;
    border-bottom: 1px solid #eeeeee;
    &::after {
      content: "";
      position: absolute;
      width: 4px;
      height: 20px;
      background: #1492ff;
      left: 10px;
      top: 2px;
    }
    > p {
      font-size: 18px;
      font-weight: bold;
      color: #333333;
    }
  }
  > div:nth-child(2) {
    padding: 24px;
  }
}
.dis-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  > div:nth-child(1) {
    min-height: 640px;
  }
  > div:nth-child(2) {
    padding: 19px 0;
    text-align: center;
    margin-top: 20px;
    .btn-wrap {
      width: 84px;
    }
  }
}
.item-upload {
  display: flex;
  .label-item-upload {
    font-weight: 400;
    color: #666666;
    line-height: 26px;
    font-size: 16px;
  }
  .value-item-upload {
    display: flex;
    flex-wrap: wrap;
    width: calc(100% - 100px);
    padding-bottom: 8px;
    .preview-item {
      width: 80px;
      height: 80px;
      border-radius: 2px;
      // overflow: hidden;
      margin: 0 12px 12px 0;
      background: #ebedf0;
      position: relative;
      .preview-list {
        width: 100%;
        height: 100%;
        ::v-deep .el-image {
          .el-image-viewer__wrapper {
            .el-image-viewer__close {
              color: #ffffff;
            }
          }
        }
      }
      .progress-box {
        width: 48px;
        height: 4px;
        background: #ffffff;
        border-radius: 4px;
        margin: 38px auto 0;
        > div {
          height: 100%;
          border-radius: 4px;
          background-color: #155bd4;
        }
      }
      .icon-btn {
        position: absolute;
        right: -8px;
        top: -8px;
        z-index: 3;
        cursor: pointer;
        color: #969799;
        font-size: 20px;
        display: none;
      }
      &:hover {
        .icon-btn {
          display: block;
        }
      }
    }
    &.uploadFile {
      padding-top: 7px;
    }
  }
}
.el-upload-box {
  margin-bottom: 12px;
  ::v-deep .el-upload--picture-card {
    width: 80px;
    height: 80px;
    line-height: 80px;
  }
}
</style>