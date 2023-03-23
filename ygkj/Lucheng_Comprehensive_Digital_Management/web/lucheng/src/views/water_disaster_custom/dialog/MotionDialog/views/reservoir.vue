<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: 概化图水库
 * @LastEditors: dtb
 * @LastEditTime: 2022-07-28 00:19:11
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\MotionDialog\views\reservoir.vue
-->
<template>
  <div class="seawall-box">
    <div class="seawall-tab">
      <div @click="switchTap(index)" :class="{ active: tapOptions == index }" v-for="(item, index) in tapList" :key="item">
        <span class="img" :class="'img_0' + (index + 1)"></span>
        <span>{{ item }}</span>
      </div>
    </div>
    <div class="seawall-content">
      <water v-if="tapOptions == 0" type='水库' :pointData="pointData" />
      <reservoir-index v-if="tapOptions == 1" :pointData="pointData" />
      <ProjectVideo v-if="tapOptions == 2" :code="pointData.code" />
      <CustomIframe :src="frameUrl" v-if="tapOptions===3" />
    </div>
  </div>
</template>

<script>
/** 自定义iframe */
let CustomIframe = {
  props: {},
  render() {
    return <iframe style={{ width: '100%', height: 'calc(100% - 80px)', 'min-height': '480px' }} />;
  }
};
import water from '../components/components/water.vue';
import ReservoirIndex from '../components/ReservoirIndex.vue';
import Monitoring from '../components/components/Monitoring.vue';
import { hydraulicApi } from '@/api/hydraulic_amap';
import ProjectVideo from '@/components/screen_project_dialog/components/ProjectVideo.vue';
export default {
  props: {
    pointData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      tapList: ['水库水位', '工程信息', '视频监控', '运行管理'],
      tapOptions: 0,
      code: '',
      frameUrl: '',
    };
  },
  components: {
    ReservoirIndex,
    Monitoring,
    CustomIframe,
    water,
    ProjectVideo
  },
  mounted() {
    if (this.pointData.type == '抗旱监管') {
      this.tapOptions = 1
    }
    this.getDynamicToken()
  },
  methods: {
    switchTap(index) {
      this.tapOptions = index;
      if (index === 3) {
        this.frameUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${this.pointData.code
          }&name=${encodeURI(encodeURI(this.pointData.name))}&fromMap=1&pName=&cName=&token=${this.token}`;
      }
    },
    getDynamicToken() {
      hydraulicApi.getDynamicToken().then((res) => {
        if (res.code == 0) {
          this.token = res.data;
        }
      });
    },
  }
};
</script>

<style lang="scss" scoped>
.seawall-box {
  .seawall-tab {
    display: flex;
    padding: 24px 0 10px;
    > div {
      width: 113px;
      height: 36px;
      color: #ffffff;
      opacity: 0.3;
      font-size: 18px;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 33px;
      background: url(~assets/images/WaterDisastersImage/seawall-tap-bg.png) no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
      .img {
        width: 21px;
        height: 19px;
        margin-right: 10px;
        &.img_01 {
          background: url(~assets/images/WaterDisastersImage/seawall-tap-01.png) no-repeat;
          background-size: 100% 100%;
        }
        &.img_02 {
          background: url(~assets/images/WaterDisastersImage/seawall-tap-02.png) no-repeat;
          background-size: 100% 100%;
        }
        &.img_03 {
          background: url(~assets/images/WaterDisastersImage/seawall-tap-03.png) no-repeat;
          background-size: 100% 100%;
        }
        &.img_04 {
          background: url(~assets/images/WaterDisastersImage/seawall-tap-04.png) no-repeat;
          background-size: 100% 100%;
        }
      }
      &.active {
        opacity: 1;
        background: url(~assets/images/WaterDisastersImage/seawall-tap-active-bg.png) no-repeat;
        background-size: 100% 100%;
      }
    }
  }
}
</style>