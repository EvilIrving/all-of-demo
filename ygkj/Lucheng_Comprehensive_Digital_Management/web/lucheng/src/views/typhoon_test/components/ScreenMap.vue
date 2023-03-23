<template>
  <div class="map-wrap">
    <div class="map" id="MainView"></div>
    <screen-footer :map-bind="map" />
    <!-- 点位悬浮框 -->
    <div v-show="popupShow" id="popup" class="ol-popup">
      <div class="ol-popup__wrapper">
        <div class="ol-popup__title">{{ featureInfo.name }}</div>
        <div class="ol-popup__content">
          <div class="ol-popup__content--props">
            <div
              :key="key"
              v-for="(prop, key) of featureInfo.props"
              class="props-item"
            >
              <span class="item-label">{{ key }}:</span>
              <span class="item-data">
                {{ prop }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-show="typhoonPoint" ref="typhoonPopup" id="typhoonPopup" class="ol-popup">
      <div id="popup-content" class="ol-popup__wrapper">
        <div class="typhoon-point_title">
          {{typhoonName}}
        </div>
        <div class="typhoon-point_content">
          <p v-for="(value, key) in typhoonPointData" :key="key" >
            <span>{{key}}</span>
            <span>{{value}}</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import XYZ from "ol/source/XYZ";
import TileLayer from "ol/layer/Tile";
import EsriJSON from "ol/format/EsriJSON";
import ImageLayer from "ol/layer/Image";
import ImageStatic from "ol/source/ImageStatic";
import { Map, View, Overlay } from "ol";
import { defaults as defaultControls } from "ol/control";
import { Fill, Stroke, Style, Circle } from "ol/style";
import { Vector as VectorSource } from "ol/source";
import { Vector as VectorLayer } from "ol/layer";
import { createXYZ } from "ol/tilegrid";
import { tile as tileStrategy } from "ol/loadingstrategy";

import { ScreenFooter } from "@/components";
import { drawPoint, addLayerClick, Typhoon,newTyphoon } from "@/utils/mapUtil.js";

export default {
  name: "ScreenMap",
  components: { ScreenFooter },
  props: {},
  data() {
    return {
      //地图
      map: null,
      //点位信息
      featureInfo: {},
      popupShow: false,
      isohyetLayer: null,
      typhoonLayers: [],
      typhoonNames: [],
      typhoonTimes: [],
      typhoonsMap:new Map(),
      typhoonPoint:false,//台风点位点击弹框显示标记
      pointOverlay:null,//台风点位点击弹框
      typhoonPointData:{},//台风点位详情
      typhoonName:"",
    };
  },
  created() {},
  mounted() {
    this.mapInit();
  },
  watch: {},
  methods: {
    /**
     * 初始化地图
     */
    mapInit() {
      //地图主图层
      let mainLayer = new TileLayer(
        {
          id: "zhejiangSLLayer",
          source: new XYZ({
            url:
              "https://sldtptgis.zjwater.com/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
            projection: "EPSG:4326",
          }),
        },
        { zoomOffset: 1 }
      );
      let SLMCLayer = new TileLayer({
        id: "zhejiangSLMCLayer",
        zIndex: 1,
        source: new XYZ({
          url:
            "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/wzmengc/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
      });
      //河道图层
      let riverLayer = new TileLayer({
        name: "wenzhouRiverLayer",
        source: new XYZ({
          url:
            "http://117.149.227.112:6080/arcgis/rest/services/waterSearch/RV_RVAA/MapServer/tile/{z}/{y}/{x}",
          projection: "EPSG:4326",
        }),
        visible: 0,
        preload: 1,
      });
      // view设置
      let viewSetting = new View({
        projection: "EPSG:4326",
        center: [120.527, 27.86],
        zoom: 9.5,
        maxZoom: 18,
        minZoom: 4,
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: "MainView",
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer, SLMCLayer],
        view: viewSetting,
      });
      // this.drawPoint();
      this.registeredEvent(); //注册事件
      this.addStationOverlay();

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
    /**
     * 设置摄像机位置
     */
    setView(lng, lat) {
      const view = this.map.getView();
      view.animate({ center: [lng, lat], duration: 200, zoom: 14 });
    },
    addStationOverlay() {
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector("#popup"),
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
      });
      this.map.addOverlay(this.overlay);
    },
    /**
     * 移除所有图层
     */
    removeAllLayer() {
      // 忽略的图层列表：按name属性
      const list = ["MainMaplayer", "wenzhouRiverLayer"];
      this.map.getLayers().forEach((element) => {
        if (list.includes(element.get("name"))) {
          this.map.removeLayer(element);
        }
      });
    },
    //加载等雨量面
    showIsohyetLayer(mapImage) {
      this.removeLayerByName("rainfall");
      this.isohyetLayer = new ImageLayer({
        name: "rainfall",
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
        opacity: 0.5,
      });
      this.map.addLayer(this.isohyetLayer);
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
    /**
     * 注册事件
     */
    registeredEvent() {
      this.map.on("singleclick", (event) => {
        
      });
      this.map.on("pointermove", (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let flag = false;
        this.map.getTargetElement().style.cursor = "auto";
        this.map.forEachFeatureAtPixel(pixel, (feature) => {
          let coodinate = event.coordinate;
          let properties = feature.getProperties();
          if (feature && properties && properties.data) {
            this.map.getTargetElement().style.cursor = "pointer";
            this.featureInfo = {
              name: properties.data.name,
              props: properties.data.props,
            };
            if (this.featureInfo) {
              this.overlay.setPosition(coodinate);
              // console.log(this.featureInfo.name);
              flag = true;
            }
          }
        });
        if (flag) {
          this.popupShow = true;
        } else {
          this.popupShow = false;
        }
      });
    },
    /**
     * 画点并将图层点位绑定事件
     * @param {Array} coordinate 点位列表
     * @param {Object} styleObj 样式对象
     * @param {String} layerName 点位图层名称，用于分辨
     */
    drawPoint(coordinates, styleObj, layerName = "pointLayer") {
      if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);

      let pointLayer = drawPoint(coordinates, styleObj);
      pointLayer.set("name", layerName);
      this.map.addLayer(pointLayer);
      let selectClick = addLayerClick(pointLayer);

      selectClick.on("select", (e) => {
        let features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          this.$emit("point-click", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    showTyphoon(data) {
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
    showTestTyphoon(data){
      const typhoon = new newTyphoon(this.map,data,this.typhoonsMap,this);
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
  },
};
</script>
<style lang='scss' scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  .map {
    width: 100%;
    height: 100%;
  }
  .ol-popup {
    position: absolute;
    bottom: 15px;
    left: -50%;
    transform: translateX(-50%);
    border-radius: 3px;
    width: 254px;
    color:#fff;
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
      .typhoon-point_title{
        text-align: center;
        font-size: 18px;
        color: #FFFFFF;
        letter-spacing: 0;
        line-height: 24px;
        font-weight: 600;
        padding-bottom: 10px;
        border-bottom: 1px solid rgba(255,255,255,0.4);
      }
      .typhoon-point_content{
        p{
          padding: 8px 0;
          >span{
            font-size: 18px;
            color: #33FFFF;
            font-weight: 600;
            padding-right: 8px;
            &:first-child{
              color: #fff;
            }
          }
        }
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
}
</style>
