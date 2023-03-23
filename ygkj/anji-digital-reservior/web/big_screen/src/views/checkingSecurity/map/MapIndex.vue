<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-12 09:56:52
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyMap\PropertyMap.vue
-->
<template>
  <div class="map_wrap">
    <!-- <div class="screen_title">
      <div class="screen_title_box">
        <img src="../../../../assets/images/title_icon.png" alt="" />
        水库风险研判
        <span>beta版</span>
      </div>
      <img
        class="haveTF"
        v-if="hasTf"
        src="../../../../assets/images/haveTF.gif"
        alt=""
        @click="showTf"
      />
    </div> -->
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <!-- <div class="popup_map" ref="popup" v-show="currentCoordinate">
      <div
        class="proj_content"
        v-show="popupData.rightType == '1' && !popupData.hoverType"
      >
        <div class="dialog_title">
          <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span> -
          <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
          <i class="el-icon-close" @click="closePopup()"></i>
        </div>
        <div class="proj_top">
          <div class="proj_left">
            <div class="proj_title" @click="lookDetail(popupData)">
              {{ popupData.NAME }}
              <span v-if="popupData.project_scale == '1'"> &nbsp[大(1)型]</span>
              <span v-if="popupData.project_scale == '2'"> &nbsp[大(2)型]</span>
              <span v-if="popupData.project_scale == '3'"> &nbsp[中型]</span>
              <span v-if="popupData.project_scale == '4'">&nbsp [小(1)型]</span>
              <span v-if="popupData.project_scale == '5'"> &nbsp[小(2)型]</span>
            </div>
            <div class="proj_detail">
              <div class="popup_item">
                <p>水位：</p>
                <span
                  >{{
                    popupData.waterLevel
                      ? popupData.waterLevel.toFixed(2) + "m"
                      : "- "
                  }} </span
                >&nbsp
                <div v-if="parseFloat(popupData.waterChange)">
                  (
                  <span>{{
                    popupData.waterChange
                      ? popupData.waterChange.toFixed(2)
                      : popupData.waterChange
                  }}</span>
                  <span v-if="popupData.waterChange"
                    ><i
                      class="el-icon-top"
                      style="color: green;font-size:16px"
                      v-if="
                        popupData.waterChange > 0 ||
                          popupData.waterChange == null
                      "
                    ></i>
                    <i
                      class="el-icon-bottom"
                      style="color: red;font-size:16px"
                      v-else
                    ></i></span
                  >)
                </div>
              </div>
              <div class="popup_item">
                <p>时间：</p>
                <span>{{
                  popupData.waterleveltm
                    ? popupData.waterleveltm.slice(5, 16)
                    : "-"
                }}</span>
              </div>
              <div class="popup_item">
                <p>总库容：</p>
                <span>{{
                  popupData.TATOL_STORAGE
                    ? popupData.TATOL_STORAGE + "万m³"
                    : "-"
                }}</span>
              </div>
              <div class="popup_item">
                <p>集雨面积：</p>
                <span>{{ popupData.RCA ? popupData.RCA + "km²" : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>坝型：</p>
                <span>{{ popupData.DTBS ? popupData.DTBS : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>安全鉴定：</p>
                <span
                  v-if="
                    popupData.status && popupData.status.slice(0, 3) == '三类坝'
                  "
                  >{{ popupData.status.slice(0, 3) }}</span
                >
                <span v-else>{{
                  popupData.status ? popupData.status : "-"
                }}</span>
              </div>
              <div
                class="popup_item"
                v-if="
                  popupData.status && popupData.status.slice(0, 3) == '三类坝'
                "
              >
                <p>除险加固：</p>
                <span>{{
                  popupData.status ? popupData.status.slice(3) : "-"
                }}</span>
              </div>
              <div
                class="popup_item"
                v-if="popupData.rightPopType && popupData.rightPopType == 'xsl'"
              >
                <p>蓄水率：</p>
                <span>{{ popupData.xsl ? popupData.xsl : "-" }}</span>
              </div>
              <div
                class="popup_item"
                v-if="
                  popupData.rightPopType && popupData.rightPopType == 'kgkn'
                "
              >
                <p>可纳雨：</p>
                <span>{{
                  popupData.rainContains
                    ? popupData.rainContains.toFixed(2) + "mm"
                    : "0"
                }}</span>
              </div>
              <div
                class="popup_item"
                v-if="
                  popupData.rightPopType && popupData.rightPopType == 'kgkn'
                "
              >
                <p>可纳洪：</p>
                <span>{{
                  popupData.knx ? popupData.knx + popupData.knxUnit : "0"
                }}</span>
              </div>

              <div
                class="popup_item"
                v-if="
                  popupData.rightPopType && popupData.rightPopType == 'kgkn'
                "
              >
                <p>可供水：</p>
                <span>{{
                  popupData.kgs ? popupData.kgs + popupData.kgsUnit : "0"
                }}</span>
              </div>
            </div>
          </div>
          <div class="proj_right">
            <div class="proj_tabs">
              <div
                :class="popupTabsIndex == 0 ? 'active' : ''"
                @click="clickTabs(0, 1)"
              >
                <div>
                  <img
                    v-if="popupTabsIndex == 0"
                    src="../../../../assets/images/image0.png"
                    alt=""
                  />
                  <img
                    v-else
                    src="../../../../assets/images/image1.png"
                    alt=""
                  />
                </div>
                照片
              </div>
              <div
                :class="[
                  popupTabsIndex == 1 ? 'active' : '',
                  videoList.length ? '' : 'notClick',
                ]"
                @click="clickTabs(1, videoList.length)"
              >
                <div>
                  <img
                    v-if="popupTabsIndex == 1"
                    src="../../../../assets/images/yx0.png"
                    alt=""
                  />
                  <img v-else src="../../../../assets/images/yx1.png" alt="" />
                </div>
                影像
              </div>
              <div
                :class="[
                  popupTabsIndex == 2 ? 'active' : '',
                  cameraList.length ? '' : 'notClick',
                ]"
                @click="clickTabs(2, cameraList.length)"
              >
                <div>
                  <img
                    v-if="popupTabsIndex == 2"
                    src="../../../../assets/images/video0.png"
                    alt=""
                  />
                  <img
                    v-else
                    src="../../../../assets/images/video1.png"
                    alt=""
                  />
                </div>
                视频
              </div>
            </div>
            <div
              v-show="popupTabsIndex == 0"
              style="cursor: pointer"
              v-loading="loadingImg"
              @click="lookPhotos(popupData)"
            >
              <div v-if="photoList.length" class="img_box">
                <el-carousel
                  ref="carousel"
                  class="carousel_box"
                  indicator-position="none"
                  arrow="always"
                  :autoplay="false"
                  @change="changeItem"
                >
                  <el-carousel-item
                    class="carousel_item"
                    v-for="(item, index) in photoList"
                    :key="index"
                  >
                    <div
                      class="swiper_img"
                      :style="{ backgroundImage: 'url(' + item.smallUrl + ')' }"
                      :title="item.name"
                    ></div>
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
            <div v-show="popupTabsIndex == 1" v-loading="loadingVideo">
              <div v-if="videoList.length" class="img_box">
                <el-carousel
                  ref="carousel"
                  class="carousel_box"
                  indicator-position="none"
                  arrow="always"
                  :autoplay="false"
                  @change="changeVideoItem"
                >
                  <el-carousel-item
                    class="carousel_item"
                    v-for="(item, index) in videoList"
                    :key="index"
                  >
                    <video
                      class="swiper_video"
                      :src="item.url"
                      controls
                    ></video>
                  </el-carousel-item>
                </el-carousel>
                <div class="preview_words">
                  <span>{{ videoIndex + 1 }}</span> / {{ videoList.length }}
                </div>
              </div>
              <div v-if="!loadingVideo && !videoList.length" class="img_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
                <p>暂无视频影像</p>
              </div>
              <div v-if="loadingVideo" class="img_loading">
                <p>视频影像加载中...</p>
              </div>
            </div>
            <div v-show="popupTabsIndex == 2" style="cursor: pointer">
              <div class="camera_box" @click="lookCamera(popupData)">
                <img src="../../../../assets/images/sk-camera.png" alt="" />
              </div>
            </div>
          </div>
        </div>
        <div class="proj_bottom">
          <div class="person_list" v-loading="!loadingPerson">
            <div class="person_item">
              <div class="person_item_top">
                <h4>政府责任人</h4>
                <img src="../../../../assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveGovName }}</p>
              <span>{{ personData.executiveGovPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>水行政主管部门负责人</h4>
                <img src="../../../../assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveWaterName }}</p>
              <span>{{ personData.executiveWaterPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>主管部门产权责任人</h4>
                <img src="../../../../assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveDepartName }}</p>
              <span>{{ personData.executiveDepartPhone }}</span>
            </div>
            <div class="person_item" v-if="personData.ifDepartment == '有'">
              <div class="person_item_top">
                <h4>管理单位责任人</h4>
                <img src="../../../../assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveManageName }}</p>
              <span>{{ personData.executiveManagePhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>技术负责人</h4>
                <img src="../../../../assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.technicalName }}</p>
              <span>{{ personData.techinicalPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>巡查负责人</h4>
                <img src="../../../../assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.patrolName }}</p>
              <span>{{ personData.patrolPhone }}</span>
            </div>
          </div>
        </div>
      </div>
      <div
        class="popup_content"
        v-show="popupData.rightType == '2' && !popupData.hoverType"
      >
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>安全等级：</p>
          <span>{{ popupData.status ? popupData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>规模：</p>
          <span v-if="popupData.project_scale == '1'">大(1)型</span>
          <span v-if="popupData.project_scale == '2'">大(2)型</span>
          <span v-if="popupData.project_scale == '3'">中型</span>
          <span v-if="popupData.project_scale == '4'">小(1)型</span>
          <span v-if="popupData.project_scale == '5'">小(2)型</span>
        </div>
      </div>
      <div
        class="popup_content"
        v-show="popupData.rightType == '3' && !popupData.hoverType"
      >
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>安全等级：</p>
          <span>{{ popupData.status ? popupData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>总库容：</p>
          <span>{{
            popupData.TATOL_STORAGE ? popupData.TATOL_STORAGE + "万m³" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>水位：</p>
          <span>{{
            popupData.waterLevel ? popupData.waterLevel.toFixed(2) + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>可纳水量：</p>
          <span>{{
            popupData.addtion ? popupData.addtion + "万m³" : "-"
          }}</span>
        </div>
      </div>
      <div
        class="popup_content"
        v-show="popupData.rightType == '8' && !popupData.hoverType"
      >
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>安全状态：</p>
          <span>{{ popupData.status ? popupData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>规模：</p>
          <span v-if="popupData.project_scale == '1'">大(1)型</span>
          <span v-if="popupData.project_scale == '2'">大(2)型</span>
          <span v-if="popupData.project_scale == '3'">中型</span>
          <span v-if="popupData.project_scale == '4'">小(1)型</span>
          <span v-if="popupData.project_scale == '5'">小(2)型</span>
        </div>
        <div class="popup_item">
          <p>正常水位：</p>
          <span>{{
            popupData.NWL ? popupData.NWL.toFixed(2) + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>当前水位：</p>
          <span>{{
            popupData.waterLevel ? popupData.waterLevel.toFixed(2) + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>时间：</p>
          <span>{{
            popupData.waterleveltm ? popupData.waterleveltm.slice(5, 16) : "-"
          }}</span>
        </div>
      </div>

      <div
        class="popup_content"
        v-show="popupData.rightType == '5' && !popupData.hoverType"
      >
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
          {{ popupData.NAME }}
        </div>
        <div class="popup_item">
          <p>安全状态：</p>
          <span>{{ popupData.status ? popupData.status : "-" }}</span>
        </div>
        <div class="popup_item">
          <p>规模：</p>
          <span v-if="popupData.project_scale == '1'">大(1)型</span>
          <span v-if="popupData.project_scale == '2'">大(2)型</span>
          <span v-if="popupData.project_scale == '3'">中型</span>
          <span v-if="popupData.project_scale == '4'">小(1)型</span>
          <span v-if="popupData.project_scale == '5'">小(2)型</span>
        </div>
        <div class="popup_item">
          <p>正常水位：</p>
          <span>{{
            popupData.NWL ? popupData.NWL.toFixed(2) + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>当前水位：</p>
          <span>{{
            popupData.waterLevel ? popupData.waterLevel.toFixed(2) + "m" : "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>时间：</p>
          <span>{{
            popupData.waterleveltm ? popupData.waterleveltm.slice(5, 16) : "-"
          }}</span>
        </div>
      </div>
      <div class="popup_content" v-show="popupData.hoverType == 1">
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
          {{ popupData.NAME }}
        </div>
      </div>
    </div> -->
    <div
      class="popup_map_hover"
      ref="hoverPopup"
      v-show="currentCoordinateHover"
    >
      <div class="popup_content">
        <div class="popup_title">
          {{ popupDataHover.NAME }}
        </div>
      </div>
    </div>
    <div id="overly"></div>
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


// import {
//   reservoirPhotoListApi,
//   reservoirVideoListApi,
//   reservoirCameraListApi,
//   reservoirPersonListApi,
//   getFile,
//   getTyInfoApi,
//   getTyListApi,
// } from "@/api/api_reservoir";

export default {
  name: "",
  components: {},
  data() {
    return {
      //地图
      serviceLink: "http://223.4.72.162:16080/arcgis/rest/services/",
      pointLayer: null,
      pointLayers: {},
      linesLayer: {},
      jhPointLayer: {},
      selectPointLayer: {},
      selectLineLayer: {},
      adcdPolygonLayer: {},
      map: null,
      pjObj: {},
      pjObjList: [],
      overlay: null,
      hoverOverlay: null,
      currentCoordinate: null,
      currentCoordinateHover: null,
      popupData: {},
      popupDataHover: {},
      defaultCenter: {
        lnglat: [120.4997109415048, 29.22000109695038],
        zoom: 8.3,
      },
      markersLayer: {},
      pointBulingbuling: null,
      //轮播图弹窗数据
      photoList: [],
      imgIndex: 0,
      loadingImg: true,
      videoList: [],
      videoIndex: 0,
      loadingVideo: true,
      popupTabsIndex: 0,
      cameraList: [],
      personData: {},
      loadingPerson: true,
      typhoonsMap: new Map(),
      hasTf: false,
      tfLng: "",
      tfLat: "",
    };
  },
  computed: {
    leftSelectAdnm() {
      return this.$store.state.leftSelectAdnm;
    },
  },
  watch: {
  },
  created() {},
  mounted() {
    this.mapInit();
  },
  methods: {
    //初始化地图
    mapInit() {
      // 点击弹窗
      // this.overlay = new Overlay({
      //   element: this.$refs.popup, // 弹窗标签，在html里
      //   autoPan: true, // 如果弹窗在底图边缘时，底图会移动
      //   autoPanAnimation: {
      //     // 底图移动动画
      //     duration: 250,
      //   },
      //   positioning: "bottom-center",
      //   offset: [-16, -10],
      // });

      // hover弹窗
      // this.hoverOverlay = new Overlay({
      //   element: this.$refs.hoverPopup, // 弹窗标签，在html里
      //   autoPan: true, // 如果弹窗在底图边缘时，底图会移动
      //   autoPanAnimation: {
      //     // 底图移动动画
      //     duration: 250,
      //   },
      //   positioning: "bottom-center",
      //   offset: [-10, -4],
      // });

      // 地图
      this.map = new Map({
        target: "mainView",
        //地图控件
        // controls: defaultControls({ attribution: true, zoom: true }),
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023",
              url:
                "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          new Tile({
            source: new XYZ({
              url:
                "https://sldtptgis.zjwater.com/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
              projection: "EPSG:4326", //使用这个坐标系
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          // new Tile({
          //   source: new XYZ({
          //     url: "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/basemap/WYX1029/MapServer/tile/{z}/{y}/{x}",
          //     projection: "EPSG:4326", //使用这个坐标系
          //     // crossOrigin: "anonymous",
          //   }),
          //   preload: 1,
          //   visible: true,
          // }),
        ],
        // overlays: [this.overlay, this.hoverOverlay], // 把弹窗加入地图
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0,
        }),
      });

      // 禁用地图双击缩放
      // const dblClickInteraction = this.map
      //   .getInteractions()
      //   .getArray()
      //   .find((interaction) => {
      //     return interaction instanceof DoubleClickZoom;
      //   });
      // this.map.removeInteraction(dblClickInteraction);

      // 监听事件
      //this.mapClick(); // 初始化地图成功后，给地图添加点击事件

      // 鼠标移入
      // this.map.on("pointermove", (e) => {
      //   var pixel = this.map.getEventPixel(e.originalEvent);
      //   var hit = this.map.hasFeatureAtPixel(pixel);
      //   var feature = this.map.forEachFeatureAtPixel(pixel, function(feature) {
      //     return feature;
      //   });
      //   if (hit) {
      //     this.map.getTargetElement().style.cursor = "pointer";
      //     let popupDataHover = {};
      //     if (
      //       feature &&
      //       feature.values_.features &&
      //       feature.values_.features.length > 0
      //     ) {
      //       if (feature.values_.features.length == 1) {
      //         popupDataHover = feature.values_.features[0].values_.data;
      //       } else {
      //         return false;
      //       }
      //     } else {
      //       if (feature.values_.data) {
      //         popupDataHover = feature.values_.data;
      //       } else {
      //         return false;
      //       }
      //     }
      //     this.popupDataHover = popupDataHover;
      //     const hdms = toStringHDMS(
      //       toLonLat([popupDataHover.lng, popupDataHover.lat])
      //     ); // 转换坐标格式
      //     this.currentCoordinateHover = hdms; // 保存坐标点
      //     setTimeout(() => {
      //       this.hoverOverlay.setPosition([
      //         popupDataHover.lng,
      //         popupDataHover.lat,
      //       ]);
      //     });
      //   } else {
      //     this.closeHoverPopup();
      //     this.map.getTargetElement().style.cursor = "";
      //   }
      // });
    },
  },
};
</script>

<style lang="scss" scoped>
.map_wrap {
  background: #fdfdf1;
  width: 100%;
  height: 100%;
  position: relative;
  .map {
    width: 100%;
    height: 100%;
  }
}
</style>
