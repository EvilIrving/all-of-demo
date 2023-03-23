<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-18 09:26:42
 * @Description: 图例
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallLegend\SeawallScreenLegend.vue
-->
<template>
  <div class='legend_wrap'>
    <!-- 风险研判 -->
    <p class="legend_title">
      <i class="lended_icon"></i>
      <span>图例</span>
      <i @click="showLegend(false)" class="open"></i>
      <!-- <i v-show="!skzgSwitchLegend" @click="showLegend(true)" class="close"></i> -->
    </p>
    <!-- 风险研判 -->
    <risk-research ref="childProp" v-if="sgzgMapType === '监测分析' " />
    <!-- 监测分析 -->
    <second-line ref="childProp" v-if="sgzgMapType === '风险研判'" />
    <!-- 闭环管控 -->
    <first-line ref="childProp" v-if="sgzgMapType === '闭环管控'" />
    <!-- 安全鉴定 -->
    <security ref="childProp" v-if="sgzgMapType === '安全鉴定'" />

    <construction ref="childProp" v-if="sgzgMapType === '工程施工'" />

    <div  class="list_btn" @click="openDialog"></div>
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
      console.log('++++++++++');
      if (this.sgzgMapType === '风险研判' || this.sgzgMapType === '闭环管控') {
        // let res = await fileDownload();
        // if (res.success) {
        //   window.open('https://sk.zjwater.com' + res.rows[0].filurl)
        // }
        //  window.open(this.$config.zhImgUrl+ '1.海塘风险管控清单0914.xls',)
        this.$parent.openGernaretion(this.sgzgMapType)
      } else {
        
        this.$parent.openGernaretion(this.sgzgMapType)
      }
    },
  }
}
</script>

<style lang='scss' scoped>
.legend_wrap {
  position: absolute;
  bottom: 70px;
  right: 420px;
  color: #fff;
  background: center url($zhImgUrl + "mal_lenged.png") no-repeat;
  background-size: 100% 100%;
  z-index: 2;
  .lineline {
    width: 100%;
    height: 2px;
    margin: 5px 0px;
    // background: center/80% url($zhImgUrl + "lineline.png") no-repeat;
  }
  .open {
    position: absolute;
    top: 50%;
    right: 10%;
    width: 7px;
    height: 5px;
    line-height: 53px;
    background: center url($zhImgUrl + "open.png") no-repeat;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .close {
    position: absolute;
    top: 50%;
    right: 10%;
    width: 7px;
    height: 5px;
    line-height: 53px;
    background: center url($zhImgUrl + "close.png") no-repeat;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .lended_icon {
    position: absolute;
    top: 30%;
    left: 15%;
    width: 16px;
    height: 16px;
    line-height: 53px;
    background: center url($zhImgUrl + "map-lenged_icon.png") no-repeat;
    background-size: 100% 100%;
  }
  .legend_title {
    width: 155px;
    height: 40px;
    line-height: 40px;
    font-size: 16px;
    color: #000;
    margin-bottom: 4px;
    text-align: center;
    letter-spacing: 5px;
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
    background: center/100% url($zhImgUrl + "oneKey.png") no-repeat;
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
    color: #000;
  }
}
.legend_wrap .el-checkbox {
  @include flexbox();
  @include flexAI(center);
}
.legend_wrap .el-checkbox__label {
  color: #000;
}
.legend_wrap .el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #193cc5;
  border-color: #193cc5;
}
.legend_wrap .el-checkbox__input.is-checked + .el-checkbox__label {
  color: #000;
}
</style>