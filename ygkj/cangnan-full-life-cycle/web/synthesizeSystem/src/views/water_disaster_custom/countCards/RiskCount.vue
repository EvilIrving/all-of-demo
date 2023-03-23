<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:29:34
 * @LastEditTime: 2021-07-02 16:12:07
 * @LastEditors: Please set LastEditors
 * @Description: 风险隐患统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\RiskCount.vue
-->
<template>
  <count-wrap title="风险隐患" class="risk-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p>
          隐患点未整改<span class="orange ml12 mr12 fs36">{{ hiddenDanger }}</span
          >个
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p>
          薄弱点未整改<span class="yellow fs36 ml12 mr12">{{ weakPoints }}</span
          >个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "RiskCount",
  data() {
    return {
      hiddenDanger: 0,
      weakPoints: 0,
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this.cancellationHiddenDangerStatic();
    this.cancellationWeakPointsStatic();
    this.timer = setInterval(()=>{
      this.cancellationHiddenDangerStatic();
      this.cancellationWeakPointsStatic();
    },300000);
  },
  methods: {
    /**
     * 隐患点销号情况
     */
    async cancellationHiddenDangerStatic() {
      let res = await disasterPreventionApi.cancellationHiddenDangerStatic();
      if (res.code == 0) {
        const index = res.data.findIndex((item) => item.reccondition == "未整改");
        this.hiddenDanger = res.data[index].num;
      }
    },
    /**
     * 薄弱点销号情况统计
     */
    async cancellationWeakPointsStatic() {
      let res = await disasterPreventionApi.cancellationWeakPointsStatic();
      if (res.code == 0) {
        const index = res.data.findIndex((item) => item.reccondition == "未整改");
        this.weakPoints = res.data[index].num;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.risk-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>
