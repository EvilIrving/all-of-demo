<template>
  <view-page>
    <div class="project-wrap dispose-info">
      <van-cell-group>
        <van-field readonly label="处理前图片：" placeholder="请上传图片">
          <template #input>
            <div class="upload-preview-box">
              <div v-for="item in form.problemsPictureList" :key="item.id">
                <van-image
                  width="70"
                  height="70"
                  @click="imagePreview(item.fileUrl)"
                  :src="item.fileUrl"
                />
              </div>
            </div>
          </template>
        </van-field>
        <van-field
          v-model="ruleForm.handling_instruction"
          label="处理后说明："
          placeholder="请输入"
          type="textarea"
          rows="2"
          autosize
        />
        <van-field readonly label="处理后图片：" placeholder="请上传图片">
          <template #input>
            <div class="upload-preview-box">
              <div v-for="item in fileList" :key="item.id">
                <van-image
                  width="80"
                  height="80"
                  @click="imagePreview(item.filePath)"
                  :src="item.filePath"
                />
                <van-icon
                  @click.stop="clearImg(item.id)"
                  class="clear-btn-icon"
                  name="clear"
                />
              </div>
              <van-uploader
                class="upload-box"
                name="problemsPicture"
                :before-read="beforeRead"
                :after-read="afterRead"
              >
              </van-uploader>
            </div>
          </template>
        </van-field>
      </van-cell-group>
      <div class="van-button-box">
        <van-button color="#036EFF" block :loading="loading" @click="submitForm"
          >提交</van-button
        >
      </div>
    </div>
  </view-page>
</template>
<script>
import { supervisedCheck } from "@/api/projectDetail";
import { fileUploadApi, handingInspectionApi } from "@/api/rainfall.js";
export default {
  data() {
    return {
      form: {},
      showPopup: false,
      iframeSrc: "",
      iframeTitle: "",
      fileList: [],
      ruleForm: {
        handling_instruction: "",
        handling_picture: "",
      },
      loading: false,
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.list({ id: this.$route.query.id });
      if (res.code == 0) {
        this.form = res.data.list[0];
      }
    },
    beforeRead(file, detail) {
      return true;
    },
    afterRead(file, detail) {
      console.log(file, detail);
      let formData = new FormData();
      formData.append("uploadFile", file.file);
      fileUploadApi(formData)
        .then((res) => {
          if (res.code == 0) {
            this.fileList.push(res.data);
          } else {
            this.$toast.fail(res.msg);
          }
        })
        .catch((err) => {
          this.$toast(err);
        });
    },
    clearImg(id) {
      let index = this.fileList.findIndex((item) => item.id == id);
      this.fileList.splice(index, 1);
    },
    lookFile(data) {
      this.iframeSrc = data.fileUrl;
      this.iframeTitle = data.fileName;
      this.showPopup = true;
    },
    imagePreview(src) {
      this.$imagePreview([src]);
    },
    async submitForm() {
      this.loading = true;
      if (this.fileList.length > 0) {
        let arr = [];
        this.fileList.forEach((item) => {
          arr.push(item.id);
        });
        this.ruleForm.handling_picture = arr.join(",");
      }
      let res = await handingInspectionApi({
        id: this.form.id,
        handType: 2,
        ...this.ruleForm,
      });
      if (res.code == 0) {
        let _this = this;
        this.$toast.success({
          message: "已处理成功~",
          duration: 1000,
          onClose: () => {
            _this.loading = false;
            _this.$router.replace(
              "propertySupervision/scheduleDetail?id=" + this.form.id
            );
          },
        });
      } else {
        this.loading = false;
        this.$toast.fail(res.message);
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.project-wrap {
  width: 100%;
  min-height: 100%;
  background: #ffffff;
  .cell-value {
    color: #333333;
    font-size: 14px;
    font-weight: 500;
    flex: 2.2;
  }
  .cell-title {
    font-weight: 500;
    color: #666666;
    font-size: 14px;
  }
  .upload-preview-box {
    display: flex;
    width: 100%;
    flex-flow: wrap;
    > div {
      margin: 0 9px 9px 0;
      position: relative;
      .clear-btn-icon {
        position: absolute;
        top: -6px;
        right: -6px;
        font-size: 18px;
      }
    }
  }
  .van-button-box {
    padding: 100px 20px 0;
    .van-button{
      font-size: 20px;
    }
  }
}
</style>

<style lang="scss">
.dispose-info .upload-box {
  .van-uploader__wrapper {
    width: 70px;
    height: 70px;
    .van-uploader__preview,
    .van-uploader__upload {
      width: 70px;
      height: 70px;
    }
    .van-uploader__upload {
      margin: 0;
    }
    .van-uploader__preview-image {
      width: 100%;
      height: 100%;
    }
  }
}
.upload-preview-box .van-image {
  margin: 0;
}
</style>