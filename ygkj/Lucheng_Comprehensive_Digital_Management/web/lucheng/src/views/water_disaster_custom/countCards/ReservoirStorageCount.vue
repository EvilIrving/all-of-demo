<!--
 * @Author: your name
 * @Date: 2021-07-12 14:24:53
 * @LastEditTime: 2021-12-07 11:24:15
 * @LastEditors: dtb
 * @Description: 水库蓄水量C位
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\ReservoirStorageCount.vue
-->
<template>
  <count-wrap title="救援队伍" class="stagnant-water-count">
    <div class="has-count">
      <div class="has-count-item">
        <p>救援队伍总数</p>
        <p style="margin-top: 7px">
          <span @click="onPointClick('total')" style="cursor: pointer" class="num">
            {{ detail.totalTeam || 0 }}
            <span class="unit">个</span>
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>{{ detail.maxExistingName }}(最多)</p>
        <p style="margin-top: 7px">
          <span @click="onPointClick('max')" style="cursor: pointer" class="num">
            {{ detail.maxExistingNum || 0 }}
            <span class="unit">人</span>
          </span>
        </p>
      </div>
      <div class="has-count-item">
        <p>{{ detail.minExistingName }}(最少)</p>
        <p style="margin-top: 7px">
          <span @click="onPointClick('min')" style="cursor: pointer" class="num">
            {{ detail.minExistingNum || 0 }}
            <span class="unit">人</span>
          </span>
        </p>
      </div>
    </div>
  </count-wrap>
</template>

<script>
import CountWrap from '../components/CountWrap.vue';
import { getTeamList, getTeamCountList } from '@/api/flow';
export default {
  name: 'ReservoirStorageCount',
  data() {
    return {
      detail: {},
      reservoiList: [],
      timer: null,
      maxExistingId: 0,
      minExistingId: 0,
    };
  },
  components: { CountWrap },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.getDeviceType();
    this.getList();
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.getList();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.drawClusterPoint([], {}, 'digReservoir1Layer');
  },
  methods: {
    async getDeviceType() {
      let res = await getTeamCountList();
      if (res.code == 0) {
        this.maxExistingId = res.data.maxExistingId
        this.minExistingId = res.data.minExistingId
        this.detail = res.data;
      }
    },
    async getList() {
      let res = await getTeamList({
        pageNum: 1,
        pageSize: 9999
      })
      if (res.code == 0) {
        this.reservoiList = res.data.list
      }
    },
    // 地图落点
    onPointClick(btnType) {
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: require('@/assets/images/legend/shuiku-juhe.png'),
        srcs: require('@/assets/images/dianwei-juhe.png')
      };
      let tempList = []
      switch (btnType) {
        case 'max':
          tempList = [this.reservoiList.find(i => i.id === this.maxExistingId)]
          break;
        case 'min':
          tempList = [this.reservoiList.find(i => i.id === this.minExistingId)]
          break;
        default:
          tempList = this.reservoiList
          break;
      }
      let pointList = tempList.map((point) => {
        let imgSrc = require('@/assets/images/legend/shuiku-juhe.png');
        return {
          name: point.name,
          type: '救援队伍',
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.id,
          src: imgSrc,
          props: { 现有人数: `${point.existingNumber}人`, 联系方式: `${point.chargePhone}` }
        };
      });
      this.map.drawClusterPoint(pointList, style, 'digReservoir1Layer');
    },
  }
};
</script>
<style lang="scss" scoped>
.stagnant-water-count {
  .has-count {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 100%;
    .has-count-item {
      & > p {
        font-size: 24px;
        margin-right: 10px;
      }
      .num {
        font-size: 28px;
        color: #1df8f3;
      }
      .unit {
        color: #fff;
        font-size: 18px;
      }
      .cut-off-rule {
        height: 2.2rem;
        margin: 0 0.4rem;
        border-right: 2px solid #fff;
      }
      .water-resource-sty {
        margin-top: 10px;
        line-height: 13px;
        > span:nth-child(1) {
          font-size: 14px;
          margin: 0 3px;
        }
        > span:nth-child(2) {
          padding: 2px 5px;
          font-size: 14px;
          color: #1df8f3;
        }
        > span:nth-child(3) {
          font-size: 14px;
          color: #fff;
        }
      }
    }
  }
}
</style>
