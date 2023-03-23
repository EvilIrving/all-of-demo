<template>
  <div class="detail-table flood-warn-message">
    <el-dialog
      top="15vh"
      width="619px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog flood-warn-message-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">预警消息</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="top">
          <p class="comTit"><i class="icon-send-dian"></i>山洪预警信息</p>
          <i class="icon-send-line" style="display: block; width: 579px; margin: 10px 0"></i>
          <!-- <p class="t-title">{{ teamInfo.name }}</p> -->
          <div class="box">
            <div class="box-first warp">
              <p class="comTit little">
                <i class="icon-send-man"></i>&nbsp;&nbsp;接收人：{{ pointData.data.chargerName }}
              </p>
            </div>
          </div>
          <div class="box-five">
            <span class="label-name">发送内容：</span>
            <div class="input content">
              <span class="address">{{ pointData && pointData.data.nvi }}村庄</span>，截止目前累计<span class="address"
                >24小时</span
              >降雨超过{{ monitorWarnValue.rainfall }}毫米，已经达到<span class="address">{{
                monitorWarnValue.warnGradeId == 4 ? '准备转移' : '立即转移'
              }}</span
              >的标准。
            </div>
          </div>
        </div>
      </div>
      <div class="dialog-footer">
        <span class="cancel-btn" @click="handleClose">取消</span>
        <span class="analysis-btn" @click="singleSendWarnSms">发送</span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "FloodWarnMessage",
  data() {
    return {
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    pointData: {
      type: Object,
    }
  },
  components: {},
  created() { },
  mounted() {
  },
  computed: {
    monitorWarnValue() {
      return this.pointData.data.monitorWarnValue;
    },
  },
  methods: {
    handleClose() {
      this.$emit("closeDialog");
    },
    async singleSendWarnSms() {
      let opt = {
        warnDetail: this.pointData.data,
        warnType: 2
      }
      let res = await disasterPreventionApi.singleSendWarnSms(opt);
      if (res.code == 0) {
        this.$message({
          type: "success",
          message: '预警成功'
        })
      } else {
        this.$message({
          type: "error",
          message: res.message
        })
      }
    }
  },
};
</script>
<style lang="scss" scoped>
.flood-warn-message-dialog.primary-dialog {
  .top {
    margin-bottom: 25px;
  }
  p.comTit {
    font-family: PingFangSC-Semibold;
    font-size: 1.125rem;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 2rem;
    font-weight: 600;
    i {
      vertical-align: middle;
      margin-top: -2px;
    }
  }
  p.little {
    font-family: PingFangSC-Semibold;
    font-size: 0.875rem;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 1.5rem;
    font-weight: 400;
    i {
      vertical-align: middle;
      margin-top: -2px;
    }
  }
  .module-title {
    color: #1ef8f2;
    padding-left: 4px;
    line-height: 40px;
  }
  .divide {
    width: 100%;
    height: 1px;
    background: rgba(0, 193, 255, 0.2);
  }
  .label-name {
    margin-left: 5px;
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 24px;
    font-weight: 400;
    vertical-align: top;
  }
  .input {
    display: inline-block;
    padding: 4px;
    margin-right: 25px;
    border: 1px solid #ffffff;
    border-radius: 4px;
    font-family: PingFang-SC-Regular;
    font-size: 14px;
    color: #ffffff;
    line-height: 20px;
    font-weight: 400;
    width: 162px;
    .address {
      color: #009aff;
    }
  }
  .content {
    width: 419px;
    height: 100px;
  }
  .title {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 32px;
    font-weight: 600;
  }
  .t-title {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #33ffff;
    letter-spacing: 0;
    line-height: 24px;
    font-weight: 600;
    margin: 10px 0;
  }
  .box {
    display: flex;
    margin-bottom: 12px;
    .warp {
      flex: 1;
    }
    .address {
      font-family: PingFang-SC-Regular;
      font-size: 14px;
      color: #ffffff;
      line-height: 20px;
      font-weight: 400;
    }
    .box-seven {
      display: inline-block;
    }
  }
  .box-four {
    margin: 20px 0;
  }
  .add-btn {
    float: right;
    height: 34px;
    line-height: 34px;
    width: 105px;
    text-align: center;
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    color: #ffffff;
  }
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
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
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
  ::v-deep.el-input-number {
    width: 5.25rem;
  }
  ::v-deep.el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  ::v-deep.el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  ::v-deep.el-select .el-input .el-select__caret {
    color: #fff;
  }
  ::v-deep input::-webkit-input-placeholder {
    color: #fff;
  }
  //   .el-table th {
  //     color: #1ef8f2;
  //   }
  //   .el-table thead tr {
  //     background-image: linear-gradient(
  //       90deg,
  //       rgba(44, 158, 191, 0.3) 3%,
  //       rgba(41, 147, 180, 0.1) 100%
  //     );
  //   }
  //   .el-table__row.el-table__row--striped {
  //     background-image: linear-gradient(
  //       90deg,
  //       rgba(255, 255, 255, 0.2) 0%,
  //       rgba(249, 249, 249, 0) 100%
  //     );
  //   }
  //   ::v-deep .el-input-number .el-input {
  //     width: 40px;
  //   }
  ::v-deep .el-input-number .el-input__inner {
    padding-left: 16px;
    padding-right: 0;
    background: none;
    text-align: left;
  }
  ::v-deep.el-input-number {
    position: relative;
    display: inline-block;
    width: 66px;
    border: 1px solid #ffffff;
    border-radius: 4px;
  }
  ::v-deep.el-input-number__decrease {
    border-left: 1px solid #fff;
    background: none;
    > i {
      color: #fff;
    }
  }
  ::v-deep.el-input-number__increase {
    height: 1.05rem;
    border-left: 1px solid #fff;
    background: none;
    > i {
      color: #fff;
    }
  }
  ::v-deep.el-input-number--small .el-input-number__decrease,
  ::v-deep.el-input-number--small .el-input-number__increase {
    width: 20px;
  }
}
</style>