<template>
  <div class="flow-monitor-card">
    <page-pane title="避灾点">
      <template #append>
        <i :class="[pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1']" @click="onPointClick" />
      </template>
      <div ref="typhoonTable" style="height: 100%; margin-top: 0.5rem">
        <el-table
          :data="flowData"
          stripe
          :cell-style="{ fontSize: '15px' }"
          height="calc(100% - 0.5rem)"
          :header-cell-style="{ color: '#00BAFF', fontSize: '16px' }"
        >
          <el-table-column label="序号" type="index" width="60"></el-table-column>
          <el-table-column label="避灾点名称" prop="pointName"></el-table-column>
          <el-table-column label="可容纳人数" prop="flow">
            <template slot-scope="scope">
              {{ scope.row.pointCapacity || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </page-pane>
  </div>
</template>

<script>
import { getWaterFlowList } from '@/api/flow.js';
import { PagePane, PagePaneItem } from '../components/PagePane';
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'FlowMonitorCard',
  layerName: 'flowLayer',
  legendName: '流量监控',
  components: { PagePane, PagePaneItem },
  data() {
    return {
      pointActive: false,
      flowDataLoading: false,
      flowData: [],
      timer: null
    };
  },
  mounted() {
    this.getFlowList();
    this.timer = setInterval(() => {
      this.getFlowList();
    }, 300000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.map.drawClusterPoint([], {}, 'flowLayer');
  },
  computed: {
    ...mapGetters([]),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  methods: {
    ...mapActions(['SetActiveCount']),
    getFlowList() {
      this.flowDataLoading = true;
      getWaterFlowList({
        pageNum: 1,
        pageSize: 99999,
      }).then((res) => {
        if (res.code == 0) {
          this.flowData = res.data.list;
        }
        this.flowDataLoading = false;
      });
    },
    tclick(title) {
      this.SetActiveCount(title);
    },
    onPointClick() {
      this.pointActive = !this.pointActive;
      if (this.pointActive) {
        let style = {
          scale: 1,
          anchor: [24, 24],
          anchorXUnits: 'pixels',
          src: require('@/assets/new_images/point/icon-disaster.png'),
          srcs: require('@/assets/new_images/point/icon-disaster-juhe.png')
        };
        let pointList = this.flowData.map((item) => {
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
        this.map.drawClusterPoint(pointList, style, 'flowLayer');
      } else {
        this.map.drawClusterPoint([], {}, 'flowLayer');
      }
    }
  }
};
</script>

<style lang="scss">
.flow-monitor-card {
  height: 100%;
}
</style>