<!--
 * @Date: 2022-10-27 11:05:10
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-17 17:08:30
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawallScreen\seawallScreenMap\SeawallScreenMap.vue
-->
<template>
  <div class="map_wrap">
    <div ref="myModal" @click="showDropDir = false" :style="{ display: showDropDir ? 'block ' : 'none' }" class="modal"></div>
    <div class="map_back" v-show="showMapBack" @click="mapBack()">返回</div>
    <div class="map_switch">
      <div class="">
        <h2 :title="nowAdcdName " class="adnm_switch">
          {{ nowAdcdName}}
          <i v-show="showDropDir" @click.stop="showSwitchBtn(true)" class="dropdown cursor" key="dropdown"></i>
          <i v-show="!showDropDir" @click.stop="showSwitchBtn(false)" class="dropup cursor" key="dropup"></i>
          <i v-show="nowAdcdName !== userAdnm" @click="mapBack()" class="el-icon-close cursor" style="font-size: 16px; margin-left: 6px"></i>
        </h2>
        <ul class="switch_btn_box cursor" tabindex="1" v-show="showDropDir" @blur.stop="mouseleaveSwitch">
          <li @mouseover="(showList1 = true), (showList2 = false)" tabindex="2" @blur.stop="(showList1 = false), (showList2 = false)">
            行政图
            <!-- <i class="right_arrow cursor" /> -->
            <i class="el-icon-arrow-right cursor" />
            <div class="false_box">
              <el-cascader-panel class="adcd_box" v-model="cascaderChecked" v-show="showList1" ref="cascaderXZ" :options="adcdList" :props="{
                  multiple: true,
                  checkStrictly: true,
                  expandTrigger: 'click',
                  emitPath: false,
                }" clearable :show-all-levels="false" @change="changeCascaderXZ"></el-cascader-panel>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div class="map" id="seawallMap"></div>
    <Popups ref="popup" :pointData="pointData" />
    <div id="overly"></div>
    <!-- <div id="overlyOne"></div> -->
    <div ref="overlyXsOne">
      <div class="adcdOverlay">
        <div class="adcd_top">{{ xsPointData.name }}</div>
        <div class="adcd_bottom">
          {{ xsPointData.value ? xsPointData.value : 0 }}
          <span>亿m³</span>
        </div>
      </div>
    </div>
    <!-- <div v-if="showOneOverly" id="overlyOne"></div> -->
    <!-- 弹窗元素 -->
    <div class="popup_map_storm_tide" ref="popup_map_storm_tide" v-show="currentCoordinate && pointData && Object.keys(pointData).length !== 0">
      <div class="popup_content" v-show="pointData.rightType == '3'">
        <div class="popup_title" style="color: #fff; cursor: pointer" @click="lookDetail(pointData)">{{ pointData.NAME }}</div>
        <div class="popup_item">
          <p>安全状态：</p>
          <span>{{ pointData.status ? pointData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>设计潮位：</p>
          <span>
            {{
            pointData.DESIGN_TIDE_LEVEL
            ? pointData.DESIGN_TIDE_LEVEL + "m"
            : "-"
            }}
          </span>
        </div>
        <div class="popup_item">
          <p>设计标准：</p>
          <span>
            {{
            pointData.DESIGN_TIDE_STANDARD
            ? pointData.DESIGN_TIDE_STANDARD + "年"
            : "-"
            }}
          </span>
        </div>
        <div class="popup_item">
          <p>塘顶最低高程：</p>
          <span>
            {{
            pointData.LOWEST_ELEVATION ? pointData.LOWEST_ELEVATION + "m" : "-"
            }}
          </span>
        </div>
        <div class="popup_item">
          <p>最高潮位：</p>
          <span>
            {{
            pointData.forecast ? pointData.forecast.toFixed(2) + "m" : "-"
            }}
          </span>
        </div>
        <div class="popup_item">
          <p>出现时间：</p>
          <span>
            {{
            pointData.forecast_time ? pointData.forecast_time.slice(5, 16) : "-"
            }}
          </span>
        </div>
      </div>
    </div>
    <div class="popup_map_hover" ref="hoverPopup" v-show="currentCoordinateHover">
      <div class="popup_content">
        <div class="popup_title">
          {{
          popupDataHover.NAME ||
          popupDataHover.name ||
          popupDataHover.proName ||
          popupDataHover.prnm
          }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View, Feature } from "ol";
import XYZ from "ol/source/XYZ";
import Tile from "ol/layer/Tile";
import { Image } from "ol/layer";
import { ol } from "ol";
import ImageArcGISRest from "ol/source/ImageArcGISRest";
import { defaults as defaultControls } from "ol/control";
import Point from "ol/geom/Point";
import { LinearRing, Polygon } from "ol/geom";
import { fromExtent } from "ol/geom/Polygon";
import Overlay from "ol/Overlay";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import { Style, Fill, Stroke, Icon, Text, Circle } from "ol/style";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import { Vector as VectorSource, Cluster as ClusterSource } from "ol/source";
import { Draw, DoubleClickZoom, Select } from "ol/interaction";
import esri2geo from "esri2geo";
import Measure from "@/utils/measure";
import jquery from "@/plugins/jquery-1.11.3";
import { click, pointerMove } from "ol/events/condition";
import GeoJSON from "ol/format/GeoJSON";
import { getVectorContext } from "ol/render";
import axios from "axios";
import {
  skzgDrawPoint,
  drawPolygon,
  drawPoint,
  drawLines,
  drawGeoJson,
  drawYellowGeoJson,
  drawPolygonByFeature
} from "@/utils/mapUtil";
// import zjs_map from '@/utils/map_json.js'
import zjsGeoJson from "@/assets/data/zjsGeoJson.json";
import cutZjsGeoJson from "@/assets/data/cutZjsGeoJson.json";
import zjsXjGeoJson from "@/assets/data/zjsXjGeoJson.json";

import { adcdTree } from "@/api/api_seawall";
import { basinStatisticApi } from "@/api/api_basin";

//地图弹窗注册中心
import Popups from "../seawallScreenMap/popups/Popups";
import popupsList from "@/views/IntelligenceScreen/seawallScreen/seawallScreenMap/popups/popupsRegister";

import {
  nxStatisticApi,
  ggAreaStatisticApi,
  availableWaterSupply
} from "@/api/api_reservoir";

export default {
  name: "",
  components: {
    Popups
  },
  data() {
    return {
      tfname: "",
      tfdetail: "",
      hasTf: false,
      //地图
      serviceLink: "http://223.4.72.162:16080/arcgis/rest/services/",
      pointLayer: null,
      //图层集合
      pointLayers: {
        seawallPopup: null
      },
      markersLayer: {},
      markersOneLayer: {},
      linesLayer: {},
      jhPointLayer: {},
      seawallRedLayer: null,
      seawallGreenLayer: null,
      seawallYellowLayer: null, // 三色图层
      overCnt: {}, //风暴潮
      currentCoordinate: null,
      overlay: null,
      seawallGreenLayer: null,
      seawallYellowLayer: null, // 三色图层
      selectPointLayer: {},
      selectLineLayer: {},
      adcdPolygonLayer: {},
      map: null,
      overlaysArr: [],
      // defaultCenter: {
      //   center: [120.2841796875, 28.9],
      //   zoom: 7.9,
      // },
      defaultCenter: {},
      switchIndex: 0,
      switchBtns: ["行政图", "流域图"],
      pointData: {},
      adcdList: [],
      selectAdcd: "",
      basinList: [],
      selectBasin: "",
      showOneOverly: false,
      xsPointData: {},
      overlyXsOne: null,
      cityLines: [],
      zjsLineLayer: null,
      zjsXjLineLayer: null,
      cascaderCheckedName: "", // 筛选的类型 集合
      popupDataHover: {},
      hoverOverlay: null,
      currentCoordinateHover: null,
      showAdcdSelect: false,
      showBasinSelect: false,
      // bxskNum: {},
      // showClDetail: false,
      nowAdcdName: this.$localData("get", "userInfo").adnm,
      // 底图切割
      base: {},
      userAdcd: this.$localData("get", "userInfo").adcd,
      userAdnm: this.$localData("get", "userInfo").adnm,
      showDropDir: false, //显示上下拉箭头
      isShowSwitchBtn: false, //是否显示切换按钮
      //定一个状态 只允许请求一次 0：请求之前 1：请求结束
      apiOnce: true,
      cascaderChecked: "",
      showMapBack: false,
      showList1: false,
      showList2: false,
      isDisabled: false, //是否 禁用 行政区划和流域的点击
      clickBack: false, //是否 重置 行政区划和流域
      mapInitViewData: {
        浙江省: {
          center: [120.2841796875, 28.9],
          zoom: 7.9
        },
        杭州市: {
          center: [119.49926673638585, 29.95238969771562],
          zoom: 9.1
        },
        嘉兴市: {
          center: [120.82295965448235, 30.62365529556139],
          zoom: 10
        },
        舟山市: {
          center: [122.31747879977257, 30.15719831907083],
          zoom: 9.5
        },
        绍兴市: {
          center: [120.65203143078425, 29.779204859669218],
          zoom: 9.5
        },
        湖州市: {
          center: [119.86013581555973, 30.774957488035955],
          zoom: 10
        },
        丽水市: {
          center: [119.52993030133263, 28.058645185047814],
          zoom: 9.3
        },
        台州市: {
          center: [121.1019486426036, 28.620996798490223],
          zoom: 9.5
        },
        温州市: {
          center: [120.48230685780125, 27.850262573033703],
          zoom: 9.3
        },
        衢州市: {
          center: [118.6963592735759, 28.903052563843694],
          zoom: 9.6
        },
        金华市: {
          center: [119.96357972413958, 29.14920448445481],
          zoom: 9.5
        },
        宁波市: {
          center: [121.55592775905025, 29.707711898447568],
          zoom: 9.5
        }
      }
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin;
    },
    seawallSwitchXsl() {
      return this.$store.state.seawallSwitchXsl;
    },
    sgzgMapType() {
      return this.$store.state.sgzgMapType;
    }
  },
  watch: {
    seawallSwitchXsl(val) {
      if (!val) {
        this.overlyXsOne.setPosition(undefined);
        this.cleanXuPopups();
      }
    },
    cascaderChecked: {
      handler(val) {
        if (!val) {
          this.nowAdcdName = this.$localData("get", "userInfo").adnm;
          return this.$localData("get", "userInfo").adcd;
        }
      },
      deep: true,
      immediate: false
    }
  },
  created() {
    // 为不同角色设置地图视图
    for (let key in this.mapInitViewData) {
      if (key == this.userAdnm) {
        this.defaultCenter = this.mapInitViewData[key];
        break;
      } else {
        this.defaultCenter = this.mapInitViewData["浙江省"];
      }
    }
  },
  mounted() {
    this.getAdcdOptions();

    //初始化弹窗
    popupsList.map(v => {
      this.mapOverlay(v.ref);
      this.overlaysArr.push(this[v.ref]);
    });
    //初始化地图
    this.mapInit();
  },
  methods: {
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.openDetailPopup(row);
    },
    //初始化弹窗
    mapOverlay(popupRef) {
      this[popupRef] = new Overlay({
        element: this.$refs.popup.$refs[popupRef], // 弹窗标签，在html里
        id: "popupRef",
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          duration: 250
        },
        positioning: "bottom-center",
        offset: [-16, -10]
      });
    },

    //初始化地图
    mapInit() {
      this.overlay = new Overlay({
        element: this.$refs.popup_map_storm_tide, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250
        },
        positioning: "bottom-center",
        offset: [-16, -10]
      });
      this.overlyXsOne = new Overlay({
        element: this.$refs.overlyXsOne, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250
        },
        positioning: "bottom-center",
        offset: [-16, -10]
      });

      // hover弹窗
      this.hoverOverlay = new Overlay({
        element: this.$refs.hoverPopup, // 弹窗标签，在html里
        autoPan: false, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250
        },
        positioning: "bottom-center",
        offset: [-10, -4]
      });

      const base = new Tile({
        source: new XYZ({
          // url: "https://sk.zjwater.com/ygMpServer/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}", //本地
          url:
            "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt2/arcgis/rest/services/basemap/ZJRasterMap/MapServer/tile/{z}/{y}/{x}", // 线上
          projection: "EPSG:4326" //使用这个坐标系
          // crossOrigin: "anonymous",
        })
        // preload: 1,
        // visible: true,
      });

      const river = new Image({
        source: new ImageArcGISRest({
          url:
            "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/river1/1river/MapServer", // 线上
          projection: "EPSG:4326" //使用这个坐标系
        })
      });

      const clipLayer = new VectorLayer({
        style: null,
        source: new VectorSource({
          features: new GeoJSON().readFeatures(cutZjsGeoJson)
        })
      });

      // 地图
      this.map = new Map({
        target: "seawallMap",
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          // 自己服务
          base,
          // 所有流域
          new Image({
            source: new ImageArcGISRest({
              url:
                "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/Base/liuyumian/MapServer",
              projection: "EPSG:4326" //使用这个坐标系
            }),
            preload: 1,
            visible: false
          }),
          river,
          clipLayer
        ],
        overlays: [
          ...this.overlaysArr,
          this.overlyXsOne,
          this.hoverOverlay,
          this.overlay
        ], // 把弹窗加入地图
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.center,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0,
          //添加这个范围 经度-最小，纬度-最小，经度-最大，纬度-最大 左下右上
          extent: [117.2, 26, 124, 32]
        })
      });

      // 禁用地图双击缩放
      const dblClickInteraction = this.map
        .getInteractions()
        .getArray()
        .find(interaction => {
          return interaction instanceof DoubleClickZoom;
        });
      this.map.removeInteraction(dblClickInteraction);

      // 监听事件
      this.mapClick(); // 地图添加点击事件
      this.mapPointerMove(); // 地图鼠标移动事件

      // 监听缩放
      this.map.on("moveend", e => {
        var zoom = this.map.getView().getZoom(); //获取当前地图的缩放级别
        if (zoom >= 8) {
          //隐藏图例
          this.$store.commit("SET_SKZG_SWITCH_LEGEND", false);
          // this.getLinesXj(false)
        } else {
          //展示图例
          this.$store.commit("SET_SKZG_SWITCH_LEGEND", true);
          // this.getLinesXj(true)
        }
        if (zoom >= 8.5) {
          // this.getLinesXj(true)
          // this.getLines(false)
        } else {
          //展示图例
          // this.getLinesXj(false)
          // this.getLines(true)
        }
      });

      // 显示所有市行政区划边界线
      this.getLines(true);
      // 切割
      const style = new Style({
        fill: new Fill({
          color: "black"
        })
      });

      clipLayer.getSource().on("addfeature", function () {
        base.setExtent(clipLayer.getSource().getExtent());
      });

      base.on("postrender", function (e) {
        const vectorContext = getVectorContext(e);
        e.context.globalCompositeOperation = "destination-in";
        clipLayer.getSource().forEachFeature(function (feature) {
          vectorContext.drawFeature(feature, style);
        });
        e.context.globalCompositeOperation = "source-over";
      });
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", evt => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        let topNumber = 0.01;
        if (
          feature &&
          feature.values_.features &&
          feature.values_.features.length > 0
        ) {
          if (feature.values_.features.length == 1) {
            //点击单个图标时
            this.pointData = feature.values_.features[0].values_.data;
            this.closeAllOverly();
            // 调整弹窗位置

            if (this.pointData.rightType === "3") {
              const coordinate = evt.coordinate; // 获取坐标
              const hdms = toStringHDMS(
                toLonLat([this.pointData.lng, this.pointData.lat])
              ); // 转换坐标格式
              this.currentCoordinate = hdms; // 保存坐标点

              setTimeout(() => {
                this._setView(
                  Number(this.pointData.lng),
                  Number(this.pointData.lat) + topNumber
                );
                setTimeout(() => {
                  this.overlay.setPosition([
                    Number(this.pointData.lng),
                    Number(this.pointData.lat)
                  ]);
                  this.map.removeLayer(this.adcdMovePolygonLayer);
                }, 400);
              }, 250);
            }

            // 刷新数据
            if (this.pointData.mapType == "cxzyxPopup") {
              let obj = JSON.parse(JSON.stringify(this.pointData));
              this.pointData = {};
              this.pointData = obj;
            }

            if (this.pointData.mapType) {
              //打开弹窗
              setTimeout(() => {
                this._setView(
                  Number(this.pointData.lng),
                  Number(this.pointData.lat) + topNumber
                );
                setTimeout(() => {
                  this.openOverly(
                    this.pointData.mapType,
                    Number(this.pointData.lng),
                    Number(this.pointData.lat)
                  );
                  this.map.removeLayer(this.adcdMovePolygonLayer);
                }, 400);
              }, 250);
            }
          } else {
            // 点击聚合图标时
            this.map.getView().fit(feature.get("features")[0].getGeometry(), {
              maxZoom: this.map.getView().getZoom() + 1,
              duration: 250
            });
          }
        } else if (feature != undefined) {
          // 点击点位
          if (feature.values_.data) {
            this.closeAllOverly();
            this.pointData = feature.values_.data;
            // 调整弹窗位置
            if (
              this.pointData.mapType == "zdyhwtPopup" ||
              this.pointData.mapType == "cxjgPopup" ||
              this.pointData.mapType == "floodControlPopup"
            ) {
              topNumber = 0.012;
            }
            // 刷新数据
            if (this.pointData.mapType == "cxzyxPopup") {
              let obj = JSON.parse(JSON.stringify(this.pointData));
              this.pointData = {};
              this.pointData = obj;
            }

            if (this.pointData.mapType) {
              //打开弹窗
              setTimeout(() => {
                this._setView(
                  Number(this.pointData.lng),
                  Number(this.pointData.lat) + topNumber
                );
                setTimeout(() => {
                  this.openOverly(
                    this.pointData.mapType,
                    Number(this.pointData.lng),
                    Number(this.pointData.lat)
                  );
                  this.map.removeLayer(this.adcdMovePolygonLayer);
                }, 400);
              }, 250);
            }
          }
          // 点击面（市级）
          else if (feature.values_["市"]) {
            this.showMapBack = true;
            this.map.removeLayer(this.adcdMovePolygonLayer);
            // if(this.nowAdcdName != feature.values_['市']){
            this.nowAdcdName = feature.values_["市"];
            this.getAdcdPointsData(this.nowAdcdName);
            this.setCityView(this.nowAdcdName);
            this.getLines(false);
            this.getLinesXj(true);
            if (this.userAdcd.indexOf("0000000000") != -1) {
              this.$store.commit(
                "SET_SKZG_SELECT_ADCD",
                feature.values_["市代码"] + "000000"
              );
            }
            // }
          }
        }
      });
    },

    setCityView(cityName) {
      const view = this.map.getView();
      view.animate({
        center: this.mapInitViewData[cityName].center,
        duration: 200,
        zoom: this.mapInitViewData[cityName].zoom
      });
    },

    // 地图鼠标移动事件
    mapPointerMove() {
      // 鼠标移入
      this.map.on("pointermove", e => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        if (hit) {
          // 手指效果
          this.map.getTargetElement().style.cursor = "pointer";
          if (
            feature &&
            feature.values_.features &&
            feature.values_.features.length > 0
          ) {
            if (feature.values_.features.length == 1) {
              // popupDataHover = feature.values_.features[0].values_.data;
            } else {
              return false;
            }
          } else {
            if (feature.values_.data) {
              // 移入点
              this.popupDataHover = feature.values_.data;
              const hdms = toStringHDMS(
                toLonLat([this.popupDataHover.lng, this.popupDataHover.lat])
              ); // 转换坐标格式
              this.currentCoordinateHover = hdms; // 保存坐标点
              setTimeout(() => {
                this.hoverOverlay.setPosition([
                  this.popupDataHover.lng,
                  this.popupDataHover.lat
                ]);
              });
            } else {
              // 移入面
              this.closeHoverPopup();
              var zoom = this.map.getView().getZoom(); //获取当前地图的缩放级别
              if (this.zjsLineLayer != null && zoom < 13) {
                this.map.removeLayer(this.adcdMovePolygonLayer);
                const cityStyle = {
                  lineColor: "#2BEF89",
                  lineWidth: 3,
                  fillColor: "rgba(32,206,116,.5)"
                };
                this.adcdMovePolygonLayer = drawPolygonByFeature(
                  feature,
                  cityStyle
                );
                this.map.addLayer(this.adcdMovePolygonLayer);
              }
              return false;
            }
          }
        } else {
          this.map.removeLayer(this.adcdMovePolygonLayer);
          this.map.getTargetElement().style.cursor = "";
        }
      });
    },

    // 清空所有点位图层
    removeAllLayer() {
      this.map.removeLayer(this.pointLayers["seawallPopup"]);
      this.map.removeLayer(this.jhPointLayer);
      this.map.removeLayer(this.linesLayer);
      this.map.removeLayer(this.seawallYellowLayer);
      this.map.removeLayer(this.seawallGreenLayer);
      this.map.removeLayer(this.seawallRedLayer);
      this.pointLayers["seawallPopup"] = null;
      this.jhPointLayer = null;
      for (let key in this.overCnt) {
        this.map.removeLayer(this.overCnt[key]);
      }
    },

    removeOneLayer(layerName) {
      if (this.pointLayers[layerName] != null) {
        this.map.removeLayer(this.pointLayers[layerName]);
        this.pointLayers[layerName] = null;
      }
    },

    removePolygon() {
      this.map.removeLayer(this.adcdPolygonLayer);
      return true;
    },

    // 初始化视图
    initView() {
      this.getLines(true);
      this.getLinesXj(false);
      if (this.userAdcd.endsWith("0000000000")) {
        //省级用户清除单个市的范围线
        this.map.removeLayer(this.adcdPolygonLayer);
      }
      const view = this.map.getView();
      view.animate({
        center: this.defaultCenter.center,
        duration: 200,
        zoom: this.defaultCenter.zoom
      });
    },

    mapBack() {
      // 重置行政
      this.$refs.cascaderXZ.clearCheckedNodes();
      this.$refs.cascaderLY.clearCheckedNodes();
      this.showDropDir = false;

      // 清除流域图层
      let baseLayer = this.map.getLayers().item(1);
      this.map.removeLayer(this.basinLayer);
      baseLayer.setVisible(false);

      this.showMapBack = false;
      this.closeAllOverly();
      this.initView();
      this.nowAdcdName = this.$localData("get", "userInfo").adnm;
      this.$store.commit("SET_SKZG_SELECT_BASIN", "");
      this.$store.commit(
        "SET_SKZG_SELECT_ADCD",
        this.$localData("get", "userInfo").adcd
      );
    },

    // 设置地图位置
    _setView(lng, lat, noBig) {
      const to = [lng, lat];
      const view = this.map.getView();
      let obj = {
        center: to,
        duration: 0
      };
      if (!noBig) {
        obj.zoom = 15;
      }
      view.animate(obj);
    },
    // 设置地图位置
    _setHtView(lnglat, zoom) {
      let to = lnglat;
      let view = this.map.getView();
      view.animate({
        center: to,
        duration: 0,
        zoom: zoom || this.defaultCenter.zoom
      });
    },
    // 打点不聚合
    drawMapPoints(coordinates, layerName) {
      this.removeOneLayer(layerName);
      this.pointLayers[layerName] = skzgDrawPoint(coordinates);
      this.pointLayers[layerName].setZIndex(99);
      this.map.addLayer(this.pointLayers[layerName]);
    },

    // 获取全省市范围线
    getLines(flag) {
      this.map.removeLayer(this.zjsLineLayer);
      this.zjsLineLayer = null;
      if (flag) {
        let styleObj = {
          lineColor: "#FABD51",
          lineWidth: 2,
          fillColor: "rgba(6,63,178,.08)"
        };
        this.zjsLineLayer = drawGeoJson(zjsGeoJson, styleObj);
        this.map.addLayer(this.zjsLineLayer);

        this.getAdcdPointsData(this.$localData("get", "userInfo").adnm);

        //有了市范围线后，市县级用户改变视图
        if (this.userAdcd.endsWith("0000000000")) {
          //省级用户
        } else {
          this.getAdcdPointsData(this.$localData("get", "userInfo").adnm);
          this.getLines(false);
          this.getLinesXj(true);
        }
      }
    },
    // 获取全省县范围线
    getLinesXj(flag) {
      this.map.removeLayer(this.zjsXjLineLayer);
      this.zjsXjLineLayer = null;
      if (flag) {
        let styleObj = {
          lineColor: "#FABD51",
          lineWidth: 2,
          fillColor: "rgba(6,63,178,.08)"
        };
        this.zjsXjLineLayer = drawGeoJson(zjsXjGeoJson, styleObj);
        this.map.addLayer(this.zjsXjLineLayer);
      }
    },

    // 单个市高亮 其他区域添加蒙层
    drawMask(selectOneFeature) {
      // 创建矢量图层
      const maskVectorSource = new VectorSource({
        features: []
      });
      const maskVectorLayer = new VectorLayer({
        source: maskVectorSource,
        style: function () {
          let style = new Style({
            stroke: new Stroke({
              color: "#2BEF89",
              width: 3
            }),
            fill: new Fill({
              color: "rgba(0,9,32,.3)"
            })
          });
          return style;
        },
        zIndex: 2
      });
      maskVectorSource.clear(true);
      // if(maskVectorLayer) map.removeLayer(maskVectorLayer)

      //生成外环
      let outerGeomotryExtent = [-180, -90, 180, 90];
      let polygonRing = fromExtent(outerGeomotryExtent);

      //生成环状几何遮罩范围
      let selectFeature = selectOneFeature;
      let insideGeometry = selectFeature.getGeometry();
      if (!insideGeometry instanceof Polygon) {
        return false;
      }
      let insideCoords = insideGeometry.getCoordinates();
      insideCoords.forEach(el => {
        let linearRing = new LinearRing(el[0]);
        polygonRing.appendLinearRing(linearRing);
      });

      //添加圆环要素，添加到数据源
      let ringFeature = new Feature({
        geometry: polygonRing
      });
      maskVectorSource.addFeature(ringFeature);

      return maskVectorLayer;
    },

    // 获取单个市范围线
    getAdcdPointsData(adnm) {
      if (adnm == "浙江省") {
        // this.getLines(true)
        return;
      }
      const cityStyle = {
        lineColor: "#2BEF89",
        lineWidth: 3,
        fillColor: "rgba(0,0,0,0)"
      };
      let zjsCityFeatureList = this.zjsLineLayer.getSource().getFeatures();
      this.map.removeLayer(this.adcdPolygonLayer);
      for (let i = 0; i < zjsCityFeatureList.length; i++) {
        if (zjsCityFeatureList[i].values_["市"] == adnm) {
          // this.adcdPolygonLayer = drawPolygonByFeature(zjsCityFeatureList[i], cityStyle)
          // this.adcdPolygonLayer.setZIndex(1);
          // this.map.addLayer(this.adcdPolygonLayer)

          this.adcdPolygonLayer = this.drawMask(zjsCityFeatureList[i]);
          this.map.addLayer(this.adcdPolygonLayer);
        }
      }

      // let that = this;
      // jquery.ajax({
      //   url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/BOU/BOU_City/MapServer/0/query",
      //   type: "get",
      //   dataType: "jsonp",
      //   data: {
      //     returnGeometry: true,
      //     where: "NAME='" + adnm + "'",
      //     outSR: "4326",
      //     outFields: "*",
      //     f: "json",
      //   },
      //   success: function (res) {
      //     let arr = res.features[0].geometry.rings;
      //     that.adcdPolygonLayer = drawPolygon(arr);
      //     that.map.addLayer(that.adcdPolygonLayer);
      //   },
      // });
    },

    // 获取流域线数据
    async getBasinLineData(basinName) {
      this.isDisabled = true;
      this.map.removeLayer(this.basinLayer);
      if (basinName == "") {
        return;
      }
      let url =
        "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/Base/liuyumian/MapServer/0/query";
      jquery.ajax({
        url: url,
        type: "get",
        dataType: "jsonp",
        data: {
          returnGeometry: true,
          where: "ennm='" + basinName + "'",
          outSR: "4326",
          outFields: "*",
          f: "json"
        },
        success: res => {
          this.isDisabled = false;
          let arr = [];
          if (res.features && res.features.length > 0) {
            esri2geo(res, (err, data) => {
              this.basinLayer = drawGeoJson(data);
              this.map.addLayer(this.basinLayer);
            });
          }
        }
      });
    },

    // 生成单个气泡
    _drawOneOverlays(pointData) {
      this.xsPointData = pointData;
      this.openOverly(
        "overlyXsOne",
        Number(pointData.lng),
        Number(pointData.lat)
      );
    },

    // 生成多个气泡
    _drawXuOverlays(pointData) {
      for (var i = 0; i < pointData.length; i++) {
        //新增放置overly的div
        jquery("#overly").after(
          `<div id='overly${i}' >
            <div class='adcdOverlay'>
              <div class="adcd_top" mapType='${pointData[i].mapType}' lng='${pointData[i].lng
          }' adcd='${pointData[i].ADCD}' adnm='${pointData[i].ADNM}' lat='${pointData[i].lat
          }'>
                ${pointData[i].name} 
              </div>
              <div class="adcd_bottom">
                ${pointData[i].value ? pointData[i].value : 0}<span>${pointData[i].unit
          }</span>
              </div>
            </div>
          </div>`
        );
        //新增overly
        this.markersLayer["overly" + i] = new Overlay({
          offset: [-16, -10],
          positioning: "bottom-center",
          position: [pointData[i].lng, pointData[i].lat],
          element: document.getElementById("overly" + i)
        });
        this.map.addOverlay(this.markersLayer["overly" + i]);
        document
          .getElementById("overly" + i)
          .addEventListener("click", event => {
            this._drawXuShuiOverlays(
              event.target.getAttribute("mapType"),
              Number(event.target.getAttribute("lng")),
              Number(event.target.getAttribute("lat")),
              event.target.getAttribute("adnm"),
              event.target.getAttribute("adcd")
            );
          });
      }
    },

    // 打开弹窗
    openOverly(popupRef, lng, lat) {
      this[popupRef].setPosition([lng, lat]);
    },

    // 关闭弹窗
    closeOverly(popupRef) {
      this[popupRef].setPosition(undefined);
    },

    // 弹窗内-关闭所有弹窗
    closeAllOverly() {
      this.initView();
      this.$store.commit("SET_SKZG_SWITCH_LEGEND", true);
      popupsList.map(v => {
        this[v.ref] != null && this[v.ref].setPosition(undefined);
      });
      this.currentCoordinate = null;
    },

    // 清除蓄水量浮窗
    cleanXuPopups() {
      for (let key in this.markersLayer) {
        this.map.removeOverlay(this.markersLayer[key]);
      }
    },

    // 关闭hover弹窗
    closeHoverPopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.hoverOverlay.setPosition(undefined);
      this.currentCoordinateHover = null;
    },

    // 地图筛选
    async getAdcdOptions() {
      this.isDisabled = true;
      let res = await adcdTree({
        adcd: this.userAdcd,
        projectCategory: "海塘",
        fetchAll: true
      });
      if (res.success) {
        this.isDisabled = false;
        let adcdAllName = "";
        if (this.userAdcd.endsWith("0000000000")) {
          adcdAllName = "浙江省";
        } else if (this.userAdcd.endsWith("00000000")) {
          adcdAllName = "全市";
        } else {
          adcdAllName = "全区/县";
        }
        let temp = [
          {
            label: adcdAllName,
            value: this.userAdcd,
            checked: true
          }
        ];

        //  const result =  this.filterTree(res.adcdTree.children,['江干区'])
        //  console.log(result);
        for (let i = 0; i < res.adcdTree.children.length; i++) {
          if (res.adcdTree.children[i].label == "江干区") {
            res.adcdTree.children.splice(i, 1);
          }
          for (let j = 0; j < res.adcdTree.children[i].children.length; j++) {
            if (res.adcdTree.children[i].children[j].label == "江干区") {
              res.adcdTree.children[i].children.splice(j, 1);
            }
          }
        }
        this.adcdList = [...temp, ...res.adcdTree.children];
      }
    },
    filterTree(tree = [], map = [], arr = []) {
      if (!tree.length) return [];
      for (let item of tree) {
        if (map.includes(item.label)) continue;
        let node = undefined;
        if (Array.isArray(item.children)) {
          // children是数组 将其存入arr,
          node = { ...item, children: [] };
        } else {
          // 不是数组，返回item
          node = { ...item };
        }
        arr.push(node);
        // 递归遍历数据
        if (item.children && item.children.length)
          filterTree(item.children, map, node.children);
      }
      return arr;
    },
    async getBasinOptions() {
      let res = await basinStatisticApi({
        statistic: "basin",
        adcd: this.userAdcd
      });
      if (res.success) {
        res.data.map((v, i) => {
          this.basinList.push({
            label: v.basinName,
            basinCode: v.basinCode,
            value: i + 1
          });
        });
        this.basinList[5].checked = true;
        let basinAllName = "";
        if (this.userAdcd.endsWith("0000000000")) {
          basinAllName = "浙江省";
        } else if (this.userAdcd.endsWith("00000000")) {
          basinAllName = "全市";
        } else {
          basinAllName = "全区/县";
        }
        this.basinList.unshift({
          label: basinAllName,
          basinCode: "",
          value: 0
        });

        this.adcdList = [...temp, ...res.adcdTree.children];
      }
    },

    mouseleaveSwitch() {
      if (!this.showList1 && !this.showList2) {
        this.showDropDir = false;
      }
    },

    // 改变行政区划事件
    changeCascaderXZ(itemVlaue) {
      let node = this.$refs.cascaderXZ.getCheckedNodes();
      let nameArray = [];
      for (let i = 0; i < node.length; i++) {
        const element = node[i];
        nameArray.push(element.label);
      }
      this.cascaderCheckedName = nameArray.join(",");
      this.cascaderChecked = itemVlaue.join(",");
      this.nowAdcdName =
        nameArray.length > 1 ? nameArray[0] + "..." : nameArray[0];
      this.changeAdcd(this.cascaderChecked, this.cascaderCheckedName);
    },
    changeAdcd(adcd) {
      if (adcd == "" && this.cascaderChecked == this.userAdcd) {
        this.removeAllLayer();
      }
      this.$store.commit("SET_SKZG_SELECT_BASIN", "");
      this.$store.commit("SET_SKZG_SELECT_ADCD", adcd);
      this.map.removeLayer(this.adcdPolygonLayer);
      // 清除流域图层
      let baseLayer = this.map.getLayers().item(1);
      this.map.removeLayer(this.basinLayer);
      baseLayer.setVisible(false);
      // 显示省行政区划边界线
      this.getLines(true);
    },

    /**
     * @function:
     * @description: 聚合打点
     * @param {*} coordinates Array 打点清单
     * @param {*} type String 点位图层名称
     * @return {*}
     */
    aggregationPointer(coordinates, type) {
      this.popupLists = coordinates;
      this[type] && this.map.removeLayer(this[type]);
      // this.removeAllLayer()
      let vectorSource = new VectorSource({});
      let clusterSource = new ClusterSource({
        distance: 80,
        source: vectorSource
      });
      for (let i = 0; i < coordinates.length; i++) {
        if (coordinates[i].lng && coordinates[i].lat) {
          const iconFeature = new Feature({
            geometry: new Point([
              Number(coordinates[i].lng),
              Number(coordinates[i].lat)
            ]),
            data: coordinates[i]
          });
          vectorSource.addFeature(iconFeature);
        }
      }

      var styleCache = {};
      this[type] = new VectorLayer({
        title: type,
        source: clusterSource,
        style: feature => {
          var size = feature.get("features").length;
          let src = null;
          if (size > 1) {
            let featureData = feature.get("features")[0].values_.data;
            switch (featureData.threeYards) {
              case "绿色":
                src = require("@/assets/images/map/green-seawall-jh1.png");
                break;
              case "红色":
                src = require("@/assets/images/map/red-seawall-jh1.png");
                break;
              case "黄色":
                src = require("@/assets/images/map/yellow-seawall-jh1.png");
                break;
              default:
                src = require("@/assets/images/map/green-seawall-jh1.png");
                break;
            }
            var style = new Style({
              image: new Icon({
                src: src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.4
              }),
              text: new Text({
                textAlign: "center",
                textBaseline: "middle",
                text: size.toString(),
                fill: new Fill({
                  color: "#fff"
                }),
                font: "normal 18px 微软雅黑",
                // x 增加 是往右  减小是 往左
                offsetX: -20,
                // y 减小 是往上  增加是 往下
                offsetY: 16,
                scale: 1
              })
            });
            styleCache[size] = style;
            return style;
          } else {
            let featureData = feature.get("features")[0].values_.data;
            let src = null;
            // this.map.getView().getZoom() >= 14
            switch (featureData.threeYards) {
              case "绿色":
                src = require("@/assets/images/map/green-seawall1.png");
                break;
              case "红色":
                src = require("@/assets/images/map/red-seawall1.png");
                break;
              case "黄色":
                src = require("@/assets/images/map/yellow-seawall1.png");
                break;
              default:
                src = require("@/assets/images/map/green-seawall1.png");
                break;
            }
            let style = new Style({
              image: new Icon({
                src: src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.4
              })
            });
            return style;
          }
        }
      });
      this[type].setZIndex(99);
      this.map.addLayer(this[type]);
      if (type == "ptSeawallPointLayer") {
        this.ptSeawallPointLayer.values_.title &&
          this.ptSeawallPointLayer.setVisible(false);
      }
    },
    // 画点 聚合
    newMapJh(coordinates, type, styleObj = {}) {
      let vectorSource = new VectorSource({});
      let clusterSource = new ClusterSource({
        distance: 80,
        source: vectorSource
      });
      for (let i = 0; i < coordinates.length; i++) {
        if (coordinates[i].lng && coordinates[i].lat) {
          const iconFeature = new Feature({
            geometry: new Point([
              Number(coordinates[i].lng),
              Number(coordinates[i].lat)
            ]),
            data: coordinates[i]
          });
          vectorSource.addFeature(iconFeature);
        }
      }
      let styleCache = {};
      const PointLayer = new VectorLayer({
        title: "",
        source: clusterSource,
        style: feature => {
          var size = feature.get("features").length;
          let src = null;
          if (size > 1) {
            let featureData = feature.get("features")[0].values_.data;
            switch (featureData.threeYards) {
              // case "绿色":
              //   src = require("@/assets/images/map/green-seawall-jh.png");
              //   break;
              // case "红色":
              //   src = require("@/assets/images/map/red-seawall-jh.png");
              //   break;
              // case "黄色":
              //   src = require("@/assets/images/map/yellow-seawall-jh.png");
              //   break;
              default:
                src = require("@/assets/images/map/yndq-jh.png");
                break;
            }
            var style = new Style({
              image: new Icon({
                src: styleObj.jhImg || src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.4
              }),
              text: new Text({
                textAlign: "center",
                textBaseline: "middle",
                font: "normal 16px 微软雅黑",
                text: size.toString(),
                fill: new Fill({
                  color: "#fff"
                }),
                // radius: 10,
                offsetX: -20,
                offsetY: 15,
                // backgroundStroke: new Stroke({
                //   color: "#fff",
                //   width: 1,
                // }),
                scale: 1
                // backgroundFill: new Fill({
                //   color: "#1F1F1F",
                // }),
                // padding: [6, 6, 3, 8],
              })
            });
            styleCache[size] = style;
            return style;
          } else {
            let featureData = feature.get("features")[0].values_.data;
            let src = null;
            // this.map.getView().getZoom() >= 14
            switch (featureData.threeYards) {
              // case "绿色":
              //   src = require("@/assets/images/map/green-seawall.png");
              //   break;
              // case "红色":
              //   src = require("@/assets/images/map/red-seawall.png");
              //   break;
              // case "黄色":
              //   src = require("@/assets/images/map/yellow-seawall.png");
              //   break;
              default:
                src = require("@/assets/images/map/yndq.png");
                break;
            }
            let style = new Style({
              image: new Icon({
                src: styleObj.img || src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.4
              })
            });
            return style;
          }
        }
      });
      if (type) {
        this.overCnt[type] = PointLayer;
        this.overCnt[type].setZIndex(99);
        this.map.addLayer(this.overCnt[type]);
      } else {
        this.jhPointLayer = PointLayer;
        this.jhPointLayer.setZIndex(99);
        this.map.addLayer(this.jhPointLayer);
      }
    },
    // 画点 不聚合
    newMapPoints(coordinates, type) {
      let scale = 0.8;
      this.removeAllLayer();
      // 点
      console.log(coordinates);
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/seawall-select.png"), //图片路径
        title: "",
        scale: scale
      };
      this[type] = drawPoint(coordinates, styleObj, true);
      this[type].setZIndex(99);
      this.map.addLayer(this[type]);
    },
    _drawLines(list) {
      this.map.removeLayer(this.linesLayer);
      for (let i = 0; i < list.length; i++) {
        list[i].coordinates = [];
        if (list[i].line && list[i].line.length) {
          for (let j = 0; j < list[i].line.length; j++) {
            list[i].coordinates.push([
              list[i].line[j].lng,
              list[i].line[j].lat
            ]);
          }
          if (
            ["seawallPopup", "firstLinePopup", "secondLinePopup"].includes(
              list[i].type
            )
          ) {
            switch (list[i].level) {
              case "红色":
                list[i].lineColor = "#e4443d";
                break;
              case "黄色":
                list[i].lineColor = "#ded052";
                break;
              case "绿色":
                list[i].lineColor = "#6eec61";
                break;
              case "1":
              case 1:
                list[i].lineColor = "#E02021";
                break;
              case "2":
              case 2:
                list[i].lineColor = "#EB7E63";
                break;
              case "3":
              case 3:
                list[i].lineColor = "#C447D6";
                break;
              case "4":
              case 4:
                list[i].lineColor = "#3154EB";
                break;
              case "5":
              case 5:
                list[i].lineColor = "#00FD6D";
                break;
              default:
                list[i].lineColor = "#00FD6D";
                break;
            }
          } else if (list[i].type === "zdyhwtPopup") {
            switch (list[i].level) {
              case "已处置":
                list[i].lineColor = "#1BC57E";
                break;
              case "处置中":
                list[i].lineColor = "#EB7E65";
                break;
              default:
                list[i].lineColor = "#549EFF";
                break;
            }
          }
        }
      }
      this.linesLayer = drawLines(list);
      this.map.addLayer(this.linesLayer);
    },
    showSwitchBtn(flag) {
      this.showDropDir = !flag;
      this.showAdcdSelect = false;
      this.showBasinSelect = false;
    }
  }
};
</script>

<style lang="scss" scoped>
.map_wrap {
  background: transparent;
  width: 100%;
  height: 100%;
  position: relative;
  .top_num {
    position: absolute;
    top: 45px;
    left: 0;
    // width: 100%;
    @include flexbox();
    @include flexAI(center);
    height: 54px;
    background: url($imageUrl + "num_bg.png") no-repeat center;
    background-size: cover;
    z-index: 99;
    h3 {
      @include flexbox();
      @include flexAI(center);
      margin: 0 10px 0 16px;
      cursor: pointer;
      span {
        font-size: 22px;
        font-family: youshe;
        color: #68e3ee;
      }
      img {
        margin-left: 5px;
        width: 30px;
        height: 32px;
      }
    }
    p {
      padding-left: 22px;
      font-size: 18px;
      span {
        font-size: 24px;
        font-weight: bold;
        font-family: youshe;
        text-decoration: underline;
        cursor: pointer;
        margin-right: 3px;
      }
    }
    & > .img_box {
      @include flex(1);
      text-align: right;
      img {
        cursor: pointer;
        width: 30px;
        height: 32px;
      }
    }
  }
  .map_back {
    margin-right: 20px;
    position: absolute;
    top: 0;
    right: 0;
    z-index: 100;
    font-size: 30px;
    font-family: youshe;
    color: #65e4ee;
    line-height: 52px;
    cursor: pointer;
  }
  .map_switch {
    margin-left: 20px;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 100;
    // 旧样式代码
    // @include flexbox();
    // @include flexAI(center);
    // position: absolute;
    // top: 0;
    // left: 25px;
    // z-index: 9;
    // .space{
    //   display: inline-block;
    //   width: 20px;
    // }
    // .map_switch_btn {
    //   @include flexbox();
    //   .switch_item {
    //     cursor: pointer;
    //     box-sizing: border-box;
    //     margin-right: 5px;
    //     width: 135px;
    //     height: 40px;
    //     line-height: 34px;
    //     text-align: center;
    //     font-size: 20px;
    //     border-width: 3px;
    //     border-style: solid;
    //     border-color: rgba(160, 169, 184, 0.3);
    //     background: rgba(15, 22, 34, 0.6);
    //     color: rgba(160, 169, 184, 0.7);
    //     &.active {
    //       color: #fff;
    //       border-color: rgb(24, 144, 255);
    //       background: rgba(24, 144, 255, 0.15);
    //     }
    //   }
    // }
    // .map_switch_select{
    //   width: 125px;
    // }

    // 新样式代码
    .adnm_switch {
      font-size: 30px;
      font-family: youshe;
      color: #65e4ee;
      line-height: 52px;
      position: relative;
      width: auto;

      i {
        width: 20px;
        height: 20px;
      }
      .dropdown {
        display: inline-block;
        background: center/100% url($imageUrl + "dropdown.png") no-repeat;
      }
      .dropup {
        display: inline-block;
        background: center/100% url($imageUrl + "dropdown.png") no-repeat;
        transform: rotate(180deg);
      }
    }

    .switch_btn_box {
      position: absolute;
      width: 150px;
      height: 70px;
      padding: 10px 0;
      @include noSelect;
      border: 1px solid #6ee3e5;
      background: #03338d;
      // background: center/100% url($imageUrl + "dropdown_bg.png")
      //   no-repeat;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-evenly);
      flex-direction: column;
      z-index: 100;
      li {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
        width: calc(100% - 20px);
        padding: 0px 10px;
        font-size: 18px;
        color: #fff;
        position: relative;
        i {
          width: 20px;
          height: 20px;
        }
        .right_arrow {
          display: inline-block;
          background: center/80% url($imageUrl + "right_arrow.png") no-repeat;
        }
        &:hover {
          color: #65e4ee;
        }
        &:active {
          color: #65e4ee;
        }
        .false_box {
          position: absolute;
          top: -33px;
          left: 150px;
          .adcd_box {
            margin-left: 20px;
            height: 100%;
          }
        }
        .false_basin_box {
          position: absolute;
          top: -50px;
          left: 150px;
          .basin_box {
            margin-left: 20px;
          }
        }
      }
      // .lineline {
      //   width: calc(100% - 40px);
      //   height: 2px;
      //   background: center/80% url($imageUrl + "lineline.png")
      //     no-repeat;
      // }
    }
    .xingzheng {
      position: absolute;
      // top: -15px;
      // left: 20px;
    }
    .liuyu {
      position: absolute;
      top: -35px;
      // left: 170px;
    }
  }

  .map {
    width: 100%;
    height: 100%;
    background: transparent;
  }
}
/* The Modal (background) */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 12; /* Sit on top */
  padding-top: 100px; /* Location of the box */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0, 0, 0); /* Fallback color */
  background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
  background-color: rgba($color: #111e52, $alpha: 0.6);
}
</style>
<style lang="scss">
.el-cascader-menu__list > li {
  label.el-checkbox.is-disabled {
    display: none;
  }
}
.el-cascader-node__label {
  color: #fff;
  &.active {
    color: #fff;
  }
}
.el-cascader-panel {
  background-color: #03338d;
  .el-icon-arrow-right {
    color: #fff;
  }
  &.disabled {
    pointer-events: none;
    cursor: default;
  }
}
.el-cascader-panel.is-bordered {
  border: 1px solid #6ee3e5;
  border-radius: 3px;
}
.el-cascader-node:not(.is-disabled):focus,
.el-cascader-node:not(.is-disabled):hover {
  background: #bac2ca65;
}
.map_switch {
  .el-button-group .el-button--primary:first-child {
    border-right: none;
  }
  .el-dropdown {
    .el-button--primary {
      background: rgba(24, 144, 255, 0.15);
      border-width: 2px;
      height: 40px;
      padding: 3px 20px;
      font-size: 20px;
    }
    .el-dropdown__caret-button {
      padding: 0 5px;
    }
  }
}
.el-dropdown-menu {
  border-color: #448ef7;
  background: #03338d;
  width: 130px;
}
.el-dropdown-menu__item {
  color: #fff;
  text-align: center;
}

.map_switch_select {
  .el-select {
    .el-input__inner {
      height: 40px;
      color: #fff;
      font-size: 16px;
      border-color: #448ef7;
      background: #03338d;
    }
  }
}
.map_select {
  border-color: #448ef7;
  background: #03338d;
  .el-select-dropdown__list {
    padding: 0;
  }
  .el-select-dropdown__item {
    color: #fff;
    height: 40px;
    line-height: 40px;
    font-size: 16px;
    &.selected {
      color: #6ee4e5;
      background: #18407c;
    }
  }
  .el-select-dropdown__item.hover,
  .el-select-dropdown__item:hover {
    background: #18407c;
  }
}
.adcdOverlay {
  width: 180px;
  text-align: center;
  .adcd_top {
    color: #fff;
    font-size: 22px;
    text-align: center;
    padding-bottom: 10px;
    background: #1b77fe;
    padding: 10px 0;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }
  .adcd_bottom {
    padding: 5px 0;
    background: #fff;
    color: #000;
    font-size: 36px;
    font-weight: bold;
    border-bottom-left-radius: -10px;
    border-bottom-right-radius: 10px;
    span {
      font-size: 20px;
      margin-left: 5px;
    }
  }
  &:after {
    content: "";
    position: absolute;
    top: 100%;
    left: -10px;
    width: 10px;
    height: 13px;
    border-width: 0;
    border-style: solid;
    border-color: transparent;
    margin-top: -7px;
    border-right-width: 14px;
    border-right-color: currentColor;
    border-radius: 0 0 20px 0;
    color: #fff;
  }
}

.popup_map_hover {
  background: #fff;
  padding: 8px;
  @include shadow(#ccc, 0px, 0px, 2px);
  border-radius: 4px;
  .popup_content {
    .popup_title {
      color: #000;
      font-size: 14px;
      font-weight: 500;
      padding-bottom: 4px;
    }
  }
}
.popup_map_storm_tide {
  // width: 220px;
  background: #052176;
  padding: 12px;
  @include shadow(#052176, 0px, 0px, 2px);
  border-radius: 4px;
  .popup_content {
    color: #fff;
    .popup_title {
      color: #fff;
      font-size: 16px;
      font-weight: 500;
    }
    .popup_item {
      padding-top: 6px;
      @include flexbox;
      @include flexAC;
      font-size: 14px;
      p {
        color: #fff;
      }
      span {
        color: #fff;
        font-size: 18px;
        font-weight: 500;
      }
    }
  }
  .proj_content {
    width: auto;
    font-size: 14px;
    .carousel_box {
      height: 200px;
      .carousel_item {
        height: 100%;
        .swiper_img {
          background: rgba(0, 0, 0, 0.6);
          width: 100%;
          height: 100%;
          background-repeat: no-repeat;
          background-size: auto 100%;
          background-position: center;
          border-radius: 4px;
        }
      }
    }
    .img_nodata {
      height: 214px;
      text-align: center;
      width: 100%;
      img {
        width: 35%;
      }
      p {
        font-size: 16px;
        color: #fff;
      }
    }
    .img_loading {
      height: 214px;
      width: 100%;
      text-align: center;
      box-sizing: border-box;
      padding-top: 70px;
      p {
        font-size: 16px;
        color: #fff;
      }
    }
    .img_box {
      position: relative;
      .preview_words {
        text-align: center;
        position: relative;
        bottom: 20px;
        z-index: 9;
        color: #fff;
      }
    }
  }
}
.v-modal {
  z-index: 96 !important;
}
</style>

