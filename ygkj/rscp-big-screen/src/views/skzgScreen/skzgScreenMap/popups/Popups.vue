<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-25 10:03:29
 * @Description: 弹窗
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\Popups.vue
-->
<template>
  <div class='popups_wrap'>
    <!-- 风险研判弹窗 -->
    <div ref="reservoirPopup">
      <ReservoirPopup :popupData="pointData" />
    </div>
    <!-- 安全鉴定弹窗 -->
    <div ref="aqjdPopup">
      <AqjdPopup :popupData="pointData" />
    </div>
    <!-- 除险加固弹窗 -->
    <div ref="cxjgPopup">
      <CxjgPopup :popupData="pointData" />
    </div>
    <!-- 限制运用弹窗 -->
    <div ref="cxzyxPopup">
      <CxzyxPopup :popupData="pointData" />
    </div>
    <!-- 问题处置弹窗 -->
    <div ref="zdyhwtPopup">
      <ZdyhwtPopup :popupData="pointData" />
    </div>

    <!-- 纳蓄弹窗 -->
    <div ref="nxypPopup">
      <NxypPopup :popupData="pointData" />
    </div>

    <!-- 防洪弹窗（泄洪） -->
    <div ref="floodControlPopup">
      <FloodControlPopup :popupData="pointData" />
    </div>

    <!-- 防洪弹窗（溢洪） -->
    <div ref="floodControlYihongPopup">
      <FloodControlYihongPopup :popupData="pointData" />
    </div>

    <!--拦洪座次 弹窗 -->
    <div ref="floodTimesPopup">
      <FloodTimesPopup :popupData="pointData" />
    </div>

    <!--供水弹窗 -->
    <div ref="supplyPopup">
      <SupplyPopup :popupData="pointData" />
    </div>

    <!--供水水量弹窗 -->
    <div ref="waterVolumePopup">
      <water-volume-popup :popupData="pointData" />
    </div>

    <!-- 灌溉弹窗 -->
    <div ref="irrigationPopup">
      <IrrigationPopup :popupData="pointData" />
    </div>

    <!-- 灌溉水量弹窗 -->
    <div ref="ggWaterPopup">
      <GgWaterPopup :popupData="pointData" />
    </div>

    <!-- 生态弹窗 -->
    <div ref="ecologyPopup">
      <EcologyPopup :popupData="pointData" />
    </div>

    <!-- 发电弹窗 -->
    <div ref="powerGePopup">
      <PowerGePopup :popupData="pointData" />
    </div>

    <!-- 当年发电量弹窗 -->
    <div ref="powerNowYearPopup">
      <PowerNowYearPopup :popupData="pointData" />
    </div>

    <!-- 蓄水总量 -->
    <div ref="XsQuantity">
      <TotalWaterStorage :popupData="pointData" />
    </div>

  </div>
</template>

<script>
import ReservoirPopup from './components/ReservoirPopup'
import AqjdPopup from './components/AqjdPopup'
import CxjgPopup from './components/CxjgPopup'
import CxzyxPopup from './components/CxzyxPopup'
import ZdyhwtPopup from './components/ZdyhwtPopup'
import NxypPopup from './components/NxypPopup'
import IrrigationPopup from './components/IrrigationPopup'
import PowerGePopup from './components/PowerGePopup'
import SupplyPopup from './components/SupplyPopup'
import EcologyPopup from './components/EcologyPopup'
import FloodControlPopup from './components/FloodControlPopup.vue'
import FloodControlYihongPopup from './components/FloodControlYihongPopup.vue'
import FloodTimesPopup from './components/FloodTimesPopup.vue'
import WaterVolumePopup from './components/WaterVolumePopup.vue'
import GgWaterPopup from './components/GgWaterPopup.vue'
import PowerNowYearPopup from './components/PowerNowYearPopup.vue'
import TotalWaterStorage from './components/TotalWaterStorage.vue'

export default {
  name: 'Popups',
  props: {
    pointData: {
      type: Object,
      required: true
    }
  },
  components: {
    ReservoirPopup,
    AqjdPopup,
    CxjgPopup,
    CxzyxPopup,
    ZdyhwtPopup,
    NxypPopup,
    IrrigationPopup,
    PowerGePopup,
    SupplyPopup,
    EcologyPopup,
    FloodControlPopup,
    FloodControlYihongPopup,
    FloodTimesPopup,
    WaterVolumePopup,
    GgWaterPopup,
    TotalWaterStorage,
    PowerNowYearPopup
  },
  data() {
    return {

    };
  },

  mounted() {
  },
  methods: {

  }
}
</script>

<style lang='scss' scoped>
.popups_wrap {
  .reservoirPopup {
    width: 300px;
    height: 300px;
    background: #fff;
  }
  .dialog__close {
    width: 20px;
    height: 20px;
    background-size: 100% 100%;
    cursor: pointer;
    background: url($zhImgUrl + "closeImg.png") no-repeat center !important;
    background-size: 100% !important;
  }
}
</style>