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
      <div class="sidebar-tab" :style="{ 'margin-top': stcd == 'SWZ_05821' ? 0 : '20px' }">
        <span
          v-for="(item, index) in tabArr"
          :key="index"
          :class="{ active: tabActive == item.id }"
          @click="handleClick(item.id)"
          :title="item.tit"
          >{{ item.tit }}</span
        >
      </div>
      <div class="content" v-if="resize" :style="{ height: stcd == 'SWZ_05821' ? '200px' : '230px' }">
        <bar-chart :chartData="chartData" :setting="setting" v-if="tabActive == 2 || tabActive == 1" />
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
            <p>{{ popupData.limitLevel == null ? '-' : popupData.limitLevel }}m</p>
          </div>
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              防洪高水位
            </p>
            <p>{{ popupData.floodLevel == null ? '-' : popupData.floodLevel }}m</p>
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
      <div v-if="popupData.stName == '桥墩坝上'">
        <p class="font-orange">
          <i class="icon-warn-tips"></i>
          当前位置发生预警， 前往
          <span class="go-btn" @click="bigScreenGo('/jointDispatch')">调度</span>
          前往
          <span class="go-btn" @click="bigScreenGo('/rescueSupport')">抢险</span>
        </p>
      </div>
      <div class="content" v-if="resize">
        <line-chart :chartData="chartData" :setting="setting" v-if="resize" />
      </div>
    </div>

    <div id="popup5" class="ol-popup-water">
      <div class="content-wrap">
        <p class="title">{{ popupData.stName }}</p>
        <div class="flex-box">
          <div>
            <p>
              <img src="../../../../assets/images/popup-icon.png" alt />
              积水点名称
            </p>
            <p>{{ popupData.name }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 点位提示框---------start----------- -->
    <div id="popup" class="ol-popup">
      <a href="#" id="popup-closer" class="ol-popup-closer"></a>
      <div id="popup-content">
        <p v-for="(value, key) in featureInfo" :key="key">
          <span v-if="key == '名称'" class="popup-name">{{ value }}</span>
          <span v-else>
            <span>{{ key }}</span
            >：
            <span>{{ value }}</span>
          </span>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { rainApi } from '@/api/dataScreen/floodJudge/index';

import 'ol/ol.css';
import { Map, View, Overlay } from 'ol';
import XYZ from 'ol/source/XYZ';
import TileLayer from 'ol/layer/Tile';
import { defaults as defaultControls } from 'ol/control';
import ImageLayer from 'ol/layer/Image';
import ImageStatic from 'ol/source/ImageStatic';
import {
  drawJgRainPoint,
  liftWaterloggingStationPoint,
  riverStationPoint,
  seaLevelStationPoint,
  sluiceStationPoint,
  setReservoirPoint
} from '@/utils/mapUtil.js';

import { Fill, Stroke, Style, Circle } from 'ol/style';
import { Vector as VectorSource } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';

import EsriJSON from 'ol/format/EsriJSON';
import { createXYZ } from 'ol/tilegrid';

import { tile as tileStrategy } from 'ol/loadingstrategy';
import $ from 'jquery';

import BarChart from './charts/BarChart';
import lineChart from './charts/LineChart';

const riverLayesBaseService = 'http://117.149.227.112:6080/arcgis/rest/services/waterSearch/';
export default {
  name: 'ScreenMap',
  components: { BarChart, lineChart },
  props: {},
  data() {
    return {
      //地图
      subtext: 'mm',
      rainLayer: null, //降雨分析图层
      waterlogging: null, //积水点
      drainage: null, //河网水位
      seaLevel: null, //潮位
      sluice: null, //闸门
      map: null,
      popupData: {},
      //图层
      overlay_rain: null,
      overlay_water: null,
      tabArr: [
        {
          tit: '平均雨量',
          id: '1'
        },
        {
          tit: '实时降雨',
          id: '2'
        },
        {
          tit: '2h降雨预报',
          id: '3'
        },
        {
          tit: '72h降雨预报',
          id: '4'
        }
      ],
      tabActive: '2',
      //图表
      resize: false, //解决第一次点卡宽度不能自适应问题
      chartData: { rows: [], columns: [] },
      setting: { labelMap: {} },
      stcd: null,
      stLng: null,
      stLat: null,
      container: null,
      content: null,
      popupCloser: null,
      overlay: null,
      featureInfo: null,
      isohyetLayer: null, //面雨量图层
      reservoirLayer: null, //水库点位图层
      sluiceTopArrLayer: null
    };
  },
  created() {},
  mounted() {
    this.mapInit();
  },
  watch: {},
  methods: {
    bigScreenGo(path) {
      this.$router.push(path);
    },
    //初始化地图
    mapInit() {
      this.map = new Map({
        target: 'mainView',
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new TileLayer(
            {
              source: new XYZ({
                url:
                  'http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/tile/{z}/{y}/{x}',
                projection: 'EPSG:4326'
              })
            },
            { zoomOffset: 1 }
          ),
          new TileLayer({
            id: 'wenzhouRiverLayerId',
            source: new XYZ({
              url: 'http://117.149.227.112:6080/arcgis/rest/services/waterSearch/RV_RVAA/MapServer/tile/{z}/{y}/{x}',
              projection: 'EPSG:4326'
            }),
            visible: 1,
            preload: 1
          })
        ],
        view: new View({
          projection: 'EPSG:4326', //使用这个坐标系
          center: [120.527, 27.86],
          zoom: 9.5,
          maxZoom: 18
        })
      });
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
      this.addOverlay(); // 添加overlay

      this.container = document.getElementById('popup');
      this.content = document.getElementById('popup-content');
      this.popupCloser = document.getElementById('popup-closer');
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: this.container,
        autoPan: true, //是否自动平移，即假如标记在屏幕边缘，弹出时自动平移地图使弹出框完全可见
        autoPanAnimation: {
          duration: 250 //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度
        }
      });
      // 地图鼠标移动事件
      this.map.on('pointermove', this._showFeatureInfo);
    },
    _showFeatureInfo(e) {
      let pixel = this.map.getEventPixel(e.originalEvent);
      let flag = false;
      this.map.getTargetElement().style.cursor = 'auto';
      this.map.forEachFeatureAtPixel(pixel, (feature) => {
        let coodinate = e.coordinate;
        let properties;
        // console.log(feature.getProperties())
        if (feature && feature.getProperties().featureProperties) {
          this.map.getTargetElement().style.cursor = 'pointer';
          properties = feature.getProperties().featureProperties;
          if (properties) {
            let obj = {
              名称:
                properties.name ||
                properties.data.stName ||
                properties.data.stationName ||
                properties.data.reservoirName ||
                '-'
            };
            this.featureInfo = obj;
          }
        } else {
          return;
        }
        //设置overlay的显示位置
        if (this.overlay) this.overlay.setPosition(coodinate);
        flag = true;
      });
      if (flag) {
        //显示overlay
        this.map.addOverlay(this.overlay);
      } else this.map.removeOverlay(this.overlay);
    },
    // 地图点击事件
    mapClick() {
      this.map.on('singleclick', (evt) => {
        console.log(evt);
        let pixel = this.map.getEventPixel(evt.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function(feature) {
          return feature;
        });
        // console.log(feature);
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
            if (flag == 'rain') {
              this.resize = false;
              //获取图表数据
              const { stcd } = feature.values_.featureProperties.data;
              this.stcd = stcd;
              this.stLng = lng;
              this.stLat = lat;
              this.getRainChart();
            }
          }
        }
      });
    },
    getRainChart() {
      rainApi
        .rainPop({
          stationCode: this.stcd,
          days: 1
        })
        .then((res) => {
          if (res) {
            this.chartData = { rows: [], columns: [] };
            this.setting = { labelMap: {} };
            this.chartData.rows = res.data;
            this.chartData.columns = ['tm', 'drp'];
            this.setting.labelMap = {
              drp: '降雨量',
              tm: '时间'
            };
            /* if (res.data.length > 0) { */
            this.overlay_rain.setPosition([this.stLng, this.stLat]);
            this.$nextTick(() => {
              this.resize = true;
            });
            /* } else {
            this.$message.warning("该点暂无雨量数据！");
          } */
          }
        });
    },
    // 添加overlay
    addOverlay() {
      // 降雨分析
      let pop_container1 = document.getElementById('popup1');
      this.overlay_rain = new Overlay({
        element: pop_container1,
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        },
        positioning: 'bottom-center',
        offset: [0, -10]
      });
      this.map.addOverlay(this.overlay_rain);
      // 水情分析
      let pop_container2 = document.getElementById('popup2');
      this.overlay_water = new Overlay({
        element: pop_container2,
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        },
        positioning: 'bottom-center',
        offset: [5, 0]
      });
      this.map.addOverlay(this.overlay_water);
    },
    // 关闭地图弹窗
    closeOverlay() {
      this.overlay_rain.setPosition(undefined);
      this.overlay_water.setPosition(undefined);
    },
    // 设置地图位置
    setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 14
      });
    },
    //降雨分析-popup-菜单
    handleClick(id) {
      this.tabActive = id;
      if (this.tabActive == 3 || this.tabActive == 4) {
        this.chartData = { rows: [], columns: [] };
        this.setting = { labelMap: {} };
        this.chartData.columns = ['tm', 'drp'];
        this.setting.labelMap = {
          drp: '降雨量',
          tm: '时间'
        };
        if (this.tabActive == 3) {
          if (this.stcd == 'SWZ_05821') {
            let day1 = this.$moment().format('MM-DD');
            this.chartData.rows = [
              {
                tm: `${day1} 00:00`,
                drp: 1.2
              },
              {
                tm: `${day1} 00:15`,
                drp: 2.5
              },
              {
                tm: `${day1} 00:30`,
                drp: 3.4
              },
              {
                tm: `${day1} 00:45`,
                drp: 2.1
              },
              {
                tm: `${day1} 01:00`,
                drp: 4.7
              },
              {
                tm: `${day1} 01:15`,
                drp: 3.1
              },
              {
                tm: `${day1} 01:30`,
                drp: 2.5
              },
              {
                tm: `${day1} 01:45`,
                drp: 1.4
              },
              {
                tm: `${day1} 02:00`,
                drp: 2.1
              }
            ];
          } else {
            this.chartData.rows = [
              {
                tm: this.getHourTime(1),
                drp: '0'
              },
              {
                tm: this.getHourTime(2),
                drp: '0'
              }
            ];
          }
        } else {
          if (this.stcd == 'SWZ_05821') {
            let day1 = this.$moment().format('MM-DD');
            let day2 = this.$moment(new Date())
              .add(1, 'days')
              .format('MM-DD');
            let day3 = this.$moment(new Date())
              .add(2, 'days')
              .format('MM-DD');
            let day4 = this.$moment(new Date())
              .add(3, 'days')
              .format('MM-DD');
            this.chartData.rows = [
              {
                tm: `${day1} 00:00`,
                drp: 1.2
              },
              {
                tm: `${day1} 03:00`,
                drp: 2.5
              },
              {
                tm: `${day1} 06:00`,
                drp: 3.4
              },
              {
                tm: `${day1} 09:00`,
                drp: 2.1
              },
              {
                tm: `${day1} 12:00`,
                drp: 4.7
              },
              {
                tm: `${day1} 15:00`,
                drp: 5.8
              },
              {
                tm: `${day1} 18:00`,
                drp: 7.7
              },
              {
                tm: `${day1} 21:00`,
                drp: 4.3
              },
              {
                tm: `${day2} 00:00`,
                drp: 3.1
              },
              {
                tm: `${day2} 03:00`,
                drp: 1.4
              },
              {
                tm: `${day2} 06:00`,
                drp: 2.1
              },
              {
                tm: `${day2} 09:00`,
                drp: 1.8
              },
              {
                tm: `${day2} 12:00`,
                drp: 1.5
              },
              {
                tm: `${day2} 15:00`,
                drp: 0.6
              },
              {
                tm: `${day2} 18:00`,
                drp: 0.1
              },
              {
                tm: `${day2} 21:00`,
                drp: 2.5
              },
              {
                tm: `${day3} 00:00`,
                drp: 1.4
              },
              {
                tm: `${day3} 03:00`,
                drp: 2.1
              },
              {
                tm: `${day3} 06:00`,
                drp: 1.8
              },
              {
                tm: `${day3} 09:00`,
                drp: 1.5
              },
              {
                tm: `${day3} 12:00`,
                drp: 0.6
              },
              {
                tm: `${day3} 15:00`,
                drp: 0
              },
              {
                tm: `${day3} 18:00`,
                drp: 0.1
              },
              {
                tm: `${day3} 21:00`,
                drp: 0.2
              },
              {
                tm: `${day4} 00:00`,
                drp: 0
              }
            ];
          } else {
            this.chartData.rows = [
              {
                tm: this.getHourTime(1),
                drp: '0'
              },
              {
                tm: this.getHourTime(12),
                drp: '0'
              },
              {
                tm: this.getHourTime(24),
                drp: '0'
              },
              {
                tm: this.getHourTime(36),
                drp: '0'
              },
              {
                tm: this.getHourTime(48),
                drp: '0'
              },
              {
                tm: this.getHourTime(60),
                drp: '0'
              },
              {
                tm: this.getHourTime(72),
                drp: '0'
              }
            ];
          }
        }
      } else if (this.tabActive == 1) {
        this.chartData = { rows: [], columns: [] };
        this.setting = { labelMap: {} };
        this.chartData.columns = ['day', 'drp'];
        this.setting.labelMap = {
          drp: '降雨量',
          day: '天数'
        };
        this.chartData.rows = [
          {
            drp: 6.8,
            day: 1
          },
          {
            drp: 5.1,
            day: 2
          },
          {
            drp: 5.5,
            day: 3
          },
          {
            drp: 5.0,
            day: 4
          },
          {
            drp: 6.0,
            day: 5
          },
          {
            drp: 3.8,
            day: 6
          },
          {
            drp: 5.9,
            day: 7
          },
          {
            drp: 7.9,
            day: 8
          },
          {
            drp: 5.4,
            day: 9
          },
          {
            drp: 3.9,
            day: 10
          },
          {
            drp: 7.8,
            day: 11
          },
          {
            drp: 6.9,
            day: 12
          },
          {
            drp: 4.6,
            day: 13
          },
          {
            drp: 7.4,
            day: 14
          },
          {
            drp: 4.8,
            day: 15
          },
          {
            drp: 3.0,
            day: 16
          },
          {
            drp: 6.0,
            day: 17
          },
          {
            drp: 6.0,
            day: 18
          },
          {
            drp: 6.9,
            day: 19
          },
          {
            drp: 6.2,
            day: 20
          },
          {
            drp: 3.5,
            day: 21
          },
          {
            drp: 7.9,
            day: 22
          },
          {
            drp: 7.9,
            day: 23
          },
          {
            drp: 5.8,
            day: 24
          },
          {
            drp: 4.4,
            day: 25
          },
          {
            drp: 5.5,
            day: 26
          },
          {
            drp: 5.3,
            day: 27
          },
          {
            drp: 4.6,
            day: 28
          },
          {
            drp: 4.2,
            day: 29
          },
          {
            drp: 4.0,
            day: 30
          }
        ];
      } else {
        this.getRainChart();
      }
    },
    getHourTime(hour) {
      let timestamp = new Date().getTime(); //当前的时间戳
      timestamp = timestamp + hour * 60 * 60 * 1000;
      return this.$moment(timestamp).format('YYYY-MM-DD HH') + ':00:00';
    },
    async rainPoint(data) {
      this.cleanAllLayer();
      this.closeOverlay();
      // console.log(data, 666);
      this.rainLayer = await drawJgRainPoint(data);
      this.map.addLayer(this.rainLayer);
    },
    //工情内涝-积水点
    async waterloggingPoint(waterPointsData) {
      this.waterlogging = await liftWaterloggingStationPoint(waterPointsData);
      this.map.addLayer(this.waterlogging);
    },
    //工情内涝-河网水位
    async drainagePoint(riverNetworkData, val) {
      this.drainage = await riverStationPoint(riverNetworkData, val);
      this.map.addLayer(this.drainage);
    },
    //工情内涝-潮位
    async seaLevelPoint(waterStationsData) {
      this.seaLevel = await seaLevelStationPoint(waterStationsData);
      this.map.addLayer(this.seaLevel);
    },
    //工情内涝-闸门
    async sluicePoint(waterLoggingData) {
      this.sluice = await sluiceStationPoint(waterLoggingData);
      this.map.addLayer(this.sluice);
    },
    //水情雨情-大中型水库
    async reservoirPoint(data) {
      this.clearReservoirLayer();
      this.reservoirLayer = setReservoirPoint(data);
      this.map.addLayer(this.reservoirLayer);
    },
    //水情雨情-最接近汛限大中型水库
    async sluiceTopArr(data) {
      this.clearReservoirLayer();
      this.sluiceTopArrLayer = setReservoirPoint(data);
      this.map.addLayer(this.sluiceTopArrLayer);
    },
    clearsluiceTopLayer() {
      this.map.removeLayer(this.sluiceTopArrLayer);
    },
    //清理水库
    clearReservoirLayer() {
      this.map.removeLayer(this.reservoirLayer);
    },
    //清理所有图层
    cleanAllLayer() {
      this.map.removeLayer(this.rainLayer);
    },
    clearIsohyetLayer() {
      if (this.isohyetLayer) {
        this.map.removeLayer(this.isohyetLayer);
      }
    },
    //加载等雨量面
    showIsohyetLayer(mapImage) {
      this.clearIsohyetLayer();
      this.isohyetLayer = new ImageLayer({
        source: new ImageStatic({
          url: mapImage.href, //这里添加静态图片的地址
          projection: 'EPSG:4326',
          imageExtent: [mapImage.extent.xmin, mapImage.extent.ymin, mapImage.extent.xmax, mapImage.extent.ymax] //WGS-84 经纬度数据范围
        }),
        opacity: 0.5
      });
      this.map.addLayer(this.isohyetLayer);
    },
    riverLayerColorSet(layerName) {
      let layerColor = 'rgb(255,203,56)';
      switch (layerName) {
        case '人工水道临水线点':
          layerColor = 'rgb(255,203,56)';
          break;
        case '人工水道临水线':
          layerColor = 'rgb(255,203,56)';
          break;
        case '人工水道中心线':
          layerColor = 'rgb(255,56,56)';
          break;
        case '人工水道水面线':
          layerColor = 'rgb(9,188,254)';
          break;
        case '人工水道管理范围线':
          layerColor = 'rgb(71,31,162)';
          break;
        case '人工水道水域面':
          layerColor = 'rgb(255,203,56)';
          break;

        case '山塘临水线点':
          layerColor = 'rgb(0,173,0)';
          break;
        case '山塘临水线':
          layerColor = 'rgb(0,173,0)';
          break;
        case '山塘管理范围线':
          layerColor = 'rgb(71,31,162)';
          break;
        case '山塘水面线':
          layerColor = 'rgb(9,188,254)';
          break;
        case '山塘水域面':
          layerColor = 'rgb(0,173,0)';
          break;

        case '其他水域临水线点':
          layerColor = 'rgb(117,179,255)';
          break;
        case '其他水域临水线':
          layerColor = 'rgb(117,179,255)';
          break;
        case '其他水域管理范围线':
          layerColor = 'rgb(71,31,162)';
          break;
        case '其他水域水面线':
          layerColor = 'rgb(9,188,254)';
          break;
        case '其他水域水域面':
          layerColor = 'rgb(117,179,255)';
          break;

        case '水库临水线点':
          layerColor = 'rgb(20,138,255)';
          break;
        case '水库临水线':
          layerColor = 'rgb(20,138,255)';
          break;
        case '水库管理范围线':
          layerColor = 'rgb(71,31,162)';
          break;
        case '水库水面线':
          layerColor = 'rgb(9,188,254)';
          break;
        case '水库水域面':
          layerColor = 'rgb(20,138,255)';
          break;

        case '河道临水线点':
          layerColor = 'rgb(21,238,241)';
          break;
        case '河道临水线':
          layerColor = 'rgb(21,238,241)';
          break;
        case '河道管理范围线':
          layerColor = 'rgb(71,31,162)';
          break;
        case '河道中心线':
          layerColor = 'rgb(255,56,56)';
          break;
        case '河道水面线':
          layerColor = 'rgb(9,188,254)';
          break;
        case '暗河':
          layerColor = 'rgb(255,126,56)';
          break;
        case '其他沟渠':
          layerColor = 'rgb(155,255,67)';
          break;
        case '河道水域面':
          layerColor = 'rgb(21,238,241)';
          break;

        case '水域断面点':
          layerColor = 'rgb(129,80,255)';
          break;
        case '水域断面线':
          layerColor = 'rgb(129,80,255)';
          break;
      }
      return layerColor;
    },
    //分批次请求GIS服务器，加载海量矢量要素数据
    lodeRiverFeatureLayers(data) {
      // console.log(data);
      //获取图层名称，设置颜色值
      let layerName = data.label;
      let layerColor = this.riverLayerColorSet(layerName);
      //点线面样式设置
      let layerDataSet = data.id.split('_')[0];
      let esrijsonFormat = new EsriJSON();
      let styleCache = {
        Point: new Style({
          image: new Circle({
            radius: 3,
            fill: new Fill({
              color: layerColor
            })
          })
        }),
        LineString: new Style({
          stroke: new Stroke({
            color: layerColor,
            width: 0.4
          })
        }),
        Polygon: new Style({
          fill: new Fill({
            color: layerColor
          })
        })
      };
      //图层请求，优化矢量加载
      let vectorSource = new VectorSource({
        loader: function(extent, resolution, projection) {
          var url =
            riverLayesBaseService +
            layerDataSet +
            '/MapServer/' +
            data.index +
            '/query/?f=json&' +
            'returnGeometry=true&spatialRel=esriSpatialRelIntersects&geometry=' +
            encodeURIComponent(
              '{"xmin":' +
                extent[0] +
                ',"ymin":' +
                extent[1] +
                ',"xmax":' +
                extent[2] +
                ',"ymax":' +
                extent[3] +
                ',"spatialReference":{"wkid":4326}}'
            ) +
            '&geometryType=esriGeometryEnvelope&inSR=4326&outFields=*' +
            '&outSR=4326';

          $.ajax({
            url: url,
            dataType: 'jsonp',
            success: function(response) {
              if (response.error) {
                console.log(response.error.message + '\n' + response.error.details.join('\n'));
              } else {
                // 读取结果
                var features = esrijsonFormat.readFeatures(response, {
                  featureProjection: projection
                });
                if (features.length > 0) {
                  vectorSource.addFeatures(features);
                }
              }
            }
          });
        },
        strategy: tileStrategy(
          createXYZ({
            tileSize: 512
          })
        )
      });
      // 矢量图层
      let layerVariable = new VectorLayer({
        source: vectorSource,
        style: function(feature) {
          let layerType = feature.getGeometry().getType();
          if (layerType.indexOf('LineString') != -1) layerType = 'LineString';
          if (layerType.indexOf('Polygon') != -1) layerType = 'Polygon';
          return styleCache[layerType];
        },
        id: data.id
      });
      this.map.addLayer(layerVariable);
      return layerVariable;
    }
  }
};
</script>
<style lang="scss" scoped>
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
    margin: 10px 0 0 0;
    .go-btn {
      display: inline-block;
      height: 32px;
      padding: 0 20px;
      line-height: 32px;
      text-align: center;
      color: #fff;
      font-size: 14px;
      cursor: pointer;
      background: url(../../../../assets/images/bigScreen-mini-tab-warn.png) no-repeat;
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
        background: url('../../../../assets/images/popup-bg2.png') no-repeat left top;
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
      background: url('../../../../assets/images/popup-project-bg.png') no-repeat center;
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
      background: url('../../../../assets/images/popup-rain-bg.png') no-repeat center;
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
        background: url(../../../../assets/images/bigScreen-mini-tab.png) no-repeat;
        background-size: 100% 100%;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
        &:last-child {
          margin-right: 0;
        }
        &.active {
          background: url(../../../../assets/images/bigScreen-mini-tab-check.png) no-repeat;
          color: #a6dff6;
          background-size: 100% 100%;
        }
      }
    }
  }
}
</style>
<style lang="scss">
// 点位提示框
.ol-popup {
  position: absolute;
  padding: 8px 15px;
  border-radius: 10px;
  bottom: 12px;
  left: -50px;
  font: italic bold 12px;
  background-color: rgba(0, 0, 0, 0.8);
  border: 1px solid #48fff4;
  color: #48fff4;
  .popup-name {
    font-size: 20px;
    line-height: 34px;
  }
}
.ol-popup:after,
.ol-popup:before {
  top: 100%;
  border: solid transparent;
  content: ' ';
  height: 0;
  width: 0;
  position: absolute;
  pointer-events: none;
}
.ol-popup:after {
  border-top-color: #3ab378;
  border-width: 10px;
  left: 48px;
  margin-left: -10px;
}
.ol-popup:before {
  border-top-color: #3ab378;
  border-width: 11px;
  left: 48px;
  margin-left: -11px;
}
#popup-content {
  display: inline-block;
  width: 180px;
  line-height: 26px;
}
</style>
