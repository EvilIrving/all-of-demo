<template>
  <div :class="['primary-dialog', leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog top="11vh" :visible.sync="visible" destroy-on-close :modal-append-to-body="true" :before-close="handleClose" :close-on-click-modal="false" class="tips-dialog">
      <div class="primary-dialog__title" slot="title" :style="minimizeStatus ? 'justify-content: space-between;' : ''">
        <p class="primary-dialog__title--label" style="color: #fff">
          {{ mainTitle }}
        </p>
      </div>
      <div class="primary-dialog__content">
        \
        <p class="first">请在地图中选择抢险地点！</p>
        <p class="second">选择抢险地点可以：</p>
        <p class="third">自动生成抢险结果及最佳路线</p>
        <!-- 底部按钮 -->
        <div slot="footer" class="dialog-footer">
          <span class="cancel-btn" @click="handleClose">取消</span>
          <span class="analysis-btn" @click="handleConfirm">选 点</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  name: 'PrimaryDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    width: {
      type: String,
      default: '600px'
    },
    mainTitle: {
      type: String,
      default: '选点提示'
    },
    leftSide: {
      type: Boolean,
      default: false
    },
    minimizeStatus: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      checkList: [],
      lookDetailsData: []
    };
  },
  components: {},
  mounted() { },
  methods: {
    ...mapActions(['SetSpotsFlag', 'SetIsAction', 'SetSpotsFlagType']),
    handleClose() {
      this.SetIsAction(false);
      this.$emit('closeDialog');
    },
    handleConfirm() {
      this.SetSpotsFlag(true);
      this.SetSpotsFlagType(0);
      this.$emit('closeDialog');
    }
  }
};
</script>

<style lang="scss" scoped>
.primary-dialog {
  ::v-deep .el-dialog {
    background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
    background-size: 100% 100%;
    background-color: rgba($color: #031a2b, $alpha: 0.9);
    width: 504px;
    .el-dialog__header {
      background-size: 18px 2px, 2px 18px;
      padding: 14px 14px 2px;
      height: 54px;
      .primary-dialog__title {
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          font-size: 16px;
          color: #00c1ff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          margin-left: 16px;
          padding-left: 26px;
          font-weight: bold;
          line-height: 32px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        top: 24px;
        .el-dialog__close {
          color: #00c1ff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      // background: linear-gradient(to bottom, #5398f6, #35b9e3) left bottom
      //     no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
      //   linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
      //   linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      background-size: 18px 2px, 2px 18px;
      // border-bottom: 0.0625rem solid #00c1ff;
      padding: 2px;
      padding-top: 0;
      .primary-dialog__content {
        height: 266px;
        border-top: 0;
        #floodEchart {
          width: 100%;
          height: 500px;
        }
        .first {
          font-family: PingFangSC-Semibold;
          font-size: 18px;
          color: #ffffff;
          line-height: 16px;
          font-weight: 600;
          padding-left: 25px;
        }
        .second,
        .third {
          font-family: PingFangSC-Regular;
          font-size: 16px;
          color: #ffffff;
          line-height: 16px;
          font-weight: 400;
          padding-left: 25px;
          margin-top: 25px;
        }
      }
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        position: absolute;
        right: 18px;
        bottom: 24px;
        > span {
          display: inline-block;
          width: 67px;
          height: 35px;
          line-height: 32px;
          text-align: center;
          border-radius: 4px;
          margin-left: 16px;
          cursor: pointer;
          color: #fff;
          background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          &.cancel-btn {
            border: 1px solid #00baff;
            color: #00baff;
            background: transparent;
          }
        }
      }
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}

.dialog-footer {
  position: absolute;
  right: 18px;
  bottom: 24px;
  > span {
    display: inline-block;
    width: 67px;
    height: 35px;
    line-height: 32px;
    text-align: center;
    border-radius: 4px;
    margin-left: 16px;
    cursor: pointer;
    color: #fff;
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    &.cancel-btn {
      border: 1px solid #00baff;
      color: #00baff;
      background: transparent;
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