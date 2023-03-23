<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-11 11:19:46
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\outsideScreen\components\BasinMap.vue
-->
<template>
  <div class="map_wrap">
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate">
      <!-- 工程点击弹窗 -->
      <div class="proj_content">
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
              <span v-if="popupData.project_scale == '1' && popupData.rightType != '2'" v-html="'&nbsp[大(1)型]'"></span>
              <span v-if="popupData.project_scale == '2' && popupData.rightType != '2'" v-html="'&nbsp[大(2)型]'"></span>
              <span v-if="popupData.project_scale == '3' && popupData.rightType != '2'" v-html="'&nbsp[中型]'"></span>
              <span v-if="popupData.project_scale == '4' && popupData.rightType != '2'" v-html="'&nbsp [小(1)型]'"></span>
              <span v-if="popupData.project_scale == '5' && popupData.rightType != '2'" v-html="'&nbsp [小(2)型]'"></span>
              <span v-if="popupData.project_scale == '1' && popupData.rightType == '2'" v-html="'&nbsp[1级]'"></span>
              <span v-if="popupData.project_scale == '2' && popupData.rightType == '2'" v-html="'&nbsp[2级]'"></span>
              <span v-if="popupData.project_scale == '3' && popupData.rightType == '2'" v-html="'&nbsp[3级]'"></span>
              <span v-if="popupData.project_scale == '4' && popupData.rightType == '2'" v-html="'&nbsp [4级]'"></span>
              <span v-if="popupData.project_scale == '5' && popupData.rightType == '2'" v-html="'&nbsp [5级]'"></span>
            </div>
            <!-- 水库 -->
            <div class="proj_detail" v-if="popupData.rightType == '1'">
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
            </div>
            <!-- 堤防 -->
            <div class="proj_detail" v-if="popupData.rightType == '2'">
              <div class="popup_item">
                <p>所在市：</p>
                <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>所在县：</p>
                <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>管理层级：</p>
                <span v-if="popupData.supervise_level == 1">省本级</span>
                <span v-if="popupData.supervise_level == 2">市本级</span>
                <span v-if="popupData.supervise_level == 3">县本级</span>
                <span v-if="popupData.supervise_level == 4">乡镇街道</span>
              </div>
              <div class="popup_item">
                <p>岸别：</p>
                <span>{{ popupData.riverBank ? popupData.riverBank : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>堤防长(m)：</p>
                <span>{{ popupData.dikeLength ? popupData.dikeLength : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>标绘长(m)：</p>
                <span>{{ popupData.drawLength ? popupData.drawLength : "-" }}</span>
              </div>
            </div>
            <!-- 水闸 -->
            <div class="proj_detail" v-if="popupData.rightType == '3'">
              <div class="popup_item">
                <p>所在市：</p>
                <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>所在县：</p>
                <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>管理层级：</p>
                <span v-if="popupData.supervise_level == 1">省本级</span>
                <span v-if="popupData.supervise_level == 2">市本级</span>
                <span v-if="popupData.supervise_level == 3">县本级</span>
                <span v-if="popupData.supervise_level == 4">乡镇街道</span>
              </div>
              <div class="popup_item">
                <p>所在河流：</p>
                <span>{{ popupData.riverName ? popupData.riverName : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>最大过闸流量(m³/s)：</p>
                <span>{{ popupData.maxFlow ? popupData.maxFlow : "-" }}</span>
              </div>
            </div>
            <!-- 泵站 -->
            <div class="proj_detail" v-if="popupData.rightType == '4'">
              <div class="popup_item">
                <p>所在市：</p>
                <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>所在县：</p>
                <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>管理层级：</p>
                <span v-if="popupData.supervise_level == 1">省本级</span>
                <span v-if="popupData.supervise_level == 2">市本级</span>
                <span v-if="popupData.supervise_level == 3">县本级</span>
                <span v-if="popupData.supervise_level == 4">乡镇街道</span>
              </div>
              <div class="popup_item">
                <p>所在河流：</p>
                <span>{{ popupData.riverName ? popupData.riverName : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>装机流量(m³/s)：</p>
                <span>{{ popupData.installedFlow ? popupData.installedFlow : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>装机功率(KW)：</p>
                <span>{{ popupData.motorPower ? popupData.motorPower : "-" }}</span>
              </div>
            </div>
            <!-- 闸站 -->
            <div class="proj_detail" v-if="popupData.rightType == '5'">
              <div class="popup_item">
                <p>所在市：</p>
                <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>所在县：</p>
                <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>管理层级：</p>
                <span v-if="popupData.supervise_level == 1">省本级</span>
                <span v-if="popupData.supervise_level == 2">市本级</span>
                <span v-if="popupData.supervise_level == 3">县本级</span>
                <span v-if="popupData.supervise_level == 4">乡镇街道</span>
              </div>
              <div class="popup_item">
                <p>过闸流量(m³/s)：</p>
                <span>{{ popupData.maxFlow ? popupData.maxFlow : "-" }}</span>
              </div>
              <div class="popup_item">
                <p>装机功率(KW)：</p>
                <span>{{ popupData.motorPower ? popupData.motorPower : "-" }}</span>
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
                    src="@/assets/images/image0.png"
                    alt=""
                  />
                  <img
                    v-else
                    src="@/assets/images/image1.png"
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
                    src="@/assets/images/yx0.png"
                    alt=""
                  />
                  <img v-else src="@/assets/images/yx1.png" alt="" />
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
                    src="@/assets/images/video0.png"
                    alt=""
                  />
                  <img
                    v-else
                    src="@/assets/images/video1.png"
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
                <img src="@/assets/images/noData.png" alt="" />
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
                <img src="@/assets/images/noData.png" alt="" />
                <p>暂无视频影像</p>
              </div>
              <div v-if="loadingVideo" class="img_loading">
                <p>视频影像加载中...</p>
              </div>
            </div>
            <!-- 视频 -->
            <div v-show="popupTabsIndex == 2" style="cursor: pointer">
              <div class="camera_box" @click="lookCamera(popupData)">
                <img src="@/assets/images/sk-camera.png" alt="" />
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
                <img src="@/assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveGovName }}</p>
              <span>{{ personData.executiveGovPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>水行政主管部门负责人</h4>
                <img src="@/assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveWaterName }}</p>
              <span>{{ personData.executiveWaterPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>主管部门产权责任人</h4>
                <img src="@/assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveDepartName }}</p>
              <span>{{ personData.executiveDepartPhone }}</span>
            </div>
            <div class="person_item" v-if="personData.ifDepartment == '有'">
              <div class="person_item_top">
                <h4>管理单位责任人</h4>
                <img src="@/assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.executiveManageName }}</p>
              <span>{{ personData.executiveManagePhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>技术负责人</h4>
                <img src="@/assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.technicalName }}</p>
              <span>{{ personData.techinicalPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>巡查负责人</h4>
                <img src="@/assets/images/person.png" alt="" />
              </div>
              <p>{{ personData.patrolName }}</p>
              <span>{{ personData.patrolPhone }}</span>
            </div>
          </div>
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
import { Image } from "ol/layer";
import ImageArcGISRest from "ol/source/ImageArcGISRest";
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
import {LinearRing, Polygon} from 'ol/geom';
import { fromExtent } from "ol/geom/Polygon";
import GeoJSON from "ol/format/GeoJSON";
import esri2geo from "esri2geo";
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
  drawGeoJson
} from "@/utils/mapUtil";

import {
  reservoirPhotoListApi,
  reservoirVideoListApi,
  reservoirCameraListApi,
  reservoirPersonListApi,
  getFile,
  getTyInfoApi,
  getTyListApi,
} from "@/api/api_reservoir";

import { dikeLineApi } from "@/api/api_basin";
import zjsGeoJson from '@/assets/data/zjsGeoJson.json'

export default {
  name: "",
  props: {
    title: {
      type: String,
      default: '工程综合研判'
    }
  },
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
      basinLayer: {},
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
        zoom: 8,
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

      userAdnm: '',
      userAdcdShi: '',
      adcdMaskLayer: null,
      mapInitViewData: {
        '浙江省': {
          adcdShi: '330000',
          lnglat: [120.2841796875, 28.9],
          zoom: 7.9
        },
        '杭州市': {
          adcdShi: '330100',
          lnglat: [119.6, 29.95238969771562],
          zoom: 9
        },
        '嘉兴市': {
          adcdShi: '330400',
          lnglat: [120.82295965448235, 30.62365529556139],
          zoom: 10
        },
        '舟山市': {
          adcdShi: '330900',
          lnglat: [122.31747879977257, 30.15719831907083],
          zoom: 9.5
        },
        '绍兴市': {
          adcdShi: '330600',
          lnglat: [120.65203143078425, 29.779204859669218],
          zoom: 9.5
        },
        '湖州市': {
          adcdShi: '330500',
          lnglat: [119.86013581555973, 30.774957488035955],
          zoom: 10
        },
        '丽水市': {
          adcdShi: '331100',
          lnglat: [119.52993030133263, 28.058645185047814],
          zoom: 9.3
        },
        '台州市': {
          adcdShi: '331000',
          lnglat: [121.1019486426036, 28.620996798490223],
          zoom: 9.5
        },
        '温州市': {
          adcdShi: '330300',
          lnglat: [120.6, 27.850262573033703],
          zoom: 9.5
        },
        '衢州市': {
          adcdShi: '330800',
          lnglat: [118.6963592735759, 28.903052563843694],
          zoom: 9.6
        },
        '金华市': {
          adcdShi: '330700',
          lnglat: [119.96357972413958, 29.14920448445481],
          zoom: 9.5
        },
        '宁波市': {
          adcdShi: '330200',
          lnglat: [121.55592775905025, 29.707711898447568],
          zoom: 9.5
        }
      }
    };
  },
  computed: {
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    },
    basinListData(){
      return this.$store.state.basinListData;
    }
  },
  watch: {
    leftSelectBasin(val) {
      this.getBasinLineData(val);
    },
  },
  created() {},
  mounted() {
    // 为不同角色设置地图视图
    this.userAdnm = this.$localData("get", "userInfo").adnm
    this.userAdcdShi = this.$localData("get", "userInfo").adcd.slice(0,5) + '0'
    for (let key in this.mapInitViewData) {
      if (this.mapInitViewData[key].adcdShi == this.userAdcdShi) {
        this.defaultCenter = this.mapInitViewData[key]
        break;
      } else {
        this.defaultCenter = this.mapInitViewData['浙江省']
      }
    }
    this.mapInit();
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
              // 影像图
              url: "https://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              // url:
              //   "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          // 黑色风格蒙层+行政区划网格
          new Tile({
            source: new XYZ({
              url:"https://webgis.ygwjg.com/arcgis/rest/services/zhejiangsheng/zhejiangshengmc/MapServer/tile/{z}/{y}/{x}",
              projection: "EPSG:4326", //使用这个坐标系
              // crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),
          
          // 所有流域
          new Image({
					  source: new ImageArcGISRest({
              url: 'https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/Base/liuyumian/MapServer',
            }),
				  }),
        ],
        overlays: [this.overlay, this.hoverOverlay], // 把弹窗加入地图
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 8,
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
      
      // 初始化完成后：获取当前用户行政区划，并高亮显示地图
      for (let i = 0; i < zjsGeoJson.features.length; i++) {
        const item = zjsGeoJson.features[i];
        if(this.userAdcdShi == item.properties['市代码']){ //匹配对应市级
          let adnmFeature = new GeoJSON().readFeatures(item)[0]
          this.adcdMaskLayer = this.drawMask(adnmFeature)
          this.map.addLayer(this.adcdMaskLayer)
        }
      }
    },
    setCityView(cityName) {
      const view = this.map.getView();
      view.animate({
        center: this.mapInitViewData[cityName].center,
        duration: 0,
        zoom: this.mapInitViewData[cityName].zoom,
      });
    },
    // 单个市高亮 其他区域添加蒙层
    drawMask(selectOneFeature){
      // 创建矢量图层
      const maskVectorSource = new VectorSource({
          features: [],
      });
      const maskVectorLayer = new VectorLayer({
        source: maskVectorSource,
        style: function(){
          let style = new Style({
            stroke:new Stroke({
              color: "#2BEF89",
              width: 1
            }),
            fill: new Fill({
              color: "rgba(0,9,32,.3)",
            }),
          })
          return style;
        },
        zIndex: 2
      });
      maskVectorSource.clear(true)
      // if(maskVectorLayer) map.removeLayer(maskVectorLayer)

      //生成外环
      let outerGeomotryExtent = [-180,-90,180,90];
      let polygonRing = fromExtent(outerGeomotryExtent);

      //生成环状几何遮罩范围
      let selectFeature = selectOneFeature;
      let insideGeometry = selectFeature.getGeometry();
      if (!insideGeometry instanceof Polygon) {
          return false;
      } 
      let insideCoords = insideGeometry.getCoordinates();
      insideCoords.forEach(el =>{ 
          let linearRing = new LinearRing(el[0]);
          polygonRing.appendLinearRing(linearRing);
      })

      //添加圆环要素，添加到数据源
      let ringFeature = new Feature({
          geometry: polygonRing
      })
      maskVectorSource.addFeature(ringFeature);

      return maskVectorLayer;
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
            this.popupTabsIndex = 0;
            this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
            this.getVideoDetailList(pointData.PRCD);
            this.getCameraList(pointData.PRCD);
            this.getPersonList(pointData.PRCD);
            this._setView1(pointData.LGTD, pointData.LTTD);
            if(pointData.rightType == '2'){
              this.getOneDikeLine(pointData.PRCD, pointData.project_scale)
            }
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
          const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
          this.currentCoordinate = hdms; // 保存坐标点
          this.popupTabsIndex = 0;
          this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
          this.getVideoDetailList(pointData.PRCD);
          this.getCameraList(pointData.PRCD);
          this.getPersonList(pointData.PRCD);

          this._setView1(pointData.LGTD, pointData.LTTD);

          if(pointData.rightType == '2'){
            this.getOneDikeLine(pointData.PRCD, pointData.project_scale)
          }
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
      // this.map.removeLayer(this.basinLayer);
    },

    removePolygon() {
      this.map.removeLayer(this.basinLayer);
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
      this.popupTabsIndex = 0;
      this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
      this.getVideoDetailList(pointData.PRCD);
      this.getCameraList(pointData.PRCD);
      this.getPersonList(pointData.PRCD);
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
          `<div id = 'overly"+i+"' class='overlay'>"
            <div class="popup_content">
              <div class="popup_title">${popupData[i].coastalName} </div>
              <div class="popup_item">
                <p>预报潮位：</p>
                <span>${
                  popupData[i].forecast ? popupData[i].forecast : "-"
                }</span>
              </div>
              <div class="popup_item">
                <p>时间：</p>
                <span>${
                  popupData[i].forecastTime ? popupData[i].forecastTime : "-"
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
    },

    //打点不聚合
    newMapPoints(coordinates, type) {
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
      type = type ? type : 'reservoir'
      this.jhPointLayer = drawPoint(coordinates, styleObj, true, type);
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
    },

    // 获取流域线数据
    async getBasinLineData(type) {
      let basinName = ''
      this.map.removeLayer(this.basinLayer);
      if(type === ''){
        return false;
      }else{
        basinName =  this.basinListData.find(item=>item.basinCode == type).basinName
      }
      let url = "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/Base/liuyumian/MapServer/0/query";
      jquery.ajax({
        url: url,
        type: "get",
        dataType: "jsonp",
        data: {
          returnGeometry: true,
          where: "ennm='" + basinName + "'",
          outSR: "4326",
          outFields: "*",
          f: "json",
        },
        success: res => {
          let arr = [];
          if (res.features && res.features.length > 0) {
            esri2geo(res, (err, data) => {
              this.basinLayer = drawGeoJson(data);
              this.map.addLayer(this.basinLayer);
            })
          }
          // for (let i = 0; i < res.features.length; i++) {
          //   arr.push(...res.features[i].geometry.paths[0])
          // }
          
        },
      });
    },

    // 画线
    _drawLines(list){
      this.map.removeLayer(this.linesLayer);
      for (let i = 0; i < list.length; i++) {
        list[i].coordinates = []
        if(list[i].line && list[i].line.length){
          for(let j = 0; j < list[i].line.length; j++){
            list[i].coordinates.push([list[i].line[j].lng, list[i].line[j].lat])
          }
          switch(list[i].level){
            case '1':
              list[i].lineColor = '#E02020'
              break;
            case '2':
              list[i].lineColor = '#EB7E65'
              break;
            case '3':
              list[i].lineColor = '#F7C739'
              break;
            case '4':
              list[i].lineColor = '#193CC5'
              break;
            case '5':
              list[i].lineColor = '#1BC57E'
              break;
            default:
              list[i].lineColor = '#C2C2C2'
              break;
          }
        }
      }
      
      this.linesLayer = drawLines(list);
      this.map.addLayer(this.linesLayer);
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

    //获取范围线
    async getOneDikeLine(prcd, level){
      let res = await dikeLineApi({
        prcd: prcd
      })
      let lineArr = []
      if(res.success){
        lineArr.push({
          line: res.rows[0].coorpot ? JSON.parse(res.rows[0].coorpot.replace(new RegExp('&quot;',"gm"),'"'))[0] : '',
          level: level
        })
        this._drawLines(lineArr)
      }
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
        // this.photoList = options;
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
      background: url("../../../assets/images/title_bg.png") no-repeat center;
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
<style lang="scss"></style>
