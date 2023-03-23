<!--
 * @Date: 2022-06-01 09:26:38
 * @Author: 
 * @Description:实时雨情
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2023-01-03 14:11:00
 * @FilePath: \lucheng-app\src\views\appView\rainfall\RainfallList.vue
-->
<template>
  <div class="project-wrap">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      :title="title"
      left-text=""
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="$router.go(-1)"
    />
    <div class="select-box">
      <van-dropdown-menu
        z-index="2333"
        class="van-dropdown-menu"
        active-color="#276AF0"
      >
        <van-dropdown-item
          title-class="title-class-name"
          v-model="alarmValue"
          :options="alarmOption"
          @change="handerChangeTime"
        />
        <van-dropdown-item
          v-if="alarmValue == 0"
          title-class="title-class-name"
          v-model="timesValue"
          :options="timesOption"
          @change="handerChangeTime"
        />
        <van-dropdown-item
          v-if="alarmValue == 0"
          title-class="title-class-name"
          v-model="rainfallValue"
          :options="rainfallOption"
          @change="handerChangeTime"
        />
      </van-dropdown-menu>
      <van-field
        class="van-field"
        placeholder="搜索"
        v-model="stationName"
        left-icon="search"
        @blur="handerChangeTime"
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
        <span>雨量站数量：{{ list.length }}座</span>
        <template slot="table">
          <tableList :theadList="theadList" :list="list" @row-click="rowClick">
            <template v-slot:stationName="item">
              <span :class="setClassName(item.data.drp)">
                {{ item.data.stationName }}
              </span>
            </template>
          </tableList>
        </template>
      </map-list-shelf>
    </div>
  </div>
</template>

<script>
import mapListShelf from "@/components/mapListShelf.vue";
import tableList from "@/components/tableList.vue";
import ProjectMap from "@/components/ProjectMap.vue";
import mapLegend from "@/components/mapLegend.vue";
import { rainfallApi } from "@/api/rainfall.js";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "RainfallList",
  components: {
    mapListShelf,
    tableList,
    ProjectMap,
    mapLegend,
  },
  computed: {
    ...mapGetters("viewType"),
  },
  data() {
    return {
      timesValue: 0,
      rainfallValue: 0,
      alarmValue: 0,
      stationName: "",
      timesOption: [
        { text: "近1小时", value: 0 },
        { text: "前1小时", value: 1 },
        { text: "前3小时", value: 2 },
        { text: "前6小时", value: 3 },
        { text: "前24小时", value: 4 },
        { text: "前72小时", value: 5 },
      ],
      rainfallOption: [
        { text: "全部雨量", value: 0 },
        { text: "0.1-10mm", value: 1 },
        { text: "10-25mm", value: 2 },
        { text: "25-50mm", value: 3 },
        { text: "50-100mm", value: 4 },
        { text: "100-250mm", value: 5 },
        { text: ">250mm", value: 6 },
      ],
      alarmOption: [
        { text: "无报警", value: 0 },
        { text: "1小时报警", value: 1 },
        { text: "3小时报警", value: 2 },
      ],
      theadList: [
        {
          label: "站名",
          prop: "stationName",
          slot: true,
        },
        {
          label: "所在地（乡镇）",
          prop: "stLoc",
          slot: false,
        },
        {
          label: "雨量（mm）",
          prop: "drp",
          slot: false,
        },
      ],
      legendList: [
        {
          label: ">250",
          color: "#FF52D1",
        },
        {
          label: "100~250",
          color: "#FF4D4F",
        },
        {
          label: "50~100",
          color: "#FFA01E",
        },
        {
          label: "25~50",
          color: "#FFCB27",
        },
        {
          label: "10~25",
          color: "#40BB5A",
        },
        {
          label: "0.1~10",
          color: "#276af0",
        },
        {
          label: "0",
          color: "#C4CAD3",
        },
      ],
      list: [],
      rainfallData: {},
      rainfallTimeData: {},
      rainType: "",
      title: "实时雨情",
    };
  },
  mounted() {
    this.addZwlog(this);
    this.rainType = this.$route.query.type || "";
    if (this.rainType) {
      this.title = "雨量超警";
      this.alarmValue = 1;
      this.alarmOption = [
        { text: "1小时报警", value: 1 },
        { text: "3小时报警", value: 2 },
      ];
    }
    this.getRainfallApi();
  },
  methods: {
    ...mapActions(["setSiteInfo"]),
    async getRainfallApi() {
      let res = await rainfallApi();
      if (res.code == 0) {
        this.rainfallData = res.data;
        this.rainfallTimeData = {
          time_0: this.rainfallData.rainfall_newsest_1_data,
          time_1: this.rainfallData.rainfall_1_data,
          time_2: this.rainfallData.rainfall_3_data,
          time_3: this.rainfallData.rainfall_6_data,
          time_4: this.rainfallData.rainfall_24_data,
          time_5: this.rainfallData.rainfall_72_data,
        };
        this.handerChangeTime();
      }
    },
    setClassName(drp) {
      if (drp <= 0.1) {
        return "blue-color";
      } else if (drp > 0.1 && drp <= 10) {
        return "blue-color";
      } else if (drp > 10 && drp <= 25) {
        return "light-green-color";
      } else if (drp > 25 && drp <= 50) {
        return "yellow-color";
      } else if (drp > 50 && drp <= 100) {
        return "light-orange-color";
      } else if (drp > 100 && drp <= 250) {
        return "red-color";
      } else {
        return "pink-color";
      }
    },
    handerChangeTime() {
      let arr = [];
      if (this.alarmValue == 0) {
        arr = this.handerChangeRainfall(
          this.rainfallTimeData[`time_${this.timesValue}`]
        );
      } else if (this.alarmValue == 1) {
        arr = this.rainfallData["1hourBeyond30List"];
      } else {
        arr = this.rainfallData["3hourBeyond60List"];
      }
      this.list = arr.sort((a, b) => {
        return b.drp - a.drp;
      });
      let pointData = this.list.map((item) => {
        let imgSrc = require("@/assets/images/map/rain_0.png");
        if (item.drp > 0.1 && item.drp <= 10) {
          imgSrc = require("@/assets/images/map/rain_10.png");
        }
        if (item.drp > 10 && item.drp <= 25) {
          imgSrc = require("@/assets/images/map/rain_10_25.png");
        }
        if (item.drp > 25 && item.drp <= 50) {
          imgSrc = require("@/assets/images/map/rain_25_50.png");
        }
        if (item.drp > 50 && item.drp <= 100) {
          imgSrc = require("@/assets/images/map/rain_50_100.png");
        }
        if (item.drp > 100 && item.drp <= 250) {
          imgSrc = require("@/assets/images/map/rain_100_250.png");
        }
        if (item.drp > 250) {
          imgSrc = require("@/assets/images/map/rain_250.png");
        }
        return {
          point: [item.longitude, item.latitude],
          imgSrc,
          type: "雨情",
          ...item,
        };
      });
      this.$refs.mapInfo._drawPoint(pointData, {}, true);
    },
    handerChangeRainfall(arr) {
      let arrData = [];
      switch (this.rainfallValue) {
        case 0:
          arrData = arr;
          break;
        case 1:
          arrData = arr.filter((item) => item.drp > 0.1 && item.drp <= 10);
          break;
        case 2:
          arrData = arr.filter((item) => item.drp > 10 && item.drp <= 25);
          break;
        case 3:
          arrData = arr.filter((item) => item.drp > 25 && item.drp <= 50);
          break;
        case 4:
          arrData = arr.filter((item) => item.drp > 50 && item.drp <= 100);
          break;
        case 5:
          arrData = arr.filter((item) => item.drp > 100 && item.drp <= 250);
          break;
        default:
          arrData = arr.filter((item) => item.drp > 250);
          break;
      }
      if (this.stationName) {
        arrData = arrData.filter(
          (item) => item.stationName.indexOf(this.stationName) != -1
        );
      }
      return arrData;
    },
    rowClick(data, index) {
      this.setSiteInfo(data);
      this.$router.push("/realTimeRainfall?code=" + data.stcd);
    },
  },
};
</script>

<style lang="scss" scoped>
.project-wrap {
  width: 100%;
  height: 100%;
  .select-box {
    height: 43px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    .van-dropdown-menu {
      flex: 1;
    }
    .van-field {
      width: 130px;
    }
  }
  .map-box {
    height: calc(100% - 44px);
    background-color: #fff;
  }
}
</style>