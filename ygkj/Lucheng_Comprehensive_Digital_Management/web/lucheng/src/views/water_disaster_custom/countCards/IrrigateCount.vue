<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:24:08
 * @LastEditTime: 2021-07-02 16:11:57
 * @LastEditors: Please set LastEditors
 * @Description: 水库统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\ReservoirCount.vue
-->
<template>
  <count-wrap title="灌区" class="reservoir-warn-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs24">
        <p>灌区总数</p>
        <p>
          <span class="yellow fs36">{{ statisticObj.irrNum }}</span
          >座
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>设计灌溉面积</p>
        <p>
          <span class="yellow fs36">{{ statisticObj.desIrrArea }}</span
          >k㎡
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>有效灌溉面积</p>
        <p>
          <span class="yellow fs36">{{ statisticObj.effIrrArea }}</span
          >k㎡
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { hydraulicApi } from "@/api/hydraulic_amap";
export default {
  name: "IrrigateCount",
  data() {
    return {
      statisticObj: {
        irrNum: 0,
        desIrrArea: 0,
        effIrrArea: 0,
      },
      timer: null,
    };
  },
  components: { CountWrap },
  created() { },
  mounted() {
    this.rsvrProjStats1();
    this.timer = setInterval(() => {
      this.rsvrProjStats1();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    async rsvrProjStats1() {
      const opt = {
        type: '灌区'
      }
      await hydraulicApi.stats4ProjectList(opt).then(res => {
        if (res.code == 0) {
          this.statisticObj = res.data;
        }
      })
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.reservoir-warn-count .has-count {
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
