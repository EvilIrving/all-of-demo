<!--
 * @Autor: ysl
 * @Date: 2021-08-07 19:15:19
 * @LastEditors: ysl
 * @LastEditTime: 2021-10-13 15:22:57
 * @Description: 
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/WaterQualityCount.vue
-->
<template>
  <count-wrap title="水质监控" class="water-quality-count">
    <el-row class="has-count">
      <el-col :span="16" class="fs24">
        <p>
          合格率
          <span class="num">{{ statData.percentOfPass || 0 }}</span> %
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>
          本月上升个数
          <span class="num">{{ statData.thisMonthWorseNum || 0 }}</span> 个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index";
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
      // return this.statData.overWarnNum;
    },
    maxStName() {
      // return this.statData.max?.stName || "-";
    },
  },
  mounted() {
    this.qualityUnitCWindow();
    this.timer = setInterval(()=>{
      this.qualityUnitCWindow();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    qualityUnitCWindow() {
      hydraulicApi.qualityUnitCWindow().then((res) => {
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
  p {
    font-size: 22px;
  }
  .num {
    font-size: 32px;
    color: #1df8f3;
    margin-left: 12px;
  }
}
</style>
