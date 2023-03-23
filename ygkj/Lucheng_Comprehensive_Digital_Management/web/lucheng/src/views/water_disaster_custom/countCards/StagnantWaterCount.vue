<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-12-28 15:35:33
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\StagnantWaterCount.vue
-->
<template>
  <count-wrap title="救援仓库" class="seawall-count">
    <div class="has-count">
      <div>
        <p>仓库总数</p>
        <p>
          <span @click="handleStagnantWater('jsbtn')" style="font-size: 30px; color: #47f5a7; cursor: pointer">{{
            warnNum.total 
          }}</span>个
        </p>
      </div>
      <div>
        <p>仓库总面积</p>
        <p><span style="font-size: 30px; color: #47f5a7">{{ warnNum.totalArea  }}</span>㎡</p>
      </div>
      <div>
        <p>仓库总价值</p>
        <p>
          <span style="font-size: 30px; color: #47f5a7">{{ warnNum.materialPrice.toFixed(2)  }}</span>元
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { getWaterWarehousesCount, getWaterWarehouses } from '@/api/flow';
export default {
  name: 'StagnantWaterCount',
  data() {
    return {
      warnNum: {},
      jsbtn: false, // 落点
      pointList: [],
      drawFlag: false,
      timer: null
    };
  },
  components: { CountWrap },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  created() { },
  mounted() {
    this._waterLoggingStats();
    this.getWaterLoggingList();
    this.timer = setInterval(() => {
      this._waterLoggingStats();
      this.getWaterLoggingList();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    async _waterLoggingStats() {
      let res = await getWaterWarehousesCount();
      if (res.code == 0) {
        this.warnNum = res.data;
      }
    },
    // 获取点位
    async getWaterLoggingList() {
      let res = await getWaterWarehouses();
      this.pointList = res.data.list;
    },
    // 地图点位
    handleStagnantWater(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/images/legend/icon-warehouse-juhe.png'),
        src: require('@/assets/images/legend/icon-warehouse.png')
      };
      let pondingList = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.name,
          type: '救援仓库',
          lng: +point.longitude,
          lat: +point.latitude,
          code: point.id,
          stationCode: point.id || '',
          props: {
            仓库面积: `${point.acreage}㎡`,
            仓库价值: `${point.materialPrice?point.materialPrice:'-'}亿`,
            联系人: `${point.chargePerson}`,
            联系方式: `${point.chargePhone}`
          }
        };
        pondingList.push(obj);
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(pondingList, style, 'pondingLayer');
      } else {
        this.map.drawClusterPoint([], {}, 'pondingLayer');
      }
    },
  }
};
</script>
<style lang="scss" scoped>
.seawall-count .has-count {
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  > div {
    p {
      &:nth-child(1) {
        padding-bottom: 5px;
      }
    }
  }
}
</style>
