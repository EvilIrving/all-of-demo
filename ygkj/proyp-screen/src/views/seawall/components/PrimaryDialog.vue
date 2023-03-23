<template>
  <el-dialog :top="top" :visible="visible" :width="width" :before-close="handleClose" :modal="modal" :append-to-body="appendToBody" full-screen :fullscreen="fullscreen" :close-on-click-modal="false" class="primary-dialog">
    <div class="primary-dialog__title" slot="title" :style="minimizeStatus ? 'justify-content: space-between;' : ''">
      <p class="primary-dialog__title--label">
        {{ mainTitle }}
      </p>
    </div>
    <div class="primary-dialog__content">
      <slot></slot>
    </div>
    <div v-if="showFooter" slot="footer" class="dialog-footer">
      <slot name="footer"></slot>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "PrimaryDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    showFooter: {
      type: Boolean,
      default: true,
    },
    appendToBody: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "2000px",
    },
    mainTitle: {
      type: String,
      default: "测试",
    },
    fullscreen: {
      type: Boolean,
      default: false,
    },
    fullscreenFlag: {
      type: Boolean,
      default: true,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    },
    modal: {
      type: Boolean,
      default: false,
    },
    top: {
      type: String,
      default: "16vh",
    },
  },
  data() {
    return {};
  },
  components: {},
  methods: {
    changeFullscreen() {
      this.$emit("changeFullscreen");
    },
    handleClose() {
      if (this.fullscreen) {
        this.$emit("changeFullscreen");
      }
      this.$emit("update:visible", false);
      this.$emit("handleClose");
    },
  },
};
</script>

<style lang="scss">
.primary-dialog {
  background: rgba($color: #111e52, $alpha: 0.8);
  z-index: 98 !important;
  .el-dialog {
    background: url($imageUrl + "dialog-bg.png") no-repeat;
    background-size: 100% 100%;
    background-color: rgba(3, 26, 43);
    overflow: hidden;
    @include flexbox;
    @include flexdirec(column, nowrap);
    .el-dialog__header {
      margin: 16px 16px 0;
      padding: 30px;
      border-bottom: 0;
      .primary-dialog__title {
        background: center/100% url($imageUrl + "title_line.png") no-repeat;
        height: 42px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          font-family: "youshe";
          font-size: 26px;
          color: #fff;
          height: 42px;
          width: 90%;
          padding-left: 15px;
          line-height: 20px;
        }
        .full-screen-icon {
          font-size: 20px;
          position: absolute;
          right: 80px;
          top: 43px;
          color: #fff;
          cursor: pointer;
          z-index: 99;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        top: 40px;
        right: 50px;
        .el-dialog__close {
          color: #ffffff;
          font-size: 24px;
        }
      }
    }
    .el-dialog__body {
      @include flex(1);
      min-height: 100px;
      margin: 0 14px;
      border-top: 0;
      border-bottom: 0;
      padding: 0 20px;
      .primary-dialog__content {
        height: 100%;
      }
    }
    .el-dialog__footer {
      margin: 0 18px 24px;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        padding-top: 44px;
        .el-button {
          text-align: center;
          border-radius: 4px;
          padding: 0;
          &.cancel {
            color: #00baff;
            border: 1px solid #00baff;
            background-color: transparent;
            width: 67px;
            height: 35px;
            line-height: 35px;
          }
          &.confirm {
            color: #fff;
            border: 0;
            background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
            width: 67px;
            height: 35px;
            line-height: 35px;
          }
        }
      }
    }
    .el-table {
      .cell {
        height: 100%;
        line-height: 37px;
      }
      .el-checkbox__inner {
        border: 0.0625rem solid #00c1ff;
        background-color: #00c1ff;
        background: rgba($color: #000000, $alpha: 0);
      }
      .el-table__body tr.current-row > td {
        background-color: rgba(41, 147, 180, 0.3);
      }
      th,
      td {
        padding: 0;
        font-size: 14px;
        color: #fff;
      }
      th {
        padding: 3px 0;
        font-size: 14px;
        background-color: rgba(41, 147, 180, 0.1);
        font-weight: bold;
        color: #00c1ff;
      }
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
    .el-dialog__headerbtn {
      top: 28px;
      right: 30px;
    }
  }
}
</style>
