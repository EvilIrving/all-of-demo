<template>
  <div>
    <el-upload
      :action="action"
      :headers="headers"
      :data="data"
      :name="name"
      :accept="accept"
      :show-file-list="showFileList"
      :with-credentials="withCredentials"
      :disabled="disabled"
      :multiple="multiple"
      :limit="limit"
      :list-type="listType"
      :on-exceed="handleExceed"
      :on-success="handleSuccess"
      :before-upload="beforeUpload"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :file-list="fileListObj">
      <i class="el-icon-plus" v-if="listType === 'picture-card'"></i>
      <el-button size="small" type="primary" v-else>点击上传</el-button>
      <slot slot="tip"></slot>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible" append-to-body title="查看图片">
      <img width="100%" :src="imageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "file-upload",
    data() {
      return {
        /*fileListObj: [{
          name: 'food.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        }, {
          name: 'food2.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        }]*/
        fileListObj: this.fileList,
        dialogVisible: false,
        imageUrl: null
      };
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
      // 是否支持多选文件
      multiple: {
        type: Boolean,
        required: false
      },
      //接受上传的文件类型（thumbnail-mode 模式下此参数无效）
      // 图片 image/gif,image/jpeg,image/jpg,image/png,image/svg
      accept: {
        type: String
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
      },
      // 最大允许上传个数
      limit: {
        type: Number
      },
      //上传的文件列表
      fileList: {
        type: Array,
        default() {
          return [];
        }
      },
      // 是否显示已上传文件列表
      showFileList: {
        type: Boolean,
        default: true
      },
      // 文件列表的类型
      listType: {
        type: String,
        default: 'text'
      }
    },
    methods: {
      /**
       * 文件列表移除文件时的钩子
       * @param file
       * @param fileList
       */
      handleRemove(file, fileList) {
        // console.log(file, fileList);
        this.$emit('success', fileList);
      },
      /**
       * 点击文件列表中已上传的文件时的钩子
       * @param file
       */
      handlePreview(file) {
        let imgTypeArr = ['png', 'jpg', 'jpeg', 'bmp', 'gif'];
        let suffixIndex = file.name.lastIndexOf(".");
        let suffix = file.name.substring(suffixIndex + 1).toLowerCase();
        if (imgTypeArr.indexOf(suffix) !== -1) {
          this.dialogVisible = true;
          this.imageUrl = file.url;
        }
      },
      /**
       * 文件超出个数限制时的钩子
       * @param files
       * @param fileList
       */
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 ${this.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      /**
       * 文件上传成功时的钩子
       * @param res
       * @param file
       */
      handleSuccess(res, file, fileList) {
        this.$emit('success', fileList);
      },
      /**
       * 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
       * @param file
       * @returns {boolean|boolean}
       */
      beforeUpload(file) {
        if (this.accept) {
          const isJPG = this.accept.indexOf(file.type) !== -1;
          if (!isJPG) {
            this.$message.error(`上传头像图片只能是 ${this.accept} 格式!`);
          }
          return isJPG;
        }
        return true;
      }
    }
  }
</script>

<style scoped>

</style>
