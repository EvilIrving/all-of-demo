<template>
  <div class="wrap-form-box">
    <el-form ref="form" :model="form" label-width="150px">
      <el-row>
        <el-col :span="11">
          <el-form-item label="日期：" prop="materialDate">
            <el-date-picker
              type="date"
              placeholder="请选择"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              v-model="form.materialDate"
              style="width: 320px"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="设备名称：" prop="equipmentName">
            <el-input
              v-model="form.equipmentName"
              placeholder="请输入"
              maxlength="50"
              style="width: 320px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="维护单位：" prop="maintainUnit">
            <el-input
              v-model="form.maintainUnit"
              placeholder="请输入"
              maxlength="50"
              style="width: 320px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="负责人：" prop="projectLeader">
            <el-input
              v-model="form.projectLeader"
              placeholder="请输入"
              maxlength="50"
              style="width: 320px"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="上传照片：">
        <span>实施前照片</span>
        <div class="avatar-uploader">
          <div
            class="img_box"
            v-for="(item, index) in beforePhotoFiles"
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
            v-model="form.beforePhoto"
            :file-list="beforePhotoFiles"
            name="uploadFile"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </el-form-item>

      <el-form-item label="上传照片：">
        <span>实施后照片</span>
        <div class="avatar-uploader">
          <div
            class="img_box"
            v-for="(item, index) in afterPhotoFiles"
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
            :on-success="handlePictureTwo"
            :show-file-list="false"
            :on-progress="handleProgressTwo"
            :before-upload="handleBeforeTwo"
            accept=".png,.jpg,.jpeg"
            v-model="form.afterPhoto"
            :file-list="afterPhotoFiles"
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
        <el-button type="primary" @click="submitForm('form')">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { databasedM } from "@/api/projectUnitDetail";

export default {
  name: "hardwareForm",
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
        equipmentName: "",
        maintainUnit: "",
        projectLeader: "",
        beforePhoto: [],
        afterPhoto: [],
      },
      afterPhotoFiles: [],
      beforePhotoFiles: [],
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
        this.form.beforePhoto.splice(index, 1);
        this.beforePhotoFiles.splice(index, 1);
      } else {
        this.afterPhotoFiles.splice(index, 1);
        this.form.afterPhoto.splice(index, 1);
      }
    },
    handlePictureOne(res, file) {
      if (res.code == 0) {
        let index = this.beforePhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.beforePhotoFiles[index] = {
          ...this.beforePhotoFiles[index],
          ...res.data,
          fileUrl: res.data.filePath,
        };
        this.$set(this.beforePhotoFiles, index, this.beforePhotoFiles[index]);
        this.form.beforePhoto.push(res.data.id);
        console.log(this.beforePhotoFiles, 22222222);
      } else {
        let index = this.beforePhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.$message.error(
          `${this.beforePhotoFiles[index].name}上传失败，请重新上传`
        );
        this.beforePhotoFiles.splice(index, 1);
      }
    },
    handlePictureTwo(res, file) {
      if (res.code == 0) {
        let index = this.afterPhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.afterPhotoFiles[index] = {
          ...this.afterPhotoFiles[index],
          ...res.data,
          fileUrl: res.data.filePath,
        };
        this.$set(this.afterPhotoFiles, index, this.afterPhotoFiles[index]);
        this.form.afterPhoto.push(res.data.id);
      } else {
        let index = this.afterPhotoFiles.findIndex(
          (item) => item.uid == file.uid
        );
        this.$message.error(
          `${this.afterPhotoFiles[index].name}上传失败，请重新上传`
        );
        this.afterPhotoFiles.splice(index, 1);
      }
    },
    handleProgressOne(e, file) {
      let index = this.beforePhotoFiles.findIndex(
        (item) => item.uid == file.uid
      );
      this.beforePhotoFiles[index].num = e.percent;
    },
    handleProgressTwo(e, file) {
      let index = this.afterPhotoFiles.findIndex(
        (item) => item.uid == file.uid
      );
      this.afterPhotoFiles[index].num = e.percent;
    },
    handleBeforeOne(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.beforePhotoFiles.push(obj);
    },
    handleBeforeTwo(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.afterPhotoFiles.push(obj);
    },

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.submitFlag) {
            return false;
          }
          this.submitFlag = false;
          delete this.form.createTime;
          delete this.form.paged;
          if (this.beforePhotoFiles.length !== 0) {
            this.form.beforePhoto = this.form.beforePhoto.join(",");
          }
          if (this.afterPhotoFiles.length !== 0) {
            this.form.afterPhoto = this.form.afterPhoto.join(",");
          }
          this.form.prcd = this.projectInfo.prcd
          console.log(this.form, 111111);
          databasedM
            .saveHardwareList(this.form)
            .then((res) => {
              if (res.code == 0) {
                let msg = "新增成功~";
                if (this.id) {
                  msg = "编辑成功~";
                }
                this.$message.success(msg);
                this.$emit("closeEditDialog", 1);
              } else {
                this.$message.error(res.msg);
                // console.log(res.msg)
                //this.$message.error({message:res.msg|| "新增失败"});
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
      let res = await databasedM.getHardwareDetail({
        id: this.id,
        //prcd:this.projectInfo.prcd
      });
      if (res.code == 0) {
        //Object.assign(this.form, res.data)
        console.log(res);
        //this.form.annual = res.data.list[0].annual.toString()
        this.form.materialDate = res.data.materialDate;
        this.form.equipmentName = res.data.equipmentName;
        this.form.maintainUnit = res.data.maintainUnit;
        this.form.projectLeader = res.data.projectLeader;
        this.form.beforePhoto = res.data.beforePhoto
          ? res.data.beforePhoto.split(",")
          : [];
        this.form.afterPhoto = res.data.afterPhoto
          ? res.data.afterPhoto.split(",")
          : [];
        this.beforePhotoFiles = res.data.beforePhotoFiles || [];
        this.afterPhotoFiles = res.data.afterPhotoFiles || [];
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