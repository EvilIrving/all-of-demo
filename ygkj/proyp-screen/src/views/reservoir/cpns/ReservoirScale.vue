<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-14 11:44:05
 * @Description: 水库规模
-->
<template>
  <ModuleLayout title="水库规模" disabled>
    <div class="reservoir-scale-box">
      <div class="reservoir-scale-flex">
        <div :class="[
            setClassName(item.id),
            activeScaleType === item.id ? activeClassName : '',
          ]" v-for="item in scaleTypeList" :key="item.id" @click="checkScaleType(item)">
          <template v-if="item.id <= 2">
            <div>{{ item.label }}</div>
            <div>
              <span>{{ item.value }}</span>座
            </div>
          </template>
          <template v-else>
            <div>
              <span>{{ item.value }}</span>座
            </div>
            <div>{{ item.label }}</div>
          </template>
        </div>
      </div>
    </div>
    <MapLegend v-if="reservoirScale === '水库规模'">
      <el-checkbox-group class="map-legend-checkbox" v-model="checkList" @change="changeCheckList">
        <el-checkbox :label="1" v-if="activeScaleType == 1"><img src="@/assets/images/map_points/reservoir-lv1-green.png" /><span>大（I）型水库</span></el-checkbox>
        <el-checkbox :label="2" v-if="activeScaleType == 2"><img src="@/assets/images/map_points/reservoir-lv2-green.png" /><span>大（II）型水库</span></el-checkbox>
        <el-checkbox :label="3" v-if="activeScaleType == 3"><img src="@/assets/images/map_points/reservoir-lv3-green.png" /><span>中型水库</span></el-checkbox>
        <el-checkbox :label="4" v-if="activeScaleType == 4"><img src="@/assets/images/map_points/reservoir-lv4-green.png" /><span>小（I）型水库</span></el-checkbox>
        <el-checkbox :label="5" v-if="activeScaleType == 5"><img src="@/assets/images/map_points/reservoir-lv5-green.png" /><span>小（II）型水库</span></el-checkbox>
      </el-checkbox-group>
    </MapLegend>
  </ModuleLayout>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
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
      pointsList: [],
      checkList: [],
      activeScaleType: "",
      scaleTypeList: [
        {
          label: "大（I）型水库",
          value: "0",
          id: 1,
        },
        {
          label: "大（II）型水库",
          value: "0",
          id: 2,
        },
        {
          label: "中型水库",
          value: "0",
          id: 3,
        },
        {
          label: "小（I）型水库",
          value: "0",
          id: 4,
        },
        {
          label: "小（II）型水库",
          value: "0",
          id: 5,
        },
      ],
    };
  },
  computed: {
    activeClassName() {
      if (this.activeScaleType) {
        if (this.activeScaleType > 2) {
          return "active-short";
        } else {
          return "active-long";
        }
      } else {
        return "";
      }
    },
    selectAdcd() {
      return this.$store.state.selectAdcd;
    },
    reservoirScale() {
      return this.$store.state.reservoir.reservoirScale;
    },
  },
  watch: {
    selectAdcd() {
      this.getSkjbtjData();
      if (this.activeScaleType) {
        this.getMapPointList();
      }
    },
    reservoirScale(value) {
      if (value !== "水库规模") {
        this.activeScaleType = "";
        this.removeDangerPoint();
      }
    },
  },
  mounted() {
    this.getSkjbtjData();
  },
  methods: {
    //设置calssName
    setClassName(index) {
      if (index > 2) {
        return "reservoir-scale-short";
      } else {
        if (index === 1) {
          return "reservoir-scale-long reservoir-one";
        } else {
          return "reservoir-scale-long reservoir-two";
        }
      }
    },
    //点击水库规模
    checkScaleType(item) {
      if (this.activeScaleType !== item.id) {
        this.checkList = [item.id];
        this.activeScaleType = item.id;
        this.getMapPointList();
        console.log(item, 'item');
        this.$store.commit("setReservoirScale", { ...item, scale: "水库规模" });
      } else {
        this.activeScaleType = "";
        this.removeDangerPoint();
      }
    },
    // 水库规模
    async getSkjbtjData() {
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.selectAdcd,
      });
      if (res.success) {
        if (res.data.length > 0) {
          const dataArr = res.data.sort((a, b) => {
            return Number(a.project_scale) - Number(b.project_scale);
          });
          let arr = [];
          dataArr.forEach((item) => {
            const typeIndex = this.scaleTypeList.findIndex(
              (ite) => ite.id === Number(item.project_scale)
            );
            arr.push(typeIndex);
            this.scaleTypeList[typeIndex].value = item.num;
          });
          this.scaleTypeList.forEach((item, index) => {
            const dataIndex = arr.findIndex((ite) => ite === index);
            if (dataIndex === -1) {
              item.value = 0;
            }
          });
        } else {
          this.scaleTypeList.forEach((item) => {
            item.value = 0;
          });
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
    },
    //点位图例
    changeCheckList() {
      this.$olMapUtil.drawPoint(
        this.map,
        this.pointsList.filter((e) =>
          this.checkList.includes(Number(e.data.project_scale))
        ),
        "水库规模落点"
      );
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
    //获取水库点位
    async getMapPointList() {
      let res = await reservoirLeftStatisticApi({
        statistic: "mapList",
        adcd: this.selectAdcd,
        itemCategory: "scale",
        itemValue: this.activeScaleType,
      });
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
      this.$olMapUtil.drawPoint(this.map, this.pointsList, "水库规模落点");
    },
    //移除图层
    removeDangerPoint() {
      this.$olMapUtil.removeLayer(this.map, "水库规模落点");
    },
  },
};
</script>
<style lang='scss' scoped>
.reservoir-scale-box {
  padding: 15px 10px 0 10px;
  .reservoir-scale-flex {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    > div {
      cursor: pointer;
      overflow: hidden;
      span {
        font-family: "agencyb";
      }
    }
  }
  .reservoir-scale-long {
    width: 190px;
    height: 116px;
    position: relative;
    box-sizing: border-box;
    padding: 15px 0 0 15px;
    > div {
      font-size: 16px;
      color: #bce7f7;
      font-family: "MicrosoftYaHei";
    }
    > div:nth-child(2) {
      text-align: right;
      padding: 15px 30px 0 0;
      font-size: 12px;
      color: #a5cdde;
      span {
        font-size: 30px;
        padding-right: 5px;
      }
    }
    &.active-long {
      &::before {
        content: "";
        position: absolute;
        left: 0px;
        top: 0px;
        width: 190px;
        height: 95px;
        background: url("../../../assets/images/reservoir-active.png") no-repeat;
      }
    }
    &.reservoir-one {
      background: url("../../../assets/images/reservoir-one.png") no-repeat;
      margin-right: 15px;
      > div:nth-child(2) {
        span {
          color: #52f8fd;
          box-shadow: 0px 3px 5px 0px rgba(0, 0, 0, 0.31);
        }
      }
    }
    &.reservoir-two {
      background: url("../../../assets/images/reservoir-two.png") no-repeat;
      > div:nth-child(2) {
        span {
          color: #1cf5ba;
          box-shadow: 0px 3px 5px 0px rgba(0, 0, 0, 0.31);
        }
      }
    }
  }

  .reservoir-scale-short {
    width: 120px;
    height: 95px;
    position: relative;
    box-sizing: border-box;
    padding-top: 12px;
    text-align: center;
    background: url("../../../assets/images/reservoir-three.png") no-repeat;
    background-size: 100% 100%;
    margin-right: 16px;
    &.active-short {
      &::before {
        content: "";
        position: absolute;
        left: 0px;
        top: 0px;
        width: 120px;
        height: 95px;
        background: url("../../../assets/images/reservoir-three-active.png")
          no-repeat;
      }
    }
    > div {
      font-size: 16px;
      padding-top: 6px;
      color: #bce7f7;
    }
    > div:nth-child(1) {
      font-size: 12px;
      color: #a5cdde;
      span {
        font-size: 30px;
        padding-right: 5px;
        color: #52f8fd;
        box-shadow: 0px 3px 5px 0px rgba(0, 0, 0, 0.31);
      }
    }
  }
}
</style>