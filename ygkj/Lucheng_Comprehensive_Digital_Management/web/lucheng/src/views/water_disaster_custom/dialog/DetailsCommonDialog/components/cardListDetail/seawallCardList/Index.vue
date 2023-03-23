<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-09-23 16:06:01
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\Index.vue
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
        <span v-if="index + 1 == tapOptions.length" class="img" :class="'img_0' + (index + 1)"></span>
        <span>{{ item }}</span>
      </div>
    </div>
    <div class="seawall-content" v-if="code">
      <SeawallIndex
        :detailData="seawallData"
        :lat="seawallData.latitude"
        :lng="seawallData.longitude"
        :code="code"
        v-if="tapOptions == 0"
      />
      <SeawallMonitoring :code="code" v-if="tapOptions == 1" />
      <SeawallFiles :code="code" v-if="tapOptions == 2" />
      <SeawallDanger :code="code" v-if="tapOptions == 3" />
      <SubsidiaryFacilities :detailData="seawallData" :code="code" v-if="tapOptions == 4" />
    </div>
  </div>
</template>

<script>
import SeawallIndex from './components/SeawallIndex.vue';
import SeawallMonitoring from './components/SeawallMonitoring.vue';
import SeawallFiles from './components/SeawallFiles.vue';
import SeawallDanger from './components/SeawallDanger.vue';
import SubsidiaryFacilities from './components/SubsidiaryList.vue';
import { bmAccBuildList, loadPage, selectAncillaryBuilding } from '@/api/seawall.js';
import { cloneObj } from '@/utils/utils';
export default {
  props: {
    seawallData: {
      type: Object,
      default: {}
    }
  },
  data() {
    return {
      tapList: ['首页', '监控', '档案', '隐患', '关联建筑'],
      tapOptions: 0,
      code: ''
    };
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  components: {
    SeawallIndex,
    SeawallMonitoring,
    SeawallFiles,
    SeawallDanger,
    SubsidiaryFacilities
  },
  mounted() {
    this.code = this.seawallData.code;
    this.getData();
    this.getLoadPage();
    this.getSelectAncillaryBuilding();
  },
  methods: {
    switchTap(index) {
      this.tapOptions = index;
    },
    async getSelectAncillaryBuilding() {
      if (this.map) {
        this.map.drawClusterPoint([], {}, `sluiceLayer`);
        this.map.drawClusterPoint([], {}, `pumpLayer`);
        // this.map.drawClusterPoint([], {}, `pipeLayer`);
        this.map.drawClusterPoint([], {}, `rainbowLayer`);
      }
      let res = await selectAncillaryBuilding({ prcd: this.code });
      if (res.code == 0) {
        this.tableData = res.data;
        if (res.data.length > 0) {
          let dryBrakeList = []; //水闸
          let wharfList = []; //泵站
          let bridgeList = []; //倒吸虹
          res.data.map((item) => {
            let obj = {
              name: item.proName,
              lat: Number(item.LTTD),
              lng: Number(item.LGTD),
              data: item,
              stake: '2',
              prnm: this.seawallData.seawallName,
              type: '附属工程',
              props: {}
            };
            if (item.project_category == '水闸') {
              dryBrakeList.push(obj);
            } else if (item.project_category == '泵站') {
              wharfList.push(obj);
            } else if (item.project_category == '倒吸虹') {
              bridgeList.push(obj);
            }
          });
          this.setCodePoint(dryBrakeList, 'sluice', true);
          this.setCodePoint(wharfList, 'pump', true);
          this.setCodePoint(bridgeList, 'rainbow', true);
        }
      }
    },
    async getLoadPage() {
      if (this.map) {
        this.map.drawClusterPoint([], {}, `stakeLayer`);
      }
      let res = await loadPage({ prcd: this.code });
      if (res.code == 0) {
        if (res.data.list.length > 0) {
          let stakeList = res.data.list.map((item) => {
            let obj = {
              name: `桩号${item.stake}`,
              lat: Number(item.stakeLatitude),
              lng: Number(item.stakeLongitude),
              data: item,
              stake: '1',
              prnm: this.seawallData.seawallName,
              type: '附属工程',
              props: {}
            };
            return obj;
          });
          this.setCodePoint(stakeList, 'stake', true);
        }
      }
    },
    async getData() {
      if (this.map) {
        this.map.drawClusterPoint([], {}, `dryBrakeLayer`);
        this.map.drawClusterPoint([], {}, `wharfLayer`);
        this.map.drawClusterPoint([], {}, `intersectLayer`);
        this.map.drawClusterPoint([], {}, `bridgeLayer`);
        this.map.drawClusterPoint([], {}, `culvertLayer`);
        this.map.drawClusterPoint([], {}, `roomLayer`);
        this.map.drawClusterPoint([], {}, `stationLayer`);
        this.map.drawClusterPoint([], {}, `watergateLayer`);
      }
      let res = await bmAccBuildList({ prcd: this.code });
      if (res.code == 0) {
        let dryBrakeList = []; //旱闸
        let wharfList = []; //码头
        let intersectList = []; //交叉路口
        let bridgeList = []; //桥梁
        let culvertList = []; //箱涵
        let roomList = []; //管理房
        let stationList = []; //测站平台
        let watergateList = []; //规模以下水闸
        res.data.map((item) => {
          let obj = {
            name: item.proName,
            lat: Number(item.lat),
            lng: Number(item.lng),
            data: item,
            stake: '0',
            type: '附属工程',
            props: {}
          };
          if (item.proType == '旱闸') {
            dryBrakeList.push(obj);
          } else if (item.proType == '码头') {
            wharfList.push(obj);
          } else if (item.proType == '海塘道路交叉出入口') {
            intersectList.push(obj);
          } else if (item.proType == '桥梁') {
            bridgeList.push(obj);
          } else if (item.proType == '箱涵') {
            culvertList.push(obj);
          } else if (item.proType == '测站平台') {
            stationList.push(obj);
          } else if (item.proType == '规模以下水闸') {
            watergateList.push(obj);
          } else {
            roomList.push(obj);
          }
        });
        this.setCodePoint(dryBrakeList, 'dryBrake', true);
        this.setCodePoint(wharfList, 'wharf', true);
        this.setCodePoint(intersectList, 'intersect', true);
        this.setCodePoint(bridgeList, 'bridge', true);
        this.setCodePoint(culvertList, 'culvert', true);
        this.setCodePoint(roomList, 'room', true);
        this.setCodePoint(stationList, 'station', true);
        this.setCodePoint(watergateList, 'watergate', true);
      }
    },
    setCodePoint(dataList, type, flag) {
      let style = flag ? this.getPointStyle(type) : {};
      this.setPoint(dataList, style, type);
    },
    getPointStyle(projectType) {
      let defaultStyle = {
        scale: 0.6,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: require(`@/assets/images/MapPoint/${projectType}-icon-juhe.png`),
        src: require(`@/assets/images/MapPoint/${projectType}-icon.png`)
      };
      let styleObj = cloneObj(defaultStyle);
      return styleObj;
    },
    setPoint(pointList, pointStyle, layer) {
      if (this.map) {
        this.map.drawClusterPoint(pointList, pointStyle, `${layer}Layer`);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.seawall-box {
  .seawall-tab {
    display: flex;
    padding: 24px 0;
    border-bottom: 1px solid #026fe0;
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