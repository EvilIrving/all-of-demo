<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-26 21:13:25
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallMap\SeawallMap.vue
-->
<template>
  <div class="map_wrap">
    <div class="screen_title">
      <div class="screen_title_box">
        <img src="../../../../assets/images/logo.png" alt="" />
        海塘防潮
        <img @click="showFanganDialog = true" class="ppt-look-icon" src="../../../../assets/images/ppt-look-icon.png" alt="" />
      </div>
    </div>
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate">
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
          <p>预报潮位：</p>
          <span>{{ popupData.forecast ? popupData.forecast + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>预报时间：</p>
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
                <div class="swiper_img" :style="{ backgroundImage: 'url(' + item.smallUrl + ')' }" :title="item.name"></div>
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
          <p>预报潮位：</p>
          <span>{{ popupData.forecast ? popupData.forecast + "m" : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>时间：</p>
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
import { defaults as defaultControls } from "ol/control";
import Point from "ol/geom/Point";
import Overlay from "ol/Overlay";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import { Style, Fill, Stroke, Icon, Text, Circle } from "ol/style";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import { Vector as VectorSource, Cluster as ClusterSource } from "ol/source";
import { Draw, DoubleClickZoom } from "ol/interaction";
import Measure from "@/utils/measure";
import jquery from "@/plugins/jquery-1.11.3";
import PrimaryDialog from "../../../skzgScreen/components/PrimaryDialog.vue";
import ProjectPopup from "../components/ProjectPopup";
import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  addLayerClick,
  newTyphoon,
} from "@/utils/mapUtil";

import { seawallPhotoListApi, getFile } from "@/api/api_seawall";

export default {
  name: "",
  components: {
    PrimaryDialog,
    ProjectPopup,
  },
  data() {
    return {
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
      redPointLayer: {}, // 红码图层
      ylTangPointLayer: {}, // 一类塘图层
      elTangPointLayer: {}, // 二类塘图层
      slTangPointLayer: {}, // 三类塘图层
      yndqPointLayer: {}, // 一年内到期图层
      cqwjdPointLayer: {}, // 超期未鉴定图层
      greenPointLayer: {}, // 绿码图层
      yellowPointLayer: {}, // 黄码图层
      ptSeawallPointLayer: {}, // 普通海塘落点图层
      selectPointLayer: {},
      selectLineLayer: {},
      map: null,
      pjObj: {},
      pjObjList: [],
      Overlay: null,
      currentCoordinate: null,
      popupData: {},
      defaultCenter: {
        lnglat: [121.4994109415048, 29.22000109695038],
        zoom: 8.5,
      },
      markersLayer: {},
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
    };
  },
  created() { },
  mounted() {
    this.mapInit();
  },
  methods: {
    //获取工程图片
    // async getProjPhoto(){

    // },
    // 弹窗内打开 智慧水库标题轮播图
    lookPhotos() {
      this.showWisdomPhotoList = true;
      this.titlePhotosList = [
        { url: this.$config.zhImgUrl + "diyizhang.png", name: "1" },
      ];
    },
    async getPhotoDetailList(prcd, seawallName) {
      this.imgIndex = 0;
      this.photoList = [];
      this.loadingImg = true;
      let res = await seawallPhotoListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        let arr = [];
        if (res.rows.length) {
          res.rows.map((v) => {
            arr.push(v.extphoto6);
          });
          this.getFileList(arr.join(","), seawallName);
        } else {
          this.$message.error("该工程暂无照片！");
        }
      } else {
        this.$message.error("获取照片失败！");
      }
    },
    async getFileList(ids, seawallName) {
      let res = await getFile({
        dataId: ids,
      });
      let photoList = [];
      if (res.list.length) {
        res.list.map((v) => {
          let url = v.filurl;
          let smallUrl = v.filurl;

          if (v.smallPicurl && v.smallPicurl != "null") {
            smallUrl = "http://sk.zjwater.com/fileUpds/" + v.smallPicurl;
          } else if (v.filurl && v.filurl != "null") {
            if (v.filurl.indexOf("https://file.ygwjg.com/") != -1) {
              smallUrl = v.filurl + "?imageView2/2/w/300/h/200";
            } else {
              smallUrl = "http://sk.zjwater.com/fileUpds/" + v.filurl;
            }
          }

          if (
            v.filurl &&
            v.filurl.indexOf("https://file.ygwjg.com/") == -1 &&
            v.filurl.indexOf("/fileUpds/") != -1
          ) {
            url = "https://sk.zjwater.com" + v.filurl;
          }
          photoList.push({
            name: v.filnm,
            url: url,
            smallUrl: smallUrl,
          });
        });
        this.photoList = photoList;
        // this.$parent.showPhotosList(photoList, seawallName)
      }
      this.loadingImg = false;
    },

    //初始化地图
    mapInit() {
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

      // 地图
      this.map = new Map({
        target: "mainView",
        //地图控件
        // controls: defaultControls({ attribution: true, zoom: true }),
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          new Tile({
            source: new XYZ({
              // url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}", //绿色
              url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/basemap/WYX1029/MapServer/tile/{z}/{y}/{x}", //白色
              projection: "EPSG:4326", //使用这个坐标系
              // crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          // 临时改动，供截图使用
          // new Tile({
          //   source: new XYZ({
          //     url:
          //       "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
          //     projection: "EPSG:4326",
          //     crossOrigin: "anonymous",
          //   }),
          //   preload: 1,
          //   visible: true,
          // }),

          // new Tile({
          //   source: new XYZ({
          //     // url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}", //绿色
          //     url: 'http://223.4.72.162:16080/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}',
          //     // url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/basemap/WYX1029/MapServer/tile/{z}/{y}/{x}", //白色
          //     projection: "EPSG:4326", //使用这个坐标系
          //     // crossOrigin: "anonymous",
          //   }),
          //   preload: 1,
          //   visible: true,
          // }),
        ],
        overlays: [this.overlay], // 把弹窗加入地图
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0,
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

      // 鼠标移入显示手指
      this.map.on("pointermove", (e) => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        if (hit) {
          this.map.getTargetElement().style.cursor = "pointer";
        } else {
          this.map.getTargetElement().style.cursor = "";
        }
      });

      this.addTyphoonPointOverlay();
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        let topNumber = 0.01;
        this.closePopup();
        if (
          feature &&
          feature.values_.features &&
          feature.values_.features.length > 0
        ) {
          if (feature.values_.features.length == 1) {
            //点击单个图标时
            let pointData = feature.values_.features[0].values_.data;
            const coordinate = evt.coordinate; // 获取坐标
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            this.popupData = pointData;
            // this.getPhotoDetailList(pointData.prcd, pointData.name);
            //打开弹窗
            setTimeout(() => {
              this._setView(Number(pointData.lng), Number(pointData.lat) + topNumber)
              setTimeout(() => {
                this.overlay.setPosition([pointData.lng, pointData.lat]);
              }, 400)
            }, 250)
            // this.overlay.setPosition([pointData.lng, pointData.lat]);
          } else {
            //点击聚合图标时
            this.map.getView().fit(feature.get("features")[0].getGeometry(), {
              maxZoom: this.map.getView().getZoom() + 1,
              duration: 250,
            });
          }
        } else {
          let pointData = feature.values_.data;
          if (pointData) {
            this.popupData = pointData;
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            // this.getPhotoDetailList(pointData.prcd, pointData.name);
            // 打开弹窗
            setTimeout(() => {
              this._setView(Number(pointData.lng), Number(pointData.lat) + topNumber)
              setTimeout(() => {
                this.overlay.setPosition([pointData.lng, pointData.lat]);
              }, 400)
            }, 250)
            // this.overlay.setPosition([pointData.lng, pointData.lat]);
          }
        }
      });
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
      this.map.removeLayer(this.redPointLayer);
      this.map.removeLayer(this.greenPointLayer);
      this.map.removeLayer(this.yellowPointLayer);
      this.map.removeLayer(this.ylTangPointLayer);
      this.map.removeLayer(this.elTangPointLayer);
      this.map.removeLayer(this.slTangPointLayer);
      this.map.removeLayer(this.cqwjdPointLayer);
      this.map.removeLayer(this.yndqPointLayer);
      this.map.removeLayer(this.jhPointLayer);
      this.map.removeLayer(this.linesLayer);
      this.map.removeLayer(this.selectPointLayer);
      this.map.removeLayer(this.selectLineLayer);
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
      const view = this.map.getView();
      view.animate({
        center: this.defaultCenter.lnglat,
        duration: 200,
        zoom: this.defaultCenter.zoom,
      });
    },

    // 设置地图位置
    _setView(lng, lat) {
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 15,
      });
    },


    // 设置弹窗
    _setPopup(pointData) {
      this.popupData = pointData;
      this.getPhotoDetailList(pointData.prcd, pointData.name);
      const hdms = toStringHDMS(toLonLat([pointData.lgtd, pointData.lttd])); // 转换坐标格式
      this.currentCoordinate = hdms; // 保存坐标点
      this.overlay.setPosition([pointData.lgtd, pointData.lttd]);
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
    _drawOverlays(pointData) {
      for (var i = 0; i < pointData.length; i++) {
        //新增放置overly的div
        jquery("#overly").after(
          `<div id = 'overly"+i+"' class='overlay'>"
            <div class="popup_content">
              <div class="popup_title">${popupData[i].coastalName} </div>
              <div class="popup_item">
                <p>预报潮位：</p>
                <span>${popupData[i].forecast ? popupData[i].forecast : "-"
          }</span>
              </div>
              <div class="popup_item">
                <p>时间：</p>
                <span>${popupData[i].forecastTime ? popupData[i].forecastTime : "-"
          }</span>
              </div>
            </div>
          </div>`
        );
        //新增overly
        this.markersLayer = new Overlay({
          offset: [-16, -10],
          positioning: "bottom-center",
          position: [pointData[i].lng, pointData[i].lat],
          element: document.getElementById("overly" + i),
        });
        this.map.addOverlay(this.markersLayer);
      }

      // for(var i = 0;i<pointData.length; i++){
      //   if(pointData[i].lat && pointData[i].lng){
      //     // this.overlay.setPosition([pointData[i].lng, pointData[i].lat]);
      //     const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
      //     this.currentCoordinate = hdms; // 保存坐标点
      //     this.overlay.setPosition([pointData[0].lng, pointData[0].lat]);
      //   }
      // }
    },
    _drawPoints(list, flag, type) {
      this.map.removeLayer(this.selectPointLayer);
      this.map.removeLayer(this.selectLineLayer);
      let scale = 0.8;
      // 点
      let src = require("@/assets/images/map/green-seawall.png"); //图片路径
      if (list.length === 1) {
        switch (list[0].threeYards) {
          case "绿色":
            src = require("@/assets/images/map/green-seawall.png"); //图片路径
            break;
          case "红色":
            src = require("@/assets/images/map/red-seawall.png"); //图片路径
            break;
          case "黄色":
            src = require("@/assets/images/map/yellow-seawall.png"); //图片路径
            break;
        }
      }

      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: src,
        title: "",
        scale: 0.4,
      };

      this.selectPointLayer = drawPoint(list, styleObj);
      this.selectPointLayer.setZIndex(999);
      this.map.addLayer(this.selectPointLayer);
      let that = this;
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
              list[i].lineColor = "#193CC5";
              break;
            case "2":
              list[i].lineColor = "#4C71FF";
              break;
            case "3":
              list[i].lineColor = "#7692FFFF";
              break;
            case "4":
              list[i].lineColor = "#ACB5FFFF";
              break;
            case "5":
              list[i].lineColor = "#E3E4FFFF";
              break;
            default:
              list[i].lineColor = "#E3E4FFFF";
              break;
          }
        }
      }
      this.linesLayer = drawLines(list);
      this.map.addLayer(this.linesLayer);
    },
    newMapPoints(coordinates, type) {
      let scale = 0.8;
      if (type == "cwyj") {
        scale = 0.4;
      }
      this.removeAllLayer();
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/gao_risk.png"), //图片路径
        title: "",
        scale: scale,
      };
      this.jhPointLayer = drawPoint(coordinates, styleObj, false, type);
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
    },
    newMapJh(coordinates, type) {
      // switch (type) {
      //   case "greenPointLayer":
      //     this.map.removeLayer(this.redPointLayer);
      //     break;
      //   case "redPointLayer":
      //     this.map.removeLayer(this.redPointLayer);
      //     break;
      //   default:
      //     this.map.removeLayer(this.greenPointLayer);
      //     break;
      // }
      console.log(type, 'LayerType');
      switch (type) {
        case 'redPointLayer':
        case 'yellowPointLayer':
        case 'greenPointLayer':
          this.map.removeLayer(this[type])
          break;
        case 'ylTangPointLayer':
        case 'elTangPointLayer':
        case 'slTangPointLayer':
        case 'yndqPointLayer':
        case 'cqwjdPointLayer':
        case 'ptSeawallPointLayer':
          this.removeAllLayer()
        default:
          this.map.removeLayer(this[type])
          break;
      }
      console.log(type, "LayerType");
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
        title: "",
        source: clusterSource,
        style: (feature) => {
          var size = feature.get("features").length;
          let src = require("@/assets/images/map/seawall-jh.png");
          if (size > 1) {
            switch (type) {
              case "ptSeawallPointLayer":
                src = require("@/assets/images/map/yndq-jh.png");
                break;
              case "redPointLayer":
                src = require("@/assets/images/map/red-seawall-jh.png");
                break;
              case "yellowPointLayer":
                src = require("@/assets/images/map/yellow-seawall-jh.png");
                break;
              case "greenPointLayer":
                src = require("@/assets/images/map/green-seawall-jh.png");
                break;
              case "ylTangPointLayer":
                src = require("@/assets/images/map/ylTang-jh.png");
                break;
              case "elTangPointLayer":
                src = require("@/assets/images/map/elTang-jh.png");
                break;
              case "slTangPointLayer":
                src = require("@/assets/images/map/slTang-jh.png");
                break;
              case "yndqPointLayer":
                src = require("@/assets/images/map/yndq-jh.png");
                break;
              case "cqwjdPointLayer":
                src = require("@/assets/images/map/cqwjd-jh.png");
                break;
              default:
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
                  color: "#fff",
                }),
                font: "normal 16px 微软雅黑",
                offsetX: -28,
                offsetY: 14,
                scale: 1,
              }),
            });
            styleCache[size] = style;
            return style;
          } else {
            let src = require("@/assets/images/map/red-seawall.png");
            // this.map.getView().getZoom() >= 14
            switch (type) {
              case "redPointLayer":
                src = require("@/assets/images/map/red-seawall.png");
                break;
              case "ptSeawallPointLayer":
                src = require("@/assets/images/map/yndq.png");
                break;
              case "yellowPointLayer":
                src = require("@/assets/images/map/yellow-seawall.png");
                break;
              case "greenPointLayer":
                src = require("@/assets/images/map/green-seawall.png");
                break;
              case "ylTangPointLayer":
                src = require("@/assets/images/map/ylTang.png");
                break;
              case "elTangPointLayer":
                src = require("@/assets/images/map/elTang.png");
                break;
              case "slTangPointLayer":
                src = require("@/assets/images/map/slTang.png");
                break;
              case "yndqPointLayer":
                src = require("@/assets/images/map/yndq.png");
                break;
              case "cqwjdPointLayer":
                src = require("@/assets/images/map/cqwjd.png");
                break;
              default:
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
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
    },

    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$parent.openLookPhotosPopup();
      // this.$parent.$refs.seawallRightPhoto.getPhotoDetailList(row.PRCD, row.NAME)
      // this.getPhotoDetailList(row.PRCD, row.NAME)
      this.$parent.showPhotosList(this.photoList, row.NAME);
    },

    // 弹窗内打开查看视频
    lookVideo(row) {
      this.$parent.openLookVideoPopup();
      this.$parent.$refs.seawallRightVideo.getVideoToken(row.cameraid);
    },

    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.openDetailPopup(row.prcd, row.name);
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
          console.log(res, "dasdasdsadsadsadsadsadsad");
          if (res.length) {
            let mydata = {
              name: res[0].name,
              code: res[0].tfid,
              points: [],
            };
            // let nowDate = new Date('2021-07-25 13:00:00')
            res[0].points.map((item, i) => {
              item.lat = Number(item.lat);
              item.lng = Number(item.lng);
              item.index = i;
              item.type = "live";
              mydata.points.push(item);
            });
            that.tfLng = res[0].points[res[0].points.length - 1].lng;
            that.tfLat = res[0].points[res[0].points.length - 1].lat;
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
.img-control {
  text-align: center;
  height: 100%;
  overflow: auto;
  padding-bottom: 5px;
}
.map_wrap {
  background: #fdfdf1;
  width: 100%;
  height: 100%;
  position: relative;
  .map {
    width: 100%;
    height: 100%;
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
    background: #fff;
    padding: 24px;
    @include shadow(#ccc, 0px, 0px, 2px);
    border-radius: 4px;
    .popup_content {
      color: #333;
      .popup_title {
        color: #000;
        font-size: 18px;
        font-weight: 500;
        padding-bottom: 4px;
      }
      .popup_item {
        padding-top: 16px;
        @include flexbox;
        @include flexAC;
        font-size: 16px;
        p {
          color: #666;
        }
        span {
          color: #000;
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
    background: #fff;
    padding: 24px;
    @include shadow(#ccc, 0px, 0px, 2px);
    border-radius: 4px;
    .popup_content {
      color: #333;
      .popup_title {
        color: #000;
        font-size: 18px;
        font-weight: 500;
        padding-bottom: 4px;
      }
      .popup_item {
        padding-top: 16px;
        @include flexbox;
        @include flexAC;
        font-size: 16px;
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
</style>