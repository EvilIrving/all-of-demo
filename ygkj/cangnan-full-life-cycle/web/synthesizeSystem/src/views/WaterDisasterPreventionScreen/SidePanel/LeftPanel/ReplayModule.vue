<template>
  <div>
    <div class="collapse-panel__border" style="width: 490px">
      <ReplayModuleMain @clickRealtimeRainfull="clickRealtimeRainfull" />
    </div>
    <div class="collapse-panel__border" style="width: 490px; margin-left: 0.5rem" v-show="analyse == 'typhoon'">
      <three-typhoo-module v-if="typhoonShow" />
    </div>
  </div>
</template>
<script>
import ReplayModuleMain from "./components/ReplayModuleMain.vue";
import TyphoonModule from "../TyphoonModule.vue";
import ThreeTyphooModule from "../ThreeTyphooModule.vue";

export default {
  name: "ReplayModule",
  components: { ReplayModuleMain, TyphoonModule, ThreeTyphooModule },
  data() {
    return {
      typhoonShow: true,
      analyse: ''
    };
  },
  watch: {
    "$store.state.analyseData": function(val) {
      this.analyse = val;
    }
  },
  methods:{
    clickRealtimeRainfull(){
      this.$emit("clickRealtimeRainfull");
    }
  }
};
</script>
<style lang="scss" scoped></style>
