<!--
 * @Author: hanyu
 * @LastEditTime: 2022-03-28 11:35:08
 * @Description: 
 * @FilePath: /rscp-big-screen/src/views/outsideScreen/seawallBlack/seawallMap/SeawallMap.vue
-->
<template>
  <div class="map_wrap">
    <!-- <div class="screen_title">
      <div class="screen_title_box">
        <img src="../../../../assets/images/title_icon.png" alt="">
        海塘防潮研判
      </div>
    </div> -->
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate">
      <!-- 安澜建设 -->
      <div class="popup_content" v-show="popupData.rightType == '2'">
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="lookDetail(popupData)"
        >
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
      <div class="proj_content" v-show="popupData.rightType == '4'">
        <div class="proj_tabs">
          <div
            :class="popupShowImg ? 'active' : ''"
            @click="popupShowImg = true"
          >
            <div>
              <img
                v-if="popupShowImg"
                src="../../../../assets/images/image0.png"
                alt=""
              />
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
        <div
          v-if="popupShowImg"
          style="cursor:pointer"
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
        <div v-else>video</div>
        <div class="proj_title" @click="lookDetail(popupData)">
          {{ popupData.NAME }}
        </div>
        <div class="proj_detail">
          <div class="popup_item">
            <p>时间：</p>
            <span>{{ popupData.tm ? popupData.tm.slice(5, 16) : "-" }}</span>
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
        </div>
      </div>

      <!-- 摄像头 -->
      <div class="popup_content" v-show="popupData.rightType == '5'">
        <div class="popup_title">
          {{ popupData.projectName }}
          <span
            v-if="popupData.state == 'ON'"
            style="color: #003ec7; cursor: pointer; float: right"
            @click="lookVideo(popupData)"
            >查看</span
          >
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
import {LinearRing, Polygon} from 'ol/geom';
import { fromExtent } from "ol/geom/Polygon";
import GeoJSON from "ol/format/GeoJSON";

import Measure from "@/utils/measure";
import jquery from "@/plugins/jquery-1.11.3";

import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  addLayerClick,
} from "@/utils/mapUtil";

import { seawallPhotoListApi, getFile } from "@/api/api_seawall";
import zjsGeoJson from '@/assets/data/zjsGeoJson.json'

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
      map: null,
      pjObj: {},
      pjObjList: [],
      Overlay: null,
      currentCoordinate: null,
      popupData: {},
      // 原先默认
      // defaultCenter: {
      //   lnglat: [120.7997109415048, 29.22000109695038],
      //   zoom: 8.3,
      // },
      // 钱塘默认中心点 和层级
      defaultCenter: {
        lnglat: [120.09841796875, 29.169012539062494],
        zoom: 8,
      },
      markersLayer: {},
      pointBulingbuling: null,

      //轮播图弹窗数据
      photoList: [],
      popupShowImg: true,
      imgIndex: 0,
      loadingImg: true,

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
    //获取工程图片
    // async getProjPhoto(){

    // },
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
        ],
        overlays: [this.overlay], // 把弹窗加入地图
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
            if (pointData.rightType == "4") {
              this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
            }
            setTimeout(() => {
              this.overlay.setPosition([pointData.lng, pointData.lat]);
            });
          } else {
            //点击聚合图标时
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
          if (pointData.rightType == "4") {
            this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
          }
          setTimeout(() => {
            this.overlay.setPosition([pointData.lng, pointData.lat]);
          }, 500);
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
      if (pointData.rightType == "4") {
        this.getPhotoDetailList(pointData.PRCD, pointData.NAME);
      }
      const hdms = toStringHDMS(toLonLat([pointData.LGTD, pointData.LTTD])); // 转换坐标格式
      this.currentCoordinate = hdms; // 保存坐标点
      this.overlay.setPosition([pointData.LGTD, pointData.LTTD]);
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
      if (type == "cwyj") {
        scale = 0.6;
      }
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/seawall-select.png"), //图片路径
        title: "",
        scale: scale,
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
              lineObj.lineColor = "#E02020";
              break;
            case "2":
              lineObj.lineColor = "#EB7E65";
              break;
            case "3":
              lineObj.lineColor = "#F7C739";
              break;
            case "4":
              lineObj.lineColor = "#193CC5";
              break;
            case "5":
              lineObj.lineColor = "#1BC57E";
              break;
            default:
              lineObj.lineColor = "#C2C2C2";
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
    newMapPoints(coordinates, type) {
      let scale = 0.8;
      if (type == "cwyj") {
        scale = 0.6;
      }
      this.removeAllLayer();
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/map/seawall-select.png"), //图片路径
        title: "",
        scale: scale,
      };
      this.jhPointLayer = drawPoint(coordinates, styleObj, true);
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
    },
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
            let src = require("@/assets/images/map/seawall-jh.png");
            if (type == "video") {
              src = require("@/assets/images/map/video_jh.png");
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
                offsetX: -12,
                offsetY: 11,
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
            let src = require("@/assets/images/map/seawall.png");
            if (type == "video") {
              if (featureData.state == "ON") {
                src = require("@/assets/images/map/video_on.png");
              } else {
                src = require("@/assets/images/map/video_off.png");
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
                scale: 0.8,
              }),
            });
            return style;
          }
        },
      });
      this.jhPointLayer.setZIndex(99);
      this.map.addLayer(this.jhPointLayer);
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
      this.$parent.openDetailPopup(row.PRCD, row.NAME);
    },

    // 轮播图弹窗方法
    changeItem(index) {
      this.imgIndex = index;
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
    }
    img {
      width: 43px;
      height: 38px;
      vertical-align: sub;
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
        color: #003ec7;
        cursor: pointer;
        font-size: 18px;
        font-weight: 500;
        padding-bottom: 4px;
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
</style>
<style lang="scss"></style>
