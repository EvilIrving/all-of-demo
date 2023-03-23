<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="750px"
    :before-close="handleClose"
    top="24vh"
    :title="name"
    class="video-dialog"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="dialog-content">
      <div class="video-box">
        <primary-video ref="videoDialog"></primary-video>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { PrimaryVideo } from "@/components";
import BarChart from "./charts/BarChart";
import { dispatchApi } from "@/api/dataScreen/rescueSupport/dispatch.js";
export default {
  name: "RescueSupport",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    name: {
      type: String,
      default: "",
    },
    info: {
      type: Object,
    },
  },
  components: { PrimaryVideo },
  data() {
    return {};
  },
  mounted() {},
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="scss">
@import "@/style/blueDialog.scss";
.video-dialog {
  .el-dialog {
    background: rgba(0, 0, 0, 0)
      url(../../../../assets/images/blue-dialog-bg.png) no-repeat center center;
    background-size: 100% 100%;
    .el-dialog__header {
      background: rgba(0, 0, 0, 0);
      padding: 20px 20px 12px;
      .el-dialog__title {
        color: #4db6e4;
      }
      .el-dialog__headerbtn {
        top: 26px;
        right: 28px;
      }
    }
    .dialog-content {
      width: 100%;
      height: 450px;
      overflow: auto;
      .video-box {
        width: calc(100% - 40px);
        height: calc(100% - 40px);
        margin-left: 16px;
      }
    }
  }
}
</style>