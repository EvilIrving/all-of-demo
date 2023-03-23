<template>
  <div class="typhoon-search" ref="typhoonSearchSide">
    <AnalysisTypoon @selectTyphoon="onSelectTyphoon" @rowClick="onRowClick" />
  </div>
</template>

<script>
import { mapGetters,mapActions } from "vuex";
import { getTyphoonDetail } from "@/api/typhoon.js";
import AnalysisTypoon from "./AnalysisTyphoon/index.vue";

export default {
  name: "typhoon-search",
  data() {
    return {
      currentTypoon: {},
    };
  },
  components: {
    AnalysisTypoon
  },
  provide() {
    return {
      typhoonCard: this,
    };
  },
  mounted() {
    
  },
  computed: {
    ...mapGetters([
      'MapComponent',
    ])
  },
  methods: {
    onSelectTyphoon({ newChecked, unChecked }) {
      unChecked.forEach((item) => {
        this.MapComponent.clearTyphoon(item.code);
      });
      newChecked.some((item, index) => {
        if (index > 1) return false;
        this.getTyphoonDetail(item.code);
      });
    },
    getTyphoonDetail(code) {
      getTyphoonDetail({ code }).then((res) => {
        if (res.code == 0) {
          let points = eval(res.data.points);
          // this.$refs.currentTyphoon.initTyphoon(res.data);
          const data = {
            ...res.data,
            points,
          };
          this.MapComponent.showTyphoon(data);
          // this.$emit("showTyphoon", data);
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.typhoon-search {
  width: 100%;
  height: 100%;
  color: #fff;
}
</style>