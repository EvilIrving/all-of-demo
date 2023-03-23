<!--
 * @Date: 2022-05-11 10:46:45
 * @Author: 
 * @Description: 防洪调度 地图
 * @LastEditors: dtb
 * @LastEditTime: 2022-05-24 13:18:19
 * @FilePath: \lucheng\src\views\ourPujiang\FloodControlMap.vue
-->
<template>
  <div class="our-pujiang-box">
    <!-- 地图 -->
    <div v-if="showRehearsalMap" class="our-map-box map-wrap" :class="{ 'max-our-type': downType == 1, 'min-our-type': downType == 2 }">
      <!-- 暴雨归档 -->
      <div class="tab-box">
        <div class="tab-control tab-control-00" @click="rainstormDialog = true">暴雨归档</div>
      </div>
      <div class="map-box map" id="rehearsalMap" ref="rehearsalMap">
        <div :class="{ 'spread-over': !separateType }">
          <div class="line left-line" :class="{ 'line-width': separateType }"></div>
          <div class="line right-line" :class="{ 'line-width': separateType }"></div>
        </div>
        <div v-if="separateType">
          <div class="line left-line" :class="{ 'line-width': separateType }"></div>
          <div class="line right-line" :class="{ 'line-width': separateType }"></div>
        </div>
      </div>
      <div v-if="!separateType" class="down-box" :class="{ 'top-rotate-box': downType == 1, 'bottom-rotate-box': downType == 2 }" @click="downBox"></div>
      <div v-if="!separateType" class="down_wrapper" :class="{ 'hideBottom': downType == 1, 'showBottom': downType == 2 }"></div>
    </div>

    <div v-else class="scheme_map">
      <div class="map-box map" id="leftMap" ref="leftMap">
        <img src="../../assets/images/ourPujiang/back.png" class="" alt="" @click="showRehearsalMap = true;closeTab(true)">
        <LabelFormItem class="form_item">
          <el-select v-model="rainfall" @change="rainSelectVal" placeholder="实时降雨">
            <el-option v-for="item in rainfallOption" :key="item.name" :label="item.name" :value="item.value" />
          </el-select>
        </LabelFormItem>
        <div :class="{ 'spread-over': !separateType }">
          <div class="line left-line" :class="{ 'line-width': separateType }"></div>
          <div class="line right-line" :class="{ 'line-width': separateType }"></div>
        </div>
      </div>
      <div class="map-box map" id="rightMap" ref="rightMap">
        <div>
          <div class="line left-line" :class="{ 'line-width': separateType }"></div>
          <div class="line right-line" :class="{ 'line-width': separateType }"></div>
        </div>
      </div>
    </div>

    <div v-show="clusterPointPopupShow" id="clusterPointPopup" class="ol-popup">
      <div :class="['ol-popup__wrapper', 'cluster-point-popup']">
        <div class="cluster-point-list">
          <div class="cluster-point-item" v-for="item in clusterPointList" :key="item.name" @click="clusterPointClick(item)">
            {{ item.name }}
          </div>
        </div>
      </div>
    </div>
    <!-- 四块内容 -->
    <div class="our-type-box" :class="{ 'hide-our-type': downType == 1, 'show-our-type': downType == 2 }">
      <div>
        <forecasted-chart />
      </div>
      <div>
        <warning-table></warning-table>
      </div>
      <div>
        <rehearsal ref="rehearsalWrapper" @changeMapType='changeMapType' />
      </div>
      <div>
        <preliminary-plan />
      </div>
    </div>

    <!-- 暴雨归档列表 -->
    <div class="our-bottom-icon"></div>
    <PrimaryMapDialog mainTitle="暴雨归档" ref="multipleDialog" :visible="rainstormDialog" @handleClose="handleClose" :appendToBody="true" :modal="true" top="6vh" width="1000px">
      <div class="dialog-content">
        <rainstorm-archive v-if="rainstormDialog" />
      </div>
    </PrimaryMapDialog>

    <!-- 点位悬浮框 -->
    <div v-show="popupShow || overlayEnterFlag" id="popup" class="ol-popup">
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
  </div>
</template>

<script>
import PreliminaryPlan from './components/PreliminaryPlan.vue'
import RainstormArchive from './dialog/RainstormArchive.vue'
import forecastedChart from '@/views/ourPujiang/components/forecastedChart'
import warningTable from '@/views/ourPujiang/components/WarningTable';
import Rehearsal from './components/Rehearsal.vue'
import { PrimaryMapDialog } from '@/components';
import esri2geo from 'esri2geo';
import { Fill, Stroke, Style } from 'ol/style';
import 'ol/ol.css';
import XYZ from 'ol/source/XYZ';
import WKT from 'ol/format/WKT';
import TileLayer from 'ol/layer/Tile';
import ImageLayer from 'ol/layer/Image';
import ImageStatic from 'ol/source/ImageStatic';
import { Map, View, Overlay } from 'ol';
import { defaults as defaultControls } from 'ol/control';
import { Vector as VectorSource, TileArcGISRest } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';
import GeoJSON from 'ol/format/GeoJSON';
import mapConfig from '@/config/mapConfig';
import {
  drawClusterPoint,
  drawPoint,
  drawLines,
  addLayerClick,
  newTyphoon,
  showTestClusterPoint,
  drawLineEx,
  drawPolygon,
  drawAreaPolygon
} from '@/utils/mapUtil.js';
import { queryMapserver } from '@/api/loadMapServe';
import { mapGetters, mapActions } from 'vuex';
import { boundingExtent } from 'ol/extent';
import luchengGeoJson from '@/assets/geojson/lucheng_FeaturesToJSON.json';
import { floodControl, sectionList } from '@/api/bigScreenPart';
import { cloneObj } from '@/utils/utils';
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
  name: 'FloodControlMap',
  props: {

  },
  components: {
    PreliminaryPlan,
    Rehearsal,
    PrimaryMapDialog,
    RainstormArchive,
    forecastedChart,
    warningTable
  },
  data() {
    return {
      rainfall: 0,
      rainfallOption: [{ name: ' 实时降雨', value: 0 }, { name: '预报降雨', value: 1 }, { name: '典型暴雨', value: 2 },],
      separateType: false,
      downType: 0,
      rainstormDialog: false,// 暴雨归档弹窗
      //地图
      map: null,
      //点位信息
      featureInfo: {},
      popupShow: false,
      isohyetLayer: null,
      textLayer: null,
      lineLayer: [],
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
      areaWKTLayer: null,
      lineFocusLayer: [],
      dynamicPointMatain: {},
      featureAttr: {},
      overlayEnterFlag: false,
      routeLayer: null, //路线图层
      designFloodLayer: {},
      windLayers: null,
      waveLayers: null,
      cloundLayer: null,
      radarRainLayer: null,
      waterRegulationPopupShow: false,
      clusterPointPopupShow: false,
      clusterPointList: [],
      clusterPointOverLay: null,
      raderTimeArr: [], //雷达降雨时间轴数据
      radarLineShow: false, //雷达降雨时间轴标记
      windWaveBaseLayers: null, //风场浪场背景图层
      isolineLayer: null, //等值线图层
      points: [],
      showRehearsalMap: true,
    };
  },
  computed: {
    ...mapGetters([
      'weatherPopover',
      'timelinebarShow',
      'CheckedArr',
      'DispatchCarousel',
      'GoodsList',
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
  created() {

  },
  mounted() {
    this.$nextTick(() => {
      this.mapInit();
    });
    this.getMapPoints()
    this.getSectionList()
  },
  methods: {
    ...mapActions([
      'setWeatherPopover',
      'changeArea',
      'changeTimelinebarShow',
      'SetWarehouseDispatchList',
      'SetTyphoonPointData',
      'SetSpotsFlag',
      'SetIsAction',
      'changeScreenLoading',
      'getForecastRainfallLayer'
    ]),
    closeTab(type) {
      this.$emit('closeTab', type)
    },
    changeMapType(type) {
      this.showRehearsalMap = type
      this.$emit('closeTab', type)
      this.leftMapInit()
      this.rightMapInit()
    },
    async getSectionList() {
      let res = await sectionList()
      if (res.code === 0 && res.data.list && res.data.list.length > 0) {
        let lineList = []
        for (let i = 0; i < res.data.list.length; i++) {
          let sectionItem = res.data.list[i];
          lineList.push({
            type: '断面',
            name: sectionItem.name,
            code: sectionItem.id,
            lineColor: '#4CEBC7',
            fontColor: '#000',
            coordinates: eval(sectionItem.coordinates),
            lineWidth: 5,
            props: {
              断面名称: sectionItem.name,
              更新时间: sectionItem.creationTime,// 无字段
              预报高水位: sectionItem.leftElevation,// 无字段
              预报时间: sectionItem.creationTime,// 无字段
            },
            ...sectionItem
          })
        }
        this.drawMoreLine(lineList)
      }
    },
    // 一次画多条线
    drawMoreLine(arr, layerName = 'lineLayer') {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let tempLayer = drawLines(arr);
      tempLayer.set('name', layerName);
      tempLayer.setZIndex(99);
      this.map.addLayer(tempLayer);
      const selectClick = addLayerClick(tempLayer);
      selectClick.on('select', function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit('point-click', features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    async getMapPoints() {
      let res = await floodControl()
      let reservoir = []; //泽雅水库
      let sluice = []; //戍浦江大闸
      let tide = []; //江心屿
      // RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站
      res.data.map((item) => {
        let obj = {
          name: item.stName,
          lat: item.stLat,
          lng: item.stLong,
          code: item.stCode,
          stType: item.stType,
          type: '水情',
        }
        if (item.stType == 'RR') {
          obj.props = {
            名称: item.stName,
            水位: item.waterLevel,
            放水状态: '-',// 无字段
            更新时间: item.time,
          }
        } else if (item.stType == 'DD') {
          obj.props = {
            名称: item.stName,
            闸上水位: item.upz,
            闸下水位: item.dwz,
            闸门状态: item.openGate > 0 ? '开启中' : '未开启',// 无字段
            更新时间: item.time,
          }
        } else {
          obj.props = {
            名称: item.stName,
            实时潮位: item.tdz,
            更新时间: item.time,
            预报高潮位: '2.957',// 无字段
            预报时间: '2022-05-25 06:00:00',// 无字段
          }
        }
        if (item.stType == 'RR') {
          reservoir.push(obj);
        } else if (item.stType == 'DD') {
          sluice.push(obj);
        } else if (item.stType == 'TT') {
          tide.push(obj);
        }
      });
      this.setPoint(reservoir, 'reservoir', 'normal', true);
      this.setPoint(sluice, 'sluice', 'normal', true);
      this.setPoint(tide, 'tide', 'normal', true);
    },

    getPointStyle(projectType, value) {
      let defaultStyle = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: null,
        src: null
      };
      let styleObj = cloneObj(defaultStyle);
      styleObj.src = require(`@/assets/images/MapPoint/point-${projectType}-${value}.png`);
      styleObj.srcs = require(`@/assets/images/MapPoint/group-${projectType}-${value}.png`);
      return styleObj;
    },
    setPoint(pointList, type, status, flag) {
      let pointStyle = flag ? this.getPointStyle(type, status) : {};
      let layer = type + status;
      this.drawClusterPoint(pointList, pointStyle, `${layer}Layer`);
    },
    downBox() {
      if (this.downType == 1) {
        this.downType = 2;
      } else {
        this.downType = 1;
      }
    },
    handleClose() {
      this.rainstormDialog = false
    },

    /**
     * 初始化地图
     */
    mapInit() {
      let mainLayer = new TileLayer(
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
      // view设置
      let viewSetting = new View({
        projection: 'EPSG:4326',
        center: [120.53062753412031, 28.032333108165375],
        zoom: 12.5,
        maxZoom: 20,
        minZoom: 3
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: 'rehearsalMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer],
        // layers: [tiandituVec, tiandituCva, mainLayer],
        view: viewSetting
      });
      this.registeredEvent(); //注册事件
      this.addStationOverlay();
    },
    leftMapInit() {
      let mainLayer = new TileLayer(
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
      // view设置
      let viewSetting = new View({
        projection: 'EPSG:4326',
        center: [120.69687110397834, 28.020567885312342],
        zoom: 12,
        maxZoom: 16,
        minZoom: 3
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: 'leftMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer],
        // layers: [tiandituVec, tiandituCva, mainLayer],
        view: viewSetting
      });
      this.registeredEvent(); //注册事件
    },
    rightMapInit() {
      let mainLayer = new TileLayer(
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
      // view设置
      let viewSetting = new View({
        projection: 'EPSG:4326',
        center: [120.69687110397834, 28.020567885312342],
        zoom: 12,
        maxZoom: 16,
        minZoom: 3
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: 'rightMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer],
        // layers: [tiandituVec, tiandituCva, mainLayer],
        view: viewSetting
      });
      this.registeredEvent(); //注册事件
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
        // autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.overlay);
    },
    //多点位时悬浮窗
    addClusterPointOverLay() {
      this.clusterPointOverLay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#clusterPointPopup'),
        // autoPan: true,
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
      this.map.on('moveend', (event) => {
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
      });
      this.map.on('pointermove', (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let flag = false;
        this.clusterPointPopupShow = false;
        this.map.getTargetElement().style.cursor = 'auto';
        this.map.forEachFeatureAtPixel(pixel, (feature) => {
          this.featureAttr = feature.values_.attr;
          let coodinate = event.coordinate;
          let properties = feature.getProperties();
          this.map.getTargetElement().style.cursor = 'pointer';
          if (feature && properties) {
            //聚合时 只展示一个点信息
            if (properties.features && properties.features.length == 1) {
              this.featureInfo = {
                name: properties.features[0].values_.data.name,
                props: properties.features[0].values_.data.props,
                label: properties.features[0].values_.data.label || false
              };
              if (this.featureInfo) {
                this.overlay.setPosition(coodinate);
                flag = true;
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
                coodinate = [properties.data.lng, properties.data.lat]
              }
              // console.log(properties.data);
              //非聚合点位时显示信息
              this.featureInfo = {
                recType: properties.data.recType || false,
                name: properties.data.name,
                props: properties.data.props,
                label: properties.data.label || false
              };
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
      let mapDom = document.getElementById('rehearsalMap');
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
     * 使用数据中的 coorpot 属性画线
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
      let layer = drawLineEx(coordinates);
      this.lineLayer.push(layer);
      layer.set('name', layerName);
      layer.setZIndex(10);
      this.map.addLayer(layer);
      let selectClick = addLayerClick(layer);
      selectClick.on('select', (e) => {
        let features = e.target.getFeatures().array_;
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
  },
};
</script>

<style lang="scss" scoped>
.our-pujiang-box {
  height: 100%;
  box-sizing: border-box;
  padding: 140px 14px 0;
  background: url(~assets/images/ourPujiang/our-pujiang-icon.png) no-repeat;
  background-size: 100% 100%;
  position: relative;
  overflow: hidden;
  .tab-box {
    position: absolute;
    right: 2%;
    top: 2%;
    z-index: 100;
    .tab-control {
      width: 128px;
      height: 43px;
      background: url(~assets/images/ScreenAsset/tab-control-icon.png) no-repeat;
      background-size: 100% 100%;
      padding-left: 16px;
      box-sizing: border-box;
      line-height: 43px;
      position: relative;
      font-size: 16px;
      color: #ffffff;
      margin-bottom: 24px;
      cursor: pointer;
      &::after {
        content: '';
        position: absolute;
        width: 60px;
        height: 60px;
        right: -10px;
        top: 50%;
        margin-top: -30px;
      }
      &.tab-control-00 {
        &::after {
          background: url(~assets/images/ourPujiang/rainstorm.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }
  }
  .our-map-box {
    height: 100%;
    // height: calc(100% - 366px);
    position: relative;
    .map-box {
      // display: flex;
      // justify-content: space-between;
      height: 100%;
      > div {
        width: 1016px;
        position: relative;
        &.spread-over {
          width: 100%;
        }
        &::after {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          height: 66px;
          width: 100%;
          background: url(~assets/images/ourPujiang/map-top-icon.png) no-repeat;
          background-size: 100% 100%;
        }
        &::before {
          content: '';
          position: absolute;
          bottom: -525px;
          left: 0;
          height: 66px;
          width: 100%;
          background: url(~assets/images/ourPujiang/map-bottom-icon.png) no-repeat;
          background-size: 100% 100%;
        }
        .line {
          position: absolute;
          width: 3px;
          height: calc(100% - 132px);
          top: 66px;
          background: url(~assets/images/ourPujiang/map-line-icon.png) no-repeat;
          background-size: 100% 100%;
          &.line-width {
            width: 2px;
          }
          &.left-line {
            left: 0;
          }
          &.right-line {
            right: 0;
            transform: rotate(180deg);
          }
        }
      }
    }
    .down-box {
      width: 46px;
      height: 38px;
      position: absolute;
      bottom: 388px;
      left: calc(50% - 23px);
      cursor: pointer;
      background: url(~assets/images/ourPujiang/down.png) no-repeat;
      background-size: 100% 100%;
      &.top-rotate-box {
        animation: 2s rotateTop 1 forwards;
      }
      &.bottom-rotate-box {
        animation: 2s rotateBottom 1 forwards;
      }
    }
    .down_wrapper {
      bottom: -14px;
      position: absolute;
      width: 100%;
      height: 388px;
      background: rgba($color: #000, $alpha: 0.7);
      &.max-our-type {
        animation: 2s maxHeight 1 forwards;
      }
      &.min-our-type {
        animation: 2s minHeight 1 forwards;
      }
    }
    &.max-our-type {
      animation: 2s maxHeight 1 forwards;
    }
    &.min-our-type {
      animation: 2s minHeight 1 forwards;
    }
  }
  .scheme_map {
    height: calc(100% - 366px);
    display: flex;
    justify-content: space-between;
    .map-box {
      height: 100%;
      width: 49.5%;
      position: relative;
      img {
        position: absolute;
        top: 2%;
        left: 2%;
        width: 8%;
        z-index: 200;
        cursor: pointer;
      }
      .form_item {
        position: absolute;
        top: 2%;
        right: 2%;
      }
      > div {
        width: 100%;
        position: relative;
        &::after {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          height: 66px;
          width: 100%;
          background: url(~assets/images/ourPujiang/map-top-icon.png) no-repeat;
          background-size: 100% 100%;
        }
        &::before {
          content: '';
          position: absolute;
          bottom: -525px;
          left: 0;
          height: 66px;
          width: 100%;
          background: url(~assets/images/ourPujiang/map-bottom-icon.png) no-repeat;
          background-size: 100% 100%;
        }
        .line {
          position: absolute;
          width: 3px;
          height: calc(100% - 132px);
          top: 66px;
          background: url(~assets/images/ourPujiang/map-line-icon.png) no-repeat;
          background-size: 100% 100%;
          &.line-width {
            width: 2px;
          }
          &.left-line {
            left: 0;
          }
          &.right-line {
            right: 0;
            transform: rotate(180deg);
          }
        }
      }
    }
  }
  .our-type-box {
    height: 320px;
    margin-top: 16px;
    display: flex;
    justify-content: space-between;
    position: absolute;
    bottom: 30px;
    width: calc(100% - 28px);
    > div {
      width: 498px;
      padding: 0 10px;
      color: #fff;
      position: relative;
      background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
      background-size: 100% 100%;
      background-color: rgba($color: #031a2b, $alpha: 0.9);
    }
    &.show-our-type {
      animation: 2s showOur 1 forwards;
    }
    &.hide-our-type {
      animation: 2s hideOur 1 forwards;
    }
  }
  .our-bottom-icon {
    width: 100%;
    height: 24px;
    position: absolute;
    bottom: 0;
    left: 0;
    background: url(~assets/images/ourPujiang/our-bottom-icon.png) no-repeat;
    background-size: 100% 100%;
  }
}
@keyframes maxHeight {
  0% {
    height: calc(100% +34px);
  }
  100% {
    height: calc(100% + 366px);
  }
}
@keyframes minHeight {
  0% {
    height: calc(100% + 366px);
  }
  100% {
    height: calc(100% +34px);
  }
}
@keyframes showOur {
  0% {
    bottom: -400px;
    opacity: 0;
  }
  100% {
    bottom: 30px;
    opacity: 1;
  }
}
@keyframes hideOur {
  0% {
    bottom: 30px;
    opacity: 1;
  }
  100% {
    bottom: -400px;
    opacity: 0;
  }
}
@keyframes rotateTop {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(180deg);
  }
}
@keyframes rotateBottom {
  0% {
    transform: rotate(180deg);
  }
  100% {
    transform: rotate(0deg);
  }
}
@keyframes hideBottom {
  0% {
    bottom: -14px;
  }
  100% {
    bottom: -388px;
  }
}
@keyframes showBottom {
  0% {
    bottom: -388px;
  }
  100% {
    bottom: -14px;
  }
}
</style>

<style lang="scss" scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  #ControlMainView.cursorImg {
    cursor: url('../../assets/images/end-point.png'), pointer !important;
  }
  #ControlMainView.analysisImg {
    cursor: url('../../assets/images/analysis.png'), pointer !important;
  }
  #ControlMainView.rainfallImg {
    cursor: url('../../assets/images/rainfall.png'), pointer !important;
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
