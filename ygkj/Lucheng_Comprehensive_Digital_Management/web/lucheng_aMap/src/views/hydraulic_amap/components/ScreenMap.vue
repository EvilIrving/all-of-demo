<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>

    <!-- 工具栏 -->
    <!-- <div class="map-operate">
      <primary-operate
        @measure="measure"
        @zoomChange="zoomChange"
        @basinCommand="basinCommand"
      ></primary-operate>
    </div> -->

    <!-- <screen-footer :map-bind="map" @measure="measure" /> -->

    <div id="popup" class="ol-popup">
      <div :class="['ol-popup__wrapper']">
        <div class="ol-popup__title">{{ featureInfo.name }}</div>
        <div class="ol-popup__content">
          <div class="ol-popup__content--props">
            <div :key="key" v-for="(prop, key) of featureInfo" class="props-item">
              <span class="item-label">{{ key }}</span>
              <span class="item-data" :style="{ color: key == '汛限水位' ? '#eec80b' : '#1cfff5' }">
                {{ prop == "null" ? "-" : prop == "null m" ? "- m" : prop }}
              </span>
            </div>
            <div v-if="featureInfo.label" class="props-label">
              <span> {{ featureInfo.label }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 台风点位信息悬浮框 -->
    <div v-show="typhoonPoint" ref="typhoonPopup" id="typhoonPopup" class="ol-popup typhoon-popup">
      <div id="popup-content" class="ol-popup__wrapper">
        <div class="typhoon-point_title">
          {{ typhoonName }}
        </div>
        <div class="typhoon-point_content">
          <p v-for="(value, key) in typhoonPointData" :key="key">
            <span>{{ key }}</span>
            <span>{{ value }}</span>
          </p>
        </div>
      </div>
    </div>
    <div v-show="clusterPointPopupShow" id="clusterPointPopup" class="ol-popup">
      <div :class="['ol-popup__wrapper', 'cluster-point-popup']">
        <div class="cluster-point-list">
          <div class="cluster-point-item" v-for="item in clusterPointList" :key="item.data.id" @click="clusterPointClick(item)">
            {{
              item.data.text ||
              item.data.name ||
              item.data.stName ||
              item.data.wagaName ||
              item.data.stationName ||
              item.data.cameraName ||
              item.data.resName ||
              item.data.mnpdName ||
              item.data.dikeName ||
              item.data.seawallName ||
              item.data.pustName ||
              item.data.gateName ||
              item.data.hystName ||
              item.data.projectName ||
              item.data.irrName ||
              item.data.cwsName ||
              item.data.prnm ||
              item.data.hystName
            }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import esri2geo from "esri2geo";
import GeoJSON from "ol/format/GeoJSON";
import { Map, View, Overlay } from "ol";
import { XYZ } from "ol/source";
import TileLayer from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import ImageLayer from "ol/layer/Image";
import ImageStatic from "ol/source/ImageStatic";
import { Fill, Stroke, Style, Circle } from "ol/style";
import { Vector as VectorSource } from "ol/source";
import { Vector as VectorLayer } from "ol/layer";
import EsriJSON from "ol/format/EsriJSON";
import { createXYZ } from "ol/tilegrid";
import { tile as tileStrategy } from "ol/loadingstrategy";
import $ from "jquery";
import { ScreenFooter, PrimaryOperate } from "@/components";
import { mapManageApi } from "@/api/mapManage";
import Measure from "@/utils/measure";
import { WindLayer } from "ol-wind";
const riverLayesBaseService = mapConfig.mapServer + "/waterSearch/";
import { boundingExtent } from "ol/extent";
import luchengGeoJson from "@/assets/geojson/lucheng_FeaturesToJSON.json";
import {
  drawSkWaterPoint,
  addLayerClick,
  drawProjectPoint,
  drawAnaylysiscetPoint,
  drawSkRainPoint,
  newTyphoon,
  drawPolygon,
  drawPoint,
  drawLines,
  drawCircleInMeter,
  drawLineInMeter,
  showTestPoint,
  drawLineEx,
  drawVideoPoint,
  drawClusterPoint,
} from "@/utils/mapUtil.js";
import { queryMapserver } from "@/api/loadMapServe";
import mapConfig from "@/config/mapConfig";
export default {
  name: "ScreenMap",
  components: { PrimaryOperate, ScreenFooter },
  props: {},
  data() {
    return {
      //地图
      subtext: "mm",
      rainLayer: null, //降雨分析图层
      waterlogging: null, //积水点
      drainage: null, //河网水位
      seaLevel: null, //潮位
      sluice: null, //闸门
      map: null,
      popupData: {},
      stcd: null,
      stLng: null,
      stLat: null,
      container: null,
      content: null,
      popupCloser: null,
      overlay: null,
      featureInfo: {},
      isohyetLayer: null, //面雨量图层
      layerObj: {},
      typhoonLayers: [],
      typhoonNames: [],
      typhoonTimes: [],
      projectInScope: [], //测面数据组
      scopeLayer: [],
      searchStatus: false,
      areaLayer: null,
      basinLayer: null,
      circleLayer: null,
      linelLineLayer: null,
      areaArray: [],
      textLayer: null,
      lineLayer: [],
      analysisPointsLayer: [],
      typhoonsMap: new Map(),
      typhoonPointData: {}, //台风点位详情
      typhoonName: "",
      typhoonPoint: false,
      pointOverlay: null, //台风点位点击弹框
      windLayers: null,
      waveLayers: null,
      cloundLayer: null,
      radarRainLayer: null,
      clusterPointPopupShow: false,
      clusterPointList: [],
      clusterPointOverLay: null,
    };
  },
  created() { },
  mounted() {
    this.$nextTick(() => {
      this.mapInit();
    });
  },
  watch: {},
  methods: {
    //加载鹿城流域图
    addLuchengGeoJsonLayer() {
      let vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(luchengGeoJson),
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
            color: "#2c79f0",
            width: 2,
          }),
          // fill: new Fill({
          //   color: 'rgba(0, 196, 255, .5)'
          // })
        }),
      });
      this.map.addLayer(this.windWaveBaseLayers);
    },
    bigScreenGo(path) {
      this.$router.push(path);
    },
    //初始化地图
    async mapInit() {
      //天地图矢量底图
      let tiandituVec = new TileLayer({
        id: "tiandituVec",
        zIndex: 0,
        source: new XYZ({
          url: "http://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b",
          projection: "EPSG:4326",
        }),
      });
      //天地图矢量底图文字标记
      let tiandituCva = new TileLayer({
        id: "tiandituVec",
        zIndex: 0,
        source: new XYZ({
          url: "http://t0.tianditu.gov.cn/DataServer?T=cva_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b",
          projection: "EPSG:4326",
        }),
      });
      //地图填充图层
      let SLTCLayer = new TileLayer({
        id: "SLTCLayer",
        source: new XYZ({
          url: "https://webgis.ygwjg.com/arcgis/rest/services/wenzhou/wenzhou_luchengbs/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        zIndex: 0,
        visible: true,
      });
      let mainLayer = new TileLayer(
        {
          id: "zhejiangmainSLLayer",
          source: new XYZ({
            url: "https://sldtpt.zjwater.com:6443/lcqslqyszhghpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
            projection: "EPSG:4326",
          }),
        },
        { zoomOffset: 1 }
      );

      let SLMCLayer = new TileLayer({
        id: "zhejiangmainSLMCLayer",
        zIndex: 100,
        source: new XYZ({
          url:
            mapConfig.mapServer +
            "wenzhou/shuiliditu/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
      });
      // 扰动图斑
      let pattern_spot20180 = new TileLayer({
        id: "pattern2018",
        zIndex: 99,
        source: new XYZ({
          url:
            mapConfig.mapServer +
            "/mapservice/pattern_spot20180/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        visible: 0,
        preload: 1,
      });
      let pattern_spot20190 = new TileLayer({
        id: "pattern2019",
        zIndex: 99,
        source: new XYZ({
          url:
            mapConfig.mapServer +
            "/mapservice/pattern_spot20190/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        visible: 0,
        preload: 1,
      });
      let pattern_spot20200 = new TileLayer({
        id: "pattern2020",
        zIndex: 99,
        source: new XYZ({
          url:
            mapConfig.mapServer +
            "/mapservice/pattern_spot20200/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        visible: 0,
        preload: 1,
      });
      // 遥感影像
      let Speckles = new TileLayer({
        id: "Speckles",
        zIndex: 99,
        source: new XYZ({
          url: "",
          projection: "EPSG:4326",
        }),
        visible: 1,
        preload: 1,
      });

      //蓝线图层
      let blueLineLayer = new TileLayer({
        id: "KG_bulueLineLayerId",
        source: new XYZ({
          url:
            mapConfig.mapServer +
            "/mapservice/wz_kg_blueline/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        visible: 0,
        preload: 1,
      });

      // view设置
      let viewSetting = new View({
        projection: "EPSG:4326",
        center: [120.55389798474596, 28.068232997940203],
        zoom: 12.3,
        maxZoom: 22,
        minZoom: 3,
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: "mainView",
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          tiandituVec,
          tiandituCva,
          // SLTCLayer,
          mainLayer,
          // SLMCLayer,
          // pattern_spot20200,
          // pattern_spot20190,
          // pattern_spot20180,
          // Speckles,
          // blueLineLayer
        ],
        view: viewSetting,
      });
      await this.addLuchengGeoJsonLayer();
      await this.mapClick(); // 初始化地图成功后，给地图添加点击事件
      await this.addStationOverlay(); //加载水利监控图层的悬浮框
      await this.addTyphoonPointOverlay();
      // 地图鼠标移动事件
      await this.map.on("pointermove", this._showFeatureInfo);
      await this.addClusterPointOverLay();
    },
    addStationOverlay() {
      this.container = document.getElementById("popup");
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: this.container,
        autoPan: true, //是否自动平移，即假如标记在屏幕边缘，弹出时自动平移地图使弹出框完全可见
        autoPanAnimation: {
          duration: 250, //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度
        },
      });
    },
    _showFeatureInfo(e) {
      let pixel = this.map.getEventPixel(e.originalEvent);
      let flag = false;
      this.clusterPointPopupShow = false;
      this.map.getTargetElement().style.cursor = "auto";
      this.map.forEachFeatureAtPixel(pixel, (feature) => {
        let coodinate = e.coordinate;
        let properties;
        //聚合点位移入事件
        if (
          feature.getProperties().features &&
          feature.getProperties().features.length == 1
        ) {
          this.map.getTargetElement().style.cursor = "pointer";
          properties = feature.getProperties().features[0].values_.data;
          this.featureInfo = {
            ...properties.props,
          };
          //设置overlay的显示位置
          if (this.overlay) this.overlay.setPosition(coodinate);
          flag = true;
        } else if (
          feature.getProperties().features &&
          feature.getProperties().features.length > 1
        ) {
          //多个聚合点时
          properties = feature.getProperties();
          let arr = properties.features.map((item) => {
            return item.values_;
          });
          this.clusterPointList = arr;
          const coodinate = [
            properties.geometry.extent_[0],
            properties.geometry.extent_[1],
          ];
          this.clusterPointOverLay.setPosition(coodinate);
          this.clusterPointPopupShow = true;
        } else if (feature && feature.getProperties().featureProperties) {
          this.map.getTargetElement().style.cursor = "pointer";
          properties = feature.getProperties().featureProperties;
          if (properties.type) {
            const pointType = properties.type;
            let obj;
            switch (pointType) {
              case "水库水位":
              case "河道水位":
              case "堰闸水位":
              case "潮位":
                {
                  obj = {
                    名称: properties.data.stName,
                    水位: properties.data.waterLevel + "m",
                  };
                }
                break;
              // case "水闸开度":
              case "水闸工情":
                {
                  if (properties.data.upz) {
                    obj = {
                      名称: properties.data.stName,
                      闸上: properties.data.upz + "m",
                      闸下: properties.data.dwz + "m",
                    };
                  } else {
                    let controlGateFlow = properties.data.controlGateFlow
                      ? properties.data.controlGateFlow
                      : "-";
                    obj = {
                      名称: properties.data.wagaName,
                      控制过闸流量: controlGateFlow + "m³/s",
                    };
                  }
                }
                break;
              case "雨情":
                {
                  let data = properties.data.drp;
                  if (properties.data.drp && Number(properties.data.drp) < 0) {
                    data = "-";
                  }
                  obj = {
                    名称: properties.data.stationName,
                    降雨量: data + "mm",
                  };
                }
                break;
              case "断面":
              case "省控断面":
              case "水质监测":
                {
                  obj = {
                    名称: properties.data.name,
                  };
                }
                break;
              case "积水":
                {
                  obj = {
                    名称: properties.data.name,
                    积水:
                      properties.data.waterLevel +
                      properties.data.waterLevelUnit,
                  };
                }
                break;
              case "视频":
                {
                  obj = {
                    名称: properties.data.cameraName,
                  };
                }
                break;
              case "水库":
                {
                  obj = {
                    名称: properties.data.resName,
                    实时水位: properties.data.waterLevel
                      ? properties.data.waterLevel + "m"
                      : "-",
                    汛限水位: properties.data.currentLimitLevel
                      ? properties.data.currentLimitLevel + "m"
                      : "-",
                    更新时间: properties.data.dataTime,
                    鉴定结论: properties.data.safetyConclusion,
                  };
                }
                break;
              case "山塘": {
                obj = {
                  名称: properties.data.mnpdName,
                  山塘类型: properties.data.mnpdCate,
                  "总容积(万m³)": properties.data.jhhsRj,
                };
                break;
              }
              case "堤防":
                {
                  obj = {
                    名称: properties.data.dikeName,
                    堤防类别: properties.data.dikeType,
                    "堤防长度(m)": properties.data.dikeLen,
                  };
                }
                break;
              case "海塘":
                {
                  obj = {
                    名称: properties.data.seawallName,
                    海塘级别: properties.data.seawallLevel,
                    "海塘长度(m)": properties.data.seawallLength,
                    防潮标准: properties.data.designTideStandard + "年",
                  };
                }
                break;
              case "泵站":
                {
                  obj = {
                    名称: properties.data.pustName,
                    泵站规模: properties.data.engScal,
                    "装机流量(m³/s)": properties.data.insFlow,
                  };
                }
                break;
              case "水闸":
                {
                  obj = {
                    名称: properties.data.wagaName,
                    水闸规模: properties.data.engScal,
                    "最大过闸流量(m³/s)": properties.data.desLockDisc,
                  };
                }
                break;
              case "闸站":
                {
                  obj = {
                    名称: properties.data.gateName,
                    闸站规模: properties.data.engScal,
                    "过闸流量(m³/s)": properties.data.maxFlow,
                  };
                }
                break;
              case "水电站":
                {
                  obj = {
                    名称: properties.data.hystName,
                    水电站规模: properties.data.engScal,
                    "总装机容量(kW)": properties.data.totInsCap,
                  };
                }
                break;
              case "防汛仓库":
                {
                  obj = {
                    名称: properties.data.name,
                    物资总价值: properties.data.materialPrice
                      ? properties.data.materialPrice + "万元"
                      : "-",
                    联系人: properties.data.chargePerson,
                    联系方式: properties.data.chargePhone,
                  };
                }
                break;
              case "救援队伍":
                {
                  obj = {
                    名称: properties.data.name,
                    救援队伍人数: properties.data.existingNumber
                      ? properties.data.existingNumber + "人"
                      : "-",
                    联系人: properties.data.chargePerson,
                    联系方式: properties.data.chargePhone,
                  };
                }
                break;
                case "避灾点":
                {
                  obj = {
                    名称: properties.data.pointName,
                    可容纳人数: properties.data.pointCapacity
                      ? properties.data.pointCapacity + "人"
                      : "-",
                    避灾点地址: properties.data.pointAddress,
                  };
                }
                break;
              default: {
                obj = {
                  名称:
                    properties.data.resName ||
                    properties.data.mnpdName ||
                    properties.data.dikeName ||
                    properties.data.seawallName ||
                    properties.data.projectName ||
                    properties.data.pustName ||
                    properties.data.wagaName ||
                    properties.data.gateName ||
                    properties.data.irrName ||
                    properties.data.cwsName ||
                    properties.data.hystName,
                };
              }
            }
            this.featureInfo = obj;
          }
          //设置overlay的显示位置
          if (this.overlay) this.overlay.setPosition(coodinate);
          flag = true;
        } else {
          const data = feature.getProperties().data;
          let obj;
          if (data && data.type == "海塘") {
            obj = {
              名称: data.name,
              ...data.props,
            };
          } else if (data && data.type == "生产建设项目") {
            obj = {
              项目名称: data.name,
            };
          } else {
            return;
          }
          this.featureInfo = obj;
          //设置overlay的显示位置
          if (this.overlay) this.overlay.setPosition(coodinate);
          flag = true;
        }
      });
      if (flag) {
        //显示overlay
        this.map.addOverlay(this.overlay);
      } else {
        this.map.removeOverlay(this.overlay);
      }
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        let pixel = this.map.getEventPixel(evt.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        //是否存在坐标
        if (feature == undefined) {
          this.closeOverlay();
        }
      });
      this.map.on("click", (evt) => {
        console.log('1111111111111')
        this.searchStatus ? this.getInScope() : "";
      });
      this.map.on("moveend", (event) => {
        window.mapZoom = this.map.getView().getZoom();
      });
    },
    // 关闭地图弹窗
    closeOverlay() {
      //   this.overlay_rain.setPosition(undefined);
      //   this.overlay_water.setPosition(undefined);
    },
    zoomChange(type) {
      let valueZoom = this.map.getView().getZoom();
      if (type == "add") {
        valueZoom++;
        this.map.getView().setZoom(valueZoom);
      } else if (type == "reduce") {
        valueZoom--;
        this.map.getView().setZoom(valueZoom);
      } else if (type == "home") {
        this.map.getView().setZoom(11.5);
        this.map.getView().setCenter([mapConfig.lng, mapConfig.lat]);
      }
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
    //清理所有图层
    cleanAllLayer() { },
    clearIsohyetLayer() {
      if (this.isohyetLayer) {
        this.map.removeLayer(this.isohyetLayer);
      }
    },
    //多点位时悬浮窗
    addClusterPointOverLay() {
      this.clusterPointOverLay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector("#clusterPointPopup"),
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
      });
      this.map.addOverlay(this.clusterPointOverLay);
      document
        .getElementById("clusterPointPopup")
        .addEventListener("pointermove", (e) => {
          this.clusterPointPopupShow = true;
          e.stopPropagation();
        });
    },
    // 显示遥感影像
    showSpecklesLayer(year) {
      const sourceMap = {
        2018: new XYZ({
          url: "http://117.149.227.112:6080/arcgis/rest/services/mapservice/WZ_YX2018/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        2019: new XYZ({
          url: "http://117.149.227.112:6080/arcgis/rest/services/mapservice/WZ_YX2019/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        2020: new XYZ({
          url: "http://117.149.227.112:6080/arcgis/rest/services/mapservice/WZ_YX2020/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
      };
      this.map.getLayers().forEach((item) => {
        if (item.get("id") === "Speckles") {
          if (year) {
            item.setVisible(1);
            item.setSource(sourceMap[year]);
          } else item.setVisible(0);
        }
      });
    },
    // 显示扰动图斑
    showPatternLayer(year) {
      this.map.getLayers().forEach((element) => {
        let layerId = element.get("id");
        if (
          ["pattern2019", "pattern2018", "pattern2020"].indexOf(layerId) != -1
        ) {
          //获取扰动图斑图层年份
          let layerNum = layerId.replace(/[^0-9]/gi, "");
          if (year && year == layerNum) {
            element.setVisible(1);
          } else {
            element.setVisible(0);
          }
        }
      });
    },
    /**
     * 画聚合点位并将图层点位绑定事件
     * @param {Array} coordinate 点位列表
     * @param {Object} styleObj 样式对象
     * @param {String} layerName 点位图层名称，用于分辨
     */
    drawClusterPoint(coordinates, styleObj, layerName = "pointLayer") {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let pointLayer = drawClusterPoint(coordinates, styleObj);
      pointLayer.set("name", layerName);
      pointLayer.setZIndex(1001);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);
      selectClick.on("select", (e) => {
        let features = e.target.getFeatures().getArray()[0].values_.features;
        //点击层级放大 点位散开
        if (features.length > 1) {
          const extent = boundingExtent(
            features.map((r) => r.getGeometry().getCoordinates())
          );
          this.map
            .getView()
            .fit(extent, { duration: 1000, padding: [100, 100, 100, 100] });
        }
        // console.log(features,8999);
        if (features.length == 1) {
          const projectTypeArr = [
            "水库",
            "山塘",
            "泵站",
            "海塘",
            "堤防",
            "水闸",
            "闸站",
            "灌区",
            "农饮水",
            "水电站",
          ];
          if (projectTypeArr.includes(features[0].values_.data.type)) {
            this.$emit(
              "showProjectDialog",
              features[0].values_.data,
              features[0].values_
            );
          } else if (features[0].values_.data.type == "规划") {
            this.$emit("programmeShowDialog", features[0].values_.data);
          } else if (
            features[0].values_.data.type == "码头" ||
            features[0].values_.data.type == "桥梁" ||
            features[0].values_.data.type == "海塘道路交叉出入口" ||
            features[0].values_.data.type == "避灾点" ||
            features[0].values_.data.type == "旱闸" ||
            features[0].values_.data.type == "箱涵" ||
            features[0].values_.data.type == "断面" ||
            features[0].values_.data.type == "测站平台" ||
            features[0].values_.data.type == "规模以下水闸" ||
            features[0].values_.data.type == "管理房"
          ) {
            this.$emit("showJcBuildingDialog", features[0].values_.data);
          } else if (features[0].values_.data.type == "桩号") {
            this.$emit("showZhuanghaoDialog", features[0].values_.data);
          } else {
            this.$emit("showDialog", features[0].values_.data);
          }
          // this.$emit("point-click", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    /**
     * 移除指定图层
     * @param {String} layerName 指定的图层名
     */
    removeLayerByName(layerName) {
      this.map.getLayers().forEach((element) => {
        if (element && element.get("name") == layerName) {
          this.map.removeLayer(element);
        }
      });
    },
    /**
     * 检验指定图层是否存在
     */
    isLayerExist(layerName) {
      let flag = false;
      this.map.getLayers().forEach((element) => {
        if (element && element.get("name") == layerName) {
          flag = true;
        }
      });
      return flag;
    },
    //加载等雨量面
    showIsohyetLayer(mapImage) {
      this.clearIsohyetLayer();
      this.isohyetLayer = new ImageLayer({
        source: new ImageStatic({
          url: mapImage.href, //这里添加静态图片的地址
          projection: "EPSG:4326",
          imageExtent: [
            mapImage.extent.xmin,
            mapImage.extent.ymin,
            mapImage.extent.xmax,
            mapImage.extent.ymax,
          ], //WGS-84 经纬度数据范围
        }),
        zIndex: 10,
        opacity: 0.5,
      });
      this.map.addLayer(this.isohyetLayer);
    },
    riverLayerColorSet(layerName) {
      let layerColor = "rgb(255,203,56)";
      switch (layerName) {
        case "人工水道临水线点图层":
          layerColor = "rgb(255,203,56)";
          break;
        case "人工水道临水线图层":
          layerColor = "rgb(255,203,56)";
          break;
        case "人工水道中心线图层":
          layerColor = "rgb(255,56,56)";
          break;
        case "人工水道水面线图层":
          layerColor = "rgb(9,188,254)";
          break;
        case "人工水道管理范围线图层":
          layerColor = "rgb(71,31,162)";
          break;
        case "人工水道水域面图层":
          layerColor = "rgb(255,203,56)";
          break;

        case "山塘临水线点图层":
          layerColor = "rgb(0,173,0)";
          break;
        case "山塘临水线图层":
          layerColor = "rgb(0,173,0)";
          break;
        case "山塘管理范围线图层":
          layerColor = "rgb(71,31,162)";
          break;
        case "山塘水面线图层":
          layerColor = "rgb(9,188,254)";
          break;
        case "山塘水域面图层":
          layerColor = "rgb(0,173,0)";
          break;

        case "其他水域临水线点图层":
          layerColor = "rgb(117,179,255)";
          break;
        case "其他水域临水线图层":
          layerColor = "rgb(117,179,255)";
          break;
        case "其他水域管理范围线图层":
          layerColor = "rgb(71,31,162)";
          break;
        case "其他水域水面线图层":
          layerColor = "rgb(9,188,254)";
          break;
        case "其他水域水域面图层":
          layerColor = "rgb(117,179,255)";
          break;

        case "水库临水线点图层":
          layerColor = "rgb(20,138,255)";
          break;
        case "水库临水线图层":
          layerColor = "rgb(20,138,255)";
          break;
        case "水库管理范围线图层":
          layerColor = "rgb(71,31,162)";
          break;
        case "水库水面线图层":
          layerColor = "rgb(9,188,254)";
          break;
        case "水库水域面图层":
          layerColor = "rgb(20,138,255)";
          break;

        case "河道临水线点图层":
          layerColor = "rgb(21,238,241)";
          break;
        case "河道临水线图层":
          layerColor = "rgb(21,238,241)";
          break;
        case "河道管理范围线图层":
          layerColor = "rgb(71,31,162)";
          break;
        case "河道中心线图层":
          layerColor = "rgb(255,56,56)";
          break;
        case "河道水面线图层":
          layerColor = "rgb(9,188,254)";
          break;
        case "暗河图层":
          layerColor = "rgb(255,126,56)";
          break;
        case "其他沟渠图层":
          layerColor = "rgb(155,255,67)";
          break;
        case "河道水域面图层":
          layerColor = "rgb(21,238,241)";
          break;

        case "水域断面点图层":
          layerColor = "rgb(129,80,255)";
          break;
        case "水域断面线图层":
          layerColor = "rgb(129,80,255)";
          break;
      }
      return layerColor;
    },
    //分批次请求GIS服务器，加载海量矢量要素数据
    lodeRiverFeatureLayers(data) {
      // console.log(this.$microStore,222);
      //获取图层名称，设置颜色值
      let layerName = data.label;
      let layerColor = this.riverLayerColorSet(layerName);
      //点线面样式设置
      let layerDataSet = data.id.split("_")[0];
      let esrijsonFormat = new EsriJSON();
      let styleCache = {
        Point: new Style({
          image: new Circle({
            radius: 3,
            fill: new Fill({
              color: layerColor,
            }),
          }),
        }),
        LineString: new Style({
          stroke: new Stroke({
            color: layerColor,
            width: 0.4,
          }),
        }),
        Polygon: new Style({
          fill: new Fill({
            color: layerColor,
          }),
        }),
      };
      //图层请求，优化矢量加载
      let vectorSource = new VectorSource({
        loader: (extent, resolution, projection) => {
          let url =
            riverLayesBaseService +
            layerDataSet +
            "/MapServer/" +
            data.index +
            "/query/?f=json&" +
            "returnGeometry=true&spatialRel=esriSpatialRelIntersects&geometry=" +
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
            "&geometryType=esriGeometryEnvelope&inSR=4326&outFields=*" +
            "&outSR=4326";

          //微应用模式子应用不支持ajax的jsonp格式请求 调用主应用的方法解决window隔离
          if (window.__POWERED_BY_QIANKUN__) {
            url += "&callback=jsonCallBack";
            this.$microStore.dispatch("Set_Json_CallBack", {
              callback: (response) => {
                if (response.error) {
                  
                } else {
                  // 读取结果
                  var features = esrijsonFormat.readFeatures(response, {
                    featureProjection: projection,
                  });
                  if (features.length > 0) {
                    vectorSource.addFeatures(features);
                  }
                }
              },
            });
            const JSONP = document.createElement("script");
            JSONP.type = "text/javascript";
            JSONP.src = url;
            document.getElementsByTagName("head")[0].appendChild(JSONP);
            setTimeout(() => {
              document.getElementsByTagName("head")[0].removeChild(JSONP);
            }, 500);
          } else {
            $.ajax({
              url: url,
              dataType: "jsonp",
              success: function (response) {
                if (response.error) {
                  
                } else {
                  // 读取结果
                  var features = esrijsonFormat.readFeatures(response, {
                    featureProjection: projection,
                  });
                  if (features.length > 0) {
                    vectorSource.addFeatures(features);
                  }
                }
              },
            });
          }
        },
        strategy: tileStrategy(
          createXYZ({
            tileSize: 512,
          })
        ),
      });
      // 矢量图层
      let layerVariable = new VectorLayer({
        source: vectorSource,
        style: function (feature) {
          let layerType = feature.getGeometry().getType();
          if (layerType.indexOf("LineString") != -1) layerType = "LineString";
          if (layerType.indexOf("Polygon") != -1) layerType = "Polygon";
          return styleCache[layerType];
        },
        id: data.id,
      });
      this.map.addLayer(layerVariable);
      return layerVariable;
    },
    clusterPointClick(item) {
      const projectTypeArr = [
        "水库",
        "山塘",
        "泵站",
        "海塘",
        "堤防",
        "海塘",
        "水闸",
        "闸站",
        "灌区",
        "农饮水",
        "水电站",
      ];
      if (projectTypeArr.includes(item.data.type)) {
        this.$emit(
          "showProjectDialog",
          item.data,
          item
        );
      } else if (item.data.type == "规划") {
        this.$emit("programmeShowDialog", item.data);
      } else if (
        item.data.type == "码头" ||
        item.data.type == "桥梁" ||
        item.data.type == "海塘道路交叉出入口" ||
        item.data.type == "箱涵" ||
        item.data.type == "断面" ||
        item.data.type == "管理房" ||
        item.data.type == "测站平台" ||
        item.data.type == "规模以下水闸" ||
        item.data.type == "旱闸"
      ) {
        this.$emit("showJcBuildingDialog", item.data);
      } else if (item.data.type == "桩号") {
        this.$emit("showZhuanghaoDialog", item.data);
      } else {
        this.$emit("showDialog", item.data);
      }
    },
    //    规划  --- 展示点位方法
    _drawPoint(coordinates, style, layerName) {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      const styleObj = {
        anchor: [0, 0],
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: require("@/assets/images/map-intake-normal.png"), //图片路径
        title: "",
        scale: 1,
      };
      let pointLayer = drawPoint(coordinates, styleObj);
      pointLayer.set("name", layerName);
      pointLayer.setZIndex(999);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);
      selectClick.on("select", (e) => {
        let features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          this.$emit("programmeShowDialog", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    //展示线方法
    _drawLine(arr, lineType) {
      this.flyPoint([arr.coordinates[0], arr.coordinates[1]], 16);
    },
    //展示点方法(用这个)
    _drawPoints(list, style, layerName, noDialog) {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      const styleObj = {
        anchor: [0, 0],
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: require("@/assets/images/reservoir.png"), //图片路径
        title: "",
        scale: 1,
      };
      const pointLayer = drawPoint(list, styleObj);
      pointLayer.set("name", layerName);
      this.map.addLayer(pointLayer);
      const selectClick = addLayerClick(pointLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          if (list.length && list[0].type == "生产建设项目") {
            _this.$emit("showProductionProjDialog", features[0].values_.data);
            return;
          }
          _this.$emit("programmeShowDialog", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
      if (list.length <= 0) return;
      if (list[0].type == "生产建设项目") return;
      this.flyPoint([list[0].lng, list[0].lat], 8);
    },
    // 规划 ---- 展示线方法
    _drawLines(arr, style, layerName) {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let tempLayer = drawLines(arr);
      tempLayer.set("name", layerName);
      tempLayer.setZIndex(999);
      this.map.addLayer(tempLayer);
      const selectClick = addLayerClick(tempLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("programmeShowDialog", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    //加载水情点位
    showWaterPoint(data, type) {
      if (this.layerObj[type]) {
        this.map.removeLayer(this.layerObj[type]);
        this.layerObj[type] = null;
      }
      this.layerObj[type] = drawSkWaterPoint(data, type);
      this.map.addLayer(this.layerObj[type]);
      const selectClick = addLayerClick(this.layerObj[type]);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("showDialog", features[0].values_.featureProperties.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    // 加载河湖线段
    _riversLrawLines(arr, style, layerName) {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let tempLayer = drawLines(arr);
      tempLayer.set("name", layerName);
      tempLayer.setZIndex(999);
      this.map.addLayer(tempLayer);
      const selectClick = addLayerClick(tempLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          if (features[0].values_.data.type == "规划") {
            _this.$emit("programmeShowDialog", features[0].values_.data);
          } else {
            _this.$emit("showRiversDialog", features[0].values_.data);
          }
        }
      });
      this.map.addInteraction(selectClick);
    },
    //加载河湖信息点位
    /**
     * 画聚合点位并将图层点位绑定事件
     * @param {Array} coordinate 点位列表
     * @param {Object} styleObj 样式对象
     * @param {String} layerName 点位图层名称，用于分辨
     */
    _drawClusterPoint(coordinates, styleObj, layerName = "pointLayer") {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
      let pointLayer = drawClusterPoint(coordinates, styleObj);
      pointLayer.set("name", layerName);
      pointLayer.setZIndex(1001);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);
      selectClick.on("select", (e) => {
        let features = e.target.getFeatures().getArray()[0].values_.features;
        //点击层级放大 点位散开
        if (features.length > 1) {
          const extent = boundingExtent(
            features.map((r) => r.getGeometry().getCoordinates())
          );
          this.map
            .getView()
            .fit(extent, { duration: 1000, padding: [100, 100, 100, 100] });
        }
        if (features.length == 1) {
          if (features[0].values_.data.type == "取水户") {
            this.$emit("showWaterIntakeDialog", features[0].values_.data);
          } else {
            this.$emit("showRiversDialog", features[0].values_.data);
          }
        }
      });
      this.map.addInteraction(selectClick);
    },
    //加载视频点位
    showVideoPoint(data, type) {
      if (this.layerObj[type]) {
        this.map.removeLayer(this.layerObj[type]);
        this.layerObj[type] = null;
      }
      this.layerObj[type] = drawVideoPoint(data, type);
      this.map.addLayer(this.layerObj[type]);
      const selectClick = addLayerClick(this.layerObj[type]);
      const _this = this;
      selectClick.on("select", function (e) {
        e.stopPropagation();
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("showDialog", features[0].values_.featureProperties.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    //加载风场浪场图层
    addWatherLayer(datas, layerType) {
      const view = this.map.getView();
      view.setZoom(6.5);
      let colorScale = [
        "rgb(36,104, 180)",
        "rgb(60,157, 194)",
        "rgb(128,205,193 )",
        "rgb(151,218,168 )",
        "rgb(198,231,181)",
        "rgb(238,247,217)",
        "rgb(255,238,159)",
        "rgb(252,217,125)",
        "rgb(255,182,100)",
        "rgb(252,150,75)",
        "rgb(250,112,52)",
        "rgb(245,64,32)",
        "rgb(237,45,28)",
        "rgb(220,24,32)",
        "rgb(180,0,35)",
      ];
      switch (layerType) {
        case "wind":
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
              generateParticleOption: false,
            },
            zIndex: 100,
          });
          this.$nextTick(() => {
            this.map.addLayer(this.windLayers);
          });
          break;
        case "wave":
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
              generateParticleOption: false,
            },
            zIndex: 101,
          });
          this.$nextTick(() => {
            this.map.addLayer(this.waveLayers);
          });
          break;
      }
      //移动事件
      this.map.on("movestart", (e) => {
        if (this.windLayers) {
          this.windLayers.setVisible(false);
        }
        if (this.waveLayers) {
          this.waveLayers.setVisible(false);
          return;
        }
      });
      this.map.on("moveend", (e) => {
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
      switch (layerType) {
        case "wind":
          if (this.windLayers != null) this.map.removeLayer(this.windLayers);
          this.windLayers = null;
          break;
        case "wave":
          if (this.waveLayers != null) this.map.removeLayer(this.waveLayers);
          this.waveLayers = null;
          break;
      }
    },
    // showCameraPoint(data,type)
    //加载雨情点位
    showRainPoint(data, type) {
      if (this.layerObj[type]) {
        this.map.removeLayer(this.layerObj[type]);
        this.layerObj[type] = null;
      }
      this.layerObj[type] = drawSkRainPoint(data, type);
      this.map.addLayer(this.layerObj[type]);
      const selectClick = addLayerClick(this.layerObj[type]);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("showDialog", features[0].values_.featureProperties.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    /**
     * @description:加载工程点位
     * @param {*} data
     * @param {*} type
     * @param {*} flag 是否非十大工程类型
     * @return {*}
     */
    showProjectPoint(data, type, flag) {
      if (this.layerObj[type]) {
        this.map.removeLayer(this.layerObj[type]);
        this.layerObj[type] = null;
      }
      this.layerObj[type] = drawProjectPoint(data, type);
      this.map.addLayer(this.layerObj[type]);
      const selectClick = addLayerClick(this.layerObj[type]);
      const _this = this;
      selectClick.on("select", function (e) {
        e.stopPropagation();
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          if (!flag) {
            _this.$emit(
              "showProjectDialog",
              features[0].values_.featureProperties.data,
              features[0].values_.featureProperties
            );
          } else {
            _this.$emit(
              "showRescueDialog",
              features[0].values_.featureProperties.data,
              features[0].values_.featureProperties
            );
          }
        }
      });
      this.map.addInteraction(selectClick);
    },
    clearLayerByName(layerName) {
      this.map.removeLayer(this.layerObj[layerName]);
      this.layerObj[layerName] = null;
    },
    // 测面传值
    measure(command) {
      if (command == "none") {
        this.clearSearchData([], "none");
      } else {
        this.searchStatus = true;
        if (this.projectInScope && this.projectInScope.length > 0) {
          this.searchStatus = false;
          this.$emit("showInScopeDialog", [], "setVisible");
          return;
        }
        if (command == "area") {
          Measure.measure(this.map, "area");
        } else if (command == "Box") {
          Measure.measure(this.map, "Box");
        } else if (command == "Circle") {
          Measure.measure(this.map, "Circle");
        }
      }
    },
    resetScopeArr() {
      this.projectInScope = [];
    },
    // 获取测面
    getInScope() {
      this.projectInScope = []; // 坐标数组
      let val = this.map.getLayers().getArray()[
        this.map.getLayers().getArray().length - 1
      ].values_.source.uidIndex_;
      val =
        typeof val[Object.keys(val)[0]] == "object"
          ? val[Object.keys(val)[0]].values_.geometry.flatCoordinates
          : "";

      if (val && val.length > 0) {
        let longitude = [],
          latitude = [];
        val.slice(0, val.length - 2).forEach((item, index) => {
          if (index % 2 == 0) {
            longitude.push(item);
          } else {
            latitude.push(item);
          }
        });
        longitude.forEach((item, index) => {
          this.projectInScope.push({
            longitude: item,
            latitude: latitude[index],
          });
        });
        if (this.searchStatus) {
          this.getList(this.projectInScope);
        }
      }
    },
    // 获取树结构列表&列表树
    async getList(val) {
      await mapManageApi.getProjectInScope(val).then((res) => {
        this.$emit("showInScopeDialog", res.data, "setData");
        this.searchStatus = false;
      });
    },
    // 测面查询内容移除
    clearSearchData(val, type) {
      if (val) {
        val.map((item) => {
          this.scopeLayer.push(item);
        });
        this.scopeLayer = [...new Set(this.scopeLayer)];
      }
      this.clearScopeLayer();
      type == "none" ? (this.projectInScope = []) : "";
      this.searchStatus = false;
    },
    clearScopeLayer() {
      if (this.scopeLayer) {
        this.scopeLayer.map((item) => {
          this.clearLayerByName(item);
        });
      }
    },
    //展示台风
    showTyphoon(data) {
      const typhoon = new newTyphoon(this.map, data, this.typhoonsMap, this);
      typhoon.init();
    },
    //根据code清除台风
    clearTyphoon(code) {
      const layer = this.typhoonsMap.get(code + "layer");
      const interval = this.typhoonsMap.get(code + "interval");
      this.map.removeLayer(layer);
      clearInterval(interval);
      const nameOverLay = this.typhoonsMap.get(code + "name");
      this.map.removeOverlay(nameOverLay);
    },
    //清除全部台风
    clearAllTyphoon() {
      for (let item in this.typhoonsMap.values_) {
        if (item.indexOf("layer") != -1) {
          this.map.removeLayer(this.typhoonsMap.values_[item]);
        } else if (item.indexOf("name") != -1) {
          const nameOverLay = this.typhoonsMap.get(item);
          this.map.removeOverlay(nameOverLay);
        } else if (item.indexOf("interval") != -1) {
          clearInterval(item);
        }
      }
    },
    addTyphoonPointOverlay() {
      this.pointOverlay = new Overlay({
        element: this.$refs.typhoonPopup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
      });
      this.map.addOverlay(this.pointOverlay);
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
    clearAreaLayer() {
      if (this.areaLayer != null) {
        this.map.removeLayer(this.areaLayer);
        this.areaLayer = null;
      }
    },
    clearCityLayer() {
      if (this.areaLayer != null) {
        for (let i = 0; i < this.areaArray.length; i++) {
          this.map.removeLayer(this.areaArray[i]);
          this.areaArray[i] = null;
        }
        this.areaArray = this.areaArray.filter((item) => item && item.trim());
      }
      this.fullMap();
    },
    flyPoint(center, zoom) {
      const view = this.map.getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom,
      });
    },
    //根据行政区划名称查询行政区划边界
    showAreaLine(areaName) {
      this.clearAreaLayer();
      const searchAreaList = [
        "鹿城区",
        "龙湾区",
        "瓯海区",
        "洞头区",
        "永嘉县",
        "平阳县",
        "苍南县",
        "文成县",
        "泰顺县",
        "瑞安市",
        "乐清市",
      ];
      //对应searchAreaList 每个行政区划的定位中心点
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
      ];
      const areaIndex = searchAreaList.indexOf(areaName);
      if (areaIndex != -1) {
        const searchUrl =
          "http://112.17.127.75:8008/arcgis/rest/services/%e6%b8%a9%e5%b7%9e%e5%8e%bf%e7%95%8c/MapServer/0/query";
        const opt = {
          where: "NAME='" + areaName + "'",
          inSR: 4490,
          outFields: "*",
          outSR: 4490,
          f: "pjson",
        };
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {});
          this.map.addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 10);
      } else {
        const searchUrl =
          mapConfig.mapServer +
          "/mapservice/wenzhou_xiangzhen/MapServer/2/query";
        const opt = {
          where: "NAME='" + areaName + "'",
          inSR: 4490,
          outFields: "*",
          outSR: 4490,
          f: "pjson",
        };
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {
            fillColor: "rgba(91,169,252, 0.6)",
            lineColor: "rgba(91,169,252, 0.6)",
          });
          this.map.addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 9.5);
      }
    },
    //高亮显示主城区划
    showCityAreaLine() {
      const searchUrl =
        mapConfig.mapServer + "/wenzhou/wzmcls/MapServer/2/query";
      const opt = {
        where: "objectid<=4",
        inSR: 4490,
        outFields: "*",
        outSR: 4490,
        f: "pjson",
      };
      let geojson = {};
      queryMapserver(searchUrl, opt).then((res) => {
        let response = res.features;
        for (let i = 0; i < response.length; i++) {
          const coordinates = response[i].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {});
          this.areaArray.push(this.areaLayer);
          this.map.addLayer(this.areaLayer);
        }
        this.map
          .getView()
          .fit(this.areaArray[2].getSource().getFeatures()[0].getGeometry(), {
            padding: [100, 300, 0, 200],
          });
      });
    },
    fullMap() {
      let view = this.map.getView();
      view.animate({
        center: [mapConfig.lng, mapConfig.lat],
        duration: 300,
        zoom: mapConfig.zoom,
      });
    },
    clearBasinLayer() {
      if (this.basinLayer != null) {
        this.map.removeLayer(this.basinLayer);
        this.basinLayer = null;
      }
    },
    //根据流域名称查询流域边界
    basinCommand(basinName) {
      if (basinName != "全流域") {
        queryMapserver("/温州流域/MapServer/0/query", {
          where: "WRNM='" + basinName + "'",
          f: "json",
        }).then((res) => {
          if (res.features && res.features.length > 0) {
            esri2geo(res, (err, data) => {
              //加载温州流域图层
              let vectorSource = new VectorSource({
                features: new GeoJSON().readFeatures(data),
              });
              this.clearBasinLayer();
              this.basinLayer = new VectorLayer({
                source: vectorSource,
                zIndex: 10,
                style: new Style({
                  stroke: new Stroke({
                    color: "#FF00E7",
                    lineDash: [4],
                    width: 2,
                  }),
                  fill: new Fill({
                    color: "rgba(0, 0, 255, 0.1)",
                  }),
                }),
              });
              const basicListObj = {
                飞云江流域: [120.15090829961352, 27.825079402365983],
                鳌江流域: [120.36649479044917, 27.5721330146127],
                瓯江流域: [120.65211311365364, 28.258244725697626],
              };
              this.map.addLayer(this.basinLayer);
              this.flyPoint(basicListObj[basinName], 10);
            });
          }
        });
      } else {
        this.clearBasinLayer();
        this.resetMap();
      }
    },
    //重置map
    resetMap() {
      this.flyPoint([mapConfig.lng, mapConfig.lat], mapConfig.zoom);
    },
    //根据距离画线
    _drawLineInMeter(obj, radius) {
      this.clearLineLayer();
      this.linelLineLayer = drawLineInMeter(obj, radius);
      this.map.addLayer(this.linelLineLayer);
      let index = parseInt(obj.length / 2 - 1);
      setTimeout(() => {
        this.flyPoint(obj[index], 13);
      }, 301);
    },
    clearLineLayer() {
      this.map.removeLayer(this.linelLineLayer);
      this.linelLineLayer = null;
      this.resetMap();
    },
    //根据距离画圈
    _drawCircleInMeter(center, radius) {
      this.clearCirlceLayer();
      this.circleLayer = drawCircleInMeter(center, radius, this.map);
      this.map.addLayer(this.circleLayer);
      this.flyPoint(center, 14);
    },
    clearCirlceLayer() {
      this.map.removeLayer(this.circleLayer);
      this.circleLayer = null;
      this.flyPoint([mapConfig.lng, mapConfig.lat], mapConfig.zoom);
    },
    _showTestPoint(data) {
      this.clearTextLayer();
      this.textLayer = showTestPoint(data);
      this.map.addLayer(this.textLayer);
    },
    clearTextLayer() {
      if (this.textLayer != null) {
        this.map.removeLayer(this.textLayer);
        this.textLayer = null;
      }
    },
    /**
     * 通用使用数据中的 coorpot 属性画线
     * @param {{coorpot:string}[]} dataList
     * @param {string} type
     */
    drawCoorpotLine(dataList, type) {
      for (let data of dataList) {
        const style = { lineWidth: 4, lineColor: "#e80000" };

        const converter = document.createElement("div");
        converter.innerHTML = !!data.coorpot ? data.coorpot : "[]";
        const lineString = decodeURIComponent(converter.innerText)
          .replaceAll(/\\/g, "")
          .replaceAll(/^"(.+)"$/g, "$1");
        let lineList = eval(lineString);

        lineList = lineList.map((line) => {
          return {
            lineList: line.map((item) => [item.lng, item.lat]),
            data: {
              type,
            },
          };
        });
        this.drawLineEx(lineList, style);
      }
    },
    handleSeawallChange(seawallList) {
      for (let seawall of seawallList) {
        let color = "#E80000";
        if (seawall.designTideStandard < 10) {
          color = "#E80000";
        } else if (seawall.designTideStandard == 10) {
          color = "#DD00DB";
        } else if (seawall.designTideStandard == 20) {
          color = "#E89100";
        } else if (seawall.designTideStandard == 50) {
          color = "#EEC80B";
        } else if (seawall.designTideStandard == 100) {
          color = "#19AF47";
        } else if (seawall.designTideStandard > 100) {
          color = "#4CEBC7";
        }
        let style = {
          lineWidth: 4,
          lineColor: color,
        };
        let lineString =
          (seawall.coorpot && seawall.coorpot.replaceAll("&quot;", "'")) ||
          "[]";
        let linelist = lineString && eval(lineString);
        linelist = linelist.map((line) => {
          line = line.map((item, index) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              type: "海塘",
              name: seawall.seawallName,
              props: {
                海塘长度: seawall.seawallLength + "m",
                防潮标准: seawall.designTideStandard + "年",
                所在位置: seawall.village,
              },
              ...seawall,
            },
          };
        });
        this.drawLineEx(linelist, style);
      }
    },
    removeLineLayer() {
      this.lineLayer.forEach((item) => {
        this.map.removeLayer(item);
      });
    },
    drawLineEx(coordinates, styleObj = {}) {
      if (!coordinates.length > 0) return;
      let layer = drawLineEx(coordinates, styleObj);
      this.lineLayer.push(layer);
      layer.set("name", "lineLayer");
      layer.setZIndex(10);
      this.map.addLayer(layer);
      const selectClick = addLayerClick(layer);
      selectClick.on("select", (e) => {
        var features = e.target.getFeatures().getArray();
        // console.log(features,555);
        if (features.length > 0) {
          // console.log(features[0].values_)
          this.$emit(
            "showProjectDialog",
            features[0].values_.data,
            features[0].values_.data
          );
        }
      });
      this.map.addInteraction(selectClick);
    },
    loadRiverLine(type) {
      this.map.getLayers().forEach((element) => {
        if (element.get("id") == "KG_bulueLineLayerId") {
          if (type) {
            element.setVisible(1);
          } else {
            element.setVisible(0);
          }
        }
      });
    },
    clearAnalysisPointsLayer() {
      if (this.analysisPointsLayer != null)
        this.map.removeLayer(this.analysisPointsLayer);
    },
    showAnalysisPoints(data) {
      this.clearAnalysisPointsLayer();
      const projectTypeArr = [
        "水库",
        "山塘",
        "堤防",
        "海塘",
        "泵站",
        "水闸",
        "闸站",
        "灌区",
        "农饮水",
        "水电站",
      ];
      const stationTypeArr = [
        "水库水位",
        "河道水位",
        "堰闸水位",
        "潮位",
        "断面",
        "闸门开度",
        "水质监测",
        "积水",
        "视频",
      ];
      const rainTypeArr = ["雨情"];
      const imgObj = {
        水库: require("@/assets/images/map_legends/map-reservoir.png"),
        山塘: require("@/assets/images/map_legends/map-pool.png"),
        堤防: require("@/assets/images/legend-dike-normal.png"),
        海塘: require("@/assets/images/legend-seawall-normal.png"),
        泵站: require("@/assets/images/map_legends/map-pump.png"),
        水闸: require("@/assets/images/map_legends/map-sluice.png"),
        闸站: require("@/assets/images/map_legends/map-sluicestation.png"),
        水电站: require("@/assets/images/map_legends/map-hydropower.png"),
        农饮水: require("@/assets/images/map_legends/map-farmwater.png"),
        灌区: require("@/assets/images/map_legends/map-irrigated.png"),
        水库水位: require("@/assets/images/map_legends/map-reservoirwater.png"),
        河道水位: require("@/assets/images/map_legends/map-riverwater.png"),
        堰闸水位: require("@/assets/images/map_legends/map-checkgate.png"),
        潮位: require("@/assets/images/map_legends/map-sealevel.png"),
        闸门开度: require("@/assets/images/map_legends/map-sluicework.png"),
        水质监测: require("@/assets/images/map_legends/map-waterquality.png"),
        断面: require("@/assets/images/map_legends/map-waterquality.png"),
        积水: require("@/assets/images/map_legends/map-hydrops.png"),
        视频: require("@/assets/images/map_legends/map-video.png"),
      };
      data.map((item) => {
        if (item.analysisType == "雨情") {
          let src = "";
          if (item.rainfall <= 0) {
            src = require("@/assets/images/legend/rain_0.png");
          } else if (item.rainfall > 0 && item.rainfall < 10) {
            src = require("@/assets/images/legend/rain_10.png");
          } else if (item.rainfall >= 10 && item.rainfall < 25) {
            src = require("@/assets/images/legend/rain_10_25.png");
          } else if (item.rainfall >= 25 && item.rainfall < 50) {
            src = require("@/assets/images/legend/rain_25_50.png");
          } else if (item.rainfall >= 50 && item.rainfall < 100) {
            src = require("@/assets/images/legend/rain_50_100.png");
          } else if (item.rainfall >= 100 && item.rainfall < 250) {
            src = require("@/assets/images/legend/rain_100_250.png");
          } else {
            src = require("@/assets/images/legend/rain_250.png");
          }
          item.src = src;
        } else {
          item.src = imgObj[item.analysisType];
        }
      });
      this.analysisPointsLayer = drawAnaylysiscetPoint(data);
      this.map.addLayer(this.analysisPointsLayer);
      const selectClick = addLayerClick(this.analysisPointsLayer);
      selectClick.on("select", (e) => {
        const features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          const featureProperties =
            features[0].getProperties().featureProperties;
          const type = featureProperties.type;
          if (projectTypeArr.includes(type)) {
            this.$emit(
              "showProjectDialog",
              featureProperties.data,
              featureProperties
            );
          } else if (
            stationTypeArr.includes(type) ||
            rainTypeArr.includes(type)
          ) {
            this.$emit("showDialog", featureProperties.data);
          }
        }
      });
      this.map.addInteraction(selectClick);
    },
    //卫星云图
    loadCloundImg(imgSrc) {
      this.removeLayerByName("cloundLayer");
      this.cloundLayer = new ImageLayer({
        name: "cloundLayer",
        source: new ImageStatic({
          url: imgSrc, //这里添加静态图片的地址
          projection: "EPSG:4326",
          imageExtent: [80.0, 0, 180.0, 60.0], //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10,
      });
      this.map.addLayer(this.cloundLayer);
    },
    loadRadarRainImg(imgSrc) {
      this.removeLayerByName("radarRainLayer");
      this.radarRainLayer = new ImageLayer({
        name: "radarRainLayer",
        source: new ImageStatic({
          url: imgSrc, //这里添加静态图片的地址
          projection: "EPSG:4326",
          imageExtent: [
            78.4564707118729, 17.192519812642708, 144.74218778452877,
            57.09042776801506,
          ], //WGS-84 经纬度数据范围
        }),
        opacity: 0.5,
        zIndex: 10,
      });
      this.map.addLayer(this.radarRainLayer);
    },
    printMap() {
      printJS({
        printable: "mainView",
        type: "html",
        targetStyles: ["*"],
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  .map-operate {
    width: auto;
    height: 40px;
    line-height: 40px;
    position: absolute;
    left: 470px;
    bottom: 40px;
    color: #fff;
    z-index: 1900;
  }
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
      background: url(~assets/images/bigScreen-mini-tab-warn.png) no-repeat;
      background-size: 100% 100%;
    }
  }
  .map {
    width: 100%;
    height: 100%;
    ::v-deep .ol-layer {
      // canvas{
      //   width:100%;
      // }
    }
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
        background: url("~assets/images/popup-bg2.png") no-repeat left top;
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
      background: url("~assets/images/popup-project-bg.png") no-repeat center;
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
      background: url("~assets/images/popup-rain-bg.png") no-repeat center;
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
        background: url(~assets/images/bigScreen-mini-tab.png) no-repeat;
        background-size: 100% 100%;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
        &:last-child {
          margin-right: 0;
        }
        &.active {
          background: url(~assets/images/bigScreen-mini-tab-check.png) no-repeat;
          color: #a6dff6;
          background-size: 100% 100%;
        }
      }
    }
  }
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

// 点位提示框
// .ol-popup {
//   position: absolute;
//   padding: 8px 15px;
//   border-radius: 10px;
//   bottom: 12px;
//   left: -50px;
//   font: italic bold 12px;
//   background-color: rgba(0, 0, 0, 0.8);
//   border: 1px solid #48fff4;
//   color: #48fff4;
//   .popup-name {
//     font-size: 20px;
//     line-height: 34px;
//   }
// }
// .ol-popup:after,
// .ol-popup:before {
//   top: 100%;
//   border: solid transparent;
//   content: " ";
//   height: 0;
//   width: 0;
//   position: absolute;
//   pointer-events: none;
// }
// .ol-popup:after {
//   border-top-color: #3ab378;
//   border-width: 10px;
//   left: 48px;
//   margin-left: -10px;
// }
// .ol-popup:before {
//   border-top-color: #3ab378;
//   border-width: 11px;
//   left: 48px;
//   margin-left: -11px;
// }
// #popup-content {
//   display: inline-block;
//   width: 180px;
//   line-height: 26px;
// }

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
      content: "";
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
      content: "";
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
}
</style>
