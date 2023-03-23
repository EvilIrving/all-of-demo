<!--
 * @Author: hanyu
 * @LastEditTime: 2022-09-19 09:58:41
 * @Description: 图例
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenLegend\SkzgScreenLegend.vue
-->
<template>
  <div class='legend_wrap'>
    <!-- 风险研判 -->
    <p class="legend_title">
      <span>图示</span>
      <i v-show="skzgSwitchLegend" @click="showLegend(false)" class="open"></i>
      <i v-show="!skzgSwitchLegend" @click="showLegend(true)" class="close"></i>
    </p>
    <div class="lineline"></div>
    <!-- 风险研判 -->
    <legend-scale ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '风险研判' " />
    <safe-appraisal ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '安全鉴定'" />
    <reinforce ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '除险加固'" />
    <clearing ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '存量清零'" />
    <over-run ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '限制运用'" />
    <dam-type-two ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '二类坝'" />
    <dam-type-three ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '三类坝'" />
    <key-problem ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '问题处置'" />
    <!-- 纳蓄 -->
    <nxyp-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '纳蓄'" />
    <xs-quantity-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '蓄水量'" />
    <xs-rate-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '蓄水率'" />
    <!-- 防洪 -->
    <flood-control ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '防洪'" />
    <flood-times ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '拦洪座次'" />
    <!-- 灌溉 -->
    <irrigation-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '灌溉'" />
    <gg-area ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '灌溉面积'" />
    <gg-water ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '灌溉水量'" />
    <!-- 供水 -->
    <water-supply ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '供水'" />
    <water-volume ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '供水水量'" />
    <water-availability-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '可供水量'" />
    <!-- 生态 -->
    <ecology-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '生态'" />
    <scenic-spot ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '风景区'" />
    <!-- 发电 -->
    <power-ge-scale ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '发电' || sgzgMapType === '装机容量'" />
    <power-now-year ref="childProp" v-show="skzgSwitchLegend" v-if="sgzgMapType === '当年发电量'" />

    <div v-show="skzgSwitchLegend && !notShowKey.includes(sgzgMapType)" class="list_btn" @click="openDialog"></div>
  </div>
</template>

<script>
import LegendScale from './components/LegendScale'
import KeyProblem from './components/KeyProblem'
import OverRun from './components/OverRun'
import Reinforce from './components/Reinforce'
import Clearing from './components/Clearing'
import WaterSupply from './components/WaterSupply'
import NxypScale from './components/NxypScale'
import XsQuantityScale from './components/XsQuantityScale'
import XsRateScale from './components/XsRateScale'
import GgArea from './components/GgArea'
import GgWater from './components/GgWater'
import EcologyScale from './components/EcologyScale'
import IrrigationScale from './components/IrrigationScale'
import PowerGeScale from './components/PowerGeScale'
import FloodControl from './components/FloodControl.vue'
import SafeAppraisal from './components/SafeAppraisal'
import DamTypeThree from './components/DamTypeThree.vue'
import DamTypeTwo from './components/DamTypeTwo'
import FloodTimes from './components/FloodTimes.vue'
import ScenicSpot from './components/ScenicSpot.vue'
import WaterAvailabilityScale from './components/WaterAvailabilityScale.vue'
import WaterVolume from './components/WaterVolume.vue'
import PowerNowYear from './components/PowerNowYear.vue'
import { clickLog } from "@/api/api_reservoir";
export default {
  name: 'SkzgScreenLegend',
  props: {

  },
  components: {
    LegendScale,
    FloodControl,
    KeyProblem,
    OverRun,
    Reinforce,
    Clearing,
    WaterSupply,
    NxypScale,
    XsQuantityScale, XsRateScale,
    EcologyScale,
    IrrigationScale,
    PowerGeScale,
    SafeAppraisal,
    DamTypeThree,
    DamTypeTwo,
    GgArea,
    GgWater,
    FloodTimes,
    ScenicSpot,
    WaterAvailabilityScale,
    WaterVolume,
    PowerNowYear
  },
  computed: {
    sgzgMapType() {
      return this.$store.state.sgzgMapType;
    },
    skzgSwitchLegend() {
      return this.$store.state.skzgSwitchLegend;
    },
    numberSelected() {
      return this.$store.state.numberSelected;
    },
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd
    },
  },
  watch: {
    sgzgSelectAdcd(val) {
      //省级取消供水水量交互
      if (this.sgzgMapType == '供水水量') {
        if ((val.indexOf('0000000000') != -1 && val.indexOf(',') == -1) || val == '') {
          // 恢复选中默认状态
          this.$parent.$refs.skzgLeft.chooseMapType('风险研判')
        }
      }
    }
  },
  data() {
    return {
      // notShowKey: ['拦洪座次', '风景区', '供水水量', '灌溉水量', '当年发电量'],//不显示 一键生成
      notShowKey: [],//不显示 一键生成
    };
  },
  mounted() {
  },
  methods: {
    openDialog() {
      clickLog({
        title: "智慧水库-" + this.sgzgMapType + "-一键生成"
      });
      let hasSingleRadio = ['安全鉴定']
      if (hasSingleRadio.includes(this.sgzgMapType)) {
        this.$parent.open(this.$refs.childProp.checkedScale, this.sgzgMapType)
        return;
      }
      if (this.sgzgMapType === '限制运用') {
        this.$parent.open(this.$refs.childProp.checkedScale, this.sgzgMapType, this.$refs.childProp.checkedStatus, this.$refs.childProp.checkedScale2)
        return;
      }
      this.$parent.open(this.$refs.childProp.checkedScale, this.sgzgMapType, this.$refs.childProp.checkedStatus)

    },
    showLegend(flag) {
      this.$store.commit('SET_SKZG_SWITCH_LEGEND', flag)
    }
  }
}
</script>

<style lang='scss' scoped>
.legend_wrap {
  color: #fff;
  background: center url($imageUrl + "legend_bg.png") no-repeat;
  background-size: 100% 100%;
  z-index: 2;
  .lineline {
    width: 100%;
    height: 2px;
    margin: 5px 0px;
    background: center/80% url($imageUrl + "lineline.png") no-repeat;
    // background-size: 100% 100%;
  }
  .open {
    position: absolute;
    top: 50%;
    right: 10%;
    width: 16px;
    height: 14px;
    line-height: 53px;
    background: center url($imageUrl + "open.png") no-repeat;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .close {
    position: absolute;
    top: 40%;
    right: 10%;
    width: 16px;
    height: 14px;
    line-height: 53px;
    background: center url($imageUrl + "close.png") no-repeat;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .legend_title {
    // width: 155px;
    height: 45px;
    line-height: 53px;
    font-size: 22px;
    color: #fff;
    margin-bottom: 4px;
    text-align: center;
    letter-spacing: 10px;
    background-size: 100% 100%;
    position: relative;
  }
  .list_btn {
    width: 80%;
    height: 57px;
    font-size: 22px;
    text-align: center;
    line-height: 57px;
    margin: 0 auto;
    background: center/100% url($imageUrl + "oneKey.png") no-repeat;
    letter-spacing: 3px;
    cursor: pointer;
  }
}
</style>

<style lang="scss">
.legend_label_radio {
  i {
    display: inline-block;
    padding-left: 5px;
    color: #69e3ee;
  }
  img {
    width: 12px;
    height: 12px;
    vertical-align: bottom;
    margin-right: 5px;
  }
}
.legend_label {
  height: 30px;
  @include flexbox();
  @include flexAI(center);
  img {
    width: 15px;
    height: 15px;
    vertical-align: bottom;
    margin-right: 5px;
  }
  span {
    font-size: 12px;
    line-height: 15px;
  }
  i {
    display: inline-block;
    padding-left: 5px;
    color: #69e3ee;
  }
}
.legend_wrap .el-checkbox {
  @include flexbox();
  @include flexAI(center);
}
.legend_wrap .el-checkbox__label {
  color: #fff;
}
.legend_wrap .el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #55bdb6;
  border-color: #55bdb6;
}
.legend_wrap .el-checkbox__input.is-checked + .el-checkbox__label {
  color: #fff;
}
</style>