<template>
  <screen-silde-panel
    v-model="sildeCollapse"
    :class="[isBigScrren && 'is-big-screen']"
    position="left"
    top="8%"
    :height="height"
    @change="handleLeftPanelchange"
  >
    <template #slot-panel>
      <DefenseModule
        v-if="currentScene == '防御态势'"
        @open-analyse-dialog="openDetailsDialog"
        @legend-change="legendChange"
      />
      <FloodModule
        v-if="currentScene == '防洪调度'"
        @open-analyse-dialog="openDetailsDialog"
        @legend-change="legendChange"
        :typhoonShowBool="typhoonShow"
        :typhoonData="typhoonData"
      />
      <ReplayModule @clickRealtimeRainfull="clickRealtimeRainfull" v-if="currentScene == '防御复盘'" />
    </template>
  </screen-silde-panel>
</template>
<script>
import { mapActions, mapGetters } from "vuex";
import { ScreenSildePanel } from "@/components";
import DefenseModule from "./DefenseModule.vue";
import FloodModule from "./FloodModule.vue";
import ReplayModule from "./ReplayModule.vue";
export default {
  name: "LeftSildePanel",
  props: {
    height: { type: [Number, String] },
    typhoonData: { type: Object },
    typhoonShow: { type: Boolean },
  },
  data() {
    return {
      sildeCollapse: true,
    };
  },
  watch: {
    collapseLeft: {
      immediate: true,
      handler(val) {
        this.sildeCollapse = val;
      },
    },
  },
  components: { DefenseModule, ScreenSildePanel, FloodModule, ReplayModule },
  computed: {
    ...mapGetters(["ScreenMap", "currentSelectArea", "currentScene", "collapseLeft"]),
    isBigScrren() {
      return this.$route.query.large == "true";
    },
  },

  methods: {
    ...mapActions(["setCollapseLeft"]),
    handleLeftPanelchange(status) {
      this.setCollapseLeft(status);
    },
    clickRealtimeRainfull(){
      this.$emit('clickRealtimeRainfull');
    },
    legendChange(name, legend) {
      this.$emit("legend-change", name || "", legend || {});
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.$emit("open-analyse-dialog", val);
    },
  },
};
</script>

<style lang="scss">
.icon-diamond {
  display: inline-block;
  height: 24px;
  width: 24px;
  background-image: url(~assets/images/WaterDisastersImage/diamond.gif);
}
.v-charts-data-empty {
  background-color: transparent;
}
.v-charts-component-loading {
  background-color: transparent;
}
</style>
