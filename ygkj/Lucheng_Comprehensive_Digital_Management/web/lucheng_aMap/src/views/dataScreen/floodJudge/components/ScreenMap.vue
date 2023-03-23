<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>
    <!-- 降雨分析-点选 -->
    <div id="popup1" class="ol-popup-rain">
      <div class="title">
        {{ popupData.stationName }}
        <i class="el-icon-close" @click="closeOverlay"></i>
      </div>
      <p class="font-orange" v-if="stcd == 'SWZ_05821'">
        <i class="icon-warn-tips"></i>
        当前位置发生预警， 前往
        <span class="go-btn" @click="bigScreenGo('/jointDispatch')">调度</span>
        前往
        <span class="go-btn" @click="bigScreenGo('/rescueSupport')">抢险</span>
      </p>
      <div
        class="sidebar-tab"
        :style="{ 'margin-top': stcd == 'SWZ_05821' ? 0 : '20px' }"
      >
        <span
          v-for="(item, index) in tabArr"
          :key="index"
          :class="{ active: tabActive == item.id }"
          @click="handleClick(item.id)"
          :title="item.tit"
          >{{ item.tit }}</span
        >
      </div>
      <div
        class="content"
        v-if="resize"
        :style="{ height: stcd == 'SWZ_05821' ? '200px' : '230px' }"
      >
        <bar-chart
          :chartData="chartData"
          :setting="setting"
          v-if="tabActive == 2 || tabActive == 1"
        />
        <line-chart
          v-if="tabActive == 3 || tabActive == 4"
          :setting="setting"
          :chartData="chartData"
          :subtext="subtext"
        ></line-chart>
      </div>
    </div>
    <!-- 水情分析-点选 -->
    <div id="popup2" class="ol-popup-water">
      <div class="content-wrap">
        <p class="title">{{ popupData.stName }}</p>
        <div class="flex-box">
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              当前水位
            </p>
            <p>{{ popupData.z }}m</p>
          </div>
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              汛限水位
            </p>
            <p>
              {{ popupData.limitLevel == null ? "-" : popupData.limitLevel }}m
            </p>
          </div>
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              防洪高水位
            </p>
            <p>
              {{ popupData.floodLevel == null ? "-" : popupData.floodLevel }}m
            </p>
          </div>
        </div>
      </div>
    </div>
    <!-- 工程分析-点选 -->
    <div id="popup3" class="ol-popup-project">
      <div class="title">
        {{ popupData.stName }}
        <i class="el-icon-close" @click="closeOverlay"></i>
      </div>
      <div class="content" v-if="resize">
        <line-chart :chartData="chartData" :setting="setting" v-if="resize" />
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

import { drawJgRainPoint, drawIsDangerPoint } from "@/utils/mapUtil.js";

import BarChart from "./charts/BarChart";
import lineChart from "./charts/LineChart";
export default {
  name: "ScreenMap",
  components: { BarChart, lineChart },
  props: {
    pageType: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      //地图
      subtext: "mm",
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
      overlay_water: null,
      overlay_work: null,
      tabArr: [
        {
          tit: "平均雨量",
          id: "1",
        },
        {
          tit: "实时降雨",
          id: "2",
        },
        {
          tit: "2h降雨预报",
          id: "3",
        },
        {
          tit: "72h降雨预报",
          id: "4",
        },
      ],
      tabActive: "2",
      //图表
      resize: false, //解决第一次点卡宽度不能自适应问题
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
      stcd: null,
      stLng: null,
      stLat: null,
      isohyetLayer:null,
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
    bigScreenGo(path) {
      this.$router.push(path);
    },
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
                  "http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/tile/{z}/{y}/{x}",
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
            if (flag == "rain") {
              this.resize = false;
              //获取图表数据
              const { stcd } = feature.values_.featureProperties.data;
              this.stcd = stcd;
              this.stLng = lng;
              this.stLat = lat;
              this.getRainChart();
            } else if (flag == "water") {
              this.overlay_water.setPosition([lng, lat]);
            } else if (flag == "work") {
              this.resize = false;
              //获取图表数据
              const { stCode, stType } = feature.values_.featureProperties.data;
              workApi
                .pointDt({
                  stationCode: stCode,
                  stationType: stType,
                  days: 1,
                })
                .then((res) => {
                  if (res) {
                    this.chartData = { rows: [], columns: [] };
                    this.setting = { labelMap: {} };
                    this.chartData.rows = res.data.data;
                    if (stType == "RR") {
                      this.chartData.columns = ["tm", "rz"];
                      this.setting.labelMap = {
                        rz: "水位",
                        tm: "时间",
                      };
                    } else if (stType == "DD") {
                      this.chartData.columns = ["tm", "upz"];
                      this.setting.labelMap = {
                        upz: "水位",
                        tm: "时间",
                      };
                    }
                    if (res.data.data.length > 0) {
                      this.overlay_work.setPosition([lng, lat]);
                      this.$nextTick(() => {
                        this.resize = true;
                      });
                    } else {
                      this.$message.warning("该点暂无水位数据！");
                    }
                  }
                });
            }
          }
        }
      });
    },
    getRainChart() {
      if (this.stcd == "SWZ_05821") {
        // 桥墩(北斗)
        this.chartData = { rows: [], columns: [] };
        this.setting = { labelMap: {} };
        let date = this.$moment().format("MM-DD");
        this.chartData.rows = [
          {
            tm: `${date} 00:00`,
            drp: 1.2,
          },
          {
            tm: `${date} 01:00`,
            drp: 2.5,
          },
          {
            tm: `${date} 02:00`,
            drp: 3.4,
          },
          {
            tm: `${date} 03:00`,
            drp: 2.1,
          },
          {
            tm: `${date} 04:00`,
            drp: 4.7,
          },
          {
            tm: `${date} 05:00`,
            drp: 5.8,
          },
          {
            tm: `${date} 06:00`,
            drp: 8.9,
          },
          {
            tm: `${date} 07:00`,
            drp: 13.2,
          },
          {
            tm: `${date} 08:00`,
            drp: 10.3,
          },
          {
            tm: `${date} 09:00`,
            drp: 15.7,
          },
          {
            tm: `${date} 10:00`,
            drp: 9.2,
          },
          {
            tm: `${date} 11:00`,
            drp: 8.5,
          },
          {
            tm: `${date} 12:00`,
            drp: 6.4,
          },
          {
            tm: `${date} 13:00`,
            drp: 3.2,
          },
          {
            tm: `${date} 14:00`,
            drp: 7.7,
          },
          {
            tm: `${date} 15:00`,
            drp: 4.3,
          },
          {
            tm: `${date} 16:00`,
            drp: 3.1,
          },
          {
            tm: `${date} 17:00`,
            drp: 2.5,
          },
          {
            tm: `${date} 18:00`,
            drp: 1.4,
          },
          {
            tm: `${date} 19:00`,
            drp: 2.1,
          },
          {
            tm: `${date} 20:00`,
            drp: 1.8,
          },
          {
            tm: `${date} 21:00`,
            drp: 1.5,
          },
          {
            tm: `${date} 22:00`,
            drp: 0.6,
          },
          {
            tm: `23:00`,
            drp: 0,
          },
        ];
        this.chartData.columns = ["tm", "drp"];
        this.setting.labelMap = {
          drp: "降雨量",
          tm: "时间",
        };
        this.overlay_rain.setPosition([this.stLng, this.stLat]);
        this.$nextTick(() => {
          this.resize = true;
        });
      } else {
        rainApi
          .rainPop({
            stationCode: this.stcd,
            days: 1,
          })
          .then((res) => {
            if (res) {
              this.chartData = { rows: [], columns: [] };
              this.setting = { labelMap: {} };
              this.chartData.rows = res.data;
              this.chartData.columns = ["tm", "drp"];
              this.setting.labelMap = {
                drp: "降雨量",
                tm: "时间",
              };
              if (res.data.length > 0) {
                this.overlay_rain.setPosition([this.stLng, this.stLat]);
                this.$nextTick(() => {
                  this.resize = true;
                });
              } else {
                this.$message.warning("该点暂无雨量数据！");
              }
            }
          });
      }
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

      // 水情分析
      let pop_container2 = document.getElementById("popup2");
      this.overlay_water = new Overlay({
        element: pop_container2,
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [5, 0],
      });
      this.map.addOverlay(this.overlay_water);

      // 工程分析
      let pop_container3 = document.getElementById("popup3");
      this.overlay_work = new Overlay({
        element: pop_container3,
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [0, -10],
      });
      this.map.addOverlay(this.overlay_work);
    },
    // 关闭地图弹窗
    closeOverlay() {
      this.overlay_rain.setPosition(undefined);
      this.overlay_water.setPosition(undefined);
      this.overlay_work.setPosition(undefined);
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
      if (this.tabActive == 3 || this.tabActive == 4) {
        this.chartData = { rows: [], columns: [] };
        this.setting = { labelMap: {} };
        this.chartData.columns = ["tm", "drp"];
        this.setting.labelMap = {
          drp: "降雨量",
          tm: "时间",
        };
        if (this.tabActive == 3) {
          if (this.stcd == "SWZ_05821") {
            let day1 = this.$moment().format("MM-DD");
            this.chartData.rows = [
              {
                tm: `${day1} 00:00`,
                drp: 1.2,
              },
              {
                tm: `${day1} 00:15`,
                drp: 2.5,
              },
              {
                tm: `${day1} 00:30`,
                drp: 3.4,
              },
              {
                tm: `${day1} 00:45`,
                drp: 2.1,
              },
              {
                tm: `${day1} 01:00`,
                drp: 4.7,
              },
              {
                tm: `${day1} 01:15`,
                drp: 3.1,
              },
              {
                tm: `${day1} 01:30`,
                drp: 2.5,
              },
              {
                tm: `${day1} 01:45`,
                drp: 1.4,
              },
              {
                tm: `${day1} 02:00`,
                drp: 2.1,
              },
            ];
          } else {
            this.chartData.rows = [
              {
                tm: this.getHourTime(1),
                drp: "0",
              },
              {
                tm: this.getHourTime(2),
                drp: "0",
              },
            ];
          }
        } else {
          if (this.stcd == "SWZ_05821") {
            let day1 = this.$moment().format("MM-DD");
            let day2 = this.$moment(new Date()).add(1, "days").format("MM-DD");
            let day3 = this.$moment(new Date()).add(2, "days").format("MM-DD");
            let day4 = this.$moment(new Date()).add(3, "days").format("MM-DD");
            this.chartData.rows = [
              {
                tm: `${day1} 00:00`,
                drp: 1.2,
              },
              {
                tm: `${day1} 03:00`,
                drp: 2.5,
              },
              {
                tm: `${day1} 06:00`,
                drp: 3.4,
              },
              {
                tm: `${day1} 09:00`,
                drp: 2.1,
              },
              {
                tm: `${day1} 12:00`,
                drp: 4.7,
              },
              {
                tm: `${day1} 15:00`,
                drp: 5.8,
              },
              {
                tm: `${day1} 18:00`,
                drp: 7.7,
              },
              {
                tm: `${day1} 21:00`,
                drp: 4.3,
              },
              {
                tm: `${day2} 00:00`,
                drp: 3.1,
              },
              {
                tm: `${day2} 03:00`,
                drp: 1.4,
              },
              {
                tm: `${day2} 06:00`,
                drp: 2.1,
              },
              {
                tm: `${day2} 09:00`,
                drp: 1.8,
              },
              {
                tm: `${day2} 12:00`,
                drp: 1.5,
              },
              {
                tm: `${day2} 15:00`,
                drp: 0.6,
              },
              {
                tm: `${day2} 18:00`,
                drp: 0.1,
              },
              {
                tm: `${day2} 21:00`,
                drp: 2.5,
              },
              {
                tm: `${day3} 00:00`,
                drp: 1.4,
              },
              {
                tm: `${day3} 03:00`,
                drp: 2.1,
              },
              {
                tm: `${day3} 06:00`,
                drp: 1.8,
              },
              {
                tm: `${day3} 09:00`,
                drp: 1.5,
              },
              {
                tm: `${day3} 12:00`,
                drp: 0.6,
              },
              {
                tm: `${day3} 15:00`,
                drp: 0,
              },
              {
                tm: `${day3} 18:00`,
                drp: 0.1,
              },
              {
                tm: `${day3} 21:00`,
                drp: 0.2,
              },
              {
                tm: `${day4} 00:00`,
                drp: 0,
              },
            ];
          } else {
            this.chartData.rows = [
              {
                tm: this.getHourTime(1),
                drp: "0",
              },
              {
                tm: this.getHourTime(12),
                drp: "0",
              },
              {
                tm: this.getHourTime(24),
                drp: "0",
              },
              {
                tm: this.getHourTime(36),
                drp: "0",
              },
              {
                tm: this.getHourTime(48),
                drp: "0",
              },
              {
                tm: this.getHourTime(60),
                drp: "0",
              },
              {
                tm: this.getHourTime(72),
                drp: "0",
              },
            ];
          }
        }
      } else if (this.tabActive == 1) {
        this.chartData = { rows: [], columns: [] };
        this.setting = { labelMap: {} };
        this.chartData.columns = ["day", "drp"];
        this.setting.labelMap = {
          drp: "降雨量",
          day: "天数",
        };
        this.chartData.rows = [
          {
            drp: 6.8,
            day: 1,
          },
          {
            drp: 5.1,
            day: 2,
          },
          {
            drp: 5.5,
            day: 3,
          },
          {
            drp: 5.0,
            day: 4,
          },
          {
            drp: 6.0,
            day: 5,
          },
          {
            drp: 3.8,
            day: 6,
          },
          {
            drp: 5.9,
            day: 7,
          },
          {
            drp: 7.9,
            day: 8,
          },
          {
            drp: 5.4,
            day: 9,
          },
          {
            drp: 3.9,
            day: 10,
          },
          {
            drp: 7.8,
            day: 11,
          },
          {
            drp: 6.9,
            day: 12,
          },
          {
            drp: 4.6,
            day: 13,
          },
          {
            drp: 7.4,
            day: 14,
          },
          {
            drp: 4.8,
            day: 15,
          },
          {
            drp: 3.0,
            day: 16,
          },
          {
            drp: 6.0,
            day: 17,
          },
          {
            drp: 6.0,
            day: 18,
          },
          {
            drp: 6.9,
            day: 19,
          },
          {
            drp: 6.2,
            day: 20,
          },
          {
            drp: 3.5,
            day: 21,
          },
          {
            drp: 7.9,
            day: 22,
          },
          {
            drp: 7.9,
            day: 23,
          },
          {
            drp: 5.8,
            day: 24,
          },
          {
            drp: 4.4,
            day: 25,
          },
          {
            drp: 5.5,
            day: 26,
          },
          {
            drp: 5.3,
            day: 27,
          },
          {
            drp: 4.6,
            day: 28,
          },
          {
            drp: 4.2,
            day: 29,
          },
          {
            drp: 4.0,
            day: 30,
          },
        ];
      } else {
        this.getRainChart();
      }
    },
    getHourTime(hour) {
      let timestamp = new Date().getTime(); //当前的时间戳
      timestamp = timestamp + hour * 60 * 60 * 1000;
      return this.$moment(timestamp).format("YYYY-MM-DD HH") + ":00:00";
    },
    // 测试打点
    showPoint() {
      this.map.removeLayer(this.pointLayer);
      let data = [
        {
          lng: 121.027,
          lat: 28.001,
          name: "降雨分析点位测试",
          type: 1,
          phone: "15612340001",
          title: "测试队名2",
          popType: "rain",
        },
        {
          lng: 119.927,
          lat: 28.001,
          name: "水情分析点位测试",
          type: 2,
          phone: "15612340002",
          title: "测试队名2",
          popType: "water",
        },
        {
          lng: 120.327,
          lat: 28.001,
          name: "工程分析点位测试",
          type: 3,
          phone: "15612340003",
          title: "测试队名3",
          popType: "work",
        },
      ];
      this.pointLayer = drawMapPoint(data);
      this.map.addLayer(this.pointLayer);
    },
    async rainPoint(data) {
      this.cleanAllLayer();
      this.closeOverlay();
      this.rainLayer = await drawJgRainPoint(data);
      this.map.addLayer(this.rainLayer);
    },
    async waterPoint(type) {
      this.cleanAllLayer();
      this.closeOverlay();
      if (type == 1) {
        let res = await waterApi.mapPoint({
          type: type ? type : 1,
        });
        let info = res.data;
        info.map((item) => {
          if (item.stName == "桥墩坝上") {
            item.warnStatus = 1;
          }
        });
        this.waterLayer = drawIsDangerPoint(info, "water");
      } else if (type == 2) {
        let res = [
          {
            stName: "温州",
            z: "0.7",
            warnLevel: "-0.89",
            sureLevel: "-",
            highestLevel: "5.55",
            stLong: 120.64,
            stLat: 28.03,
          },
          {
            stName: "灵昆",
            z: "-0.09",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "-",
            stLong: 120.85,
            stLat: 27.97,
          },
          {
            stName: "瑞安",
            z: "0.11",
            warnLevel: "-1.17",
            sureLevel: "-",
            highestLevel: "5.03",
            stLong: 120.62,
            stLat: 27.79,
          },
          {
            stName: "鳌江",
            z: "0.11",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "5.22",
            stLong: 120.55,
            stLat: 27.59,
          },
          {
            stName: "埭头",
            z: "10.78",
            warnLevel: "18.92",
            sureLevel: "19.92",
            highestLevel: "17.92",
            stLong: 120.28,
            stLat: 27.6,
          },
          {
            stName: "石柱",
            z: "16.54",
            warnLevel: "31.79",
            sureLevel: "30.79",
            highestLevel: "29.79",
            stLong: 120.75,
            stLat: 28.26,
          },
          {
            stName: "峃口",
            z: "21.12",
            warnLevel: "38.5",
            sureLevel: "39.5",
            highestLevel: "37.5",
            stLong: 120.15,
            stLat: 27.75,
          },
        ];
        this.waterLayer = drawIsDangerPoint(res, "water");
      } else if (type == 3) {
        let res = [
          {
            stName: "西山",
            z: "2.59",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "4.42",
            stLong: 120.62,
            stLat: 27.99,
          },
          {
            stName: "永强",
            z: "2.39",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.93",
            stLong: 120.82,
            stLat: 27.91,
          },
          {
            stName: "林垟",
            z: "2.72",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.29",
            stLong: 120.61,
            stLat: 27.71,
          },
          {
            stName: "平阳",
            z: "2.69",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "3.85",
            stLong: 120.55,
            stLat: 27.67,
          },
          {
            stName: "鳌江内河",
            z: "2.77",
            warnLevel: "3",
            sureLevel: "3.6",
            highestLevel: "3.65",
            stLong: 120.55,
            stLat: 27.6,
          },
          {
            stName: "宜山",
            z: "2.36",
            warnLevel: "3",
            sureLevel: "3.5",
            highestLevel: "3.55",
            stLong: 120.54,
            stLat: 27.51,
          },
        ];
        this.waterLayer = drawIsDangerPoint(res, "water");
      } else {
        // console.log("drwa way");
        let res = [
          {
            stName: "西山",
            z: "2.59",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "4.42",
            stLong: 120.62,
            stLat: 27.99,
          },
          {
            stName: "永强",
            z: "2.39",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.93",
            stLong: 120.82,
            stLat: 27.91,
          },
          {
            stName: "林垟",
            z: "2.72",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.29",
            stLong: 120.61,
            stLat: 27.71,
          },
          {
            stName: "平阳",
            z: "2.69",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "3.85",
            stLong: 120.55,
            stLat: 27.67,
          },
          {
            stName: "鳌江内河",
            z: "2.77",
            warnLevel: "3",
            sureLevel: "3.6",
            highestLevel: "3.65",
            stLong: 120.55,
            stLat: 27.6,
          },
          {
            stName: "宜山",
            z: "2.36",
            warnLevel: "3",
            sureLevel: "3.5",
            highestLevel: "3.55",
            stLong: 120.54,
            stLat: 27.51,
          },
          {
            stName: "温州",
            z: "0.7",
            warnLevel: "-0.89",
            sureLevel: "-",
            highestLevel: "5.55",
            stLong: 120.64,
            stLat: 28.03,
          },
          {
            stName: "灵昆",
            z: "-0.09",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "-",
            stLong: 120.85,
            stLat: 27.97,
          },
          {
            stName: "瑞安",
            z: "0.11",
            warnLevel: "-1.17",
            sureLevel: "-",
            highestLevel: "5.03",
            stLong: 120.62,
            stLat: 27.79,
          },
          {
            stName: "鳌江",
            z: "0.11",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "5.22",
            stLong: 120.55,
            stLat: 27.59,
          },
          {
            stName: "埭头",
            z: "10.78",
            warnLevel: "18.92",
            sureLevel: "19.92",
            highestLevel: "17.92",
            stLong: 120.28,
            stLat: 27.6,
          },
          {
            stName: "石柱",
            z: "16.54",
            warnLevel: "31.79",
            sureLevel: "30.79",
            highestLevel: "29.79",
            stLong: 120.75,
            stLat: 28.26,
          },
          {
            stName: "峃口",
            z: "21.12",
            warnLevel: "38.5",
            sureLevel: "39.5",
            highestLevel: "37.5",
            stLong: 120.15,
            stLat: 27.75,
          },
        ];
        this.waterLayer = drawIsDangerPoint(res, "water");
      }
      this.map.addLayer(this.waterLayer);
    },
    async workPoint(data) {
      // this.showPoint();
      this.cleanAllLayer();
      this.closeOverlay();
      this.workLayer = await drawIsDangerPoint(data, "work");
      this.map.addLayer(this.workLayer);
    },
    //清理所有图层
    cleanAllLayer() {
      this.map.removeLayer(this.rainLayer);
      this.map.removeLayer(this.workLayer);
      this.map.removeLayer(this.waterLayer);
      this.map.removeLayer(this.RainfallLayer);
    },
    //等雨面量
    showConstantRainfall() {
      this.clearRainfallLayer();
      this.RainfallLayer = new ImageLayer({
        source: new ImageStatic({
          url: require("@/assets/images/dyml3.png"), //这里添加静态图片的地址
          // url: "http://192.168.0.149:6080/arcgis/rest/directories/arcgisoutput/gps/idwSameValueRain_GPServer/gps/idwSameValueRain_MapServer/_ags_mapf1cf515acedd4b50b5c458f7e55f7f17.png",//这里添加静态图片的地址
          projection: "EPSG:4326",
          imageExtent: [
            119.53461054597985,
            26.976787411801833,
            121.3386,
            28.7807874,
          ], //WGS-84 经纬度数据范围
        }),
        opacity: 1,
      });
      this.map.addLayer(this.RainfallLayer);
    },
    clearRainfallLayer() {
      this.map.removeLayer(this.RainfallLayer);
    },
    //降雨等值面
    showIsohyetLayer(mapImage){
      this.clearIsohyetLayer();
      this.isohyetLayer =  new ImageLayer({
        source: new ImageStatic({
            url: mapImage.href,//这里添加静态图片的地址
            // url: "http://localhost:6080/arcgis/rest/directories/arcgisoutput/gps/idwSameValueRain_GPServer/gps/idwSameValueRain_MapServer/_ags_mapf1cf515acedd4b50b5c458f7e55f7f17.png",//这里添加静态图片的地址
            //url: "/images/等值面.png",//这里添加静态图片的地址
            projection: 'EPSG:4326',
            imageExtent: [mapImage.extent.xmin,mapImage.extent.ymin, mapImage.extent.xmax,mapImage.extent.ymax] //WGS-84 经纬度数据范围  
        }),
        opacity: 0.5
      });
      this.map.addLayer(this.isohyetLayer);
    },
    clearIsohyetLayer(){
      if(this.isohyetLayer){
        this.map.removeLayer(this.isohyetLayer);
      }
    }
  },
};
</script>
<style lang='scss' scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  .icon-warn-tips {
    vertical-align: middle;
  }
  .font-orange {
    color: #fa6400;
    height: 32px;
    font-size: 14px;
    padding-left: 24px;
    margin: 10px 0;
    .go-btn {
      display: inline-block;
      height: 32px;
      padding: 0 20px;
      line-height: 32px;
      text-align: center;
      color: #fff;
      font-size: 14px;
      cursor: pointer;
      background: url(../../../../assets/images/bigScreen-mini-tab-warn.png)
        no-repeat;
      background-size: 100% 100%;
    }
  }
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
      // margin-top: 20px;
      padding: 0 40px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      > span {
        display: inline-block;
        height: 32px;
        padding: 0 5px;
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
