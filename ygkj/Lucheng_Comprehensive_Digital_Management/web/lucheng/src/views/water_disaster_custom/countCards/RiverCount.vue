<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:26:38
 * @LastEditTime: 2021-07-02 16:12:14
 * @LastEditors: Please set LastEditors
 * @Description: 河网统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\RiverCount.vue
-->
<template>
  <count-wrap title="河网" class="river-count">
    <el-row class="has-count">
      <el-col :span="4" class="fs24">
        <p>报警数量</p>
        <p><span class="yellow fs36"  style="font-size: 28px;">{{realtimeChartList.overWarnNum}}</span>个</p>
      </el-col>
      <el-col :span="10" class="fs24">
        <p>平均纳洪能力 | 未来6h降雨</p>
        <p>
          <span class="yellow fs36" style="font-size: 28px;">{{realtimeChartList.avgWarnStorageAbility }}</span>mm | 
          <span class="yellow fs36" style="font-size: 28px;">{{ realtimeChartList.forecastNext6hRainfall.toFixed(1) || '-' }}</span>mm
        </p>
      </el-col>
      <el-col :span="10" class="fs24">
        <p>纳洪量 | {{realtimeChartList.nearestLargeMiddleRsvr == null ? '--' : realtimeChartList.nearestLargeMiddleRsvr.resName }}</p>
        <p>
          <span class="orange fs36" style="font-size: 28px;">{{ realtimeChartList.totalWarnStorageSub }}</span>万m³ | 
          <span class="yellow fs36" style="font-size: 28px;">{{ realtimeChartList.nearestLargeMiddleRsvr == null ? '-' : realtimeChartList.nearestLargeMiddleRsvr.waterLevelCapacity }}</span>万m³
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "RiverCount",
  data() {
    return {
      realtimeChartList: {
        avgWarnStorageAbility: 0,
        totalWarnStorageSub: 0,
        timer:null,
      },
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.riverNetworkStaStats();
    this.timer = setInterval(()=>{
      this.riverNetworkStaStats();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    /**
     * 第二页河网数据
     */
    async riverNetworkStaStats() {
      await disasterPreventionApi.NetworkStaStats().then((res) => {
        if (res.code == 0) {
          this.realtimeChartList = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.river-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 100%;
  }

  p {
    text-align: center;
  }
}
</style>
