<!--
 * @Autor: ysl
 * @Date: 2021-09-02 10:41:18
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-03 16:52:33
 * @Description: 前期影响雨量C位
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/countCards/ProphaseInfluencedRainfallCount.vue
-->
<template> 
  <count-wrap title="前期影响雨量" class="mountain-warn-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs22" v-if="prophaseInfuencedRainfallCount.aridest">
        <p>最干旱县市区<span class="num fs26"> {{ prophaseInfuencedRainfallCount.aridest.areaName }} <span class="line">|</span> {{ prophaseInfuencedRainfallCount.aridest.drp | toFixed }} </span> </p>
      </el-col>
      <el-col :span="12" class="fs22" v-if="prophaseInfuencedRainfallCount.moistest">
        <p>最湿润县市区<span class="num fs26"> {{ prophaseInfuencedRainfallCount.moistest.areaName }} <span class="line">|</span> {{ prophaseInfuencedRainfallCount.moistest.drp | toFixed }} </span> </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { programmeApi } from "@/api/programme_api/index.js";
import { mapGetters } from "vuex";
export default {
  name: "ProphaseInfluencedRainfallCount",
  data() {
    return {
      warnNum: 1,
      flag: false,
      overviewList: {}
    };
  },
  watch: {
  },
  computed: {
    ...mapGetters(["torrentsWarning", "prophaseInfuencedRainfallCount"]),
  },
  components: { CountWrap },
  mounted() {
  },
  methods: {
  },
  filters: {
    toFixed(val) {
      return val && Number(val).toFixed(2)
    }
  },
}
</script>

<style lang="scss" scoped>
  @import "./style/CountCard";
  .mountain-warn-count .has-count {
    height: 100%;
    .el-col {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 100%;
    }
    .fs22 {
      font-size: 22px;
    }
    .fs26 {
      font-size: 26px;
    }
    p {
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      font-weight: bold;
      .num {
        margin-left: 16px;
        color: #1df8f3;
      }
      .line {
        margin: 0 4px;
        color: #ffffff;
      }
    }
  }
</style>
