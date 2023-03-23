<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-11 11:20:05
 * @Description: 
 * @FilePath: \rscp-big-screen\src\views\sgzgOtherScreen\basin\basinMap\BasinMap.vue
-->
<template>
  <div class="map_wrap">
    <div class="screen_title">
      <div class="screen_time_box">
        {{time}}
      </div>
      <div class="screen_title_box">
        {{title}}
      </div>
      <div class="screen_user_box">
        <img :src="$config.imgUrl + 'man.png'" alt="" />
        <span>{{user}}</span>
      </div>
      <img class="haveTF" v-if="hasTf" src="@/assets/images/haveTF.gif" alt="" @click="showTf" />
      <!-- <img class="noTF" v-else :src="$config.swImgUrl + 'noTF.png'" alt="" /> -->
    </div>
     <!-- <marquee :name='tfname' :detail='tfdetail' v-if="youTf"></marquee> -->
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate">
      <!-- 工程点击弹窗 -->
      <div class="proj_content">
        <div class="dialog_title">
          <!-- 水库信息 -->
          <span>{{ popupData.cityADNM ? popupData.cityADNM : "-" }}</span> -
          <span>{{ popupData.countryADNM ? popupData.countryADNM : "-" }}</span>
          <img :src="$config.swImgUrl + 'popup_line.png'" />
          <img :src="$config.swImgUrl + 'close_icon.png'" class="closeIcon" @click="closePopup()" />
          <!--          <i class="el-icon-close" @click="closePopup()"></i>-->
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
                <span>{{
                    popupData.waterLevel
                      ? popupData.waterLevel.toFixed(2) + "m"
                      : "- "
                  }} </span>
                <!-- 括号填写是否上升及数字 -->
                <div v-if="parseFloat(popupData.waterChange)">
                  <span v-html="'&nbsp'"></span>(
                  <span>{{
                    popupData.waterChange
                      ? popupData.waterChange.toFixed(2)
                      : popupData.waterChange
                  }}</span>
                  <span v-if="popupData.waterChange"><i class="el-icon-top" style="color: green;font-size:16px" v-if="
                        popupData.waterChange > 0 ||
                          popupData.waterChange == null
                      "></i>
                    <i class="el-icon-bottom" style="color: red;font-size:16px" v-else></i></span>)
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
                <span v-if="
                    popupData.status && popupData.status.slice(0, 3) == '三类坝'
                  ">{{ popupData.status.slice(0, 3) }}</span>
                <span v-else>{{
                  popupData.status ? popupData.status : "-"
                }}</span>
              </div>
              <div class="popup_item" v-if="
                  popupData.status && popupData.status.slice(0, 3) == '三类坝'
                ">
                <p>除险加固：</p>
                <span>{{
                  popupData.status ? popupData.status.slice(3) : "-"
                }}</span>
              </div>
              <div class="popup_item" v-if="popupData.rightPopType && popupData.rightPopType == 'xsl'">
                <p>蓄水率：</p>
                <span>{{ popupData.xsl ? popupData.xsl : "-" }}</span>
              </div>
              <div class="popup_item" v-if="
                  popupData.rightPopType && popupData.rightPopType == 'kgkn'
                ">
                <p>可纳雨：</p>
                <span>{{
                  popupData.rainContains
                    ? popupData.rainContains.toFixed(2) + "mm"
                    : "0"
                }}</span>
              </div>
              <div class="popup_item" v-if="
                  popupData.rightPopType && popupData.rightPopType == 'kgkn'
                ">
                <p>可纳洪：</p>
                <span>{{
                  popupData.knx ? popupData.knx + popupData.knxUnit : "0"
                }}</span>
              </div>

              <div class="popup_item" v-if="
                  popupData.rightPopType && popupData.rightPopType == 'kgkn'
                ">
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
              <div :class="popupTabsIndex == 0 ? 'active' : ''" @click="clickTabs(0, 1)">
                <img v-if="popupTabsIndex == 0" :src="$config.swImgUrl + 'photo_active.png'" alt="" />
                <img v-else :src="$config.swImgUrl + 'photo_active.png'" alt="" />
              </div>
              <div :class="[
                  popupTabsIndex == 1 ? 'active' : '',
                  videoList.length ? '' : 'notClick',
                ]" @click="clickTabs(1, videoList.length)">
                <img v-if="popupTabsIndex == 1" :src="$config.swImgUrl + 'image_active.png'" alt="" />
                <img v-else :src="$config.swImgUrl + 'image_0.png'" alt="" />
              </div>
              <div :class="[
                  popupTabsIndex == 2 ? 'active' : '',
                  cameraList.length ? '' : 'notClick',
                ]" @click="clickTabs(2, cameraList.length)">
                <img v-if="popupTabsIndex == 2" :src="$config.swImgUrl + 'vedio_active.png'" alt="" />
                <img v-else :src="$config.swImgUrl + 'vedio_0.png'" alt="" />
              </div>
            </div>
            <!-- 照片 -->
            <div v-show="popupTabsIndex == 0" style="cursor: pointer" v-loading="loadingImg" @click="lookPhotos(popupData)">
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
                <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
                <p>暂无图片</p>
              </div>
              <div v-if="loadingImg" class="img_loading">
                <p>图片加载中...</p>
              </div>
            </div>
            <!-- 影像 -->
            <div v-show="popupTabsIndex == 1" v-loading="loadingVideo">
              <div v-if="videoList.length" class="img_box">
                <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeVideoItem">
                  <el-carousel-item class="carousel_item" v-for="(item, index) in videoList" :key="index">
                    <video class="swiper_video" :src="item.url" controls></video>
                  </el-carousel-item>
                </el-carousel>
                <div class="preview_words">
                  <span>{{ videoIndex + 1 }}</span> / {{ videoList.length }}
                </div>
              </div>
              <div v-if="!loadingVideo && !videoList.length" class="img_nodata">
                <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
                <p>暂无视频影像</p>
              </div>
              <div v-if="loadingVideo" class="img_loading">
                <p>视频影像加载中...</p>
              </div>
            </div>
            <!-- 视频 -->
            <div v-show="popupTabsIndex == 2" style="cursor: pointer">
              <div class="camera_box" @click="lookCamera(popupData)">
                <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
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
                <img :src="$config.swImgUrl + 'person_icon.png'" alt="" />
              </div>
              <p>{{ personData.executiveGovName }}</p>
              <span>{{ personData.executiveGovPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>水行政主管部门负责人</h4>
                <img :src="$config.swImgUrl + 'person_icon.png'" alt="" />
              </div>
              <p>{{ personData.executiveWaterName }}</p>
              <span>{{ personData.executiveWaterPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>主管部门产权责任人</h4>
                <img :src="$config.swImgUrl + 'person_icon.png'" alt="" />
              </div>
              <p>{{ personData.executiveDepartName }}</p>
              <span>{{ personData.executiveDepartPhone }}</span>
            </div>
            <div class="person_item" v-if="personData.ifDepartment == '有'">
              <div class="person_item_top">
                <h4>管理单位责任人</h4>
                <img :src="$config.swImgUrl + 'person_icon.png'" alt="" />
              </div>
              <p>{{ personData.executiveManageName }}</p>
              <span>{{ personData.executiveManagePhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>技术负责人</h4>
                <img :src="$config.swImgUrl + 'person_icon.png'" alt="" />
              </div>
              <p>{{ personData.technicalName }}</p>
              <span>{{ personData.techinicalPhone }}</span>
            </div>
            <div class="person_item">
              <div class="person_item_top">
                <h4>巡查负责人</h4>
                <img :src="$config.swImgUrl + 'person_icon.png'" alt="" />
              </div>
              <p>{{ personData.patrolName }}</p>
              <span>{{ personData.patrolPhone }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="popup_map_hover" ref="hoverPopup" v-show="currentCoordinateHover">
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
import { Map, View } from "ol";
import XYZ from "ol/source/XYZ";
import { Image, Vector as VectorLayer } from "ol/layer";
import ImageArcGISRest from "ol/source/ImageArcGISRest";
import Tile from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import Overlay from "ol/Overlay";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import { DoubleClickZoom } from "ol/interaction";
import esri2geo from "esri2geo";
import Measure from "@/utils/measure";
import jquery from "@/plugins/jquery-1.11.3";
import { Vector as VectorSource } from "ol/source";
import { Style, Fill } from "ol/style";
import { getVectorContext } from "ol/render";

import GeoJSON from "ol/format/GeoJSON";
import zjsGeoJson from "@/assets/data/zjsGeoJson.json";
import cutZjsGeoJson from "@/assets/data/cutZjsGeoJson.json";
import Marquee from '../../../skzgScreen/components/Marquee.vue'
import { realTimeTyphoon} from '@/api/api_basin'


import { drawPoint, drawLines, newTyphoon, drawGeoJson } from "@/utils/mapUtil";

import {
  reservoirPhotoListApi,
  reservoirVideoListApi,
  reservoirCameraListApi,
  reservoirPersonListApi,
  getFile,
  getTyInfoApi,
  getTyListApi
} from "@/api/api_reservoir";

import { dikeLineApi } from "@/api/api_basin";
export default {
  name: "",
  props: {
    title: {
      type: String,
      default: "工程综合研判"
    },
    show: {
      type: Boolean,
      default: true
    }
  },
  components: {
    Marquee,
  },
  data() {
    return {
       tfname: '',
      tfdetail: '',
      youTf: false,
      time: "",
      timer: null,
      user: "--",
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
        lnglat: [120.6, 28.8],
        zoom: 7.8
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
      tfLat: ""
    };
  },
  computed: {
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    },
    basinListData() {
      return this.$store.state.basinListData;
    }
  },
  watch: {
    leftSelectBasin(val) {
      this.getBasinLineData(val);
    }
  },
  created() {
    this.time = new Date().format("yyyy年MM月dd日 hh:mm:ss");
    this.timer = setInterval(() => {
      this.time = new Date().format("yyyy年MM月dd日 hh:mm:ss");
    }, 1000);
    this.user = this.$localData("get", "userInfo").psnm;
  },
  mounted() {
    this.mapInit();
     realTimeTyphoon().then(res => {
      if (res.success && res.data.length !== 0) {
        this.youTf = true
        let { enname, lat, lng, movedirection, movespeed, name, power, pressure, strong, radius7, radius10, speed, tfid, time, timeformate } = res.data[0]
        this.tfname = `${tfid.substring(0, 3)}年第${tfid.substring(4, 5)}号${strong}${name}(${enname})`
        this.tfdetail = `${timeformate}，风速${speed}米/秒，移速${movespeed}公里/小时，东经${lng}°，北纬${lat}°，气压${pressure}百帕，近中心最大风力${power}级以上`
      } else {
        this.youTf = false
      }
    })
    setTimeout(() => {
      this.showTyphoon();
    }, 1000);
    // let userInfo = this.$localData("get", "userInfo");
    // this.getBasinLineData();
  },
  destroyed() {
    clearInterval(this.timer);
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
          duration: 250
        },
        positioning: "bottom-center",
        offset: [-16, -10]
      });

      // hover弹窗
      this.hoverOverlay = new Overlay({
        element: this.$refs.hoverPopup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
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
        target: "mainView",
        //地图控件
        // controls: defaultControls({ attribution: true, zoom: true }),
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          // new Tile({
          //   source: new XYZ({
          //     // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
          //     url:
          //       "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
          //     projection: "EPSG:4326",
          //     crossOrigin: "anonymous"
          //   }),
          //   preload: 1,
          //   visible: true
          // }),

          // //绿色水利地图
          // new Tile({
          //   source: new XYZ({
          //     url:
          //       "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
          //     projection: "EPSG:4326",
          //     crossOrigin: "anonymous"
          //   }),
          //   preload: 1,
          //   visible: true
          // }),

          // // 所有流域
          // new Image({
          //   source: new ImageArcGISRest({
          //     url:
          //       "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt/arcgis/rest/services/Base/liuyumian/MapServer"
          //   })
          // })

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
        overlays: [this.overlay, this.hoverOverlay], // 把弹窗加入地图
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0
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
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件

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

      // 鼠标移入
      this.map.on("pointermove", e => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
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
              popupDataHover.lat
            ]);
          });
        } else {
          this.closeHoverPopup();
          this.map.getTargetElement().style.cursor = "";
        }
      });
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

        // this.getAdcdPointsData(this.$localData("get", "userInfo").adnm);

        // //有了市范围线后，市县级用户改变视图
        // if (this.userAdcd.endsWith("0000000000")) {
        //   //省级用户
        // } else {
        //   // this.getAdcdPointsData(this.$localData("get", "userInfo").adnm);
        //   this.getLines(false);
        //   this.getLinesXj(true);
        // }
      }
    },

    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", evt => {
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
            let pointData = feature.values_.features[0].values_.data;
            // const coordinate = evt.coordinate; // 获取坐标
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            this.popupData = pointData;
            this.popupTabsIndex = 0;
            this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
            this.getVideoDetailList(pointData.PRCD);
            this.getCameraList(pointData.PRCD);
            this.getPersonList(pointData.PRCD);
            console.log(pointData.lng, pointData.LGTD, 777)
            this._setView1(Number(pointData.LGTD) + 1.034, Number(pointData.LTTD) + 0.013);
            if (pointData.rightType == "2") {
              this.getOneDikeLine(pointData.PRCD, pointData.project_scale);
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
              duration: 250
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
          this._setView1(Number(pointData.LGTD) + 1.134, Number(pointData.LTTD) + 0.013);


          if (pointData.rightType == "2") {
            this.getOneDikeLine(pointData.PRCD, pointData.project_scale);
          }
          setTimeout(() => {
            this._setView(
              Number(pointData.LGTD) + 0.004,
              Number(pointData.LTTD) + 0.013
            );
          }, 300);

          setTimeout(() => {
            this.overlay.setPosition([Number(pointData.LGTD) + 0.005, Number(pointData.LTTD) - 0.0027]);
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
        zoom: this.defaultCenter.zoom
      });
    },

    // 设置地图位置
    _setView1(lng, lat, noBig) {
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

      if (this.show == true) {
        this.$parent.showHideLayer()
      }
    },
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
        zoom: zoom
      });
    },

    // 生成多个气泡
    _drawOverlays(pointData) {
      let popupData = pointData;
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
          element: document.getElementById("overly" + i)
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
        scale: 1
      };
      type = type ? type : "reservoir";
      this.jhPointLayer = drawPoint(coordinates, styleObj, true, type);
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
    },

    // 获取流域线数据
    async getBasinLineData(type) {
      let basinName = "";
      this.map.removeLayer(this.basinLayer);
      if (type === "") {
        return false;
      } else {
        basinName = this.basinListData.find(item => item.basinCode == type)
          .basinName;
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
          // let arr = [];
          if (res.features && res.features.length > 0) {
            esri2geo(res, (err, data) => {
              this.basinLayer = drawGeoJson(data);
              this.map.addLayer(this.basinLayer);
            });
          }
          // for (let i = 0; i < res.features.length; i++) {
          //   arr.push(...res.features[i].geometry.paths[0])
          // }
        }
      });
    },

    // 画线
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
          switch (list[i].level) {
            case "1":
              list[i].lineColor = "#E02020";
              break;
            case "2":
              list[i].lineColor = "#EB7E65";
              break;
            case "3":
              list[i].lineColor = "#F7C739";
              break;
            case "4":
              list[i].lineColor = "#193CC5";
              break;
            case "5":
              list[i].lineColor = "#1BC57E";
              break;
            default:
              list[i].lineColor = "#C2C2C2";
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
    async getOneDikeLine(prcd, level) {
      let res = await dikeLineApi({
        prcd: prcd
      });
      let lineArr = [];
      if (res.success) {
        lineArr.push({
          line: res.rows[0].coorpot
            ? JSON.parse(
              res.rows[0].coorpot.replace(new RegExp("&quot;", "gm"), '"')
            )[0]
            : "",
          level: level
        });
        this._drawLines(lineArr);
      }
    },

    //获取工程图片
    async getPhotoDetailList(prcd) {
      this.imgIndex = 0;
      this.photoList = [];
      this.loadingImg = true;
      let res = await reservoirPhotoListApi({
        viewType: "latestAll",
        prcd: prcd,
        fetchAll: true
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
            smallUrl: `${res.rows[0].damBehindUrl}`
          },
          {
            name: `${res.rows[0].damCrest}`,
            url: `${res.rows[0].damCrestFullurl}`,
            smallUrl: `${res.rows[0].damCrestUrl}`
          },
          {
            name: `${res.rows[0].damFront}`,
            url: `${res.rows[0].damFrontFullurl}`,
            smallUrl: `${res.rows[0].damFrontUrl}`
          },
          {
            name: `${res.rows[0].damToe}`,
            url: `${res.rows[0].damToeFullurl}`,
            smallUrl: `${res.rows[0].damToeUrl}`
          },
          {
            name: `${res.rows[0].extphoto5}`,
            url: `${res.rows[0].extphoto5Fullurl}`,
            smallUrl: `${res.rows[0].extphoto5Url}`
          },
          {
            name: `${res.rows[0].extphoto6}`,
            url: `${res.rows[0].extphoto6Fullurl}`,
            smallUrl: `${res.rows[0].extphoto6Url}`
          },
          {
            name: `${res.rows[0].outletDrainageFacilities}`,
            url: `${res.rows[0].outletDrainageFacilitiesFullurl}`,
            smallUrl: `${res.rows[0].outletDrainageFacilitiesUrl}`
          },
          {
            name: `${res.rows[0].spillway}`,
            url: `${res.rows[0].spillwayFullurl}`,
            smallUrl: `${res.rows[0].spillwayUrl}`
          },
          {
            name: `${res.rows[0].spillwayInletSection}`,
            url: `${res.rows[0].spillwayInletSectionFullurl}`,
            smallUrl: `${res.rows[0].spillwayInletSectionUrl}`
          }
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
    async getFileList(ids) {
      let res = await getFile({
        dataId: ids
      });
      let photoList = [];
      if (res.list.length) {
        res.list.map(v => {
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
            smallUrl: smallUrl
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
        fetchAll: true
      });
      if (res.success) {
        let arr = [];
        if (res.rows.length) {
          res.rows.map(v => {
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
        dataId: ids
      });
      let videoList = [];
      if (res.list.length) {
        res.list.map(v => {
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
            url: url
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
        fetchAll: true
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
        year: new Date().format("yyyy")
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
      let tyres = await getTyListApi();
      if (tyres.success) {
        if (tyres.rows && tyres.rows.length > 0) {
          this.hasTf = true;
          tyres.rows.map(async item => {
            item.code = item.tfid;
            let res = await getTyInfoApi(item.tfid);
            if (res) {
              let mydata = item;
              mydata.points = [];
              res.rows.map(item => {
                item.lat = Number(item.lat);
                item.lng = Number(item.lng);
                mydata.points.unshift(item);
              });
              this.tfLng = res.rows[0].lng;
              this.tfLat = res.rows[0].lat;
              const typhoon = new newTyphoon(
                this.map,
                mydata,
                this.typhoonsMap,
                this
              );
              typhoon.init();
            }
          });
        } else {
          this.hasTf = false;
        }
      }
    },

    showTf() {
      // let lng = this.tfLng
      // let lat = this.tfLat
      // [120.4997109415048, 29.22000109695038],
      let lng = 122.4997109415048;
      let lat = 29.22000109695038;
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: 6
      });
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../../style/common.scss";
.map_wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background: url($imageUrl + "skzg_bg.png") no-repeat center;
  .map {
    @include map();
  }
  .screen_title {
    @include header();
    .screen_time_box {
      @include time();
    }
    .screen_title_box {
      @include title();
    }
    .screen_user_box {
      @include user();
    }
    img {
      width: 38px;
      height: 43px;
      vertical-align: sub;
    }
    .haveTF {
      @include TF();
    }
    .noTF {
      position: absolute;
      left: 500px;
      top: 153px;
      width: 122px;
      height: 147px;
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
    /* background: #fff;
    padding: 16px;*/
    //@include shadow(#ccc, 0px, 0px, 2px);
    // border-radius: 4px;
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
      width: 882px;
      height: 599px;
      font-size: 14px;
      background: url($swImgUrl + "popup_bgi.png") no-repeat;
      background-size: 882px 599px;
      padding-top: 32px;

      .dialog_title {
        //width: 144px;
        height: 31px;
        font-size: 24px;
        font-family: YouShe;
        color: #ffffff;
        line-height: 31px;
        margin-left: 55px;

        img {
          width: 802px;
          height: 19px;
          position: absolute;
          top: 50px;
          left: 40px;
        }
        .closeIcon {
          width: 15px;
          height: 15px;
          position: absolute;
          top: 43px;
          left: 816px;
          cursor: pointer;
        }
        /*// background: #e7f3fb;
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
        }*/
      }
      .proj_top {
        width: 100%;
        @include flexbox;
        @include flexJC(space-between);
        margin-top: 20px;
        .proj_left {
          width: 262px;
          .proj_title {
            height: 24px;
            font-size: 20px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #69e3ee;
            line-height: 24px;
            margin-top: 16px;
            margin-left: 40px;
          }
          .proj_detail {
            margin-top: 16px;
            margin-left: 40px;
            .popup_item {
              @include flexbox;
              padding-left: 5px;
              height: 40px;
              font-size: 14px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #ffffff;
              line-height: 40px;
              &:nth-child(odd) {
                height: 40px;
                background: #03338d;
                border: 1px solid #0051b5;
              }
            }
          }
        }
        .proj_right {
          width: calc(100% - 310px);
          .carousel_box {
            height: 300px;
            .carousel_item {
              height: 100%;
              .swiper_img {
                // background: #ccc;
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
            ::v-deep .el-carousel__arrow--right:hover {
              background: #46fdff;
            }
            ::v-deep .el-carousel__arrow--left:hover {
              background: #46fdff;
            }
          }
          .proj_tabs {
            width: 100%;
            @include flexbox;
            @include flexAC;
            @include flexJC(center);
            padding-bottom: 10px;
            cursor: pointer;
            img {
              width: 80px;
              height: 32px;
              margin-right: 16px;
            }
          }
        }
      }
      .proj_bottom {
        .person_list {
          @include flexbox;
          margin-top: 40px;
          margin-left: 40px;
          .person_item {
            width: 127px;
            height: 96px;
            background: #03338d;
            border-radius: 4px;
            border: 1px solid #0051b5;
            margin-right: 8px;
            padding-left: 8px;
            img {
              width: 32px;
              height: 32px;
            }
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
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: rgba(255, 255, 255, 0.6);
                line-height: 18px;
              }
              img {
                padding-left: 5px;
                width: 26px;
                height: 26px;
              }
            }
            p {
              //height: 24px;
              margin-top: 10px;
              font-size: 16px;
              font-family: PingFangSC-Medium, PingFang SC;
              font-weight: 500;
              color: #ffffff;
              line-height: 24px;
            }
            span {
              height: 16px;
              font-size: 14px;
              font-family: ArialMT;
              color: rgba(255, 255, 255, 0.6);
              line-height: 16px;
              letter-spacing: 1px;
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
