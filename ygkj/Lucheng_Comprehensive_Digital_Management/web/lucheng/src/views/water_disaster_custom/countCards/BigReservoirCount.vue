<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:30:22
 * @LastEditTime: 2021-07-02 17:24:56
 * @LastEditors: Please set LastEditors
 * @Description: 大中型水库统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\BigReservoirCount.vue
-->
<template>
  <count-wrap title="大中型水库" class="big-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p>
          大中型水库泄洪<span class="yellow ml38 mr12 fs36">{{
            resData.drainageRsvrNum
          }}</span
          >座
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p>
          累计泄洪量<span class="yellow fs36 ml38 mr12">{{ resData.drainageTotal }}</span
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
  name: "BigReservoirCount",
  data() {
    return {
      //大中型水库统计数据
      resData: {
        drainageRsvrNum: 0,
        drainageTotal: 0,
      },
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.rsvrProjDrainageStats();
    this.timer = setInterval(()=>{
      this.rsvrProjDrainageStats();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    /**
     * 大中型水库
     */
    async rsvrProjDrainageStats() {
      await disasterPreventionApi.rsvrProjDrainageStats().then((res) => {
        if (res.code == 0) {
          this.resData = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.big-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>
