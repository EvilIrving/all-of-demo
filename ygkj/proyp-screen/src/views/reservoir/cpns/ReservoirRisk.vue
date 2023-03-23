<template>
  <ModuleLayout title="风险研判" disabled>
    <div class="risk-type-box">
      <div class="risk-type-flex">
        <div
          :class="[
            setClassName(item) ? 'risk-type-long' : 'risk-type-short',
            activeRiskType === item.label ? activeClassName : '',
          ]"
          v-for="(item, index) in riskTypeList"
          :key="index"
          @click="checkRiskType(item)"
        >
          <div>{{ item.label }}</div>
          <span>{{ item.value }}</span>
        </div>
      </div>
    </div>
    <MapLegend v-if="reservoirRiskType && !reservoirWarnType">
      <el-checkbox-group
        class="map-legend-checkbox"
        v-model="checkList"
        @change="changeCheckList"
      >
        <el-checkbox :label="1"
          ><img src="@/assets/images/map_points/reservoir-lv1-green.png" /><span
            >大（I）型水库</span
          ></el-checkbox
        >
        <el-checkbox :label="2"
          ><img src="@/assets/images/map_points/reservoir-lv2-green.png" /><span
            >大（II）型水库</span
          ></el-checkbox
        >
        <el-checkbox :label="3"
          ><img src="@/assets/images/map_points/reservoir-lv3-green.png" /><span
            >中型水库</span
          ></el-checkbox
        >
        <el-checkbox :label="4"
          ><img src="@/assets/images/map_points/reservoir-lv4-green.png" /><span
            >小（I）型水库</span
          ></el-checkbox
        >
        <el-checkbox :label="5"
          ><img src="@/assets/images/map_points/reservoir-lv5-green.png" /><span
            >小（II）型水库</span
          ></el-checkbox
        >
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "ReservoirRisk",
  props: {
    map: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      checkList: [1, 2, 3, 4, 5],
      pointsList: [],
      activeRiskType: "",
      riskTypeList: [
        {
          label: "全部水库",
          value: 0,
        },
        {
          label: "加固中水库",
          value: 0,
        },
        {
          label: "一类坝水库",
          value: 0,
        },
        {
          label: "未加固二类坝",
          value: 0,
        },
        {
          label: "未加固三类坝",
          value: 0,
        },
      ],
    };
  },
  computed: {
    activeClassName() {
      if (this.activeRiskType) {
        let className = "";
        switch (this.activeRiskType) {
          case "加固中水库":
            className = "active-long";
            break;
          case "全部水库":
            className = "active-long";
            break;
          case "一类坝水库":
            className = "active-short";
            break;
          case "未加固二类坝":
            className = "active-short";
            break;
          default:
            className = "active-short";
            break;
        }
        return className;
      } else {
        return "";
      }
    },
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
    reservoirRiskType() {
      return this.$store.state.reservoir.reservoirRiskType;
    },
    reservoirWarnType() {
      return this.$store.state.reservoir.reservoirWarnType;
    },
  },
  watch: {
    selectAdcd() {
      this.getRightSelectList();
      if (this.activeRiskType) {
        const data = this.riskTypeList.find(
          (item) => item.label === this.activeRiskType
        );
        this.getMapPointList(data);
      }
    },
    reservoirRiskType(val) {
      if (!val) {
        this.removeDangerPoint();
        this.activeRiskType = "";
      }
    },
    reservoirWarnType(val) {
      if (val) {
        this.removeDangerPoint();
      } else {
        if (this.activeRiskType) {
          const data = this.riskTypeList.find(
            (item) => item.label === this.activeRiskType
          );
          this.getMapPointList(data);
        }
      }
    },
  },
  mounted() {
    this.getRightSelectList();
  },
  methods: {
    async getRightSelectList() {
      let opt = {
        statistic: "mapTabs",
        adcd: this.selectAdcd,
      };
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        this.riskTypeList = [
          {
            label: "全部水库",
            value: res.data[0].totalCnt ? res.data[0].totalCnt : 0,
            typeCategory: "",
            typeValue: "",
          },
          {
            label: "加固中水库",
            value: res.data[0].processCnt ? res.data[0].processCnt : 0,
            typeCategory: "processCnt",
            typeValue: "三类坝",
          },
          {
            label: "一类坝水库",
            value: res.data[0].firstClass ? res.data[0].firstClass : 0,
            typeCategory: "firstClass",
            typeValue: "一类坝",
          },
          {
            label: "未加固二类坝",
            value: res.data[0].secondClass ? res.data[0].secondClass : 0,
            typeCategory: "secondClass",
            typeValue: "二类坝",
          },
          {
            label: "未加固三类坝",
            value: res.data[0].thirdClass ? res.data[0].thirdClass : 0,
            typeCategory: "thirdClass",
            typeValue: "三类坝",
          },
        ];
      }
    },
    setClassName(item) {
      if (item.label === "加固中水库" || item.label === "全部水库") {
        return true;
      } else {
        return false;
      }
    },
    checkRiskType(item) {
      if (this.activeRiskType !== item.label) {
        this.activeRiskType = item.label;
        this.$store.commit("setReservoirRiskType", this.activeRiskType);
        if (!this.reservoirWarnType) {
          this.getMapPointList(item);
        }
      } else {
        this.$store.commit("setReservoirRiskType", "");
      }
    },
    //设置水库不同状态的点位图标
    setPointIcon(data) {
      if (data.waterLevel > data.DSFLZ) {
        return require(`@/assets/images/map_points/reservoir-lv${data.project_scale}-red.png`);
      } else if (data.waterLevel > data.limitWaterLevel) {
        return require(`@/assets/images/map_points/reservoir-lv${data.project_scale}-orange.png`);
      } else {
        return require(`@/assets/images/map_points/reservoir-lv${data.project_scale}-green.png`);
      }
    },
    //点位图例
    changeCheckList() {
      this.$olMapUtil.drawPoint(
        this.map,
        this.pointsList.filter((e) =>
          this.checkList.includes(Number(e.data.project_scale))
        ),
        "水库风险研判落点"
      );
    },
    //获取水库点位
    async getMapPointList(data) {
      let res = await reservoirLeftStatisticApi({
        statistic: "mapList",
        adcd: this.selectAdcd,
        itemCategory: data.typeCategory,
        itemValue: data.typeValue,
      });
      if (res.success) {
        this.pointsList = res.data.map((item) => {
          return {
            longitude: item.LGTD,
            latitude: item.LTTD,
            styleObj: {
              src: require(`@/assets/images/map_points/reservoir-lv${item.project_scale}-green.png`),
              scale: 0.7,
            },
            data: item,
          };
        });
        this.$olMapUtil.drawPoint(
          this.map,
          this.pointsList,
          "水库风险研判落点"
        );
        this.checkList = [1, 2, 3, 4, 5];
      }
    },
    //移除图层
    removeDangerPoint() {
      this.$olMapUtil.removeLayer(this.map, "水库风险研判落点");
    },
  },
};
</script>

<style lang="scss" scoped>
.risk-type-box {
  padding: 15px 10px 0 20px;
  .risk-type-flex {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    > div {
      cursor: pointer;
      text-align: center;
      overflow: hidden;
      color: #b1dcff;
      font-size: 18px;
      > span {
        font-family: "agencyb";
        color: #74dbff;
        font-size: 30px;
      }
    }
  }
  .risk-type-long {
    width: 207px;
    height: 74px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background: url("../../../assets/images/reservoir-risk-long.png") no-repeat;
    &.active-long {
      &::before {
        content: "";
        position: absolute;
        width: 223px;
        height: 91px;
        left: -8px;
        top: -9px;
        background: url("../../../assets/images/reservoir-risk-long-active.png")
          no-repeat;
      }
    }
    > span {
      padding-left: 20px;
    }
  }
  .risk-type-short {
    margin-top: 10px;
    width: 135px;
    height: 74px;
    position: relative;
    box-sizing: border-box;
    padding-top: 10px;
    background: url("../../../assets/images/reservoir-risk-short.png") no-repeat;
    &.active-short {
      &::before {
        content: "";
        position: absolute;
        left: -8px;
        top: -9px;
        width: 152px;
        height: 91px;
        background: url("../../../assets/images/reservoir-risk-short-active.png")
          no-repeat;
      }
    }
    > div {
      padding-bottom: 5px;
    }
  }
}
</style>