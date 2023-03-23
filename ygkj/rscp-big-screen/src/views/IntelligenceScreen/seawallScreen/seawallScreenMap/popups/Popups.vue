<!--
 * @Author: hanyu
 * @LastEditTime: 2023-01-05 13:59:26
 * @Description: 弹窗
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenMap\popups\Popups.vue
-->
<template>
  <div class='popups_wrap'>
    <!-- 风险研判弹窗 -->
    <div ref="seawallPopup">
      <seawall-popup :popupData="pointData" />
    </div>
    <!-- 一线海塘弹窗 -->
    <div ref="firstLinePopup">
      <!-- <first-line-popup :popupData="pointData" /> -->
      <seawall-popup :popupData="pointData" />
    </div>
    <!-- 二道防线弹窗 -->
    <div ref="secondLinePopup">
      <seawall-popup :popupData="pointData" />
      <!-- <second-line-popup :popupData="pointData" /> -->
    </div>
    <!-- 安全鉴定弹窗 -->
    <div ref="aqjdPopup">
      <aqjd-popup :popupData="pointData" />
    </div>
    <!-- 除险加固弹窗 -->
    <div ref="cxjgPopup">
      <cxjg-popup :popupData="pointData" />
    </div>

    <!-- 问题处置弹窗 -->
    <div ref="zdyhwtPopup">
      <ZdyhwtPopup :popupData="pointData" />
    </div>

    <!-- 附属建筑物 -->
    <div ref="affiliatePopup">
      <affiliate-popup :popupData="pointData" />
    </div>
    <!-- 口门管控 -->
    <div ref="gateControlPopup">
      <gate-control-popup :popupData="pointData" />
    </div>
  </div>
</template>

<script>
import SeawallPopup from './components/SeawallPopup'
import FirstLinePopup from './components/FirstLinePopup'
import SecondLinePopup from './components/SecondLinePopup'
import ZdyhwtPopup from './components/ZdyhwtPopup'
import AqjdPopup from './components/AqjdPopup.vue'
import CxjgPopup from './components/CxjgPopup.vue'
import AffiliatePopup from './components/AffiliatePopup.vue'
import GateControlPopup from './components/GateControlPopup.vue'

export default {
  name: 'Popups',
  props: {
    pointData: {
      type: Object,
      required: true
    }
  },
  components: {
    SeawallPopup,
    FirstLinePopup,
    SecondLinePopup,
    AqjdPopup,
    CxjgPopup,
    ZdyhwtPopup,
    AffiliatePopup,
    GateControlPopup,
  },
  data() {
    return {

    };
  },

  mounted() {
    console.log(this.pointData, 'pointData');
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
}
</style>