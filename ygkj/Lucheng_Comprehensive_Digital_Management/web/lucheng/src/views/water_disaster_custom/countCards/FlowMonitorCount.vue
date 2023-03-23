<!--
 * @Author: your name
 * @Date: 2021-07-15 09:04:20
 * @LastEditTime: 2021-12-07 10:46:01
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\FlowMonitorCount.vue
-->
<template>
  <count-wrap title="避灾点" class="flow-monitor-count">
    <el-row class="has-count">
      <el-col :span="8" class="fs24">
        <p>避灾点可容纳人数</p>
        <p>
          <span class="orange fs36">{{ statData.sumCapacity || '-' }}</span>人
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>{{ statData.maxPoint || '-' }}(最多)</p>
        <p>
          <span @click="onPointClick('max')" class="yellow fs36" style="cursor: pointer">{{ statData.maxCapacity || '-' }}</span>人
        </p>
      </el-col>
      <el-col :span="8" class="fs24">
        <p>{{ statData.minPoint || '-' }}(最少)</p>
        <p>
          <span @click="onPointClick('min')" style="cursor: pointer" class="orange fs36">{{ statData.minCapacity || '-' }}</span>人
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import { getWaterFlowStat, getWaterFlowList } from "@/api/flow.js";
import CountWrap from "../components/CountWrap.vue";
export default {
  name: "FlowMonitorCount",
  layerName: 'flowLayer',
  legendName: '流量监控',
  data() {
    return {
      statData: {},
      warnList: [],
      maxList: [],
      warnFlag: false,
      maxFlag: false,
      timer: null,
      pointActive: false,
      maxPointId: 0,
      maxPoint: [],
      minPointId: 0,
      minPoint: [],
      flowDataLoading: false,

    };
  },
  components: { CountWrap },
  computed: {
    overWarnNum() {
      return this.statData.overWarnNum;
    },
    maxFlow() {
      return this.statData.max?.flow || "-";
    },
    maxStName() {
      return this.statData.max?.stName || "-";
    },
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  mounted() {
    this.getWaterFlowStat();
    this.getFlowList();
    this.timer = setInterval(() => {
      this.getWaterFlowStat();
      this.getFlowList();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.drawClusterPoint([], {}, 'flowLayer');
  },
  methods: {
    getFlowList() {
      this.flowDataLoading = true;
      getWaterFlowList({
        pageNum: 1,
        pageSize: 99999,
      }).then((res) => {
        if (res.code == 0) {
          this.flowData = res.data.list;
          this.maxPoint = [res.data.list.find(i => i.id === this.maxPointId)]
          this.minPoint = [res.data.list.find(i => i.id === this.minPointId)]
        }
        this.flowDataLoading = false;
      });
    },
    getWaterFlowStat() {
      getWaterFlowStat().then((res) => {
        if (res.code === 0) {
          this.maxPointId = res.data.maxPointId
          this.minPointId = res.data.minPointId
          this.statData = res.data;
        }
      });
    },
    onPointClick(type) {
      this.map.drawClusterPoint([], {}, 'flowLayer');
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        src: require('@/assets/images/legend/icon-disaster.png'),
        srcs: require('@/assets/images/legend/icon-disaster-juhe.png')
      };
      let pointList
      if (type === 'max') {
        pointList = this.maxPoint.map((item) => {
          return {
            name: item.pointName,
            lng: item.longitude,
            lat: item.latitude,
            type: '避灾点',
            code: item.id,
            props: {
              可容纳人数: `${item.pointCapacity || '-'}人`,
              避灾点地址: `${item.pointAddress}`
            }
          };
        });
      } else {
        pointList = this.minPoint.map((item) => {
          return {
            name: item.pointName,
            lng: item.longitude,
            lat: item.latitude,
            type: '避灾点',
            code: item.id,
            props: {
              可容纳人数: `${item.pointCapacity || '-'}人`,
              避灾点地址: `${item.pointAddress}`
            }
          };
        });
      }

      this.map.drawClusterPoint(pointList, style, 'flowLayer');
    }
  },
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.flow-monitor-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 100%;
  }
}
</style>
