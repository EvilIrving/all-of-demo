<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="日期：" prop="materialDate">
            <el-date-picker
              type="date"
              placeholder="请选择"
              v-model="form.materialDate"
              style="width: 300px"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="实施人员：" prop="implementer">
            <el-input
              v-model="form.implementer"
              placeholder="请输入"
              maxlength="50"
              style="width: 320px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="上传图片：" prop="imgIds">
        <span>实施前照片</span>
        <div class="avatar-uploader">
          <div
            class="img_box"
            v-for="(item, index) in startPhotoFiles"
            :key="index"
          >
            <span
              class="el-icon-error icon-btn"
              @click.stop="clearImage(index, 1)"
            ></span>
            <div class="preview-list" v-if="item.fileUrl">
              <el-image
                style="width: 100%; height: 100%"
                :src="item.fileUrl"
                :preview-src-list="[item.fileUrl]"
              >
              </el-image>
            </div>
            <div class="progress-box" v-else>
              <div :style="`width:${item.num || 0}%`"></div>
            </div>
          </div>
          <el-upload
            :action="$config.uploadUrl"
            :headers="headers"
            :on-success="handlePictureOne"
            :show-file-list="false"
            :on-progress="handleProgressOne"
            :before-upload="handleBeforeOne"
            accept=".png,.jpg,.jpeg"
            v-model="form.startPhoto"
            :file-list="startPhotoFiles"
            name="uploadFile"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </el-form-item>

      <el-form-item label="上传图片：" prop="imgIds">
        <span>实施后照片</span>
        <div class="avatar-uploader">
          <div
            class="img_box"
            v-for="(item, index) in endPhotoFiles"
            :key="index"
          >
            <span
              class="el-icon-error icon-btn"
              @click.stop="clearImage(index, 2)"
            ></span>
            <div class="preview-list" v-if="item.fileUrl">
              <el-image
                style="width: 100%; height: 100%"
                :src="item.fileUrl"
                :preview-src-list="[item.fileUrl]"
              >
              </el-image>
            </div>
            <div class="progress-box" v-else>
              <div :style="`width:${item.num || 0}%`"></div>
            </div>
          </div>
          <el-upload
            :action="$config.uploadUrl"
            :headers="headers"
            :on-success="handlePictureTwo"
            :show-file-list="false"
            :on-progress="handleProgressTwo"
            :before-upload="handleBeforeTwo"
            accept=".png,.jpg,.jpeg"
            v-model="form.endPhoto"
            :file-list="endPhotoFiles"
            name="uploadFile"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </el-form-item>

      <div class="submit-box">
        <el-button
          @click="
            () => {
              this.$emit('closeEditDialog');
            }
          "
          >取消</el-button
        >
        <el-button type="primary" @click="submitForm('form')">提交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { greenCleaning } from "@/api/projectUnitDetail";

export default {
  name: "newCleanForm",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  components: {},
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },

  data() {
    return {
      form: {
        materialDate: "",
        implementer: "",
        endPhoto: [],
        startPhoto: [],
      },
      startPhotoFiles: [],
      endPhotoFiles: [],
      fileList: {},
      headers: {
        Authorization: this.$sessionData("get", "userInfo").accessToken,
      },
      submitFlag: true,
    };
  },
  mounted() {
    if (this.id) {
       this.form.id = this.id
      this.getDetail();
    }
  },
  methods: {
    clearImage(index, type) {
      if (type === 1) {
        this.form.startPhoto.splice(index, 1);
        this.startPhotoFiles.splice(index, 1);
      } else {
        this.endPhotoFiles.splice(index, 1);
        this.form.endPhoto.splice(index, 1);
      }
    },
    handlePictureOne(res, file) {
      if (res.code == 0) {
        let index = this.startPhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.startPhotoFiles[index] = {
          ...this.startPhotoFiles[index],
          ...res.data,
          fileUrl: res.data.filePath,
        };
        this.$set(this.startPhotoFiles, index, this.startPhotoFiles[index]);
        this.form.startPhoto.push(res.data.id);
        console.log(this.startPhotoFiles, 22222222);
      } else {
        let index = this.startPhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.$message.error(
          `${this.startPhotoFiles[index].name}上传失败，请重新上传`
        );
        this.startPhotoFiles.splice(index, 1);
      }
    },
    handlePictureTwo(res, file) {
      if (res.code == 0) {
        let index = this.endPhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.endPhotoFiles[index] = {
          ...this.endPhotoFiles[index],
          ...res.data,
          fileUrl: res.data.filePath,
        };
        this.$set(this.endPhotoFiles, index, this.endPhotoFiles[index]);
        this.form.endPhoto.push(res.data.id);
      } else {
        let index = this.endPhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.$message.error(
          `${this.endPhotoFiles[index].name}上传失败，请重新上传`
        );
        this.endPhotoFiles.splice(index, 1);
      }
    },
    handleProgressOne(e, file) {
      let index = this.startPhotoFiles.findIndex(
        (item) => item.uid == file.uid
      );
      this.startPhotoFiles[index].num = e.percent;
    },
    handleProgressTwo(e, file) {
      let index = this.endPhotoFiles.findIndex((item) => item.uid == file.uid);
      this.endPhotoFiles[index].num = e.percent;
    },
    handleBeforeOne(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.startPhotoFiles.push(obj);
    },
    handleBeforeTwo(file) {
      console.log(file);
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.endPhotoFiles.push(obj);
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime;
          if (this.startPhotoFiles.length !== 0) {
            this.form.startPhoto = this.form.startPhoto.join(",");
          }
          if (this.endPhotoFiles.length !== 0) {
            this.form.endPhoto = this.form.endPhoto.join(",");
          }
          this.form["prcd"] = this.projectInfo.prcd;
          console.log(this.form);
          greenCleaning
            .saveGreenCleaning(this.form)
            .then((res) => {
              console.log(res);
              console.log(this.form, 1111111111);
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                //this.$message.error(res.msg);
                this.$message.error({ message: res.msg || "添加失败" });
              }
              this.submitFlag = true;
            })
            .catch((err) => {
              this.submitFlag = true;
              this.$message.error(err);
            });
        } else {
          return false;
        }
      });
    },
    async getDetail() {
      let res = await greenCleaning.getGreenCleaning({
        id: this.id,
      });
      if (res.code == 0) {
        this.form.materialDate = res.data.materialDate;
        this.form.implementer = res.data.implementer;
        this.form.endPhoto = res.data.endPhoto
          ? res.data.endPhoto.split(",")
          : [];
        this.form.startPhoto = res.data.startPhoto
          ? res.data.startPhoto.split(",")
          : [];
        this.endPhotoFiles = res.data.endPhotoFiles || [];
        this.startPhotoFiles = res.data.startPhotoFiles || [];
      }
    },
  },
};
</script>

<style scoped lang="scss">
.wrap-form-box {
  .training-form-box {
    display: flex;
    width: 100%;
    justify-content: space-between;
    > div {
      width: 48%;
    }
  }
  .submit-box {
    text-align: right;
  }
  .img_box {
    cursor: pointer;
    float: left;

    @include flexbox();
    flex-direction: row;
    margin-right: 3px;
    img {
      width: 80px;
      height: 80px;
      margin: 0 5px;
    }

    > div {
      width: 80px;
      height: 80px;
      margin-right: 3px;
      padding: 2px;
      position: relative;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(center);

      .active {
        display: none;
      }
    }
  }
}
</style>


<style lang="scss">
.avatar-uploader {
  width: 90%;
  @include flexbox();
  .el-upload {
    height: 80px;
    width: 80px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    .img_box {
      .el-progress--without-text .el-progress-bar {
        display: block;
      }
      .el-progress-bar {
        width: 60%;
        margin-top: 30px;
      }
    }
  }
}
</style>