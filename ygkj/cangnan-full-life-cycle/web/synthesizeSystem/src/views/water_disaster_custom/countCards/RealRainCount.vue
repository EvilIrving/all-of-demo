<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:29:55
 * @LastEditTime: 2021-07-02 16:11:48
 * @LastEditors: Please set LastEditors
 * @Description: 实时降雨统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\RealRainCount.vue
-->
<template>
  <count-wrap title="实时降雨" class="rain-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p>
          1小时超30mm<span class="yellow ml12 mr12 fs36">{{
            realtimePreList["1hourBeyond30"] || "0"
          }}</span
          >个
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p>
          3小时超60mm<span class="orange fs36 ml12 mr12">{{
            realtimePreList["3hourBeyond60"] || "0"
          }}</span
          >个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "RealRainCount",
  data() {
    return {
      //短临预报降雨数据
      realtimePreList: {},
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.getRainfallData();
    this.timer = setInterval(()=>{
      this.getRainfallData();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    /** 初始化雨量数据 */
    getRainfallData(areaName) {
      let opt = {
        areaName: "",
      };
      if (areaName && areaName != "温州市") {
        opt.areaName = areaName;
      } else {
        opt.areaName = "";
      }
      disasterPreventionApi.realtimeRainfallByAreaName(opt).then((res) => {
        if (res.code == 0) {
          this.realtimePreList = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.rain-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>
