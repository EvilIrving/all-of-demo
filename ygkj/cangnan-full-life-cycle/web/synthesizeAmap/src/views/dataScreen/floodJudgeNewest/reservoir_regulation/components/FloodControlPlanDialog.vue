<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    top="10vh"
    title="防汛预案"
    class="dispatch-dialog"
    :close-on-click-modal="false"
  >
    <div class="dialog-content" style="height: 650px">
      <div class="preview-body">
        <div class="pdf" v-show="fileType == 'pdf'">
          <pdf v-for="item in numPages" :key="item" :src="src" :page="item" />
        </div>
        <div class="pdf" v-show="fileType == '图片'">
          <img style="width: 100%" :src="fileUrl" alt="" />
        </div>
      </div>
    </div>
    <!-- 尾部取消 -->
    <div slot="footer">
      <div class="form-footer">
        <el-button class="form-btn btn-save" @click="handleClose"
          >取消</el-button
        >
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { getFileList } from "@/api/login";
import pdf from "vue-pdf";
export default {
  name: "JudgedDialog",
  components: { pdf },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      show: true,
      currentPage: 1,
      fileList: [],
      numPages: [],
      fileType: "pdf",
      src: "",
      fileId: "",
      fileUrl: "",
      id: "0bc1686c-1c24-4555-9a36-2535ddda5401",
    };
  },
  mounted() {
    this.getFile();
  },
  methods: {
    //取消按钮
    handleClose() {
      this.$emit("handleClose");
    },
    //pdf调用
    aa(src) {
      this.src = pdf.createLoadingTask(src);
      this.src.promise.then((pdf) => {
        this.numPages = pdf.numPages;
      });
    },
    // pdf加载时
    loadPdfHandler(e) {
      this.currentPage = 1; // 加载的时候先加载第一页
    },
    // 获取图片id
    async getFile() {
      this.fileList = [];
      await getFileList({ ids: this.id }).then((res) => {
        if (res.code == 0) {
          let info = res.data.data;
          info.map((item) => {
            this.fileList.push({
              name: item.fileName,
              url: item.fileUrl,
              id: item.id,
            });
          });
          if (this.fileType == "pdf") {
            this.aa(this.fileList[0].url);
            this.fileId = this.fileList[0].id;
          } else if (this.fileType == "图片") {
            this.fileUrl = this.fileList[0].url;
            // window.open(this.fileList[0].url)
          }
        }
      });
    },
  },
};
</script>

<style lang="scss">
.dispatch-dialog {
  user-select: none;
  .el-dialog {
    .dialog-content {
      overflow: auto;
      padding: 10px 30px;
    }
  }
}
.form-btn {
  background: rgba(0, 0, 0, 0);
  color: #2c72be;
  border: none;
  border-radius: 0;
  &.btn-save {
    color: #fff;
    background: #239def;
  }
}
</style>