<template>
  <screen-silde-panel
    v-model="sildeCollapse"
    :class="[isBigScrren && 'is-big-screen']"
    position="right"
    top="8%"
    :height="height"
    @change="handleRightPanelchange"
  >
    <template #slot-panel>
      <TwoFloodModuleLeftPart
        style="margin-right: 0.5rem"
        v-if="currentScene == '防御复盘'"
        @legend-change="handleLegendChange"
        @open-analyse-dialog="openDetailsDialog"
      />
      <TwoFloodModuleRightPart
        v-if="currentScene == '防御复盘'"
        @legend-change="handleLegendChange"
        @open-analyse-dialog="openDetailsDialog"
      />
      <DefenseModule
        v-if="currentScene == '防御态势'"
        @legend-change="handleLegendChange"
        @open-analyse-dialog="openDetailsDialog"
      />
      <FloodModule
        v-else-if="currentScene == '防洪调度'"
        @legend-change="handleLegendChange"
        @FloodForecasting="FloodForecasting"
        @open-analyse-dialog="openDetailsDialog"
      />
    </template>
  </screen-silde-panel>
</template>
<script>
import { mapActions, mapGetters } from "vuex";
import { ScreenSildePanel } from "@/components";
import DefenseModule from "./DefenseModule.vue";
import FloodModule from "./FloodModule.vue";
// 第二场景左右屏
import TwoFloodModuleLeftPart from "./components/TwoFloodModuleLeftPart.vue";
import TwoFloodModuleRightPart from "./components/TwoFloodModuleRightPart.vue";
export default {
  name: "RightSildePanel",
  props: {
    height: { type: [Number, String] },
  },
  data() {
    return {
      sildeCollapse: true,
    };
  },
  components: {
    TwoFloodModuleLeftPart,
    TwoFloodModuleRightPart,
    ScreenSildePanel,
    DefenseModule,
    FloodModule,
  },
  watch: {
    collapseRight: {
      immediate: true,
      handler(val) {
        this.sildeCollapse = val;
      },
    },
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "currentScene",
      "collapseRight",
    ]),
    isBigScrren() {
      return this.$route.query.large == "true";
    },
  },
  methods: {
    ...mapActions(["setCollapseRight"]),
    handleRightPanelchange(status) {
      this.setCollapseRight(status);
    },
    /**
     * 洪水预备
     */
    FloodForecasting() {
      this.$emit("FloodForecasting");
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val, data) {
      this.$emit("open-analyse-dialog", val, data);
    },
    handleLegendChange(name, legend) {
      this.$emit("legend-change", name || "", legend || {});
    },
  },
};
</script>
