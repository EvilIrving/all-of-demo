<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-11 11:20:18
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\sgzgScreen\storageSupply\storageSupplyMap\StorageSupplyMap.vue
-->
<template>
  <div class="map_wrap">
    <div class="screen_title">
      <!-- <img
        class="haveTF"
        v-if="hasTf"
        src="../../../../assets/images/haveTF.gif"
        alt=""
        @click="showTf"
      /> -->
      <!-- <img
        class="noTF"
        v-else
        src="../../../../assets/images/noTF.png"
        alt=""
      /> -->
    </div>
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate">
      <!-- 工程气泡 有照片视频 -->
      <div
        class="proj_content"
        v-show="popupData.rightType == '1' && !popupData.hoverType"
      >
        <div class="dialog_title">
          <!-- 水库信息 -->
          <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span> -
          <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
          <i class="el-icon-close" @click="closePopup()"></i>
        </div>
        <!-- 上部分 -->
        <div class="proj_top">
          <!-- 上部分-左侧 -->
          <div class="proj_left">
            <!-- 内容 -->
            <div class="proj_title" @click="lookDetail(popupData)">
              {{ popupData.NAME }}
              <span v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
              <span v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
              <span v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
              <span v-if="popupData.project_scale == '4'" v-html="'&nbsp [小(1)型]'"></span>
              <span v-if="popupData.project_scale == '5'" v-html="'&nbsp [小(2)型]'"></span>
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
                >
                <!-- 括号填写是否上升及数字 -->
                <div v-if="parseFloat(popupData.waterChange)">
                  <span v-html="'&nbsp'"></span>(
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
                <!-- 加个箭头 -->
              </div>
              <div class="popup_item">
                <p>时间：</p>
                <span>{{
                  popupData.waterleveltm
                    ? popupData.waterleveltm.slice(5, 16)
                    : "-"
                }}</span>
              </div>
              <!-- <div class="popup_item">
                <p>规模：</p>
                <span v-if="popupData.project_scale == '1'">大(1)型</span>
                <span v-if="popupData.project_scale == '2'">大(2)型</span>
                <span v-if="popupData.project_scale == '3'">中型</span>
                <span v-if="popupData.project_scale == '4'">小(1)型</span>
                <span v-if="popupData.project_scale == '5'">小(2)型</span>
              </div> -->
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

              <!-- <div class="popup_item">
                <p>所在市：</p>
                <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>所在县：</p>
                <span>{{
                  popupData.countryADNM ? popupData.countryADNM : "-"
                }}</span>
              </div> -->
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
              <!-- 预测预警 -->
              <div class="popup_item" v-if="popupData.isYcyj">
                <p>{{popupData.overName}}：</p>
                <span>{{ popupData.overValue ? popupData.overValue.toFixed(2) + 'm' : '0' }}</span>
              </div>
              <div class="popup_item" v-if="popupData.isYcyj">
                <p>预报水位：</p>
                <span style="color:#ff0000">{{ popupData.ybWater ? popupData.ybWater.toFixed(2) + 'm' : '0' }}</span>
              </div>
            </div>
          </div>
          <!-- 上部分-右侧 -->
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
            <!-- 照片 -->
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
            <!-- 影像 -->
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
            <!-- 视频 -->
            <div v-show="popupTabsIndex == 2" style="cursor: pointer">
              <div class="camera_box" @click="lookCamera(popupData)">
                <img src="../../../../assets/images/sk-camera.png" alt="" />
              </div>
            </div>
          </div>
        </div>
        <!-- 下部分 -->
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

      <!-- 病险水库 -->
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

      <!-- 纳蓄能力 -->
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

      <!-- 实时预警 -->
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

      <!-- 预测预警 -->
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

      <!-- hover -->
      <div class="popup_content" v-show="popupData.hoverType == 1">
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
          {{ popupData.NAME }}
        </div>
      </div>
    </div>
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
import Measure from "@/utils/measure";
import jquery from "@/plugins/jquery-1.11.3";

import axios from "axios";

import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  addLayerClick,
  newTyphoon,
} from "@/utils/mapUtil";

import {
  reservoirPhotoListApi,
  reservoirVideoListApi,
  reservoirCameraListApi,
  reservoirPersonListApi,
  getFile,
  reservoirLeftStatisticApi
} from "@/api/api_reservoir";

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
        lnglat: [120.09841796875, 29.169012539062494],
        zoom: 8.4,
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
    leftSelectAdnm(val) {
      this.getAdcdPointsData(val);
    },
  },
  created() {},
  mounted() {
    this.mapInit();
    // setTimeout(() => {
      // this.showTyphoon();
    // }, 1000);
    let userInfo = this.$localData("get", "userInfo");
    this.getAdcdPointsData(userInfo.adnm);
    this.getMapFloowData()
  },
  methods: {
    //初始化地图
    mapInit() {
      // 点击弹窗
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

      // hover弹窗
      this.hoverOverlay = new Overlay({
        element: this.$refs.hoverPopup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [-10, -4],
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
              // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              url:
                "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
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
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          //临时改动，供截图使用
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
        overlays: [this.overlay, this.hoverOverlay], // 把弹窗加入地图
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

      // 鼠标移入
      this.map.on("pointermove", (e) => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        var feature = this.map.forEachFeatureAtPixel(pixel, function(feature) {
          return feature;
        });
        if (hit) {
          this.map.getTargetElement().style.cursor = "pointer";
          let popupDataHover = {};
          if (
            feature &&
            feature.values_.features &&
            feature.values_.features.length > 0
          ) {
            if (feature.values_.features.length == 1) {
              popupDataHover = feature.values_.features[0].values_.data;
            } else {
              return false;
            }
          } else {
            if (feature.values_.data) {
              popupDataHover = feature.values_.data;
            } else {
              return false;
            }
          }
          this.popupDataHover = popupDataHover;
          const hdms = toStringHDMS(
            toLonLat([popupDataHover.lng, popupDataHover.lat])
          ); // 转换坐标格式
          this.currentCoordinateHover = hdms; // 保存坐标点
          setTimeout(() => {
            this.hoverOverlay.setPosition([
              popupDataHover.lng,
              popupDataHover.lat,
            ]);
          });
        } else {
          this.closeHoverPopup();
          this.map.getTargetElement().style.cursor = "";
        }
      });
    },
    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function(feature) {
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
            let pointData = feature.values_.features[0].values_.data;
            const coordinate = evt.coordinate; // 获取坐标
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            this.popupData = pointData;
            this.popupData.hoverType = "";
            if (pointData.rightType == "1") {
              this.popupTabsIndex = 0;
              this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
              this.getVideoDetailList(pointData.PRCD);
              this.getCameraList(pointData.PRCD);
              this.getPersonList(pointData.PRCD);
            }
            this._setView1(pointData.LGTD, pointData.LTTD);
            setTimeout(() => {
              this._setView(
                Number(pointData.LGTD) + 0.004,
                Number(pointData.LTTD) + 0.013
              );
            }, 300);

            setTimeout(() => {
              this.overlay.setPosition([pointData.lng, pointData.lat]);
            });
          } else {
            // 点击聚合图标时
            this.map.getView().fit(feature.get("features")[0].getGeometry(), {
              maxZoom: this.map.getView().getZoom() + 1,
              duration: 250,
            });
          }
        } else {
          let pointData = feature.values_.data;
          this.popupData = pointData;
          this.popupData.hoverType = "";
          const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
          this.currentCoordinate = hdms; // 保存坐标点
          if (pointData.rightType == "1") {
            this.popupTabsIndex = 0;
            this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
            this.getVideoDetailList(pointData.PRCD);
            this.getCameraList(pointData.PRCD);
            this.getPersonList(pointData.PRCD);
          }

          this._setView1(pointData.LGTD, pointData.LTTD);
          setTimeout(() => {
            this._setView(
              Number(pointData.LGTD) + 0.004,
              Number(pointData.LTTD) + 0.013
            );
          }, 300);

          setTimeout(() => {
            this.overlay.setPosition([pointData.lng, pointData.lat]);
          });
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
      this.map.removeLayer(this.jhPointLayer);
      this.map.removeLayer(this.linesLayer);
      this.map.removeLayer(this.selectPointLayer);
      this.map.removeLayer(this.selectLineLayer);
      // this.map.removeLayer(this.adcdPolygonLayer);
    },

    removePolygon() {
      this.map.removeLayer(this.adcdPolygonLayer);
      return true;
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
    _setView1(lng, lat, noBig) {
      const to = [lng, lat];
      const view = this.map.getView();
      let obj = {
        center: to,
        duration: 0,
      };
      if (!noBig) {
        obj.zoom = 15;
      }
      view.animate(obj);
    },
    _setView(lng, lat, noBig) {
      const to = [lng, lat];
      const view = this.map.getView();
      let obj = {
        center: to,
        duration: 0,
      };
      if (!noBig) {
        obj.zoom = 15;
      }
      view.animate(obj);
    },

    // 设置弹窗
    _setPopup(pointData) {
      this.popupData = pointData;
      if (pointData.rightType == "1") {
        this.popupTabsIndex = 0;
        this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
        this.getVideoDetailList(pointData.PRCD);
        this.getCameraList(pointData.PRCD);
        this.getPersonList(pointData.PRCD);
      }
      const hdms = toStringHDMS(toLonLat([pointData.LGTD, pointData.LTTD])); // 转换坐标格式
      this.currentCoordinate = hdms; // 保存坐标点
      this.overlay.setPosition([pointData.LGTD, pointData.LTTD]);
    },

    // 右下角zoom加减
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
    // 生成多个气泡
    _drawOverlays(pointData) {
      for (var i = 0; i < pointData.length; i++) {
        //新增放置overly的div
        jquery("#overly").after(
          `<div id='overly${i}'>
            <div class='adcdOverlay'>
              <div class="adcd_top">
                ${pointData[i].adnm}
              </div>
              <div class="adcd_bottom">
                <div>
                  <i></i>
                  <p>蓄水量：<span>${pointData[i].currenty ? (pointData[i].currenty / 10000).toFixed(2) : 0}亿方</span></p>
                </div>
                <div>
                  <i></i>
                  <p>可供水：<span>${pointData[i].supply ? (pointData[i].supply / 10000).toFixed(2) : 0}亿方</span></p>
                </div>
                <div>
                  <i></i>
                  <p>可纳洪：<span>${pointData[i].addtion ? (pointData[i].addtion / 10000).toFixed(2) : 0}亿方</span></p>
                </div>
              </div>
            </div>
            <div class="adcd_name">${pointData[i].adnm}</div>
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
    },

    // 点位选中闪烁
    _drawPoints(list) {
      this.map.removeLayer(this.selectPointLayer);
      this.map.removeLayer(this.selectLineLayer);
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/reservoir-select.png"), //图片路径
        title: "",
        scale: 0.8,
      };

      this.selectPointLayer = drawPoint(list, styleObj);
      this.selectPointLayer.setZIndex(999);
      this.map.addLayer(this.selectPointLayer);
      let that = this;
      this.pointBulingbuling = setInterval(() => {
        // that.map.addLayer(that.selectPointLayer);
        that.selectPointLayer.setVisible(false);
        setTimeout(() => {
          that.selectPointLayer.setVisible(true);
          // that.map.removeLayer(that.selectPointLayer);
        }, 300);
      }, 600);
      setTimeout(() => {
        clearInterval(that.pointBulingbuling);
        setTimeout(() => {
          that.map.removeLayer(that.selectPointLayer);

          // that.map.addLayer(that.selectPointLayer);
          // that.selectPointLayer.setVisible(true)
        }, 300);
      }, 3300);
    },

    //打点不聚合
    newMapPoints(coordinates) {
      this.removeAllLayer();
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/reservoir.png"), //图片路径
        title: "",
        scale: 1,
      };
      this.jhPointLayer = drawPoint(coordinates, styleObj, true, "reservoir");
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
    },

    //打点聚合
    newMapJh(coordinates, type) {
      this.removeAllLayer();
      let vectorSource = new VectorSource({});
      let clusterSource = new ClusterSource({
        // distance: this.jhFlag ? 100 : 0,
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
      this.jhPointLayer = new VectorLayer({
        title: "",
        source: clusterSource,
        style: (feature) => {
          var size = feature.get("features").length;
          if (size > 1) {
            let featureData = feature.get("features")[0].values_.data;
            let src = require("@/assets/images/map/reservoir-jh.png");
            if (type == "video") {
              src = require("@/assets/images/map/video_jh.png");
            }
            if (type == "sscw-red" || type == "cwyj") {
              src = require("@/assets/images/map/reservoir-jh-red.png");
            } else if (type == "sscw-orange") {
              src = require("@/assets/images/map/reservoir-jh-orange.png");
            } else if (type == "sscw-blue") {
              src = require("@/assets/images/map/reservoir-jh.png");
            }
            var style = new Style({
              image: new Icon({
                src: src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: 0.8,
              }),
              text: new Text({
                textAlign: "center",
                textBaseline: "middle",
                font: "normal 12px 微软雅黑",
                text: size.toString(),
                fill: new Fill({
                  color: "#fff",
                }),
                // radius: 10,
                offsetX: -13,
                offsetY: 13,
                // backgroundStroke: new Stroke({
                //   color: "#fff",
                //   width: 1,
                // }),
                scale: 1,
                // backgroundFill: new Fill({
                //   color: "#1F1F1F",
                // }),
                // padding: [6, 6, 3, 8],
              }),
            });
            styleCache[size] = style;
            return style;
          } else {
            let featureData = feature.get("features")[0].values_.data;
            let src = require("@/assets/images/map/reservoir.png");
            let scale = 0.8;
            if (type == "video") {
              if (featureData.state == "ON") {
                src = require("@/assets/images/map/video_on.png");
              } else {
                src = require("@/assets/images/map/video_off.png");
              }
            }
            if (type == "sscw-red" || type == "cwyj") {
              src = require("@/assets/images/map/reservoir-red.png");
            } else if (type == "sscw-orange") {
              src = require("@/assets/images/map/reservoir-orange.png");
            } else if (type == "sscw-blue") {
              src = require("@/assets/images/map/reservoir.png");
            }
            if (type == "all") {
              scale = 1;
              switch (featureData.colorType) {
                case "green":
                  src = require("@/assets/images/map/reservoir-green4.png");
                  break;
                case "blue":
                  src = require("@/assets/images/map/reservoir-blue4.png");
                  break;
                case "yellow":
                  src = require("@/assets/images/map/reservoir-yellow4.png");
                  break;
                case "orange":
                  src = require("@/assets/images/map/reservoir-orange4.png");
                  break;
                case "red":
                  src = require("@/assets/images/map/reservoir-red4.png");
                  break;
              }
            }

            let name = "";
            // this.map.getView().getZoom() >= 14
            let style = new Style({
              image: new Icon({
                src: src,
                anchor: [1, 1],
                anchorXUnits: "fraction",
                anchorYUnits: "pixels",
                scale: scale,
              }),
            });
            return style;
          }
        },
      });
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
    },
    // 获取各市蓄供数据
    async getMapFloowData(){
      let res = await reservoirLeftStatisticApi({
        statistic: "capacity",
        adcd: this.$localData("get", "userInfo").adcd,
      });
      if(res.data.length){
        let pointsData = []
        for (let i = 0; i < res.data.length; i++) {
          switch (res.data[i].adnm){
            case '杭州市':
              res.data[i].lng = '119.9'
              res.data[i].lat = '30.1'
              break;
            case '宁波市':
              res.data[i].lng = '121.5'
              res.data[i].lat = '29.44'
              break;
            case '温州市':
              res.data[i].lng = '120.42'
              res.data[i].lat = '27.6'
              break;
            case '湖州市':
              res.data[i].lng = '120'
              res.data[i].lat = '30.75'
              break;
            case '绍兴市':
              res.data[i].lng = '120.7'
              res.data[i].lat = '29.65'
              break;
            case '金华市':
              res.data[i].lng = '119.88'
              res.data[i].lat = '29.25'
              break;
            case '丽水市':
              res.data[i].lng = '120.1'
              res.data[i].lat = '28.39'
              break;
            case '衢州市':
              res.data[i].lng = '119'
              res.data[i].lat = '28.7'
              break;
            case '舟山市':
              res.data[i].lng = '122.2'
              res.data[i].lat = '30'
              break;
            case '台州市':
              res.data[i].lng = '121.25'
              res.data[i].lat = '28.39'
              break;
            case '丽水市':
              res.data[i].lng = '119.3'
              res.data[i].lat = '27.6'
              break;
          }
          if(res.data[i].adnm != '省级'){
            pointsData.push(res.data[i])
          }
        }
        this._drawOverlays(pointsData)
      }
    },

    // 获取市范围线数据
    async getAdcdPointsData(adnm) {
      this.map.removeLayer(this.adcdPolygonLayer);
      // if (this.removePolygon()) {
      if (adnm == "浙江省") {
        return;
      }
      let that = this;
      let url =
        "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/BOU/BOU_City/MapServer/0/query";
      jquery.ajax({
        url: url,
        type: "get",
        dataType: "jsonp",
        data: {
          returnGeometry: true,
          where: "NAME='" + adnm + "'",
          outSR: "4326",
          outFields: "*",
          f: "json",
        },
        // complete: () => {
        //   this.map.removeLayer(this.adcdPolygonLayer);
        // },
        success: function(res) {
          let arr = res.features[0].geometry.rings;
          // setTimeout(() => {
          //    that.map.removeLayer(that.adcdPolygonLayer)
          // }, 300);
          that.adcdPolygonLayer = drawPolygon(arr);
          that.map.addLayer(that.adcdPolygonLayer);
        },
      });

      // let pointerArr = [[[119.54258, 29.849864],[119.492776, 29.934794],[119.854019, 30.44297]]]
      // this.adcdPolygonLayer = drawPolygon(pointerArr)
      // this.map.addLayer(this.adcdPolygonLayer);
      // }
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

    // 关闭点击弹窗
    closePopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
    },

    // 关闭hover弹窗
    closeHoverPopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.hoverOverlay.setPosition(undefined);
      this.currentCoordinateHover = null;
    },

    // 弹窗内打开查看图片
    lookPhotos(row) {
      this.$parent.openLookPhotosPopup();

      this.$parent.showPhotosList(this.photoList, row.NAME);
    },

    // 弹窗内打开查看视频
    lookVideo(row) {
      this.$parent.openLookVideoPopup();
      this.$parent.$refs.reservoirRightVideo.getVideoToken(row.cameraid);
    },

    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.openDetailPopup(row);
    },

    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
    },
    // 轮播图弹窗方法
    changeVideoItem(index) {
      this.videoIndex = index;
    },

    //获取工程图片
    async getPhotoDetailList(prcd, reservoirName) {
      this.imgIndex = 0;
      this.photoList = [];
      this.loadingImg = true;
      let res = await reservoirPhotoListApi({
        viewType: "latestAll",
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        // let arr = [];
        // if (res.rows.length) {
        //   res.rows.map((v) => {
        //     arr.push(v.extphoto6);
        //   });
        //   this.getFileList(arr.join(","), reservoirName);
        // }
        let options = [
          {
            name: `${res.rows[0].damBehind}`,
            url: `${res.rows[0].damBehindFullurl}`,
            smallUrl: `${res.rows[0].damBehindUrl ? res.rows[0].damBehindUrl : res.rows[0].damBehindFullurl}`,
          },
          {
            name: `${res.rows[0].damCrest}`,
            url: `${res.rows[0].damCrestFullurl}`,
            smallUrl: `${res.rows[0].damCrestUrl?res.rows[0].damCrestUrl:res.rows[0].damCrestFullurl}`,
          },
          {
            name: `${res.rows[0].damFront}`,
            url: `${res.rows[0].damFrontFullurl}`,
            smallUrl: `${res.rows[0].damFrontUrl?res.rows[0].damFrontUrl:res.rows[0].damFrontFullurl}`,
          },
          {
            name: `${res.rows[0].damToe}`,
            url: `${res.rows[0].damToeFullurl}`,
            smallUrl: `${res.rows[0].damToeUrl?res.rows[0].damToeUrl:res.rows[0].damToeFullurl}`,
          },
          {
            name: `${res.rows[0].extphoto5}`,
            url: `${res.rows[0].extphoto5Fullurl}`,
            smallUrl: `${res.rows[0].extphoto5Url?res.rows[0].extphoto5Url:res.rows[0].extphoto5Fullurl}`,
          },
          {
            name: `${res.rows[0].extphoto6}`,
            url: `${res.rows[0].extphoto6Fullurl}`,
            smallUrl: `${res.rows[0].extphoto6Url?res.rows[0].extphoto6Url:res.rows[0].extphoto6Fullurl}`,
          },
          {
            name: `${res.rows[0].outletDrainageFacilities}`,
            url: `${res.rows[0].outletDrainageFacilitiesFullurl}`,
            smallUrl: `${res.rows[0].outletDrainageFacilitiesUrl?res.rows[0].outletDrainageFacilitiesUrl:res.rows[0].outletDrainageFacilitiesFullurl}`,
          },
          {
            name: `${res.rows[0].spillway}`,
            url: `${res.rows[0].spillwayFullurl}`,
            smallUrl: `${res.rows[0].spillwayUrl?res.rows[0].spillwayUrl:res.rows[0].spillwayFullurl}`,
          },
          {
            name: `${res.rows[0].spillwayInletSection}`,
            url: `${res.rows[0].spillwayInletSectionFullurl}`,
            smallUrl: `${res.rows[0].spillwayInletSectionUrl?res.rows[0].spillwayInletSectionUrl:res.rows[0].spillwayInletSectionFullurl}`,
          },
        ];
        options.map((v) => {
          // 缩略图
          if (v.smallUrl && v.smallUrl != "null") {
            if (v.smallUrl.indexOf("https://file.ygwjg.com/") != -1) {
              v.smallUrl = v.smallUrl + "?imageView2/2/w/300/h/200";
            } else if (v.smallUrl.indexOf("fileOss") !== -1) {
              v.smallUrl = "http://sk.zjwater.com/" + v.smallUrl;
            } else {
              v.smallUrl = "http://sk.zjwater.com/fileUpds/" + v.smallUrl;
            }
          }

          if (v.url.indexOf("fileOss") !== -1) {
            v.url = "http://sk.zjwater.com/" + v.url;
          } else if (v.url && v.url.indexOf("https://file.ygwjg.com/") == -1) {
            v.url = "http://sk.zjwater.com/fileUpds/" + v.url;
          }
        });
        this.photoList = options;
        this.loadingImg = false;
      } else {
        this.loadingImg = true;
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
      }
      this.loadingImg = false;
    },

    // 获取工程视频影像
    async getVideoDetailList(prcd) {
      this.videoIndex = 0;
      this.videoList = [];
      this.loadingVideo = true;
      let res = await reservoirVideoListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        let arr = [];
        if (res.rows.length) {
          res.rows.map((v) => {
            arr.push(v.videouri);
          });
          this.getVideoFileList(arr.join(","));
        } else {
          this.loadingVideo = false;
        }
      } else {
        this.loadingVideo = false;
      }
    },
    async getVideoFileList(ids) {
      let res = await getFile({
        // dataId: '9D06FE1A368848B6A7E097DC30F32FDE',
        dataId: ids,
      });
      let videoList = [];
      if (res.list.length) {
        res.list.map((v) => {
          let url = v.filurl;
          if (
            v.filurl &&
            v.filurl.indexOf("https://file.ygwjg.com/") == -1 &&
            v.filurl.indexOf("/fileUpds/") != -1
          ) {
            url = "https://sk.zjwater.com" + v.filurl;
          }
          videoList.push({
            name: v.filnm,
            url: url,
          });
        });
        this.videoList = videoList;
      }
      this.loadingVideo = false;
    },

    // 获取摄像头数据
    async getCameraList(prcd) {
      let res = await reservoirCameraListApi({
        prcd: prcd,
        fetchAll: true,
      });
      if (res.success) {
        this.cameraList = res.rows;
      }
    },
    lookCamera(popupData) {
      this.$parent.openLookCameraPopup(popupData.PRCD, popupData.NAME);
    },

    // 获取人员信息
    async getPersonList(prcd) {
      let res = await reservoirPersonListApi({
        prcd: prcd,
        year: new Date().format("yyyy"),
      });
      if (res.success) {
        this.personData = res.rows[0];
      }
    },

    //弹窗tabs点击事件
    clickTabs(index, length) {
      if (length) {
        this.popupTabsIndex = index;
      }
    },

    async showTyphoon() {
      let that = this;
      jquery.ajax({
        url: "http://typhoon.zjwater.gov.cn/Api/TyhoonActivity",
        type: "get",
        dataType: "jsonp",
        data: {},
        success: function(res) {
          if(res.length){
            that.hasTf = true;
            for (let i = 0; i < res.length; i++) {
              jquery.ajax({
                url: "http://typhoon.zjwater.gov.cn/Api/TyphoonInfo/" + res[0].tfid,
                type: "get",
                dataType: "jsonp",
                data: {},
                success: function(res) {
                  if (res.length) {
                      let mydata = {
                        name: res[0].name,
                        code: res[0].tfid,
                        points: []
                      };
                      // let nowDate = new Date('2021-07-25 13:00:00')
                      res[0].points.map((item,i) => {
                        item.lat = Number(item.lat);
                        item.lng = Number(item.lng);
                        item.index = i;
                        item.type = 'live';
                        mydata.points.push(item);
                        // let typhoonTime = new Date(item.time)
                        // if(typhoonTime < nowDate){
                        //   mydata.points.push(item);
                        // }
                      });
                      that.tfLng = res[0].points[res[0].points.length-1].lng;
                      that.tfLat = res[0].points[res[0].points.length-1].lat;
                      const typhoon = new newTyphoon(
                        that.map,
                        mydata,
                        that.typhoonsMap,
                        that
                      );
                      typhoon.init();
                    }
                },
              });
            }
          }
        }
      })
    },
    showTf() {
      let lng = this.tfLng
      let lat = this.tfLat
      // [120.4997109415048, 29.22000109695038],
      // let lng = 122.4997109415048;
      // let lat = 29.22000109695038;
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 5.5,
      });
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
  .screen_title {
    width: 100%;
    position: absolute;
    top: 0;
    text-align: center;
    z-index: 9;
    .screen_title_box {
      letter-spacing: 3px;
      margin: 0 auto;
      width: 1137px;
      height: 80px;
      background: url("../../../../assets/images/title_bg.png") no-repeat center;
      background-size: 100% 100%;
      color: #ffffff;
      font-size: 34px;
      font-weight: 600;
      box-sizing: border-box;
      padding-top: 20px;
      span {
        font-size: 14px;
        letter-spacing: normal;
        vertical-align: text-top;
      }
    }
    img {
      width: 43px;
      height: 38px;
      vertical-align: sub;
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
  .popup_map {
    background: #fff;
    padding: 16px;
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
      width: 730px;
      font-size: 14px;

      .dialog_title {
        background: #e7f3fb;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        padding-left: 16px;
        font-weight: bold;
        position: absolute;
        width: 100%;
        box-sizing: border-box;
        top: 0;
        left: 0;
        & > i {
          position: absolute;
          top: 10px;
          right: 10px;
          color: #999;
          font-size: 20px;
          cursor: pointer;
          z-index: 10;
        }
      }
      .proj_top {
        width: 100%;
        @include flexbox;
        @include flexJC(space-between);
        margin-top: 40px;
        .proj_left {
          width: 200px;
          .proj_title {
            height: 40px;
            line-height: 20px;
            box-sizing: border-box;
            padding-top: 5px;
            color: #003ec7;
            cursor: pointer;
            font-size: 18px;
            font-weight: 500;
            font-weight: bold;
          }
          .proj_detail {
            // @include flexbox;
            // @include flexJC(space-between);
            // @include flexflow(row wrap);
            .popup_item {
              height: 38px;
              line-height: 38px;
              @include flexbox;
              padding-left: 5px;
              p {
                color: #666;
              }
              span {
                color: #000;
              }
              &:nth-child(odd) {
                background: #f8f8f8;
              }
            }
          }
        }
        .proj_right {
          width: calc(100% - 210px);
          .carousel_box {
            height: 300px;
            .carousel_item {
              height: 100%;
              .swiper_img {
                background: #ccc;
                width: 100%;
                height: 100%;
                background-repeat: no-repeat;
                background-size: auto 100%;
                background-position: center;
                border-radius: 4px;
              }
              .swiper_video {
                width: 100%;
                height: 100%;
                border-radius: 4px;
              }
            }
          }
          .camera_box {
            height: 200px;
            width: 100%;
            img {
              width: 100%;
            }
          }
          .img_nodata {
            height: 300px;
            text-align: center;
            width: 100%;
            img {
              width: 35%;
              margin-top: 20px;
            }
            p {
              font-size: 16px;
              color: #999;
              padding-top: 15px;
            }
          }
          .img_loading {
            height: 300px;
            width: 100%;
            text-align: center;
            box-sizing: border-box;
            padding-top: 90px;
            p {
              font-size: 16px;
              color: #999;
            }
          }
          .img_box {
            position: relative;
            .preview_words {
              text-align: center;
              position: absolute;
              width: 100%;
              bottom: 10px;
              z-index: 9;
              color: #fff;
            }
          }
          .proj_tabs {
            width: 100%;
            @include flexbox;
            @include flexAC;
            @include flexJC(center);
            padding-bottom: 10px;
            & > div {
              // width: 80px;
              padding-right: 10px;

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
                margin-right: 8px;
                img {
                  width: 50%;
                  margin-top: 7px;
                }
              }
            }
            & > div.notClick {
              cursor: not-allowed;
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
        }
      }
      .proj_bottom {
        .person_list {
          @include flexbox;
          margin-top: 10px;
          .person_item {
            width: 120px;
            box-sizing: border-box;
            padding: 0 5px;
            background: #f8f8f8;
            border: 1px solid #d6d6d6;
            border-radius: 4px;
            margin-right: 10px;
            &:last-child {
              margin-right: 0;
            }
            .person_item_top {
              @include flexbox;
              padding-top: 6px;
              height: 33px;
              h3 {
                font-size: 14px;
                line-height: 18px;
              }
              h4 {
                font-size: 13px;
                line-height: 16px;
              }
              img {
                padding-left: 5px;
                width: 26px;
                height: 26px;
              }
            }
            p {
              color: #000;
              font-size: 14px;
              font-weight: 500;
              margin-top: 10px;
            }
            span {
              color: #666;
              display: block;
              font-size: 14px;
              padding: 4px 0;
            }
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
        display: flex;
        align-items: center;
        font-size: 16px;
        p {
          color: #666;
        }
        span {
          color: #000;
        }
        el-icon-bottom {
          color: red;
        }
        el-icon-top {
          color: green;
        }
      }
    }
  }
  
}
</style>
<style lang="scss">
.adcdOverlay{
  background: rgba(255,255,255,0.90);
  border-radius: 8px;
  width: 168px;
  overflow: hidden;
  .adcd_top{
    text-align: center;
    background: #3A8AEF;
    font-size: 20px;
    color: #FFFFFF;
    font-weight: bold;
    height: 40px;
    line-height: 40px;
  }
  .adcd_bottom{
    &>div{
      @include flexbox();
      @include flexAI(center);
      margin: 3px 0 3px 10px;
      &>i{
        width: 6px;
        height: 6px;
        background: #193CC5;
        margin-right: 10px;
      }
      &>p{
        font-size: 14px;
        color: #999999;
        line-height: 24px;
        &>span{
          font-size: 14px;
          color: #333333;
        }
      }
    }
  }
}
.adcd_name{
  text-align: center;
  font-size: 14px;
  font-weight: bold;
  margin-top: 10px;
}
</style>
