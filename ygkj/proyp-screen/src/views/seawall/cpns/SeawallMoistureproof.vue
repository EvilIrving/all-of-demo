<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-20 17:06:30
 * @Description: 海塘防潮分析
-->
<template>
  <ModuleLayout title="防潮分析" disabled>
    <div slot="extra">
      <el-select class="module-extra-select" v-model="value" placeholder="请选择" @change="changeValue">
        <el-option v-for="item in options" :key="item.id" :label="item.planName" :value="item.id"></el-option>
      </el-select>
    </div>
    <div class='seawall-moistureproof'>
      <div class="item" @click="clickItem('防潮分析-超设计潮位')" :class="{active: selectModuleName === '防潮分析-超设计潮位'}">
        <div class="data">
          <span class="value yellow">{{value1}}</span>
          <span class="unit">个</span>
        </div>
        <img src="@/assets/images/moisture-proof-analysis-icon1.png">
        <div class="label">超设计潮位</div>
      </div>
      <div class="item" @click="clickItem('防潮分析-超病险塘能力')" :class="{active: selectModuleName === '防潮分析-超病险塘能力'}">
        <div class="data">
          <span class="value yellow">{{value2}}</span>
          <span class="unit">个</span>
        </div>
        <img src="@/assets/images/moisture-proof-analysis-icon2.png">
        <div class="label">超病险塘能力</div>
      </div>
      <div class="item" @click="clickItem('防潮分析-超塘顶高程')" :class="{active: selectModuleName === '防潮分析-超塘顶高程'}">
        <div class="data">
          <span class="value red">{{value3}}</span>
          <span class="unit">个</span>
        </div>
        <img src="@/assets/images/moisture-proof-analysis-icon3.png">
        <div class="label">超塘顶高程</div>
      </div>
    </div>
    <div class="point-popup" ref="seawallMoistureproofPointPopup" v-show="clickFeatVal && clickFeatVal.layerName === '防潮分析落点'">
      <div class="name">{{clickFeatVal?.data?.NAME??'-'}}</div>
      <div class="item"><span class="label">安全状态：</span><span class="value">{{clickFeatVal?.data?.status??'-'}}</span></div>
      <div class="item"><span class="label">设计潮位：</span><span class="value">{{clickFeatVal?.data?.DESIGN_TIDE_LEVEL??'-'}}m</span></div>
      <div class="item"><span class="label">设计标准：</span><span class="value">{{clickFeatVal?.data?.DESIGN_TIDE_STANDARD??'-'}}年</span></div>
      <div class="item"><span class="label">塘顶最低高程：</span><span class="value">{{clickFeatVal?.data?.LOWEST_ELEVATION??'-'}}m</span></div>
      <div class="item"><span class="label">最高潮位：</span><span class="value">{{clickFeatVal?.data?.forecast??'-'}}m</span></div>
      <div class="item"><span class="label">出现时间：</span><span class="value">{{clickFeatVal?.data?.forecast_time??'-'}}</span></div>
    </div>
    <MapLegend v-show="selectModuleName === '防潮分析-超设计潮位' || selectModuleName === '防潮分析-超病险塘能力' || selectModuleName === '防潮分析-超塘顶高程'">
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList">
        <el-checkbox :label="1" @change="changeCheckList(1)"><img src="@/assets/images/map_points/seawall-1.png" /><span>超设计潮位</span></el-checkbox>
        <el-checkbox :label="2" @change="changeCheckList(2)"><img src="@/assets/images/map_points/seawall-2.png" /><span>超病险塘能力</span></el-checkbox>
        <el-checkbox :label="3" @change="changeCheckList(3)"><img src="@/assets/images/map_points/seawall-3.png" /><span>超塘顶高程</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
// import SeawallPopup from "../popups/SeawallPopup.vue";
import {
  getPlanApi,
  moistureproofAnalysisStatisticApi,
  moistureproofAnalysisOverCntMapListApi,
  moistureproofAnalysisSickCntMapListApi,
  moistureproofAnalysisOverTopMapListApi,
} from "api/api_seawall.js";
export default {
  name: "SeawallMoistureproof",
  props: {
    map: {
      type: Object,
      default: null,
    },
  },
  components: {
    // SeawallPopup,
  },
  data() {
    return {
      value: "",
      options: [],
      value1: 0,
      value2: 0,
      value3: 0,
      pointsInfo: [],
      checkList: [],
    };
  },
  computed: {
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
    selectModuleName() {
      return this.$store.state.selectModuleName;
    },
    clickFeatVal() {
      return this.$store.state.map.clickFeatVal;
    },
  },
  watch: {
    selectAdcd() {
      this.getStatisticData();
    },
    selectModuleName(value) {
      if (value === "防潮分析-超设计潮位") {
        this.checkList = [1];
      } else if (value === "防潮分析-超病险塘能力") {
        this.checkList = [2];
      } else if (value === "防潮分析-超塘顶高程") {
        this.checkList = [3];
      }
      if (
        value === "防潮分析-超设计潮位" ||
        value === "防潮分析-超病险塘能力" ||
        value === "防潮分析-超塘顶高程"
      ) {
        this.drawSeawallPoints();
      } else {
        this.removeSeawallPoints();
      }
    },
    clickFeatVal(value) {
      if (value && value.layerName && value.layerName === "防潮分析落点") {
        console.log(value);
        this.$olMapUtil.flyPoint(
          this.map,
          [Number(value.data.longitude), Number(value.data.latitude) + 0.005],
          15
        );
        this.$olMapUtil.drawHoverFrame(
          this.map,
          value.data || {},
          this.$refs.seawallMoistureproofPointPopup,
          45,
          "防潮分析落点浮窗"
        );
      } else {
        this.$olMapUtil.removeHoverFrame(this.map, "防潮分析落点浮窗");
      }
    },
  },
  created() {
    this.getOptions();
  },
  mounted() {},
  destroyed() {},
  methods: {
    clickItem(item) {
      this.$store.commit("setModuleName", item);
    },
    async drawSeawallPoints() {
      if (this.map) {
        await this.getPointsInfo();
        this.$olMapUtil.drawPoint(this.map, this.pointsInfo, "防潮分析落点");
      }
    },
    removeSeawallPoints() {
      this.$olMapUtil.removeLayer(this.map, "防潮分析落点");
    },
    getOptions() {
      getPlanApi({
        fetchAll: true,
        planName: "综合",
      })
        .then(({ rows }) => {
          this.options = rows || [];
          if (this.options.length > 0) this.value = this.options[0].id ?? "";
        })
        .finally(() => {
          this.getStatisticData();
        });
    },
    getStatisticData() {
      moistureproofAnalysisStatisticApi(this.value, this.selectAdcd).then(
        ({ data = [] }) => {
          let statisticData = {};
          if (data && data.length > 0) statisticData = data[0];
          this.value1 = statisticData.overCnt ?? "-";
          this.value2 = statisticData.sickCnt ?? "-";
          this.value3 = statisticData.overTop ?? "-";
        }
      );
    },
    changeValue() {
      this.getStatisticData();
    },
    async getPointsInfo() {
      this.removeSeawallPoints();
      if (this.selectModuleName === "防潮分析-超设计潮位") {
        await moistureproofAnalysisOverCntMapListApi(
          this.value,
          this.selectAdcd
        ).then(({ data }) => {
          this.pointsInfo = (data || []).map((e) => {
            e.longitude = e.LGTD;
            e.latitude = e.LTTD;
            e.styleObj = {
              src: require("images/map_points/seawall-1.png"),
              scale: 0.75,
            };
            e.mapLegendFilterFlag = 1;
            return e;
          });
        });
      } else if (this.selectModuleName === "防潮分析-超病险塘能力") {
        await moistureproofAnalysisSickCntMapListApi(
          this.value,
          this.selectAdcd
        ).then(({ data }) => {
          this.pointsInfo = (data || []).map((e) => {
            e.longitude = e.LGTD;
            e.latitude = e.LTTD;
            e.styleObj = {
              src: require("images/map_points/seawall-2.png"),
              scale: 0.75,
            };
            e.mapLegendFilterFlag = 2;
            return e;
          });
        });
      } else if (this.selectModuleName === "防潮分析-超塘顶高程") {
        await moistureproofAnalysisOverTopMapListApi(
          this.value,
          this.selectAdcd
        ).then(({ data }) => {
          this.pointsInfo = (data || []).map((e) => {
            e.longitude = e.LGTD;
            e.latitude = e.LTTD;
            e.styleObj = {
              src: require("images/map_points/seawall-3.png"),
              scale: 0.75,
            };

            e.mapLegendFilterFlag = 3;
            return e;
          });
        });
      }
    },
    changeCheckList(value) {
      if (value === 1) {
        this.clickItem("防潮分析-超设计潮位");
      } else if (value === 2) {
        this.clickItem("防潮分析-超病险塘能力");
      } else if (value === 3) {
        this.clickItem("防潮分析-超塘顶高程");
      }
    },
  },
};
</script>
<style lang='scss' scoped>
.seawall-moistureproof {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  padding: 13px 18px 0;
  .item {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 121px;
    height: 153px;
    cursor: pointer;
    &.active {
      background-image: url("~@/assets/images/item-active-border-121_153.png");
    }
    .data {
      .value {
        font-size: 34px;
        font-family: "agencyb";
        &.yellow {
          color: yellow;
        }
        &.red {
          color: red;
        }
      }
      .unit {
        font-size: 18px;
        font-family: "fzzyjw";
        color: #7890bf;
      }
    }
    > img {
      margin-top: -8px;
    }
    .label {
      font-family: "fzzyjw";
      font-size: 18px;
      color: #eaf6ff;
    }
  }
}
.point-popup {
  position: relative;
  z-index: 99;
  padding: 15px;
  background-image: linear-gradient(
      0deg,
      rgba(0, 26, 85, 0.3) 0%,
      rgba(8, 77, 152, 0.3) 0%,
      rgba(16, 127, 219, 0.3) 0%,
      rgba(17, 89, 171, 0.3) 0%,
      rgba(18, 50, 122, 0.3) 0%,
      rgba(16, 127, 219, 0.3) 100%
    ),
    linear-gradient(#00184d, #00184d);
  border-radius: 6px;
  .name {
    font-size: 18px;
  }
  .item {
    margin-top: 5px;
    .label {
      font-size: 16px;
    }
    .value {
      font-size: 18px;
    }
  }
}
</style>