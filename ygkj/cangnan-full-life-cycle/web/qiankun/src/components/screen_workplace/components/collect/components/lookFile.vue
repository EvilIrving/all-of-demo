<!--
 * @Author: hanyu
 * @LastEditTime: 2021-01-15 17:04:24
 * @Description: 预览文件
 * @FilePath: /hydrology/src/views/daily_business/archives_manage/components/lookFile.vue
-->
<template>
  <el-dialog
    :visible.sync="visible"
    width="74%"
    center
    :before-close="close"
    top="5vh"
    title="预览文件"
    class="custom-dialog preview-pdf"
    :close-on-click-modal="true"
    append-to-body
  >
    <div class="preview-body">
      <iframe
        :src="fileUrl"
        frameborder="0"
        style="width: 100%; height: 100%"
      ></iframe>
    </div>

    <div slot="footer">
      <el-row>
        <div class="form-footer">
          <el-button @click="close">关闭</el-button>
        </div>
      </el-row>
    </div>
  </el-dialog>
</template>
<script>
export default {
  props: {
    visible: Boolean,
    id: String,
    fileUrl: String,
  },
  components: {
  },
  watch: {
    visible(val) {
      this.$nextTick(() => { });
    },
  },
  data() {
    return {
    };
  },
  mounted() {
  },
  methods: {
    // 关闭
    close() {
      this.$emit("close");
    },
  },
};
</script>
<style lang='scss'>
@import "../style.scss";
.custom-dialog.preview-pdf .preview-body {
  height: 600px;
  width: 100%;
  font-size: 14px;
}
</style>
