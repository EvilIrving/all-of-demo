<template>
  <div class="flow-monitor-card">
    <page-pane title="流量监控" @onTitle="tclick('流量监控')">
      <template slot="append">
        <i
          :class="[pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1']"
          @click="onPointClick"
        />
        <!-- <i class="icon-liebiao" /> -->
      </template>
      <div ref="typhoonTable" style="height: 100%; margin-top: 0.5rem">
        <el-table
          v-loading="flowDataLoading"
          :data="flowData" stripe
          :cell-style="{fontSize: '15px'}"
          height="calc(100% - 0.5rem)"
          :header-cell-style="{color:'#1cfff5',fontSize: '16px'}"
        >
          <el-table-column label="序号" type="index" width="60" />
          <el-table-column label="站点名称" prop="stName" width="80" />
          <el-table-column label="流量" prop="flow" width="60" />
          <el-table-column :show-overflow-tooltip="true" label="更新时间" prop="time">
            <template slot-scope="scope">
              {{scope.row.time ? (new Date(scope.row.time).format("yyyy-MM-dd hh:mm")):'-'}}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </page-pane>
  </div>
</template>

<script>
import { getWaterFlowList } from "@/api/flow.js";
import { PagePane, PagePaneItem } from "../components/PagePane";
import { mapGetters,mapActions } from 'vuex';
export default {
  name: "FlowMonitorCard",
  layerName: "flowLayer",
  legendName: "流量监控",
  components: { PagePane, PagePaneItem },
  data() {
    return {
      pointActive: false,
      flowDataLoading: false,
      flowData: [],
      timer:null,
    };
  },
  mounted() {
    this.getFlowList();
    this.timer = setInterval(()=>{
      this.getFlowList();
    },300000);
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  computed: {
    ...mapGetters([]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  methods: {
    ...mapActions(["SetActiveCount"]),
    getFlowList() {
      this.flowDataLoading = true;
      getWaterFlowList().then((res) => {
        if (res.code == 0) {
          this.flowData = res.data;
        }
        this.flowDataLoading = false;
      });
    },
    tclick(title){
      this.SetActiveCount(title);
    },
    onPointClick() {
      this.pointActive = !this.pointActive;
      if (this.pointActive) {
        let style = {
          scale: 0.7,
          anchor: [24, 16],
          anchorXUnits: "pixels",
          src: require("@/assets/images/MapPoint/flow-normal.png"),
        };
        let imgSrc = require("@/assets/images/MapPoint/flow-normal.png");
        let pointList = this.flowData.map((item) => {
          if(item.flowOverWarn){
            imgSrc = require("@/assets/images/MapPoint/flow-abnormal.png");
          }
          return {
            name: item.stName,
            lng: item.stLong,
            lat: item.stLat,
            type: "流量",
            src: imgSrc,
            code: item.stCode,
            props: { 流量: `${item.flow || "-"}m³/s` },
          }
        });
        this.map.drawPoint(pointList, style, "flowLayer");
        this.$emit("legend-change", "流量监控", {
          label: "流量监控",
          children: [
            {
              label: "正常",
              icon: "icon-flow-legend-normal",
            },
            {
              label: "超警",
              icon: "icon-flow-legend-abnormal",
            },
          ],
        });
      } else {
        this.map.drawPoint([], {}, "flowLayer");
        this.$emit("legend-change", "流量监控", {});
      }
    },
  },
};
</script>

<style lang="scss">
  .flow-monitor-card {
    height: 100%;
  }
</style>