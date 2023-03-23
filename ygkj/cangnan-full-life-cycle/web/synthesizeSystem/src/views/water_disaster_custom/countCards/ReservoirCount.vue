<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:24:08
 * @LastEditTime: 2021-07-02 16:11:57
 * @LastEditors: Please set LastEditors
 * @Description: 水库统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\ReservoirCount.vue
-->
<template>
  <count-wrap title="水库" class="reservoir-warn-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p>大中型超警</p>
        <p>
          <span class="yellow fs36">{{ reservoirData.lmOverLimit }}</span
          >座
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p>小型超警</p>
        <p>
          <span class="yellow fs36">{{
            reservoirData.s1OverLimit + reservoirData.s2OverLimit
          }}</span
          >座
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "ReservoirCount",
  data() {
    return {
      reservoirData: {
        lmOverLimit: 0,
        s1OverLimit: 0,
        s2OverLimit: 0,
      },
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.rsvrProjStats1();
    this.timer = setInterval(()=>{
      this.rsvrProjStats1();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    async rsvrProjStats1() {
      await disasterPreventionApi.rsvrProjStats1().then((res) => {
        if (res.code == 0) {
          this.reservoirData = res.data;
        }
      });
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
