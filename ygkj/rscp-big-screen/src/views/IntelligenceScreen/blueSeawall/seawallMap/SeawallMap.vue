<template>
  <div class="map_wrap">
    <div class="map" id="mainView"></div>
    <!-- <div class="map_back" v-show="showMapBack" @click="mapBack()">
      <div class="map_back_wrap">
        <i class="clearIcon"></i>
        <span>清除</span>
      </div>
    </div> -->
    <div ref="myModal" @click="showDropDir=false" :style="{'display':showDropDir? 'block ':'none'}" class="modal"></div>
    <div class="map_switch cursor">
      <!-- <div class="map_switch cursor" @mouseover.stop="showDropDir = true,showList=true" @mouseleave.stop="showDropDir = false,showList=false">
      <h2 :title="cascaderCheckedName" class="adnm_switch">{{cascaderCheckedName}}
        <i v-show="showDropDir" class="dropdown cursor" key="dropdown"></i>
        <i v-show="!showDropDir" class="dropup cursor" key="dropup"></i>
      </h2> -->
      <h2 :title="cascaderCheckedName " class="adnm_switch">{{cascaderCheckedName}}
        <i v-show="showDropDir" @click.stop="showDropDir = false,showList=false" class="dropdown cursor" key="dropdown"></i>
        <i v-show="!showDropDir" @click.stop="showDropDir = true,showList=true" class="dropup cursor" key="dropup"></i>
      </h2>
      <div class="false_box">
        <el-cascader-panel tabindex="1" class='adcd_box' v-model="cascaderChecked" v-show="showList" ref="cascaderXZ" :options="adcdList" :props="{emitPath:false }" clearable :show-all-levels="false" @change="changeCascader">
        </el-cascader-panel>
      </div>
    </div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate && (popupData && Object.keys(popupData).length !== 0)">
      <!-- 安澜建设 -->
      <div class="popup_content" v-show="popupData.rightType == '2'">
        <div class="popup_title" style="color: #003ec7; cursor: pointer" @click="lookDetail(popupData)">
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>时间：</p>
          <span>{{ popupData.tm ? popupData.tm.slice(5, 16) : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>实时潮位：</p>
          <span>{{ popupData.tide ? popupData.tide + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>设计潮位：</p>
          <span>{{
            popupData.DESIGN_TIDE_LEVEL
              ? popupData.DESIGN_TIDE_LEVEL + "m"
              : "-"
          }}</span>
        </div>
      </div>

      <!-- 预警 -->
      <div class="popup_content" v-show="popupData.rightType == '3'">
        <div class="popup_title" style="color: #003ec7; cursor: pointer" @click="lookDetail(popupData)">
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>安全状态：</p>
          <span>{{ popupData.status ? popupData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>设计潮位：</p>
          <span>{{
            popupData.DESIGN_TIDE_LEVEL
              ? popupData.DESIGN_TIDE_LEVEL + "m"
              : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>设计标准：</p>
          <span>{{
            popupData.DESIGN_TIDE_STANDARD
              ? popupData.DESIGN_TIDE_STANDARD + "年"
              : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>塘顶最低高程：</p>
          <span>{{
            popupData.LOWEST_ELEVATION ? popupData.LOWEST_ELEVATION + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>最高潮位：</p>
          <span>{{ popupData.forecast ? popupData.forecast.toFixed(2) + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>出现时间：</p>
          <span>{{
            popupData.forecast_time ? popupData.forecast_time.slice(5, 16) : "-"
          }}</span>
        </div>
      </div>

      <!-- 实时预警 -->
      <div class="popup_content" v-show="popupData.rightType == '8'">
        <div class="popup_title" style="color: #003ec7; cursor: pointer" @click="lookDetail(popupData)">
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>安全状态：</p>
          <span>{{ popupData.status ? popupData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>设计潮位：</p>
          <span>{{
            popupData.DESIGN_TIDE_LEVEL
              ? popupData.DESIGN_TIDE_LEVEL + "m"
              : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>设计标准：</p>
          <span>{{
            popupData.DESIGN_TIDE_STANDARD
              ? popupData.DESIGN_TIDE_STANDARD + "年"
              : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>海塘最低高程：</p>
          <span>{{
            popupData.LOWEST_ELEVATION ? popupData.LOWEST_ELEVATION + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>实时潮位：</p>
          <span>{{ popupData.tide ? popupData.tide + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>时间：</p>
          <span>{{ popupData.tm ? popupData.tm.slice(5, 16) : "-" }}</span>
        </div>
      </div>

      <!-- 工程气泡 有照片视频 -->
      <div class="proj_content" v-show="false">
        <div class="proj_title" @click="lookDetail(popupData)">
          {{ popupData.name }}
        </div>
        <div class="proj_tabs">
          <div :class="popupShowImg ? 'active' : ''" @click="popupShowImg = true">
            <div>
              <img v-if="popupShowImg" src="../../../../assets/images/image0.png" alt="" />
              <img v-else src="../../../../assets/images/image1.png" alt="" />
            </div>
            照片
          </div>
          <!-- <div :class="!popupShowImg ? 'active' : ''" @click="popupShowImg = false">
              <div>
                <img v-if="!popupShowImg" src="../../../../assets/images/video0.png" alt="">
                <img v-else src="../../../../assets/images/video1.png" alt="">
              </div>
              视频
            </div> -->
        </div>
        <div v-if="popupShowImg" style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(popupData)">
          <div v-if="photoList.length" class="img_box">
            <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeItem">
              <el-carousel-item class="carousel_item" v-for="(item, index) in photoList" :key="index">
                <div class="swiper_img" :style="{ backgroundImage: 'url(' + item.smallUrl + ')' }" :title="item.name">
                </div>
              </el-carousel-item>
            </el-carousel>
            <div class="preview_words">
              <span>{{ imgIndex + 1 }}</span> / {{ photoList.length }}
            </div>
          </div>
          <div v-if="!loadingImg && !photoList.length" class="img_nodata">
            <img src="../../../../assets/images/noData.png" alt="" />
            <p>暂无图片</p>
          </div>
          <div v-if="loadingImg" class="img_loading">
            <p>图片加载中...</p>
          </div>
        </div>
        <div v-else>video</div>
        <!-- <div class="proj_title" @click="lookDetail(popupData)">{{popupData.name}}</div>
        <div class="proj_detail">
          <div class="popup_item">
            <p>时间：</p>
            <span>{{ popupData.tm ? popupData.tm.slice(5, 16) : "-" }}</span>
          </div>
          <div class="popup_item">
            <p>设计潮位：</p>
            <span>{{
              popupData.designTideLevel
                ? popupData.designTideLevel + "m"
                : "-"
            }}</span>
          </div>
          <div class="popup_item">
            <p>潮位：</p>
            <span>{{ popupData.tide ? popupData.tide + "m" : "-" }}</span>
          </div>
          <div class="popup_item">
            <p>设计标准：</p>
            <span>{{
              popupData.DESIGN_TIDE_STANDARD
                ? popupData.DESIGN_TIDE_STANDARD + "年"
                : "-"
            }}</span>
          </div>
        </div> -->
      </div>

      <project-popup :popupData="popupData" v-show="popupData.rightType == '4'" />

      <!-- 摄像头 -->
      <div class="popup_content" v-show="popupData.rightType == '5'">
        <div class="popup_title">
          {{ popupData.projectName }}
          <span v-if="popupData.state == 'ON'" style="color: #003ec7; cursor: pointer; float: right" @click="lookVideo(popupData)">查看</span>
        </div>
        <div class="popup_item">
          <p>摄像头名称：</p>
          <span>{{ popupData.cameraname ? popupData.cameraname : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>摄像头状态：</p>
          <span v-if="popupData.state == 'ON'">在线</span>
          <span style="color: #ff0000" v-else>离线</span>
        </div>
      </div>

      <!-- 预报潮位 -->
      <div class="popup_content" v-show="popupData.rightType == '6'">
        <div class="popup_title">{{ popupData.stnm }}</div>
        <div class="popup_item">
          <p>最高潮位：</p>
          <span>{{ popupData.forecast ? popupData.forecast + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>出现时间：</p>
          <span>{{
            popupData.forecastTime ? popupData.forecastTime.slice(5, 16) : "-"
          }}</span>
        </div>
      </div>

      <!-- 实时潮位 -->
      <div class="popup_content" v-show="popupData.rightType == '7'">
        <div class="popup_title">{{ popupData.stnm }}</div>
        <div class="popup_item">
          <p>实时潮位：</p>
          <span>{{ popupData.tide ? popupData.tide + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>时间：</p>
          <span>{{ popupData.tm ? popupData.tm.slice(5, 16) : "-" }}</span>
        </div>
      </div>
    </div>
    <div id="overly"></div>
    <div id="cityoverly" ref="citypopup"></div>

    <div class="popup_map_hover" ref="hoverPopup" v-show="currentCoordinate">
      <div class="popup_content">
        <div class="popup_title">
          {{ popupData.NAME || popupData.name || popupData.proName || popupData.prnm}}
        </div>
      </div>
    </div>

    <PrimaryDialog mainTitle=" " custom-class="fangan_dialog" :visible="showFanganDialog" width="1200px" :showFooter="false" top="3.5vh" :appendToBody="true" :fullscreen="fullscreen" @changeFullscreen="fullscreen = !fullscreen" @handleClose="showFanganDialog = false">
      <div class="img-control">
        <img style="width: 100%" src="../../../../assets/images/ppt_pic.png" alt="" />
      </div>
    </PrimaryDialog>

    <div class="mask" v-show="showWisdomPhotoList">
      <div :class="['report-dialog', { 'full-screen': fullscreen }]">
        <!-- <div class="report-dialog-close">
          <i class="el-icon-close" style="z-index: 2" @click="closeTitlePhotosPopup()"></i>
        </div> -->
        <div class="dialog-icon-group">
          <i v-if="!fullscreen" class="dialog-icon dialog-zoom-in" @click="fullscreen = true"></i>
          <i v-else class="dialog-icon dialog-zoom-out" @click="fullscreen = false"></i>
          <i class="dialog-icon dialog-close" @click="showWisdomPhotoList = false"></i>
        </div>
        <div :class="['report-dialog-inner', { 'full-screen': fullscreen }]">
          <!-- arrow="always" -->
          <el-carousel ref="carousel" class="carousel_box ppt-carousel" indicator-position="inside" :autoplay="false">
            <el-carousel-item class="carousel_item" v-for="(item, index) in titlePhotosList" :key="index">
              <!-- <div class="swiper_img" :style="{backgroundImage:'url('+item.url+')'}" :title="item.name"></div> -->
              <!-- <el-image style="width:100%;height: 85.5vh;" :src="item.url" :fit="fit"></el-image> -->
              <img :src="item.url" alt="" :class="[{ 'full-box': fullscreen }]" />
            </el-carousel-item>
          </el-carousel>
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

import PrimaryDialog from "../../../skzgScreen/components/PrimaryDialog.vue";
import ProjectPopup from "../components/ProjectPopup";
import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  drawGeoJson,
  drawPolygonByFeature,
  addLayerClick,
  newTyphoon,
} from "@/utils/mapUtil";
import zjsGeoJson from "@/assets/data/zjsGeoJson.json";
import cutZjsGeoJson from "@/assets/data/cutZjsGeoJson.json";
import zjsXjGeoJson from "@/assets/data/zjsXjGeoJson.json";
import { zhSeawall } from "@/api/api_seawall";
import { getAdcdTree } from '@/api/api_utils'

export default {
  name: "",
  props: {
    activeName: {
      type: String,
      default: 'first'
    }
  },
  components: {
    PrimaryDialog,
    ProjectPopup,
  },
  data() {
    return {
      hideCity: false,
      showFanganDialog: false,
      showWisdomPhotoList: false,
      titlePhotosList: [], //新增标题轮播图弹窗
      fullscreen: false,
      //地图
      serviceLink: "http://223.4.72.162:16080/arcgis/rest/services/",
      pointLayer: null,
      pointLayers: {},
      linesLayer: {},
      jhPointLayer: {},
      hoverOverlay: null,
      overlay: null,
      cityOverlays: null,
      popupLists: [], // 潮位悬浮窗列表
      htStatistics: [], // 市级海塘统计悬浮窗列表

      ptSeawallPointLayer: {}, // 普通海塘落点图层
      adcdMovePolygonLayer: {}, // 普通海塘落点图层
      selectPointLayer: {},
      adcdPolygonLayer: {},
      userAdcd: this.$localData("get", "userInfo").adcd,
      userAdnm: this.$localData("get", "userInfo").adnm,
      zjsLineLayer: null,
      zjsXjLineLayer: null,
      selectLineLayer: {},
      map: null,
      pjObj: {},
      pjObjList: [],
      Overlay: null,
      currentCoordinate: null,
      singleclick: false, // 地图点击事件触发
      moveendEvent: true, // 地图滚动事件触发
      popupData: {},
      defaultCenter: {
        //  第一个数增加 左移 第二个增大上移
        lnglat: [121.2841796875, 29.35238969771562],
        zoom: 8.1,
      },
      markersLayer: {},
      markersCityLayer: {}, // 市级海塘统计悬浮窗
      pointBulingbuling: null,

      //轮播图弹窗数据
      photoList: [],
      popupShowImg: true,
      imgIndex: 0,
      loadingImg: true,

      typhoonsMap: new Map(),
      hasTf: false,
      tfLng: "",
      tfLat: "",

      typhoonPoint: false, //台风点位点击弹框显示标记
      pointOverlay: null, //台风点位点击弹框
      typhoonPointData: {}, //台风点位详情
      typhoonName: "",
      typhoonObject: null,

      jiaxingLayer: null,
      wenzhouLayer: null,
      taizhouLayer: null,
      ningboLayer: null,
      zhoushanLayer: null,
      cascaderChecked: '',
      cascaderCheckedName: '',
      adcdList: [],
      showDropDir: false,//显示上下拉箭头
      userAdcd: this.$localData("get", "userInfo").adcd,
      userAdnm: this.$localData("get", "userInfo").adnm,
      showList: false,
    };
  },
  computed: {
    showMapBack() {
      if (this.$store.state.leftSelectAdcd == '' && this.$store.state.leftSelectProjectScale == '') {
        return false
      }
      return true
    },
    leftSelectAdcd() {
      let adcd = this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd
      return adcd
    },
    touBuTabsActive() {
      return this.$store.state.touBuTabsActive
    },
    choosedItem() {
      return this.$store.state.choosedItem
    },
    choosedItemBox() {
      return this.$store.state.choosedItemBox
    },
    damType() {
      let type = ''
      if (this.choosedItem !== '') {
        switch (this.choosedItem) {
          case '安澜工程':
            type = 12
            break;
          case '水闸':
            type = 13
            break;
          case '泵站':
            type = 14
            break;
          case '闸站':
            type = 15
            break;
          default:
            type = ''
            break;
        }
      } else {
        switch (this.choosedItemBox) {
          case '数量二类塘':
            type = 2
            break;
          case '数量三类塘':
            type = 3
            break;
          case '未加固二类塘':
            type = 6
            break;
          case '未加固三类塘':
            type = 7
            break;
          case '加固中二类塘':
            type = 9
            break;
          case '加固中三类塘':
            type = 10
            break;
          default:
            type = ''
            break;
        }
      }
      return type
    },
  },
  watch: {
    leftSelectAdcd() {
      this.getCityJsonData()
    },
    damType: {
      handler(val) {
        this.getCityJsonData()
      },
      deep: true
    },
    touBuTabsActive: {
      handler(val) {
        this.cascaderCheckedName = "浙江省"
        this.cascaderChecked = this.userAdcd
      },
      deep: true
    }
  },
  created() { },
  mounted() {
    this.mapInit();
    this.getAdcdOptions()
    this.cascaderCheckedName = "浙江省"
    this.cascaderChecked = this.userAdcd
  },
  methods: {
    blurCascader() {
      this.showList = false;
      this.showDropDir = false;
    },
    // 改变行政区划事件
    changeCascader(itemVlaue) {
      this.showList = false;
      this.showDropDir = false;
      let node = this.$refs.cascaderXZ.getCheckedNodes()
      this.cascaderCheckedName = node[0].label
      this.cascaderChecked = itemVlaue
      if (node[0].label == "浙江省") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", this.userAdcd);
        this.removeAllLayer();
        this.closePopup();
        this.initView();
      } else {
        this.$store.commit("SET_LEFT_SELECT_ADCD", itemVlaue);
      }
    },
    // 地图筛选
    async getAdcdOptions() {
      let res = await zhSeawall.adcdLocation({
        adcd: this.$localData("get", "userInfo").adcd,
        userAdcd: this.$localData("get", "userInfo").adcd,
      })
      if (res.success) {
        let adcdAllName = ''
        if (this.userAdcd.endsWith("0000000000")) {
          adcdAllName = "浙江省"
        }
        let temp = [{
          label: adcdAllName,
          value: this.userAdcd,
          checked: true,
        }]
        for (let j = 0; j < res.data.length; j++) {
          res.data[j].label = res.data[j].adnm
          res.data[j].value = res.data[j].adcd
          res.data[j].checked = false
        }
        this.adcdList = [...temp, ...res.data]
      }
    },
    // 弹窗内打开 智慧水库标题轮播图
    lookPhotos() {
      this.showWisdomPhotoList = true;
      this.titlePhotosList = [
        { url: this.$config.zhImgUrl + "diyizhang.png", name: "1" },
      ];
    },

    //初始化地图
    async mapInit() {
      // hover弹窗
      this.hoverOverlay = new Overlay({
        element: this.$refs.hoverPopup, // 弹窗标签，在html里
        autoPan: false, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [-10, -4],
      });

      // 弹窗
      this.overlay = new Overlay({
        element: this.$refs.popup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [-16, -10],
      });

      // 市级悬浮窗
      this.cityOverlays = new Overlay({
        element: this.$refs.citypopup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [-0, -0],
      });

      const base = new Tile({
        source: new XYZ({
          url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt2/arcgis/rest/services/basemap/ZJRasterMap/MapServer/tile/{z}/{y}/{x}", // 线上
          projection: "EPSG:4326", //使用这个坐标系
        }),
      });
      const river = new Image({
        source: new ImageArcGISRest({
          url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/river1/1river/MapServer", // 线上
          projection: "EPSG:4326", //使用这个坐标系
        }),
      });

      const clipLayer = new VectorLayer({
        style: null,
        source: new VectorSource({
          features: new GeoJSON().readFeatures(cutZjsGeoJson),
        }),
      });

      // 地图
      this.map = new Map({
        target: "mainView",
        //地图控件
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021", //  白色地图
              // url: "https://t3.tianditu.gov.cn/DataServer?T=cia_c&X={x}&Y={y}&L={z}&tk=bea4826cd9f20c39d86d6226c45593e9", // 影像图 暗色地图
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),
          base,
          // 所有流域
          new Image({
            source: new ImageArcGISRest({
              url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/Base/liuyumian/MapServer",
              projection: "EPSG:4326", //使用这个坐标系
            }),
            preload: 1,
            visible: false,
          }),
          river,
          clipLayer,
        ],
        overlays: [this.overlay, this.hoverOverlay, this.cityOverlays], // 把弹窗加入地图
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 8.1,
          //添加这个范围 经度-最小，纬度-最小，经度-最大，纬度-最大 左下右上
          // extent: [117.2, 26, 124, 32]
        }),
      });

      // 禁用地图双击缩放
      const dblClickInteraction = this.map
        .getInteractions()
        .getArray()
        .find((interaction) => {
          return interaction instanceof DoubleClickZoom;
        });
      this.map.removeInteraction(dblClickInteraction);

      // 监听事件
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件
      this.mapPointerMove(); // 地图鼠标移动事件


      this.getLines(true);
      // 监听缩放
      this.map.on("moveend", (e) => {
        // 悬浮窗展示逻辑：有点击事件并且有滚动事件时不触发_drawOverlays 方法 ，只有滚动事件时触发_drawOverlays 方法
        this.singleclick ? this.moveendEvent = false : this.moveendEvent = true
        let ViewZoom = this.map.getView().getZoom() //获取当前地图的缩放级别
        if (this.popupLists.length && this.popupLists[0].rightType == 6 && this.moveendEvent && this.activeName == 'second') {
          if (ViewZoom >= 9) {
            // 展示全部
            for (let key in this.markersLayer) {
              this.map.removeOverlay(this.markersLayer[key])
              // this.markersLayer[key].setVisible(false)
            }
            this._drawOverlays(this.popupLists, false)
          } else {
            for (let key in this.markersLayer) {
              // this.markersLayer[key].setVisible(false)
              this.map.removeOverlay(this.markersLayer[key])
            }
            // 展示固定点位悬浮窗
            this._drawOverlays(this.popupLists, true)
          }
        }

        // this.map.getLayers().getArray()[7].setVisible(false);

        // 设置是否隐藏城市统计图层  
        if (ViewZoom >= 8.3) {
          this.hideCity = true
        } else {
          this.hideCity = false
        }

        if (this.hideCity) {
          if (this.activeName === 'four') {

          } else {
            if (this.choosedItem === '旱闸' || this.choosedItem === '缺口' || this.choosedItem === '道口') {
              this.removeLayer(this.ptSeawallPointLayer)
            } else {
              for (let key in this.markersCityLayer) {
                this.map.removeOverlay(this.markersCityLayer[key])
              }
            }

          }
          Object.keys(this.ptSeawallPointLayer).length !== 0 ? this.ptSeawallPointLayer.setVisible(true) : ''
        } else {
          if (this.activeName === 'first') {
            Object.keys(this.ptSeawallPointLayer).length !== 0 ? this.ptSeawallPointLayer.setVisible(false) : ''
            this._drawBlockOverlays(this.choosedItem || this.choosedItemBox)
          } else {
            if (this.activeName === 'four') {

            } else {
              for (let key in this.markersCityLayer) {
                this.map.removeOverlay(this.markersCityLayer[key])
              }
            }
            Object.keys(this.ptSeawallPointLayer).length !== 0 ? this.ptSeawallPointLayer.setVisible(false) : ''
          }
        }

      });


      // 监听地图层级变化
      this.map.getView().on('change:resolution', function (e) {

        // var style = anchor.getStyle();
        // 重新设置图标的缩放率，基于层级10来做缩放
        // style.getImage().setScale(this.getZoom() / 10);
        // anchor.setStyle(style);
      })

      this.addTyphoonPointOverlay();
      // 切割
      const style = new Style({
        fill: new Fill({
          color: "black",
        }),
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

      await this.getCityJsonData()
    },
    clearCityLayer() {
      for (let key in this.markersCityLayer) {
        this.map.removeOverlay(this.markersCityLayer[key])
      }
    },
    // 地图点击事件
    mapClick() {
      let topNumber = 0.01
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        this.closePopup();
        if (
          feature &&
          feature.values_.features &&
          feature.values_.features.length > 0
        ) {
          if (feature.values_.features.length == 1) {
            //点击单个图标时
            this.singleclick = true
            let pointData = feature.values_.features[0].values_.data;
            const coordinate = evt.coordinate; // 获取坐标
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            this.popupData = pointData;
            //打开弹窗
            this._setHtView([pointData.lng, Number(pointData.lat) + topNumber], 15);
            setTimeout(() => {
              this.overlay.setPosition([pointData.lng, pointData.lat]);
              this.singleclick = false
            }, 250);
          } else {
            //点击聚合图标时
            this.map.getView().fit(feature.get("features")[0].getGeometry(), {
              maxZoom: this.map.getView().getZoom() + 2,
              duration: 250,
            });
          }
        } else {
          let pointData = feature.values_.data;
          if (pointData) {
            this.popupData = pointData;
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点

            // 打开弹窗
            this._setHtView([pointData.lng, Number(pointData.lat) + topNumber], 15);
            setTimeout(() => {
              this.overlay.setPosition([pointData.lng, pointData.lat]);
            }, 250);

          }
        }
      });
    },
    // 地图鼠标移动事件
    mapPointerMove() {
      // 鼠标移入
      this.map.on('pointermove', (e) => {
        // console.log(e,'pointermove');
        var pixel = this.map.getEventPixel(e.originalEvent)
        var hit = this.map.hasFeatureAtPixel(pixel)
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        if (hit) {
          // 手指效果
          this.map.getTargetElement().style.cursor = 'pointer'
          if (
            feature &&
            feature.values_.features &&
            feature.values_.features.length > 0
          ) {
            if (feature.values_.features.length == 1) {

            } else {
              return false;
            }
          } else {
            if (feature.values_.data) {
              // 移入点


            } else {
              // 移入面

            }
          }
        } else {
          this.map.removeLayer(this.adcdMovePolygonLayer)
          this.map.getTargetElement().style.cursor = ''
        }
      })
    },

    mapBack() {
      this.$store.commit("SET_LEFT_SELECT_ADCD", "");
      this.$store.commit('SET_LEFT_SELECT_PROJECT_SCALE', '')
    },
    resetMap() {
      this.$store.commit("SET_LEFT_SELECT_ADCD", "");
      this.$store.commit('SET_LEFT_SELECT_PROJECT_SCALE', '')
    },
    /**
     * @description: 清空所有图层 需把增加的图层清除方法写入
     * @param {*}
     * @return {*}
     */
    removeAllLayer() {
      this.closePopup();
      clearInterval(this.pointBulingbuling);
      this.map.removeLayer(this.ptSeawallPointLayer);
      this.map.removeLayer(this.jhPointLayer);
      this.map.removeLayer(this.linesLayer);
      this.map.removeLayer(this.selectPointLayer);
      this.map.removeLayer(this.selectLineLayer);
      for (let key in this.markersLayer) {
        this.map.removeOverlay(this.markersLayer[key]);
      }
      // for (let key in this.markersCityLayer) {
      //   this.map.removeOverlay(this.markersCityLayer[key])
      // }
    },
    //清除台风
    // removeTyphoonLayer() {
    //   console.log(this.map.getLayers().getArray());
    //   this.map.removeLayer(this.typhoonsMap);
    // },
    removeTyphoonLayer() {
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
      //清除台风点选中图标
      let picOverlay = this.map.getOverlayById("overlayWind");
      if (picOverlay) {
        this.map.removeOverlay(picOverlay);
      }
      this.map.removeOverlay(this.lyr);
    },
    // 初始化视图
    initView() {
      let center = this.defaultCenter.lnglat;
      let zoom = this.defaultCenter.zoom;
      if (this.activeName == 'first' || this.activeName == 'second') {
        center = [121.2841796875, 29.35238969771562];
        zoom = 7.9
      } else if (this.activeName == 'third' || this.activeName == 'four') {
        center = [121.7841796875, 29.35238969771562];
        zoom = 8.1
        for (let key in this.markersCityLayer) {
          this.map.removeOverlay(this.markersCityLayer[key])
        }
      }

      this.getLinesXj(false);
      if (this.userAdcd.endsWith("0000000000")) {
        //省级用户清除单个市的范围线
        this.map.removeLayer(this.adcdPolygonLayer);
      }
      const view = this.map.getView();
      view.animate({
        center: center,
        duration: 200,
        zoom: zoom,
      });
    },
    removeCityLayer() {
      this.map.removeLayer(this.jiaxingLayer);
      this.map.removeLayer(this.taizhouLayer);
      this.map.removeLayer(this.wenzhouLayer);
      this.map.removeLayer(this.ningboLayer);
      this.map.removeLayer(this.zhoushanLayer);
    },
    setCityfillColor() {
      this.removeCityLayer();
      let jiaxing = zjsGeoJson.features.find(
        (item) => item.properties["市"] === "嘉兴市"
      );
      let wenzhou = zjsGeoJson.features.find(
        (item) => item.properties["市"] === "温州市"
      );
      let taizhou = zjsGeoJson.features.find(
        (item) => item.properties["市"] === "台州市"
      );
      let ningbo = zjsGeoJson.features.find(
        (item) => item.properties["市"] === "宁波市"
      );
      let zhoushan = zjsGeoJson.features.find(
        (item) => item.properties["市"] === "舟山市"
      );
      let styleObj = {
        lineColor: "#FABD51",
        lineWidth: 0,
        fillColor: "rgba(70, 253, 255, 0.7)",
      };

      this.jiaxingLayer = drawGeoJson(jiaxing, styleObj);
      this.wenzhouLayer = drawGeoJson(wenzhou, styleObj);
      this.taizhouLayer = drawGeoJson(taizhou, styleObj);
      this.ningboLayer = drawGeoJson(ningbo, styleObj);
      this.zhoushanLayer = drawGeoJson(zhoushan, styleObj);
      this.map.addLayer(this.jiaxingLayer);
      this.map.addLayer(this.wenzhouLayer);
      this.map.addLayer(this.taizhouLayer);
      this.map.addLayer(this.ningboLayer);
      this.map.addLayer(this.zhoushanLayer);
    },
    // 获取全省市范围线
    getLines(flag) {
      this.map.removeLayer(this.zjsLineLayer);
      this.zjsLineLayer = null;
      if (flag) {
        let styleObj = {
          lineColor: "#FABD51",
          lineWidth: 2,
          fillColor: "rgba(6,63,178,.08)",
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
    // 单个市高亮 其他区域添加蒙层
    drawMask(selectOneFeature) {
      // 创建矢量图层
      const maskVectorSource = new VectorSource({
        features: [],
      });
      const maskVectorLayer = new VectorLayer({
        source: maskVectorSource,
        style: function () {
          let style = new Style({
            stroke: new Stroke({
              color: "#2BEF89",
              width: 3,
            }),
            fill: new Fill({
              color: "rgba(0,9,32,.3)",
            }),
          });
          return style;
        },
        zIndex: 2,
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
      insideCoords.forEach((el) => {
        let linearRing = new LinearRing(el[0]);
        polygonRing.appendLinearRing(linearRing);
      });

      //添加圆环要素，添加到数据源
      let ringFeature = new Feature({
        geometry: polygonRing,
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
        fillColor: "rgba(0,0,0,0)",
      };
      let zjsCityFeatureList = this.zjsLineLayer.getSource().getFeatures();
      this.map.removeLayer(this.adcdPolygonLayer);
      for (let i = 0; i < zjsCityFeatureList.length; i++) {
        if (zjsCityFeatureList[i].values_["市"] == adnm) {
          this.adcdPolygonLayer = this.drawMask(zjsCityFeatureList[i]);
          this.map.addLayer(this.adcdPolygonLayer);
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
          fillColor: "rgba(6,63,178,.08)",
        };
        this.zjsXjLineLayer = drawGeoJson(zjsXjGeoJson, styleObj);
        this.map.addLayer(this.zjsXjLineLayer);
      }
    },

    // 设置地图位置
    _setView(lng, lat) {
      console.log('_______setView______');
      let to = [lng, lat];
      let view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 15,
      });
    },
    // 设置地图位置
    _setHtView(lnglat, zoom) {
      console.log(zoom, '________setHtView______', lnglat);
      let to = lnglat;
      let view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: zoom || this.defaultCenter.zoom,
      });
    },

    // 设置弹窗
    _setPopup(pointData) {
      this.popupData = pointData;
      const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
      this.currentCoordinate = hdms; // 保存坐标点
      this.overlay.setPosition([pointData.lng, pointData.lat]);
    },

    _setZoom(type) {
      let zoom = "";
      if (type == "plus") {
        zoom = this.map.getView().getZoom() + 1;
      } else {
        zoom = this.map.getView().getZoom() - 1;
      }
      this.map.getView().animate({
        // center: to,
        duration: 200,
        zoom: zoom,
      });
    },


    /** 
     * @function: 获取市级中心点及海塘数量统计
     * @description: 
     * @return {*}
     */
    async getCityJsonData() {
      this.htStatistics = []
      let res = await zhSeawall.adcdLocation({
        adcd: this.leftSelectAdcd,
        userAdcd: this.leftSelectAdcd,
        dam: this.damType, // 获取地图悬浮窗统计类型
      });
      if (res.success && res.data.length) {
        res.data.map(element => {
          switch (element.adnm) {
            case '杭州市':
              element.dryCloseFupanNum = 5
              element.gapCloseFupanNum = 0
              element.crossCloseFupanNum = 0
              break;
            case '宁波市':
              element.dryCloseFupanNum = 0
              element.gapCloseFupanNum = 155
              element.crossCloseFupanNum = 103
              break;
            case '温州市':
              element.dryCloseFupanNum = 249
              element.gapCloseFupanNum = 89
              element.crossCloseFupanNum = 81
              break;
            case '嘉兴市':
              element.dryCloseFupanNum = 34
              element.gapCloseFupanNum = 0
              element.crossCloseFupanNum = 5
              break;
            case '绍兴市':
              element.dryCloseFupanNum = 1
              element.gapCloseFupanNum = 4
              element.crossCloseFupanNum = 3
              break;
            case '舟山市':
              element.dryCloseFupanNum = 0
              element.gapCloseFupanNum = 0
              element.crossCloseFupanNum = 238
              break;
            case '台州市':
              element.dryCloseFupanNum = 161
              element.gapCloseFupanNum = 10
              element.crossCloseFupanNum = 32
              break;
          }
        })
        this.htStatistics = res.data
        if (this.map.getView().getZoom() < 8.3 && this.activeName == 'first') this._drawBlockOverlays(this.choosedItem || this.choosedItemBox)
      } else {
        // this.$message.error(res.msg ? res.msg : "请求失败");
      }
    },

    /** 
     * @function: 
     * @description: 
     * @param {*} pointData 点位信息
     * @param {*} flag true 时显示部分悬浮窗 false 时显示全部悬浮窗
     * @return {*} 
     */
    _drawOverlays(pointData, flag) {
      for (var i = 0; i < pointData.length; i++) {
        //新增放置overly的div
        if (flag) {
          if (pointData[i].showPopup) {
            jquery("#overly").after(
              `<div id='overly${i}' class='pointOverlay'>
            <div class="popup_content">
              <div class="popup_title">${pointData[i].stnm} </div>
              <div class="popup_item">
                <p>最高潮位：</p>
                <span>${pointData[i].forecast ? pointData[i].forecast.toFixed(2) + 'm' : "-"
              }</span>
              </div>
              <div class="popup_item">
                <p>出现时间：</p>
                ${pointData[i].forecastTime ? pointData[i].forecastTime.slice(5, 16) : "-"
              }
              </div>
            </div>
          </div>`
            );
            //新增overly
            this.markersLayer['overly' + i] = new Overlay({
              offset: [-16, -10],
              positioning: "bottom-center",
              position: [pointData[i].lng, pointData[i].lat],
              element: document.getElementById("overly" + i),
            });
            this.map.addOverlay(this.markersLayer['overly' + i]);
            document.getElementById("overly" + i).addEventListener('click', (event) => {
            })
          }
        } else {
          jquery("#overly").after(
            `<div id='overly${i}' class='pointOverlay'>
            <div class="popup_content">
              <div class="popup_title">${pointData[i].stnm} </div>
              <div class="popup_item">
                <p>最高潮位：</p>
                <span>${pointData[i].forecast ? pointData[i].forecast.toFixed(2) + 'm' : "-"
            }</span>
              </div>
              <div class="popup_item">
                <p>出现时间：</p>
                ${pointData[i].forecastTime ? pointData[i].forecastTime.slice(5, 16) : "-"
            }
              </div>
            </div>
          </div>`
          );
          //新增overly
          this.markersLayer["overly" + i] = new Overlay({
            offset: [-16, -10],
            positioning: "bottom-center",
            position: [pointData[i].lng, pointData[i].lat],
            element: document.getElementById("overly" + i),
          });
          this.map.addOverlay(this.markersLayer["overly" + i]);
        }
      }
      for (var i = 0; i < pointData.length; i++) {
        if (pointData[i].lat && pointData[i].lng && pointData[i].showPopup) {
          const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
          this.currentCoordinate = hdms; // 保存坐标点
          // this.overlay.setPosition([pointData[0].lng, pointData[0].lat]);
        }
      }
    },

    /** 
     * @function: 
     * @description: 
     * @param {*} pointDatas 点位信息 经纬度 统一用lgtd lttd
     * @param {*} type 显示块块悬浮窗 图层类型
     * @return {*}
     */
    _drawBlockOverlays(type) {
      for (let key in this.markersCityLayer) {
        this.map.removeOverlay(this.markersCityLayer[key])
      }
      for (var i = 0; i < this.htStatistics.length; i++) {
        // 修改单位
        let itemValue = this.htStatistics[i]
        switch (type) {
          case '水闸':
            itemValue.class = '_sluice'
            break;
          case '泵站':
            itemValue.class = '_pump'
            break;
          case '闸站':
            itemValue.class = '_damGate'
            break;
          default:
            itemValue.class = ''
            break;
        }

        switch (type) {
          case '分布':
          case '安澜工程':
          case '数量二类塘':
          case '数量三类塘':
          case '未加固二类塘':
          case '未加固三类塘':
          case '加固中二类塘':
          case '加固中三类塘':
            itemValue.unit = '条（段）'
            break;
          case '长度':
            itemValue.unit = 'km'
            break;
          case '水闸':
          case '泵站':
          case '闸站':
            itemValue.unit = '座'
            break;
          case '人员转移正常':
          case '人员转移复盘':
          case '人员转移':
            itemValue.unit = '人'
            break;
          case '巡查人次正常':
          case '巡查人次复盘':
          case '巡查人次':
            itemValue.unit = '次'
            break;
          default:
            itemValue.unit = '处'
            break;
        }
        // 修改显示数量的字段 为 displayNum
        switch (type) {
          case '水闸':
          case '泵站':
          case '闸站':
          case '分布':
          case '安澜工程':
          case '数量二类塘':
          case '数量三类塘':
          case '未加固二类塘':
          case '未加固三类塘':
          case '加固中二类塘':
          case '加固中三类塘':
            itemValue.displayNum = itemValue.num
            break;
          case '长度':
            itemValue.displayNum = (itemValue.length / 1000).toFixed(0)
            break;
          case '人员转移':
          case '人员转移正常':
          case '人员转移复盘':
            itemValue.displayNum = itemValue.popNum
            break;
          case '巡查人次正常':
          case '巡查人次复盘':
          case '巡查人次':
            itemValue.displayNum = itemValue.patrolNum
            break;
          case '旱闸':
          case '旱闸总数':
            itemValue.displayNum = itemValue.dryGate
            break;
          case '旱闸正常':
            itemValue.displayNum = itemValue.dryCloseNum
            break;
          case '旱闸复盘':
            itemValue.displayNum = itemValue.dryCloseFupanNum
            break;
          case '缺口':
          case '缺口总数':
            itemValue.displayNum = itemValue.gapNum
            break;
          case '缺口正常':
            itemValue.displayNum = itemValue.gapCloseNum
            break;
          case '缺口复盘':
            itemValue.displayNum = itemValue.gapCloseFupanNum
            break;
          case '道口':
          case '道口总数':
            itemValue.displayNum = itemValue.crossNum
            break;
          case '道口正常':
            itemValue.displayNum = itemValue.crossCloseNum
            break;
          case '道口复盘':
            itemValue.displayNum = itemValue.crossCloseFupanNum
            break;

          default:
            itemValue.displayNum = 0
            break;
        }
        if (itemValue.displayNum) {
          jquery("#cityoverly").after(
            `<div id='cityoverly${i}' class='cityOverlay' lng='${itemValue.lgtd}' lat='${itemValue.lttd}'>
                  <i class="htTotal ${itemValue.class}" ></i>
                  <div class="city_item_warp">
                     <span class="city_name">${itemValue.adnm} </span>       
                     <span class="city_num">${itemValue.displayNum} </span>       
                     <p class="city_uni">${itemValue.unit}</p>       
                  </div>
              </div>`
          );
          //新增overly
          this.markersCityLayer['cityoverly' + i] = new Overlay({
            // 第一个负数 往左移  第二个负数往上移
            offset: [10, -25],
            positioning: "right-center",
            position: [Number(itemValue.lgtd), Number(itemValue.lttd)],
            element: document.getElementById("cityoverly" + i),
          });
          this.map.addOverlay(this.markersCityLayer['cityoverly' + i]);
        }
      }
    },
    _drawPoints(list, flag, type) {
      this.map.removeLayer(this.selectPointLayer);
      // this.map.removeLayer(this.selectLineLayer);
      let scale = 0.4;
      // 点
      let src = require("@/assets/images/map/green-seawall.png"); //图片路径
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: src,
        title: "",
        scale: 0.4,
      };

      this.selectPointLayer = drawPoint(list, styleObj, true, type);
      this.selectPointLayer.setZIndex(999);
      this.map.addLayer(this.selectPointLayer);
      // let that = this;
      // this.pointBulingbuling = setInterval(() => {
      //   that.selectPointLayer.setVisible(false);
      //   setTimeout(() => {
      //     that.selectPointLayer.setVisible(true);
      //   }, 300);
      // }, 600);
      // setTimeout(() => {
      //   clearInterval(that.pointBulingbuling);
      //   setTimeout(() => {
      //     that.map.removeLayer(that.selectPointLayer);
      //   }, 300);
      // }, 3300);

      // flag 为 true时只落点 不划线
      if (!flag) {
        // 线
        let lineObj = list[0].lineArr[0];
        lineObj.coordinates = [];
        if (lineObj.line && lineObj.line.length) {
          for (let j = 0; j < lineObj.line.length; j++) {
            lineObj.coordinates.push([
              lineObj.line[j].lng,
              lineObj.line[j].lat,
            ]);
          }
          switch (lineObj.level) {
            case "1":
              lineObj.lineColor = "#193CC5";
              break;
            case "2":
              lineObj.lineColor = "#4C71FF";
              break;
            case "3":
              lineObj.lineColor = "#7692FFFF";
              break;
            case "4":
              lineObj.lineColor = "#ACB5FFFF";
              break;
            case "5":
              lineObj.lineColor = "#E3E4FFFF";
              break;
            default:
              lineObj.lineColor = "#E3E4FFFF";
              break;
          }
        }
        lineObj.lineWidth = 8;
        this.selectLineLayer = drawLines([lineObj]);
        this.map.addLayer(this.selectLineLayer);
      }
    },

    _drawLines(list) {
      this.map.removeLayer(this.linesLayer);
      for (let i = 0; i < list.length; i++) {
        list[i].coordinates = [];
        if (list[i].line && list[i].line.length) {
          for (let j = 0; j < list[i].line.length; j++) {
            list[i].coordinates.push([
              list[i].line[j].lng,
              list[i].line[j].lat,
            ]);
          }
          switch (list[i].level) {
            case "1":
              list[i].lineColor = "#0C9800";
              break;
            case "2":
              list[i].lineColor = "#0db200";
              break;
            case "3":
              list[i].lineColor = "#12e800";
              break;
            case "4":
              list[i].lineColor = "#93f28b";
              break;
            case "5":
              list[i].lineColor = "#e6fbe4";
              break;
            default:
              list[i].lineColor = "#e6fbe4";
              break;
          }
        }
      }
      this.linesLayer = drawLines(list);
      this.map.addLayer(this.linesLayer);
    },
    newMapPoints(coordinates, type) {
      let scale = 0.4;
      if (type == "cwyj") {
        scale = 0.4;
      }
      this.removeAllLayer();
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/yndq.png"), //图片路径
        title: "",
        scale: scale,
      };
      this.popupLists = coordinates
      this.jhPointLayer = drawPoint(coordinates, styleObj, false, type);
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
      if (type === "tide") {
        this._drawOverlays(coordinates, true)
      }
    },
    newMapJh(coordinates, type) {
      this.popupLists = coordinates
      switch (type) {
        case "ptSeawallPointLayer":
          this.removeAllLayer();
        default:
          this.map.removeLayer(this[type]);
          break;
      }
      // this.removeAllLayer()
      // this.map.removeLayer(this[type])
      let vectorSource = new VectorSource({});
      let clusterSource = new ClusterSource({
        distance: 80,
        source: vectorSource,
      });
      for (let i = 0; i < coordinates.length; i++) {
        if (coordinates[i].lng && coordinates[i].lat) {
          const iconFeature = new Feature({
            geometry: new Point([
              Number(coordinates[i].lng),
              Number(coordinates[i].lat),
            ]),
            data: coordinates[i],
          });
          vectorSource.addFeature(iconFeature);
        }
      }

      var styleCache = {};
      this[type] = new VectorLayer({
        title: type,
        source: clusterSource,
        style: (feature) => {
          var size = feature.get("features").length;
          let src = require("@/assets/images/map/seawall-jh.png");
          if (size > 1) {
            let featureData = feature.get("features")[0].values_.data;
            switch (featureData.color) {
              case "_damGate":
                src = require("@/assets/images/map/_damGate-jh.png");
                break;
              case "_pump":
                src = require("@/assets/images/map/_pump-jh.png");
                break;
              case "_sluice":
                src = require("@/assets/images/map/_sluice-jh.png");
                break;
              default:
                src = require("@/assets/images/map/yndq-jh.png");
                break;
            }
            var style = new Style({
              image: new Icon({
                src: src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.4,
              }),
              text: new Text({
                textAlign: "center",
                textBaseline: "middle",
                text: size.toString(),
                fill: new Fill({
                  
                }),
                font: "normal 18px 微软雅黑",
                // x 增加 是往右  减小是 往左
                offsetX: -20,
                // y 增加 是往上  减小是 往下
                offsetY: 16,
                scale: 1,
              }),
            });
            styleCache[size] = style;
            return style;
          } else {
            let featureData = feature.get("features")[0].values_.data;
            let src = require("@/assets/images/map/yndq.png");
            // this.map.getView().getZoom() >= 14
            switch (featureData.color) {
              case "_damGate":
                src = require("@/assets/images/map/_damGate.png");
                break;
              case "_pump":
                src = require("@/assets/images/map/_pump.png");
                break;
              case "_sluice":
                src = require("@/assets/images/map/_sluice.png");
                break;
              default:
                src = require("@/assets/images/map/yndq.png");
                break;
            }
            let style = new Style({
              image: new Icon({
                src: src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.4,
              }),
            });
            return style;
          }
        },
      });
      this[type].setZIndex(99);
      this.map.addLayer(this[type]);
      if (type == 'ptSeawallPointLayer') {
        this.ptSeawallPointLayer.values_.title && this.ptSeawallPointLayer.setVisible(false)
      }
    },
    getHtList(coordinates) {
      this.popupLists = coordinates
    },

    // 工具箱
    mapToolBar(type) {
      if (type == "none") {
        Measure.meatureFeaturesClear(this.map);
      } else {
        Measure.measure(this.map, type);
      }
    },

    // 设置聚合范围
    setCluster(val) {
      this.jhPointLayer.getSource().setDistance(val);
    },

    // 关闭弹窗
    closePopup() {
      this.popupData = {}
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
    },

    // 关闭弹窗
    closeHtPopup() {
      this.popupData = {}
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
      this._setHtView(this.defaultCenter.lnglat, this.defaultCenter.zoom)
    },



    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$parent.openLookPhotosPopup();
      this.$parent.showPhotosList(this.photoList, row.NAME);
    },

    // 弹窗内打开查看视频
    lookVideo(row) {
      this.$parent.openLookVideoPopup();
      this.$parent.$refs.seawallRightVideo.getVideoToken(row.cameraid);
    },

    // 关闭hover弹窗
    closeHoverPopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.hoverOverlay.setPosition(undefined);
      this.currentCoordinate = null;
    },

    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.openDetailPopup(row);
    },

    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
    },
    async showTyphoon(tfid, type) {
      let that = this;
      jquery.ajax({
        url: "https://sk.zjwater.com/typhoonServer/Api/TyphoonInfo/" + tfid,
        type: "get",
        dataType: "jsonp",
        data: {},
        success: function (res) {
          if (res.length) {
            let mydata = {
              name: res[0].name,
              code: res[0].tfid,
              points: [],
            };
            // let nowDate = new Date('2021-07-25 13:00:00')
            let points = res[0].points.slice(0, 92);
            // res[0].points = points;
            points.map((item, i) => {
              item.lat = Number(item.lat);
              item.lng = Number(item.lng);
              item.index = i;
              item.type = "live";
              mydata.points.push(item);
            });
            that.tfLng = points[points.length - 1].lng;
            that.tfLat = points[points.length - 1].lat;
            if (type) {
              that.showTf();
            }
            that.removeTyphoonLayer();
            that.typhoonObject = new newTyphoon(
              that.map,
              mydata,
              that.typhoonsMap,
              that,
              true
            );
            that.typhoonObject.init();
          }
        },
      });
    },

    showTf() {
      let lng = this.tfLng;
      let lat = this.tfLat;
      // [120.4997109415048, 29.22000109695038],
      // let lng = 122.4997109415048;
      // let lat = 29.22000109695038;
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 6,
      });
    },

    addTyphoonPointOverlay() {
      this.pointOverlay = new Overlay({
        element: this.$refs.typhoonPopup, // 弹窗标签，在html里
        autoPan: false,
        positioning: "bottom-center",
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
      });
      this.map.addOverlay(this.pointOverlay);
    },
  },
};
</script>

<style lang='scss' scoped>
.ol-popup {
  position: absolute;
  bottom: 15px;
  left: -50%;
  transform: translateX(-56%);
  border-radius: 3px;
  min-width: 150px;
  .ol-popup__wrapper {
    // background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
    background-color: #ffffff;
    // border: 2px solid #00c1ff;
    // box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
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
      border-top: 7px solid #ffffff;
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
      border-top: 10px solid #ffffff;
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
    color: #333333;
    .typhoon-point_title {
      text-align: center;
      font-size: 20px;
      color: #333333;
      letter-spacing: 0;
      line-height: 24px;
      font-weight: 600;
      padding-bottom: 5px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.4);
    }
    .typhoon-point_content {
      p {
        padding: 8px 0;
        > span {
          font-size: 16px;
          color: #333333;
          font-weight: 600;
          padding-right: 8px;
          &:first-child {
            color: rgba(0, 0, 0, 0.48);
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
.map_wrap {
  background: transparent;
  width: 100%;
  height: 100%;
  position: relative;
  .map_back {
    margin-right: 20px;
    position: absolute;
    top: 10%;
    right: 28%;
    z-index: 100;

    line-height: 52px;
    cursor: pointer;
    .map_back_wrap {
      @include flexbox;
      @include flexAC;
      i {
        width: 14px;
        height: 16px;
        background: url("../../../../assets/images/clearBtn.png") no-repeat
          center;
        background-size: 100% 100%;
      }
      span {
        font-size: 16px;
        // font-family: youshe;
        // color: #65e4ee;
        color: #ffffff;
        padding-left: 6px;
      }
    }
  }
  .map_switch {
    margin-left: 20px;
    position: absolute;
    top: 10%;
    right: 28%;
    z-index: 16;
    .adnm_switch {
      font-size: 30px;
      font-family: pangmen3;
      color: #65e4ee;
      line-height: 52px;
      margin-top: 16px;
      padding: 6px 10px;
      line-height: 46px;
      position: relative;
      width: auto;
      background: center/100% url($zhImgUrl + "drop_btn_bg.png") no-repeat;
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
    .false_box {
      position: absolute;
      top: 75px;
      left: -24px;
      color: #fff;
      height: 290px;
      .adcd_box {
        margin-left: 20px;
        height: 290px;
        background: center url($zhImgUrl + "drop_bg.png") no-repeat;
        background-size: 97% 100%;
      }
    }
  }
  /* The Modal (background) */
  .modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 15; /* Sit on top */
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
  .map {
    width: 100%;
    height: 100%;
    // top: 7%;
    // left: 0%;
    // width: 84%;
    // height: 97%;
  }
  .screen_title {
    width: 100%;
    position: absolute;
    top: 0;
    text-align: center;
    z-index: 9;
    .screen_title_box {
      letter-spacing: 3px;
      margin: 0 auto;
      width: 1217px;
      height: 68px;
      background: url("../../../../assets/images/title-bg-icon.png") no-repeat
        center;
      background-size: 100% 100%;
      color: #ffffff;
      font-size: 34px;
      font-weight: 600;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      justify-content: center;
      img {
        width: 50px;
        height: 58px;
        margin-right: 15px;
      }
      .ppt-look-icon {
        width: 32px;
        height: 32px;
        margin-right: 0;
        margin-left: 5px;
        cursor: pointer;
      }
    }

    .haveTF {
      position: absolute;
      right: 490px;
      top: 8px;
      width: 68px;
      height: 68px;
      cursor: pointer;
    }
    .noTF {
      position: absolute;
      right: 500px;
      top: 12px;
      width: 62px;
      height: 62px;
    }
  }
  .popup_map {
    // width: 220px;
    background: #fff;
    padding: 12px;
    @include shadow(#ccc, 0px, 0px, 2px);
    border-radius: 4px;
    .popup_content {
      color: #333;
      .popup_title {
        color: #000;
        font-size: 16px;
        font-weight: 500;
      }
      .popup_item {
        padding-top: 6px;
        @include flexbox;
        @include flexAC;
        font-size: 14px;
        p {
          color: #666;
        }
        span {
          color: #000;
          font-size: 18px;
          font-weight: 500;
        }
      }
    }
    .proj_content {
      width: 350px;
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
          color: #999;
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
          color: #999;
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

      .proj_tabs {
        @include flexbox;
        @include flexAC;
        padding-bottom: 10px;
        & > div {
          width: 80px;
          @include flexbox;
          @include flexAC;
          background: #f3f3f3;
          height: 30px;
          border-radius: 20px;
          cursor: pointer;
          color: #333;
          margin-right: 15px;
          & > div {
            width: 30px;
            height: 30px;
            border-radius: 20px;
            text-align: center;
            background: #f3f3f3;
            border: 1px solid #cccccc;
            box-sizing: border-box;
            margin-right: 5px;
            img {
              width: 50%;
              margin-top: 6px;
            }
          }
        }
        & > div.active {
          color: #006ce1;
          font-weight: bold;
          & > div {
            background: #006ce1;
            border: 1px solid #006ce1;
            box-sizing: border-box;
          }
        }
      }
      .proj_title {
        cursor: pointer;
        font-size: 20px;
        margin-bottom: 16px;
        font-weight: bold;
      }
      .proj_detail {
        @include flexbox;
        @include flexJC(space-between);
        @include flexflow(row wrap);
        .popup_item {
          margin-top: 10px;
          width: 49%;
          @include flexbox;
          p {
            color: #666;
          }
          span {
            color: #000;
          }
        }
      }
    }
  }
  .overlay {
    // width: 180px;
    background: #fff;
    padding: 12px;
    @include shadow(#ccc, 0px, 0px, 2px);
    border-radius: 4px;
    .popup_content {
      color: #333;
      .popup_title {
        color: #000;
        font-size: 16px;
        font-weight: 500;
      }
      .popup_item {
        padding-top: 6px;
        @include flexbox;
        @include flexAC;
        font-size: 14px;
        p {
          color: #666;
        }
        span {
          color: #000;
          font-size: 18px;
          font-weight: 500;
        }
      }
    }
  }
}

.mask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 3000;

  .report-dialog {
    position: absolute;
    left: 50%;
    top: 50%;
    padding: 40px 20px 20px 20px;
    transform: translate(-50%, -50%);
    background-color: #fff;
    &.full-screen {
      width: 98%;
      height: 94%;
    }
  }
  .report-dialog-close {
    position: absolute;
    top: -16px;
    right: 19px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #fff;
    z-index: 3001;
    cursor: pointer;
  }

  .dialog-icon-group {
    display: flex;
    position: absolute;
    right: 0;
    top: 0;
    padding: 10px 20px;
  }
  .dialog-icon {
    width: 20px;
    height: 20px;
    background-size: 100% 100%;
    cursor: pointer;
  }
  .dialog-zoom-in {
    background: url($imageUrl + "maxscreen.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-zoom-out {
    background: url($imageUrl + "minscreen.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-close {
    background: url($imageUrl + "closeImg.png") no-repeat center;
    background-size: 100%;
  }
  .dialog-icon + .dialog-icon {
    margin-left: 20px;
  }
  .report-dialog-close:before {
    box-sizing: border-box;
    position: absolute;
    left: -21px;
    top: -44px;
    content: "";
    width: 60px;
    height: 60px;
    border-top: 0;
    border-bottom: 20px solid rgba(0, 103, 223, 0.42);
    border-left: 2px solid transparent;
    border-right: 3px solid transparent;
  }
  .report-dialog-inner {
    width: 152vh;
    height: 85.5vh;
    &.full-screen {
      width: 100%;
      height: 100%;
    }
    .ppt-carousel {
      height: 100%;
      img {
        width: 100%;
        height: 100%;
        &.full-box {
          width: 100%;
          height: 100%;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.report-dialog-inner .el-carousel__button {
  width: 12px;
  height: 12px;
  background-color: #443d3d;
  border-radius: 50%;
}
.pointOverlay {
  // width: 180px;
  background: #fff;
  padding: 10px;
  @include shadow(#ccc, 0px, 0px, 2px);
  border-radius: 4px;
  .popup_content {
    color: #333;
    .popup_title {
      color: #000;
      font-size: 16px;
      font-weight: 500;
    }
    .popup_item {
      padding-top: 6px;
      @include flexbox;
      @include flexAC;
      font-size: 14px;
      p {
        color: #666;
      }
      span {
        color: #000;
        font-size: 18px;
        font-weight: 500;
      }
    }
  }
}
.cityOverlay {
  width: 330px;
  height: 50px;
  @include flexbox();
  @include flexAC();
  i.htTotal {
    width: 36px;
    height: 36px;
    background: url("../../../../assets/images/yndq.png") no-repeat center;
    background-size: 100% 100%;
    &._damGate {
      background: url("../../../../assets/images/map/_damGate.png") no-repeat
        center;
      background-size: 100% 100%;
    }
    &._pump {
      background: url("../../../../assets/images/map/_pump.png") no-repeat
        center;
      background-size: 100% 100%;
    }
    &._sluice {
      background: url("../../../../assets/images/map/_sluice.png") no-repeat
        center;
      background-size: 100% 100%;
    }
  }
  .city_item_warp {
    background: url("../../../../assets/images/city_bg.png") no-repeat center;
    background-size: 100% 100%;
    color: #fff;
    font-size: 20px;
    width: 230px;
    padding: 20px 5px 20px 30px;
    .city_name {
    }
    .city_num {
      font-family: pangmen3;
      padding-left: 16px;
      font-size: 24px;
    }

    .city_uni {
      width: 80px;
      display: inline-block;
    }
  }
}
.el-cascader-menu {
  min-width: 100px;
  width: 150px;
  height: 290px;
}
.el-cascader-menu__list > li {
  label.el-checkbox.is-disabled {
    display: none;
  }
}
.el-cascader-node__label {
  color: #fff;
  padding: 0 16px;
  &.active {
    color: #fff;
  }
}
.el-cascader-node__prefix {
  font-size: 16px;
  color: #6ee3e5;
  font-weight: 600;
}
.map_wrap .adcd_box {
  background-color: transparent;
  .el-icon-arrow-right {
    color: #fff;
  }
  &.disabled {
    pointer-events: none;
    cursor: default;
  }
}

.map_wrap .adcd_box.is-bordered {
  border: none;
  border-radius: 0px;
}
.el-cascader-node:not(.is-disabled):focus,
.el-cascader-node:not(.is-disabled):hover {
  background: #bac2ca65;
}
</style>