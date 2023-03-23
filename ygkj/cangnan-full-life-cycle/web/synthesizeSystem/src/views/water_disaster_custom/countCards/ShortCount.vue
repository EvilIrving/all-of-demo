<!--
 * @Author:dyn
 * @Date: 2021-07-01 18:13:28
 * @LastEditTime: 2021-07-02 16:12:30
 * @LastEditors: Please set LastEditors
 * @Description: 短临预报
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\ShortCount.vue
-->
<template>
  <count-wrap title="短临预报" class="short-count">
    <div class="has-count">
      <div class="fs24">
        <p>预计1小时最大雨量</p>
        <p>
          <span class="green fs32">{{ forecastPreMaxList.maxRainfall.toFixed(1) }}</span
          >mm
        </p>
      </div>
      <div class="fs24">
        <p>预计1小时最大降雨区域</p>
        <p>
          <span class="green fs32">
            {{ forecastPreMaxList.maxRainfallAreaName }}
            {{ forecastPreMaxList.areaMaxRainfall.toFixed(1) }}</span
          >mm
        </p>
      </div>
      <div class="fs24">
        <p>预计1小时最大降雨流域</p>
        <p>
          <span class="green fs32"
            >{{ forecastPreMaxList.maxRainfallBasinName }}
            {{ forecastPreMaxList.maxBasinRainfall.toFixed(1) }}</span
          >mm
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "ShortCount",
  data() {
    return {
      warnNum: 1,
      forecastPreMaxList: {
        maxRainfall: 0,
        areaMaxRainfall: 0,
        maxBasinRainfall: 0,
      },
      timer:null
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.getRainfallMaxValuePredict();
    this.timer = setInterval(()=>{
      this.getRainfallMaxValuePredict();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    /** 初始化雨量数据 */
    getRainfallMaxValuePredict() {
      disasterPreventionApi.rainfallMaxValuePredict({ hour: 1 }).then((res) => {
        this.forecastPreMaxList = { ...res.data };
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.short-count .has-count {
  height: 100%;
  display: flex;
  justify-content: space-between;
  > div {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    height: 100%;
  }
}
</style>
