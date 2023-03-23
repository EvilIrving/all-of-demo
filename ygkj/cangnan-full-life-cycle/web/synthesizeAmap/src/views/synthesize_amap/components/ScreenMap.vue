<template>
  <div class="map-wrap">
    <div class="map" id="mainView"></div>
    <!-- <screen-footer :map-bind="map" @measure="measure" /> -->

    <div id="popup" class="ol-popup">
      <div :class="['ol-popup__wrapper']">
        <div class="ol-popup__title">{{ featureInfo.name }}</div>
        <div class="ol-popup__content">
          <div class="ol-popup__content--props">
            <div :key="key" v-for="(prop, key) of featureInfo" class="props-item">
              <span class="item-label">{{ key }}</span>
              <span
                class="item-data"
                :style="{ color: key == '汛限水位' ? '#eec80b' : '#1cfff5' }"
              >
                {{ prop == 'null' ? '-' : prop == 'null m' ? '- m' : prop }}
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
import { mapManageApi } from "@/api/mapManage";
import Measure from "@/utils/measure";
const riverLayesBaseService =
  "http://117.149.227.112:6080/arcgis/rest/services/waterSearch/";
import {
  drawSkWaterPoint,
  addLayerClick,
  drawProjectPoint,
  drawSkRainPoint,
  Typhoon,
  drawPolygon,
  drawCircleInMeter,
  showTestPoint,
  drawLineEx,
  drawVideoPoint,
} from "@/utils/mapUtil.js";
import { queryMapserver } from "@/api/loadMapServe";
import mapConfig from "@/config/mapConfig";
export default {
  name: "ScreenMap",
  components: { },
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
      areaArray: [],
      textLayer: null,
      lineLayer: [],
      analysisPointsLayer: [],
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
      //地图填充图层
      let SLTCLayer = new TileLayer({
        id: "SLTCLayer",
        source: new XYZ({
          url: "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/wenzhouTC/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        visible: true,
      });
      let mainLayer = new TileLayer(
        {
          id: "zhejiangmainSLLayer",
          source: new XYZ({
            url: "https://sldtptgis.zjwater.com/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
            projection: "EPSG:4326",
          }),
        },
        { zoomOffset: 1 }
      );
      let SLMCLayer = new TileLayer({
        id: "zhejiangmainSLMCLayer",
        zIndex: 1,
        source: new XYZ({
          url: "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/newwzmcls/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
      });
      // view设置
      let viewSetting = new View({
        projection: "EPSG:4326",
        center: [120.527, 27.86],
        zoom: 9.5,
        maxZoom: 18,
        minZoom: 8,
        extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: "mainView",
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [SLTCLayer, mainLayer, SLMCLayer],
        view: viewSetting,
      });
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
      this.addStationOverlay(); //加载水利监控图层的悬浮框
      // 地图鼠标移动事件
      this.map.on("pointermove", this._showFeatureInfo);
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
      this.map.getTargetElement().style.cursor = "auto";
      this.map.forEachFeatureAtPixel(pixel, (feature) => {
        let coodinate = e.coordinate;
        let properties;
        // console.log(feature.getProperties())
        // console.log(feature.getProperties(),998)
        if (feature && feature.getProperties().featureProperties) {
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
                  // console.log(properties,999)
                  obj = {
                    名称: properties.data.stationName,
                    降雨量:
                      (properties.data.drp ) + "mm",
                  };
                }
                break;
              case "国控断面":
              case "省控断面":
              case "水文站水质":
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
              case '水库':{
                obj = {
                  "名称":properties.data.resName,
                  "实时水位":properties.data.waterLevel + 'm',
                  "汛限水位":properties.data.currentLimitLevel+ 'm',
                  "更新时间":properties.data.dataTime,
                  "鉴定结论":properties.data.safetyConclusion
                }
              }break;
              case '山塘':{
                obj = {
                  "名称":properties.data.mnpdName,
                  "山塘类型":properties.data.mnpdCate,
                  "总容积(万m³)":properties.data.mnpdCate,
                }
              }break;
              case '堤防':{
                obj = {
                  "名称":properties.data.dikeName,
                  "堤防类别":properties.data.dikeType,
                  "堤防长度(m)":properties.data.dikeLen
                }
              }break;
              case '海塘':{
                obj = {
                  "名称":properties.data.seawallName,
                  "海塘级别":properties.data.seawallLevel,
                  "海塘长度(m)":properties.data.reachStandardLength
                }
              }break;
              case '泵站':{
                obj = {
                  "名称":properties.data.pustName,
                  '泵站规模':properties.data.engScal,
                  '装机流量(m³/s)':properties.data.insFlow
                }
              }break;
              case '水闸':{
                obj = {
                  "名称":properties.data.wagaName,
                  '水闸规模':properties.data.engScal,
                  '最大过闸流量(m³/s)':properties.data.desLockDisc
                }
              }break;
              case '闸站':{
                obj = {
                  "名称":properties.data.gateName,
                  '闸站规模':properties.data.engScal,
                  '过闸流量(m³/s)':properties.data.maxFlow
                }
              }break;
              case '水电站':{
                obj = {
                  "名称":properties.data.hystName,
                  '水电站规模':properties.data.engScal,
                  '总装机容量(kW)':properties.data.totInsCap
                }
              }break;
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
        } else {
          const data = feature.getProperties().data;
          // console.log(data,126);
          let obj;
          if(data.type == '海塘'){
            obj = {
              '名称':data.name,
              ...data.props
            };
          }else{
            return;
          }
          this.featureInfo = obj;
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
      this.map.on("singleclick", (evt) => {
        console.log(evt);
        let pixel = this.map.getEventPixel(evt.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        // console.log(feature);
        //是否存在坐标
        if (feature == undefined) {
          this.closeOverlay();
        } else {
          // let lng = feature.values_.featureProperties.lng;
          // let lat = feature.values_.featureProperties.lat;
          // this.closeOverlay();
          // if (lng && lat) {
          //   this.popupData = feature.values_.featureProperties.data;
          //   const { flag } = feature.values_.featureProperties.data;
          //   if (flag == "rain") {
          //     //获取图表数据
          //     const { stcd } = feature.values_.featureProperties.data;
          //     this.stcd = stcd;
          //     this.stLng = lng;
          //     this.stLat = lat;
          //   }
          // }
        }
      });
      this.map.on("click", (evt) => {
        this.searchStatus ? this.getInScope() : "";
      });
    },
    // 关闭地图弹窗
    closeOverlay() {
      //   this.overlay_rain.setPosition(undefined);
      //   this.overlay_water.setPosition(undefined);
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
    cleanAllLayer() {},
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
        case "人工水道临水线点":
          layerColor = "rgb(255,203,56)";
          break;
        case "人工水道临水线":
          layerColor = "rgb(255,203,56)";
          break;
        case "人工水道中心线":
          layerColor = "rgb(255,56,56)";
          break;
        case "人工水道水面线":
          layerColor = "rgb(9,188,254)";
          break;
        case "人工水道管理范围线":
          layerColor = "rgb(71,31,162)";
          break;
        case "人工水道水域面":
          layerColor = "rgb(255,203,56)";
          break;

        case "山塘临水线点":
          layerColor = "rgb(0,173,0)";
          break;
        case "山塘临水线":
          layerColor = "rgb(0,173,0)";
          break;
        case "山塘管理范围线":
          layerColor = "rgb(71,31,162)";
          break;
        case "山塘水面线":
          layerColor = "rgb(9,188,254)";
          break;
        case "山塘水域面":
          layerColor = "rgb(0,173,0)";
          break;

        case "其他水域临水线点":
          layerColor = "rgb(117,179,255)";
          break;
        case "其他水域临水线":
          layerColor = "rgb(117,179,255)";
          break;
        case "其他水域管理范围线":
          layerColor = "rgb(71,31,162)";
          break;
        case "其他水域水面线":
          layerColor = "rgb(9,188,254)";
          break;
        case "其他水域水域面":
          layerColor = "rgb(117,179,255)";
          break;

        case "水库临水线点":
          layerColor = "rgb(20,138,255)";
          break;
        case "水库临水线":
          layerColor = "rgb(20,138,255)";
          break;
        case "水库管理范围线":
          layerColor = "rgb(71,31,162)";
          break;
        case "水库水面线":
          layerColor = "rgb(9,188,254)";
          break;
        case "水库水域面":
          layerColor = "rgb(20,138,255)";
          break;

        case "河道临水线点":
          layerColor = "rgb(21,238,241)";
          break;
        case "河道临水线":
          layerColor = "rgb(21,238,241)";
          break;
        case "河道管理范围线":
          layerColor = "rgb(71,31,162)";
          break;
        case "河道中心线":
          layerColor = "rgb(255,56,56)";
          break;
        case "河道水面线":
          layerColor = "rgb(9,188,254)";
          break;
        case "暗河":
          layerColor = "rgb(255,126,56)";
          break;
        case "其他沟渠":
          layerColor = "rgb(155,255,67)";
          break;
        case "河道水域面":
          layerColor = "rgb(21,238,241)";
          break;

        case "水域断面点":
          layerColor = "rgb(129,80,255)";
          break;
        case "水域断面线":
          layerColor = "rgb(129,80,255)";
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
        loader: function (extent, resolution, projection) {
          var url =
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

          $.ajax({
            url: url,
            dataType: "jsonp",
            success: function (response) {
              if (response.error) {
                console.log(
                  response.error.message +
                    "\n" +
                    response.error.details.join("\n")
                );
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
          // console.log(features[0].values_)
          _this.$emit("showDialog", features[0].values_.featureProperties.data);
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
          // console.log(features[0].values_)
          _this.$emit("showDialog", features[0].values_.featureProperties.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    // showCameraPoint(data,type)
    //加载雨情点位
    showRainPoint(data, type) {
      console.log(data,222);
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
          // console.log(features[0].values_)
          _this.$emit("showDialog", features[0].values_.featureProperties.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    //加载工程点位
    showProjectPoint(data, type) {
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
          // console.log(features[0])
          _this.$emit(
            "showProjectDialog",
            features[0].values_.featureProperties.data,
            features[0].values_.featureProperties
          );
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
      console.log("getInScope", val, val[Object.keys(val)[0]]);
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
      this.clearTyphoonLayer();
      const typhoon = new Typhoon(
        this.map,
        data,
        this.typhoonLayers,
        this.typhoonNames,
        this.typhoonTimes,
        this
      );
      typhoon.init();
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
          "http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_xiangzhen/MapServer/2/query";
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
        "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/wzmcls/MapServer/2/query";
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
    },
    //重置map
    resetMap() {
      this.flyPoint([mapConfig.lng, mapConfig.lat], mapConfig.zoom);
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
    handleSeawallChange(seawallList) {
      // console.log(seawallList,333)
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
        // console.log(linelist,555)
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
                防潮标准: seawall.designTideStandard + "m",
                所在位置: seawall.village,
              },
              ...seawall
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
      selectClick.on("select",(e) => {
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
    clearAnalysisPointsLayer() {
      if (this.analysisPointsLayer != null)
        this.map.removeLayer(this.analysisPointsLayer);
    },
    showAnalysisPoints(data, type) {
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
        "闸门开度",
        "水文站水质",
        "积水",
      ];
      const rainTypeArr = ["雨情"];
      const imgObj = {
        水库: require("@/assets/images/legend-large-normal.png"),
        山塘: require("@/assets/images/legend-pond-normal.png"),
        堤防: require("@/assets/images/legend-dike-normal.png"),
        海塘: require("@/assets/images/legend-seawall-normal.png"),
        泵站: require("@/assets/images/legend-pump-normal.png"),
        水闸: require("@/assets/images/map-sluice-normal.png"),
        闸站: require("@/assets/images/legend-sluice-normal.png"),
        水电站: require("@/assets/images/legend-hydropower-normal.png"),
        水库水位: require("@/assets/images/legend/zhengchangshuiku.png"),
        河道水位: require("@/assets/images/legend/legend-stagnant-normal.png"),
        堰闸水位: require("@/assets/images/legend/zhamen1.png"),
        潮位: require("@/assets/images/legend/choawei.png"),
        闸门开度: require("@/assets/images/legend/zhamen1.png"),
        水文站水质: require("@/assets/images/legend/jishuidian.png"),
        积水: require("@/assets/images/legend/jishuidian.png"),
      };
      if (projectTypeArr.indexOf(type) != -1) {
        // console.log('project');
        data.map((item) => {
          item.src = imgObj[type];
        });
        this.analysisPointsLayer = drawProjectPoint(data, type);
        this.map.addLayer(this.analysisPointsLayer);
        const selectClick = addLayerClick(this.analysisPointsLayer);
        const _this = this;
        selectClick.on("select", function (e) {
          var features = e.target.getFeatures().getArray();
          if (features.length > 0) {
            _this.$emit(
              "showProjectDialog",
              features[0].values_.featureProperties.data,
              features[0].values_.featureProperties
            );
          }
        });
        this.map.addInteraction(selectClick);
      } else if (stationTypeArr.indexOf(type) != -1) {
        // console.log('station');
        data.map((item) => {
          item.src = imgObj[type];
        });
        this.analysisPointsLayer = drawSkWaterPoint(data, type);
        this.map.addLayer(this.analysisPointsLayer);
        const selectClick = addLayerClick(this.analysisPointsLayer);
        const _this = this;
        selectClick.on("select", function (e) {
          var features = e.target.getFeatures().getArray();
          if (features.length > 0) {
            // console.log(features[0].values_)
            _this.$emit(
              "showDialog",
              features[0].values_.featureProperties.data
            );
          }
        });
        this.map.addInteraction(selectClick);
      } else if (rainTypeArr.indexOf(type) != -1) {
        // console.log('rain');
        data.map((item) => {
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
          return item;
        });
        this.analysisPointsLayer = drawSkRainPoint(data, type);
        this.map.addLayer(this.analysisPointsLayer);
        const selectClick = addLayerClick(this.analysisPointsLayer);
        const _this = this;
        selectClick.on("select", function (e) {
          var features = e.target.getFeatures().getArray();
          if (features.length > 0) {
            // console.log(features[0].values_)
            _this.$emit(
              "showDialog",
              features[0].values_.featureProperties.data
            );
          }
        });
        this.map.addInteraction(selectClick);
      }
    },
  },
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
      background: url(~assets/images/bigScreen-mini-tab-warn.png) no-repeat;
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
  }
</style>
