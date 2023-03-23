<!--
 * @Author: your name
 * @Date: 2021-07-21 20:29:23
 * @LastEditTime: 2021-07-22 14:30:25
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\FloodRiskCount.vue
-->
<template>
  <count-wrap title="洪水风险图" class="flood-risk-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs24">
        <p>淹没面积</p>
        <p>
          <span class="yellow fs36">{{ FloodRiskTotal.floodedAreaTotal }}</span
          >km²
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>影响人口</p>
        <p>
          <span class="orange fs36">{{
            FloodRiskTotal.submergedPopulationTotal
          }}</span
          >万人
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>淹没区洪水损失</p>
        <p>
          <span class="orange fs36">{{ FloodRiskTotal.floodLossTotal }}</span
          >万元
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";
export default {
  name: "FloodRiskCount",
  data() {
    return {
      warnNum: 1,
      flag: false,
    };
  },
  computed: {
    ...mapGetters(["FloodRiskTotal"]),
  },
  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.flood-risk-count .has-count {
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
