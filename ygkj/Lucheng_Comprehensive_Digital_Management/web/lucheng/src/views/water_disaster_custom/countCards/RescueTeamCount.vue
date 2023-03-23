<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:24:08
 * @LastEditTime: 2021-12-07 10:35:20
 * @LastEditors: dtb
 * @Description: 救援队伍统计卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\RescueTeamCount.vue
-->
<template>
  <count-wrap title="救援队伍" class="reservoir-warn-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs16">
        <p>救援队伍总数</p>
        <p>
          <span class="work-green fs36">{{ reservoirData.lmOverLimit }}</span> 个
        </p>
      </el-col>
      <el-col :span="8" class="fs16">
        <p>XXXXX救援队名称(最多)</p>
        <p>
          <span class="work-green fs36">{{ reservoirData.s1OverLimit + reservoirData.s2OverLimit }}</span>人
        </p>
      </el-col>
      <el-col :span="8" class="fs16">
        <p>XXXXX救援队名称(最多)</p>
        <p>
          <span class="work-green fs36">{{ reservoirData.s1OverLimit + reservoirData.s2OverLimit }}</span>人
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { disasterPreventionApi } from '@/api/RightSildePanel';
export default {
  name: 'RescueTeamCount',
  data() {
    return {
      reservoirData: {
        lmOverLimit: 0,
        s1OverLimit: 0,
        s2OverLimit: 0
      },
      timer: null
    };
  },
  components: { CountWrap },
  created() { },
  mounted() {
    this.rsvrProjStats1();
    this.timer = setInterval(() => {
      this.rsvrProjStats1();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    async rsvrProjStats1() {
      await disasterPreventionApi.rsvrProjStats1().then((res) => {
        if (res.code == 0) {
          this.reservoirData = res.data;
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.reservoir-warn-count .has-count {
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
