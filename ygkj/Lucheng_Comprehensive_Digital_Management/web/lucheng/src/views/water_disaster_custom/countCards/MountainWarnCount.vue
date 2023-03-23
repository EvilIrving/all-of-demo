<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:02:25
 * @LastEditTime: 2021-07-02 17:06:00
 * @LastEditors: Please set LastEditors
 * @Description: 山洪预警统计卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\MountainWarnCount.vue
-->
<template>
  <count-wrap title="山洪预警" class="mountain-warn-count">
    <!-- <div class="no-count" v-if="warnNum == 0">当前无预警</div> -->
    <el-row class="has-count">
      <el-col :span="8" :class="['fs24', typeSelect == 'monitorWarn' ? 'active' : '']">
        <p>当前影响村落</p>
        <p>
          <span class="yellow fs36" @click="doWarnPointClick('monitorWarn')">{{ monitorWarnNum }}</span
          >人
        </p>
      </el-col>
      <el-col :span="8" :class="['fs24', typeSelect == 'predictWarn' ? 'active' : '']">
        <p>未来影响村落</p>
        <p>
          <span class="yellow fs36" @click="doWarnPointClick('predictWarn')">{{ predictWarnNum }}</span
          >人
        </p>
      </el-col>
      <el-col :span="8" :class="['fs24', typeSelect == 'pastMonitorWarn' ? 'active' : '']">
        <p>过去24小时影响村落</p>
        <p>
          <span class="yellow fs36" @click="doWarnPointClick('pastMonitorWarn')">{{ pastMonitorWarnNum }}</span
          >个
        </p>
      </el-col>
      <!-- <template v-else>
        <el-col :span="8" class="fs24">
          <p>过去24小时影响村落</p>
          <p><span class="yellow fs36"> </span>人</p>
        </el-col>
        <el-col :span="8" class="fs24">
          <p>立即转移人口</p>
          <p><span class="orange fs36"> </span>人</p>
        </el-col>
      </template> -->
    </el-row>
  </count-wrap>
</template>

<script>
// import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";

import CountWrap from "../components/CountWrap.vue";
import { mapGetters } from "vuex";
export default {
  name: "MountainWarnCount",
  data() {
    return {
      warnNum: 1,
      flag: false,
      monitorWarnNum: "",//当前山洪预警数量
      monitorWarnList: [],
      pastMonitorWarnNum: "",//过去山洪预警数量
      pastMonitorWarnList: [],
      predictWarnNum: "",//未来山洪预警数量
      predictWarnList: [],
      typeSelect: "",
    };
  },
  computed: {
    ...mapGetters(["ScreenMap", "torrentsWarning", "MountainCardTab", "torrentsWarn"]),
    importvillage() {
      //  console.log(this.torrentsWarn.data.promptlyMoveVillage,1234)
      let res = Number(this.torrentsWarn.data.promptlyMoveVillage) + Number(this.torrentsWarn.data.warningMoveVillage)
      // console.log(res,567)
      return res
    }
  },

  components: { CountWrap },
  created() { },
  mounted() {
    this.getMountainTorrentCWindow();
  },
  beforeDestroy() {
    this.clearWarnPoint();
    this.$emit("legend-change", "山洪预警", {});
  },
  methods: {
    async getMountainTorrentCWindow() {
      let res = await disasterPreventionApi.getMountainTorrentCWindow();
      this.monitorWarnNum = res.data.monitorWarnNum;
      this.monitorWarnList = res.data.monitorWarnList;
      this.pastMonitorWarnNum = res.data.pastMonitorWarnNum;
      this.pastMonitorWarnList = res.data.pastMonitorWarnList;
      this.predictWarnNum = res.data.predictWarnNum;
      this.predictWarnList = res.data.predictWarnList;
    },
    doWarnPointClick(type) {
      this.clearWarnPoint();
      if (this.typeSelect == type) {
        this.typeSelect = "";
        this.$emit("legend-change", "山洪预警", {});
      } else {
        this.typeSelect = type;
        this.drawWarnPoint(this[`${type}List`], type);
      }
    },
    drawWarnPoint(arr, type) {
      const warnPointStyle = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/past-warn-mtflood-cluster.png"),
        src: require("@/assets/images/MapPoint/past-warn-mtflood.png")
      }
      const errorPointStyle = {
        scale: 0.7,
        anchor: [27, 30],
        distance: parseInt(10, 10),
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/past-error-mtflood-cluster.png"),
        src: require("@/assets/images/MapPoint/past-error-mtflood.png")
      }
      let warnPointList = [];
      let errorPointList = [];
      arr.forEach(item => {
        let obj = {
          name: item.stationName,
          type: "山洪预警",
          lng: Number(item.lon),
          lat: Number(item.lat),
          code: item.id,
          data: item,
          props: {
            村庄: (item.nvi),
            预警发生时间: (item[`${type}Value`].warnTm || "-"),
          }
        };
        if (item[`${type}Value`].warnGradeId == '4') {
          warnPointList.push(obj);
        } else {
          errorPointList.push(obj);
        }
      });
      this.ScreenMap.drawClusterPoint(warnPointList, warnPointStyle, 'warnPointLayer');
      this.ScreenMap.drawClusterPoint(errorPointList, errorPointStyle, 'errorPointStyleLayer');
      this.$emit("legend-change", "山洪预警", {
        label: "山洪预警",
        children: [
          {
            label: "准备转移/可能发生",
            icon: "icon-legend-mtflood-warn",
          },
          {
            label: "立即转移/可能性很大",
            icon: "icon-legend-mtflood-error",
          },
        ],
      });
    },
    clearWarnPoint() {
      this.ScreenMap.drawClusterPoint([], {}, "warnPointLayer");
      this.ScreenMap.drawClusterPoint([], {}, "errorPointStyleLayer");
    }
  },
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.mountain-warn-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 100%;
    padding: 8px;
    p {
      span {
        cursor: pointer;
      }
    }
    &.active {
      color: #1ef8f2;
    }
  }

  p {
    text-align: center;
  }
}
</style>
