<!--
 * @Date: 2022-06-01 09:26:38
 * @Author: 
 * @Description: 实时水情
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2023-01-30 09:37:02
 * @FilePath: \lucheng-app\src\views\oldAppView\waterRegimen\WaterRegimenList.vue
-->
<template>
  <view-page>
    <div class="project-wrap">
      <div class="tap-options">
        <div
          @click="handerChangeType(item.value)"
          :class="{ active: typeValue === item.value }"
          v-for="item in typeOption"
          :key="item.value"
        >
          {{ item.text }}
        </div>
      </div>
      <div class="select-box">
        <van-dropdown-menu
          z-index="2333"
          class="van-dropdown-menu"
          active-color="#276AF0"
        >
          <!-- <van-dropdown-item
            title-class="title-class-name"
            v-model="typeValue"
            :options="typeOption"
            @change="handerChangeType"
          /> -->
          <van-dropdown-item
            title-class="title-class-name"
            v-model="alarmValue"
            :options="alarmOption"
            @change="getWaterStationsApi"
          />
        </van-dropdown-menu>
        <van-field
          class="van-field"
          v-model="stationName"
          left-icon="search"
          placeholder="搜索"
          @blur="getWaterStationsApi"
        />
      </div>
      <div class="map-box">
        <map-list-shelf>
          <template slot="map">
            <project-map ref="mapInfo" @golink="rowClick"></project-map>
            <div class="map-legend">
              <map-legend :legendList="legendList"></map-legend>
            </div>
          </template>
          <span class="map-title-box"
            >{{ typeValue | type }}数量：{{ list.length }}座</span
          >
          <template slot="table">
            <tableList
              :theadList="theadList"
              :list="list"
              @row-click="rowClick"
            >
              <template v-slot:stName="item"
                ><span :class="setClassName(item.data)">{{
                  item.data.stName
                }}</span></template
              >
            </tableList>
          </template>
        </map-list-shelf>
      </div>
    </div>
  </view-page>
</template>

<script>
import mapListShelf from "@/components/mapListShelf.vue";
import tableList from "@/components/tableList.vue";
import ProjectMap from "@/components/ProjectMap.vue";
import mapLegend from "@/components/mapLegend.vue";
import { waterStationsApi } from "@/api/rainfall.js";
import { mapActions } from "vuex";
export default {
  name: "RainfallList",
  components: {
    mapListShelf,
    tableList,
    ProjectMap,
    mapLegend,
  },
  filters: {
    type: (value) => {
      switch (value) {
        case "RR":
          return "水库";
        case "ZZ":
          return "河道";
        case "DD":
          return "闸坝";
        default:
          return "潮位";
      }
    },
  },
  data() {
    return {
      typeValue: "RR",
      alarmValue: 0,
      stationName: "",
      typeOption: [
        { text: "水库", value: "RR" },
        { text: "河道", value: "ZZ" },
        { text: "闸坝", value: "DD" },
        { text: "潮位", value: "TT" },
      ],
      alarmOption: [],
      list: [],
      legendList: [],
      waterType: "",
      title: "实时水情",
    };
  },
  computed: {
    theadList: function () {
      return this.checkType();
    },
  },
  mounted() {
    this.addZwlog(this);
    this.waterType = this.$route.query.type || "";
    if (this.waterType) {
      this.title = "水位超警";
      this.alarmValue = 1;
    }
    this.getWaterStationsApi();
  },
  methods: {
    ...mapActions(["setSiteInfo"]),
    async getWaterStationsApi() {
      let res = await waterStationsApi({
        stationType: this.typeValue,
        stationName: this.stationName,
      });
      if (res.code == 0) {
        let list = [];
        switch (this.alarmValue) {
          case 0:
            list = res.data;
            break;
          case 1:
            list = res.data.filter(
              (item) => item.waterLevel > item.limitLevel && item.limitLevel > 0
            );
            break;
          case 2:
            if (this.typeValue == "ZZ") {
              list = res.data.filter(
                (item) =>
                  item.waterLevel > item.warningLevel && item.warningLevel > 0
              );
            } else if (this.typeValue == "DD") {
              list = res.data.filter(
                (item) => item.upz > item.warningLevel && item.warningLevel > 0
              );
            } else {
              list = res.data.filter(
                (item) => item.tdz > item.warningLevel && item.warningLevel > 0
              );
            }
            break;
          default:
            if (this.typeValue == "ZZ") {
              list = res.data.filter(
                (item) =>
                  item.waterLevel > item.guaranteeLevel &&
                  item.guaranteeLevel > 0
              );
            } else {
              list = res.data.filter(
                (item) =>
                  item.upz > item.guaranteeLevel && item.guaranteeLevel > 0
              );
            }
            break;
        }
        this.list = list;
        let pointData = this.list.map((item) => {
          let imgSrc = "";
          switch (this.typeValue) {
            case "RR":
              if (item.waterLevel > item.limitLevel && item.limitLevel > 0) {
                imgSrc = require("@/assets/images/map/reservoir-warn.png");
              } else {
                imgSrc = require("@/assets/images/map/reservoir.png");
              }
              break;
            case "ZZ":
              if (
                item.waterLevel > item.guaranteeLevel &&
                item.guaranteeLevel > 0
              ) {
                imgSrc = require("@/assets/images/map/riverway-assure.png");
              } else if (
                item.waterLevel > item.warningLevel &&
                item.warningLevel > 0
              ) {
                imgSrc = require("@/assets/images/map/riverway-warn.png");
              } else {
                imgSrc = require("@/assets/images/map/riverway.png");
              }
              break;
            case "DD":
              if (item.upz > item.guaranteeLevel && item.guaranteeLevel > 0) {
                imgSrc = require("@/assets/images/map/sluice-assure.png");
              } else if (
                item.upz > item.warningLevel &&
                item.warningLevel > 0
              ) {
                imgSrc = require("@/assets/images/map/sluice-warn.png");
              } else {
                imgSrc = require("@/assets/images/map/sluice.png");
              }
              break;
            default:
              if (item.tdz > item.warningLevel && item.warningLevel > 0) {
                imgSrc = require("@/assets/images/map/sea-level-warn.png");
              } else {
                imgSrc = require("@/assets/images/map/sea-level.png");
              }
              break;
          }
          return {
            point: [item.stLong, item.stLat],
            imgSrc,
            type: "水情",
            ...item,
          };
        });
        this.$refs.mapInfo._drawPoint(pointData, { scale: 0.9 }, true);
      }
    },
    setClassName(item) {
      switch (this.typeValue) {
        case "RR":
          if (item.waterLevel > item.limitLevel && item.limitLevel > 0) {
            return "red-color";
          } else {
            return "blue-color";
          }
        case "ZZ":
          if (
            item.waterLevel > item.guaranteeLevel &&
            item.guaranteeLevel > 0
          ) {
            return "light-orange-color";
          } else if (
            item.waterLevel > item.warningLevel &&
            item.warningLevel > 0
          ) {
            return "red-color";
          } else {
            return "blue-color";
          }
        case "DD":
          if (item.upz > item.guaranteeLevel && item.guaranteeLevel > 0) {
            return "light-orange-color";
          } else if (item.upz > item.warningLevel && item.warningLevel > 0) {
            return "red-color";
          } else {
            return "blue-color";
          }
        default:
          if (item.tdz > item.warningLevel && item.warningLevel > 0) {
            return "red-color";
          } else {
            return "blue-color";
          }
      }
    },
    handerChangeType(typeValue) {
      this.typeValue = typeValue;
      this.alarmValue = 0;
      if (this.waterType) {
        if (this.typeValue == "RR") {
          this.alarmValue = 1;
        } else {
          this.alarmValue = 2;
        }
      }
      this.getWaterStationsApi();
    },
    checkType() {
      let theadList = [];
      if (this.typeValue == "RR") {
        theadList = [
          {
            label: "站名",
            prop: "stName",
            slot: true,
          },
          {
            label: "水位(m)",
            prop: "waterLevel",
            slot: false,
          },
          {
            label: "汛限(m)",
            prop: "limitLevel",
            slot: false,
          },
        ];
        this.legendList = [
          {
            label: "正常",
            color: "#276AF0",
          },
          {
            label: "超汛限",
            color: "#FF4D4F",
          },
        ];
        this.alarmOption = [
          { text: "全部", value: 0 },
          { text: "超汛限", value: 1 },
        ];
      } else if (this.typeValue == "ZZ" || this.typeValue == "DD") {
        theadList = [
          {
            label: "站名",
            prop: "stName",
            slot: true,
          },
          {
            label: "水位(m)",
            prop: this.typeValue == "ZZ" ? "waterLevel" : "upz",
            slot: false,
          },
          {
            label: "警戒(m)",
            prop: "warningLevel",
            slot: false,
          },
          {
            label: "保证(m)",
            prop: "guaranteeLevel",
            slot: false,
          },
        ];
        this.legendList = [
          {
            label: "正常",
            color: "#276AF0",
          },
          {
            label: "超警戒",
            color: "#FF4D4F",
          },
          {
            label: "超保证",
            color: "#FFA01E",
          },
        ];
        this.alarmOption = [
          { text: "全部", value: 0 },
          { text: "超警戒", value: 2 },
          { text: "超保证", value: 3 },
        ];
      } else {
        theadList = [
          {
            label: "站名",
            prop: "stName",
            slot: true,
          },
          {
            label: "水位(m)",
            prop: "tdz",
            slot: false,
          },
          {
            label: "警戒(m)",
            prop: "warningLevel",
            slot: false,
          },
        ];
        this.legendList = [
          {
            label: "正常",
            color: "#276AF0",
          },
          {
            label: "超警戒",
            color: "#FF4D4F",
          },
        ];
        this.alarmOption = [
          { text: "全部", value: 0 },
          { text: "超警戒", value: 2 },
        ];
      }
      if (this.waterType) {
        this.alarmOption.splice(0, 1);
      }
      return theadList;
    },
    rowClick(data) {
      this.setSiteInfo(data);
      this.$router.push(
        `/waterRegimen?code=${data.stCode}&type=${data.stType}`
      );
    },
  },
};
</script>

<style lang="scss" scoped>
.project-wrap {
  width: 100%;
  height: 100%;
  padding-top: 1px;
  box-sizing: border-box;
  .tap-options {
    padding: 9px 15px 5px;
    display: flex;
    > div {
      flex: 1;
      background: #f6f6f6;
      height: 30px;
      line-height: 33px;
      text-align: center;
      color: #74767a;
      font-size: 20px;
      &.active {
        background: #276af0;
        border-radius: 2px;
        color: #ffffff;
        font-weight: 500;
      }
    }
  }
  .select-box {
    height: 43px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    .van-dropdown-menu {
      flex: 1;
    }
    .van-field {
      width: 274px;
    }
  }
  .map-box {
    height: calc(100% - 88px);
    background-color: #fff;
    .map-title-box {
      font-size: 20px;
    }
  }
}
</style>
