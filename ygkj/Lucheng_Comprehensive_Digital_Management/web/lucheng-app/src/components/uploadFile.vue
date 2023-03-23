<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-26 10:51:49
 * @Description:
 * @FilePath: \lucheng_project\src\components\uploadFile.vue
-->
<template>
  <div>
    <el-upload
      ref="elUpload"
      class="upload-demo"
      :list-type="listType"
      :action="$config.uploadUrl"
      :on-preview="handlePreview"
      :headers="headers"
      :before-upload="handleBeforeUpload"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :accept="accept"
      :multiple="multiple"
      :name="name"
      :limit="limit"
      :file-list="fileList"
      :show-file-list="showFileList"
      :disabled="disabled"
    >
      <i v-if="listType == 'picture-card'" class="el-icon-plus"></i>
      <div class="upload-btn" v-if="listType != 'picture-card'">
        <img src="../assets/images/officeInformation/uploadIcon.png" alt="" />
        <span>添加文件</span>
      </div>
      <div slot="tip" class="el-upload__tip" v-if="acceptType">
        只能上传{{ acceptType }}文件，且单个文件不超过{{ size }}{{ sizeType }}
      </div>
      <div slot="tip" class="el-upload__tip" v-else>
        单个文件不超过{{ size }}{{ sizeType }}
      </div>
    </el-upload>
  </div>
</template>

<script>
export default {
  props: {
    //上传的文件类型 例如'.pdf,.jpg'
    accept: {
      type: String,
      default: "",
    },
    //上传文件个个数
    limit: {
      type: Number,
      default: 1,
    },
    //上传文件的大小
    size: {
      type: Number,
      default: 1,
    },
    //上传文件大小的后缀
    sizeType: {
      type: String,
      default: "MB",
    },
    //上传的文件字段名
    name: {
      type: String,
      default: "uploadFile",
    },
    //是否多选
    multiple: {
      type: Boolean,
      default: false,
    },
    //上传的文件列表
    fileList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    //是否禁用上传
    disabled: {
      type: Boolean,
      default: false,
    },
    //是否显示文件列表
    showFileList: {
      type: Boolean,
      default: true,
    },
    //自定义传参
    fromData: {
      type: Object,
      default: () => {
        return {};
      },
    },
    //文件列表的类型
    listType: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      headers: {
        Authorization: this.$sessionData("get", "userInfo").accessToken,
      },
      acceptType: "",
    };
  },
  mounted() {
    let arr = this.accept.replace(/\,/g, "").split(".");
    arr.shift();
    if (arr.length == 1) {
      this.acceptType = arr.join("");
    } else if (arr.length > 1) {
      this.acceptType = arr.join("/");
    }
  },
  methods: {
    //上传之前的验证
    handleBeforeUpload(file) {
      let nameArr = file.name.split(".");
      let type = nameArr[nameArr.length - 1];
      if (this.accept.indexOf(type) == -1) {
        this.$message.error("文件格式不正确，请重新上传~");
        return false;
      }
      let sizeNum = "";
      if (this.sizeType == "MB" || this.sizeType == "mb") {
        sizeNum = file.size / 1024 / 1024;
      } else if (this.sizeType == "KB" || this.sizeType == "kb") {
        sizeNum = file.size / 1024;
      }
      if (sizeNum > this.size) {
        this.$message.error("文件过大，请重新上传~");
        return false;
      }
      return true;
    },
    //预览文件
    handlePreview(data) {
      this.$emit("review", data);
    },
    //移除文件
    handleRemove(data) {
      this.$emit("remove", data);
    },
    //上传成功
    handleSuccess(res) {
      let obj = {
        file: res,
        data: this.fromData,
      };
      this.$emit("success", obj);
    },
  },
};
</script>

<style lang="scss" scoped>
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
