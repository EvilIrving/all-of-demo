<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:29:11
 * @LastEditTime: 2021-07-02 17:15:03
 * @LastEditors: Please set LastEditors
 * @Description: 海塘统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\SeawallCount.vue
-->

<template>
  <count-wrap title="海塘" class="seawall-count">
    <el-row class="has-count">
      <el-col :span="6" class="fs24">
        <p>蓝色</p>
        <p>
          <span class="blue fs36">{{ info.blueLength }}</span
          >km
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>黄色</p>
        <p>
          <span class="yellow fs36">{{ info.yellowLength }}</span
          >km
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>橙色</p>
        <p>
          <span class="orange fs36">{{ info.orangeLength }}</span
          >km
        </p>
      </el-col>
      <el-col :span="6" class="fs24">
        <p>红色</p>
        <p>
          <span class="red fs36">{{ info.redLength }}</span
          >km
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "SeawallCount",
  data() {
    return {
      info: {
        blueLength: 0,
        orangeLength: 0,
        redLength: 0,
        yellowLength: 0,
      },
      timer:null,
    };
  },
  components: { CountWrap },
  created() {},
  mounted() {
    this._seawallWarnStatistic();
    this.timer = setInterval(()=>{
      this._seawallWarnStatistic();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    async _seawallWarnStatistic() {
      await disasterPreventionApi.seawallWarnStatistic({ drill: false }).then((res) => {
        if (res.code == 0) {
          this.info = res.data;
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/CountCard";
.seawall-count .has-count {
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
