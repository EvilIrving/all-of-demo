<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-18 17:42:25
 * @Description: 
 * @FilePath: /lucheng_project/src/views/aMap/AMap.vue
-->
<template>
  <div class='map_wrap'>
    <div class="map" id="mainView"></div>
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
import {
  drawPoint
} from "@/utils/mapUtil";

export default {
  name: '',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      map: null,
      pointLayer: null,
      defaultCenter: {
        lnglat: [120.4997109415048, 29.22000109695038],
        zoom: 8.3,
      },
    };
  },
  mounted() {
    this.mapInit()
  },
  methods: {
    //初始化地图
    mapInit() {
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
              url: "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          //绿色水利地图
          new Tile({
            source: new XYZ({
              url: "http://223.4.72.162:16080/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          })
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
    //打点不聚合
    newMapPoints(coordinates) {
      this.removeAllLayer();
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/reservoir.png"), //图片路径
        title: "",
        scale: 1,
      };
      this.pointLayer = drawPoint(coordinates, styleObj, true, type);
      this.map.addLayer(this.pointLayer);
    },
  }
}
</script>

<style lang='scss' scoped>
  .map_wrap{
    background: #fdfdf1;
    position: relative;
    width: 100%;
    height: 100%;
    .map {
      width: 100%;
      height: 100%;
    }
  }
</style>