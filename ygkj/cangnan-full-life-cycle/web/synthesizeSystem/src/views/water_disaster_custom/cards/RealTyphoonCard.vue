<!--
 * @Author: your name
 * @Date: 2021-07-01 14:16:35
 * @LastEditTime: 2021-07-12 09:33:37
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\RealTyphoonCard.vue
-->

<template>
  <div class="real-typhoon-card">
    <panel-item :inSimulation="previewModule" title="台风">
      <typhoon-module :typhoonList="typhoonList"></typhoon-module>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import TyphoonModule from "../../WaterDisasterPreventionScreen/SidePanel/TyphoonModule.vue";
export default {
  name: "TyphoonCard",
  components: {
    PanelItem,
    TyphoonModule,
  },
  props: ["typhoonList"],
  computed: {
    ...mapGetters(["previewModule"]),
  },
  mounted() {
    this.$nextTick(() => {});
  },
};
</script>
<style lang="scss" scoped>
.typhoon-card {
  height: 100%;
  // padding: 0 16px;
}
</style>
<style lang="scss">
.typhoon-card .small-module-sty .typhoon-details-sty .typhoon-singlet-line {
  width: calc(15.625rem - 60px);
}
.typhoon-card
  .small-module-sty
  .typhoon-details-sty
  .typhoon-information-sty
  > div
  > div
  > span {
  width: 60px;
}
</style>
