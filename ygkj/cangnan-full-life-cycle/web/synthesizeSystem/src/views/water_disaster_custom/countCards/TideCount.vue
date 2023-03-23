<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:28:49
 * @LastEditTime: 2021-07-02 17:05:43
 * @LastEditors: Please set LastEditors
 * @Description: 潮位统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\TideCount.vue
-->
<template>
  <count-wrap title="潮位" class="tide-count">
    <div class="no-count" v-if="warnNum == 0">当前无预警</div>
    <div class="has-count" v-else>
      报警数量
      <span class="fs36 ml38 mr12 yellow">5</span>个
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "TideCount",
  data() {
    return {
      warnNum: 0,
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this._tideWarnStatistic();
    this.timer = setInterval(()=>{
      this._tideWarnStatistic();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    async _tideWarnStatistic() {
      await disasterPreventionApi.tideWarnStatistic({ drill: false }).then((res) => {
        if (res.code == 0) {
          this.warnNum = res.data.warnNum;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.tide-count .has-count {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
</style>
