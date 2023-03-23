<template>
  <div :class="[leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog :top="top" :visible="visible" :width="width" :before-close="handleClose" :modal="modal" :append-to-body="appendToBody" full-screen :fullscreen="fullscreen" :close-on-click-modal="false" class="primary-dialog" :class="{ 'add-compoents': mainTitle !== '预演' &&  mainTitle !== '新增一键督查'}">
      <div class="primary-dialog__title" slot="title" :style="minimizeStatus ? 'justify-content: space-between;' : ''">
        <p class="primary-dialog__title--label">
          {{ mainTitle }}
          <!-- <i class="peripheral-analysis" @click="peripheralAnalysis" v-if="hasAnalysis"></i> -->
        </p>
        <!-- <i class="minimize el-icon-minus" @click="minimize" v-if="minimizeStatus"></i> -->
        <!-- <i v-if="fullscreenFlag" @click="changeFullscreen" :class="['el-icon-full-screen', 'full-screen-icon']"></i> -->
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
  name: 'PrimaryDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    appendToBody: {
      type: Boolean,
      default: false
    },
    width: {
      type: String,
      default: '900px'
    },
    mainTitle: {
      type: String,
      default: '测试'
    },
    fullscreen: {
      type: Boolean,
      default: false
    },
    fullscreenFlag: {
      type: Boolean,
      default: false
    },
    leftSide: {
      type: Boolean,
      default: false
    },
    minimizeStatus: {
      type: Boolean,
      default: false
    },
    hasAnalysis: { type: Boolean, default: true },
    modal: {
      type: Boolean,
      default: false
    },
    top: {
      type: String,
      default: '3vh'
    }
  },
  data() {
    return {};
  },
  components: {},
  methods: {
    changeFullscreen() {
      this.$emit('changeFullscreen');
    },
    handleClose() {
      this.$emit('update:visible', false);
      this.$emit('handleClose');
    },
    peripheralAnalysis() {
      this.$emit('analysis');
    },
    minimize() {
      this.$emit('minimize');
    }
  }
};
</script>

<style lang="scss">
.primary-dialog {
  .el-dialog {
    background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
    background-size: 100% 100%;
    background-color: rgba(3, 26, 43, 0.9);
    overflow: hidden;
    @include flexbox;
    @include flexflow(column, nowrap);
    .el-dialog__header {
      padding: 16px 16px 0;
      // border: 1px solid #00c1ff;
      // box-shadow: inset 0 4px 30px 0 #2351ef40;
      // background-image: linear-gradient(180deg, #1f375f 0%, #1b3047 100%);
      border-bottom: 0;
      margin: 0 2px 0;
      .primary-dialog__title {
        background: url(~assets/images/WaterDisastersImage/title-bg.png) no-repeat;
        background-size: 100% 100%;
        height: 42px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          font-size: 18px;
          color: #fff;
          height: 42px;
          width: 80%;
          padding-left: 40px;
          line-height: 42px;
        }
        .full-screen-icon {
          position: absolute;
          right: 24px;
          color: #00c1ff;
          cursor: pointer;
          top: 4px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #ffffff;
        }
      }
    }
    .el-dialog__body {
      @include flex(1);
      min-height: 100px;
      padding: 0 18px;
      // background-image: linear-gradient(180deg, #1c3357 0%, #0b2045 100%);
      // border: 1px solid #00c1ff;
      border-top: 0;
      border-bottom: 0;
      margin: 0 2px;
      .primary-dialog__content {
        height: 100%;
      }
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
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
      content: '';
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      // background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
      //   linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
    .el-dialog__headerbtn {
      top: 28px;
      right: 30px;
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.add-compoents {
  .el-dialog {
    background: url(~assets/images/WaterDisastersImage/dialog-bg.png) no-repeat;
    background-size: 100% 100%;
    .el-dialog__header {
      padding-top: 24px;
      .primary-dialog__title {
        background: url(~assets/images/WaterDisastersImage/dialog-title-bg.png) no-repeat;
        background-size: 100% 100%;
      }
    }
    .el-dialog__footer {
      .dialog-footer {
        padding-top: 20px;
      }
    }
    .el-dialog__headerbtn {
      top: 36px;
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
