<template>
  <div>
    <el-upload
      class="avatar-uploader"
      :action="action"
      :headers="headers"
      :data="data"
      :name="name"
      :show-file-list="false"
      :with-credentials="withCredentials"
      :disabled="disabled"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload">
      <img v-if="imageUrl" :src="imageUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
  </div>
</template>

<script>
  export default {
    name: "avatar-upload",
    data() {
      return {
        imageUrl: ''
      }
    },
    props: {
      //必选参数，上传的地址
      action: {
        type: String,
        required: true
      },
      //设置上传的请求头部
      headers: {
        type: Object,
        default() {
          return {};
        }
      },
      // 上传时附带的额外参数
      data: {
        type: Object,
        default() {
          return {};
        }
      },
      // 上传的文件字段名
      name: {
        type: String,
        default: 'file'
      },
      // 支持发送 cookie 凭证信息
      withCredentials: {
        type: Boolean,
        default: false
      },
      // 是否禁用
      disabled: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      /**
       * 文件上传成功时的钩子
       * @param res
       * @param file
       */
      handleAvatarSuccess(res, file) {
        let data = res.data;
        this.imageUrl = data.url;
        this.$emit('success', res.data);
      },
      /**
       * 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
       * @param file
       * @returns {boolean|boolean}
       */
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      }
    }
  }
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
