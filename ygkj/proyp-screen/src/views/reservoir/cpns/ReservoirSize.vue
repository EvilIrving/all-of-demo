<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-03-07 17:50:00
 * @Description: 大中型水库
-->
<template>
  <ModuleLayout title="大中型水库" disabled>
    <div class="reservoir-size">
      <div class="item" :class="{ active: reservoirWarnType === '大中型水库-超设计洪水位' }" @click="clickItem('大中型水库-超设计洪水位')">
        <img src="@/assets/images/reservoir_icon_red.png" />
        <div class="panel">
          <div class="label">超设计</div>
          <div class="label">洪水位</div>
          <div class="data">
            <span class="value red">{{ value1 }}</span>
            <span class="unit">座</span>
          </div>
        </div>
      </div>
      <div class="item" :class="{ active: reservoirWarnType === '大中型水库-超正常水位' }" @click="clickItem('大中型水库-超正常水位')">
        <img src="@/assets/images/reservoir_icon_yellow.png" />
        <div class="panel">
          <div class="label">超正常</div>
          <div class="label">水位</div>
          <div class="data">
            <span class="value yellow">{{ value2 }}</span>
            <span class="unit">座</span>
          </div>
        </div>
      </div>
      <div class="item" :class="{ active: reservoirWarnType === '大中型水库-超限制水位' }" @click="clickItem('大中型水库-超限制水位')">
        <img src="@/assets/images/reservoir_icon_yellow.png" />
        <div class="panel">
          <div class="label">超限制</div>
          <div class="label">水位</div>
          <div class="data">
            <span class="value yellow">{{ value3 }}</span>
            <span class="unit">座</span>
          </div>
        </div>
      </div>
    </div>
    <MapLegend v-if="
        reservoirWarnType === '大中型水库-超设计洪水位' ||
        reservoirWarnType === '大中型水库-超限制水位' ||
        reservoirWarnType === '大中型水库-超正常水位'
      ">
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList" @change="changeCheckList">
        <el-checkbox :label="1"><img src="@/assets/images/map_points/reservoir-lv3-red.png" /><span>超设计</span></el-checkbox>
        <el-checkbox :label="2"><img src="@/assets/images/map_points/reservoir-lv3-normal.png" /><span>超正常</span></el-checkbox>
        <el-checkbox :label="2"><img src="@/assets/images/map_points/reservoir-lv3-orange.png" /><span>超限制</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import {
  largeMediumReservoirsStatisticApi,
  largeMediumReservoirsListApi,
} from "api/api_reservoir.js";
export default {
  name: "ReservoirScale",
  props: {
    map: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      value1: 0,
      value2: 0,
      value3: 0,
      pointsInfo: [],
      checkList: [1, 2, 3],
    };
  },
  computed: {
    // 行政区划code
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
    // 风险研判中选择的类型
    reservoirRiskType() {
      return this.$store.state.reservoir.reservoirRiskType;
    },
    // 大中型水库中选择的类型
    reservoirWarnType() {
      return this.$store.state.reservoir.reservoirWarnType;
    },
  },
  watch: {
    selectAdcd() {
      // 行政区划code改变时，重新统计数据
      this.getReservoirStatisticData();
      // 行政区划code改变时，若需要打点则重新获取点位数据
      this.handleReservoirPoints();
    },
    reservoirRiskType() {
      // 风险研判类型改变时，重新统计数据
      this.getReservoirStatisticData();
      // 风险研判类型改变时，若需要打点则重新获取点位数据
      this.handleReservoirPoints();
    },
    reservoirWarnType() {
      // 选中时绘制落点，未选中时移除落点
      this.handleReservoirPoints();
    },
  },
  created() {
    this.getReservoirStatisticData();
  },
  mounted() { },
  methods: {
    /**
     * @description 改变选中项
     */
    clickItem(item) {
      this.$store.commit("setReservoirWarnType", item);
    },
    /**
     * @description 获取统计数据
     */
    getReservoirStatisticData() {
      let riskType = null;
      switch (this.reservoirRiskType) {
        case "加固中水库":
          riskType = "processCnt";
          break;
        case "一类坝水库":
          riskType = "firstClass";
          break;
        case "未加固二类坝":
          riskType = "secondClass";
          break;
        case "未加固三类坝":
          riskType = "thirdClass";
          break;
        default:
          break;
      }
      largeMediumReservoirsStatisticApi(this.selectAdcd, riskType).then(
        ({ data }) => {
          let statisticData = {};
          if (data && data.length > 0) statisticData = data[0];
          this.value1 = statisticData.DSFLZ ?? "-";
          this.value2 = statisticData.normal ?? "-";
          this.value3 = statisticData.limitWaterLevel ?? "-";
        }
      );
    },
    /**
     * @description 获取水库点位信息
     */
    async getReservoirListData() {
      let warnType = null;
      switch (this.reservoirWarnType) {
        case "大中型水库-超设计洪水位":
          warnType = "DSFLZ";
          break;
        case "大中型水库-超正常水位":
          warnType = "normal";
          break;
        case "大中型水库-超限制水位":
          warnType = "limitWaterLevel";
          break;
        default:
          break;
      }
      let riskType = null;
      switch (this.reservoirRiskType) {
        case "加固中水库":
          riskType = "processCnt";
          break;
        case "一类坝水库":
          riskType = "firstClass";
          break;
        case "未加固二类坝":
          riskType = "secondClass";
          break;
        case "未加固三类坝":
          riskType = "thirdClass";
          break;
        default:
          break;
      }
      await largeMediumReservoirsListApi(
        this.selectAdcd,
        warnType,
        riskType
      ).then(({ data }) => {
        this.pointsInfo = data || [];
      });
    },
    /**
     * @description 判断地图落点/移除落点
     */
    handleReservoirPoints() {
      if (
        this.reservoirWarnType === "大中型水库-超设计洪水位" ||
        this.reservoirWarnType === "大中型水库-超限制水位" ||
        this.reservoirWarnType === "大中型水库-超正常水位"
      ) {
        this.drawReservoirPoints();
      } else {
        this.removeReservoirPoints();
      }
    },
    /**
     * @description 绘制水库点位
     */
    async drawReservoirPoints() {
      if (this.map) {
        await this.getReservoirListData();
        const points = this.pointsInfo.map((e) => {
          let src;
          if (e.waterLevel ?? 0 > e.DSFLZ) {
            src = require(`@/assets/images/map_points/reservoir-lv${e.project_scale}-red.png`);
            e.mapLegendFilterFlag = 1;
          } else if (e.waterLevel ?? 0 > e.limitWaterLevel) {
            src = require(`@/assets/images/map_points/reservoir-lv${e.project_scale}-orange.png`);
            e.mapLegendFilterFlag = 2;
          } else {
            src = require(`@/assets/images/map_points/reservoir-lv${e.project_scale}-green.png`);
            e.mapLegendFilterFlag = 0;
          }

          return {
            latitude: e.LTTD,
            longitude: e.LGTD,
            styleObj: {
              src,
              scale: 0.75,
            },
            data: e,
          };
        });
        this.pointsInfo = points
        this.$olMapUtil.drawPoint(this.map, this.pointsInfo, "大中型水库落点");
      }
    },
    /**
     * @description 移除水库点位
     */
    removeReservoirPoints() {
      this.$olMapUtil.removeLayer(this.map, "大中型水库落点");
    },
    /**
     * @description 图例筛选
     */
    changeCheckList() {
      this.$olMapUtil.drawPoint(
        this.map,
        this.pointsInfo.filter((e) =>
          this.checkList.includes(e.mapLegendFilterFlag)
        ),
        "大中型水库落点"
      );
    },
  },
};
</script>
<style lang="scss" scoped>
.reservoir-size {
  display: flex;
  width: 100%;
  height: 100%;
  padding: 0 22px;
  .item {
    display: flex;
    width: 50%;
    margin-top: 10px;
    padding-top: 6px;
    padding-left: 10px;
    cursor: pointer;
    &.active {
      background-image: url("~@/assets/images/item-active-border-208_79.png");
      background-size: 100% 100%;
    }
    > img {
      width: 67px;
      height: 66px;
    }
    .panel {
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      flex: 1;
      height: 66px;
      margin-left: 10px;
      .label {
        font-family: "fzzyjw";
      }
      .data {
        .value {
          font-family: "digitalnarrow";
          font-size: 30px;
          &.red {
            color: red;
          }
          &.yellow {
            color: yellow;
          }
        }
        .unit {
          font-family: "fzzyjw";
          font-size: 14px;
          opacity: 0.75;
          margin-left: 10px;
        }
      }
    }
  }
}
</style>