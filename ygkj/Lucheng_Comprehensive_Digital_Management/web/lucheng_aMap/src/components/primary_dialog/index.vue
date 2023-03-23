<template>
  <div :class="[leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog
      :visible.sync="visible"
      :width="width"
      :before-close="handleClose"
      :modal="modal"
      :append-to-body="appendToBody"
      :fullscreen="fullscreen"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label">
          {{ mainTitle }}
          <i
            class="peripheral-analysis"
            @click="peripheralAnalysis"
            v-if="hasAnalysis"
          ></i>
        </p>
        <i
          class="minimize el-icon-minus"
          @click="minimize"
          v-if="minimizeStatus"
        ></i>
        <i
          v-if="fullscreenFlag"
          @click="changeFullscreen"
          :class="['el-icon-full-screen', 'full-screen-icon']"
        ></i>
      </div>
      <div class="primary-dialog__content">
        <slot></slot>
      </div>
      <div slot="footer" class="dialog-footer">
        <slot name="footer"></slot>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PrimaryDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    appendToBody: {
      type: Boolean,
      default: false,
    },
    width: {
      type: String,
      default: "900px",
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
      default: false
    },
    leftSide: {
      type: Boolean,
      default: false,
    },
    minimizeStatus: {
      type: Boolean,
      default: false,
    },
    hasAnalysis: { type: Boolean, default: true },
    modal: {
      type: Boolean,
      default: false,
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
      this.$emit("handleClose");
    },
    peripheralAnalysis() {
      this.$emit("analysis");
    },
    minimize() {
      this.$emit("minimize");
    },
  },
};
</script>

<style lang="scss">
.primary-dialog {
  .el-dialog {
    background-image: linear-gradient(
      180deg,
      rgba(8, 42, 53, 1) 2%,
      rgba(5, 14, 48, 1) 100%
    );
    overflow: hidden;
    @include flexbox;
    @include flexflow(column, nowrap);
    .el-dialog__header {
      height: 54px;
      padding: 18px;
      border: 1px solid #00c1ff;
      // box-shadow: inset 0 4px 30px 0 #2351ef40;
      background: transparent;
      border-bottom: 0;
      margin: 2px 2px 0;
      .primary-dialog__title {
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        position: relative;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
          font-size: 16px;
          color: #fff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          // margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
        .full-screen-icon {
          position: absolute;
          right: 24px;
          color: #00c1ff;
          cursor: pointer;
          top: 0px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #fff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      @include flex(1);
      min-height: 100px;
      padding: 0 18px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      border-bottom: 0;
      margin: 0 2px;
      .primary-dialog__content {
        height: 100%;
      }
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        .el-button {
          padding: 6px 16px;
          &.cancel {
            color: #1ef8f2;
            border: 1px solid #1ef8f2;
            background-color: transparent;
            border-radius: 4px;
          }
          &.confirm {
            color: #fff;
            border: 0;
            background-image: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            );
          }
        }
      }
    }
    .el-table {
      .cell {
        height: 37px;
        line-height: 37px;
      }
      .el-checkbox__inner {
        border: 0.0625rem solid #00c1ff;
        background-color: #00c1ff;
        // opacity: 0;
        background: rgba($color: #000000, $alpha: 0);
      }
      .el-table__body tr.current-row > td {
        background-color: rgba(41, 147, 180, 0.3);
      }
      .el-table__row.el-table__row--striped {
        background-image: linear-gradient(
          90deg,
          rgba(255, 255, 255, 0.2) 0%,
          rgba(249, 249, 249, 0) 100%
        );
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
        font-weight: bold;
        color: #56fefe !important;
      }
      thead {
        background-image: linear-gradient(
          90deg,
          rgba(44, 158, 191, 0.3) 3%,
          rgba(41, 147, 180, 0.1) 100%
        ) !important;
      }
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
          no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
        linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  background: url(~assets/images/peripheral_analysis.png) no-repeat;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
</style>
