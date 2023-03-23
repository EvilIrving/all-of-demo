<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-07-15 18:21:04
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="积水监控" class="stagnant-water-count">
    <div class="has-count">
      发生积水数量
      <span
        class="fs36 ml38 mr12 yellow"
        @click="drawPonit"
        style="cursor: pointer"
        >{{ this.warnNum }}</span
      >个
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "StagnantWaterCount",
  data() {
    return {
      warnNum: 1,
      pointList: [],
      drawFlag: false,
      timer:null
    };
  },
  components: { CountWrap },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  created() {},
  mounted() {
    this._waterLoggingStats();
    this._WaterLoggingList();
    this.timer = setInterval(()=>{
      this._waterLoggingStats();
      this._WaterLoggingList();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    async _waterLoggingStats() {
      let res = await disasterPreventionApi.waterLoggingStats({ statsType: 2 });
      this.warnNum = res.data.data.hasWater ? res.data.data.hasWater : 0;
    },

    async _WaterLoggingList() {
      let res = await disasterPreventionApi.waterLoggingList({ ponding: true });
      this.pointList = res.data;
    },
    drawPonit() {
      this.drawFlag = !this.drawFlag;
      // if (!this.map.isLayerExist("pondingLayer")) {
      if (this.drawFlag) {
        let style = {
          scale: 0.7,
          anchor: [27, 30],
          anchorXUnits: "pixels",
          srcs: require("@/assets/images/MapPoint/ponding-group.png"),
          src: require("@/assets/images/MapPoint/ponding-point.png"),
        };
        let pondingList = [];
        for (let point of this.pointList) {
          let obj = {
            name: point.name,
            type: "积水",
            lng: Number(point.longitude),
            lat: Number(point.latitude),
            code: point.id,
            stationCode: point.id || "",
          };
          pondingList.push(obj);
        }
        this.map.drawClusterPoint(pondingList, style, "warnPondingLayer");
        this.$emit("legend-change", "积水", {
          label: "积水",
          children: [
            {
              label: "无积水",
              icon: "icon-legend-noponding",
            },
            {
              label: "积水",
              icon: "icon-legend-ponding",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "warnPondingLayer");
        this.$emit("legend-change", "积水", {});
      }
      // }
    },
  },
};
</script>
<style lang="scss" scoped>
.stagnant-water-count .has-count {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
</style>
