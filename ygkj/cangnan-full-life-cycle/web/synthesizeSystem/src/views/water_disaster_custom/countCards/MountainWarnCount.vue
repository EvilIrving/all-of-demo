<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:02:25
 * @LastEditTime: 2021-07-02 17:06:00
 * @LastEditors: Please set LastEditors
 * @Description: 山洪预警统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\MountainWarnCount.vue
-->
<template>
  <count-wrap title="山洪预警" class="mountain-warn-count">
    <div class="no-count" v-if="warnNum == 0">当前无预警</div>
    <el-row class="has-count" v-else>
      <el-col :span="8" class="fs24">
        <p>影响村落</p>
        <p><span class="yellow fs36">{{ importvillage || 0}}</span>个</p>
      </el-col>
      <template v-if="MountainCardTab == '预报'">
        <el-col :span="8" class="fs24">
          <p>有可能影响人口</p>
          <p>
            <span class="yellow fs36">{{ this.torrentsWarning.grade4Person || 0 }}</span
            >人
          </p>
        </el-col>
        <el-col :span="8" class="fs24">
          <p>可能性很大影响人口</p>
          <p>
            <span class="orange fs36">{{ this.torrentsWarning.grade5Person || 0 }}</span
            >人
          </p>
        </el-col>
      </template>
      <template v-else>
        <el-col :span="8" class="fs24">
          <p>准备转移人口</p>
          <p>
            <span class="yellow fs36">{{ this.torrentsWarning.grade4Person || 0 }}</span
            >人
          </p>
        </el-col>
        <el-col :span="8" class="fs24">
          <p>立即转移人口</p>
          <p>
            <span class="orange fs36">{{ this.torrentsWarning.grade5Person || 0 }}</span
            >人
          </p>
        </el-col>
      </template>
    </el-row>
  </count-wrap>
</template>

<script>
// import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";
export default {
  name: "MountainWarnCount", 
  data() {
    return {
      warnNum: 1,
      flag: false,
    };
  },
  computed: {
    ...mapGetters(["torrentsWarning", "MountainCardTab","torrentsWarn"]),
     importvillage(){
      //  console.log(this.torrentsWarn.data.promptlyMoveVillage,1234)
let res=Number(this.torrentsWarn.data.promptlyMoveVillage)+Number(this.torrentsWarn.data.warningMoveVillage)
// console.log(res,567)
return res
    }
  },

  components: { CountWrap },
  created() {},
  mounted() {},
  methods: {
   
  },
};
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
