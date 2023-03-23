<template>
  <div class="shupu_wraper">
    <!-- 切换tabs -->
    <ul class="stname_list" v-show="showTab">
      <li v-for="(item, index) in riverFloodControlList" :key="index" :class="{ active: index === riverFloodControlIndex }" @click="switchTab(item.name, index)">
        <span>{{ item.name }}</span>
      </li>
    </ul>
    <flood-control-map v-if="riverFloodControlIndex === 0" ref="floodControl" @closeTab='closeTab' />
    <water-chart v-if="riverFloodControlIndex === 1" />
    <flood-risk-map v-if="riverFloodControlIndex === 2" />
  </div>
</template>

<script>
import FloodRiskMap from './FloodRiskMap.vue'
import WaterChart from './WaterChart.vue'
import FloodControlMap from './FloodControlMap.vue'
export default {
  data() {
    return {
      riverFloodControlIndex: 0,
      imgSrc: require('@/assets/images/distributionMap/control.png'),
      riverFloodControlList: [
        { active: 0, name: '地图', imgSrc: require('@/assets/images/distributionMap/control.png') },
        { active: 1, name: '水位图', imgSrc: require('@/assets/images/distributionMap/load.png') },
        { active: 2, name: '风险图', imgSrc: require('@/assets/images/distributionMap/drain.png') }
      ],
      showTab: true,
    };
  },
  components: {
    FloodRiskMap,
    FloodControlMap,
    WaterChart,
  },
  methods: {
    switchTab(name, index) {
      this.riverFloodControlIndex = index;
      this.imgSrc = this.riverFloodControlList[index].imgSrc;
    },
    closeTab(type) {
      this.showTab = type
    }
  }
};
</script>

<style lang="scss" scoped>
.shupu_wraper {
  height: 100%;
  box-sizing: border-box;
  background: url(~assets/images/ourPujiang/our-pujiang-icon.png) no-repeat;
  background-size: 100% 100%;
  position: relative;
  overflow: hidden;
  .stname_list {
    position: absolute;
    top: 12%;
    width: 18%;
    height: 10%;
    margin: 0 30px;
    // float: left;
    @include flexbox;
    @include flexJC(space-between);
    @include flexAI(center);
    @include flexflow(row, nowrap);
    z-index: 100;
    li {
      @include flexbox;
      @include flexJC(flex-start);
      @include flexAI(center);
      list-style: none;
      width: 28%;
      height: 35%;
      cursor: pointer;
      background: url('../../assets/images/distributionMap/scheduled.png') no-repeat;
      background-size: 100%;
      span {
        font-size: 14px;
        color: #fff;
        // background: linear-gradient(180deg, #36c0e4 50%, #0639ff 100%);
        // background-clip: text;
        // -webkit-background-clip: text;
        // color: transparent;
        margin-left: 26px;
      }
      &.active {
        background: url('../../assets/images/distributionMap/scheduled_ac.png') no-repeat;
        background-size: 100%;
        span {
          color: #23e3fb;
        }
      }
    }
  }
}
</style>