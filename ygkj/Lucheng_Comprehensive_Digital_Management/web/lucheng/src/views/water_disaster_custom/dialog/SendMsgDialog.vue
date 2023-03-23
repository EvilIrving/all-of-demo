<template>
  <div class="detail-table emergency-scheme">
    <el-dialog top="12vh" destroy-on-close :visible.sync="visible" :before-close="handleClose" append-to-body :modal-append-to-body="true" :close-on-click-modal="false" class="primary-dialog emergency-scheme-dialog">
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">立即发送</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="top">
          <p class="comTit"><i class="icon-send-dian"></i>抢险队伍信息</p>
          <i class="icon-send-line"></i>
          <p class="t-title">{{ teamInfo.name }}</p>
          <div class="box">
            <div class="box-first warp">
              <p class="comTit little"><i class="icon-send-man"></i>&nbsp;&nbsp;人数：{{ teamInfo.existingNumber }}</p>
            </div>
            <div class="box-second warp">
              <p class="comTit little"><i class="icon-send-tupian"></i>&nbsp;&nbsp;行政区划：{{ teamInfo.areaName }}</p>
            </div>
            <div class="box-third warp">
              <p class="comTit little">
                <i class="icon-send-zuobian"></i>&nbsp;&nbsp;目标距离(km)：{{ teamInfo.distance }}
              </p>
            </div>
            <div class="box-third warp">
              <p class="comTit little">
                <i class="icon-send-luxian"></i>&nbsp;&nbsp; 最佳路线：
                <span>{{ teamDispatchInfo.duration }}</span>
                <span class="info-divide">|</span>
                <span>{{ teamDispatchInfo.distance }}公里</span>
                <span class="info-divide">|</span>
                <span>{{ teamDispatchInfo.traffic_lights }}个红绿灯 </span>
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
            <!-- <div class="input content">
              {{ teamInfo.name }}，请速安排人员到<span class="address">{{ teamInfo.address }}</span
              >进行抢险支持
            </div> -->
            <el-input v-model="teamContent" size="mini" placeholder="请输入内容" clearable></el-input>
          </div>

        </div>
        <div class="bom">
          <p class="comTit"><i class="icon-send-dian"></i>防汛仓库信息</p>
          <i class="icon-send-line"></i>
          <p class="t-title">{{ warehouseInfo.name }}</p>
          <div class="box">
            <div class="box-seven warp" v-for="(value, index) in goodsList" :key="index">
              <p class="comTit little">
                <i class="icon-send-goods"></i>&nbsp;&nbsp;{{ goodsList[index].materialName }}：{{
                  goodsList[index].num
                }}
              </p>
            </div>
          </div>
          <div class="box-four" v-show="Object.keys(warehouseInfo).length > 0">
            <span class="label-name">仓库负责人：</span>
            <div class="input">{{ warehouseInfo.chargePerson }}</div>
            <span class="label-name">联系方式：</span>
            <div class="input">{{ warehouseInfo.chargePhone }}</div>
          </div>
          <div class="box-five">
            <span class="label-name">发送内容：</span>
            <!-- <div class="input content">
              {{ warehouseInfo.name }},请速安排人员携带物资（<span v-for="(value, index) in goodsList" :key="index"
                >{{ goodsList[index].materialName }},{{ goodsList[index].num }};</span
              >），到<span class="address">{{ teamInfo.address }}</span
              >进行抢险支持
            </div> -->.
            <el-input v-model="storeContent" size="mini" placeholder="请输入内容" clearable></el-input>
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
import axios from 'axios';
import { mapGetters } from 'vuex';
import { disasterPreventionApi } from '@/api/RightSildePanel';
export default {
  name: 'EmergencySchemeDialog',
  data() {
    return {
      teamInfo: {}, // 抢险支持-仓库调度-选中队伍
      warehouseInfo: {}, // 抢险支持-仓库调度-选中仓库
      input1: '',
      address1: '',
      teamDispatchInfo: {},
      goodsList: [],
      storeContent: '',
      teamContent: '',
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  components: {},
  created() { },
  mounted() {
    this.getTeamInfo();
    this.getWarehouseInfo();
    this.getGoodsList();
  },
  computed: {
    ...mapGetters(['TeamDispatchList']),
    sendContent() {
      return this.content + '请点击：';
    },
  },
  watch: {
    TeamDispatchList(val) {
      this.teamDispatchInfo = val && val.length > 0 ? val[0] : [];
    }
  },
  methods: {
    getTeamInfo() {
      this.teamInfo = this.$store.state.custom.teamInfo;
    },
    getWarehouseInfo() {
      this.warehouseInfo = this.$store.state.custom.warehouseInfo;
      console.log(Object.keys(this.warehouseInfo), 'this.warehouseInfothis.warehouseInfo')
    },
    getGoodsList() {
      this.goodsList = this.$store.state.custom.goodsList;
    },
    handleClose() {
      this.$emit('closeDialog');
    },
    //智能分析
    async send() {
      let res = await disasterPreventionApi.wareHouseSendSms({
        phone: this.teamInfo.chargePhone,
        userName: this.teamInfo.chargePerson,
        content: this.teamContent
      });
      let res2 = await disasterPreventionApi.wareHouseSendSms({
        phone: this.warehouseInfo.chargePhone,
        userName: this.warehouseInfo.chargePerson,
        content: this.storeContent
      });
      if (res.code == 0 && res2.code === 0) {
        this.$message.success('发送成功！');
        this.handleClose();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.el-input {
  width: 80%;
}
.emergency-scheme-dialog.primary-dialog {
  .top {
    margin-bottom: 25px;
  }
  p.comTit {
    font-family: PingFangSC-Semibold;
    font-size: 18px;
    color: #ffffff;
    letter-spacing: 0;
    line-height: 2rem;
    i.icon-send-dian {
      background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
      background-size: 100% 100%;
      width: 20px;
      height: 20px;
      vertical-align: middle;
      margin-top: -2px;
      margin-right: 8px;
    }
  }
  .icon-send-line {
    width: 100%;
    height: 4px;
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview-01.png) no-repeat;
    background-size: 100% 100%;
    display: block;
    margin: 10px 0;
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
      &.icon-send-man {
        background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
        background-size: 100% 100%;
        width: 20px;
        height: 20px;
      }
      &.icon-send-tupian {
        background: url(~assets/images/WaterDisastersImage/icon-send-tupian.png) no-repeat;
        background-size: 100% 100%;
        width: 20px;
        height: 20px;
      }
      &.icon-send-zuobian {
        background: url(~assets/images/WaterDisastersImage/icon-send-zuobian.png) no-repeat;
        background-size: 100% 100%;
        width: 20px;
        height: 20px;
      }
      &.icon-send-luxian {
        background: url(~assets/images/WaterDisastersImage/icon-send-luxian.png) no-repeat;
        background-size: 100% 100%;
        width: 20px;
        height: 20px;
      }
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
    width: calc(100% - 100px);
    margin-right: 0;
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
    font-size: 24px;
    background: linear-gradient(180deg, #5de6fb 0%, #02a4ed 100%);
    -webkit-background-clip: text;
    font-family: 'YouYaBiaoTiHei';
    letter-spacing: 0;
    line-height: 24px;
    margin: 16px 0 10px;
    color: transparent;
  }
  .box {
    display: flex;
    .warp {
      margin-right: 48px;
      .info-divide {
        margin: 0 10px;
      }
      &:last-child {
        margin-right: 0;
      }
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
    background: url(~assets/images/WaterDisastersImage/dialog-bg.png) no-repeat;
    background-size: 100% 100%;
    width: 1200px;
    .el-dialog__headerbtn {
      top: 39px;
      right: 30px;
      font-size: 18px;
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__header {
      padding-top: 28px;
      padding-bottom: 16px;
      .primary-dialog__title {
        background: url(~assets/images/WaterDisastersImage/dialog-title-bg.png) no-repeat;
        background-size: 100% 100%;
        height: 42px;
        padding: 0;
        .primary-dialog__title--label {
          width: 80%;
          height: 42px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          font-weight: bold;
          line-height: 42px;
          padding-left: 36px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      position: relative;
      .primary-dialog__content {
        height: 600px;
        border-top: 0;
        padding: 0 16px;
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
        border: 1px solid #00baff;
        color: #00baff;
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