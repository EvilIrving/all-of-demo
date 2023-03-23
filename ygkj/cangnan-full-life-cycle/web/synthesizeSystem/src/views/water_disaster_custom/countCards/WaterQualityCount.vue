<template>
  <count-wrap title="水质监控" class="water-quality-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs24">
        <p>水文站点Ⅱ类以上</p>
        <p>
          <span class="orange fs36">{{ overWarnNum }}</span
          >个
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>国控断面Ⅲ类以上</p>
        <p>
          <span class="yellow fs36">{{ maxStName }}</span>
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>省控断面Ⅲ类以上</p>
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
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.water-quality-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 100%;
  }
}
</style>
