<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-16 10:02:22
 * @Description: 
 * @FilePath: \lucheng\src\components\uploadFile.vue
-->
<template>
  <div>
    <el-upload
      class="upload-demo"
      :action="config.fileUploadUrl"
      :on-preview="handlePreview"
      :headers="headers"
      :before-upload="handleBeforeUpload"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :on-error="handleError"
      :accept="accept"
      :multiple="multiple"
      :name="name"
      :limit="limit"
      :file-list="fileList"
    >
      <div class="upload-btn">
        <img src="../assets/images/upload.png" alt="" />
        <span>添加文件</span>
      </div>
      <div slot="tip" class="el-upload__tip">只能上传{{ acceptType }}文件，且不超过{{ size }}{{ sizeType }}</div>
    </el-upload>
  </div>
</template>

<script>
import { localData } from '@/plugins/storage';
import config from '@/config/config';
export default {
  props: {
    //上传的文件类型 例如'.pdf,.jpg'
    accept: {
      type: String,
      default: ''
    },
    //上传文件个个数
    limit: {
      type: Number,
      default: 1
    },
    //上传文件的大小
    size: {
      type: Number,
      default: 1
    },
    //上传文件大小的后缀
    sizeType: {
      type: String,
      default: 'MB'
    },
    //上传的文件字段名
    name: {
      type: String,
      default: 'uploadFile'
    },
    //是否多选
    multiple: {
      type: Boolean,
      default: false
    },
    //上传的文件列表
    fileList: {
      type: Array,
      default: () => {
        return [];
      }
    },
    //自定义传参
    fromData: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {
    return {
      headers: {
        Authorization: localData("get", "userInfo").accessToken,
      },
      acceptType: '',
      config: config
    };
  },
  mounted() {
    let arr = this.accept.replace(/\,/g, '').split('.');
    arr.shift();
    if (arr.length == 1) {
      this.acceptType = arr.join('');
    } else if (arr.length > 1) {
      this.acceptType = arr.join('/');
    }
  },
  methods: {
    //上传之前的验证
    handleBeforeUpload(file) {
      let nameArr = file.name.split('.');
      let type = nameArr[nameArr.length - 1];
      if (this.accept.indexOf(type) == -1) {
        this.$message.error('文件格式不正确，请重新上传~');
        return false;
      }
      let sizeNum = '';
      if (this.sizeType == 'MB' || this.sizeType == 'mb') {
        sizeNum = file.size / 1024 / 1024;
      } else if (this.sizeType == 'KB' || this.sizeType == 'kb') {
        sizeNum = file.size / 1024;
      }
      if (sizeNum > this.size) {
        this.$message.error('文件过大，请重新上传~');
        return false;
      }
      return true;
    },
    //预览文件
    handlePreview(data) {
      this.$emit('review', data);
    },
    //移除文件
    handleRemove(data) {
      this.$emit('remove', data);
    },
    //上传成功
    handleSuccess(res) {
      let obj = {
        file: res,
        data: this.fromData
      };
      this.$emit('success', obj);
    },
    //上传失败
    handleError(res) {
      let obj = {
        file: res,
        data: this.fromData
      };
      this.$emit('error', obj);
    }
  }
};
</script>

<style lang="scss" scoped>
.upload-demo {
  .upload-btn {
    display: flex;
    align-items: center;
    color: #00baff;
    img {
      width: 14px;
      height: 14px;
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

<style lang="scss">
.el-upload-list__item:hover {
  background-color: transparent;
}
.el-upload-list__item.is-success .el-upload-list__item-name:hover,
.el-upload-list__item.is-success .el-upload-list__item-name:focus {
  outline: 0;
}
</style>