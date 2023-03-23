<!--
 * @Author: your name
 * @Date: 2021-07-15 09:04:20
 * @LastEditTime: 2021-07-15 18:18:18
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\FlowMonitorCount.vue
-->
<template>
  <count-wrap title="流量监控" class="flow-monitor-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs24">
        <p>流量报警数量</p>
        <p>
          <span
            class="orange fs36"
            @click="onPointClick('warn')"
            style="cursor: pointer"
            >{{ overWarnNum }}</span
          >个
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>最大流量站点</p>
        <p>
          <span
            class="yellow fs36"
            @click="onPointClick('max')"
            style="cursor: pointer"
            >{{ maxStName }}</span
          >
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>最大流量值</p>
        <p>
          <span class="orange fs36">{{ maxFlow }}</span
          >(m³/s)
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import { getWaterFlowStat } from "@/api/flow.js";
import CountWrap from "../components/CountWrap.vue";
export default {
  name: "FlowMonitorCount",
  data() {
    return {
      statData: {},
      warnList: [],
      maxList: [],
      warnFlag: false,
      maxFlag: false,
      timer:null,
    };
  },
  components: { CountWrap },
  computed: {
    overWarnNum() {
      return this.statData.overWarnNum;
    },
    maxFlow() {
      return this.statData.max?.flow || "-";
    },
    maxStName() {
      return this.statData.max?.stName || "-";
    },
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  mounted() {
    this.getWaterFlowStat();
    this.timer = setInterval(()=>{
      this.getWaterFlowStat();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    getWaterFlowStat() {
      getWaterFlowStat().then((res) => {
        if (res.code == 0) {
          this.statData = res.data;
          this.warnList =
            res.data.overWarnNum > 0 ? res.data.overWarnStation : [];
          this.maxList[0] = res.data.max;
        }
      });
    },
    onPointClick(type) {
      let list = [];
      let layerName = "";
      if (type == "warn") {
        this.warnFlag = !this.warnFlag;
        list = this.warnList;
        layerName = "flowWarnLayer";
      } else {
        this.maxFlag = !this.maxFlag;
        list = this.maxList;
        layerName = "flowMaxLayer";
      }
      if (
        (type == "warn" && !this.warnFlag) ||
        (type == "max" && !this.maxFlag)
      ) {
        this.map.drawPoint([], {}, layerName);
      } else {
        let style = {
          scale: 0.7,
          anchor: [24, 16],
          anchorXUnits: "pixels",
          src: require("@/assets/images/MapPoint/flow-normal.png"),
        };
        let imgSrc = require("@/assets/images/MapPoint/flow-normal.png");
        let pointList = list.map((item) =>{
          if(item.flowOverWarn){
            imgSrc = require("@/assets/images/MapPoint/flow-abnormal.png");
          }
          return {
            name: item.stName,
            lng: item.stLong,
            lat: item.stLat,
            type: "流量",
            src: imgSrc,
            code: item.stCode,
            props: { 流量: `${item.flow || "-"}m³/s` },
          }
        });
        this.map.drawPoint(pointList, style, layerName);
      }
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.flow-monitor-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 100%;
  }
}
</style>
