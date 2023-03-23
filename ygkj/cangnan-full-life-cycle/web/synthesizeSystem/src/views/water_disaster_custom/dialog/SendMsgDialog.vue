<template>
  <div class="detail-table emergency-scheme">
    <el-dialog
      top="10vh"
      width="619px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog emergency-scheme-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">立即发送</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="top">
          <p class="comTit"><i class="icon-send-dian"></i>抢险队伍信息</p>
          <i
            class="icon-send-line"
            style="display: block; width: 579px; margin: 10px 0"
          ></i>
          <p class="t-title">{{ teamInfo.name }}</p>
          <div class="box">
            <div class="box-first warp">
              <p class="comTit little">
                <i class="icon-send-man"></i>&nbsp;&nbsp;人数：{{
                  teamInfo.existingNumber
                }}
              </p>
            </div>
            <div class="box-second warp">
              <p class="comTit little">
                <i class="icon-send-tupian"></i>&nbsp;&nbsp;行政区划：{{
                  teamInfo.areaName
                }}
              </p>
            </div>
            <div class="box-third warp">
              <p class="comTit little">
                <i class="icon-send-zuobian"></i>&nbsp;&nbsp;目标距离(m)：{{
                  teamInfo.distance
                }}
              </p>
            </div>
          </div>
          <div class="box-four">
            <span class="label-name">队伍负责人：</span>
            <div class="input">{{ teamInfo.chargePerson }}</div>
            <span class="label-name">联系方式：</span>
            <div class="input">{{ teamInfo.chargePhone }}</div>
          </div>
          <div class="box-five">
            <span class="label-name">发送内容：</span>
            <div class="input content">
              {{ teamInfo.name }}，请速安排人员到<span class="address">{{
                teamInfo.address
              }}</span
              >进行抢险支持
            </div>
          </div>
        </div>
        <div class="bom">
          <p class="comTit"><i class="icon-send-dian"></i>防汛仓库信息</p>
          <i
            class="icon-send-line"
            style="display: block; width: 579px; margin: 10px 0"
          ></i>
          <p class="t-title">{{ warehouseInfo.name }}</p>
          <div class="box">
            <div
              class="box-seven warp"
              v-for="(value, index) in goodsList"
              :key="index"
            >
              <p class="comTit little">
                <i class="icon-send-goods"></i>&nbsp;&nbsp;{{
                  goodsList[index].materialName
                }}：{{ goodsList[index].num }}
              </p>
            </div>
          </div>
          <div class="box-four">
            <span class="label-name">仓库负责人：</span>
            <div class="input">{{ warehouseInfo.chargePerson }}</div>
            <span class="label-name">联系方式：</span>
            <div class="input">{{ warehouseInfo.chargePhone }}</div>
          </div>
          <div class="box-five">
            <span class="label-name">发送内容：</span>
            <div class="input content">
              {{ warehouseInfo.name }},请速安排人员携带物资（<span
                v-for="(value, index) in goodsList"
                :key="index"
                >{{ goodsList[index].materialName }},{{
                  goodsList[index].num
                }};</span
              >），到<span class="address">{{ teamInfo.address }}</span
              >进行抢险支持
            </div>
          </div>
        </div>
      </div>
      <div class="dialog-footer">
        <span class="cancel-btn" @click="handleClose">取消</span>
        <span class="analysis-btn" @click="send">发送</span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "EmergencySchemeDialog",
  data() {
    return {
      teamInfo: {}, // 抢险支持-仓库调度-选中队伍
      warehouseInfo: {}, // 抢险支持-仓库调度-选中仓库
      input1: "",
      address1: "",
      goodsList: [],
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  created() {},
  mounted() {
    this.getTeamInfo();
    this.getWarehouseInfo();
    this.getGoodsList();
  },
  computed: {},
  methods: {
    getTeamInfo() {
      this.teamInfo = this.$store.state.custom.teamInfo;
    },
    getWarehouseInfo() {
      this.warehouseInfo = this.$store.state.custom.warehouseInfo;
    },
    getGoodsList() {
      this.goodsList = this.$store.state.custom.goodsList;
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    //智能分析
    async send() {
      let res = await disasterPreventionApi.wareHouseSendSms({
        phone: this.teamInfo.chargePhone,
        userName: this.teamInfo.chargePerson,
      });
      if (res.code == 0) {
        this.$message.success("发送成功！");
        this.handleClose();
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.emergency-scheme-dialog.primary-dialog {
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
        height: 600px;
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