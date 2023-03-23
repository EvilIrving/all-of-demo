<template>
  <el-dialog
    :visible.sync="visible"
    width="74%"
    center
    :before-close="close"
    top="5vh"
    title="预览文件"
    class="list-dialog preview-pdf"
    :close-on-click-modal="true"
  >
    <div class="preview-body">
      <div class="pdf">
        <pdf v-for="item in numPages" :key="item" :src="src" :page="item" />
      </div>
    </div>
    <div slot="footer">
      <el-row>
        <div class="form-footer">
          <el-button class="form-btn btn-cancel" @click="close">关闭</el-button>
        </div>
      </el-row>
    </div>
  </el-dialog>
</template>
<script>
import { getFileList } from "@/api/login";
// import pdf from "vue-pdf";
export default {
  props: {
    visible: Boolean,
    id: String,
  },
  components: {},
  watch: {
    visible(val) {
      this.$nextTick(() => {});
    },
  },
  data() {
    return {
      show: false,
      isLook: false,
      currentPage: 1,
      pageCount: 1,
      fileList: [],
      numPages: [],
      src: "",
      fileId: "",
      fileUrl: "",
    };
  },
  mounted() {
    this.getFile();
  },
  methods: {
    closeAddNote() {
      this.addNoteShow = false;
    },
    // 关闭
    close() {
      this.$emit("close");
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
            this.aa(this.fileList[0].url);
            this.fileId = this.fileList[0].id;
        }
      });
    },
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
  },
};
</script>
<style lang='scss'>
@import "@/style/listDialog.scss";
.list-dialog.preview-pdf .el-dialog--center .el-dialog__body {
  padding-bottom: 0;
  .list-dialog.preview-pdf .preview-body {
    .pdf {
      width: 100%;
      height: 700px;
      overflow-y: auto;
    }
  }
}
</style>
<style lang="scss" scoped>
.list-dialog.preview-pdf .preview-body {
  height: 700px;
  width: 100%;
  display: flex;
  font-size: 14px;
  .pdf {
    width: 100%;
    height: 700px;
    overflow-y: auto;
  }
  .menu-list {
    width: 240px;
    height: 100%;
    padding-top: 10px;
    overflow-y: auto;
    .menu-item {
      line-height: 36px;
      color: #999999;
      cursor: pointer;
      text-align: center;
      .active-circle {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #328173;
        vertical-align: middle;
      }
      &.active {
        color: #328173;
      }
    }
  }
  .notes-list {
    display: flex;
    flex-direction: column;
    width: 330px;
    height: 100%;
    padding-left: 16px;
    padding-top: 20px;
    .el-input .el-input__inner {
      background: rgba(255, 255, 255, 0.4);
      border: 1px solid #e5e5e5;
      border-radius: 20px;
      border-radius: 20px;
    }
    .notes-operate {
      margin: 10px 0;
      i {
        cursor: pointer;
        &:first-child {
          margin-right: 10px;
        }
      }
    }
    .notes-box {
      height: calc(100% - 64px);
      overflow-y: auto;
    }
    .notes-item {
      padding: 8px;
      margin-bottom: 10px;
    }
    .notes-item.active {
      background: #f2f2f2;
    }
    .notes-name {
      color: #328173;
      line-height: 20px;
    }
    .notes-content {
      color: #333333;
      line-height: 20px;
    }
  }
}
</style>
