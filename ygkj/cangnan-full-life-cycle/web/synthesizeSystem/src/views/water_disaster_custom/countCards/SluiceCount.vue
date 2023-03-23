<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:25:51
 * @LastEditTime: 2021-07-02 16:12:40
 * @LastEditors: Please set LastEditors
 * @Description: 水闸统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\SluiceCount.vue
-->
<template>
  <count-wrap title="水闸" class="sluice-count">
    <div class="has-count">
      开启数量
      <span class="fs36 ml38 mr12 yellow">{{ wagaData.wrpOpenNum }}</span
      >个
    </div>
  </count-wrap>
</template>

<script>
import { disasterPreventionApi } from "@/api/RightSildePanel";
import CountWrap from "../components/CountWrap.vue";
export default {
  name: "SluiceCount",
  data() {
    return {
      //水闸统计数据
      wagaData: {
        wrpOpenNum: 0,
      },
      //累计泄洪时间
      num: 1,
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.wagaProjStats();
    this.timer = setInterval(()=>{
      this.wagaProjStats();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    /**
     * 水闸统计数据
     */
    async wagaProjStats() {
      let obj = {
        hours: this.num,
      };
      await disasterPreventionApi.wagaProjStats(obj).then((res) => {
        if (res.code == 0) {
          this.wagaData = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.sluice-count .has-count {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
</style>
