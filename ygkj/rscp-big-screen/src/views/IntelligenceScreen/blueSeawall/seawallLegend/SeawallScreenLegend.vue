<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-21 21:29:40
 * @Description: 图例
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\blueSeawall\seawallLegend\SeawallScreenLegend.vue
-->
<template>
  <div class='legend_wrap'>
    <!-- 风险研判 -->
    <p class="legend_title">
      <i class="lended_icon"></i>
      <span>图例</span>
      <!-- <i @click="showLegend(false)" class="open"></i> -->
      <!-- <i v-show="!skzgSwitchLegend" @click="showLegend(true)" class="close"></i> -->
    </p>
    <!-- 风险研判 -->
    <risk-research ref="childProp" v-if="sgzgMapType === '海塘概况' " />
    <!-- 风险研判 -->
    <second-line ref="childProp" v-if="sgzgMapType === '风险研判'" />
    <!-- 闭环管控 -->
    <first-line ref="childProp" v-if="sgzgMapType === '闭环管控'" />
    <!-- 安全鉴定 -->
    <security ref="childProp" v-if="sgzgMapType === '安全鉴定'" />

    <construction ref="childProp" v-if="sgzgMapType === '工程施工'" />

    <div class="list_btn" @click="openDialog"></div>
  </div>
</template>

<script>
import RiskResearch from './components/RiskResearch.vue'
import FirstLine from './components/FirstLine.vue'
import SecondLine from './components/SecondLine.vue'
import Security from './components/Security.vue'
import Construction from './components/Construction.vue'
import { fileDownload } from '@/api/api_seawall'
export default {
  name: 'SeawallScreenLegend',
  props: {
    sgzgMapType: {
      type: String,
      require: true,
      default: 'first'
    }
  },
  components: {
    RiskResearch,
    FirstLine,
    SecondLine,
    Security,
    Construction,
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd
    },
  },
  watch: {

  },
  data() {
    return {
      notShowKey: [],//不显示 一键生成
      skzgSwitchLegend: false
    };
  },
  mounted() {
  },
  methods: {
    showLegend(flag) {
      this.skzgSwitchLegend = flag
    },
    async openDialog() {
      this.$parent.openGernaretion(this.sgzgMapType)
    },
  }
}
</script>

<style lang='scss' scoped>
.legend_wrap {
  position: absolute;
  bottom: 80px;
  right: 540px;
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
    color: #000;
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
    background-color: #000;
    margin-right: 5px;
  }
  img {
    width: 15px;
    height: 15px;
    vertical-align: bottom;
    margin-right: 5px;
  }
  span {
    font-size: 16px;
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
.legend_wrap
  .el-checkbox.one
  .el-checkbox__input.is-checked
  .el-checkbox__inner {
  background-color: #0dac00;
  border-color: #0dac00;
}
.legend_wrap .el-checkbox.four .el-checkbox__inner::after,.legend_wrap .el-checkbox.five .el-checkbox__inner::after  {
  border: 0.005208rem solid #0c9800;
  border-left: 0;
  border-top: 0;
}
.legend_wrap
  .el-checkbox.two
  .el-checkbox__input.is-checked
  .el-checkbox__inner {
  background-color: #17da07;
  border-color: #17da07;
}
.legend_wrap
  .el-checkbox.three
  .el-checkbox__input.is-checked
  .el-checkbox__inner {
  background-color: #5ae34f;
  border-color: #5ae34f;
}
.legend_wrap
  .el-checkbox.four
  .el-checkbox__input.is-checked
  .el-checkbox__inner {
  background-color: #a8eba2;
  border-color: #a8eba2;
}
.legend_wrap
  .el-checkbox.five
  .el-checkbox__input.is-checked
  .el-checkbox__inner {
  background-color: #d0f2ce;
  border-color: #d0f2ce;
}
.legend_wrap .el-checkbox__input.is-checked + .el-checkbox__label {
  color: #fff;
}
</style>