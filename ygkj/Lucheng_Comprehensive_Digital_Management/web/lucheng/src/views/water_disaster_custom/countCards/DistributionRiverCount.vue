<!--
 * @Author: ly
 * @Date: 2021-07-01 18:02:25
 * @LastEditTime: 2021-07-29 17:06:00
 * @LastEditors: Please set LastEditors
 * @Description: 河流分布 C位
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\DistributionRiverCount.vue
-->
<template> 
  <count-wrap title="河流分布" class="mountain-warn-count">
    <div class="no-count" v-if="warnNum == 0">当前无数据</div>
    <el-row class="has-count" v-else>
      <el-col :span="12" class="fs24">
        <p>河流总数量<span class="yellow fs36"> {{overviewList.resultNum}} </span> 个</p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p>河流总长度<span class="yellow fs36"> {{overviewList.totalAmount}} </span> km</p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { programmeApi } from "@/api/programme_api/index.js";
import { mapGetters } from "vuex";
export default {
  name: "DistributionRiverCount",
  data() {
    return {
      warnNum: 1,
      flag: false,
      overviewList: {}
    };
  },
  watch: {
    '$store.state.yearData': function(val) {
      this.year = val;
      this.statsCwindowOfWaterResearch();
    }
  },
  computed: {
    ...mapGetters(["torrentsWarning", "SetDistributionRiverCount"]),
  },
  components: { CountWrap },
  mounted() {
    this.$nextTick(()=>{
      this.statsCwindowOfWaterResearch();
    })
  },
  methods: {
    async statsCwindowOfWaterResearch() {
      await programmeApi.statsCwindowOfWaterResearch().then((res) => {
        if (res.code == 0) {
          this.overviewList = res.data;
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "./style/CountCard";
  .mountain-warn-count .has-count {
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
