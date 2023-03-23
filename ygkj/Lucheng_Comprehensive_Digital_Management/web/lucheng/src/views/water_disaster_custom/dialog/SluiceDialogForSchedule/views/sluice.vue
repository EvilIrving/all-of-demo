<!--
 * @Date: 2022-04-02 13:35:34
 * @Author: dtb
 * @Description: 
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-08 16:27:57
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\views\sluice.vue
-->
<template>
  <div class="seawall-box">
    <div class="seawall-tab">
      <div
        @click="switchTap(index)"
        :class="{ active: tapOptions == index }"
        v-for="(item, index) in tapList"
        :key="item"
      >
        <span class="img" :class="'img_0' + (index + 1)"></span>
        <span>{{ item }}</span>
      </div>
    </div>
    <div class="seawall-content">
      <sluice-index :pointData="pointData" v-if="tapOptions == 0" />
      <sluice-monitoring :pointData="pointData" v-if="tapOptions == 1" />
      <CustomIframe :src="frameUrl" v-if="tapOptions === 2" />,
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
import SluiceIndex from '../components/SluiceIndex.vue';
import SluiceMonitoring from '../components/SluiceMonitoring.vue';
import { hydraulicApi } from '@/api/hydraulic_amap';
export default {
  props: {
    pointData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      tapList: ['首页', '监控', '运管'],
      tapOptions: 0,
      code: '',
      frameUrl: ''
    };
  },
  components: {
    SluiceIndex,
    SluiceMonitoring,
    CustomIframe
  },
  mounted() {
    if (this.pointData.type == '抗旱监管') {
      this.tapOptions = 1;
    }
    this.getDynamicToken();
  },
  methods: {
    switchTap(index) {
      this.tapOptions = index;
      if (index === 2) {
        this.frameUrl = `https://sk.zjwater.com/cp/skip/reservoirDetailSingle?prcd=${
          this.pointData.projectCode
        }&name=${encodeURI(encodeURI(this.pointData.name))}&fromMap=1&pName=&cName=&token=${this.token}`;
      }
    },
    getDynamicToken() {
      hydraulicApi.getDynamicToken().then((res) => {
        if (res.code == 0) {
          this.token = res.data;
        }
      });
    }
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