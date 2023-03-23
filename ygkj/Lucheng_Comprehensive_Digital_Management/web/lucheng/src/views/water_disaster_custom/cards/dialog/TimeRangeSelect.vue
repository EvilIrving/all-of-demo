<template>
  <div class="detail-table time-range-select">
    <el-dialog
      top="15vh"
      width="1000px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog time-range-select-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">分析</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <p>注：请滑动滑块选择时段。</p>
        <div class="slider-box">
          <el-slider :show-tooltip="false" :step="12.5" v-model="value" range :marks="marks"> </el-slider>
        </div>
      </div>
      <div class="dialog-footer">
        <span class="cancel-btn" @click="handleClose">取消</span>
        <span class="analysis-btn" @click="doConfirm">确定</span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "TimeRangeSelect",
  data() {
    return {
      value: [0, 100],
      marks: {
        0: {
          label: '前72小时',
          style: {
            'text-align': 'right'
          }
        },
        12.5: '前24小时',
        25: '前6小时',
        37.5: '前3小时',
        50: '前1小时',
        62.5: '近1小时(现在)',
        75: '未来1小时',
        87.5: '未来3小时',
        100: {
          label: '未来6小时',
          style: {
            width: '100px',
          }
        }
      }
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  created() { },
  mounted() {
  },
  computed: {

  },
  methods: {
    handleClose() {
      this.$emit("closeDialog");
    },
    doConfirm() {
      this.$emit("doConfirm", this.value, this.marks);
    }
  },
};
</script>
<style lang="scss" scoped>
.time-range-select-dialog.primary-dialog {
  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }
  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 300px;
        border-top: 0;
        padding: 0 24px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
        p {
          margin: 16px 0;
          color: #fff;
        }
        .slider-box {
          padding: 0 16px;
          .el-slider__bar {
            background: rgba(29, 247, 241, 0.5);
          }
          .el-slider__button {
            border: 2px solid rgba(29, 247, 241, 0.5);
          }
          .el-slider__stop {
            width: 12px;
            height: 12px;
            top: -50%;
          }
        }
      }
      .el-radio__label {
        visibility: hidden;
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
        border: 1px solid #1ef8f2;
        color: #1ef8f2;
        background: transparent;
      }
    }
  }
}
</style>