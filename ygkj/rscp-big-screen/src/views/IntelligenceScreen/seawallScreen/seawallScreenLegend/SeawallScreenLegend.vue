<!--
 * @Author: hanyu
 * @LastEditTime: 2023-02-23 17:49:18
 * @Description: 图例
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenLegend\SeawallScreenLegend.vue
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
    <risk-research ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '风险研判' " />
    <!-- 防潮体系 -->
    <first-line ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '一线海塘'" />
    <second-line ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '非一线海塘'" />
    <!-- 风险管控 -->
    <safe-appraisal ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '安全鉴定'" />
    <reinforce ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '除险加固'" />
    <!-- 防潮分析 -->
    <moisture-analysis ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '防潮分析'" />
    <!-- 问题处置 -->
    <key-problem ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '问题处置'" />

    <!-- 附属建筑物 -->
    <affiliate ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '附属建筑物'" />
    <!-- 口门管控 -->
    <gate-control ref="childProp" :defultSelected="numberSelected" v-show="skzgSwitchLegend" v-if="sgzgMapType === '口门管控'" />
    <div v-show="skzgSwitchLegend && !notShowKey.includes(sgzgMapType)" class="list_btn" @click="openDialog"></div>
  </div>
</template>

<script>
import KeyProblem from './components/KeyProblem'
import Reinforce from './components/Reinforce'
import SafeAppraisal from './components/SafeAppraisal'
import RiskResearch from './components/RiskResearch.vue'
import FirstLine from './components/FirstLine.vue'
import SecondLine from './components/SecondLine.vue'
import MoistureAnalysis from './components/MoistureAnalysis.vue'
import { clickLog } from "@/api/api_reservoir";
import Affiliate from './components/Affiliate.vue'
import GateControl from './components/GateControl.vue'
export default {
  name: 'SeawallScreenLegend',
  props: {

  },
  components: {
    Reinforce,
    SafeAppraisal,
    RiskResearch,
    KeyProblem,
    FirstLine,
    SecondLine,
    MoistureAnalysis,
    Affiliate,
    GateControl
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
  watch: {},
  data() {
    return {
      notShowKey: ['附属建筑物',],//不显示 一键生成
    };
  },
  mounted() {
  },
  methods: {
    openDialog() {
      clickLog({
        title: `智慧海塘-${this.sgzgMapType}-一键生成`
      });
      let hasSingleRadio = ['安全鉴定']
      if (hasSingleRadio.includes(this.sgzgMapType)) {
        this.$parent.open(this.$refs.childProp.checkedScale, this.sgzgMapType)
        return;
      }
      if(this.sgzgMapType == '除险加固'){
        this.$parent.open(this.$refs.childProp.dam, this.sgzgMapType)
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
  .color_block {
    width: 12px;
    height: 2px;
    background-color: #ccc;
    margin-right: 5px;
  }
}
.legend_label {
  height: 30px;
  @include flexbox();
  @include flexAI(center);
  .color_block {
    width: 12px;
    height: 2px;
    background-color: #ccc;
    margin-right: 5px;
  }
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