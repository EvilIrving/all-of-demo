<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>
    <!-- 降雨分析-点选 -->
    <div id="popup1" class="ol-popup-rain">
      <div class="title">
        {{ popupData.name }}
        <i class="el-icon-close" @click="closeOverlay"></i>
      </div>
      <div class="sidebar-tab">
        <span
          v-for="(item, index) in tabArr"
          :key="index"
          :class="{ active: tabActive == item.id }"
          @click="handleClick(item.id)"
          :title="item.tit"
          >{{ item.tit }}</span
        >
      </div>
      <div class="content" v-if="resize">
        <line-chart
          v-if="tabActive == 1"
          :setting="setting"
          :chartData="chartData"
          :subtext="subtext"
          :chartMarkLine="chartMarkLine"
        ></line-chart>
        <div v-if="tabActive == 2">视频监控</div>
      </div>
    </div>
  </div>
</template>

<script>
import { rainApi, waterApi, workApi } from "@/api/dataScreen/floodJudge/index";

import "ol/ol.css";
import { Map, View, Overlay } from "ol";
import XYZ from "ol/source/XYZ";
import TileLayer from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";

import ImageLayer from "ol/layer/Image";
import ImageStatic from "ol/source/ImageStatic";

import { drawStagnantPoint, drawFloodStationPoint } from "@/utils/mapUtil.js";

import lineChart from "./charts/LineChart";
export default {
  name: "ScreenMap",
  components: { lineChart },
  props: {
    pageType: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      //地图
      subtext: "m",
      serviceLink: "http://112.17.127.75:8008/arcgis/rest/services/",
      pointLayer: null, //点位测试图层
      rainLayer: null, //降雨分析图层
      waterLayer: null, //水情分析图层
      workLayer: null, //工程分析图层
      RainfallLayer: null, //等雨面量图层
      map: null,
      popupData: {},
      //图层
      overlay_rain: null,
      tabArr: [
        {
          tit: "水位统计",
          id: "1",
        },
        {
          tit: "视频监控",
          id: "2",
        },
      ],
      tabActive: "1",
      //图表
      resize: false, //解决第一次点卡宽度不能自适应问题
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
      chartMarkLine: {},
      stcd: null,
      stLng: null,
      stLat: null,
    };
  },
  created() {},
  mounted() {
    this.mapInit();
  },
  watch: {
    pageType: function (val) {
      // if (val == "water") {
      //   this.waterPoint();
      // }
    },
  },
  methods: {
    //初始化地图
    mapInit() {
      this.map = new Map({
        target: "mainView",
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new TileLayer(
            {
              source: new XYZ({
                url:
                  "http://192.168.0.149:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/tile/{z}/{y}/{x}",
                projection: "EPSG:4326",
              }),
            },
            { zoomOffset: 1 }
          ),
        ],
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: [120.527, 27.86],
          zoom: 9.5,
          maxZoom: 18,
          zoom: 9.5,
        }),
      });
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
      this.addOverlay(); // 添加overlay
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        let pixel = this.map.getEventPixel(evt.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });

        //点位信息
        console.log("feature: ", feature);

        //是否存在坐标
        if (feature == undefined) {
          this.closeOverlay();
        } else {
          let lng = feature.values_.featureProperties.lng;
          let lat = feature.values_.featureProperties.lat;
          this.closeOverlay();
          if (lng && lat) {
            this.popupData = feature.values_.featureProperties.data;
            const { flag } = feature.values_.featureProperties.data;
            if (flag == "floodStation") {
              this.resize = false;
              //获取图表数据
              const { stcd } = feature.values_.featureProperties.data;
              this.stcd = stcd;
              this.stLng = lng;
              this.stLat = lat;
              this.chartData = { rows: [], columns: [] };
              this.setting = { labelMap: {} };
              let date = this.$moment().format("MM-DD");
              this.chartData.rows = [
                {
                  tm: `${date} 00:00`,
                  drp: 0.0,
                  water: 0.0,
                  rain: 0.0,
                },
                {
                  tm: `${date} 01:00`,
                  drp: 0.033,
                  water: 0.07,
                  rain: 0.044,
                },
                {
                  tm: `${date} 02:00`,
                  drp: 0.036,
                  water: 0.07,
                  rain: 0.048,
                },
                {
                  tm: `${date} 03:00`,
                  drp: 0.036,
                  water: 0.07,
                  rain: 0.048,
                },
                {
                  tm: `${date} 04:00`,
                  drp: 0.042,
                  water: 0.08,
                  rain: 0.056,
                },
                {
                  tm: `${date} 05:00`,
                  drp: 0.051,
                  water: 0.1,
                  rain: 0.068,
                },
                {
                  tm: `${date} 06:00`,
                  drp: 0.069,
                  water: 0.14,
                  rain: 0.092,
                },
                {
                  tm: `${date} 07:00`,
                  drp: 0.093,
                  water: 0.19,
                  rain: 0.124,
                },
                {
                  tm: `${date} 08:00`,
                  drp: 0.12,
                  water: 0.24,
                  rain: 0.16,
                },
                {
                  tm: `${date} 09:00`,
                  drp: 0.126,
                  water: 0.25,
                  rain: 0.168,
                },
                {
                  tm: `${date} 10:00`,
                  drp: 0.129,
                  water: 0.26,
                  rain: 0.172,
                },
                {
                  tm: `${date} 11:00`,
                  drp: 0.129,
                  water: 0.26,
                  rain: 0.172,
                },
                {
                  tm: `${date} 12:00`,
                  drp: 0.144,
                  water: 0.23,
                  rain: 0.152,
                },
                {
                  tm: `${date} 13:00`,
                  drp: 0.105,
                  water: 0.21,
                  rain: 0.14,
                },
                {
                  tm: `${date} 14:00`,
                  drp: 0.099,
                  water: 0.2,
                  rain: 0.132,
                },
                {
                  tm: `${date} 15:00`,
                  drp: 0.075,
                  water: 0.15,
                  rain: 0.1,
                },
                {
                  tm: `${date} 16:00`,
                  drp: 0.069,
                  water: 0.14,
                  rain: 0.092,
                },
                {
                  tm: `${date} 17:00`,
                  drp: 0.066,
                  water: 0.13,
                  rain: 0.088,
                },
                {
                  tm: `${date} 18:00`,
                  drp: 0.063,
                  water: 0.13,
                  rain: 0.084,
                },
                {
                  tm: `${date} 19:00`,
                  drp: 0.063,
                  water: 0.13,
                  rain: 0.084,
                },
                {
                  tm: `${date} 20:00`,
                  drp: 0.06,
                  water: 0.12,
                  rain: 0.08,
                },
                {
                  tm: `${date} 21:00`,
                  drp: 0.057,
                  water: 0.11,
                  rain: 0.076,
                },
                {
                  tm: `${date} 22:00`,
                  drp: 0.051,
                  water: 0.1,
                  rain: 0.068,
                },
                {
                  tm: `23:00`,
                  drp: 0.048,
                  water: 0.1,
                  rain: 0.064,
                },
              ];
              this.chartData.columns = ["tm", "drp", "water", "rain"];
              this.setting.labelMap = {
                drp: "积水点1",
                water: "积水点2",
                rain: "积水点3",
                tm: "时间",
              };
              this.chartMarkLine = {
                symbol: "none",
                data: [
                  {
                    yAxis: 0.5,
                    label: {
                      show: "true",
                      position: "middle",
                      formatter: "历史最大值",
                      color: "#e02020",
                      fontSize: 14,
                    },
                    lineStyle: {
                      color: "#e02020",
                      width: 1,
                    },
                  },
                ],
              };
              this.overlay_rain.setPosition([this.stLng, this.stLat]);
              this.$nextTick(() => {
                this.resize = true;
              });
            }
          }
        }
      });
    },

    // 添加overlay
    addOverlay() {
      // 降雨分析
      let pop_container1 = document.getElementById("popup1");
      this.overlay_rain = new Overlay({
        element: pop_container1,
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [0, -10],
      });
      this.map.addOverlay(this.overlay_rain);
    },
    // 关闭地图弹窗
    closeOverlay() {
      this.overlay_rain.setPosition(undefined);
    },
    // 设置地图位置
    setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 14,
      });
    },
    //降雨分析-popup-菜单
    handleClick(id) {
      this.tabActive = id;
    },
    getHourTime(hour) {
      let timestamp = new Date().getTime(); //当前的时间戳
      timestamp = timestamp + hour * 60 * 60 * 1000;
      return this.$moment(timestamp).format("YYYY-MM-DD HH") + ":00:00";
    },

    async rainPoint(data, stationData) {
      this.cleanAllLayer();
      this.closeOverlay();
      this.rainLayer = await drawStagnantPoint(data);
      this.map.addLayer(this.rainLayer);
      this.waterLayer = await drawFloodStationPoint(stationData);
      this.map.addLayer(this.waterLayer);
    },


    //清理所有图层
    cleanAllLayer() {
      this.map.removeLayer(this.rainLayer);
      this.map.removeLayer(this.workLayer);
      this.map.removeLayer(this.waterLayer);
      this.map.removeLayer(this.RainfallLayer);
    },
  },
};
</script>
<style lang='scss' scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  .map {
    width: 100%;
    height: 100%;
    // 水情分析
    .ol-popup-water {
      position: relative;
      height: 79px;
      width: 1px;
      background: #239def;
      .content-wrap {
        position: absolute;
        bottom: 79px;
        left: 0;
        width: 320px;
        height: 130px;
        background: url("../../../../assets/images/popup-bg2.png") no-repeat
          left top;
        background-size: 100% 100%;
        color: #239def;
        padding: 20px 10px;
        .title {
          font-size: 22px;
          color: #50bcea;
          padding-left: 10px;
        }
        .flex-box {
          width: 100%;
          height: 60px;
          margin-top: 10px;
          display: flex;
          & > div {
            flex: 1;
            text-align: center;
            display: flex;
            justify-content: space-around;
            flex-direction: column;
          }
        }
      }
    }
    //工程分析
    .ol-popup-project {
      width: 447px;
      height: 284px;
      background: url("../../../../assets/images/popup-project-bg.png")
        no-repeat center;
      background-size: 100% 100%;
      .title {
        height: 46px;
        line-height: 46px;
        padding-left: 30px;
        font-size: 18px;
        color: #50bcea;
        i {
          position: absolute;
          top: 20px;
          right: 30px;
          cursor: pointer;
        }
      }
      .content {
        width: 436px;
        height: 230px;
      }
    }
    //降雨分析
    .ol-popup-rain {
      width: 447px;
      height: 344px;
      background: url("../../../../assets/images/popup-rain-bg.png") no-repeat
        center;
      background-size: 100% 100%;
      .title {
        height: 46px;
        line-height: 46px;
        padding-left: 30px;
        font-size: 18px;
        color: #50bcea;
        i {
          position: absolute;
          top: 21px;
          right: 30px;
          cursor: pointer;
        }
      }
      .content {
        position: relative;
        width: 436px;
        height: 230px;
      }
    }

    //tab
    .sidebar-tab {
      width: 438px;
      margin-top: 20px;
      padding: 0 40px;
      display: flex;
      justify-content: initial;
      align-items: center;
      > span {
        display: inline-block;
        height: 32px;
        padding: 0 8px;
        line-height: 32px;
        text-align: center;
        color: #2c5a90;
        font-size: 14px;
        cursor: pointer;
        background: url(../../../../assets/images/bigScreen-mini-tab.png)
          no-repeat;
        background-size: 100% 100%;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
        margin-right: 16px;
        &:last-child {
          margin-right: 0;
        }
        &.active {
          background: url(../../../../assets/images/bigScreen-mini-tab-check.png)
            no-repeat;
          color: #a6dff6;
          background-size: 100% 100%;
        }
      }
    }
  }
}
</style>
