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
      <el-col :span="8" class="fs24">
        <p>报警数量</p>
        <p><span class="yellow fs36">258</span>个</p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>平均纳洪能力</p>
        <p>
          <span class="yellow fs36">{{ realtimeChartList.avgWarnStorageAbility }}</span
          >mm
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>纳洪量</p>
        <p>
          <span class="orange fs36">{{ realtimeChartList.totalWarnStorageSub }}</span
          >万m³
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
