<!--
 * @Date: 2022-05-11 15:10:23
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-31 17:22:56
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\ScreenFloodMap.vue
-->
<template>
  <div class="map-wrap">
    <div
      ref="ControlMainView"
      class="map"
      id="ControlMainView"
      :class="SpotsFlag ? (SpotsFlagType == 0 ? 'cursorImg' : SpotsFlagType == 1 ? 'analysisImg' : 'rainfallImg') : ''"
    />
 

    <!-- 影像底图切换时间栏 -->
    <div :style="timelineStyle" class="playable-timeline-bar is-big-screen" v-if="timelinebarShow">
      <playable-timeline-bar
        dark
        ref="playTimeLine"
        :timeline-list="timelineList"
        :before-play-next="playNext"
        @change="handleTimelineChange"
        :defaultIndex="defaultIndex"
        :hidePlay="true"
      />
    </div>

    <!-- 降雨雷达时间轴 -->
    <div
      v-if="radarLineShow"
      :style="timelineStyle"
      :class="['playable-timeline-bar', isBigScrren ? 'is-big-screen' : '']"
    >
      <playable-timeline-bar
        dark
        ref="RaderCloundBar"
        :timeline-list="raderTimeArr"
        @change="handleRaderTimeChange"
        :defaultIndex="0"
        :hidePlay="true"
      />
    </div>

    <screen-footer :map-bind="map" @doSceneSave="doSceneSave" />
    <!-- 点位悬浮框 -->
    <div v-show="popupShow || overlayEnterFlag" id="popup" class="ol-popup">
      <!-- , isBigScrren && 'is-big-screen' -->
      <div :class="['ol-popup__wrapper']">
        <div class="ol-popup__title">{{ featureInfo.name }}</div>
        <div class="ol-popup__content">
          <div class="ol-popup__content--props">
            <div :key="key" v-for="(prop, key) of featureInfo.props" class="props-item">
              <span class="item-label">{{ key }}</span>
              <span class="item-data" :style="{ color: key == '汛限水位' ? '#eec80b' : '#1cfff5' }">
                {{ prop ? prop : '-' }}
              </span>
            </div>
            <div v-if="featureInfo.label" class="props-label">
              <span> {{ featureInfo.label }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
   
    <!-- 调度方案仓库悬浮框 -->
    <div
      v-show="schemeWarehousePoint"
      ref="schemeWarehousePopup"
      id="schemeWarehousePopup"
      class="ol-popup scheme-popup"
    >
      <div id="popup-content" class="ol-popup__wrapper">
        <div class="scheme-point_title">{{ schemeWarehouseInfo.name }}</div>
        <div class="scheme-point_content">
          <div v-for="(value, key) in schemeWarehouseInfo" :key="key">
            <template v-if="key !== 'name'">
              <div class="scheme-point_sub_box">
                <p class="scheme-point_sub_title">{{ key }}</p>
                <i class="icon-popup-divide"></i>
              </div>
              <p v-for="(item, index) in value" :key="index">
                <span>{{ item.label || item.materialName }}</span>
                <span>{{ item.value || item.num }}</span>
              </p>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- 调度方案队伍悬浮框 -->
    <div v-show="schemeRanksPoint" ref="schemeRanksPopup" id="schemeRanksPopup" class="ol-popup scheme-popup">
      <div id="popup-content" class="ol-popup__wrapper">
        <div class="scheme-point_title">{{ schemeRanksInfo.name }}</div>
        <!-- <i class="close_poin">关闭</i> -->
        <div class="scheme-point_content">
          <div v-for="(value, key) in schemeRanksInfo" :key="key">
            <template v-if="key !== 'name'">
              <div class="scheme-point_sub_box">
                <p class="scheme-point_sub_title">{{ key }}</p>
                <i class="icon-popup-divide"></i>
              </div>
              <p v-for="(item, index) in value" :key="index">
                <span>{{ item.label || item.materialName }}</span>
                <span>{{ item.value || item.num }}</span>
              </p>
            </template>
          </div>
        </div>
      </div>
    </div>
    <div v-show="waterSourPopupShow" id="waterSourPopup" class="ol-popup">
      <div :class="['ol-popup__wrapper', 'waterSour-popup']">
        <div v-if="featureAttr != null && featureAttr.type == '降水量'" style="width: 180px">
          <el-row :gutter="20">
            <el-col :span="24" class="item">
              <span>{{ featureAttr.name }}</span>
            </el-col>
            <el-col :span="24" class="item">
              <span>年平均降水量</span>
              <span>{{ featureAttr.avg.toFixed(2) }} mm</span>
            </el-col>
            <el-col :span="24" class="item">
              <span>降水量</span>
              <span>{{ featureAttr.data }} mm</span>
            </el-col>
            <el-col :span="24" class="item">
              <span>占比</span>
              <span>{{ featureAttr.precent }} %</span>
            </el-col>
          </el-row>
        </div>
        <div
          v-else-if="featureAttr != null && featureAttr.type == '水资源量'"
          :style="{ width: featureAttr.active == 0 ? '180px' : '280px' }"
        >
          <el-row :gutter="20">
            <el-col :span="24" class="item">
              <span>{{ featureAttr.adnm }}</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 0">
              <span>水资源总量</span>
              <span>{{ featureAttr.totalWaterResource || '-' }} 亿m³</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 0">
              <span>较上一年</span>
              <span>{{ featureAttr.twrCompareLastYearRate || '-' }} %</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 0">
              <span>占全市水资源总量</span>
              <span>{{ featureAttr.twrOfCityRate || '-' }} %</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>人均水资源量</span>
              <span>{{ featureAttr.watResPerCapita || '-' }} m³</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>较上一年</span>
              <span>{{ featureAttr.wrpcCompareLastYearRate || '-' }} %</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>相对于温州人均</span>
              <span
                >{{ featureAttr.wzWrpc || '-' }} m³
                {{ featureAttr.wrpcSubWzPosNeg == true ? '多' : '少' }}
                <span>{{ featureAttr.wrpcSubWzAbsVal || '-' }}m³</span></span
              >
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>相对于浙江省人均</span>
              <span
                >{{ featureAttr.zjWrpc || '-' }} m³
                {{ featureAttr.wrpcSubZjPosNeg == true ? '多' : '少' }}
                <span>{{ featureAttr.wrpcSubZjAbsVal || '-' }}m³</span></span
              >
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>相对于全国人均</span>
              <span
                >{{ featureAttr.cnWrpc || '-' }} m³
                {{ featureAttr.wrpcSubCnPosNeg == true ? '多' : '少' }}
                <span>{{ featureAttr.wrpcSubCnAbsVal || '-' }}m³</span></span
              >
            </el-col>
          </el-row>
        </div>
        <div
          v-else-if="featureAttr != null && featureAttr.type == '用水量'"
          :style="{ width: featureAttr.active == 1 ? '180px' : '280px' }"
        >
          <el-row :gutter="20">
            <el-col :span="24" class="item">
              <span>{{ featureAttr.adnm }}</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>用水量</span>
              <span>{{ featureAttr.totalConsume || '-' }} 亿m³</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>较上一年</span>
              <span>{{ featureAttr.tcCompareLastYearRate || '-' }} %</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 1">
              <span>占全市用水总量</span>
              <span>{{ featureAttr.tcOfCityRate || '-' }} %</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 2">
              <span>人均综合用水量</span>
              <span>{{ featureAttr.watConPerCapita || '-' }} m³</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 2">
              <span>较上一年</span>
              <span>{{ featureAttr.wcpcCompareLastYearRate || '-' }} %</span>
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 2">
              <span>相对于温州市</span>
              <span
                >{{ featureAttr.wzWcpc || '-' }} m³
                {{ featureAttr.wcpcSubWzPosNeg == true ? '多' : '少' }}
                <span>{{ featureAttr.wcpcSubWzAbsVal || '-' }}m³</span></span
              >
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 2">
              <span>相对于浙江省</span>
              <span
                >{{ featureAttr.zjWcpc || '-' }} m³
                {{ featureAttr.wcpcSubZjPosNeg == true ? '多' : '少' }}
                <span>{{ featureAttr.wcpcSubZjAbsVal || '-' }}m³</span></span
              >
            </el-col>
            <el-col :span="24" class="item" v-if="featureAttr.active == 2">
              <span>相对于全国</span>
              <span
                >{{ featureAttr.cnWcpc || '-' }} m³
                {{ featureAttr.wcpcSubCnPosNeg == true ? '多' : '少' }}
                <span>{{ featureAttr.wcpcSubCnAbsVal || '-' }}m³</span></span
              >
            </el-col>
          </el-row>
        </div>
        <div v-if="featureAttr == null">
          <div class="top-box">
            <img :src="featureInfo.props && featureInfo.props.imgSrc" alt="" />
            <span>{{ featureInfo.name }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12" class="item">
              <span>水源类型</span>
              <span>{{ featureInfo.props && featureInfo.props._type }}</span>
            </el-col>
            <el-col :span="12" class="item">
              <span>目标水质</span>
              <span>{{ featureInfo.props && featureInfo.props.target }}</span>
            </el-col>
            <el-col :span="24" class="item">
              <span>水量预警</span>
              <span
                :style="{
                  color: setWaterWarnColor(featureInfo.props && featureInfo.props.warn)
                }"
                >{{ featureInfo.props && featureInfo.props.warn }}</span
              >
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div v-show="waterSecurityPopupShow" id="waterSecurityPopup" class="ol-popup">
      <div :class="['ol-popup__wrapper', 'waterSour-popup']">
        <!-- <div class="top-box">
          <span>{{  }}</span>
        </div> -->
        <el-row>
          <el-col :span="24">
            <span>{{ featureInfo.name }}</span>
          </el-col>
          <el-col :span="24" class="item">
            <span>供水能力</span>
            <span>{{ featureInfo.supplyAbility }}天</span>
          </el-col>
        </el-row>
      </div>
    </div>
    <div v-show="waterRegulationPopupShow" id="waterRegulationPopup" class="ol-popup">
      <div :class="['ol-popup__wrapper', 'waterSour-popup']" style="min-width: 15.375rem">
        <el-row>
          <el-col :span="24">
            <span>取水口名称：</span>
            <span>{{ featureInfo.wainName }}</span>
          </el-col>
          <el-col :span="24" class="item">
            <span>实时取水量（m³）：</span>
            <span>{{ featureInfo.intake }}</span>
          </el-col>
          <el-col :span="24" class="item">
            <span>更新时间：</span>
            <span>{{ featureInfo.tm }}</span>
          </el-col>
        </el-row>
      </div>
    </div>

    <div v-show="clusterPointPopupShow" id="clusterPointPopup" class="ol-popup">
      <div :class="['ol-popup__wrapper', 'cluster-point-popup']">
        <div class="cluster-point-list">
          <div
            class="cluster-point-item"
            v-for="item in clusterPointList"
            :key="item.name"
            @click="clusterPointClick(item)"
          >
            {{ item.name }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import esri2geo from 'esri2geo';
import { Fill, Stroke, Style } from 'ol/style';
import 'ol/ol.css';
import XYZ from 'ol/source/XYZ';
import WKT from 'ol/format/WKT';
import TileLayer from 'ol/layer/Tile';
import ImageLayer from 'ol/layer/Image';
import ImageStatic from 'ol/source/ImageStatic';
import { Map, View, Overlay, Feature } from 'ol';
import { defaults as defaultControls } from 'ol/control';
import { Vector as VectorSource, TileArcGISRest } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';
import GeoJSON from 'ol/format/GeoJSON';
import mapConfig from '@/config/mapConfig';
import { ScreenFooter, PlayableTimelineBar } from '@/components';
import Text from 'ol/style/Text';
import { WindLayer } from 'ol-wind';
import $ from 'jquery';
import {
  drawClusterPoint,
  drawPoint,
  drawLines,
  addLayerClick,
  newTyphoon,
  showTestClusterPoint,
  drawLineEx,
  drawPolygon,
  drawRoutePoint,
  drawLine,
  drawAreaPolygon
} from '@/utils/mapUtil.js';
import { queryMapserver } from '@/api/loadMapServe';
import { mapGetters, mapActions } from 'vuex';
import axios from 'axios';
import { boundingExtent } from 'ol/extent';
import printJS from 'print-js';
import baseGeoJson from '@/assets/geojson/extent_FeaturesToJSON.json';
import luchengGeoJson from '@/assets/geojson/lucheng_FeaturesToJSON.json';

const searchAreaList = [
  '鹿城区',
  '龙湾区',
  '瓯海区',
  '洞头区',
  '永嘉县',
  '平阳县',
  '苍南县',
  '文成县',
  '泰顺县',
  '瑞安市',
  '乐清市',
  '温州市'
];
const centerPoint = [
  [120.55389798474596, 28.068232997940203],
  [120.81894315076158, 27.89931820301833],
  [120.54703152966783, 27.963862880752703],
  [121.03317654919908, 27.919917568252703],
  [120.66376126599596, 28.337398037002703],
  [120.36143482403382, 27.64214415802697],
  [120.44959860797451, 27.440954047283753],
  [120.00699559080968, 27.82237370619345],
  [119.85989517628137, 27.524267556397135],
  [120.43658087217555, 27.839296762732104],
  [121.01717188880941, 28.276692685577352],
  [120.57336116457707, 27.55135084435864],
  [120.54195024899947, 28.00796028614328]
];

export default {
  name: 'ScreenFloodMap',
  components: { ScreenFooter, PlayableTimelineBar },
  props: {},
  data() {
    return {
      //地图
      map: null,
      //点位信息
      featureInfo: {},
      popupShow: false,
      isohyetLayer: null,
      typhoonLayers: [],
      typhoonNames: [],
      typhoonTimes: [],
      textLayer: null,
      lineLayer: [],
      weatherInfoList: [
        { name: '鹿城区', show: true },
        { name: '龙湾区', show: true },
        { name: '瓯海区', show: true },
        { name: '洞头区', show: true },
        { name: '永嘉县', show: true },
        { name: '平阳县', show: true },
        { name: '苍南县', show: true },
        { name: '文成县', show: true },
        { name: '泰顺县', show: true },
        { name: '瑞安市', show: true },
        { name: '乐清市', show: true },
        { name: '温州市', show: true }
      ],
      weatherOverlay: [],
      areaLayer: null,
      basinLayer: null,
      timelineList: [
        { label: '2010', value: 2010 },
        { label: '2012', value: 2012 },
        { label: '2014', value: 2014 },
        { label: '2016', value: 2016 },
        { label: '2017', value: 2017 },
        { label: '2018', value: 2018 },
        { label: '2019', value: 2019 }
      ],
      defaultIndex: 6,
      areaWKTLayer: null,
      lineFocusLayer: [],
      dynamicPointMatain: {},
      featureAttr: {},
      overlayEnterFlag: false,
      routeLayer: null, //路线图层
      lineLayer_team: null, //人员调度路线图层
      lineLayer_warehouse: null, //仓库调度路线图层
      pointLayer_team_start: null, //队伍起点
      pointLayer_warehouse_start: null, //仓库起点
      typhoonsMap: new Map(),
      typhoonPoint: false, //台风点位点击弹框显示标记
      pointOverlay: null, //台风点位点击弹框
      typhoonPointData: {}, //台风点位详情
      typhoonName: '',
      schemeWarehousePoint: false, // 调度方案仓库点位
      schemeWarehouseOverlay: null, // 调度方案仓库弹框
      schemeWarehouseInfo: {}, // 调度方案仓库信息
      schemeRanksPoint: false, // 调度方案队伍点位
      schemeRanksOverlay: null, // 调度方案队伍弹框
      schemeRanksInfo: {}, // 调度方案队伍信息
      designFloodLayer: {},
      windLayers: null,
      waveLayers: null,
      cloundLayer: null,
      radarRainLayer: null,
      overlay1: null,
      waterSourPopupShow: false,
      overlay2: null,
      waterSecurityPopupShow: false,
      overlay3: null,
      waterRegulationPopupShow: false,
      clusterPointPopupShow: false,
      clusterPointList: [],
      clusterPointOverLay: null,
      probabilityCircleLayer: null,
      raderTimeArr: [], //雷达降雨时间轴数据
      radarLineShow: false, //雷达降雨时间轴标记
      windWaveBaseLayers: null, //风场浪场背景图层
      isolineLayer: null, //等值线图层
      distributionMapInfo: {} //
    };
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      this.mapInit();
      // document.addEventListener("load", () => {
      //   this.getElementPagePosition(document.getElementsByClassName('oujiang')[0])
      // });
    });
  },
  computed: {
    ...mapGetters([
      'weatherPopover',
      'currentScene',
      'timelinebarShow',
      'currentScene',
      'CheckedArr',
      'DispatchCarousel',
      'GoodsList',
      'SpotsFlag',
      'SpotsFlagType',
      'IsAction',
      'BsName',
      'ForecastPointFlag',
      'RadarRainList',
      'sluiceDistributionMap',
      'currentSelectArea'
    ]),
    isBigScrren() {
      return this.$route.query.large == 'true';
    },
    timelineStyle() {
      return this.currentScene == '防御复盘' ? { bottom: '200px' } : { bottom: '100px' };
    },
    distributionMapShow: () => {
      if (this.$store.state.sluiceDistributionMap) {
        this.getElementPagePosition(this.$refs.oujiangBox);
        return true;
      } else {
        return false;
      }
    }
  },
  watch: {
    currentScene: {
      immediate: true,
      handler() {
        this.clearTyphoonLayer();
      }
    },

    timelinebarShow(val) {
      const layer = new TileLayer({
        id: 'wz2019',
        zIndex: 99,
        source: new TileArcGISRest(
          {
            url: 'http://10.36.161.208:16080/wzgt22/754677ad50484012ba9a9ccb0946aa18/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2019/MapServer'
          },
          { zoomOffset: 1 }
        )
      });
      if (val) {
        this.map.addLayer(layer);
      } else {
        this.map.removeLayer(layer);
      }
    },
    '$store.state.analyseData': function (val) {
      if (val == 'custom') {
        this.clearTyphoonLayer();
        this.$store.commit('typhoonDataList', {});
      }
    },
    RadarRainList(val) {
      this.raderTimeArr = [];
      val.map((item) => {
        let obj = {
          label: new Date(item.monitorTime).format('MM-dd hh:mm'),
          value: item.url
        };
        this.raderTimeArr.push(obj);
      });
    },
    '$store.state.sluiceDistributionMap': async (newVal) => {
      if (newVal === true) {
        this.getLatestData();
        this.getLatestGatage();
      }
    }
  },
  methods: {
    ...mapActions([
      'setWeatherPopover',
      'changeArea',
      'changeTimelinebarShow',
      'SetTeamDispatchList',
      'SetWarehouseDispatchList',
      'SetDispatchCarousel',
      'SetTyphoonPointData',
      'SetSpotsFlag',
      'SetIsAction',
      'changeScreenLoading',
      'getForecastRainfallLayer'
    ]),

    /** 天气卡片点击 */
    handleWeatherPopupClick(item) {
      setTimeout(() => {
        this.setWeatherPopover(true);
      }, 300);
      this.changeArea(item.areaName);
    },
    /**
     * 初始化地图
     */
    mapInit() {
      // //地图填充图层
      // let SLTCLayer = new TileLayer({
      //   id: 'SLTCLayer',
      //   source: new XYZ({
      //     url: mapConfig.mapServerTwo + 'arcgis/rest/services/lucheng/lcPoint_Polygon/MapServer/tile/{z}/{y}/{x}',
      //     projection: 'EPSG:4326'
      //   })
      // });
      // http://223.4.72.162:18099/WZSSPT/arcgis/rest/services/basemap/WYX1029/MapServer
      //地图主图层
      // const mainLayer = new TileLayer(
      //   {
      //     id: 'zhejiangmainSLLayer',
      //     source: new XYZ({
      //       url: 'https://sldtpt.zjwater.com:6443/WZSSPT/arcgis/rest/services/basemap/WYX1029/MapServer/tile/{z}/{y}/{x}',
      //       projection: 'EPSG:4326'
      //     })
      //   },
      //   { zoomOffset: 1 }
      // );
      const mainLayer = new TileLayer(
        {
          id: 'zhejiangluchengmainSLLayer',
          source: new XYZ({
            url:
              mapConfig.mapServerTwo +
              'wzgt25/147a2cb528474bc78ba28632c16d037c/arcgis/rest/services/CGCS2000/2020yx_cgcs2000/MapServer/tile/{z}/{y}/{x}',
            projection: 'EPSG:4326'
          })
        },
        { zoomOffset: 1 }
      );
      // let SLMCLayer = new TileLayer({
      //   id: "zhejiangmainSLMCLayer",
      //   zIndex: 2,
      //   source: new XYZ({
      //     /* mapConfig.mapServer + "wenzhou/wzmengc/MapServer/tile/{z}/{y}/{x}" */
      //     url: mapConfig.mapServer + "wenzhou/newwzmcls/MapServer/tile/{z}/{y}/{x}",
      //     projection: "EPSG:4326",
      //   }),
      // });
      // let SLMCLayer = new TileLayer({
      //   id: 'zhejiangmainSLMCLayer',
      //   zIndex: 2,
      //   source: new XYZ({
      //     url: mapConfig.mapServer + 'wenzhou/shuiliditu/MapServer/tile/{z}/{y}/{x}',
      //     projection: 'EPSG:4326'
      //   })
      // });
      // 天地图矢量底图
      // let tiandituVec = new TileLayer({
      //   id: 'tiandituVec',
      //   zIndex: 0,
      //   source: new XYZ({
      //     url: 'http://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b',
      //     projection: 'EPSG:4326'
      //   })
      // });
      // // 天地图矢量底图文字标记
      // let tiandituCva = new TileLayer({
      //   id: 'tiandituVec',
      //   zIndex: 0,
      //   source: new XYZ({
      //     url: 'http://t0.tianditu.gov.cn/DataServer?T=cva_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b',
      //     projection: 'EPSG:4326'
      //   })
      // });

      // view设置
      let viewSetting = new View({
        projection: 'EPSG:4326',
        center: [120.68112339344061, 28.032333108165375],
        zoom: 12.5,
        maxZoom: 20,
        minZoom: 3
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: 'ControlMainView',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer],
        // layers: [tiandituVec, tiandituCva, mainLayer],
        view: viewSetting
      });

      //画布样式
      // setTimeout(() => {
      //   const img = new Image();
      //   img.src = require("@/assets/images/mengceng.png");
      //   console.log(this.map.getViewport().querySelector('canvas'));
      //   const canvas = this.map.getViewport().querySelector('canvas');
      //   const ctx = canvas.getContext('2d');
      //   this.map.on('postcompose', function(){
      //     // 可以实现动画效果
      //     ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
      //     this.render();
      //   }, this.map);
      // },100)

      // this.drawPoint();
      this.registeredEvent(); //注册事件
      this.addStationOverlay();
      this.addTyphoonPointOverlay();
      this.addWaterSourOverlay();
      this.addWaterSecurityOverlay();
      this.addWaterRegulationOverlay();
      this.addClusterPointOverLay();
      this.addLuchengGeoJsonLayer();
    },
    /**
     * 设置摄像机位置
     */
    setView(lng, lat) {
      const view = this.map.getView();
      view.animate({ center: [lng, lat], duration: 200, zoom: 14 });
    },
    addStationOverlay() {
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#popup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.overlay);
    },
    addWaterSourOverlay() {
      this.overlay1 = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#waterSourPopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.overlay1);
    },
    // 水源保障
    addWaterSecurityOverlay() {
      this.overlay2 = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#waterSecurityPopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.overlay2);
    },
    // 取水监管
    addWaterRegulationOverlay() {
      this.overlay3 = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#waterRegulationPopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.overlay3);
    },
    //多点位时悬浮窗
    addClusterPointOverLay() {
      this.clusterPointOverLay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#clusterPointPopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.clusterPointOverLay);
      document.getElementById('clusterPointPopup').addEventListener('pointermove', (e) => {
        this.waterRegulationPopupShow = true;
        e.stopPropagation();
      });
    },
    addTyphoonPointOverlay() {
      this.pointOverlay = new Overlay({
        element: this.$refs.typhoonPopup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250
        }
      });
      this.map.addOverlay(this.pointOverlay);
    },
    addWeatherOverlay(coodinateList) {
      this.weatherInfoList = [];
      for (let item of coodinateList) {
        this.weatherInfoList.push({ ...item, show: true });
      }
      // console.log(999, this.weatherInfoList);
      this.$nextTick(() => {
        this.weatherInfoList.forEach((item, index) => {
          // console.log(document.getElementById(`weather-popup-${index}`))
          this.weatherOverlay[index] = new Overlay({
            //设置弹出框的容器
            element: document.querySelector(`#weather-popup-${index}`),
            positioning: 'bottom-center',
            autoPan: true,
            autoPanAnimation: {
              duration: 250
            }
          });
          this.map.addOverlay(this.weatherOverlay[index]);
          this.weatherOverlay[index].setPosition(item.coodinate);
        });
      });
    },
    //添加经济评价/人口评价的overlay
    addFixedOverlay(list, type, tab) {
      const searchAreaList = [
        '鹿城区',
        '龙湾区',
        '瓯海区',
        '洞头区',
        '永嘉县',
        '平阳县',
        '苍南县',
        '文成县',
        '泰顺县',
        '瑞安市',
        '乐清市',
        '龙港市'
      ];
      for (let i = 0; i < list.length; i++) {
        let typeClass = '';
        if (list[i].valueType && list[i].valueType == 1) {
          typeClass = 'fixed-ovly-green';
        } else if (list[i].valueType && list[i].valueType == 2) {
          typeClass = 'fixed-ovly-blue';
        }
        let domText = `<div class="fixed-ovly ${typeClass}" id='${type + i}'>
            <p>${list[i].adnm}</p>
            <p>${tab == '人口评价' ? '可支撑人口' : '可支撑经济'}
              <span>
                ${tab == '人口评价' ? list[i].supportPop + '万人' : list[i].supportEco + '万元'}
              </span>
            </p>
          </div>`;
        let idx = searchAreaList.indexOf(list[i].adnm);
        let coodinate = idx == -1 ? [] : centerPoint[idx];
        $('#ControlMainView').append(domText);
        this.$nextTick(() => {
          let overlay = new Overlay({
            id: type + i,
            element: document.getElementById(type + i),
            positioning: 'bottom-center',
            autoPan: true,
            autoPanAnimation: {
              duration: 250
            },
            stopEvent: false,
            position: coodinate
          });
          this.map.addOverlay(overlay);
        });
      }
    },
    //添加气象预警的overlay
    addWeatherWarnOverlay(list, type) {
      for (let i = 0; i < list.length; i++) {
        let domText = `
          <div
            id="weather-popup-${i}"
            class="ol-weather-popup"
          >
            <div>
              <div class="ol-weather-popup__content">
                <div class="weather-icon">
                  <i class="${list[i].icon}"></i>
                </div>
                <div class="weather-label">
                  <div class="label-name">
                    <span>${list[i].areaName}</span>
                    <span>${list[i].temperature}℃</span>
                  </div>
                  <span class="weather">${list[i].weather}</span>
                </div>
              </div>
            </div>
            <div class="weather-details">
              <div>
                <p>
                  <i
                    class="icon-waether-pre-pro"
                    style="vertical-align: middle; margin-right: 4px"
                  ></i>
                  <span>${list[i].humidity || '-'}%</span>
                </p>
                <p>
                  <i
                    class="icon-weather-pre"
                    style="vertical-align: middle; margin-right: 4px"
                  ></i>
                  <span>${list[i].rain || '-'}mm</span>
                </p>
              </div>
              <div>
                <p>
                  <i
                    class="icon-weather-air-pre"
                    style="vertical-align: middle; margin-right: 4px"
                  ></i>
                  <span>${list[i].airPressure || '-'}hPa</span>
                </p>
                <p>
                  <i
                    class="icon-weather-visibility"
                    style="vertical-align: middle; margin-right: 4px"
                  ></i>
                  <span>${list[i].visibility || '-'}m</span>
                </p>
              </div>
              <p style="vertical-align: middle; margin-top: 7px">
                <i
                  class="icon-footer-wind"
                  style="vertical-align: middle; margin-right: 4px"
                ></i>
                <span style="margin-right: 4px">${list[i].winds || '-'}</span>
                <span>${list[i].currentWindSpeed || '-'}m/s</span>
              </p>
            </div>
          </div>
        `;
        let idx = searchAreaList.indexOf(list[i].areaName);
        let coodinate = idx == -1 ? [] : centerPoint[idx];
        $('#ControlMainView').append(domText);
        this.$nextTick(() => {
          let overlay = new Overlay({
            id: type + i,
            element: document.getElementById(type + i),
            positioning: 'bottom-center',
            autoPan: true,
            autoPanAnimation: {
              duration: 250
            },
            stopEvent: false,
            position: coodinate
          });
          this.map.addOverlay(overlay);
        });
      }
    },
    //添加节水指标的overlay
    addSavingActionFixedOverlay(list, type) {
      for (let i = 0; i < list.length; i++) {
        let typeClass = '';
        if (list[i].valueType && list[i].valueType == 1) {
          typeClass = 'fixed-ovly-green';
        } else if (list[i].valueType && list[i].valueType == 2) {
          typeClass = 'fixed-ovly-blue';
        }
        let domText = `<div class="fixed-ovly ${typeClass}" id='${type + i}'>
            <p>${list[i].adnm}</p>
            <p>${list[i].type}
              <span>
                ${list[i].value + list[i].unit}
              </span>
            </p>
          </div>`;
        let idx = searchAreaList.indexOf(list[i].adnm);
        let coodinate = idx == -1 ? [] : centerPoint[idx];
        $('#ControlMainView').append(domText);
        this.$nextTick(() => {
          let overlay = new Overlay({
            id: type + i,
            element: document.getElementById(type + i),
            positioning: 'bottom-center',
            autoPan: true,
            autoPanAnimation: {
              duration: 250
            },
            stopEvent: false,
            position: coodinate
          });
          this.map.addOverlay(overlay);
        });
      }
    },
    removeOverlayByName(name) {
      let overlayArr = this.map.getOverlays().getArray();
      overlayArr.forEach((item) => {
        let id = item.getId() + '';
        if (id.indexOf(name) != -1) {
          this.$nextTick(() => {
            this.map.removeOverlay(item);
          });
        }
      });
    },
    //添加调度仓库弹框
    addSchemeWarehouseOverlay() {
      this.schemeWarehouseOverlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#schemeWarehousePopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.schemeWarehouseOverlay);
    },
    // 添加调度队伍弹框
    addSchemeRanksOverlay() {
      this.schemeRanksOverlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#schemeRanksPopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.schemeRanksOverlay);
    },

    //添加水域变化的overlay
    addWaterChangeFixedOverlay(list, type, tab) {
      for (let i = 0; i < list.length; i++) {
        let typeClass = 'fixed-ovly-blue';
        if (list[i].valueType && list[i].valueType == 3) {
          typeClass = 'fixed-ovly-green';
        } else if (list[i].valueType && list[i].valueType == 1) {
          typeClass = '';
        }
        let domText1 = `<div class="fixed-ovly ${typeClass}" id='${type + i}'>
            <p>${list[i].areaName}</p>
            <p>水域面积<span> ${list[i].waterArea + 'k㎡'} </span> </p>
          </div>`;
        let domText2 = `<div class="fixed-ovly ${typeClass}" id='${type + i}'>
            <p>${list[i].areaName}</p>
            <p>占用面积<span> ${list[i].occupiedArea + 'k㎡'} </span> </p>
            <p>补偿面积<span> ${list[i].compensationArea + 'k㎡'} </span> </p>
          </div>`;
        let idx = searchAreaList.indexOf(list[i].areaName);
        let coodinate = idx == -1 ? [] : centerPoint[idx];
        $('#ControlMainView').append(tab == '水域面积' ? domText1 : domText2);
        this.$nextTick(() => {
          let overlay = new Overlay({
            id: type + i,
            element: document.getElementById(type + i),
            positioning: 'bottom-center',
            autoPan: true,
            autoPanAnimation: {
              duration: 250
            },
            stopEvent: false,
            position: coodinate
          });
          this.map.addOverlay(overlay);
        });
      }
    },

    /**
     * 移除所有图层
     */
    removeAllLayer() {
      // 忽略的图层列表：按name属性
      const list = ['MainMaplayer', 'wenzhouRiverLayer'];
      let arr = [];
      this.map.getLayers().forEach((element) => {
        if (element && element.get('name') && !list.includes(element.get('name'))) {
          arr.push(element);
        }
      });
      for (let item of arr) {
        this.map.removeLayer(item);
      }
    },
    //将返回地址替换成代理地址
    replaceUrl(url) {
      return url.replace('http://10.36.134.56:6080/', 'https://117.149.228.207:8081/xglptMpServer/');
    },
    //加载等雨量面
    showIsohyetLayer(mapImage) {
      this.removeLayerByName('rainfall');
      this.map.removeLayer(this.isohyetLayer);
      this.isohyetLayer = new ImageLayer({
        name: 'rainfall',
        source: new ImageStatic({
          url: this.replaceUrl(mapImage.href), //这里添加静态图片的地址
          projection: 'EPSG:4326',
          imageExtent: [119.53839579772948, 26.979173080444387, 121.33689579772948, 28.77767308044439] //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10
      });
      this.map.addLayer(this.isohyetLayer);
    },
    /**
     * 移除指定图层
     * @param {String} layerName 指定的图层名
     */
    removeLayerByName(layerName) {
      if (layerName == 'areaWKTLayer') {
        this.map.removeLayer(this.areaWKTLayer);
      } else if (layerName == 'focuLineLayer') {
        this.removeFocusLineLayer();
        this.removeLineLayer();
      }
      this.map.getLayers().forEach((element) => {
        if (element && element.get('name') == layerName) {
          this.$nextTick(() => {
            this.map.removeLayer(element);
          });
        }
      });
    },
    /**
     * 检验指定图层是否存在
     */
    isLayerExist(layerName) {
      let flag = false;
      this.map.getLayers().forEach((element) => {
        if (element && element.get('name') == layerName) {
          flag = true;
        }
      });
      return flag;
    },
    /**
     * 注册事件
     */
    registeredEvent() {
      this.map.on('moveend', () => {
        /** @global */
        window.mapZoom = this.map.getView().getZoom();
      });
      this.map.on('singleclick', (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          let properties = feature.getProperties();
          if (properties.features && properties.features.length > 1) {
            let props = {};
            for (let item of properties.features) {
              props[item.values_.data.name] = item.values_.data.stationCode;
            }
          }
          return feature;
        });
        // 抢险调度弹框
        // if (!feature) {
        if (this.SpotsFlag) {
          let dispatchLat = event.coordinate[1].toFixed(3);
          let dispatchLng = event.coordinate[0].toFixed(3);
          // let index = this.CheckedArr.findIndex((item) => item == "抢险支持");
          // if (index > -1)
          this.$emit('showScheduleScheme', dispatchLat, dispatchLng);
        }

        //短临预报弹框标记
        if (this.ForecastPointFlag) {
          let forecastLat = event.coordinate[1].toFixed(3);
          let forecastLng = event.coordinate[0].toFixed(3);
          this.$emit('showForecastDialog', forecastLat, forecastLng);
        }
        // }
      });
      this.map.on('pointermove', (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let flag = false;
        this.waterSourPopupShow = false;
        this.waterSecurityPopupShow = false;
        this.waterRegulationPopupShow = false;
        this.clusterPointPopupShow = false;
        this.map.getTargetElement().style.cursor = 'auto';
        this.map.forEachFeatureAtPixel(pixel, (feature) => {
          this.featureAttr = feature.values_.attr;
          let coodinate = event.coordinate;
          let properties = feature.getProperties();
          this.map.getTargetElement().style.cursor = 'pointer';
          if (feature.values_.attr != null) {
            if (feature.values_.attr.type == '降水量') {
              this.overlay1.setPosition(coodinate);
              this.waterSourPopupShow = true;
            } else if (feature.values_.attr.type == '水资源量') {
              this.overlay1.setPosition(coodinate);
              this.waterSourPopupShow = true;
            } else if (feature.values_.attr.type == '用水量') {
              this.overlay1.setPosition(coodinate);
              this.waterSourPopupShow = true;
            }
          } else {
            if (feature && properties) {
              //聚合时 只展示一个点信息
              if (properties.features && properties.features.length == 1) {
                this.featureInfo = {
                  name: properties.features[0].values_.data.name,
                  props: properties.features[0].values_.data.props,
                  label: properties.features[0].values_.data.label || false
                };
                if (properties.features[0].values_.data.type == '水源供水') {
                  if (this.featureInfo) {
                    this.overlay1.setPosition(coodinate);
                    this.waterSourPopupShow = true;
                  }
                } else if (properties.features[0].values_.data.type == '水源保障') {
                  if (this.featureInfo) {
                    (this.featureInfo.supplyAbility = properties.features[0].values_.data.supplyAbility),
                      this.overlay2.setPosition(coodinate);
                    this.waterSecurityPopupShow = true;
                  }
                } else if (properties.features[0].values_.data.type == '取水监管') {
                  if (this.featureInfo) {
                    (this.featureInfo.wainName = properties.features[0].values_.data.wainName),
                      (this.featureInfo.intake = properties.features[0].values_.data.intake),
                      (this.featureInfo.tm = properties.features[0].values_.data.tm),
                      this.overlay3.setPosition(coodinate);
                    this.waterRegulationPopupShow = true;
                  }
                } else {
                  if (this.featureInfo) {
                    this.overlay.setPosition(coodinate);
                    flag = true;
                  }
                }
              } else if (
                properties.features &&
                properties.features.length > 1 &&
                properties.features[0].values_.data.type != '山洪'
              ) {
                //多个聚合点时
                // console.log(properties.features)
                let arr = properties.features.map((item) => {
                  return item.values_.data;
                });
                this.clusterPointList = arr;
                const coodinate = [properties.geometry.extent_[0], properties.geometry.extent_[1]];
                this.clusterPointOverLay.setPosition(coodinate);
                this.clusterPointPopupShow = true;
              } else if (properties.data) {
                if (properties.data.modular && properties.data.modular == '流域灾害风险评估') {
                  coodinate = [ properties.data.lng, properties.data.lat ]
                }
                // console.log(properties.data);
                //非聚合点位时显示信息
                this.featureInfo = {
                  recType: properties.data.recType || false,
                  name: properties.data.name,
                  props: properties.data.props,
                  label: properties.data.label || false
                };
                if (
                  (properties.data.type && properties.data.type == '水源供水') ||
                  (properties.data.type && properties.data.type == '水源保障')
                ) {
                } else {
                  if (this.featureInfo) {
                    this.overlay.setPosition(coodinate);
                    flag = true;
                  }
                  if (properties.data.no_pointermove) {
                    flag = false;
                  }
                }
              }
            }
          }
        });
        if (flag) {
          this.popupShow = true;
        } else {
          this.popupShow = false;
        }
      });
    },

    /**
     * 画点并将图层点位绑定事件
     * @param {Array} coordinate 点位列表
     * @param {Object} styleObj 样式对象
     * @param {String} layerName 点位图层名称，用于分辨
     */
    drawPoint(coordinates, styleObj, layerName = 'pointLayer') {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let pointLayer = drawPoint(coordinates, styleObj);
      pointLayer.set('name', layerName);
      pointLayer.setZIndex(10);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);
      selectClick.on('select', (e) => {
        let features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          this.$emit('point-click', features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    clusterPointClick(item) {
      this.$emit('point-click', item);
    },
    drawDynamicPoint(coordinates, typeName) {
      if (this.dynamicPointMatain[typeName]) {
        this.clearDynamicPoint(typeName);
      }
      this.dynamicPointMatain[typeName] = [];
      let mapDom = document.getElementById('ControlMainView');
      coordinates.map((item, index) => {
        let pointDom = document.createElement('div');
        pointDom.id = `${typeName + index}`;
        pointDom.className = typeName;
        let childDom = document.createElement('div');
        childDom.className = 'dynamic-child';
        pointDom.appendChild(childDom);
        mapDom.appendChild(pointDom);

        pointDom.addEventListener('pointermove', () => {
          this.featureInfo = {
            name: item.name,
            props: item.props,
            label: item.label || false
          };
          if (this.featureInfo) {
            this.overlay.setPosition([item.lng, item.lat]);
            this.overlayEnterFlag = true;
          }
        });

        pointDom.addEventListener('mouseout', () => {
          this.overlayEnterFlag = false;
        });

        pointDom.addEventListener('click', () => {
          const data = {
            ...item
          };
          console.log(data, '试试看点击点位');
          this.$emit('point-click', data);
        });

        let marker = new Overlay({
          position: [item.lng, item.lat],
          stopEvent: false,
          positioning: 'center-bottom',
          element: document.getElementById(`${typeName + index}`),
          offset: [-25, -25] //根据图标大小设置偏移位置
        });
        this.dynamicPointMatain[typeName].push(marker);
        this.map.addOverlay(marker);
        marker.on('pointermove', (event) => {
          // console.log(event);
        });
      });
    },
    clearDynamicPoint(typeName) {
      if (typeName) {
        if (this.dynamicPointMatain[typeName]) {
          this.dynamicPointMatain[typeName].forEach((item) => {
            this.map.removeOverlay(item);
          });
          // this.dynamicPointMatain[typeName];
          delete this.dynamicPointMatain[typeName];
          let dom = document.getElementsByClassName(typeName);
          if (dom.length > 0) {
            dom.forEach((item) => {
              dom.parentNode.removeChild(item);
            });
          }
        }
      } else {
        //清除全部动态点位s
        for (let key in this.dynamicPointMatain) {
          this.dynamicPointMatain[key].forEach((child) => {
            this.map.removeOverlay(child);
          });
          let dom = document.getElementsByClassName(key);
          if (dom.length > 0) {
            dom.forEach((item) => {
              dom.parentNode.removeChild(item);
            });
          }
        }
        this.dynamicPointMatain = {};
      }
    },
    clearAreaWKTLayer() {
      if (this.areaWKTLayer != null) {
        this.map.removeLayer(this.areaWKTLayer);
        this.areaWKTLayer = null;
      }
    },
    drawAreaWKT(arr) {
      // console.log(arr,333);
      this.clearAreaWKTLayer();
      var format = new WKT();
      var source = new VectorSource({ wrapX: false });
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].coordinates) {
          var feature = format.readFeature(arr[i].coordinates);
          feature.data = arr[i];
          source.addFeature(feature);
        }
      }
      let styleFunction = function (feature) {
        let styles = [];
        let type = feature.data.lvl;
        let color = '#E8B005';
        let rgba = 'rgba(232,176,5,0.5)';
        if (type == '0') {
          color = '#F39100';
          rgba = 'rgba(243,145,0,0.5)';
        } else {
          color = '#F10000';
          rgba = 'rgba(241,0,0,0.5)';
        }
        styles.push(
          new Style({
            stroke: new Stroke({
              color: color,
              width: 2
            }),
            fill: new Fill({
              color: rgba
            })
          })
        );
        return styles;
      };
      this.areaWKTLayer = new VectorLayer({
        source: source,
        style: styleFunction,
        zIndex: 1001
      });
      this.map.addLayer(this.areaWKTLayer);
    },
    drawAreaPolygon(coordinates, layerName = 'areaPolygon') {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let areaPolygon = drawAreaPolygon(coordinates);
      areaPolygon.set('name', layerName);
      this.map.addLayer(areaPolygon);
    },
    // 加载河道线段
    _riversLrawLines(arr, style, layerName) {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let tempLayer = drawLines(arr);
      tempLayer.set('name', layerName);
      tempLayer.setZIndex(99);
      this.map.addLayer(tempLayer);
      const selectClick = addLayerClick(tempLayer);
      const _this = this;
      selectClick.on('select', function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit('point-click', features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    /**
     * 画聚合点位并将图层点位绑定事件
     * @param {Array} coordinate 点位列表
     * @param {Object} styleObj 样式对象
     * @param {String} layerName 点位图层名称，用于分辨
     */
    drawClusterPoint(coordinates, styleObj, layerName = 'pointLayer') {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let pointLayer = drawClusterPoint(coordinates, styleObj);
      pointLayer.set('name', layerName);
      pointLayer.setZIndex(20);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);
      selectClick.on('select', (e) => {
        let features = e.target.getFeatures().getArray()[0].values_.features;
        //点击层级放大 点位散开
        if (features.length > 1) {
          const extent = boundingExtent(features.map((r) => r.getGeometry().getCoordinates()));
          this.map.getView().fit(extent, { duration: 1000, padding: [100, 100, 100, 100] });
        }
        if (features.length == 1) {
          this.$emit('point-click', features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    /**
     * 通用使用数据中的 coorpot 属性画线
     * @param {{coorpot:string}[]} dataList
     * @param {string} type
     */
    drawCoorpotLine(dataList, type, dataFn, style = { lineWidth: 4, lineColor: '#e80000' }) {
      for (let data of dataList) {
        const converter = document.createElement('div');
        converter.innerHTML = data.coorpot ? data.coorpot : '[]';
        const lineString = decodeURIComponent(converter.innerText)
          .replaceAll(/\\/g, '')
          .replaceAll(/^"(.+)"$/g, '$1');
        let lineList = eval(lineString);
        const displayData = typeof dataFn == 'undefined' ? {} : dataFn(data);
        lineList = lineList.map((line) => {
          return {
            lineList: line.map((item) => [item.lng, item.lat]),
            data: {
              ...displayData,
              type
            }
          };
        });
        if (typeof style == 'object') {
          this.drawLineEx(lineList, style);
        } else if (typeof style == 'function') {
          this.drawLineEx(lineList, style(data));
        }
      }
    },
    /**
     * drawLine
     */
    drawLineEx(coordinates, styleObj = {}, layerName = 'lineLayer') {
      if (!coordinates.length > 0) return;
      let layer = drawLineEx(coordinates, styleObj);
      this.lineLayer.push(layer);
      layer.set('name', layerName);
      layer.setZIndex(10);
      this.map.addLayer(layer);
      let selectClick = addLayerClick(layer);
      selectClick.on('select', (e) => {
        let features = e.target.getFeatures().array_;
        // console.log(e.target.getFeatures(),555)
        if (e.target.getFeatures().array_.length == 1) {
          this.$emit('point-click', features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },

    removeLineLayer() {
      this.lineLayer.forEach((item) => {
        this.map.removeLayer(item);
      });
    },
    drawLineFocus(coordinates, styleObj = {}) {
      if (!coordinates.length > 0) return;
      let layer = drawLineEx(coordinates, styleObj);
      this.lineFocusLayer.push(layer);
      layer.set('name', 'focuLineLayer');
      layer.setZIndex(10);
      this.map.addLayer(layer);
    },
    removeFocusLineLayer() {
      // console.log(this.lineFocusLayer,1)
      this.lineFocusLayer.forEach((item) => {
        this.map.removeLayer(item);
      });
    },
    showTyphoon(data) {
      const typhoon = new newTyphoon(this.map, data, this.typhoonsMap, this);
      typhoon.init();
    },
    //根据code清除台风
    clearTyphoon(code) {
      const layer = this.typhoonsMap.get(code + 'layer');
      const interval = this.typhoonsMap.get(code + 'interval');
      this.map.removeLayer(layer);
      clearInterval(interval);
      const nameOverLay = this.typhoonsMap.get(code + 'name');
      this.map.removeOverlay(nameOverLay);
      //清除台风点选中图标
      let picOverlay = this.map.getOverlayById('overlayWind');
      if (picOverlay) {
        this.map.removeOverlay(picOverlay);
      }
      // console.log(this.typhoonsMap, code, 123);
    },
    //清除全部台风
    clearAllTyphoon() {
      for (let item in this.typhoonsMap.values_) {
        if (item.indexOf('layer') != -1) {
          this.map.removeLayer(this.typhoonsMap.values_[item]);
        } else if (item.indexOf('name') != -1) {
          const nameOverLay = this.typhoonsMap.get(item);
          this.map.removeOverlay(nameOverLay);
        } else if (item.indexOf('interval') != -1) {
          clearInterval(item);
        }
      }
      //清除台风点选中图标
      let picOverlay = this.map.getOverlayById('overlayWind');
      if (picOverlay) {
        this.map.removeOverlay(picOverlay);
      }
    },
    clearTyphoonLayer() {
      if (this.typhoonLayers.length > 0) {
        this.typhoonLayers.map((item) => {
          this.map.removeLayer(item);
        });
        this.typhoonLayers = [];
      }
      if (this.typhoonNames.length > 0) {
        this.typhoonNames.map((item) => {
          this.map.removeOverlay(item);
        });
        this.typhoonNames = [];
      }
      if (this.typhoonTimes.length > 0) {
        this.typhoonTimes.map((item) => {
          clearInterval(item);
        });
        this.typhoonTimes = [];
      }
    },
    _showTestPoint(data) {
      this.clearTextLayer();
      this.textLayer = showTestClusterPoint(data);
      this.map.addLayer(this.textLayer);
    },
    clearTextLayer() {
      if (this.textLayer != null) {
        this.map.removeLayer(this.textLayer);
        this.textLayer = null;
      }
    },
    //根据行政区划名称查询行政区划边界 lineFlag为true时加载边界线 不传加载面
    showAreaLine(areaName, lineFlag) {
      this.clearAreaLayer();

      const areaIndex = searchAreaList.indexOf(areaName);
      if (areaIndex != -1) {
        const searchUrl = mapConfig.mapServer + 'wenzhou/wenzhou_fangxunend/MapServer/3/query';
        const opt = {
          where: "NAME='" + areaName + "'",
          outFields: '*',
          f: 'pjson'
        };
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {
            fillColor: 'rgb(232, 0, 0, 0.4)',
            lineColor: '#FF551F',
            lineDash: [6],
            lineWidth: 4
          });
          this.map.addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 10);
      } else {
        const searchUrl =
          'http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/2/query';
        const opt = {
          where: "NAME='" + areaName + "'",
          outFields: '*',
          f: 'pjson'
        };
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {
            fillColor: 'rgba(91,169,252, 0.6)',
            lineColor: '#FF551F'
          });
          this.map.addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 9.5);
      }
    },
    //根据行政区划名称查询行政区划面
    showArea(areaName, color) {
      this.clearAreaLayer();

      const areaIndex = searchAreaList.indexOf(areaName);
      if (areaIndex != -1) {
        const searchUrl = mapConfig.mapServer + 'wenzhou/wenzhou_fangxunend/MapServer/3/query';
        const opt = {
          where: "NAME='" + areaName + "'",
          outFields: '*',
          f: 'pjson'
        };
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {
            fillColor: color,
            lineColor: color,
            lineDash: [6],
            lineWidth: 4
          });
          this.areaLayer.set('name', 'areaLayer');
          this.map.addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 10);
      }
    },
    //打印地图
    printMap() {
      printJS({
        printable: 'ControlMainView',
        type: 'html',
        targetStyles: ['*']
      });
    },
    //定位到对应位置和层级
    flyPoint(center, zoom) {
      const view = this.map.getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom
      });
    },
    clearAreaLayer() {
      if (this.areaLayer != null) {
        this.map.removeLayer(this.areaLayer);
        this.areaLayer = null;
      }
    },
    /** 流域切换 */
    basinCommand(command) {
      if (command == '全流域') {
        if (this.basinLayer != null) {
          this.map.removeLayer(this.basinLayer);
          this.basinLayer = null;
          this.fullMap();
        }
        return;
      }
      queryMapserver(
        '/温州流域/MapServer/0/query',
        {
          where: "WRNM='" + command + "'",
          f: 'json'
        },
        true
      ).then((res) => {
        if (res.features && res.features.length > 0) {
          esri2geo(res, (err, data) => {
            //加载温州流域图层
            let vectorSource = new VectorSource({
              features: new GeoJSON().readFeatures(data)
            });
            if (this.basinLayer != null) {
              this.map.removeLayer(this.basinLayer);
              this.basinLayer = null;
            }
            this.basinLayer = new VectorLayer({
              source: vectorSource,
              style: new Style({
                stroke: new Stroke({
                  color: '#FF00E7',
                  lineDash: [6],
                  width: 4
                }),
                fill: new Fill({
                  color: 'rgba(0, 0, 255, 0)'
                })
              }),
              zIndex: 1000
            });
            this.map.addLayer(this.basinLayer);
            this.map.getView().fit(this.basinLayer.getSource().getFeatures()[0].getGeometry(), {
              padding: [100, 300, 0, 200]
            });
          });
        }
      });
    },
    fullMap() {
      let view = this.map.getView();
      view.animate({
        center: [mapConfig.lng, mapConfig.lat],
        duration: 300,
        zoom: mapConfig.zoom
      });
    },
    playNext() {
      return new Promise((resolve) => {
        resolve();
      });
    },
    removeYxLayers() {
      const yxArr = ['wz2010', 'wz2012', 'wz2014', 'wz2016', 'wz2017', 'wz2018', 'wz2019'];
      this.map.getLayers().forEach((element) => {
        let layerId = element.get('id');

        if (yxArr.includes(layerId)) {
          this.map.removeLayer(element);
        }
      });
    },
    handleTimelineChange(idx) {
      //温州历年影像图层
      const yxLayers = {
        2010: new TileLayer({
          id: 'wz2010',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/1fec7dfbdc744c2d9bc25e7f2b2043e8/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2010/MapServer'
            },
            { zoomOffset: 1 }
          )
        }),
        2012: new TileLayer({
          id: 'wz2012',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/57d3b2406d5643e6a3646d0f3942a0a3/arcgis/rest/services/HISIMG/WZ2000_SPOT5_2012/MapServer'
            },
            { zoomOffset: 1 }
          )
        }),
        2014: new TileLayer({
          id: 'wz2014',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/4c687988428d4261aeb49caa87a8bcd5/arcgis/rest/services/HISIMG/WZ2000_DOM_2014/MapServer'
            },
            { zoomOffset: 1 }
          )
        }),
        2016: new TileLayer({
          id: 'wz2016',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/a2511b47fcb94458995e546e64f301af/arcgis/rest/services/HISIMG/WZ2000_GF_2016_2M/MapServe'
            },
            { zoomOffset: 1 }
          )
        }),
        2017: new TileLayer({
          id: 'wz2017',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/fd7dc8e1cf7f4756b4059419ed492200/arcgis/rest/services/HISIMG/WZ2000_DOM_2017/MapServer'
            },
            { zoomOffset: 1 }
          )
        }),
        2018: new TileLayer({
          id: 'wz2018',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/563382047626443aba7e546b86c0cf4f/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2018/MapServer'
            },
            { zoomOffset: 1 }
          )
        }),
        2019: new TileLayer({
          id: 'wz2019',
          zIndex: 1,
          source: new TileArcGISRest(
            {
              url: 'http://10.36.161.208:16080/wzgt22/754677ad50484012ba9a9ccb0946aa18/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2019/MapServer'
            },
            { zoomOffset: 1 }
          )
        })
      };
      this.removeYxLayers();
      // console.log(yxLayers[this.timelineList[idx].label])
      this.map.addLayer(yxLayers[this.timelineList[idx].label]);
    },
    //雷达降雨变化
    handleRaderTimeChange(value) {
      this.loadRadarRainImg(this.raderTimeArr[value].value);
    },
    //保存场景
    doSceneSave() {
      this.$emit('doSceneSave');
    },
    //获取调度信息
    dispatchInfo(data, stationInfo) {
      // this.removeAllLayer();
      const stationPos = [stationInfo.lng, stationInfo.lat];
      //绘制队伍路线
      this.getRouterLine([data.teamInfo.longitude, data.teamInfo.latitude], stationPos, 1, data.teamInfo);
      if (data.warehouses.length > 0) {
        //绘制仓库路线
        this.getRouterLine(
          [data.warehouses[0].longitude, data.warehouses[0].latitude],
          stationPos,
          2,
          data.warehouses[0]
        );
      }
      //设置测站终点
      this.pointLayer_station_end && this.map.removeLayer(this.pointLayer_station_end);
      this.pointLayer_station_end = drawRoutePoint(stationPos, 3);
      this.pointLayer_station_end.set('name', 'pointLayer_station_end');
      this.map.addLayer(this.pointLayer_station_end);
      this.SpotsFlag && this.SetSpotsFlag(false);
    },

    //获取路线的方法
    async getRouterLine(startPoint, endPoint, type, pointInfo) {
      await axios({
        url: `https://restapi.amap.com/v3/direction/driving?origin=${startPoint[0]},${startPoint[1]}&destination=${endPoint[0]},${endPoint[1]}&output=xml&key=${this.$config.gaoDeKey}&output=json`,
        method: 'get'
      }).then((res) => {
        // console.log(res);
        if (res.data.route.paths.length > 0) {
          const route = res.data.route;
          const steps = route.paths[0].steps;
          let routeLine = [];
          steps.map((item) => {
            const tempArr = item.polyline.split(';');
            tempArr.map((child) => {
              let tempPoint = child.split(',');
              tempPoint[0] = Number(tempPoint[0]);
              tempPoint[1] = Number(tempPoint[1]);
              routeLine.push(tempPoint);
            });
          });
          if (type == '1') {
            this.addSchemeRanksOverlay();
            //队伍展示路线
            this.map.removeLayer(this.lineLayer_team);
            this.lineLayer_team = drawLine(routeLine, {
              lineColor: '#E10000',
              lineWidth: 4
            });
            this.lineLayer_team.set('name', 'lineLayer_team');
            this.map.addLayer(this.lineLayer_team);
            //存储队伍调度路线详情
            const teamInfoList = [];
            for (let i = 0; i < route.paths.length; i++) {
              const distance = Number(route.paths[i].distance) / 1000;
              const hour = Math.floor(Number(route.paths[i].duration) / 3600);
              const minute = Math.floor((Number(route.paths[i].duration) % 3600) / 60);
              const duration = hour + '小时' + +minute + '分钟';
              const routeInfoItem = {
                taxi_cost: route.taxi_cost,
                distance: distance,
                traffic_lights: route.paths[i].traffic_lights,
                duration: duration
              };
              teamInfoList.push(routeInfoItem);
            }
            this.SetTeamDispatchList(teamInfoList);

            if (pointInfo) {
              //人员调度
              const peoplePos = [pointInfo.longitude, pointInfo.latitude];
              //设置队伍起点
              this.pointLayer_team_start && this.map.removeLayer(this.pointLayer_team_start);
              this.pointLayer_team_start = drawRoutePoint(peoplePos, 1);
              this.pointLayer_team_start.set('name', 'pointLayer_team_start');

              // 队伍起点弹框
              this.schemeRanksInfo = {
                name: pointInfo.name,
                基本信息: [
                  {
                    label: '队伍人数',
                    value: `${pointInfo.existingNumber || '-'}`
                  },
                  {
                    label: '队伍责任人',
                    value: `${pointInfo.chargePerson || '-'}`
                  },
                  {
                    label: '联系电话',
                    value: `${pointInfo.chargePhone || '-'}`
                  }
                ],
                调度信息: [
                  {
                    label: '距离',
                    value: teamInfoList.length > 0 ? `${teamInfoList[0].distance || '-'}km` : '-'
                  },
                  {
                    label: '时长',
                    value: teamInfoList.length > 0 ? `${teamInfoList[0].duration || '-'}` : '-'
                  }
                ]
              };
              this.map.addLayer(this.pointLayer_team_start);
              this.schemeRanksPoint = true;
              this.schemeRanksOverlay.setPosition(peoplePos);
            }
          } else {
            this.addSchemeWarehouseOverlay();
            //仓库展示路线
            this.map.removeLayer(this.lineLayer_warehouse);
            this.lineLayer_warehouse = drawLine(routeLine, {
              lineColor: '#079700',
              lineWidth: 4
            });
            this.lineLayer_warehouse.set('name', 'lineLayer_warehouse');
            this.map.addLayer(this.lineLayer_warehouse);
            //存储仓库调度路线详情
            const warehoureInfoList = [];
            for (let i = 0; i < route.paths.length; i++) {
              const distance = Number(route.paths[i].distance) / 1000;
              const hour = Math.floor(Number(route.paths[i].duration) / 3600);
              const minute = Math.floor((Number(route.paths[i].duration) % 3600) / 60);
              const duration = hour + '小时' + +minute + '分钟';
              const routeInfoItem = {
                taxi_cost: route.taxi_cost,
                distance: distance,
                traffic_lights: route.paths[i].traffic_lights,
                duration: duration
              };
              warehoureInfoList.push(routeInfoItem);
            }
            this.SetWarehouseDispatchList(warehoureInfoList);

            if (pointInfo) {
              //物资调度
              const wareHousePos = [pointInfo.longitude, pointInfo.latitude];

              //设置仓库起点
              this.pointLayer_warehouse_start && this.map.removeLayer(this.pointLayer_warehouse_start);
              this.pointLayer_warehouse_start = drawRoutePoint(wareHousePos, 2);
              this.pointLayer_warehouse_start.set('name', 'pointLayer_warehouse_start');
              // 仓库起点弹框
              this.schemeWarehouseInfo = {
                name: pointInfo.name,
                基本信息: [
                  {
                    label: '仓库负责人',
                    value: `${pointInfo.chargePerson || '-'}`
                  },
                  {
                    label: '联系电话',
                    value: `${pointInfo.chargePhone || '-'}`
                  }
                ],
                调度物资: this.GoodsList,
                调度信息: [
                  {
                    label: '距离',
                    value: warehoureInfoList.length > 0 ? `${warehoureInfoList[0].distance || '-'}km` : '-'
                  },
                  {
                    label: '时长',
                    value: warehoureInfoList.length > 0 ? `${warehoureInfoList[0].duration || '-'}` : '-'
                  }
                ]
              };
              this.map.addLayer(this.pointLayer_warehouse_start);
              this.schemeWarehousePoint = true;
              this.schemeWarehouseOverlay.setPosition(wareHousePos);
            }
          }
          // 调度结果轮播页面
          this.$nextTick(() => {
            this.SetDispatchCarousel(true);
          });
        }
      });
    },
    // 清除调度路线信息
    clearDispatch() {
      this.schemeWarehousePoint = false;
      this.schemeRanksPoint = false;
      this.pointLayer_team_start && this.map.removeLayer(this.pointLayer_team_start);
      this.pointLayer_warehouse_start && this.map.removeLayer(this.pointLayer_warehouse_start);
      this.pointLayer_station_end && this.map.removeLayer(this.pointLayer_station_end);
      this.lineLayer_team && this.map.removeLayer(this.lineLayer_team);
      this.lineLayer_warehouse && this.map.removeLayer(this.lineLayer_warehouse);
      this.DispatchCarousel && this.SetDispatchCarousel(false);
      this.SpotsFlag && this.SetSpotsFlag(false);
      this.IsAction && this.SetIsAction(false);
    },
    // 洪水风险淹没图
    drawDesignFlood(name, year) {
      const layerName = `${name}${year}`;
      // const url = `${mapConfig.oldMapServer}hsfx/${layerName}/MapServer/tile/{z}/{y}/{x}`;
      // this.designFloodLayer[layerName] = new TileLayer({
      //   id: layerName,
      //   zIndex: 10,
      //   source: new XYZ({
      //     url: url,
      //     projection: 'EPSG:4326'
      //   })
      // });
      // this.map.addLayer(this.designFloodLayer[layerName]);
      let arr = [
        {
          label: 'OJ_YMSSSJ',
          value: [120.4133965517858, 28.11956497907321, 120.5020176328294, 28.15820904531849]
        },
        {
          label: 'WRYMSSsj',
          value: [120.5175539498315, 27.70766999715319, 120.8827936263254, 27.99103623688936]
        },
        {
          label: 'YMSSsj',
          value: [120.4751277584375, 28.02914495154861, 120.5685037800489, 28.11169988052677]
        }
      ];
      let imgSrc = require(`@/assets/images/custom_card/${layerName}.png`);
        console.log(arr.find(item => item.label == name).value)
      this.designFloodLayer[layerName] = new ImageLayer({
        name: 'radarRainLayer',
        source: new ImageStatic({
          url: imgSrc, //这里添加静态图片的地址
          projection: 'EPSG:4326',
          imageExtent: arr.find(item => item.label == name).value //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10
      });
      this.map.addLayer(this.designFloodLayer[layerName]);
    },
    clearDesignFlood(layerName) {
      if (layerName) {
        this.map.removeLayer(this.designFloodLayer[layerName]);
      } else {
        if (Object.keys(this.designFloodLayer).length > 0) {
          for (let key in this.designFloodLayer) {
            this.map.removeLayer(this.designFloodLayer[key]);
          }
        }
      }
    },
    //加载鹿城流域图
    addLuchengGeoJsonLayer() {
      let vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(luchengGeoJson)
      });
      if (this.windWaveBaseLayers != null) {
        this.map.removeLayer(this.windWaveBaseLayers);
        this.windWaveBaseLayers = null;
      }
      this.windWaveBaseLayers = new VectorLayer({
        source: vectorSource,
        opacity: 0.85,
        style: new Style({
          stroke: new Stroke({
            color: '#FF3F00',
            width: 2
          })
          // fill: new Fill({
          //   color: 'rgba(0, 196, 255, .5)'
          // })
        }),
        zIndex: 1
      });
      this.map.addLayer(this.windWaveBaseLayers);
    },
    //加载风场浪场底图
    addWeatherBaseLayer() {
      //加载温州流域图层
      let vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(baseGeoJson)
      });
      if (this.windWaveBaseLayers != null) {
        this.map.removeLayer(this.windWaveBaseLayers);
        this.windWaveBaseLayers = null;
      }
      this.windWaveBaseLayers = new VectorLayer({
        source: vectorSource,
        opacity: 0.85,
        style: new Style({
          stroke: new Stroke({
            color: '#103759',
            lineDash: [6],
            width: 4
          }),
          fill: new Fill({
            color: '#103759'
          })
        }),
        zIndex: 10
      });
      this.map.addLayer(this.windWaveBaseLayers);
    },
    //加载风场浪场图层
    addWatherLayer(datas, layerType) {
      this.addWeatherBaseLayer();
      const view = this.map.getView();
      view.setZoom(6.5);
      let colorScale = [
        'rgb(36,104, 180)',
        'rgb(60,157, 194)',
        'rgb(128,205,193 )',
        'rgb(151,218,168 )',
        'rgb(198,231,181)',
        'rgb(238,247,217)',
        'rgb(255,238,159)',
        'rgb(252,217,125)',
        'rgb(255,182,100)',
        'rgb(252,150,75)',
        'rgb(250,112,52)',
        'rgb(245,64,32)',
        'rgb(237,45,28)',
        'rgb(220,24,32)',
        'rgb(180,0,35)'
      ];
      switch (layerType) {
        case 'wind':
          // 开启风场
          if (this.windLayers != null) {
            this.windLayers.setVisible(true);
            return;
          }
          this.windLayers = new WindLayer(datas, {
            windOptions: {
              colorScale: colorScale,
              lineWidth: 2,
              frameRate: 25,
              globalAlpha: 0.9,
              velocityScale: 1 / 1000,
              paths: 5000,
              generateParticleOption: false
            },
            zIndex: 100
          });
          this.$nextTick(() => {
            this.map.addLayer(this.windLayers);
          });
          break;
        case 'wave':
          // 开启浪场
          if (this.waveLayers != null) {
            this.waveLayers.setVisible(true);
            return;
          }
          this.waveLayers = new WindLayer(datas, {
            windOptions: {
              colorScale: colorScale,
              lineWidth: 8,
              frameRate: 15,
              globalAlpha: 0.8,
              velocityScale: 1 / 100, //速度
              paths: 2000,
              generateParticleOption: false
            },
            zIndex: 101
          });
          this.$nextTick(() => {
            this.map.addLayer(this.waveLayers);
          });
          break;
      }
      //移动事件
      this.map.on('movestart', (e) => {
        // console.log(e)
        if (this.windLayers) {
          this.windLayers.setVisible(false);
        }
        if (this.waveLayers) {
          this.waveLayers.setVisible(false);
          return;
        }
      });
      this.map.on('moveend', (e) => {
        setTimeout(() => {
          if (this.windLayers) {
            this.windLayers.setVisible(true);
          }
          if (this.waveLayers) {
            this.waveLayers.setVisible(true);
            return;
          }
        }, 2000);
      });
    },
    //清除风场浪场图层
    closeWeatherLayers(layerType) {
      if (this.windWaveBaseLayers != null) {
        this.map.removeLayer(this.windWaveBaseLayers);
        this.windWaveBaseLayers = null;
      }
      switch (layerType) {
        case 'wind':
          if (this.windLayers != null) this.map.removeLayer(this.windLayers);
          this.windLayers = null;
          break;
        case 'wave':
          if (this.waveLayers != null) this.map.removeLayer(this.waveLayers);
          this.waveLayers = null;
          break;
      }
    },
    loadCloundImg(imgSrc) {
      this.removeLayerByName('cloundLayer');
      this.cloundLayer = new ImageLayer({
        name: 'cloundLayer',
        source: new ImageStatic({
          url: imgSrc, //这里添加静态图片的地址
          projection: 'EPSG:4326',
          imageExtent: [80.0, 0, 180.0, 60.0] //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10
      });
      this.map.addLayer(this.cloundLayer);
    },
    loadRadarRainImg(imgSrc) {
      this.removeLayerByName('radarRainLayer');
      this.radarRainLayer = new ImageLayer({
        name: 'radarRainLayer',
        source: new ImageStatic({
          url: imgSrc, //这里添加静态图片的地址
          projection: 'EPSG:4326',
          imageExtent: [78.4564707118729, 17.192519812642708, 144.74218778452877, 57.09042776801506] //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10
      });
      this.map.addLayer(this.radarRainLayer);
    },
    // 设置水量预警等级颜色
    setWaterWarnColor(val) {
      switch (val) {
        case '暂无风险':
          return '#19AF47';
        case '黄色预警':
          return '#e89100';
        case '橙色预警':
          return '#ff551f';
        case '红色预警':
          return '#e80000';
      }
    },
    removeProbabilityCircle() {
      if (this.probabilityCircleLayer) {
        this.map.removeLayer(this.probabilityCircleLayer);
      }
    },
    addMapLayer(pointLayer, layerName) {
      pointLayer.set('name', layerName);
      pointLayer.setZIndex(20);
      this.map.addLayer(pointLayer);
    },
    removeMapLayer(layerName) {
      if (this.isLayerExist(layerName)) {
        this.removeLayerByName(layerName);
      }
    },
    //加载台风70%概率风圈
    addTyphoonProbabilityCircle(coordinates) {
      this.removeProbabilityCircle();
      this.probabilityCircleLayer = drawPolygon(coordinates, {
        fillColor: 'rgb(234,16,16, 0.2)',
        lineColor: 'rgb(234,16,16, 0.2)',
        lineWidth: 2
      });
      this.probabilityCircleLayer.set('name', 'probabilityCircleLayer');
      this.map.addLayer(this.probabilityCircleLayer);
    },
    //获取等值线颜色
    getIsolineColor(count) {
      let color = '#00a0ff';
      if (count >= 10 && count < 25) {
        color = '#4bc704';
      } else if (count >= 25 && count < 50) {
        color = '#fecb6e';
      } else if (count >= 50 && count < 100) {
        color = '#ff8a21';
      } else if (count >= 100 && count < 250) {
        color = '#fe323a';
      } else if (count >= 250) {
        color = '#f32fdf';
      }
      return color;
    },
    //加载等值线
    showIsoline(result) {
      esri2geo(result, (err, data) => {
        let vectorSource = new VectorSource({
          features: new GeoJSON().readFeatures(data)
        });
        if (this.isolineLayer != null) {
          this.map.removeLayer(this.isolineLayer);
          this.isolineLayer = null;
        }

        //设置等值线样式和数字
        const FeatureStyle = (feat) => {
          const count = feat.values_.Contour ? feat.values_.Contour : '';
          const color = this.getIsolineColor(count);
          return new Style({
            stroke: new Stroke({
              color,
              width: 2
            }),
            text: new Text({
              text: count.toFixed(0),
              textAlign: 'center',
              fill: new Fill({
                //文字填充样式
                color: '#fff'
              })
            })
          });
        };

        this.isolineLayer = new VectorLayer({
          source: vectorSource,
          zIndex: 100,
          style: FeatureStyle
        });
        this.isolineLayer.set('name', 'isolineLayer');
        this.map.addLayer(this.isolineLayer);
      });
    },
    // 展示预报网格数据
    showPredictRain(data, param) {
      let arr = [];
      for (let item of data) {
        if (item.rainfall > 0) {
          arr.push(item);
        }
      }
      if (arr.length > 0) {
        this.changeScreenLoading(true);
        let params = this.formatRainfallParam(arr, 'rainfall');
        this._showTestPoint(params.features);
        this.getForecastRainfallLayer({
          opt: params,
          type: false,
          area: this.currentSelectArea,
          hour: param.hour
        }).then((result) => {
          let mapImage = result.mapImage;
          this.changeScreenLoading(false);
          this.getIsohyetOpacity(mapImage.xmin, mapImage.ymin, mapImage.xmax, mapImage.ymax, result.jobId, 'forecast');
        });
      } else {
        this.changeScreenLoading(false);
        this.ScreenMap.removeLayerByName('rainfall');
        this.ScreenMap.clearTextLayer();
      }
    },
    async getIsohyetOpacity(xmin, ymin, xmax, ymax, jobId, type) {
      //type用来区分预报和实时等值面服务 forecast为预报 预报请求112 实时请求117
      let bbox = xmin + ',' + ymin + ',' + xmax + ',' + ymax;
      let params = new URLSearchParams();
      params.append('bbox', bbox);
      params.append('f', 'pjson');
      params.append('transparent', true);
      let url = `${mapConfig.mapServer}gps/dengzhiPIDWReal0605/MapServer/jobs/${jobId}/export`;
      if (type == 'forecast') {
        url = `${mapConfig.mapServer}gps/PointTRaster/MapServer/jobs/${jobId}/export`;
      }
      await axios({
        url: url,
        method: 'post',
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        data: params
      }).then((res) => {
        this.changeScreenLoading(false);
        this.showIsohyetLayer(res.data);
      });
    },
    /** 雨量数据转换成等雨量面参数格式 */
    formatRainfallParam(list, paramName) {
      let geoJson = {
        features: []
      };
      let featuresArr = [];
      list.forEach((item) => {
        // let drp = item.drp > 0 ? item.drp : 0;
        // if (item[paramName] != "-" && item[paramName] > 0) {
        let obj = {
          attributes: {
            x: Number(item.longitude),
            y: Number(item.latitude),
            value: item[paramName]
          },
          geometry: {
            x: Number(item.longitude),
            y: Number(item.latitude)
          }
        };
        featuresArr.push(obj);
        // }
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    //加载雨量点位
    loadRainfallPoint(rainList, showFlag, type) {
      let style = {
        anchor: [5, 5],
        anchorXUnits: 'pixels'
      };
      let pointList = [];
      if (showFlag) {
        for (let point of rainList) {
          let imgSrc = require('@/assets/images/legend/rain_0.png');
          if (point.drp > 0) {
            imgSrc = require('@/assets/images/legend/rain_10.png');
          }
          if (point.drp > 10) {
            imgSrc = require('@/assets/images/legend/rain_10_25.png');
          }
          if (point.drp > 25) {
            imgSrc = require('@/assets/images/legend/rain_25_50.png');
          }
          if (point.drp > 50) {
            imgSrc = require('@/assets/images/legend/rain_50_100.png');
          }
          if (point.drp > 100) {
            imgSrc = require('@/assets/images/legend/rain_100_250.png');
          }
          if (point.drp >= 250) {
            imgSrc = require('@/assets/images/legend/rain_250.png');
          }
          if (point.drp == null) {
            point.drp = -1;
          }
          if (type) {
            pointList.push({
              type: '雨量',
              lng: point.longitude,
              lat: point.latitude,
              props: { 雨量: `${Number(point.drp) == -1 ? '-' : point.drp}mm` },
              src: imgSrc,
              pointType: true
            });
          } else {
            if (point.mgstcd) {
              pointList.push({
                name: point.stationName,
                type: '雨量',
                lng: point.longitude,
                lat: point.latitude,
                code: point.mgstcd,
                props: { 雨量: `${Number(point.drp) == -1 ? '-' : point.drp}mm`, 位置: `${point.stLoc || '-'}` },
                src: imgSrc
              });
            }
          }
        }
        this.drawPoint(pointList, style, 'rainLayer');
      } else {
        this.drawPoint([], {}, 'rainLayer');
      }
    }
  },
  beforeDestroy() {
    // window.removeEventListener("load");
  }
};
</script>
<style lang="scss" scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  #ControlMainView.cursorImg {
    cursor: url('../../../assets/images/end-point.png'), pointer !important;
  }
  #ControlMainView.analysisImg {
    cursor: url('../../../assets/images/analysis.png'), pointer !important;
  }
  #ControlMainView.rainfallImg {
    cursor: url('../../../assets/images/rainfall.png'), pointer !important;
  }
  .map {
    width: 100%;
    height: 100%;
    ::v-deep .ol-layer {
      // canvas{
      //   width:100%;
      // }
    }
  }
  .playable-timeline-bar {
    position: absolute;
    width: 800px;
    bottom: 100px;
    height: 100px;
    @include flexbox;
    @include flexJC(center);
    @include flexAC;
    z-index: 100;
    left: 50%;
    transform: translateX(-50%);
    &.is-big-screen {
      transform: translateX(-50%) scaleX(0.5);
    }
  }
  .ol-popup {
    position: absolute;
    bottom: 15px;
    left: -50%;
    transform: translateX(-50%);
    border-radius: 3px;
    min-width: 150px;
    .ol-popup__wrapper {
      background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
      border: 2px solid #00c1ff;
      box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
      border-radius: 3px;
      height: 100%;
      width: 100%;
      padding: 16px;
      position: relative;
      opacity: 0.9;
      &:before {
        content: '';
        position: absolute;
        display: block;
        box-sizing: content-box;
        width: 0px;
        height: 0px;
        border: 7px solid transparent;
        border-top: 7px solid #103567;
        border-bottom: 0;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%) translateY(100%);
        z-index: 100;
      }
      &:after {
        content: '';
        position: absolute;
        display: block;
        box-sizing: content-box;
        width: 0px;
        height: 0px;
        border: 10px solid transparent;
        border-top: 10px solid #00c1ff;
        border-bottom: 0;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%) translateY(100%);
      }
      .ol-popup__title {
        font-size: 18px;
        color: #fff;
      }
      .ol-popup__content {
        color: #fff;
        .ol-popup__content--props {
          display: flex;
          flex-direction: column;
          .props-item {
            // padding: 0 12px;
            position: relative;
            display: flex;
            align-items: center;
            min-width: 150px;
            width: max-content;
            /* &:not(:only-child) {
              flex-direction: column;
              min-width: 100px;
              &:after {
                content: "";
                position: absolute;
                height: 80%;
                right: 0;
                top: 10%;
                border-right: 1px solid #00c1ff4d;
              }
              &:first-child {
                padding-left: 0;
              }
              &:last-child {
                padding-right: 0;
                &:after {
                  border-right: 0;
                }
              }
              .item-label {
                font-size: 16px;
              }
              .item-data {
                font-size: 18px;
                color: #33ffff;
                line-height: 25px;
              }
            } */
            .item-label {
              font-size: 16px;
            }
            .item-data {
              margin-left: 8px;
              font-size: 18px;
              color: #33ffff;
              line-height: 25px;
            }
            &:first-child {
              &:not(:only-child) {
                margin-top: 8px;
              }
            }
          }
          .props-label {
            line-height: 20px;
            width: 350px;
          }
        }
      }
    }
    &.typhoon-popup {
      width: 254px;
      color: #fff;
      .typhoon-point_title {
        text-align: center;
        font-size: 18px;
        color: #ffffff;
        letter-spacing: 0;
        line-height: 24px;
        font-weight: 600;
        padding-bottom: 10px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.4);
      }
      .typhoon-point_content {
        p {
          padding: 8px 0;
          > span {
            font-size: 18px;
            color: #33ffff;
            font-weight: 600;
            padding-right: 8px;
            &:first-child {
              color: #fff;
            }
          }
        }
      }
    }
    &.scheme-popup {
      // min-width: 236px;
      font-size: 16px;
      color: #ffffff;
      line-height: 26px;
      .ol-popup__wrapper {
        padding: 8px 16px;
      }
      .scheme-point_sub_title {
        font-size: 14px;
        color: #1ef8f2;
      }
      .scheme-point_sub_box {
        display: flex;
        flex-direction: column;
        height: 32px;
      }
      p {
        span {
          display: inline-block;
        }
        span:first-child {
          width: 90px;
          text-align: left;
        }
        span:last-child {
          font-size: 18px;
          color: #33ffff;
        }
      }
    }
  }
  .icon-footer-wind {
    width: 1rem;
    height: 1rem;
    margin: 0;
  }
}

.stationimage_box {
  height: 315px;
  width: 100%;
  background-size: auto 100%;
  position: relative;
  .station_list {
    height: 315px;
    width: 100%;
    @include flexbox();
    @include flexAI(center);
    // @include flexJC(space-between);
    padding: 25px 50px 0 50px;
    .station_item {
      margin-right: 70px;
      position: relative;
      text-align: center;
      &:last-child {
        margin-right: 0;
      }
      p {
        width: 100%;
        text-align: center;
        position: relative;
        top: -70px;
        color: #1492ff;
        font-size: 16px;
      }
      img {
        cursor: pointer;
      }
    }
  }
}
</style>
<style lang="scss">
.ol-weather-popup {
  user-select: none;
  cursor: pointer;
  width: 240px;
  height: 160px;
  background-image: linear-gradient(180deg, #0096c3 2%, #103567 100%);
  border: 2px solid #00c1ff;
  box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
  border-radius: 3px;
  position: relative;
  opacity: 0.9;
  &:before {
    content: '';
    position: absolute;
    display: block;
    box-sizing: content-box;
    width: 0px;
    height: 0px;
    border: 7px solid transparent;
    border-top: 7px solid #103567;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%) translateY(100%);
    z-index: 100;
  }
  &:after {
    content: '';
    position: absolute;
    display: block;
    box-sizing: content-box;

    width: 0px;
    height: 0px;
    border: 10px solid transparent;
    border-top: 10px solid #00c1ff;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%) translateY(100%);
  }
  .weather-details {
    border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
    margin: 0 8px;
    padding-top: 8px;
    color: #fff;
    > div {
      padding: 7px 0;
      display: flex;
      justify-content: space-between;
    }
  }
  .ol-weather-popup__content {
    height: 100%;
    width: 100%;
    color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .weather-icon {
      margin-left: 16px;
      width: 48px;
      i {
        width: 32px;
        height: 32px;
      }
    }
    .weather-label {
      flex: 2;
      text-align: left;
      display: flex;
      flex-direction: column;
      line-height: 24px;
      font-size: 16px;
      margin-right: 8px;
      .label-name {
        display: flex;
        justify-content: space-between;
      }
      .weather {
        font-size: 18px;
      }
    }
  }
}
.map {
  .fixed-ovly {
    background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
    border: 2px solid #00c1ff;
    box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
    border-radius: 3px;
    height: 100%;
    width: 100%;
    padding: 16px;
    position: relative;
    opacity: 0.9;
    color: #fff;
    p {
      padding-bottom: 6px;
      span {
        color: #00c1ff;
      }
    }
    &.fixed-ovly-blue {
      background-image: linear-gradient(180deg, #00afaf 2%, #002c2c 100%);
      border: 2px solid #33ffff;
      p {
        span {
          color: #33ffff;
        }
      }
    }
    &.fixed-ovly-green {
      background-image: linear-gradient(180deg, #00b638 2%, #00240b 100%);
      border: 2px solid #19af47;
      p {
        span {
          color: #00ff4e;
        }
      }
    }

    &:before {
      content: '';
      position: absolute;
      display: block;
      box-sizing: content-box;
      width: 0px;
      height: 0px;
      border: 7px solid transparent;
      border-top: 7px solid #103567;
      border-bottom: 0;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
      z-index: 100;
    }
    &:after {
      content: '';
      position: absolute;
      display: block;
      box-sizing: content-box;
      width: 0px;
      height: 0px;
      border: 10px solid transparent;
      border-top: 10px solid #00c1ff;
      border-bottom: 0;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
    }
  }
}
.rainrisk-dynamic {
  width: 50px;
  height: 50px;
  background: url(~assets/images/MapPoint/risk-rain.png) no-repeat center center;
  background-size: 60% 65%;
}
.flood-dynamic {
  width: 50px;
  height: 50px;
  background: url(~assets/images/MapPoint/torrents_higher.png) no-repeat center center;
  background-size: 60% 65%;
}
.reservoir-dynamic {
  width: 50px;
  height: 50px;
  background: url(~assets/images/MapPoint/reservoir-fault.png) no-repeat center center;
  background-size: 60% 65%;
}
.dynamic-child {
  background-color: red;
  border-radius: 100%;
  -webkit-animation: scaleout 1s infinite ease-in-out;
  animation: scaleout 1s infinite ease-in-out;
  width: 50px;
  height: 50px;
}
#clusterPointPopup {
  .cluster-point-popup {
    width: 300px;
    .cluster-point-list {
      max-height: 300px;
      overflow: auto;
      .cluster-point-item {
        color: #fff;
        padding: 6px 0;
        font-size: 14px;
        cursor: pointer;
        &:hover {
          color: #1cfff5;
        }
      }
    }
  }
}

.waterSour-popup {
  color: #fff;
  font-size: 14px;
  .top-box {
    position: relative;
    width: 250px;
    height: 100px;
    img {
      width: 100%;
      height: 100%;
    }
    span {
      position: absolute;
      bottom: 5px;
      left: 5px;
    }
  }
  .item {
    margin-top: 10px;
    span:nth-child(2) {
      color: #33ffff;
      margin-left: 8px;
    }
  }
}

// background: url(~assets/images/MapPoint/risk-rain.gif) no-repeat center center;
// background-size:100% 100%;
@-webkit-keyframes scaleout {
  0% {
    -webkit-transform: scale(0);
  }
  100% {
    -webkit-transform: scale(1);
    opacity: 0;
  }
}
@keyframes scaleout {
  0% {
    transform: scale(0);
    -webkit-transform: scale(0);
  }
  100% {
    transform: scale(1);
    -webkit-transform: scale(1);
    opacity: 0;
  }
}
</style>
